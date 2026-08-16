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
/// Mirrors android/ NewsDetailScreen.kt.
struct NewsDetailView: View {
	let onBack: () -> Void

	@State private var saved = false
	@State private var showSuccess = false

	var body: some View {
		ZStack {
			VStack(spacing: 0) {
				// Fixed top bar — back circle + share.
				HStack {
					AuthBackCircle(action: onBack)
					Spacer()
					Image("IcNewsShare")
						.resizable()
						.frame(width: 24, height: 24)
						.accessibilityLabel("Share")
				}
				.padding(.leading, 16)
				.padding(.trailing, 18)
				.padding(.top, 10)
				.padding(.bottom, 12)

				ScrollView {
					VStack(spacing: 0) {
						HeroImage(saved: saved)
						VStack(alignment: .leading, spacing: 15) {
							Text("Apple climbs 5% on foldable iPhone push")
								.font(StakFont.sora(20, .semiBold))
								.lineSpacing(32 - 20)
								.foregroundStyle(StakColors.textPrimary)
							Text("A bigger foldable order and the widest iPhone lineup in years sent Apple toward a record, and to within touching distance of Nvidia’s crown.")
								.font(StakFont.geist(14))
								.lineSpacing(22 - 14)
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
							HStack(spacing: 8) {
								ArticleTag(text: "Apple")
								if saved {
									ArticleTag(text: "Tech")
								}
							}
							ReadNext()
						}
						.frame(maxWidth: .infinity, alignment: .leading)
						.padding(.horizontal, 20)
						.padding(.top, 22)
						.padding(.bottom, 28)
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

/// 360x208 r10 hero — phone art, Tech & Ai toast, play badge, bookmark/saved
/// chip. The oversized phone render is centered then offset and clipped by
/// the card, exactly like the Compose overflow draw.
private struct HeroImage: View {
	let saved: Bool

	var body: some View {
		ZStack {
			Color(argb: 0xFFC4C4C4)
			Image("NewsHeroPhone")
				.resizable()
				.frame(width: 407, height: 271.18)
				.offset(x: -0.5, y: 16.59)
			Image("IcHeroPlay")
				.resizable()
				.frame(width: 59.92, height: 53.75)
				.rotationEffect(.degrees(90))
				.offset(x: -0.5, y: 12.5)
			Text("Tech & Ai")
				.font(StakFont.geist(10, .medium))
				.foregroundStyle(StakColors.textPrimary)
				.padding(.horizontal, 7)
				.padding(.vertical, 5)
				.background(Color(argb: 0x40242B3D), in: RoundedRectangle(cornerRadius: 7.88))
				.padding(.leading, 9)
				.padding(.bottom, 10)
				.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .bottomLeading)
			if saved {
				HStack(spacing: 4) {
					Image("IcSavedBookmark")
						.resizable()
						.frame(width: 11, height: 11)
					Text("Saved to My STAK")
						.font(StakFont.geist(10, .medium))
						.foregroundStyle(StakColors.textPrimary)
				}
				.padding(.horizontal, 7)
				.padding(.vertical, 5)
				.background(Color(argb: 0x40242B3D), in: RoundedRectangle(cornerRadius: 7.88))
				.padding(.top, 8)
				.padding(.trailing, 7)
				.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topTrailing)
			} else {
				Image("IcHeroBookmark")
					.resizable()
					.frame(width: 17.79, height: 18.27)
					.padding(.top, 8)
					.padding(.trailing, 11)
					.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topTrailing)
			}
		}
		.frame(maxWidth: .infinity)
		.frame(height: 208)
		.clipShape(RoundedRectangle(cornerRadius: 10))
		.padding(.horizontal, 15)
	}
}

private struct Byline: View {
	var body: some View {
		HStack(spacing: 8) {
			ZStack {
				Circle().fill(News.chipBg)
				Text("B")
					.font(StakFont.sora(10, .semiBold))
					.foregroundStyle(Color(argb: 0xFF9EADC7))
			}
			.frame(width: 24, height: 24)
			HStack(spacing: 5) {
				Text("Bloomberg")
					.font(StakFont.geist(12, .medium))
					.foregroundStyle(StakColors.textPrimary)
				Text("· Jul 2 · 3 min read")
					.font(StakFont.geist(12))
					.foregroundStyle(News.faint)
			}
		}
		.padding(.vertical, 2)
	}
}

/// 150x52 gradient CTA with the plus mark.
private struct AddToStakButton: View {
	let action: () -> Void

	var body: some View {
		Button(action: action) {
			HStack(spacing: 8) {
				Text("Add to STAK")
					.font(StakFont.geist(14, .medium))
					.foregroundStyle(StakColors.textPrimary)
				Image("IcPlusSmall")
					.resizable()
					.frame(width: 14, height: 14)
			}
			.frame(width: 150, height: 52)
			.background(ctaGradient, in: RoundedRectangle(cornerRadius: 6))
			.overlay(
				RoundedRectangle(cornerRadius: 6)
					.strokeBorder(ctaBorder, lineWidth: 0.36)
			)
		}
		.buttonStyle(.plain)
	}
}

/// 1pt #2a3346 hairline (the Android Divider()).
private struct NewsHairline: View {
	var body: some View {
		Rectangle()
			.fill(News.divider)
			.frame(height: 1)
	}
}

/// AAPL price card — badge, Daily chip, $308.63 + sparkline; saved adds View row.
private struct StockCard: View {
	let saved: Bool

	var body: some View {
		VStack(alignment: .leading, spacing: 13) {
			HStack(spacing: 0) {
				ZStack {
					Circle().fill(News.chipBg)
					Text("A")
						.font(StakFont.sora(18, .semiBold))
						.foregroundStyle(Color(argb: 0xFF9EADC7))
				}
				.frame(width: 44, height: 44)
				Spacer().frame(width: 12)
				VStack(alignment: .leading, spacing: 3) {
					Text("Apple Inc.")
						.font(StakFont.sora(15, .semiBold))
						.foregroundStyle(StakColors.textPrimary)
					Text("AAPL")
						.font(StakFont.geist(12))
						.foregroundStyle(News.muted)
				}
				Spacer()
				HStack(spacing: 5) {
					Text("Daily")
						.font(StakFont.geist(8, .medium))
						.tracking(0.4)
						.foregroundStyle(StakColors.textPrimary)
					Image("IcDailyChevron")
						.resizable()
						.frame(width: 6.53, height: 3.56)
				}
				.padding(.horizontal, 10)
				.padding(.vertical, 4)
				.background(Color(argb: 0x403E4958), in: RoundedRectangle(cornerRadius: 6))
			}
			HStack(alignment: .bottom) {
				VStack(alignment: .leading, spacing: 3) {
					Text("$308.63")
						.font(StakFont.sora(26, .semiBold))
						.foregroundStyle(StakColors.textPrimary)
					Text("+4.84% today")
						.font(StakFont.geist(13, .medium))
						.foregroundStyle(News.green)
				}
				Spacer()
				Image("NewsSparkline")
					.resizable()
					.frame(width: 110, height: 40)
			}
			if saved {
				NewsHairline()
				HStack {
					Text("View AAPL in My STAK")
						.font(StakFont.geist(13, .medium))
						.foregroundStyle(News.teal)
					Spacer()
					Image("IcDailyChevron")
						.resizable()
						.frame(width: 6.88, height: 3.75)
						.rotationEffect(.degrees(-90))
				}
				.padding(.top, 1)
			}
		}
		.frame(maxWidth: .infinity, alignment: .leading)
		.padding(.horizontal, 16)
		.padding(.top, 16)
		.padding(.bottom, 14)
		.background(News.cardBg, in: RoundedRectangle(cornerRadius: 16))
	}
}

/// "The gist" — sparkle header + three check bullets.
private struct GistCard: View {
	var body: some View {
		VStack(alignment: .leading, spacing: 12) {
			HStack(spacing: 8) {
				Image("IcGistSparkle")
					.resizable()
					.frame(width: 18, height: 18)
				Text("The gist")
					.font(StakFont.sora(14, .semiBold))
					.foregroundStyle(StakColors.textPrimary)
			}
			GistBullet(text: "Apple rose about 5% on plans for its widest iPhone lineup yet.")
			GistBullet(text: "It raised foldable orders to 10 million units, a show of confidence.")
			GistBullet(text: "The stock sits about 4% from passing Nvidia as the most valuable company.")
		}
		.frame(maxWidth: .infinity, alignment: .leading)
		.padding(16)
		.background(News.cardBg, in: RoundedRectangle(cornerRadius: 14))
	}
}

private struct GistBullet: View {
	let text: String

	var body: some View {
		HStack(alignment: .top, spacing: 10) {
			Image("IcGistCheck")
				.resizable()
				.frame(width: 16, height: 16)
			Text(text)
				.font(StakFont.geist(13))
				.lineSpacing(19 - 13)
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
		Text(text)
			.font(StakFont.geist(size))
			.lineSpacing(line - size)
			.foregroundStyle(News.body)
			.frame(maxWidth: .infinity, alignment: .leading)
	}
}

private struct PullQuote: View {
	var body: some View {
		HStack(spacing: 14) {
			RoundedRectangle(cornerRadius: 2)
				.fill(News.teal)
				.frame(width: 3)
			Text("Companies do not quietly double down on a product they expect to flop.")
				.font(StakFont.sora(16, .semiBold))
				.lineSpacing(26 - 16)
				.foregroundStyle(Color(argb: 0xFFD3D3DD))
				.frame(maxWidth: .infinity, alignment: .leading)
		}
		// IntrinsicSize.Min in Compose: the teal bar stretches to the
		// quote's own height, no further.
		.fixedSize(horizontal: false, vertical: true)
		.padding(.leading, 2)
		.padding(.vertical, 6)
	}
}

private struct NewToThisCard: View {
	var body: some View {
		VStack(alignment: .leading, spacing: 9) {
			HStack(spacing: 8) {
				Image("IcGistHelp")
					.resizable()
					.frame(width: 18, height: 18)
				Text("New to this?")
					.font(StakFont.sora(13, .semiBold))
					.foregroundStyle(News.teal)
			}
			Text("A foldable phone opens out into a small tablet. For Apple it means a pricier device to sell, and a way to win back buyers who drifted to Samsung, which has offered foldables for years.")
				.font(StakFont.geist(13))
				.lineSpacing(20 - 13)
				.foregroundStyle(News.body)
		}
		.frame(maxWidth: .infinity, alignment: .leading)
		.padding(16)
		.background(News.cardBg, in: RoundedRectangle(cornerRadius: 14))
	}
}

private struct SourceRow: View {
	var body: some View {
		HStack {
			Text("Source")
				.font(StakFont.sora(13))
				.foregroundStyle(StakColors.textPrimary)
			Spacer()
			Image("IcNewsExternal")
				.resizable()
				.frame(width: 19, height: 19)
		}
		.frame(width: 81)
	}
}

private struct KeyStatsCard: View {
	var body: some View {
		VStack(alignment: .leading, spacing: 13) {
			HStack(spacing: 8) {
				Image("IcGistInfo")
					.resizable()
					.frame(width: 18, height: 18)
				Text("Key stats")
					.font(StakFont.sora(14, .semiBold))
					.foregroundStyle(StakColors.textPrimary)
			}
			StatRow(l1: "Market cap", v1: "$4.58T", l2: "P/E ratio", v2: "34.2")
			StatRow(l1: "Day range", v1: "$301.20–$309.80", l2: "Volume", v2: "82.4M")
			StatRow(l1: "52-wk range", v1: "$201.50–$317.40", l2: "Div yield", v2: "0.42%")
		}
		.frame(maxWidth: .infinity, alignment: .leading)
		.padding(16)
		.background(News.cardBg, in: RoundedRectangle(cornerRadius: 14))
	}
}

private struct StatRow: View {
	let l1: String
	let v1: String
	let l2: String
	let v2: String

	var body: some View {
		HStack(alignment: .top, spacing: 14) {
			StatCell(label: l1, value: v1)
				.frame(width: 212, alignment: .leading)
			StatCell(label: l2, value: v2)
		}
	}
}

private struct StatCell: View {
	let label: String
	let value: String

	var body: some View {
		VStack(alignment: .leading, spacing: 3) {
			Text(label)
				.font(StakFont.geist(11))
				.foregroundStyle(News.faint)
			Text(value)
				.font(StakFont.sora(13, .semiBold))
				.foregroundStyle(StakColors.textPrimary)
		}
	}
}

/// rgba(105,179,202,0.1) r12 tag chip.
private struct ArticleTag: View {
	let text: String

	var body: some View {
		Text(text)
			.font(StakFont.geist(11, .medium))
			.foregroundStyle(News.muted)
			.padding(.horizontal, 11)
			.padding(.vertical, 5)
			.background(Color(argb: 0x1A69B3CA), in: RoundedRectangle(cornerRadius: 12))
	}
}

private struct ReadNext: View {
	var body: some View {
		VStack(alignment: .leading, spacing: 10) {
			Text("READ NEXT")
				.font(StakFont.geist(10, .medium))
				.tracking(0.5)
				.foregroundStyle(News.muted)
			ForEach(0..<2, id: \.self) { _ in
				HStack(spacing: 12) {
					Image("NewsThumbTSLARn")
						.resizable()
						.scaledToFill()
						.frame(width: 60, height: 60)
						.clipShape(RoundedRectangle(cornerRadius: 10))
					VStack(alignment: .leading, spacing: 5) {
						Text("CNBC · 2d")
							.font(StakFont.geist(11))
							.foregroundStyle(News.muted)
						Text("Tesla drops 7% even after beating deliveries")
							.font(StakFont.sora(14))
							.lineSpacing(19 - 14)
							.foregroundStyle(StakColors.textPrimary)
					}
					.frame(maxWidth: .infinity, alignment: .leading)
				}
				.padding(12)
				.background(News.cardBg, in: RoundedRectangle(cornerRadius: 14))
			}
		}
		.padding(.top, 6)
	}
}

/// Save success — #0a1020 scrim at 45% + the r24 #181f30 bottom sheet (101:1169).
private struct SaveSuccessOverlay: View {
	let onViewInMyStak: () -> Void
	let onDismiss: () -> Void

	var body: some View {
		ZStack(alignment: .bottom) {
			Color(argb: 0x730A1020)
				.ignoresSafeArea()
				.onTapGesture(perform: onDismiss)
			VStack(spacing: 14) {
				RoundedRectangle(cornerRadius: 2)
					.fill(News.divider)
					.frame(width: 40, height: 4)
					.padding(.bottom, 4)
				VStack(spacing: 14) {
					Image("IcSheetCheck")
						.resizable()
						.frame(width: 47, height: 47)
					Text("Saved to My STAK")
						.font(StakFont.sora(18, .semiBold))
						.foregroundStyle(StakColors.textPrimary)
				}
				HStack(spacing: 11) {
					ZStack {
						Circle().fill(News.chipBg)
						Text("A")
							.font(StakFont.sora(15, .semiBold))
							.foregroundStyle(Color(argb: 0xFF9EADC7))
					}
					.frame(width: 38, height: 38)
					VStack(alignment: .leading, spacing: 2) {
						Text("Apple")
							.font(StakFont.geist(13, .medium))
							.foregroundStyle(StakColors.textPrimary)
						Text("$229.35 today")
							.font(StakFont.geist(10))
							.foregroundStyle(News.muted)
					}
					.frame(maxWidth: .infinity, alignment: .leading)
					Text("▲ 1.2%")
						.font(StakFont.geist(12, .medium))
						.foregroundStyle(News.green)
				}
				.padding(.horizontal, 14)
				.padding(.vertical, 12)
				.background(Color(argb: 0x1A69B3CA), in: RoundedRectangle(cornerRadius: 6))
				Text("Watching from today · no money committed")
					.font(StakFont.geist(12))
					.lineSpacing(18 - 12)
					.foregroundStyle(News.body)
					.frame(maxWidth: .infinity, alignment: .leading)
				VStack(spacing: 16) {
					Button(action: onViewInMyStak) {
						Text("View in My STAK")
							.font(StakFont.geist(14, .medium))
							.foregroundStyle(StakColors.textPrimary)
							.frame(maxWidth: .infinity)
							.frame(height: 52)
							.background(ctaGradient, in: RoundedRectangle(cornerRadius: 6))
							.overlay(
								RoundedRectangle(cornerRadius: 6)
									.strokeBorder(ctaBorder, lineWidth: 0.36)
							)
					}
					.buttonStyle(.plain)
					Button(action: onDismiss) {
						Text("Back")
							.font(StakFont.sora(14))
							.foregroundStyle(News.muted)
							.frame(maxWidth: .infinity)
							.frame(height: 52)
							.overlay(
								RoundedRectangle(cornerRadius: 6)
									.strokeBorder(Color(argb: 0x54343B4F), lineWidth: 0.36)
							)
							.contentShape(Rectangle())
					}
					.buttonStyle(.plain)
				}
			}
			.padding(.horizontal, 20)
			.padding(.top, 10)
			.padding(.bottom, 30)
			.frame(maxWidth: .infinity)
			// The sheet surface runs under the home indicator (the Android
			// navigationBarsPadding sits inside the background too).
			.background {
				UnevenRoundedRectangle(topLeadingRadius: 24, bottomLeadingRadius: 0, bottomTrailingRadius: 0, topTrailingRadius: 24, style: .circular)
					.fill(News.cardBg)
					.ignoresSafeArea(edges: .bottom)
			}
		}
	}
}
