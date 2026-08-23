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

/// One For You / Markets list row.
private struct NewsRowModel: Identifiable {
	let thumb: String
	let source: String
	let headline: String
	var id: String { headline }
}

private let forYouRows: [NewsRowModel] = [
	NewsRowModel(thumb: "NewsThumbNVDA", source: "Reuters · 2d", headline: "Nvidia lags the chip rally it kicked off"),
	NewsRowModel(thumb: "NewsThumbAAPL", source: "Bloomberg · 2d", headline: "Apple climbs 5% on foldable iPhone push"),
	NewsRowModel(thumb: "NewsThumbTSLA", source: "CNBC · 2d", headline: "Tesla drops 7% even after beating deliveries")
]

private let marketsRows: [NewsRowModel] = [
	NewsRowModel(thumb: "NewsThumbJobs", source: "Reuters · 2d", headline: "June jobs miss eases Fed hike bets"),
	NewsRowModel(thumb: "NewsThumbChips", source: "Bloomberg · 2d", headline: "Memory chips soar as the AI trade rotates"),
	NewsRowModel(thumb: "NewsThumbOil", source: "Reuters · 3d", headline: "Oil slips after positive Iran talks")
]

/// 03 · News — "News listing tab", Figma node 1:1228 (CHINEDU file; mirrors
/// android/ NewsScreen.kt). Fixed header ("News", date, search circle),
/// then the scrolling stack: the compact Market Mood row, the teal
/// TODAY'S BRIEF carousel card with pager dots, the two-tile story grid,
/// and the For You / Markets card lists. The tab bar comes from MainTabsView.
/// Every metric is scaled by the 390pt artboard unit (`figmaUnit`), exactly
/// like the Android build's `u` scaling.
struct NewsView: View {
	let onOpenArticle: () -> Void

	// Designer's call (2026-08-22): the search icon opens a search bar that
	// word-matches the news content; empty when nothing matches.
	@State private var searching = false
	@State private var query = ""

	private var q: String { query.trimmingCharacters(in: .whitespaces) }
	private func matches(_ text: String) -> Bool {
		q.isEmpty || text.range(of: q, options: .caseInsensitive) != nil
	}

	var body: some View {
		let u = figmaUnit
		VStack(spacing: 0) {
			HStack {
				VStack(alignment: .leading, spacing: 4 * u) {
					Text("News")
						.font(StakFont.sora(26 * u, .semiBold))
						.foregroundStyle(StakColors.textPrimary)
					Text("Saturday, July 4")
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
					if NewsBriefFeed.briefs().contains(where: { matches($0.title) }) {
						BriefCarousel(onRead: onOpenArticle)
					}
					StoryGrid(onOpenArticle: onOpenArticle, query: q)
					let forYou = forYouRows.filter { matches($0.headline) }
					if !forYou.isEmpty {
						NewsSectionView(title: "For You", rows: forYou, onOpenArticle: onOpenArticle)
					}
					let markets = marketsRows.filter { matches($0.headline) }
					if !markets.isEmpty {
						NewsSectionView(title: "Markets", rows: markets, onOpenArticle: onOpenArticle)
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

/// Compact Market Mood row — #171d2c r12 with the small low-volatility gauge
/// (the baked NewsGaugeSmall asset).
private struct MoodMiniRow: View {
	var body: some View {
		let u = figmaUnit
		HStack {
			VStack(alignment: .leading, spacing: 2 * u) {
				Text("Market Mood")
					.font(StakFont.sora(13 * u, .semiBold))
					.foregroundStyle(StakColors.textPrimary)
				Text("Low volatility")
					.font(StakFont.geist(11 * u))
					.foregroundStyle(News.teal)
			}
			Spacer()
			Image("NewsGaugeSmall")
				.resizable()
				.frame(width: 40.97 * u, height: 20.76 * u)
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
	let onRead: () -> Void
	@State private var page: Int? = 0

	var body: some View {
		let u = figmaUnit
		let briefs = NewsBriefFeed.briefs()
		VStack(spacing: 12 * u) {
			ScrollView(.horizontal, showsIndicators: false) {
				LazyHStack(spacing: 0) {
					ForEach(briefs) { brief in
						BriefCard(brief: brief, onRead: onRead)
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

/// The two 128-unit story tiles ("Markets" / "Your stocks").
private struct StoryGrid: View {
	let onOpenArticle: () -> Void
	var query: String = ""

	private func show(_ text: String) -> Bool {
		query.isEmpty || text.range(of: query, options: .caseInsensitive) != nil
	}

	var body: some View {
		let u = figmaUnit
		let fed = show("Fed minutes land Wednesday")
		let apple = show("Apple Climbs 5% on foldable iphone")
		if fed || apple {
			HStack(spacing: 12 * u) {
				if fed {
					StoryTile(
						tag: "Markets",
						headline: "Fed minutes land Wednesday",
						source: "Reuters · 2h",
						onTap: {}
					)
				}
				if apple {
					StoryTile(
						tag: "Your stocks",
						headline: "Apple Climbs 5% on foldable iphone",
						source: "CNBC · 3h",
						onTap: onOpenArticle
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
					.font(StakFont.sora(14 * u, .light))
					.lineSpacing((20 - 14) * u)
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
	let title: String
	let rows: [NewsRowModel]
	let onOpenArticle: () -> Void

	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 10 * u) {
			Text(title)
				.font(StakFont.sora(16 * u, .semiBold))
				.foregroundStyle(News.headerGray)
			ForEach(rows) { row in
				Button {
					// Authored motion (1:1228): the rows have no connection.
				} label: {
					HStack(spacing: 12 * u) {
						Image(row.thumb)
							.resizable()
							.scaledToFill()
							.frame(width: 60 * u, height: 60 * u)
							.clipShape(RoundedRectangle(cornerRadius: 10 * u))
						VStack(alignment: .leading, spacing: 5 * u) {
							HStack {
								Text(row.source)
									.font(StakFont.geist(11 * u))
									.foregroundStyle(News.muted)
								Spacer()
								NewsTag(text: "In your STAK")
							}
							Text(row.headline)
								.font(StakFont.sora(14 * u, .light))
								.lineSpacing((19 - 14) * u)
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
