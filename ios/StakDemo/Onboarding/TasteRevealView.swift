import SwiftUI

/// One taste bar — label, strength word (its own gray), fill fraction of the track.
private struct TasteBar: Identifiable {
	let label: String
	let strength: String
	let strengthColor: Color
	let fraction: CGFloat
	var id: String { label }
}

/// Figma fills 286/248/165/70 over the 310px track.
private let bars: [TasteBar] = [
	TasteBar(label: "Tech curious", strength: "Strong", strengthColor: Color(argb: 0xFF69B3CA), fraction: 286 / 310),
	TasteBar(label: "Growth seeking", strength: "Strong", strengthColor: Color(argb: 0xFF69B3CA), fraction: 248 / 310),
	TasteBar(label: "Consumer brands", strength: "Medium", strengthColor: Color(argb: 0xFF819ABB), fraction: 165 / 310),
	TasteBar(label: "Income & dividends", strength: "Light", strengthColor: Color(argb: 0xFF5C6B85), fraction: 70 / 310)
]

/// Onboarding · 07 Taste reveal — Figma node 1554:9051 (CHINEDU file, "STEP 6 OF 6").
///
/// The quiz result: teal eyebrow, "Here's what you're into." headline,
/// the #181f30 taste card with four strength bars (fills animate in on
/// entry, landing on the exact Figma widths), the Risk style chip
/// ("Growth-Oriented" ›) and the "Lets go!" / Back CTAs.
struct TasteRevealView: View {
	let onBack: () -> Void
	let onLetsGo: () -> Void

	@State private var revealed = false

	var body: some View {
		let u = figmaUnit
		VStack(spacing: 0) {
			HStack {
				AuthBackCircle(action: onBack)
				Spacer()
				StepLabel(text: "STEP 6 OF 6")
			}
			.padding(.horizontal, 20 * u)
			.padding(.top, 10 * u)
			.padding(.bottom, 4 * u)

			ScrollView {
				VStack(alignment: .leading, spacing: 16 * u) {
					Text("YOUR STARTING STAK TASTE")
						.font(StakFont.geist(10 * u, .medium))
						.tracking(0.9 * u)
						.foregroundStyle(Auth.linkTeal)
					VStack(alignment: .leading, spacing: 12 * u) {
						Text("Here’s what you’re into.")
							.font(StakFont.sora(26 * u, .semiBold))
							.lineSpacing((33 - 26) * u)
							.foregroundStyle(StakColors.textPrimary)
						Text("Built from your picks. It gets smarter with every swipe.")
							.font(StakFont.geist(12 * u))
							.foregroundStyle(Auth.subtitleGray)
					}

					// Taste card — four strength bars.
					VStack(spacing: 14 * u) {
						ForEach(Array(bars.enumerated()), id: \.element.id) { index, bar in
							VStack(spacing: 6 * u) {
								HStack {
									Text(bar.label)
										.font(StakFont.geist(12 * u))
										.foregroundStyle(StakColors.textPrimary)
									Spacer()
									Text(bar.strength)
										.font(StakFont.geist(10 * u, .medium))
										.foregroundStyle(bar.strengthColor)
								}
								GeometryReader { proxy in
									ZStack(alignment: .leading) {
										RoundedRectangle(cornerRadius: 2.5 * u).fill(Auth.dividerLine)
										RoundedRectangle(cornerRadius: 2.5 * u)
											.fill(Auth.linkTeal)
											.frame(width: proxy.size.width * (revealed ? bar.fraction : 0))
											.animation(
												.easeOut(duration: 0.55).delay(0.12 * Double(index)),
												value: revealed
											)
									}
								}
								.frame(height: 5 * u)
							}
						}
					}
					.padding(16 * u)
					.background(Auth.inputBg, in: RoundedRectangle(cornerRadius: 16 * u))

					// Risk style chip.
					HStack(spacing: 10 * u) {
						VStack(alignment: .leading, spacing: 2 * u) {
							Text("Risk style")
								.font(StakFont.geist(11 * u))
								.foregroundStyle(StakColors.muted)
							Text("Growth-Oriented")
								.font(StakFont.geist(14 * u, .medium))
								.foregroundStyle(StakColors.textPrimary)
						}
						.frame(maxWidth: .infinity, alignment: .leading)
						Text("›")
							.font(StakFont.geist(15 * u, .medium))
							.foregroundStyle(Auth.linkTeal)
					}
					.padding(.leading, 16 * u)
					.padding(.trailing, 14 * u)
					.padding(.vertical, 13 * u)
					.background(Auth.inputBg, in: RoundedRectangle(cornerRadius: 14 * u))

					Text("Your deck adjusts as you swipe.")
						.font(StakFont.geist(11 * u))
						.foregroundStyle(Auth.faintText)
						.frame(maxWidth: .infinity)
				}
				.frame(maxWidth: .infinity, alignment: .leading)
				.padding(.horizontal, 24 * u)
				.padding(.top, 14 * u)
			}

			VStack(spacing: 10 * u) {
				AuthCta(text: "Lets go!", action: onLetsGo)
				AuthSecondaryButton(text: "Back", action: onBack)
			}
			.padding(.top, 8 * u)
			.padding(.bottom, 26 * u)
		}
		.background(StakColors.bg.ignoresSafeArea())
		.onAppear { revealed = true }
	}
}
