import SwiftUI

/// A page pushed over the tab shell (the house Push Right transition).
private enum PushedPage: Identifiable, Equatable {
	case newsDetail
	case stockDetail(fromMyStak: Bool)
	case collection
	case profile

	var id: String {
		switch self {
		case .newsDetail: return "newsDetail"
		case .stockDetail(let fromMyStak): return "stockDetail-\(fromMyStak)"
		case .collection: return "collection"
		case .profile: return "profile"
		}
	}
}

/// The bottom-tab shell — mirrors android/ MainShell (StakNavHost.kt).
/// The bar hides while Home is in its first-run state (the scrim + pill
/// own the bottom). Tab switches are the prototype's "Swap overlay ·
/// Instant"; pushed pages (article, stock detail, collection, profile)
/// slide in with the house Push Right. Simulate awaits its port.
struct MainTabsView: View {
	@State private var tab = MainTab.home
	@State private var homeFirstRun = true
	@State private var pushed: [PushedPage] = []

	var body: some View {
		ZStack {
			VStack(spacing: 0) {
				Group {
					switch tab {
					case .home:
						HomeView(
							firstRun: homeFirstRun,
							onSeeTodaysPick: { homeFirstRun = false },
							onProfile: { push(.profile) }
						)
					case .news:
						NewsView(onOpenArticle: { push(.newsDetail) })
					case .discover:
						DiscoverView(onLearnMore: { push(.stockDetail(fromMyStak: false)) })
					case .myStak:
						MyStakView(
							onOpenCollection: { push(.collection) },
							onStartSwiping: { tab = .discover }
						)
					case .simulate:
						// Placeholder until the Simulate tab is ported.
						Text(tab.rawValue)
							.foregroundStyle(Color.white)
					}
				}
				.frame(maxWidth: .infinity, maxHeight: .infinity)
				if !(tab == .home && homeFirstRun) {
					MainTabBar(selected: $tab)
				}
			}

			ForEach(pushed) { page in
				pageView(page)
					.id(page.id)
					.transition(FlowAnim.pushRight.transition)
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}

	@ViewBuilder
	private func pageView(_ page: PushedPage) -> some View {
		switch page {
		case .newsDetail:
			NewsDetailView(onBack: { pop() })
		case .stockDetail(let fromMyStak):
			StockDetailView(onBack: { pop() }, fromMyStak: fromMyStak)
		case .collection:
			CollectionView(
				onBack: { pop() },
				onOpenStock: { push(.stockDetail(fromMyStak: true)) }
			)
		case .profile:
			ProfileView(onBack: { pop() })
		}
	}

	private func push(_ page: PushedPage) {
		withAnimation(FlowAnim.pushRight.animation) { pushed.append(page) }
	}

	private func pop() {
		withAnimation(FlowAnim.pushLeft.animation) { _ = pushed.popLast() }
	}
}
