import SwiftUI

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
private let ctaBorder = Color(argb: 0xA1659EAD)

/// 03 · News — the article page in its three frames: "News detail page
/// unsaved" (1:1495), "News detail · Save success" (101:1005, the bottom
/// sheet over a 45% #0a1020 scrim) and "News detail page saved" (1:1359 —
/// hero toast, View-in-My-STAK row on the stock card, Apple + Tech tags).
/// Mirrors android/ NewsDetailScreen.kt. Every metric is scaled by the
/// 390pt artboard unit (`figmaUnit`), exactly like the Android build.
struct NewsDetailView: View {
	let onBack: () -> Void

	@State private var saved = false
	@State private var showSuccess = false

	var body: some View {
		let u = figmaUnit
		ZStack {
			VStack(spacing: 0) {
				// Fixed top bar — back circle + share.
				HStack {
					AuthBackCircle(action: onBack)
					Spacer()
					Image("IcNewsShare")
						.resizable()
						.frame(width: 24 * u, height: 24 * u)
						.accessibilityLabel("Share")
				}
				.padding(.leading, 16 * u)
				.padding(.trailing, 18 * u)
				.padding(.top, 10 * u)
				.padding(.bottom, 12 * u)

				ScrollView {
					VStack(spacing: 0) {
						HeroImage(saved: saved)
						VStack(alignment: .leading, spacing: 15 * u) {
							Text("Apple climbs 5% on foldable iPhone push")
								// RENDER-measured 20sp (the metadata's 24 lied); lh32 box stands.
								.font(StakFont.sora(20 * u, .semiBold))
								.lineSpacing((32 - 20) * u)
								.foregroundStyle(StakColors.textPrimary)
							Text("A bigger foldable order and the widest iPhone lineup in years sent Apple toward a record, and to within touching distance of Nvidia’s crown.")
								// 14.3 keeps the authored line-1 break after "lineup".
								.font(StakFont.geist(14.3 * u))
								.lineSpacing((22 - 14) * u)
								.foregroundStyle(News.muted)
							Byline()
							if !saved {
								AddToStakButton { showSuccess = true }
							}
							NewsHairline()
							StockCard(saved: saved)
							GistCard()
							Paragraph(
								text: "Apple had one of its best days in months on Thursday, climbing almost 5 percent after word got out that the company is planning its widest iPhone lineup in years. Nikkei Asia reported that Apple has asked suppliers to prepare at least five new models, and to lift output of its first foldable to around 10 million units, well above the seven to eight million it had penciled in.",
								size: 15, line: 24
							)
							Paragraph(
								text: "That last number is the tell. Companies do not quietly double down on a product they expect to flop, and the foldable, which the rumor mill has taken to calling the iPhone Ultra, is now expected to land between late 2026 and the first half of 2027. Traders read the order size as confidence and bought accordingly. Apple gained about 182 billion dollars in market value on the day, nearly enough on its own to paper over a sell-off tearing through chip stocks."
							)
							PullQuote()
							NewToThisCard()
							Paragraph(
								text: "The rally leaves Apple roughly 4 percent shy of retaking the title of most valuable company in the world from Nvidia, a crown the two have passed back and forth all year. It also lets the stock shake off a rough June, when a rare mid-cycle price increase on Macs and iPads, blamed on climbing memory costs, sent shares lower and rattled investors who had grown used to Apple holding the line."
							)
							Paragraph(
								text: "The real verdict comes on July 30, when Apple reports fiscal third quarter results. Wall Street is penciling in revenue of around 108 billion dollars, but the number everyone will hunt for is any early read on how the new lineup, and its price tags, are actually selling."
							)
							SourceRow()
							KeyStatsCard()
							NewsHairline()
							HStack(spacing: 8 * u) {
								ArticleTag(text: "Apple")
								if saved {
									ArticleTag(text: "Tech")
								}
							}
							ReadNext()
						}
						.frame(maxWidth: .infinity, alignment: .leading)
						.padding(.horizontal, 20 * u)
						.padding(.top, 22 * u)
						.padding(.bottom, 28 * u)
					}
				}
			}
			if showSuccess {
				SaveSuccessOverlay(
					onViewInMyStak: { showSuccess = false; saved = true },
					onDismiss: { showSuccess = false; saved = true }
				)
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}

/// 360x208 r24 hero — phone art, Tech & Ai toast, play badge, bookmark/saved
/// chip. The authored image is oversized (407x271.18 in the 360x208 card,
/// top-left at -24,-15) — it keeps its exact authored frame (the Compose
/// requiredSize + Crop becomes .scaledToFill + explicit .frame + .clipped)
/// and the card's rounded clip crops the overflow.
private struct HeroImage: View {
	let saved: Bool

	var body: some View {
		let u = figmaUnit
		ZStack {
			Color(argb: 0xFFC4C4C4)
			Image("NewsHeroPhone")
				.resizable()
				.scaledToFill()
				.frame(width: 407 * u, height: 271.18 * u)
				.clipped()
				.offset(x: -0.5 * u, y: 16.59 * u)
			Image("IcHeroPlay")
				.resizable()
				.frame(width: 59.92 * u, height: 53.75 * u)
				.rotationEffect(.degrees(90))
				.offset(x: -0.5 * u, y: 12.5 * u)
			Text("Tech & Ai")
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
				.padding(.vertical, 5 * u)
				.background(Color(argb: 0x40242B3D), in: RoundedRectangle(cornerRadius: 7.88 * u))
				.padding(.top, 8 * u)
				.padding(.trailing, 7 * u)
				.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topTrailing)
			} else {
				Image("IcHeroBookmark")
					.resizable()
					.frame(width: 17.79 * u, height: 18.27 * u)
					.padding(.top, 8 * u)
					.padding(.trailing, 11 * u)
					.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topTrailing)
			}
		}
		.frame(maxWidth: .infinity)
		.frame(height: 208 * u)
		.clipShape(RoundedRectangle(cornerRadius: 24 * u))
		.padding(.horizontal, 15 * u)
	}
}

private struct Byline: View {
	var body: some View {
		let u = figmaUnit
		HStack(spacing: 8 * u) {
			ZStack {
				Circle().fill(News.chipBg)
				Text("B")
					.font(StakFont.sora(10 * u, .semiBold))
					.foregroundStyle(Color(argb: 0xFF9EADC7))
			}
			.frame(width: 24 * u, height: 24 * u)
			HStack(spacing: 5 * u) {
				Text("Bloomberg")
					.font(StakFont.geist(12 * u, .medium))
					.foregroundStyle(StakColors.textPrimary)
				Text("· Jul 2 · 3 min read")
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

/// AAPL price card — badge, Daily chip, $308.63 + sparkline; saved adds View row.
private struct StockCard: View {
	let saved: Bool

	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 13 * u) {
			HStack(spacing: 0) {
				ZStack {
					Circle().fill(News.chipBg)
					Text("A")
						.font(StakFont.sora(18 * u, .semiBold))
						.foregroundStyle(Color(argb: 0xFF9EADC7))
				}
				.frame(width: 44 * u, height: 44 * u)
				Spacer().frame(width: 12 * u)
				VStack(alignment: .leading, spacing: 3 * u) {
					Text("Apple Inc.")
						.font(StakFont.sora(15 * u, .semiBold))
						.foregroundStyle(StakColors.textPrimary)
					Text("AAPL")
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
					Text("$308.63")
						.font(StakFont.sora(26 * u, .semiBold))
						.foregroundStyle(StakColors.textPrimary)
					Text("+4.84% today")
						.font(StakFont.geist(13 * u, .medium))
						.foregroundStyle(News.green)
				}
				Spacer()
				Image("NewsSparkline")
					.resizable()
					.frame(width: 110 * u, height: 40 * u)
			}
			if saved {
				NewsHairline()
				HStack {
					Text("View AAPL in My STAK")
						.font(StakFont.geist(13 * u, .medium))
						.foregroundStyle(News.teal)
					Spacer()
					Image("IcDailyChevron")
						.resizable()
						.frame(width: 6.88 * u, height: 3.75 * u)
						.rotationEffect(.degrees(-90))
				}
				.padding(.top, 1 * u)
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
			GistBullet(text: "Apple rose about 5% on plans for its widest iPhone lineup yet.")
			GistBullet(text: "It raised foldable orders to 10 million units, a show of confidence.")
			GistBullet(text: "The stock sits about 4% from passing Nvidia as the most valuable company.")
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
	var body: some View {
		let u = figmaUnit
		HStack(spacing: 14 * u) {
			RoundedRectangle(cornerRadius: 2 * u)
				.fill(News.teal)
				.frame(width: 3 * u)
			Text("Companies do not quietly double down on a product they expect to flop.")
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
			Text("A foldable phone opens out into a small tablet. For Apple it means a pricier device to sell, and a way to win back buyers who drifted to Samsung, which has offered foldables for years.")
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
			StatRow(l1: "Market cap", v1: "$4.58T", l2: "P/E ratio", v2: "34.2")
			StatRow(l1: "Day range", v1: "$301.20–$309.80", l2: "Volume", v2: "82.4M")
			StatRow(l1: "52-wk range", v1: "$201.50–$317.40", l2: "Div yield", v2: "0.42%")
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

private struct ReadNext: View {
	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 10 * u) {
			Text("READ NEXT")
				.font(StakFont.geist(10 * u, .medium))
				.tracking(0.5 * u)
				.foregroundStyle(News.muted)
			ForEach(0..<2, id: \.self) { _ in
				HStack(spacing: 12 * u) {
					Image("NewsThumbTSLARn")
						.resizable()
						.scaledToFill()
						.frame(width: 60 * u, height: 60 * u)
						.clipShape(RoundedRectangle(cornerRadius: 10 * u))
					VStack(alignment: .leading, spacing: 5 * u) {
						Text("CNBC · 2d")
							.font(StakFont.geist(11 * u))
							.foregroundStyle(News.muted)
						Text("Tesla drops 7% even after beating deliveries")
							.font(StakFont.sora(14 * u))
							.lineSpacing((19 - 14) * u)
							.foregroundStyle(StakColors.textPrimary)
					}
					.frame(maxWidth: .infinity, alignment: .leading)
				}
				.padding(12 * u)
				.background(News.cardBg, in: RoundedRectangle(cornerRadius: 14 * u))
			}
		}
		.padding(.top, 6 * u)
	}
}

/// Save success — #0a1020 scrim at ~45% + the r24 #181f30 bottom sheet (101:1169).
private struct SaveSuccessOverlay: View {
	let onViewInMyStak: () -> Void
	let onDismiss: () -> Void

	var body: some View {
		let u = figmaUnit
		ZStack(alignment: .bottom) {
			Color(argb: 0x730A1020)
				.ignoresSafeArea()
				.onTapGesture(perform: onDismiss)
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
						Text("A")
							.font(StakFont.sora(15 * u, .semiBold))
							.foregroundStyle(Color(argb: 0xFF9EADC7))
					}
					.frame(width: 38 * u, height: 38 * u)
					VStack(alignment: .leading, spacing: 2 * u) {
						Text("Apple")
							.font(StakFont.geist(13 * u, .medium))
							.foregroundStyle(StakColors.textPrimary)
						Text("$229.35 today")
							.font(StakFont.geist(10 * u))
							.foregroundStyle(News.muted)
					}
					.frame(maxWidth: .infinity, alignment: .leading)
					Text("▲ 1.2%")
						.font(StakFont.geist(12 * u, .medium))
						.foregroundStyle(News.green)
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
