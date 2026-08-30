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
	static let teal = Color(argb: 0xFF69B3CA)
	static let tealTint = Color(argb: 0x1A69B3CA)
	static let chipBg = Color(argb: 0xFF242B3D)
	static let divider = Color(argb: 0xFF2A3346)
	static let badgeInk = Color(argb: 0xFF9EADC7)
	static let brightInk = Color(argb: 0xFFF2F6FC)
	static let saveChipBg = Color(argb: 0x26FFFFFF)
	static let amountBg = Color(argb: 0xFF0B1430)
	static let amountBorder = Color(argb: 0x1FFFFFFF)
	static let amountInk = Color(argb: 0xFFDCE7F7)
	static let amountSelBg = Color(argb: 0xFF0F2A38)
	static let amountSelBorder = Color(argb: 0xFF5DA8BF)
	static let amountSelInk = Color(argb: 0xFFA6E4F7)
	static let ctaBorder = StakColors.ctaBorderGradient
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
	let artBg: Color
}

private let deck: [DeckCard] = [
	DeckCard(
		art: "DiscCardNVDA", ticker: "NVDA · NVIDIA Corp",
		headline: "Chip demand is outrunning supply, and NVIDIA sets the prices.",
		price: "$122.10", change: "▲ 2.4% today",
		tip: "Chip stocks swing hard. Small stakes, long views.",
		cardTop: Color(argb: 0xFF152A47), artBg: Color(argb: 0xFF142844)
	),
	DeckCard(
		art: "DiscCardAAPL", ticker: "AAPL · Apple Inc",
		headline: "Two billion devices, and every one of them keeps paying Apple.",
		price: "$229.35", change: "▲ 1.2% today",
		tip: "Steady giants move slower. Stable stocks often do.",
		cardTop: Color(argb: 0xFF283E5D), artBg: Color(argb: 0xFF253A59)
	),
	DeckCard(
		art: "DiscCardGOOGL", ticker: "GOOGL · Alphabet Inc",
		headline: "Search pays for everything, and nine billion-user products ride behind it.",
		price: "$178.90", change: "▲ 0.8% today",
		tip: "Ad money moves with the economy, so some quarters just drift.",
		cardTop: Color(argb: 0xFF263D5D), artBg: Color(argb: 0xFF2F486E)
	)
]

struct DiscoverView: View {
	var onLearnMore: () -> Void = {}
	/// Raised to the shell — the buy ticket scrims the TAB BAR too (frame
	/// 1:1970), so MainTabsView owns the overlay, mirroring Android's
	/// MainShell `discoverBuy` hoist.
	var onPracticeBuy: (BuySpec) -> Void = { _ in }

	@State private var seen = 0
	@State private var savedToast = false
	@State private var dragOffset: CGFloat = 0
	@State private var frontOpacity: Double = 1
	@State private var frontScale: CGFloat = 1
	/// Guards the drag while the fly-off or enter animation runs.
	@State private var shuffling = false

	var body: some View {
		let u = figmaUnit
		ZStack {
			VStack(spacing: 0) {
				// Header — Discover + progress ring, kicker below.
				VStack(alignment: .leading, spacing: 5 * u) {
					// 1:1627 centres the 33-tall title in the 44-tall ring row (measured exact);
					// the end-of-deck frame (1:2330) authors the title 7 higher against the ring.
					HStack {
						Text("Discover")
							.font(StakFont.sora(26 * u, .semiBold))
							.lineSpacing((33 - 26) * u)
							.foregroundStyle(Color.white)
							.offset(y: seen >= 12 ? -7 * u : 0)
						Spacer()
						let count = min(seen + 1, 12)
						ZStack {
							ProgressRing(progress: CGFloat(count) / 12)
							Text("\(count)/12")
								.font(StakFont.sora(11 * u))
								.foregroundStyle(Color.white)
						}
						.frame(width: 44 * u, height: 44 * u)
					}
					Text("TODAY · AI & CHIPS")
						.font(StakFont.geist(10 * u, .medium))
						.tracking(0.9 * u)
						.foregroundStyle(Disc.muted)
				}
				.padding(.horizontal, 20 * u)
				.padding(.top, 10 * u)

				Spacer().frame(height: 27 * u)

				if seen >= 12 {
					EndOfDeck(
						onPracticeBuySaves: { onPracticeBuy(nvdaBuy) },
						onSwipeAgain: { seen = 0 }
					)
					Spacer(minLength: 0)
				} else {
					// Deck — a fixed composition: every dimension scales by the
					// 390pt artboard unit so proportions match the frame on any
					// device. The shuffle lives inside the deck bounds — the
					// diving card must never cover the gesture/CTA zone.
					VStack(spacing: 0) {
						ZStack(alignment: .top) {
							// The authored deck (1:1627): the queued cards behind
							// are the DESIGNED ILLUSION — the exact authored
							// slabs, always (they give the illusion of a queue).
							Image("DiscPeekTop")
								.resizable()
								.frame(width: 273.66 * u, height: 336.66 * u)
								.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
								.offset(x: 39 * u, y: 0)
							Image("DiscPeekMid")
								.resizable()
								.frame(width: 313.14 * u, height: 352.87 * u)
								.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
								.offset(x: 18 * u, y: 24 * u)
							FrontDeckCard(card: deck[seen % 3], onSave: { MyStakHoldings.shared.add(deck[seen % 3].ticker); savedToast = true }, u: u)
								.scaleEffect(frontScale)
								.opacity(frontOpacity)
								.offset(y: 54.65 * u + dragOffset)
								.onTapGesture(perform: onLearnMore)
						}
						.frame(maxWidth: .infinity)
						.frame(height: 484.65 * u, alignment: .top)
						.clipped()
						.contentShape(Rectangle())
						.gesture(
							DragGesture()
								.onChanged { value in
									if !shuffling {
										dragOffset = max(0, value.translation.height)
									}
								}
								.onEnded { _ in
									guard !shuffling else { return }
									if dragOffset > 110 * u {
										// The frame's card shuffle: the swiped card
										// flies off fading while the counter steps
										// and the cycled card enters at the front
										// slot alone (1:1627).
										shuffling = true
										withAnimation(.easeOut(duration: 0.28)) { dragOffset = 500 * u }
										withAnimation(.easeOut(duration: 0.3)) { frontOpacity = 0 }
										DispatchQueue.main.asyncAfter(deadline: .now() + 0.3) {
											seen += 1
											dragOffset = 0
											frontScale = 0.97
											withAnimation(.easeOut(duration: 0.2)) {
												frontOpacity = 1
												frontScale = 1
											}
											DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
												shuffling = false
											}
										}
									} else {
										withAnimation(.easeOut(duration: 0.18)) { dragOffset = 0 }
									}
								}
						)
						.padding(.horizontal, 20 * u)
						Spacer().frame(height: 10 * u)
						VStack(spacing: 5 * u) {
							VStack(spacing: 1 * u) {
								GestureChevron(u: u)
								GestureChevron(u: u)
							}
							.opacity(0.5)
							Text("Swipe down")
								.font(StakFont.geist(10 * u))
								.foregroundStyle(Disc.faint)
						}
						Spacer().frame(height: 19 * u)
						HStack(spacing: 36 * u) {
							Button { onPracticeBuy(nvdaBuy) } label: {
								Text("Practice buy")
									.font(StakFont.geist(14 * u, .medium))
									.foregroundStyle(Color.white)
									.frame(width: 120 * u, height: 52 * u)
									// Authored drop shadow (1:1783 Inspect): dy 12.28, blur
									// 12.28, #52AAC7 at 9% (Learn more's is disabled).
									.background {
										RoundedRectangle(cornerRadius: 6 * u)
											.fill(Color(argb: 0xFF52AAC7))
											.opacity(0.09)
											.blur(radius: 12.28 * u)
											.offset(y: 12.28 * u)
									}
									.background(discCtaGradient, in: RoundedRectangle(cornerRadius: 6 * u))
									.overlay(RoundedRectangle(cornerRadius: 6 * u).strokeBorder(Disc.ctaBorder, lineWidth: 0.36 * u))
							}
							.buttonStyle(.plain)
							Button(action: onLearnMore) {
								Text("Learn more")
									.font(StakFont.sora(12 * u))
									.foregroundStyle(Disc.muted)
									.frame(width: 120 * u, height: 52 * u)
									.overlay(RoundedRectangle(cornerRadius: 6 * u).strokeBorder(Color(argb: 0x54343B4F), lineWidth: 0.36 * u))
							}
							.buttonStyle(.plain)
						}
						Spacer(minLength: 19 * u)
					}
				}
			}

			// Saved toast (frame 1:1796) — centered pill under the header.
			if savedToast {
				VStack {
					HStack(spacing: 13.5 * u) {
						Image("IcSavedBookmark")
							.resizable()
							.frame(width: 14 * u, height: 14 * u)
						Text("Saved to My STAK")
							.font(StakFont.geist(12 * u, .medium))
							.foregroundStyle(Color.white)
					}
					.padding(.leading, 14 * u)
					.padding(.trailing, 12 * u)
					.frame(height: 39 * u)
					// Authored (1:1796): 148.5x39 translucent pill — the peek slab shows through.
					.background(Disc.chipBg.opacity(0.5), in: RoundedRectangle(cornerRadius: 19.5 * u))
					.padding(.top, 78 * u)
					Spacer()
				}
				.transition(.opacity)
				.onAppear {
					DispatchQueue.main.asyncAfter(deadline: .now() + 2.2) {
						withAnimation { savedToast = false }
					}
				}
			}

		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}

/// 44u progress ring — #2a3346 track + #69b3ca arc from 12 o'clock,
/// 4u round stroke inset 4u.
struct ProgressRing: View {
	let progress: CGFloat

	var body: some View {
		let u = figmaUnit
		ZStack {
			Circle()
				.stroke(Disc.divider, style: StrokeStyle(lineWidth: 4 * u, lineCap: .round))
			Circle()
				.trim(from: 0, to: progress)
				.stroke(Disc.teal, style: StrokeStyle(lineWidth: 4 * u, lineCap: .round))
				.rotationEffect(.degrees(-90))
		}
		.padding(4 * u)
	}
}

/// The full-size front card (350u wide) with its live Save chip.
/// The deck's layer structure: every boundary in the frame is a
/// brightness step plus a thin dark rim (the authored exports carry
/// it). NVDA's dark chrome makes its own step; light-topped cards need
/// the rim — a tight dark seam hugging the edge — so the card reads as
/// its own layer over the queue in EVERY state.
private struct FrontDeckCard: View {
	let card: DeckCard
	let onSave: () -> Void
	let u: CGFloat

	var body: some View {
		DeckCardBody(card: card, onSave: onSave, u: u)
			.frame(width: 350 * u)
			.background { CardSeam(u: u) }
	}
}

/// Concentric 1pt rounded strokes reaching 6u out from the card edge,
/// #060b16 fading by 0.5·(1−t)² — mirrors the Kotlin drawBehind loop.
private struct CardSeam: View {
	let u: CGFloat

	var body: some View {
		let reach = 6 * u
		ZStack {
			ForEach(0..<Int(ceil(reach)), id: \.self) { i in
				let d = CGFloat(i)
				let t = d / reach
				RoundedRectangle(cornerRadius: 22 * u + d)
					.stroke(Color(argb: 0xFF060B16).opacity(0.5 * (1 - t) * (1 - t)), lineWidth: 1)
					.padding(-d)
			}
		}
	}
}

private struct DeckCardBody: View {
	let card: DeckCard
	let onSave: (() -> Void)?
	let u: CGFloat

	var body: some View {
		// The authored card template (1:1740, shared by all three designs):
		// art 340x229 at y4, overlay at 258 -> gap 25.
		VStack(spacing: 25 * u) {
			ZStack(alignment: .topTrailing) {
				Image(card.art)
					.resizable()
					.scaledToFill()
					.frame(width: 340 * u, height: 229 * u)
					.background(card.artBg)
					.clipShape(RoundedRectangle(cornerRadius: 18 * u))
				if card.art != "DiscCardNVDA" {
					// NVDA's chip is baked into its art; the others draw it live
					// at the template's authored spot (art x264 y6, 340-264-72=4).
					SaveChip(u: u)
						.padding(.top, 6 * u)
						.padding(.trailing, 4 * u)
				}
				if let onSave {
					Button(action: onSave) {
						Color.clear.frame(width: 86 * u, height: 38 * u)
					}
					.buttonStyle(.plain)
					.padding(.top, 2 * u)
					.padding(.trailing, 6 * u)
				}
			}
			VStack(alignment: .leading, spacing: 19 * u) {
				VStack(alignment: .leading, spacing: 8 * u) {
					Text(card.ticker)
						.font(StakFont.geist(10 * u))
						.lineSpacing((13 - 10) * u)
						.foregroundStyle(Disc.muted)
					Text(card.headline)
						.font(StakFont.geist(16 * u))
						.lineSpacing((23 - 16) * u)
						.foregroundStyle(Color.white)
					HStack(alignment: .bottom, spacing: 9 * u) {
						Text(card.price)
							.font(StakFont.sora(20 * u, .semiBold))
							.lineSpacing((25 - 20) * u)
							.foregroundStyle(Color.white)
						Text(card.change)
							.font(StakFont.geist(11 * u, .medium))
							.lineSpacing((14 - 11) * u)
							.foregroundStyle(Disc.green)
							.padding(.bottom, 2 * u)
					}
				}
				HStack(alignment: .center, spacing: 8 * u) {
					Text("TIP")
						.font(StakFont.geist(10 * u, .medium))
						.tracking(0.9 * u)
						.foregroundStyle(Disc.teal)
					Text(card.tip)
						.font(StakFont.geist(11 * u))
						.lineSpacing((15 - 11) * u)
						.foregroundStyle(Disc.body)
						.frame(maxWidth: .infinity, alignment: .leading)
				}
				.padding(.horizontal, 12 * u)
				.padding(.vertical, 9 * u)
				.background(Disc.tealTint, in: RoundedRectangle(cornerRadius: 10 * u))
			}
			.padding(.horizontal, 18 * u)
			.padding(.bottom, 16 * u)
		}
		.padding(.top, 4 * u)
		.padding(.bottom, 4 * u)
		.frame(maxWidth: .infinity)
		.background(
			LinearGradient(colors: [card.cardTop, Color(argb: 0xFF0C1526)], startPoint: .top, endPoint: .bottom),
			in: RoundedRectangle(cornerRadius: 22 * u)
		)
	}
}

/// rgba(255,255,255,0.15) Save pill with the small bookmark — 72x30 at
/// the template's authored spot.
private struct SaveChip: View {
	let u: CGFloat

	var body: some View {
		HStack(spacing: 6 * u) {
			Text("Save")
				.font(StakFont.geist(12 * u, .medium))
				.foregroundStyle(Color.white)
			Image("IcHeroBookmark")
				.resizable()
				.frame(width: 12 * u, height: 12 * u)
		}
		.padding(.horizontal, 13 * u)
		.padding(.vertical, 7 * u)
		.background(Disc.saveChipBg, in: RoundedRectangle(cornerRadius: 16 * u))
	}
}

/// 16x8 down-chevron stroke (muted).
private struct GestureChevron: View {
	let u: CGFloat

	var body: some View {
		ChevronShape()
			.stroke(StakColors.muted, style: StrokeStyle(lineWidth: 1.6 * u, lineCap: .round, lineJoin: .round))
			.frame(width: 16 * u, height: 8 * u)
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
		let u = figmaUnit
		VStack(spacing: 0) {
			// Authored column (1:2330): title 190, stats 274, CTA 396, swipe 577.
			Spacer().frame(height: 34 * u)
			Text("Deck complete")
				.font(StakFont.sora(22 * u, .semiBold))
				.foregroundStyle(Disc.brightInk)
			Spacer().frame(height: 8 * u)
			Text("Twelve cards, twelve signals. Your taste graph got smarter.")
				.font(StakFont.geist(12 * u))
				.foregroundStyle(Disc.muted)
			Spacer().frame(height: 32 * u)
			HStack(spacing: 10 * u) {
				statTile("Seen", "12", u)
				statTile("Saved", "7", u)
				statTile("Bought", "2", u)
			}
			Spacer().frame(height: 52 * u)
			SheetCta(text: "Practice buy your saves", action: onPracticeBuySaves)
			Spacer().frame(height: 9 * u)
			Button(action: { /* My STAK lands in a later phase. */ }) {
				Text("Review saves in My STAK")
					.font(StakFont.sora(13 * u))
					.foregroundStyle(Disc.muted)
					.frame(maxWidth: .infinity)
					.frame(height: 52 * u)
					.overlay(RoundedRectangle(cornerRadius: 6 * u).strokeBorder(Color(argb: 0x54343B4F), lineWidth: 0.36 * u))
			}
			.buttonStyle(.plain)
			Spacer().frame(height: 14 * u)
			Text("A new deck lands tomorrow with your morning brief.")
				.font(StakFont.geist(10 * u))
				.foregroundStyle(Disc.muted)
			Spacer().frame(height: 40.5 * u)
			Button(action: onSwipeAgain) {
				Text("Swipe today’s deck again")
					.font(StakFont.sora(13 * u))
					.foregroundStyle(Disc.muted)
					.frame(maxWidth: .infinity)
					.frame(height: 32 * u)
			}
			.buttonStyle(.plain)
		}
		.padding(.horizontal, 20 * u)
	}

	private func statTile(_ label: String, _ value: String, _ u: CGFloat) -> some View {
		VStack(spacing: 4 * u) {
			Text(label)
				.font(StakFont.geist(10 * u))
				.foregroundStyle(Disc.muted)
			Text(value)
				.font(StakFont.sora(20 * u, .semiBold))
				.foregroundStyle(Disc.brightInk)
		}
		.frame(width: 110 * u)
		.padding(.horizontal, 10 * u)
		.padding(.vertical, 14 * u)
		.background(Disc.sheetBg, in: RoundedRectangle(cornerRadius: 12 * u))
	}
}

// MARK: - Practice-buy sheets (1:1970 Buy / 85:1205 Order filled)

/// Shared sheet scaffold — 45% #0a1020 scrim + r24 #181f30 sheet.
struct SheetScaffold<Content: View>: View {
	let onDismiss: () -> Void
	@ViewBuilder let content: Content

	var body: some View {
		let u = figmaUnit
		ZStack(alignment: .bottom) {
			// Authored ticket scrim rgba(0,0,0,0.6) (1:2158).
			Color(argb: 0x99000000)
				.ignoresSafeArea()
				.onTapGesture(perform: onDismiss)
			VStack(spacing: 0) {
				// Authored (1:2159): handle at y10–14, title at y32 — 18 below
				// the rect after the 10 top padding.
				RoundedRectangle(cornerRadius: 2 * u)
					.fill(Disc.divider)
					.frame(width: 40 * u, height: 4 * u)
					.padding(.bottom, 18 * u)
				content
			}
			.padding(.horizontal, 20 * u)
			.padding(.top, 10 * u)
			.padding(.bottom, 30 * u)
			.frame(maxWidth: .infinity)
			.background(Disc.sheetBg, in: UnevenRoundedRectangle(topLeadingRadius: 24 * u, topTrailingRadius: 24 * u))
			.ignoresSafeArea(edges: .bottom)
		}
	}
}

/// Stock row used by the ticket sheets — teal-tinted, badge + price + change.
struct SheetStockRow: View {
	let spec: BuySpec

	var body: some View {
		let u = figmaUnit
		HStack(spacing: 12 * u) {
			ZStack {
				Circle().fill(Disc.chipBg)
				Text(spec.badge)
					.font(StakFont.sora(15 * u, .semiBold))
					.foregroundStyle(Disc.badgeInk)
			}
			.frame(width: 38 * u, height: 38 * u)
			VStack(alignment: .leading, spacing: 2 * u) {
				Text(spec.name)
					.font(StakFont.geist(13 * u, .medium))
					.foregroundStyle(Color.white)
				Text(spec.priceLine)
					.font(StakFont.geist(10 * u))
					.foregroundStyle(Disc.muted)
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			Text(spec.change)
				.font(StakFont.geist(12 * u, .medium))
				.foregroundStyle(Disc.green)
		}
		.padding(.horizontal, 14 * u)
		.padding(.vertical, 12 * u)
		.background(Disc.tealTint, in: RoundedRectangle(cornerRadius: 6 * u))
	}
}

struct SheetCta: View {
	let text: String
	let action: () -> Void

	var body: some View {
		let u = figmaUnit
		Button(action: action) {
			Text(text)
				.font(StakFont.geist(14 * u, .medium))
				.foregroundStyle(Color.white)
				.frame(maxWidth: .infinity)
				.frame(height: 52 * u)
				// Authored drop shadow (85:1394 Inspect): dy 12.28, blur 12.28,
				// #52AAC7 at 9% — the same glow the deck's Practice buy carries.
				.background {
					RoundedRectangle(cornerRadius: 6 * u)
						.fill(Color(argb: 0xFF52AAC7))
						.opacity(0.09)
						.blur(radius: 12.28 * u)
						.offset(y: 12.28 * u)
				}
				.background(discCtaGradient, in: RoundedRectangle(cornerRadius: 6 * u))
				.overlay(RoundedRectangle(cornerRadius: 6 * u).strokeBorder(Disc.ctaBorder, lineWidth: 0.36 * u))
		}
		.buttonStyle(.plain)
	}
}

struct SheetSecondary: View {
	let text: String
	let action: () -> Void

	var body: some View {
		let u = figmaUnit
		Button(action: action) {
			Text(text)
				.font(StakFont.sora(14 * u))
				.foregroundStyle(Disc.muted)
				.frame(maxWidth: .infinity)
				.frame(height: 52 * u)
				// Authored (1:1970): faint 4% white fill under the hairline.
				.background(RoundedRectangle(cornerRadius: 6 * u).fill(Color.white.opacity(0.04)))
				.overlay(RoundedRectangle(cornerRadius: 6 * u).strokeBorder(Color(argb: 0x54343B4F), lineWidth: 0.36 * u))
		}
		.buttonStyle(.plain)
	}
}

/// "Buy …?" practice ticket (frame 1:1970, sheet 1:2159).
struct PracticeBuySheet: View {
	let spec: BuySpec
	let onConfirm: () -> Void
	let onDismiss: () -> Void
	/// 1:1970 authors "Not yet"; the Simulate ticket (1:4232) authors "Back".
	var secondary: String = "Not yet"

	@State private var selected = 1

	var body: some View {
		let u = figmaUnit
		SheetScaffold(onDismiss: onDismiss) {
			VStack(alignment: .leading, spacing: 14 * u) {
				Text(spec.title)
					.font(StakFont.sora(18 * u, .semiBold))
					.foregroundStyle(Color.white)
				SheetStockRow(spec: spec)
				Text("Your paper stake starts at today’s price and tracks the real move live, in either direction.")
					.font(StakFont.geist(12 * u))
					.lineSpacing((18 - 12) * u)
					.foregroundStyle(Disc.body)
				VStack(alignment: .leading, spacing: 12 * u) {
					HStack(spacing: 6 * u) {
						Text("Cash available")
							.font(StakFont.geist(12 * u))
							.foregroundStyle(Disc.muted)
						Text(spec.cashBefore)
							.font(StakFont.geist(12 * u, .medium))
							.foregroundStyle(Disc.brightInk)
					}
					HStack(spacing: 8 * u) {
						ForEach(Array(["$10", "$25", "$50", "$100", "Custom"].enumerated()), id: \.offset) { i, label in
							let sel = i == selected
							Button { selected = i } label: {
								Text(label)
									.font(StakFont.geist(12 * u, .medium))
									.foregroundStyle(sel ? Disc.amountSelInk : Disc.amountInk)
									.frame(maxWidth: .infinity)
									.padding(.vertical, 8 * u)
									.background(sel ? Disc.amountSelBg : Disc.amountBg, in: RoundedRectangle(cornerRadius: 10 * u))
									.overlay(
										RoundedRectangle(cornerRadius: 10 * u)
											.strokeBorder(sel ? Disc.amountSelBorder : Disc.amountBorder, lineWidth: sel ? 0.5 * u : 1 * u)
									)
							}
							.buttonStyle(.plain)
						}
					}
				}
				// Authored: chips → shares line is a 24 gap (14 + 10).
				HStack(alignment: .bottom, spacing: 6 * u) {
					Text("You get")
						.font(StakFont.geist(12 * u))
						.foregroundStyle(Disc.muted)
					Text(spec.shares)
						.font(StakFont.sora(15 * u, .semiBold))
						.foregroundStyle(Disc.brightInk)
					Text("shares of \(spec.symbol)")
						.font(StakFont.geist(12 * u))
						.foregroundStyle(Disc.muted)
				}
				.frame(maxWidth: .infinity)
				.padding(.top, 10 * u)
				VStack(spacing: 16 * u) {
					SheetCta(text: "Confirm practice buy", action: onConfirm)
					SheetSecondary(text: secondary, action: onDismiss)
				}
			}
		}
	}
}

/// "Order filled" sheet (frame 85:1205, sheet 85:1394).
struct OrderFilledSheet: View {
	let spec: BuySpec
	let onDismiss: () -> Void
	var primary: String = "View in My STAK"
	var secondary: String = "Keep exploring"

	var body: some View {
		let u = figmaUnit
		SheetScaffold(onDismiss: onDismiss) {
			VStack(spacing: 14 * u) {
				Image("IcSheetCheck")
					.resizable()
					.frame(width: 47 * u, height: 47 * u)
				Text("Order filled")
					.font(StakFont.sora(18 * u, .semiBold))
					.foregroundStyle(Color.white)
				SheetStockRow(spec: spec)
				// Authored status line (85:1407): 18-tall, left-aligned, 14 below the stock row.
				Text("Filled instantly · paper order")
					.font(StakFont.geist(14 * u))
					.lineSpacing((18 - 14) * u)
					.foregroundStyle(Disc.body)
					.frame(maxWidth: .infinity, alignment: .leading)
				HStack(spacing: 6 * u) {
					Text("Cash available")
						.font(StakFont.geist(12 * u))
						.foregroundStyle(Disc.muted)
					Text(spec.cashAfter)
						.font(StakFont.geist(12 * u, .medium))
						.foregroundStyle(Disc.brightInk)
					Spacer()
				}
				HStack(alignment: .bottom, spacing: 6 * u) {
					Text("You now hold")
						.font(StakFont.geist(12 * u))
						.foregroundStyle(Disc.muted)
					Text(spec.shares)
						.font(StakFont.sora(15 * u, .semiBold))
						.foregroundStyle(Disc.brightInk)
					Text("shares of \(spec.symbol)")
						.font(StakFont.geist(12 * u))
						.foregroundStyle(Disc.muted)
				}
				.frame(maxWidth: .infinity)
				// Authored ticket (85:1408): Cash row 0-16, Shares line at 40 -> a 24 gap.
				.padding(.top, 10 * u)
				VStack(spacing: 16 * u) {
					SheetCta(text: primary, action: onDismiss)
					SheetSecondary(text: secondary, action: onDismiss)
				}
			}
		}
	}
}

/// Buy → Order-filled flow, reused by the Stock Detail page.
struct DiscoverBuyFlow: View {
	let spec: BuySpec
	let onClose: () -> Void
	var filledPrimary: String = "View in My STAK"
	var filledSecondary: String = "Keep exploring"
	var ticketSecondary: String = "Not yet"

	@State private var filled = false

	var body: some View {
		if !filled {
			PracticeBuySheet(spec: spec, onConfirm: { filled = true }, onDismiss: onClose, secondary: ticketSecondary)
		} else {
			OrderFilledSheet(spec: spec, onDismiss: onClose, primary: filledPrimary, secondary: filledSecondary)
		}
	}
}
