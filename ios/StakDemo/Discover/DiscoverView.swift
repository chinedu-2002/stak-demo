import SwiftUI

/// 04 · Discover — "first run" (CHINEDU 1:1627) with its states: the
/// swipe deck (twelve cards cycling the three designed ones), the Save
/// toast (1:1796), the Buy practice ticket (1:1970) and Order filled
/// (85:1205), and the End-of-deck receipt (1:2330).
/// Ported from android/ ui/discover/DiscoverScreen.kt.
enum Disc {
	static let sheetBg = Color(argb: 0xFF181F30)
	static let muted = Color(argb: 0xFF819ABB)
	static let faint = Color(argb: 0xFF5C6B85)
	static let body = Color(argb: 0xFFC8D2E0)
	static let green = Color(argb: 0xFF2FD08A)
	static let red = Color(argb: 0xFFFF5A6A)
	static let teal = Color(argb: 0xFF69B3CA)
	static let tealTint = Color(argb: 0x1A69B3CA)
	static let chipBg = Color(argb: 0xFF242B3D)
	static let divider = Color(argb: 0xFF2A3346)
	static let badgeInk = Color(argb: 0xFF9EADC7)
	static let brightInk = Color(argb: 0xFFF2F6FC)
	static let saveChipBg = Color(argb: 0x17FFFFFF)
	static let amountBg = Color(argb: 0xFF0B1430)
	static let amountBorder = Color(argb: 0x1FFFFFFF)
	static let amountInk = Color(argb: 0xFFDCE7F7)
	static let amountSelBg = Color(argb: 0xFF0F2A38)
	static let amountSelBorder = Color(argb: 0xFF5DA8BF)
	static let amountSelInk = Color(argb: 0xFFA6E4F7)
	static let ctaBorder = Color(argb: 0xA1659EAD)
}

let discCtaGradient = LinearGradient(
	stops: [
		.init(color: Color(argb: 0xFFA6E4F7), location: 0.0889),
		.init(color: Color(argb: 0xFF5DA8BF), location: 0.3919),
		.init(color: Color(argb: 0xFF3C98B4), location: 0.7255),
		.init(color: Color(argb: 0xFF3C98B4), location: 1)
	],
	startPoint: .top, endPoint: .bottom
)

/// A practice-buy ticket's stock values (Buy NVDA? 1:2159 / Buy AAPL? 1:3423).
struct BuySpec {
	let title: String
	let badge: String
	let name: String
	let priceLine: String
	let change: String
	let cashBefore: String
	let cashAfter: String
	let shares: String
	let symbol: String
}

let nvdaBuy = BuySpec(
	title: "Buy NVDA?", badge: "N", name: "NVIDIA Corp", priceLine: "$122.10 today",
	change: "▲ 2.4%", cashBefore: "$8,800.00", cashAfter: "$8,775.00", shares: "0.2048", symbol: "NVDA"
)
let aaplBuy = BuySpec(
	title: "Buy AAPL?", badge: "A", name: "Apple", priceLine: "$229.35 today",
	change: "▲ 1.2%", cashBefore: "$8,800.00", cashAfter: "$8,775.00", shares: "0.1090", symbol: "AAPL"
)

/// One deck card's designed content (art + copy at the front-card scale).
private struct DeckCard {
	let art: String
	let ticker: String
	let headline: String
	let price: String
	let change: String
	let tip: String
	let cardTop: Color
}

private let deck: [DeckCard] = [
	DeckCard(
		art: "DiscCardNVDA", ticker: "NVDA · NVIDIA Corp",
		headline: "Chip demand is outrunning supply, and NVIDIA sets the prices.",
		price: "$122.10", change: "▲ 2.4% today",
		tip: "Chip stocks swing hard. Small stakes, long views.",
		cardTop: Color(argb: 0xFF152A47)
	),
	DeckCard(
		art: "DiscCardAAPL", ticker: "AAPL · Apple Inc",
		headline: "Two billion devices, and every one of them keeps paying Apple.",
		price: "$229.35", change: "▲ 1.2% today",
		tip: "Steady giants move slower. Stable stocks often do.",
		cardTop: Color(argb: 0xFF283E5D)
	),
	DeckCard(
		art: "DiscCardGOOGL", ticker: "GOOGL · Alphabet Inc",
		headline: "Search pays for everything, and nine billion-user products ride behind it.",
		price: "$178.90", change: "▲ 0.8% today",
		tip: "Ad money moves with the economy, so some quarters just drift.",
		cardTop: Color(argb: 0xFF263D5D)
	)
]

struct DiscoverView: View {
	var onLearnMore: () -> Void = {}

	@State private var seen = 0
	@State private var showBuy = false
	@State private var savedToast = false
	@State private var dragOffset: CGFloat = 0

	var body: some View {
		ZStack {
			VStack(spacing: 0) {
				// Header — Discover + progress ring, kicker below.
				VStack(alignment: .leading, spacing: 5) {
					HStack {
						Text("Discover")
							.font(StakFont.sora(26, .semiBold))
							.foregroundStyle(Color.white)
						Spacer()
						let count = min(seen + 1, 12)
						ZStack {
							ProgressRing(progress: CGFloat(count) / 12)
							Text("\(count)/12")
								.font(StakFont.sora(11))
								.foregroundStyle(Color.white)
						}
						.frame(width: 44, height: 44)
					}
					Text("TODAY · AI & CHIPS")
						.font(StakFont.geist(10, .medium))
						.tracking(0.9)
						.foregroundStyle(Disc.faint)
						.padding(.horizontal, 2)
				}
				.padding(.horizontal, 20)
				.padding(.top, 10)

				Spacer().frame(height: 27)

				if seen >= 12 {
					EndOfDeck(
						onPracticeBuySaves: { showBuy = true },
						onSwipeAgain: { seen = 0 }
					)
					Spacer(minLength: 0)
				} else {
					VStack(spacing: 0) {
						// Deck — three stacked gradient cards, front one drags down.
						ZStack(alignment: .topLeading) {
							let order = [deck[(seen + 2) % 3], deck[(seen + 1) % 3], deck[seen % 3]]
							BackDeckCard(card: order[0], scale: 251.81 / 350, rotation: 4.03, offsetX: 0.29, offsetY: -53.85)
							BackDeckCard(card: order[1], scale: 299.51 / 350, rotation: -2.33, offsetX: -0.58, offsetY: -2.0)
							DeckCardBody(card: order[2], onSave: { savedToast = true })
								.frame(width: 350)
								.frame(maxWidth: .infinity)
								.offset(y: 54.65 + dragOffset)
								.onTapGesture(perform: onLearnMore)
						}
						.padding(.horizontal, 20)
						.frame(maxWidth: .infinity)
						.frame(height: 484.65, alignment: .top)
						.contentShape(Rectangle())
						.gesture(
							DragGesture()
								.onChanged { value in
									dragOffset = max(0, value.translation.height)
								}
								.onEnded { value in
									if value.translation.height > 110 {
										withAnimation(.easeOut(duration: 0.22)) { dragOffset = 700 }
										DispatchQueue.main.asyncAfter(deadline: .now() + 0.22) {
											seen += 1
											dragOffset = 0
										}
									} else {
										withAnimation(.easeOut(duration: 0.18)) { dragOffset = 0 }
									}
								}
						)
						Spacer().frame(height: 10)
						VStack(spacing: 5) {
							VStack(spacing: 1) {
								GestureChevron()
								GestureChevron()
							}
							.opacity(0.5)
							Text("Swipe down")
								.font(StakFont.geist(10))
								.foregroundStyle(Disc.faint)
						}
						Spacer().frame(height: 19)
						HStack(spacing: 36) {
							Button { showBuy = true } label: {
								Text("Practice buy")
									.font(StakFont.geist(14, .medium))
									.foregroundStyle(Color.white)
									.frame(width: 120, height: 52)
									.background(discCtaGradient, in: RoundedRectangle(cornerRadius: 6))
									.overlay(RoundedRectangle(cornerRadius: 6).strokeBorder(Disc.ctaBorder, lineWidth: 0.36))
							}
							.buttonStyle(.plain)
							Button(action: onLearnMore) {
								Text("Learn more")
									.font(StakFont.sora(12))
									.foregroundStyle(Disc.muted)
									.frame(width: 120, height: 52)
									.overlay(RoundedRectangle(cornerRadius: 6).strokeBorder(Color(argb: 0x54343B4F), lineWidth: 0.36))
							}
							.buttonStyle(.plain)
						}
						Spacer(minLength: 19)
					}
				}
			}

			// Saved toast (frame 1:1796) — centered pill under the header.
			if savedToast {
				VStack {
					HStack(spacing: 6) {
						Image("IcSavedBookmark")
							.resizable()
							.frame(width: 12, height: 12)
						Text("Saved to My STAK")
							.font(StakFont.geist(12, .medium))
							.foregroundStyle(Color.white)
					}
					.padding(.horizontal, 14)
					.padding(.vertical, 10)
					.background(Disc.chipBg, in: RoundedRectangle(cornerRadius: 19.5))
					.padding(.top, 78)
					Spacer()
				}
				.transition(.opacity)
				.onAppear {
					DispatchQueue.main.asyncAfter(deadline: .now() + 2.2) {
						withAnimation { savedToast = false }
					}
				}
			}

			if showBuy {
				DiscoverBuyFlow(spec: nvdaBuy, onClose: { showBuy = false })
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}

/// 3pt ring inset 4pt in a 44pt box — track #2a3346, teal progress from 12 o'clock.
struct ProgressRing: View {
	let progress: CGFloat

	var body: some View {
		ZStack {
			Circle()
				.stroke(Disc.divider, style: StrokeStyle(lineWidth: 3, lineCap: .round))
			Circle()
				.trim(from: 0, to: progress)
				.stroke(Disc.teal, style: StrokeStyle(lineWidth: 3, lineCap: .round))
				.rotationEffect(.degrees(-90))
		}
		.padding(5.5)
	}
}

/// One of the two tilted back cards, authored at the 350x444.4 front
/// size and scaled; the offsets place the rotated bounds so the card
/// tops peek exactly as in the frame (GOOGL at deck-y 0, AAPL at 24.2).
private struct BackDeckCard: View {
	let card: DeckCard
	let scale: CGFloat
	let rotation: Double
	let offsetX: CGFloat
	let offsetY: CGFloat

	var body: some View {
		DeckCardBody(card: card, onSave: nil)
			.frame(width: 350, height: 444.4, alignment: .top)
			.scaleEffect(scale)
			.rotationEffect(.degrees(rotation))
			.offset(x: offsetX, y: offsetY)
			.frame(maxWidth: .infinity, alignment: .topLeading)
	}
}

private struct DeckCardBody: View {
	let card: DeckCard
	let onSave: (() -> Void)?

	var body: some View {
		VStack(spacing: 25) {
			ZStack(alignment: .topTrailing) {
				Image(card.art)
					.resizable()
					.frame(width: 340, height: 229)
					.clipShape(RoundedRectangle(cornerRadius: 18))
				if card.art != "DiscCardNVDA" {
					// NVDA's chip is baked into its art; the others draw it live —
					// on the back cards as well, as the frame shows.
					SaveChip()
						.padding(.top, 6)
						.padding(.trailing, 10)
				}
				if let onSave {
					Button(action: onSave) {
						Color.clear.frame(width: 86, height: 38)
					}
					.buttonStyle(.plain)
					.padding(.top, 2)
					.padding(.trailing, 6)
				}
			}
			VStack(alignment: .leading, spacing: 19) {
				VStack(alignment: .leading, spacing: 8) {
					Text(card.ticker)
						.font(StakFont.geist(10))
						.foregroundStyle(Disc.muted)
					Text(card.headline)
						.font(StakFont.geist(16))
						.lineSpacing(23 - 16)
						.foregroundStyle(Color.white)
					HStack(alignment: .lastTextBaseline, spacing: 9) {
						Text(card.price)
							.font(StakFont.sora(20, .semiBold))
							.foregroundStyle(Color.white)
						Text(card.change)
							.font(StakFont.geist(11, .medium))
							.foregroundStyle(Disc.green)
					}
				}
				HStack(alignment: .center, spacing: 8) {
					Text("TIP")
						.font(StakFont.geist(10, .medium))
						.tracking(0.9)
						.foregroundStyle(Disc.teal)
					Text(card.tip)
						.font(StakFont.geist(11))
						.lineSpacing(15 - 11)
						.foregroundStyle(Disc.body)
						.frame(maxWidth: .infinity, alignment: .leading)
				}
				.padding(.horizontal, 12)
				.padding(.vertical, 9)
				.background(Disc.tealTint, in: RoundedRectangle(cornerRadius: 10))
			}
			.padding(.horizontal, 18)
			.padding(.bottom, 16)
		}
		.padding(.vertical, 4)
		.background(
			LinearGradient(colors: [card.cardTop, Color(argb: 0xFF0C1526)], startPoint: .top, endPoint: .bottom),
			in: RoundedRectangle(cornerRadius: 22)
		)
	}
}

/// rgba(255,255,255,0.09) Save pill with the small bookmark.
private struct SaveChip: View {
	var body: some View {
		HStack(spacing: 6) {
			Text("Save")
				.font(StakFont.geist(12, .medium))
				.foregroundStyle(Color.white)
			Image("IcSavedBookmark")
				.resizable()
				.frame(width: 12, height: 12)
		}
		.padding(.horizontal, 13)
		.padding(.vertical, 7)
		.background(Disc.saveChipBg, in: RoundedRectangle(cornerRadius: 16))
	}
}

/// 16x8 down-chevron stroke (muted).
private struct GestureChevron: View {
	var body: some View {
		ChevronShape()
			.stroke(StakColors.muted, style: StrokeStyle(lineWidth: 1.6, lineCap: .round, lineJoin: .round))
			.frame(width: 16, height: 8)
	}
}

private struct ChevronShape: Shape {
	func path(in rect: CGRect) -> Path {
		var p = Path()
		p.move(to: CGPoint(x: 0, y: 0))
		p.addLine(to: CGPoint(x: rect.midX, y: rect.maxY))
		p.addLine(to: CGPoint(x: rect.maxX, y: 0))
		return p
	}
}

/// Discover · End of deck (CHINEDU 1:2330) — receipt stats + CTAs.
private struct EndOfDeck: View {
	let onPracticeBuySaves: () -> Void
	let onSwipeAgain: () -> Void

	var body: some View {
		VStack(spacing: 0) {
			Spacer().frame(height: 47)
			Text("Deck complete")
				.font(StakFont.sora(22, .semiBold))
				.foregroundStyle(Disc.brightInk)
			Spacer().frame(height: 14)
			Text("Twelve cards, twelve signals. Your taste graph got smarter.")
				.font(StakFont.geist(12))
				.foregroundStyle(Disc.muted)
			Spacer().frame(height: 33)
			HStack(spacing: 10) {
				statTile("Seen", "12")
				statTile("Saved", "7")
				statTile("Bought", "2")
			}
			Spacer().frame(height: 56)
			SheetCta(text: "Practice buy your saves", action: onPracticeBuySaves)
			Spacer().frame(height: 9)
			SheetSecondary(text: "Review saves in My STAK", action: {})
			Spacer().frame(height: 14)
			Text("A new deck lands tomorrow with your morning brief.")
				.font(StakFont.geist(10))
				.foregroundStyle(Disc.muted)
			Spacer().frame(height: 22)
			Button(action: onSwipeAgain) {
				Text("Swipe today’s deck again")
					.font(StakFont.sora(13))
					.foregroundStyle(Disc.muted)
					.frame(maxWidth: .infinity)
					.frame(height: 32)
			}
			.buttonStyle(.plain)
		}
		.padding(.horizontal, 20)
	}

	private func statTile(_ label: String, _ value: String) -> some View {
		VStack(spacing: 4) {
			Text(label)
				.font(StakFont.geist(10))
				.foregroundStyle(Disc.muted)
			Text(value)
				.font(StakFont.sora(20, .semiBold))
				.foregroundStyle(Disc.brightInk)
		}
		.frame(width: 110)
		.padding(.horizontal, 10)
		.padding(.vertical, 14)
		.background(Disc.sheetBg, in: RoundedRectangle(cornerRadius: 12))
	}
}

// MARK: - Practice-buy sheets (1:1970 Buy / 85:1205 Order filled)

/// Shared sheet scaffold — 45% #0a1020 scrim + r24 #181f30 sheet.
struct SheetScaffold<Content: View>: View {
	let onDismiss: () -> Void
	@ViewBuilder let content: Content

	var body: some View {
		ZStack(alignment: .bottom) {
			Color(argb: 0x730A1020)
				.ignoresSafeArea()
				.onTapGesture(perform: onDismiss)
			VStack(spacing: 0) {
				RoundedRectangle(cornerRadius: 2)
					.fill(Disc.divider)
					.frame(width: 40, height: 4)
					.padding(.bottom, 4)
				content
			}
			.padding(.horizontal, 20)
			.padding(.top, 10)
			.padding(.bottom, 30)
			.frame(maxWidth: .infinity)
			.background(Disc.sheetBg, in: UnevenRoundedRectangle(topLeadingRadius: 24, topTrailingRadius: 24))
			.ignoresSafeArea(edges: .bottom)
		}
	}
}

/// Stock row used by the ticket sheets — teal-tinted, badge + price + change.
struct SheetStockRow: View {
	let spec: BuySpec

	var body: some View {
		HStack(spacing: 12) {
			ZStack {
				Circle().fill(Disc.chipBg)
				Text(spec.badge)
					.font(StakFont.sora(15, .semiBold))
					.foregroundStyle(Disc.badgeInk)
			}
			.frame(width: 38, height: 38)
			VStack(alignment: .leading, spacing: 2) {
				Text(spec.name)
					.font(StakFont.geist(13, .medium))
					.foregroundStyle(Color.white)
				Text(spec.priceLine)
					.font(StakFont.geist(10))
					.foregroundStyle(Disc.muted)
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			Text(spec.change)
				.font(StakFont.geist(12, .medium))
				.foregroundStyle(Disc.green)
		}
		.padding(.horizontal, 14)
		.padding(.vertical, 12)
		.background(Disc.tealTint, in: RoundedRectangle(cornerRadius: 6))
	}
}

struct SheetCta: View {
	let text: String
	let action: () -> Void

	var body: some View {
		Button(action: action) {
			Text(text)
				.font(StakFont.geist(14, .medium))
				.foregroundStyle(Color.white)
				.frame(maxWidth: .infinity)
				.frame(height: 52)
				.background(discCtaGradient, in: RoundedRectangle(cornerRadius: 6))
				.overlay(RoundedRectangle(cornerRadius: 6).strokeBorder(Disc.ctaBorder, lineWidth: 0.36))
		}
		.buttonStyle(.plain)
	}
}

struct SheetSecondary: View {
	let text: String
	let action: () -> Void

	var body: some View {
		Button(action: action) {
			Text(text)
				.font(StakFont.sora(14))
				.foregroundStyle(Disc.muted)
				.frame(maxWidth: .infinity)
				.frame(height: 52)
				.overlay(RoundedRectangle(cornerRadius: 6).strokeBorder(Color(argb: 0x54343B4F), lineWidth: 0.36))
		}
		.buttonStyle(.plain)
	}
}

/// "Buy …?" practice ticket (frame 1:1970, sheet 1:2159).
struct PracticeBuySheet: View {
	let spec: BuySpec
	let onConfirm: () -> Void
	let onDismiss: () -> Void

	@State private var selected = 1

	var body: some View {
		SheetScaffold(onDismiss: onDismiss) {
			VStack(alignment: .leading, spacing: 14) {
				Text(spec.title)
					.font(StakFont.sora(18, .semiBold))
					.foregroundStyle(Color.white)
				SheetStockRow(spec: spec)
				Text("Your paper stake starts at today’s price and tracks the real move live, in either direction.")
					.font(StakFont.geist(12))
					.lineSpacing(18 - 12)
					.foregroundStyle(Disc.body)
				VStack(alignment: .leading, spacing: 12) {
					HStack(spacing: 6) {
						Text("Cash available")
							.font(StakFont.geist(12))
							.foregroundStyle(Disc.muted)
						Text(spec.cashBefore)
							.font(StakFont.geist(12, .medium))
							.foregroundStyle(Disc.brightInk)
					}
					HStack(spacing: 8) {
						ForEach(Array(["$10", "$25", "$50", "$100", "Custom"].enumerated()), id: \.offset) { i, label in
							let sel = i == selected
							Button { selected = i } label: {
								Text(label)
									.font(StakFont.geist(12, .medium))
									.foregroundStyle(sel ? Disc.amountSelInk : Disc.amountInk)
									.frame(maxWidth: .infinity)
									.padding(.vertical, 8)
									.background(sel ? Disc.amountSelBg : Disc.amountBg, in: RoundedRectangle(cornerRadius: 10))
									.overlay(
										RoundedRectangle(cornerRadius: 10)
											.strokeBorder(sel ? Disc.amountSelBorder : Disc.amountBorder, lineWidth: sel ? 0.5 : 1)
									)
							}
							.buttonStyle(.plain)
						}
					}
				}
				Spacer().frame(height: 10)
				HStack(alignment: .lastTextBaseline, spacing: 6) {
					Spacer()
					Text("You get")
						.font(StakFont.geist(12))
						.foregroundStyle(Disc.muted)
					Text(spec.shares)
						.font(StakFont.sora(15, .semiBold))
						.foregroundStyle(Disc.brightInk)
					Text("shares of \(spec.symbol)")
						.font(StakFont.geist(12))
						.foregroundStyle(Disc.muted)
					Spacer()
				}
				VStack(spacing: 16) {
					SheetCta(text: "Confirm practice buy", action: onConfirm)
					SheetSecondary(text: "Not yet", action: onDismiss)
				}
			}
		}
	}
}

/// "Order filled" sheet (frame 85:1205, sheet 85:1394).
struct OrderFilledSheet: View {
	let spec: BuySpec
	let onDismiss: () -> Void

	var body: some View {
		SheetScaffold(onDismiss: onDismiss) {
			VStack(spacing: 14) {
				Image("IcSheetCheck")
					.resizable()
					.frame(width: 47, height: 47)
				Text("Order filled")
					.font(StakFont.sora(18, .semiBold))
					.foregroundStyle(Color.white)
				SheetStockRow(spec: spec)
				HStack(spacing: 6) {
					Text("Cash available")
						.font(StakFont.geist(12))
						.foregroundStyle(Disc.muted)
					Text(spec.cashAfter)
						.font(StakFont.geist(12, .medium))
						.foregroundStyle(Disc.brightInk)
					Spacer()
				}
				HStack(alignment: .lastTextBaseline, spacing: 6) {
					Spacer()
					Text("You now hold")
						.font(StakFont.geist(12))
						.foregroundStyle(Disc.muted)
					Text(spec.shares)
						.font(StakFont.sora(15, .semiBold))
						.foregroundStyle(Disc.brightInk)
					Text("shares of \(spec.symbol)")
						.font(StakFont.geist(12))
						.foregroundStyle(Disc.muted)
					Spacer()
				}
				VStack(spacing: 16) {
					SheetCta(text: "View in My STAK", action: onDismiss)
					SheetSecondary(text: "Keep exploring", action: onDismiss)
				}
			}
		}
	}
}

/// Buy → Order-filled flow, reused by the Stock Detail page.
struct DiscoverBuyFlow: View {
	let spec: BuySpec
	let onClose: () -> Void

	@State private var filled = false

	var body: some View {
		if !filled {
			PracticeBuySheet(spec: spec, onConfirm: { filled = true }, onDismiss: onClose)
		} else {
			OrderFilledSheet(spec: spec, onDismiss: onClose)
		}
	}
}
