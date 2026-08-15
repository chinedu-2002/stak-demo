import SwiftUI

/// The three designed tutorial cards, front to back, in their Figma poses
/// (deck frame 306x423, CHINEDU nodes 1:450 / 1:411 / 1:370).
private struct TutorialCard {
	let asset: String
	let x: CGFloat
	let y: CGFloat
	let w: CGFloat
	let h: CGFloat
}

private let cards: [TutorialCard] = [
	TutorialCard(asset: "TutorialCardNVDA", x: 0, y: 47.71, w: 305.53, h: 376.9),
	TutorialCard(asset: "TutorialCardAAPL", x: 15.59, y: 21.13, w: 273.41, h: 309.46),
	TutorialCard(asset: "TutorialCardGOOGL", x: 33.57, y: 0, w: 238.66, h: 290.59)
]

/// Onboarding · 03 Swipe tutorial — Figma node 1:344 (CHINEDU file, "STEP 3 OF 6").
///
/// The stacked swipe deck: NVDA in front with AAPL and GOOGL peeking
/// behind at their designed tilts (each card is its full Figma render).
/// Swiping down flings the front card away to reveal the next; after all
/// three the deck resets so the user can keep practicing. Chevrons +
/// "Swipe down" hint under the stack, Continue/Back below.
struct SwipeTutorialView: View {
	let onBack: () -> Void
	let onContinue: () -> Void

	@State private var swiped = 0
	@State private var dragOffset: CGFloat = 0

	var body: some View {
		VStack(spacing: 0) {
			HStack {
				AuthBackCircle(action: onBack)
				Spacer()
				StepLabel(text: "STEP 3 OF 6")
			}
			.padding(.horizontal, 20)
			.padding(.top, 10)
			.padding(.bottom, 4)

			VStack(alignment: .leading, spacing: 18) {
				VStack(alignment: .leading, spacing: 12) {
					Text("Now try a few swipes.")
						.font(StakFont.sora(24, .semiBold))
						.lineSpacing(31 - 24)
						.foregroundStyle(StakColors.textPrimary)
					Text("Swipe down for the next card. Save what you like.")
						.font(StakFont.geist(12))
						.foregroundStyle(Auth.subtitleGray)
				}
				.frame(maxWidth: .infinity, alignment: .leading)

				VStack(spacing: 9) {
					// The deck — cards keep their designed poses; the front one drags down.
					ZStack(alignment: .topLeading) {
						let remaining = Array(cards.dropFirst(swiped))
						ForEach(Array(remaining.reversed().enumerated()), id: \.element.asset) { index, card in
							let isTop = index == remaining.count - 1
							Image(card.asset)
								.resizable()
								.frame(width: card.w, height: card.h)
								.offset(x: card.x, y: card.y + (isTop ? dragOffset : 0))
						}
					}
					.frame(width: 306, height: 423.07, alignment: .topLeading)
					.contentShape(Rectangle())
					.gesture(
						DragGesture()
							.onChanged { value in
								dragOffset = max(0, value.translation.height)
							}
							.onEnded { value in
								if value.translation.height > 110 {
									withAnimation(.easeIn(duration: 0.22)) {
										dragOffset = 700
									} completion: {
										swiped = (swiped + 1) % (cards.count + 1) // 3 swipes, then reset
										dragOffset = 0
									}
								} else {
									withAnimation(.easeOut(duration: 0.18)) { dragOffset = 0 }
								}
							}
					)

					// Gesture hint — twin chevrons at 50% + "Swipe down".
					VStack(spacing: 4) {
						VStack(spacing: 1) {
							ChevronDown()
							ChevronDown()
						}
						.opacity(0.5)
						Text("Swipe down")
							.font(StakFont.geist(8.73))
							.foregroundStyle(Auth.faintText)
					}
				}
				.frame(maxWidth: .infinity)
				.padding(.top, 10)

				Spacer(minLength: 0)
			}
			.padding(.horizontal, 24)
			.padding(.top, 14)

			VStack(spacing: 10) {
				AuthCta(text: "Continue", action: onContinue)
				AuthSecondaryButton(text: "Back", action: onBack)
			}
			.padding(.top, 8)
			.padding(.bottom, 26)
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}

/// 13.97x6.98 down-chevron stroke, StakColors.muted.
private struct ChevronDown: View {
	var body: some View {
		ChevronShape()
			.stroke(StakColors.muted, style: StrokeStyle(lineWidth: 1.6, lineCap: .round, lineJoin: .round))
			.frame(width: 13.97, height: 6.98)
	}
}

private struct ChevronShape: Shape {
	func path(in rect: CGRect) -> Path {
		var p = Path()
		p.move(to: CGPoint(x: 0, y: 0))
		p.addLine(to: CGPoint(x: rect.width / 2, y: rect.height))
		p.addLine(to: CGPoint(x: rect.width, y: 0))
		return p
	}
}
