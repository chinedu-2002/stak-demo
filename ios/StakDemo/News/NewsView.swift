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
struct NewsView: View {
	let onOpenArticle: () -> Void

	var body: some View {
		VStack(spacing: 0) {
			HStack {
				VStack(alignment: .leading, spacing: 4) {
					Text("News")
						.font(StakFont.sora(26, .semiBold))
						.foregroundStyle(StakColors.textPrimary)
					Text("Saturday, July 4")
						.font(StakFont.geist(13))
						.foregroundStyle(News.muted)
				}
				Spacer()
				ZStack {
					Circle().fill(News.cardBg)
					Image("IcNewsSearch")
						.resizable()
						.frame(width: 20, height: 20)
				}
				.frame(width: 40, height: 40)
				.accessibilityLabel("Search")
			}
			.padding(.horizontal, 20)
			.padding(.top, 22)

			ScrollView {
				VStack(spacing: 22) {
					MoodMiniRow()
					BriefCarousel(onRead: onOpenArticle)
					StoryGrid(onOpenArticle: onOpenArticle)
					NewsSectionView(title: "For You", rows: forYouRows, onOpenArticle: onOpenArticle)
					NewsSectionView(title: "Markets", rows: marketsRows, onOpenArticle: onOpenArticle)
				}
				.padding(.horizontal, 20)
				.padding(.top, 22)
				// The Android column ends on a 0dp spacer, which its 22dp
				// item spacing turns into a 22dp bottom inset.
				.padding(.bottom, 22)
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}

/// Compact Market Mood row — #171d2c r12 with the small low-volatility gauge.
private struct MoodMiniRow: View {
	var body: some View {
		HStack {
			VStack(alignment: .leading, spacing: 2) {
				Text("Market Mood")
					.font(StakFont.sora(13, .semiBold))
					.foregroundStyle(StakColors.textPrimary)
				Text("Low volatility")
					.font(StakFont.geist(11))
					.foregroundStyle(News.teal)
			}
			Spacer()
			Image("NewsGaugeSmall")
				.resizable()
				.frame(width: 40.97, height: 20.76)
		}
		.padding(.horizontal, 14)
		.padding(.vertical, 12)
		.background(News.moodBg, in: RoundedRectangle(cornerRadius: 12))
	}
}

/// TODAY'S BRIEF — teal r18 feature card + pager dots.
private struct BriefCarousel: View {
	let onRead: () -> Void

	var body: some View {
		VStack(spacing: 12) {
			Button(action: onRead) {
				VStack(alignment: .leading, spacing: 9) {
					Text("TODAY’S BRIEF")
						.font(StakFont.geist(10, .medium))
						.tracking(0.6)
						.foregroundStyle(News.ink)
					Text("Dow closes at a record as chips slide")
						.font(StakFont.sora(19, .semiBold))
						.lineSpacing(25 - 19)
						.foregroundStyle(News.ink)
					Text("Wall Street split into the long weekend. The Dow hit an all time high while a memory chip rout pulled the Nasdaq down, and a soft jobs report eased the pressure on the...")
						.font(StakFont.geist(12))
						.lineSpacing(17 - 12)
						.foregroundStyle(News.ink)
					HStack {
						Text("Bloomberg · 10h")
							.font(StakFont.geist(11))
							.foregroundStyle(News.ink.opacity(0.6))
						Spacer()
						HStack(spacing: 4) {
							Text("Read")
								.font(StakFont.geist(12, .medium))
								.foregroundStyle(News.ink)
							Text("›")
								.font(StakFont.geist(13, .medium))
								.foregroundStyle(News.ink)
						}
					}
					.padding(.top, 4)
				}
				.frame(maxWidth: .infinity, alignment: .leading)
				.padding(.top, 18)
				.padding(.horizontal, 18)
				.padding(.bottom, 16)
				.background(News.teal, in: RoundedRectangle(cornerRadius: 18))
			}
			.buttonStyle(.plain)

			// Pager dots — 16x6 active pill (#69b3ca) + three #5c6b85 dots,
			// 6pt gaps: the same 52x6 strip the Android Canvas draws.
			HStack(spacing: 6) {
				RoundedRectangle(cornerRadius: 3)
					.fill(News.teal)
					.frame(width: 16, height: 6)
				ForEach(0..<3, id: \.self) { _ in
					Circle()
						.fill(News.faint)
						.frame(width: 6, height: 6)
				}
			}
			.frame(width: 52, height: 6)
		}
	}
}

/// The two 128pt story tiles ("Markets" / "Your stocks").
private struct StoryGrid: View {
	let onOpenArticle: () -> Void

	var body: some View {
		HStack(spacing: 12) {
			StoryTile(
				tag: "Markets",
				headline: "Fed minutes land Wednesday",
				source: "Reuters · 2h",
				onTap: {}
			)
			StoryTile(
				tag: "Your stocks",
				headline: "Apple Climbs 5% on foldable iphone",
				source: "CNBC · 3h",
				onTap: onOpenArticle
			)
		}
	}
}

private struct StoryTile: View {
	let tag: String
	let headline: String
	let source: String
	let onTap: () -> Void

	var body: some View {
		Button(action: onTap) {
			VStack(alignment: .leading, spacing: 8) {
				NewsTag(text: tag, tracking: 0.4)
				Text(headline)
					.font(StakFont.sora(12, .light))
					.lineSpacing(20 - 12)
					.foregroundStyle(StakColors.textPrimary)
				Spacer(minLength: 0)
				Text(source)
					.font(StakFont.geist(10))
					.foregroundStyle(News.muted)
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			.padding(14)
			.frame(height: 128)
			.background(News.cardBg, in: RoundedRectangle(cornerRadius: 12))
		}
		.buttonStyle(.plain)
	}
}

/// #242b3d r5 chip — Geist Medium 8 #819abb.
struct NewsTag: View {
	let text: String
	var tracking: CGFloat = 0

	var body: some View {
		Text(text)
			.font(StakFont.geist(8, .medium))
			.tracking(tracking)
			.foregroundStyle(News.muted)
			.padding(.horizontal, 7)
			.padding(.vertical, 3)
			.background(News.chipBg, in: RoundedRectangle(cornerRadius: 5))
	}
}

/// "For You" / "Markets" — Sora 16 #d3d3d3 header + 60pt-thumb cards.
private struct NewsSectionView: View {
	let title: String
	let rows: [NewsRowModel]
	let onOpenArticle: () -> Void

	var body: some View {
		VStack(alignment: .leading, spacing: 10) {
			Text(title)
				.font(StakFont.sora(16, .semiBold))
				.foregroundStyle(News.headerGray)
				.padding(.bottom, 2)
			ForEach(rows) { row in
				Button {
					// Only the Apple story routes to the article page,
					// same as the Android build.
					if row.headline.contains("Apple") { onOpenArticle() }
				} label: {
					HStack(spacing: 12) {
						Image(row.thumb)
							.resizable()
							.scaledToFill()
							.frame(width: 60, height: 60)
							.clipShape(RoundedRectangle(cornerRadius: 10))
						VStack(alignment: .leading, spacing: 5) {
							HStack {
								Text(row.source)
									.font(StakFont.geist(11))
									.foregroundStyle(News.muted)
								Spacer()
								NewsTag(text: "In your STAK")
							}
							Text(row.headline)
								.font(StakFont.sora(12, .light))
								.lineSpacing(19 - 12)
								.foregroundStyle(StakColors.textPrimary)
								.frame(maxWidth: .infinity, alignment: .leading)
						}
					}
					.padding(12)
					.background(News.cardBg, in: RoundedRectangle(cornerRadius: 14))
				}
				.buttonStyle(.plain)
			}
		}
		.frame(maxWidth: .infinity, alignment: .leading)
	}
}
