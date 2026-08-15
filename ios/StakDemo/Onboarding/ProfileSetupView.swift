import SwiftUI

private let nameMax = 20

/// Onboarding · 09 Profile setup — Figma node 1:793 (CHINEDU file,
/// "STEP · LAST ONE").
///
/// Final onboarding step: 96pt #242b3d avatar circle with the teal ring
/// and the display-name initial, "Add a photo" link, the DISPLAY NAME
/// input card with its live "n / 20" counter, and the gradient
/// "Proceed to home" CTA.
struct ProfileSetupView: View {
	let onBack: () -> Void
	let onProceed: () -> Void

	@State private var name = ""

	var body: some View {
		VStack(spacing: 0) {
			HStack {
				AuthBackCircle(action: onBack)
				Spacer()
			}
			.padding(.horizontal, 20)
			.padding(.top, 10)
			.padding(.bottom, 4)
			OnboardingKicker(text: "STEP · LAST ONE")

			VStack(alignment: .leading, spacing: 18) {
				VStack(alignment: .leading, spacing: 12) {
					Text("Make it yours")
						.font(StakFont.sora(26, .semiBold))
						.foregroundStyle(StakColors.textPrimary)
					Text("Pick a name and photo. This is how you’ll show up on leaderboards.")
						.font(StakFont.geist(12))
						.foregroundStyle(Auth.subtitleGray)
						.frame(width: 276, alignment: .leading)
				}

				// Avatar — 96pt #242b3d circle, 2pt teal ring, Sora 36 initial.
				VStack(spacing: 10) {
					ZStack {
						Circle().fill(Color(argb: 0xFF242B3D))
						Circle().strokeBorder(Auth.linkTeal, lineWidth: 2)
						Text(name.first.map { String($0).uppercased() } ?? "")
							.font(StakFont.sora(36, .semiBold))
							.foregroundStyle(Color(argb: 0xFF9EADC7))
					}
					.frame(width: 96, height: 96)
					Button(action: { /* photo picker not designed yet */ }) {
						Text("Add a photo")
							.font(StakFont.geist(12, .medium))
							.foregroundStyle(Auth.linkTeal)
					}
					.buttonStyle(.plain)
				}
				.frame(maxWidth: .infinity)
				.padding(.vertical, 6)

				Text("DISPLAY NAME")
					.font(StakFont.geist(10, .medium))
					.tracking(1.2)
					.foregroundStyle(Auth.faintText)

				// Name input — #181f30 r14 card with the live "n / 20" counter.
				HStack {
					TextField("", text: $name)
						.font(StakFont.geist(14))
						.foregroundStyle(StakColors.textPrimary)
						.tint(StakColors.accent)
						.textInputAutocapitalization(.words)
						.autocorrectionDisabled()
						.onChange(of: name) { _, newValue in
							if newValue.count > nameMax {
								name = String(newValue.prefix(nameMax))
							}
						}
					Text("\(name.count) / \(nameMax)")
						.font(StakFont.geist(11))
						.foregroundStyle(Auth.faintText)
				}
				.padding(16)
				.background(Auth.inputBg, in: RoundedRectangle(cornerRadius: 14))

				Text("You can change this anytime in Profile.")
					.font(StakFont.geist(11))
					.foregroundStyle(Auth.faintText)

				Spacer(minLength: 0)
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			.padding(.horizontal, 24)
			.padding(.top, 14)

			VStack(spacing: 0) {
				AuthCta(text: "Proceed to home", action: onProceed)
			}
			.padding(.top, 8)
			.padding(.bottom, 26)
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}
