import SwiftUI
import WebKit
import AVKit

/// The CHINEDU CTA gradient (Add to STAK / View in My STAK).
private let ctaGradient = LinearGradient(
	stops: [
		.init(color: Color(argb: 0xFFA6E4F7), location: 0.0889),
		.init(color: Color(argb: 0xFF5DA8BF), location: 0.3919),
		.init(color: Color(argb: 0xFF3C98B4), location: 0.7255),
		.init(color: Color(argb: 0xFF3C98B4), location: 1)
	],
	startPoint: .top,
	endPoint: .bottom
)
private let ctaBorder = StakColors.ctaBorderGradient

/// 03 · News — the article page in its three frames: "News detail page
/// unsaved" (1:1495), "News detail · Save success" (101:1005, the bottom
/// sheet over a rgba(12,19,32,0.55) scrim) and "News detail page saved" (1:1359 —
/// hero toast, View-in-My-STAK row on the stock card, Apple + Tech tags).
/// Mirrors android/ NewsDetailScreen.kt. Every metric is scaled by the
/// 390pt artboard unit (`figmaUnit`), exactly like the Android build.
struct NewsDetailView: View {
	/// The served article for the tapped story (user, 2026-08-25); the
	/// Apple article is the authored one and renders frame-exact.
	var articleId: String = NewsArticleFeed.apple
	let onBack: () -> Void
	var onViewInMyStak: () -> Void = {}
	/// READ NEXT rows push the next story's article (user, 2026-08-25).
	var onOpenArticle: (String) -> Void = { _ in }

	@State private var saved = false
	@State private var showSuccess = false

	private var article: NewsArticleFeed.Article { NewsArticleFeed.article(articleId) }

	var body: some View {
		let u = figmaUnit
		let article = self.article
		ZStack {
			VStack(spacing: 0) {
				// Fixed top bar — back circle + share.
				HStack {
					AuthBackCircle(action: onBack)
					Spacer()
					// Designer's call (2026-08-22): share creates a link that
					// takes a co-app user to the shared info.
					ShareLink(item: article.shareText) {
						Image("IcNewsShare")
							.resizable()
							.frame(width: 24 * u, height: 24 * u)
					}
					.buttonStyle(.plain)
					.accessibilityLabel("Share")
				}
				.padding(.leading, 16 * u)
				.padding(.trailing, 18 * u)
				.padding(.top, 10 * u)
				.padding(.bottom, 12 * u)

				ScrollView {
					VStack(spacing: 0) {
						// Authored motion (1:1495): the hero bookmark -> News detail
						// page saved, Instant - a direct save that skips the sheet.
						HeroImage(media: article.media, category: article.category, saved: saved, onBookmark: { saved = true; if let t = article.ticker { MyStakHoldings.shared.add(t) } })
						VStack(alignment: .leading, spacing: 15 * u) {
							Text(article.headline)
								// RENDER-measured 20sp (the metadata's 24 lied); lh32 box stands.
								.font(StakFont.sora(20 * u, .semiBold))
								.lineSpacing((32 - 20) * u)
								.foregroundStyle(StakColors.textPrimary)
							Text(article.subtitle)
								// 14.3 keeps the authored line-1 break after "lineup".
								.font(StakFont.geist(14.3 * u))
								.lineSpacing((22 - 14.3) * u)
								.foregroundStyle(News.muted)
							Byline(source: article.source, meta: article.sourceMeta)
							if !saved {
								// Designer's call (2026-08-22): the sheet scale-ins.
								AddToStakButton { withAnimation(.easeOut(duration: 0.3)) { showSuccess = true } }
							}
						NewsHairline()
							// ONE template for every story (user, 2026-08-25: the
							// Apple article is the section's PLACEHOLDER - each
							// block renders per story from served data; stock
							// blocks appear whenever the story has a ticker).
							if let ticker = article.ticker { StockCard(saved: saved, ticker: ticker, facts: NewsArticleFeed.stockFacts(ticker)) }
							if !article.gist.isEmpty { GistCard(bullets: article.gist) }
							if let first = article.paragraphs.first {
								Paragraph(text: first, size: 15, line: 24)
							}
							if article.paragraphs.count > 1 {
								Paragraph(text: article.paragraphs[1])
							}
							if let quote = article.pullQuote { PullQuote(text: quote) }
							if let explainer = article.explainer { NewToThisCard(body_: explainer) }
							ForEach(Array(article.paragraphs.dropFirst(2).enumerated()), id: \.offset) { _, text in
								Paragraph(text: text)
							}
							SourceRow()
							if let ticker = article.ticker { KeyStatsCard(facts: NewsArticleFeed.stockFacts(ticker)) }
							NewsHairline()
							HStack(spacing: 8 * u) {
								if let first = article.tags.first { ArticleTag(text: first) }
								if saved, article.tags.count > 1 {
									ArticleTag(text: article.tags[1])
								}
							}
							ReadNext(currentId: article.id, onOpen: onOpenArticle)
						}
						.frame(maxWidth: .infinity, alignment: .leading)
						.padding(.horizontal, 20 * u)
						.padding(.top, 22 * u)
						.padding(.bottom, 28 * u)
					}
				}
			}
			// Authored (101:1005 Motion): Back -> News detail page saved,
			// DISSOLVE 300 EaseOut; View in My STAK -> My STAK Overview,
			// Push Right 300 (hoisted to the shell). Entry stays instant (its
			// authored animate type is still unreadable from the file).
			if showSuccess {
				SaveSuccessOverlay(
					facts: NewsArticleFeed.stockFacts(article.ticker ?? "AAPL"),
					onViewInMyStak: { saved = true; if let t = article.ticker { MyStakHoldings.shared.add(t) }; onViewInMyStak() },
					onDismiss: {
						saved = true
						if let t = article.ticker { MyStakHoldings.shared.add(t) }
						withAnimation(.easeOut(duration: 0.3)) { showSuccess = false }
					}
				)
				.transition(.asymmetric(insertion: .scale(scale: 0.92).combined(with: .opacity), removal: .opacity))
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}

/// 360x208 r10 hero — phone art, Tech & Ai toast, play badge, bookmark/saved
/// chip. The authored image is oversized (407x271.18 in the 360x208 card,
/// top-left at -24,-15) — it keeps its exact authored frame (the Compose
/// requiredSize + Crop becomes .scaledToFill + explicit .frame + .clipped)
/// and the card's rounded clip crops the overflow.
// CONTRACT (user, 2026-08-25): this screen is the News info page TEMPLATE.
// The design authors exactly ONE article (1:1495, the Apple foldable
// story), so every authored news tap lands here in the demo. In
// production the backend serves each story's own headline, subtitle,
// body and media into this page - same slot pattern as NewsMedia.
private struct HeroImage: View {
	let media: NewsMedia
	let category: String
	let saved: Bool
	let onBookmark: () -> Void
	/// The hero is a media slot: poster + play glyph at rest (frame-exact),
	/// the served video playing IN PLACE once tapped (user, 2026-08-23).
	@State private var playing = false

	var body: some View {
		let u = figmaUnit
		ZStack {
			Color(argb: 0xFFC4C4C4)
			if playing, case let .video(url, _, _) = media {
				// A failed OR FINISHED stream returns to the poster + glyph
				// instead of stranding a frame (user, 2026-08-25/26).
				NewsVideoPlayer(url: url, onDone: { playing = false })
			} else {
				let poster: String? = {
					switch media {
					case let .image(asset, _): return asset
					case let .video(_, asset, _): return asset
					}
				}()
				if let poster {
					Image(poster)
						.resizable()
						.scaledToFill()
						.frame(width: 407 * u, height: 271.18 * u)
						.clipped()
						.offset(x: -0.5 * u, y: 16.59 * u)
				}
				if case .video = media {
					Button { playing = true } label: {
						Image("IcHeroPlay")
							.resizable()
							.frame(width: 59.92 * u, height: 53.75 * u)
							.rotationEffect(.degrees(90))
					}
					.buttonStyle(.plain)
					.offset(x: -0.5 * u, y: 12.5 * u)
				}
			}
			Text(category)
				.font(StakFont.geist(10 * u, .medium))
				.foregroundStyle(StakColors.textPrimary)
				.padding(.horizontal, 7 * u)
				.padding(.vertical, 5 * u)
				.background(Color(argb: 0x40242B3D), in: RoundedRectangle(cornerRadius: 7.88 * u))
				.padding(.leading, 9 * u)
				.padding(.bottom, 10 * u)
				.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .bottomLeading)
			if saved {
				HStack(spacing: 4 * u) {
					Image("IcSavedBookmark")
						.resizable()
						.frame(width: 11 * u, height: 11 * u)
					Text("Saved to My STAK")
						.font(StakFont.geist(10 * u, .medium))
						.foregroundStyle(StakColors.textPrimary)
				}
				.padding(.horizontal, 7 * u)
				// Authored toast is 21 tall (1:1386): 13 text + 4/4 pads.
				.padding(.vertical, 4 * u)
				.background(Color(argb: 0x40242B3D), in: RoundedRectangle(cornerRadius: 7.88 * u))
				.padding(.top, 8 * u)
				.padding(.trailing, 7 * u)
				.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topTrailing)
			} else {
				Button(action: onBookmark) {
					Image("IcHeroBookmark")
						.resizable()
						.frame(width: 17.79 * u, height: 18.27 * u)
				}
				.buttonStyle(.plain)
				.padding(.top, 8 * u)
				.padding(.trailing, 11 * u)
				.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topTrailing)
			}
		}
		.frame(maxWidth: .infinity)
		.frame(height: 208 * u)
		// Authored radius 10 (1:1517 Inspect) - the earlier 24 was wrong.
		.clipShape(RoundedRectangle(cornerRadius: 10 * u))
		.padding(.horizontal, 15 * u)
	}
}

/// Templated per story - the authored sample is "Bloomberg · Jul 2 · 3 min read".
private struct Byline: View {
	let source: String
	let meta: String

	var body: some View {
		let u = figmaUnit
		HStack(spacing: 8 * u) {
			ZStack {
				Circle().fill(News.chipBg)
				Text(String(source.prefix(1)))
					.font(StakFont.sora(10 * u, .semiBold))
					.foregroundStyle(Color(argb: 0xFF9EADC7))
			}
			.frame(width: 24 * u, height: 24 * u)
			HStack(spacing: 5 * u) {
				Text(source)
					.font(StakFont.geist(12 * u, .medium))
					.foregroundStyle(StakColors.textPrimary)
				Text(meta)
					.font(StakFont.geist(12 * u))
					.foregroundStyle(News.faint)
			}
		}
		.padding(.vertical, 2 * u)
	}
}

/// 150x52 gradient CTA with the plus mark.
private struct AddToStakButton: View {
	let action: () -> Void

	var body: some View {
		let u = figmaUnit
		Button(action: action) {
			HStack(spacing: 8 * u) {
				Text("Add to STAK")
					.font(StakFont.geist(14 * u, .medium))
					.foregroundStyle(StakColors.textPrimary)
				Image("IcPlusSmall")
					.resizable()
					.frame(width: 14 * u, height: 14 * u)
			}
			.frame(width: 150 * u, height: 52 * u)
			.background(ctaGradient, in: RoundedRectangle(cornerRadius: 6 * u))
			.overlay(
				RoundedRectangle(cornerRadius: 6 * u)
					.strokeBorder(ctaBorder, lineWidth: 0.36 * u)
			)
		}
		.buttonStyle(.plain)
	}
}

/// 1-unit #2a3346 hairline (the Android Divider()).
private struct NewsHairline: View {
	var body: some View {
		let u = figmaUnit
		Rectangle()
			.fill(News.divider)
			.frame(height: 1 * u)
	}
}

/// The story's stock card - EVERY article renders it with its own
/// stock's served facts (user, 2026-08-25: "use the apple features...
/// replace the placeholder"). The sparkline stays the authored demo
/// asset until the backend serves chart data.
private struct StockCard: View {
	let saved: Bool
	let ticker: String
	let facts: NewsArticleFeed.StockFacts

	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 13 * u) {
			HStack(spacing: 0) {
				ZStack {
					Circle().fill(News.chipBg)
					Text(String(facts.name.prefix(1)))
						.font(StakFont.sora(18 * u, .semiBold))
						.foregroundStyle(Color(argb: 0xFF9EADC7))
				}
				.frame(width: 44 * u, height: 44 * u)
				Spacer().frame(width: 12 * u)
				VStack(alignment: .leading, spacing: 3 * u) {
					Text(facts.name)
						.font(StakFont.sora(15 * u, .semiBold))
						.foregroundStyle(StakColors.textPrimary)
					Text(ticker)
						.font(StakFont.geist(12 * u))
						.foregroundStyle(News.muted)
				}
				Spacer()
				HStack(spacing: 5 * u) {
					Text("Daily")
						.font(StakFont.geist(8 * u, .medium))
						.tracking(0.4 * u)
						.foregroundStyle(StakColors.textPrimary)
					Image("IcDailyChevron")
						.resizable()
						.frame(width: 6.53 * u, height: 3.56 * u)
				}
				.padding(.horizontal, 10 * u)
				.padding(.vertical, 4 * u)
				.background(Color(argb: 0x403E4958), in: RoundedRectangle(cornerRadius: 6 * u))
			}
			HStack(alignment: .bottom) {
				VStack(alignment: .leading, spacing: 3 * u) {
					Text(facts.price)
						.font(StakFont.sora(26 * u, .semiBold))
						.foregroundStyle(StakColors.textPrimary)
					Text(facts.change)
						.font(StakFont.geist(13 * u, .medium))
						// The app's authored up/down pair (green here, the
						// My STAK / Simulate red for down moves).
						.foregroundStyle(facts.up ? News.green : Color(argb: 0xFFFF5A6A))
				}
				Spacer()
				// The chart must agree with the move (user, 2026-08-25:
				// "showing red then the graph is green") - the down variant
				// is the authored line flipped + recolored to the app red.
				Image(facts.up ? "NewsSparkline" : "NewsSparklineDown")
					.resizable()
					.frame(width: 110 * u, height: 40 * u)
			}
			if saved {
				NewsHairline()
				HStack {
					Text("View \(ticker) in My STAK")
						.font(StakFont.geist(13 * u, .medium))
						.foregroundStyle(News.teal)
					Spacer()
					Image("IcDailyChevron")
						.resizable()
						.frame(width: 6.88 * u, height: 3.75 * u)
						.rotationEffect(.degrees(-90))
				}
			}
		}
		.frame(maxWidth: .infinity, alignment: .leading)
		.padding(.horizontal, 16 * u)
		.padding(.top, 16 * u)
		.padding(.bottom, 14 * u)
		.background(News.cardBg, in: RoundedRectangle(cornerRadius: 16 * u))
	}
}

/// "The gist" — sparkle header + three check bullets.
private struct GistCard: View {
	let bullets: [String]

	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 12 * u) {
			HStack(spacing: 8 * u) {
				Image("IcGistSparkle")
					.resizable()
					.frame(width: 18 * u, height: 18 * u)
				Text("The gist")
					.font(StakFont.sora(14 * u, .semiBold))
					.foregroundStyle(StakColors.textPrimary)
			}
			ForEach(bullets, id: \.self) { GistBullet(text: $0) }
		}
		.frame(maxWidth: .infinity, alignment: .leading)
		.padding(16 * u)
		.background(News.cardBg, in: RoundedRectangle(cornerRadius: 14 * u))
	}
}

private struct GistBullet: View {
	let text: String

	var body: some View {
		let u = figmaUnit
		HStack(alignment: .top, spacing: 10 * u) {
			Image("IcGistCheck")
				.resizable()
				.frame(width: 16 * u, height: 16 * u)
			Text(text)
				.font(StakFont.geist(13 * u))
				.lineSpacing((19 - 13) * u)
				.foregroundStyle(News.body)
				.frame(maxWidth: .infinity, alignment: .leading)
		}
	}
}

private struct Paragraph: View {
	let text: String
	var size: CGFloat = 14
	var line: CGFloat = 23

	var body: some View {
		let u = figmaUnit
		Text(text)
			.font(StakFont.geist(size * u))
			.lineSpacing((line - size) * u)
			.foregroundStyle(News.body)
			.frame(maxWidth: .infinity, alignment: .leading)
	}
}

private struct PullQuote: View {
	let text: String

	var body: some View {
		let u = figmaUnit
		HStack(spacing: 14 * u) {
			RoundedRectangle(cornerRadius: 2 * u)
				.fill(News.teal)
				.frame(width: 3 * u)
			Text(text)
				.font(StakFont.sora(16 * u, .semiBold))
				.lineSpacing((26 - 16) * u)
				.foregroundStyle(Color(argb: 0xFFD3D3DD))
				.frame(maxWidth: .infinity, alignment: .leading)
		}
		// IntrinsicSize.Min in Compose: the teal bar stretches to the
		// quote's own height, no further.
		.fixedSize(horizontal: false, vertical: true)
		.padding(.leading, 2 * u)
		.padding(.vertical, 6 * u)
	}
}

private struct NewToThisCard: View {
	let body_: String

	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 9 * u) {
			HStack(spacing: 8 * u) {
				Image("IcGistHelp")
					.resizable()
					.frame(width: 18 * u, height: 18 * u)
				Text("New to this?")
					.font(StakFont.sora(13 * u, .semiBold))
					.foregroundStyle(News.teal)
			}
			Text(body_)
				.font(StakFont.geist(13 * u))
				.lineSpacing((20 - 13) * u)
				.foregroundStyle(News.body)
		}
		.frame(maxWidth: .infinity, alignment: .leading)
		.padding(16 * u)
		.background(News.cardBg, in: RoundedRectangle(cornerRadius: 14 * u))
	}
}

private struct SourceRow: View {
	var body: some View {
		let u = figmaUnit
		HStack {
			Text("Source")
				.font(StakFont.sora(13 * u))
				.foregroundStyle(StakColors.textPrimary)
			Spacer()
			Image("IcNewsExternal")
				.resizable()
				.frame(width: 19 * u, height: 19 * u)
		}
		.frame(width: 81 * u)
	}
}

private struct KeyStatsCard: View {
	let facts: NewsArticleFeed.StockFacts

	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 13 * u) {
			HStack(spacing: 8 * u) {
				Image("IcGistInfo")
					.resizable()
					.frame(width: 18 * u, height: 18 * u)
				Text("Key stats")
					.font(StakFont.sora(14 * u, .semiBold))
					.foregroundStyle(StakColors.textPrimary)
			}
			StatRow(l1: "Market cap", v1: facts.marketCap, l2: "P/E ratio", v2: facts.peRatio)
			StatRow(l1: "Day range", v1: facts.dayRange, l2: "Volume", v2: facts.volume)
			StatRow(l1: "52-wk range", v1: facts.week52, l2: "Div yield", v2: facts.divYield)
		}
		.frame(maxWidth: .infinity, alignment: .leading)
		.padding(16 * u)
		.background(News.cardBg, in: RoundedRectangle(cornerRadius: 14 * u))
	}
}

private struct StatRow: View {
	let l1: String
	let v1: String
	let l2: String
	let v2: String

	var body: some View {
		let u = figmaUnit
		HStack(alignment: .top, spacing: 14 * u) {
			StatCell(label: l1, value: v1)
				.frame(width: 212 * u, alignment: .leading)
			StatCell(label: l2, value: v2)
		}
	}
}

private struct StatCell: View {
	let label: String
	let value: String

	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 3 * u) {
			Text(label)
				.font(StakFont.geist(11 * u))
				.foregroundStyle(News.faint)
			Text(value)
				.font(StakFont.sora(13 * u, .semiBold))
				.foregroundStyle(StakColors.textPrimary)
		}
	}
}

/// rgba(105,179,202,0.1) r12 tag chip.
private struct ArticleTag: View {
	let text: String

	var body: some View {
		let u = figmaUnit
		Text(text)
			.font(StakFont.geist(11 * u, .medium))
			.foregroundStyle(News.muted)
			.padding(.horizontal, 11 * u)
			.padding(.vertical, 5 * u)
			.background(Color(argb: 0x1A69B3CA), in: RoundedRectangle(cornerRadius: 12 * u))
	}
}

/// READ NEXT - the authored sample repeats the Tesla row twice as its
/// placeholder; the served rows are two other stories, and each opens
/// its own article (user, 2026-08-25).
private struct ReadNext: View {
	let currentId: String
	let onOpen: (String) -> Void

	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 10 * u) {
			Text("READ NEXT")
				.font(StakFont.geist(10 * u, .medium))
				.tracking(0.5 * u)
				.foregroundStyle(News.muted)
			ForEach(NewsArticleFeed.readNext(excluding: currentId), id: \.id) { next in
				Button {
					onOpen(next.id)
				} label: {
					HStack(spacing: 12 * u) {
						if let thumb = next.thumb {
							Image(thumb)
								.resizable()
								.scaledToFill()
								.frame(width: 60 * u, height: 60 * u)
								.clipShape(RoundedRectangle(cornerRadius: 10 * u))
						}
						VStack(alignment: .leading, spacing: 5 * u) {
							Text("\(next.source) · \(next.age)")
								.font(StakFont.geist(11 * u))
								.foregroundStyle(News.muted)
							Text(next.headline)
								.font(StakFont.sora(14 * u))
								.lineSpacing((19 - 14) * u)
								.foregroundStyle(StakColors.textPrimary)
						}
						.frame(maxWidth: .infinity, alignment: .leading)
					}
					.padding(12 * u)
					.background(News.cardBg, in: RoundedRectangle(cornerRadius: 14 * u))
				}
				.buttonStyle(.plain)
			}
		}
		.padding(.top, 6 * u)
	}
}

/// Save success — rgba(12,19,32,0.55) scrim + the r24 #181f30 bottom
/// sheet (101:1169). The stock row shows the SAVED stock's served facts
/// (user, 2026-08-25: every article = the full Apple page with the
/// story's own content; the frame's Apple row was placeholder).
private struct SaveSuccessOverlay: View {
	let facts: NewsArticleFeed.StockFacts
	let onViewInMyStak: () -> Void
	let onDismiss: () -> Void

	var body: some View {
		let u = figmaUnit
		ZStack(alignment: .bottom) {
			// Authored scrim rgba(12,19,32,0.55) (101:1168); it has NO
			// prototype connection - tapping it does not dismiss.
			Color(argb: 0x8C0C1320)
				.ignoresSafeArea()
			VStack(spacing: 14 * u) {
				RoundedRectangle(cornerRadius: 2 * u)
					.fill(News.divider)
					.frame(width: 40 * u, height: 4 * u)
					.padding(.bottom, 4 * u)
				VStack(spacing: 14 * u) {
					Image("IcSheetCheck")
						.resizable()
						.frame(width: 47 * u, height: 47 * u)
					Text("Saved to My STAK")
						.font(StakFont.sora(18 * u, .semiBold))
						.foregroundStyle(StakColors.textPrimary)
				}
				HStack(spacing: 11 * u) {
					ZStack {
						Circle().fill(News.chipBg)
						Text(String(facts.shortName.prefix(1)))
							.font(StakFont.sora(15 * u, .semiBold))
							.foregroundStyle(Color(argb: 0xFF9EADC7))
					}
					.frame(width: 38 * u, height: 38 * u)
					VStack(alignment: .leading, spacing: 2 * u) {
						Text(facts.shortName)
							.font(StakFont.geist(13 * u, .medium))
							.foregroundStyle(StakColors.textPrimary)
						Text("\(facts.price) today")
							.font(StakFont.geist(10 * u))
							.foregroundStyle(News.muted)
					}
					.frame(maxWidth: .infinity, alignment: .leading)
					// "+4.84% today" -> "▲ 4.84%" (the sheet's authored format).
					Text("\(facts.up ? "▲" : "▼") \(String(facts.change.dropFirst()).replacingOccurrences(of: " today", with: ""))")
						.font(StakFont.geist(12 * u, .medium))
						.foregroundStyle(facts.up ? News.green : Color(argb: 0xFFFF5A6A))
				}
				.padding(.horizontal, 14 * u)
				.padding(.vertical, 12 * u)
				.background(Color(argb: 0x1A69B3CA), in: RoundedRectangle(cornerRadius: 6 * u))
				Text("Watching from today · no money committed")
					.font(StakFont.geist(12 * u))
					.lineSpacing((18 - 12) * u)
					.foregroundStyle(News.body)
					.frame(maxWidth: .infinity, alignment: .leading)
				VStack(spacing: 16 * u) {
					Button(action: onViewInMyStak) {
						Text("View in My STAK")
							.font(StakFont.geist(14 * u, .medium))
							.foregroundStyle(StakColors.textPrimary)
							.frame(maxWidth: .infinity)
							.frame(height: 52 * u)
							.background(ctaGradient, in: RoundedRectangle(cornerRadius: 6 * u))
							.overlay(
								RoundedRectangle(cornerRadius: 6 * u)
									.strokeBorder(ctaBorder, lineWidth: 0.36 * u)
							)
					}
					.buttonStyle(.plain)
					Button(action: onDismiss) {
						Text("Back")
							.font(StakFont.sora(14 * u))
							.foregroundStyle(News.muted)
							.frame(maxWidth: .infinity)
							.frame(height: 52 * u)
							.overlay(
								RoundedRectangle(cornerRadius: 6 * u)
									.strokeBorder(Color(argb: 0x54343B4F), lineWidth: 0.36 * u)
							)
							.contentShape(Rectangle())
					}
					.buttonStyle(.plain)
				}
			}
			.padding(.horizontal, 20 * u)
			.padding(.top, 10 * u)
			// Authored sheet is 388 tall - its 30 bottom pad INCLUDES the
			// home-indicator zone (101:1169), so the content ignores the
			// bottom safe area and pads to the physical screen bottom.
			.padding(.bottom, 30 * u)
			.frame(maxWidth: .infinity)
			.background {
				UnevenRoundedRectangle(topLeadingRadius: 24 * u, bottomLeadingRadius: 0, bottomTrailingRadius: 0, topTrailingRadius: 24 * u, style: .circular)
					.fill(News.cardBg)
			}
			.ignoresSafeArea(edges: .bottom)
		}
	}
}

/// Plays the served video inside the hero box: YouTube links through the
/// embeddable player (WKWebView), any other link through AVKit. Both
/// autoplay once the user tapped the play glyph.
private struct NewsVideoPlayer: View {
	let url: String
	var onDone: () -> Void = {}

	var body: some View {
		if let embed = NewsMedia.youTubeEmbedURL(for: url) {
			YouTubeEmbedView(url: embed)
		} else if let direct = URL(string: url) {
			AutoplayVideoPlayer(url: direct, onDone: onDone)
		}
	}
}

private struct AutoplayVideoPlayer: View {
	let url: URL
	var onDone: () -> Void = {}
	@State private var player: AVPlayer? = nil

	var body: some View {
		VideoPlayer(player: player)
			.onAppear {
				let p = AVPlayer(url: url)
				player = p
				p.play()
			}
			.onDisappear { player?.pause() }
			// A finished or failed clip returns the hero to its poster +
			// play glyph (user, 2026-08-26: "i cant see the play icon").
			.onReceive(NotificationCenter.default.publisher(for: .AVPlayerItemDidPlayToEndTime)) { _ in onDone() }
			.onReceive(NotificationCenter.default.publisher(for: .AVPlayerItemFailedToPlayToEndTime)) { _ in onDone() }
	}
}

private struct YouTubeEmbedView: UIViewRepresentable {
	let url: URL

	func makeUIView(context: Context) -> WKWebView {
		let config = WKWebViewConfiguration()
		config.allowsInlineMediaPlayback = true
		config.mediaTypesRequiringUserActionForPlayback = []
		let view = WKWebView(frame: .zero, configuration: config)
		view.isOpaque = false
		view.scrollView.isScrollEnabled = false
		// YouTube's player refuses embeds with no HTTP Referer - a bare
		// URLRequest load dies with "configuration error 153" (user,
		// 2026-08-25; reproduced + fix verified in the browser). The
		// iframe wrapper + base URL presents an embedding origin.
		let html = """
		<!doctype html><html><head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<style>html,body{margin:0;padding:0;background:#000;height:100%;overflow:hidden}iframe{position:absolute;top:0;left:0;width:100%;height:100%;border:0}</style>
		</head><body>
		<iframe src="\(url.absoluteString)" allow="autoplay; encrypted-media; picture-in-picture" allowfullscreen></iframe>
		</body></html>
		"""
		view.loadHTMLString(html, baseURL: URL(string: "https://stak.app"))
		return view
	}

	func updateUIView(_ uiView: WKWebView, context: Context) {}
}
