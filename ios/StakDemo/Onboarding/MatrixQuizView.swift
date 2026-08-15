import SwiftUI

/// One 2x2 matrix option — icon in a #242b3d circle, Geist title/subtitle.
/// Icon glyphs keep their native Figma frame size (21.06 for the goal set,
/// up to the full 37.9 circle for the risk plus/eye).
struct MatrixOption: Identifiable {
	let title: String
	let subtitle: String
	let icon: String
	var iconSize: CGFloat = 21.06
	var circleSize: CGFloat = 37.9
	var id: String { title }
}

/// Shared single-select 2x2 matrix quiz screen — the layout of Figma
/// "Onboarding · 04 Goal · matrix" (1:498) and "05 Risk matrix" (1:569):
/// step label, Sora 24/31 headline, Geist 12 subtitle, #181f30 cards
/// (163.2x155.8, r16.85) with the hairline #69b3ca selected border, and
/// Continue/Back CTAs (Continue gates on a pick).
struct MatrixQuizView: View {
	let stepLabel: String
	let headline: String
	let subtitle: String
	let options: [MatrixOption]
	let onBack: () -> Void
	let onContinue: () -> Void

	@State private var selected: String?

	var body: some View {
		VStack(spacing: 0) {
			HStack {
				AuthBackCircle(action: onBack)
				Spacer()
				StepLabel(text: stepLabel)
			}
			.padding(.horizontal, 20)
			.padding(.top, 10)
			.padding(.bottom, 4)

			ScrollView {
				VStack(alignment: .leading, spacing: 16) {
					VStack(alignment: .leading, spacing: 12) {
						Text(headline)
							.font(StakFont.sora(24, .semiBold))
							.lineSpacing(31 - 24)
							.foregroundStyle(StakColors.textPrimary)
						Text(subtitle)
							.font(StakFont.geist(12))
							.foregroundStyle(Auth.subtitleGray)
					}

					VStack(alignment: .leading, spacing: 12) {
						ForEach(0..<2) { row in
							HStack(spacing: 12.63) {
								ForEach(options[row * 2..<(row * 2 + 2)]) { option in
									MatrixCard(option: option, selected: selected == option.id) {
										selected = option.id
									}
								}
							}
						}
					}
					.padding(.top, 4)
				}
				.frame(maxWidth: .infinity, alignment: .leading)
				.padding(.horizontal, 24)
				.padding(.top, 14)
			}

			VStack(spacing: 10) {
				AuthCta(text: "Continue") {
					if selected != nil { onContinue() }
				}
				AuthSecondaryButton(text: "Back", action: onBack)
			}
			.padding(.top, 8)
			.padding(.bottom, 26)
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}

private struct MatrixCard: View {
	let option: MatrixOption
	let selected: Bool
	let action: () -> Void

	var body: some View {
		Button(action: action) {
			VStack(alignment: .leading, spacing: 21.06) {
				ZStack {
					Circle().fill(Color(argb: 0xFF242B3D))
					Image(option.icon)
						.resizable()
						.frame(width: option.iconSize, height: option.iconSize)
				}
				.frame(width: option.circleSize, height: option.circleSize)
				VStack(alignment: .leading, spacing: 5.26) {
					Text(option.title)
						.font(StakFont.geist(12, .medium))
						.foregroundStyle(StakColors.textPrimary)
						.frame(width: 142.13, alignment: .leading)
					Text(option.subtitle)
						.font(StakFont.geist(10))
						.foregroundStyle(Auth.faintText)
						.frame(width: 121.07, alignment: .leading)
				}
				.frame(height: 66.33, alignment: .top)
			}
			.padding(.horizontal, 14.74)
			.frame(width: 163.18, height: 155.81, alignment: .leading)
			.background(Auth.inputBg, in: RoundedRectangle(cornerRadius: 16.85))
			.overlay {
				if selected {
					RoundedRectangle(cornerRadius: 16.85)
						.strokeBorder(Auth.linkTeal, lineWidth: 0.53)
				}
			}
		}
		.buttonStyle(.plain)
	}
}
