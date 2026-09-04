import SwiftUI

/// Palette of the CHINEDU "03 · News" frames — mirrors android/ NewsScreen.kt.
enum News {
	static let moodBg = Color(argb: 0xFF171D2C)
	static let cardBg = Color(argb: 0xFF181F30)
	static let teal = Color(argb: 0xFF69B3CA)
	static let ink = Color(argb: 0xFF0E162B)
	static let muted = Color(argb: 0xFF819ABB)
	static let faint = Color(argb: 0xFF5C6B85)
	static let chipBg = Color(argb: 0xFF242B3D)
	static let headerGray = Color(argb: 0xFFD3D3D3)
	static let body = Color(argb: 0xFFC8D2E0)
	static let green = Color(argb: 0xFF2FD08A)
	static let divider = Color(argb: 0xFF2A3346)
}

// The For You / Markets rows render from the served section feeds
// (NewsArticleFeed.forYou / .markets); EVERY story opens its article
// (user, 2026-08-25).

/// 03 · News — "News listing tab", Figma node 1:1228 (CHINEDU file; mirrors
/// android/ NewsScreen.kt). Fixed header ("News", date, search circle),
/// then the scrolling stack: the compact Market Mood row, the teal
/// TODAY'S BRIEF carousel card with pager dots, the two-tile story grid,
/// and the For You / Markets card lists. The tab bar comes from MainTabsView.
/// Every metric is scaled by the 390pt artboard unit (`figmaUnit`), exactly
/// like the Android build's `u` scaling.
struct NewsView: View {
	let onOpenArticle: (String) -> Void
	// Section membership is holdings-driven (For You = held stocks), so a
	// save must re-render the listing, not just the row chips.
	@ObservedObject private var holdings = MyStakHoldings.shared

	// Designer's call (2026-08-22): the search icon opens a search bar that
	// word-matches the news content; empty when nothing matches.
	@State private var searching = false
	@State private var query = ""

	private var q: String { query.trimmingCharacters(in: .whitespaces) }
	private func matches(_ text: String) -> Bool {
		q.isEmpty || text.range(of: q, options: .caseInsensitive) != nil
	}
	// Search reads the whole story, not just its headline (Codex audit
	// 2026-09-04): subtitle, source, tags and ticker too.
	private func matchesArticle(_ a: NewsArticleFeed.Article) -> Bool { articleMatches(a, q) }
	private func matchesBrief(_ b: NewsBriefFeed.Brief) -> Bool {
		matches(b.title) || matches(b.body) || matches(b.source)
	}

	private static func todayLabel() -> String {
		let f = DateFormatter()
		f.locale = Locale(identifier: "en_US_POSIX")
		f.dateFormat = "EEEE, MMMM d"
		return f.string(from: Date())
	}

	var body: some View {
		let u = figmaUnit
		VStack(spacing: 0) {
			HStack {
				VStack(alignment: .leading, spacing: 4 * u) {
					Text("News")
						.font(StakFont.sora(26 * u, .semiBold))
						.foregroundStyle(StakColors.textPrimary)
					// The frame's "Saturday, July 4" is the authored example; the
					// screen shows today (Codex audit 2026-09-04), in the frame's
					// English format whatever the device locale.
					Text(Self.todayLabel())
						.font(StakFont.geist(13 * u))
						.foregroundStyle(News.muted)
				}
				Spacer()
				Button {
					searching.toggle()
					if !searching { query = "" }
				} label: {
					ZStack {
						Circle().fill(News.cardBg)
						Image("IcNewsSearch")
							.resizable()
							.frame(width: 20 * u, height: 20 * u)
					}
					.frame(width: 40 * u, height: 40 * u)
				}
				.buttonStyle(.plain)
				.accessibilityLabel("Search")
			}
			.padding(.horizontal, 20 * u)
			.padding(.top, 22 * u)

			if searching {
				TextField("Search news", text: $query)
					.font(StakFont.geist(13 * u))
					.foregroundStyle(StakColors.textPrimary)
					.tint(News.teal)
					.autocorrectionDisabled()
					.textInputAutocapitalization(.never)
					.padding(.horizontal, 16 * u)
					.padding(.vertical, 13 * u)
					.background(News.cardBg, in: RoundedRectangle(cornerRadius: 12 * u))
					.padding(.horizontal, 20 * u)
					.padding(.top, 12 * u)
			}
			ScrollView {
				VStack(spacing: 22 * u) {
					if q.isEmpty { MoodMiniRow() }
					// Designer's call (2026-08-22): today's brief on tap leads to
					// the News info page (Story tile stays wired per 1:1228).
					// Only the briefs that match ride the carousel (Codex audit
					// 2026-09-04: one hit used to show all four pages).
					let briefHits = NewsBriefFeed.briefs().filter { matchesBrief($0) }
					if !briefHits.isEmpty {
						// Each brief opens ITS OWN article (user, 2026-08-25). The
						// index guard covers a served brief without a mapped article.
						BriefCarousel(briefs: briefHits, onRead: { page in
							if NewsArticleFeed.briefArticles.indices.contains(page) {
								onOpenArticle(NewsArticleFeed.briefArticles[page])
							}
						})
					}
					StoryGrid(onOpenArticle: onOpenArticle, query: q)
					// The rows render from the served section feeds - STRICT
					// stock news only (user, 2026-08-25); EVERY story opens
					// its article.
					let forYou = NewsArticleFeed.forYou().filter { matchesArticle($0) }
					if !forYou.isEmpty {
						NewsSectionView(title: "For You", rows: forYou, onOpen: onOpenArticle)
					}
					let markets = NewsArticleFeed.markets().filter { matchesArticle($0) }
					if !markets.isEmpty {
						NewsSectionView(title: "Markets", rows: markets, onOpen: onOpenArticle)
					}
				}
				.padding(.horizontal, 20 * u)
				.padding(.top, 22 * u)
				// The Android column ends on a 0dp spacer, which its 22dp
				// item spacing turns into a 22dp bottom inset.
				.padding(.bottom, 22 * u)
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}

/// Search over the whole story: headline, subtitle, source, tags, ticker.
private func articleMatches(_ a: NewsArticleFeed.Article, _ q: String) -> Bool {
	if q.isEmpty { return true }
	func hit(_ t: String) -> Bool { t.range(of: q, options: .caseInsensitive) != nil }
	return hit(a.headline) || hit(a.subtitle) || hit(a.source) || a.tags.contains(where: hit) || (a.ticker.map(hit) ?? false)
}

/// Compact Market Mood row — #171d2c r12 with the small gauge. Both the
/// status line and the needle are the SAME shared mood state Home shows
/// (Codex audit 2026-09-04): the row used to hard-code "Low volatility"
/// over the baked NewsGaugeSmall needle resting in the red band.
private struct MoodMiniRow: View {
	var body: some View {
		let u = figmaUnit
		HStack {
			VStack(alignment: .leading, spacing: 2 * u) {
				Text("Market Mood")
					.font(StakFont.sora(13 * u, .semiBold))
					.foregroundStyle(StakColors.textPrimary)
				Text(MarketMoodFeed.statusLead)
					.font(StakFont.geist(11 * u))
					.foregroundStyle(News.teal)
			}
			Spacer()
			// The Home gauge drawn at the authored compact size (40.97 x 20.76).
			MarketMoodGauge(scale: 40.97 / 56.9018)
		}
		.padding(.horizontal, 14 * u)
		.padding(.vertical, 12 * u)
		.background(News.moodBg, in: RoundedRectangle(cornerRadius: 12 * u))
	}
}

/// TODAY'S BRIEF — teal r18 feature card + pager dots. The user swipes
/// left and right through the served briefs (user, 2026-08-22); the
/// active dot follows the page. Text comes from NewsBriefFeed.
private struct BriefCarousel: View {
	/// The briefs on this ride - the search's hits, or every brief.
	let briefs: [NewsBriefFeed.Brief]
	let onRead: (Int) -> Void
	@State private var page: Int? = 0

	var body: some View {
		let u = figmaUnit
		VStack(spacing: 12 * u) {
			ScrollView(.horizontal, showsIndicators: false) {
				LazyHStack(spacing: 0) {
					ForEach(briefs) { brief in
						BriefCard(brief: brief, onRead: { onRead(brief.id) })
							.containerRelativeFrame(.horizontal)
							.id(brief.id)
					}
				}
				.scrollTargetLayout()
			}
			.scrollTargetBehavior(.paging)
			.scrollPosition(id: $page)

			// Pager dots — the active page is the 16x6 teal pill, the rest
			// 6pt #5c6b85 dots, 6pt gaps: the authored 52x6 strip.
			HStack(spacing: 6 * u) {
				ForEach(briefs) { brief in
					if brief.id == (page ?? 0) {
						RoundedRectangle(cornerRadius: 3 * u)
							.fill(News.teal)
							.frame(width: 16 * u, height: 6 * u)
					} else {
						Circle()
							.fill(News.faint)
							.frame(width: 6 * u, height: 6 * u)
					}
				}
			}
			.frame(height: 6 * u)
			// Authored carousel block (1:1261) is 229 tall: 15 of slack under
			// the dots (the Android 12 column gap + 3 spacer) before the section gap.
			.padding(.bottom, 15 * u)
		}
	}
}

/// One brief card in the carousel slot.
private struct BriefCard: View {
	let brief: NewsBriefFeed.Brief
	let onRead: () -> Void

	var body: some View {
		let u = figmaUnit
		Button(action: onRead) {
			VStack(alignment: .leading, spacing: 9 * u) {
				Text("TODAY\u{2019}S BRIEF")
					.font(StakFont.geist(10 * u, .medium))
					.tracking(0.6 * u)
					.foregroundStyle(News.ink)
				Text(brief.title)
					.font(StakFont.sora(19 * u, .semiBold))
					.lineSpacing((25 - 19) * u)
					.foregroundStyle(News.ink)
				Text(brief.body)
					// 12.2 restores the authored 3-line wrap (12.5 wraps to 4).
					.font(StakFont.geist(12.2 * u))
					.lineSpacing((17 - 12.2) * u)
					.foregroundStyle(News.ink)
				HStack {
					Text(brief.source)
						.font(StakFont.geist(11 * u))
						.foregroundStyle(News.ink.opacity(0.6))
					Spacer()
					HStack(spacing: 4 * u) {
						Text("Read")
							.font(StakFont.geist(12 * u, .medium))
							.foregroundStyle(News.ink)
						Text("\u{203A}")
							.font(StakFont.geist(13 * u, .medium))
							.foregroundStyle(News.ink)
					}
				}
				.frame(height: 21 * u)
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			.padding(.top, 18 * u)
			.padding(.horizontal, 18 * u)
			.padding(.bottom, 16 * u)
			.background(News.teal, in: RoundedRectangle(cornerRadius: 18 * u))
		}
		.buttonStyle(.plain)
	}
}

/// The two 128-unit story tiles. The tags are authored SLOT labels
/// (1:1228: "Markets" = the day's top stock story, "Your stocks" = the
/// top story from the user's holdings); the tile copy renders from the
/// SERVED story - the frame's copy was placeholder (user, 2026-08-25:
/// "the design by the ui is just placeholder"), and only strict stock
/// news is served.
private struct StoryGrid: View {
	let onOpenArticle: (String) -> Void
	var query: String = ""

	private func visible(_ a: NewsArticleFeed.Article) -> Bool {
		NewsArticleFeed.isStockNews(a) && articleMatches(a, query)
	}

	var body: some View {
		let u = figmaUnit
		let market = NewsArticleFeed.article(NewsArticleFeed.marketTile)
		let yours = NewsArticleFeed.article(NewsArticleFeed.apple)
		let showMarket = visible(market)
		let showYours = visible(yours)
		if showMarket || showYours {
			HStack(spacing: 12 * u) {
				if showMarket {
					StoryTile(
						tag: "Markets",
						headline: market.headline,
						source: "\(market.source) · \(market.age)",
						onTap: { onOpenArticle(market.id) }
					)
				}
				if showYours {
					StoryTile(
						tag: "Your stocks",
						headline: yours.headline,
						source: "\(yours.source) · \(yours.age)",
						onTap: { onOpenArticle(yours.id) }
					)
				}
			}
			.frame(height: 128 * u)
		}
	}
}

private struct StoryTile: View {
	let tag: String
	let headline: String
	let source: String
	let onTap: () -> Void

	var body: some View {
		let u = figmaUnit
		Button(action: onTap) {
			VStack(alignment: .leading, spacing: 8 * u) {
				NewsTag(text: tag, tracking: 0.4 * u)
				Text(headline)
					// Authored (1:1277): Sora Light 12 in the 20 line box.
					.font(StakFont.sora(12 * u, .light))
					.lineSpacing((20 - 12) * u)
					.foregroundStyle(StakColors.textPrimary)
				Spacer(minLength: 0)
				Text(source)
					.font(StakFont.geist(10 * u))
					.foregroundStyle(News.muted)
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			.padding(14 * u)
			.frame(height: 128 * u)
			.background(News.cardBg, in: RoundedRectangle(cornerRadius: 12 * u))
		}
		.buttonStyle(.plain)
	}
}

/// #242b3d r5 chip — Geist Medium 8 #819abb.
struct NewsTag: View {
	let text: String
	var tracking: CGFloat = 0

	var body: some View {
		let u = figmaUnit
		Text(text)
			.font(StakFont.geist(8 * u, .medium))
			.tracking(tracking)
			.foregroundStyle(News.muted)
			.padding(.horizontal, 7 * u)
			.padding(.vertical, 3 * u)
			.background(News.chipBg, in: RoundedRectangle(cornerRadius: 5 * u))
	}
}

/// "For You" / "Markets" — Sora 16 #d3d3d3 header + 60-unit-thumb cards
/// (84-unit rows: 60 thumb + 12 padding each side, 10-unit gaps).
private struct NewsSectionView: View {
	@ObservedObject var holdings = MyStakHoldings.shared
	let title: String
	let rows: [NewsArticleFeed.Article]
	let onOpen: (String) -> Void

	private func rowPosterAsset(_ row: NewsArticleFeed.Article) -> String? {
		switch row.media {
		case .image(let posterAsset, _, _): return posterAsset
		case .video(_, let posterAsset, _, _): return posterAsset
		}
	}

	private func rowPosterUrl(_ row: NewsArticleFeed.Article) -> String? {
		switch row.media {
		case .image(_, let url, _): return url
		case .video(_, _, let posterUrl, _): return posterUrl
		}
	}

	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 10 * u) {
			Text(title)
				.font(StakFont.sora(16 * u, .semiBold))
				.foregroundStyle(News.headerGray)
			ForEach(rows, id: \.id) { row in
				// Every row opens its story's article (user, 2026-08-25).
				Button {
					onOpen(row.id)
				} label: {
					HStack(spacing: 12 * u) {
						// Every row carries art (user, 2026-09-02): the bundled
						// thumb when served, else the story’s own media poster.
						if let thumb = row.thumb ?? rowPosterAsset(row) {
							Image(thumb)
								.resizable()
								.scaledToFill()
								.frame(width: 60 * u, height: 60 * u)
								.clipShape(RoundedRectangle(cornerRadius: 10 * u))
						} else if let urlString = rowPosterUrl(row), let url = URL(string: urlString) {
							AsyncImage(url: url) { img in
								img.resizable().scaledToFill()
							} placeholder: {
								News.cardBg
							}
							.frame(width: 60 * u, height: 60 * u)
							.clipShape(RoundedRectangle(cornerRadius: 10 * u))
						}
						VStack(alignment: .leading, spacing: 5 * u) {
							HStack {
								Text("\(row.source) · \(row.age)")
									.font(StakFont.geist(11 * u))
									.foregroundStyle(News.muted)
								Spacer()
								// Only for stocks the user holds (user, 2026-08-23).
								if holdings.holdsAny(row.relatedTickers) {
									NewsTag(text: "In your STAK")
								}
							}
							Text(row.headline)
								// Authored (1:1295): Sora Light 12 in the 19 line box.
								.font(StakFont.sora(12 * u, .light))
								.lineSpacing((19 - 12) * u)
								.foregroundStyle(StakColors.textPrimary)
								.frame(maxWidth: .infinity, alignment: .leading)
						}
					}
					.padding(12 * u)
					.background(News.cardBg, in: RoundedRectangle(cornerRadius: 14 * u))
				}
				.buttonStyle(.plain)
			}
		}
		.frame(maxWidth: .infinity, alignment: .leading)
	}
}
