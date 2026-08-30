import SwiftUI

/// A page pushed over the tab shell (the house Push Right transition).
private enum PushedPage: Identifiable, Equatable {
	case newsDetail(article: String)
	case stockDetail(fromMyStak: Bool)
	case collection
	case profile
	case simPortfolio
	case simPick
	case leaderboard

	var id: String {
		switch self {
		case .newsDetail(let article): return "newsDetail-\(article)"
		case .stockDetail(let fromMyStak): return "stockDetail-\(fromMyStak)"
		case .collection: return "collection"
		case .profile: return "profile"
		case .simPortfolio: return "simPortfolio"
		case .simPick: return "simPick"
		case .leaderboard: return "leaderboard"
		}
	}
}

/// The bottom-tab shell — mirrors android/ MainShell (StakNavHost.kt).
/// The bar hides while Home is in its first-run state (the scrim + pill
/// own the bottom). Tab switches are the prototype's "Swap overlay ·
/// Instant"; pushed pages (article, stock detail, collection, profile,
/// the Simulate portfolio/pick/leaderboard) slide in with the house
/// Push Right.
struct MainTabsView: View {
	var onLogOut: () -> Void = {}
	@State private var tab = MainTab.home
	// First run shows only in the session that signed in / created the
	// account; a launch that resumed a saved session lands on Home Main.
	@State private var homeFirstRun = !Session.shared.resumedSignedIn
	@State private var pushed: [PushedEntry] = []

	/// One live entry on the pushed stack. Identity is PER PUSH (a fresh
	/// uid), not per page - an article chain can legally revisit a story
	/// (A -> B -> A via READ NEXT), and ForEach needs distinct ids for
	/// the twins, mirroring Android's per-push back-stack entries
	/// (audit 2026-08-25).
	private struct PushedEntry: Identifiable, Equatable {
		let id = UUID()
		let page: PushedPage
	}
	/// Hoisted Discover buy ticket — the sheet's scrim covers the tab bar
	/// (frame 1:1970), so the shell owns it, mirroring Android MainShell.
	@State private var discoverBuy: BuySpec? = nil
	/// 1:4232 / 85:895: the Simulate ticket also covers the tab bar.
	@State private var simulateBuy = false

	var body: some View {
		ZStack {
			VStack(spacing: 0) {
				Group {
					switch tab {
					case .home:
						HomeView(
							firstRun: homeFirstRun,
							// Authored (1:958/1:1097 Motion): the pill and the deck
							// banner jump to the Discover deck ("first run" frame),
							// the mood card to News, the why-card to My STAK — all
							// Instant; first-run ends once the pick is seen.
							onSeeTodaysPick: { homeFirstRun = false; tab = .discover },
							onProfile: { push(.profile) },
							onOpenNews: { tab = .news },
							onOpenMyStak: { tab = .myStak },
							onOpenDeck: { homeFirstRun = false; tab = .discover }
						)
					case .news:
						// Authored (1:1228): Story tile -> News detail unsaved, Instant.
						NewsView(onOpenArticle: { id in pushInstant(.newsDetail(article: id)) })
					case .discover:
						DiscoverView(
							// Authored (1:1785): Learn more -> Stock Detail folded, Instant.
							onLearnMore: { pushInstant(.stockDetail(fromMyStak: false)) },
							onPracticeBuy: { discoverBuy = $0 }
						)
					case .myStak:
						MyStakView(
							onOpenCollection: { push(.collection) },
							onStartSwiping: { tab = .discover }
						)
					case .simulate:
						SimulateView(
							onOpenPortfolio: { push(.simPortfolio) },
							onOpenPick: { push(.simPick) },
							onOpenLeaderboard: { push(.leaderboard) },
							onPracticeBuy: { simulateBuy = true }
						)
					}
				}
				.frame(maxWidth: .infinity, maxHeight: .infinity)
				if !(tab == .home && homeFirstRun) {
					// authored height includes the home-indicator zone, so the
					// shell lets it run to the physical bottom of the screen.
					// One stable bar on every tab (user, 2026-08-23): only the
					// active tab changes.
					MainTabBar(selected: $tab)
				}
			}
			.ignoresSafeArea(edges: .bottom)

			ForEach(pushed) { entry in
				pageView(entry.page)
					.id(entry.id)
					.transition(FlowAnim.pushRight.transition)
			}

			if let spec = discoverBuy {
				DiscoverBuyFlow(spec: spec, onClose: { discoverBuy = nil })
			}
			if simulateBuy {
				DiscoverBuyFlow(spec: pltrBuy, onClose: { simulateBuy = false }, filledPrimary: "View portfolio", filledSecondary: "Done", ticketSecondary: "Back")
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}

	@ViewBuilder
	private func pageView(_ page: PushedPage) -> some View {
		switch page {
		case .newsDetail(let article):
			// The article's back motion is not yet authored - it mirrors the
			// instant entry until its panel says otherwise.
			NewsDetailView(
				articleId: article,
				onBack: { popInstant() },
				// Authored (101:1005): View in My STAK -> My STAK Overview,
				// Push Right 300 - the article slides out trailing while the
				// shell (already on the My STAK tab) is revealed.
				onViewInMyStak: {
					tab = .myStak
					// The whole article chain clears - after READ NEXT hops
					// the stack can hold several articles, and popping one
					// would strand the user on the previous story
					// (audit 2026-08-25).
					withAnimation(FlowAnim.pushRight.animation) { pushed.removeAll() }
				},
				// READ NEXT rows push the next story's article (user, 2026-08-25).
				onOpenArticle: { id in pushInstant(.newsDetail(article: id)) }
			)
		case .stockDetail(let fromMyStak):
			// Discover-entry back mirrors the instant entry (provisional);
			// the My STAK entry keeps the house pop until its panel.
			StockDetailView(onBack: { if fromMyStak { pop() } else { popInstant() } }, fromMyStak: fromMyStak)
		case .collection:
			CollectionView(
				onBack: { pop() },
				onOpenStock: { push(.stockDetail(fromMyStak: true)) }
			)
		case .profile:
			ProfileView(onBack: { pop() }, onLogOut: onLogOut)
		case .simPortfolio:
			SimPortfolioView(
				onBack: { pop() },
				onOpenPick: { push(.simPick) }
			)
		case .simPick:
			PickDetailView(onBack: { pop() })
		case .leaderboard:
			LeaderboardView(onBack: { pop() })
		}
	}

	private func push(_ page: PushedPage) {
		// Double-tap during the 300ms slide would stack the same page
		// twice (audit 2026-08-25).
		guard pushed.last?.page.id != page.id else { return }
		withAnimation(FlowAnim.pushRight.animation) { pushed.append(PushedEntry(page: page)) }
	}

	private func pop() {
		withAnimation(FlowAnim.pushLeft.animation) { _ = pushed.popLast() }
	}

	/// Appends/removes with no animation - the prototype's "Instant".
	private func pushInstant(_ page: PushedPage) {
		guard pushed.last?.page.id != page.id else { return }
		pushed.append(PushedEntry(page: page))
	}

	private func popInstant() {
		_ = pushed.popLast()
	}
}
