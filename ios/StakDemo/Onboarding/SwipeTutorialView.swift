import SwiftUI

/// One deck card's designed content (art + copy at the front-card scale) —
/// the shared Discover card grammar (template 1:1740). Mirrors DECK in
/// android/ ui/discover/DiscoverScreen.kt (private on iOS, so restated here).
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

// The tutorial deck (1:344) is the Discover deck at 87.4% — the same
// authored queue slabs behind a live front card built from the shared
// Discover card template. Slab poses template-matched to the frame.
private let deckScale: CGFloat = 305.75 / 350

/// Onboarding · 03 Swipe tutorial — Figma node 1:344 (CHINEDU file, "STEP 3 OF 6").
///
/// The stacked swipe deck: the authored queue slabs (AAPL and GOOGL at
/// their designed tilts) behind a live front card from the Discover
/// template at the frame's 87.4% scale. Swiping down reshuffles: the
/// front card flies off to the back of the queue and the next design
/// takes the front row, cycling in order. Chevrons + "Swipe down" hint
/// under the stack, Continue/Back below.
/// Ported from android/ ui/onboarding/SwipeTutorialScreen.kt.
struct SwipeTutorialView: View {
	let onBack: () -> Void
	let onContinue: () -> Void

	@State private var swiped = 0
	@State private var topOffset: CGFloat = 0
	@State private var promote: CGFloat = 0
	@State private var enter: CGFloat = 1
	@State private var entering = false

	var body: some View {
		let u = figmaUnit
		let u2 = u * deckScale
		Artboard {
			HStack {
				AuthBackCircle(action: onBack)
				Spacer()
				Text("STEP 3 OF 6")
					.font(StakFont.geist(10 * u, .medium))
					.tracking(0.9 * u)
					.foregroundStyle(Auth.faintText)
			}
			.padding(.horizontal, 20 * u)
			.padding(.top, 10 * u)
			.padding(.bottom, 4 * u)

			VStack(alignment: .leading, spacing: 18 * u) {
				VStack(alignment: .leading, spacing: 12 * u) {
					Text("Now try a few swipes.")
						.font(StakFont.sora(24 * u, .semiBold))
						.lineSpacing((31 - 24) * u)
						.foregroundStyle(StakColors.textPrimary)
					Text("Swipe down for the next card. Save what you like.")
						.font(StakFont.geist(12 * u))
						.foregroundStyle(Auth.subtitleGray)
				}
				.frame(maxWidth: .infinity, alignment: .leading)

				VStack(spacing: 0) {
					// The deck — the authored queue slabs stay put while the live
					// front card cycles. User's motion (2026-08-21): swipe down
					// reshuffles the front card to the back and the next takes the
					// front row in an organized sequence — the Discover grammar.
					ZStack(alignment: .topLeading) {
						Image("TutorialCardGOOGL")
							.resizable()
							.frame(width: 238.75 * u, height: 290.75 * u)
							.offset(x: 33.5 * u, y: 0)
						Image("TutorialCardAAPL")
							.resizable()
							.frame(width: 273.5 * u, height: 309.5 * u)
							.offset(x: 15.5 * u, y: 21 * u)
						FrontDeckCard(card: deck[swiped % 3], onSave: {}, u: u2)
							.scaleEffect(0.97 + 0.03 * enter)
							.opacity((1 - promote) * enter)
							.frame(maxWidth: .infinity, alignment: .top)
							.offset(y: 47.5 * u + topOffset)
					}
					.frame(width: 306 * u, height: 423.07 * u, alignment: .topLeading)
					.clipped()
					.contentShape(Rectangle())
					.gesture(
						DragGesture()
							.onChanged { value in
								if promote == 0 && !entering {
									topOffset = max(0, value.translation.height)
								}
							}
							.onEnded { _ in
								guard promote == 0 && !entering else { return }
								if topOffset > 110 * u2 {
									// The frame's card shuffle: the swiped card flies
									// off fading while the next design takes the front.
									withAnimation(.easeOut(duration: 0.28)) { topOffset = 500 * u2 }
									withAnimation(.easeOut(duration: 0.3)) {
										promote = 1
									} completion: {
										entering = true
										swiped += 1
										topOffset = 0
										promote = 0
										enter = 0
									}
								} else {
									// Compose tween(180) — default FastOutSlowIn curve.
									withAnimation(.timingCurve(0.4, 0, 0.2, 1, duration: 0.18)) { topOffset = 0 }
								}
							}
					)
					.onChange(of: swiped) { _, _ in
						// Next design enters: alpha + scale 0.97 -> 1, 200ms ease-out.
						withAnimation(.easeOut(duration: 0.2)) {
							enter = 1
						} completion: {
							entering = false
						}
					}

					Spacer().frame(height: 9 * u)

					// Gesture hint — twin chevrons at 50% + "Swipe down".
					VStack(spacing: 4 * u) {
						VStack(spacing: 1 * u) {
							ChevronDown(u: u)
							ChevronDown(u: u)
						}
						.opacity(0.5)
						Text("Swipe down")
							.font(StakFont.geist(8.73 * u))
							.foregroundStyle(Auth.faintText)
					}

					Spacer(minLength: 0)
				}
				.frame(maxWidth: .infinity)
				.padding(.top, 10 * u)
			}
			.padding(.horizontal, 24 * u)
			.padding(.top, 14 * u)

			VStack(spacing: 10 * u) {
				AuthCta(text: "Continue", action: onContinue)
				AuthSecondaryButton(text: "Back", action: onBack)
			}
			.padding(.top, 8 * u)
			.padding(.bottom, 26 * u)
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}

/// The full-size front card (350-wide template) with its live Save chip.
/// The deck's layer structure: every boundary in the frame is a
/// brightness step plus a thin dark rim (the authored exports carry it).
/// NVDA's dark chrome makes its own step; light-topped cards need the
/// rim — a tight dark seam hugging the edge — so the card reads as its
/// own layer over the queue in EVERY state. Mirrors FrontDeckCard in
/// android/ ui/discover/DiscoverScreen.kt at the passed unit.
private struct FrontDeckCard: View {
	let card: DeckCard
	let onSave: () -> Void
	let u: CGFloat

	var body: some View {
		DeckCardBody(card: card, onSave: onSave, u: u)
			.background {
				// Concentric 1pt strokes fading out over a 6u reach.
				let reach = 6 * u
				ForEach(Array(stride(from: CGFloat(0), to: reach, by: 1)), id: \.self) { d in
					let t = d / reach
					RoundedRectangle(cornerRadius: 22 * u + d)
						.stroke(Color(argb: 0xFF060B16).opacity(0.5 * (1 - t) * (1 - t)), lineWidth: 1)
						.padding(-d)
				}
			}
	}
}

/// The authored card template (1:1740, shared by all three designs):
/// art 340x229 at y4, overlay at 258 -> gap 25 — every literal at the
/// passed unit so the tutorial renders it at the frame's 87.4% scale.
private struct DeckCardBody: View {
	let card: DeckCard
	let onSave: (() -> Void)?
	let u: CGFloat

	var body: some View {
		VStack(spacing: 25 * u) {
			ZStack(alignment: .topTrailing) {
				Image(card.art)
					.resizable()
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
			// This frame's card authors looser text gaps than a uniform 87.4%
			// scale of the Discover card (1:1627) — the extra row paddings are
			// render-fitted against the 2x export of 1:344 (Android ca6e3de).
			VStack(alignment: .leading, spacing: 19 * u) {
				VStack(alignment: .leading, spacing: 8 * u) {
					Text(card.ticker)
						.font(StakFont.geist(10 * u))
						.foregroundStyle(Disc.muted)
					Text(card.headline)
						.font(StakFont.geist(16 * u))
						.lineSpacing((23 - 16) * u)
						.foregroundStyle(Color.white)
						.padding(.top, 0.85 * u)
					HStack(alignment: .bottom, spacing: 9 * u) {
						Text(card.price)
							.font(StakFont.sora(20 * u, .semiBold))
							.foregroundStyle(Color.white)
						Text(card.change)
							.font(StakFont.geist(11 * u, .medium))
							.foregroundStyle(Disc.green)
							.padding(.bottom, 2 * u)
					}
					.padding(.top, 1.65 * u)
				}
				HStack(spacing: 8 * u) {
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
				.padding(.top, 0.4 * u)
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			.padding(.horizontal, 18 * u)
			.padding(.bottom, 16 * u)
			.padding(.top, 0.85 * u)
		}
		.padding(.top, 4 * u)
		.padding(.bottom, 4 * u)
		.frame(width: 350 * u)
		.background(
			LinearGradient(colors: [card.cardTop, Color(argb: 0xFF0C1526)], startPoint: .top, endPoint: .bottom),
			in: RoundedRectangle(cornerRadius: 22 * u)
		)
	}
}

/// rgba(255,255,255,0.09) Save pill with the small bookmark.
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
		// 1:402 / 1:441 / 1:469 author the chip at rgba(255,255,255,0.09); was 0.15 (exact-design audit 2026-09-04).
		.background(Color(argb: 0x17FFFFFF), in: RoundedRectangle(cornerRadius: 16 * u))
	}
}

/// The authored 13.97x6.98 down-chevron (1:487 / 1:489): the exported
/// vector - stroke #5C6B85 at 1.75, round caps, the glyph inset inside
/// its frame. Was a hand-drawn corner-to-corner #819ABB 1.6 stroke
/// (exact-design audit 2026-09-04).
private struct ChevronDown: View {
	let u: CGFloat

	var body: some View {
		Image("SwipeChevron")
			.resizable()
			.frame(width: 13.97 * u, height: 6.98 * u)
	}
}
