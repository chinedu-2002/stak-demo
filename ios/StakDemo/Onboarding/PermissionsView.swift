import SwiftUI

/// Onboarding · 08 Permissions — Figma node 1:749 (CHINEDU file,
/// "STEP · ALMOST THERE").
///
/// Post-signup permissions ask: notifications + account security rows as
/// #181f30 cards with the teal 42x24 toggles (both on by default), the
/// settings footnote, and the gradient "Allow and continue" CTA over the
/// hairline "Not now".
struct PermissionsView: View {
	let onBack: () -> Void
	let onContinue: () -> Void

	@State private var notifications = true
	@State private var accountSecurity = true

	var body: some View {
		VStack(spacing: 0) {
			HStack {
				AuthBackCircle(action: onBack)
				Spacer()
			}
			.padding(.horizontal, 20)
			.padding(.top, 10)
			.padding(.bottom, 4)
			OnboardingKicker(text: "STEP · ALMOST THERE")

			VStack(alignment: .leading, spacing: 18) {
				VStack(alignment: .leading, spacing: 12) {
					Text("Stay in the loop")
						.font(StakFont.sora(26, .semiBold))
						.foregroundStyle(StakColors.textPrimary)
					Text("Two quick permissions so STAK can alert you and keep your account secure.")
						.font(StakFont.geist(12))
						.foregroundStyle(Auth.subtitleGray)
						.frame(width: 276, alignment: .leading)
				}

				PermissionCard(
					title: "Notifications",
					description: "Price moves on your picks and your daily deck.",
					isOn: $notifications
				)
				PermissionCard(
					title: "Account security",
					description: "Face ID keeps your account locked to you.",
					isOn: $accountSecurity
				)

				Text("You can change these anytime in Settings.")
					.font(StakFont.geist(11))
					.foregroundStyle(Auth.faintText)

				Spacer(minLength: 0)
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			.padding(.horizontal, 24)
			.padding(.top, 14)

			VStack(spacing: 10) {
				AuthCta(text: "Allow and continue", action: onContinue)
				AuthSecondaryButton(text: "Not now", action: onContinue)
			}
			.padding(.top, 8)
			.padding(.bottom, 26)
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}

/// One permission row — #181f30 r14 card, copy column + the 42x24 toggle.
private struct PermissionCard: View {
	let title: String
	let description: String
	@Binding var isOn: Bool

	var body: some View {
		HStack(spacing: 12) {
			VStack(alignment: .leading, spacing: 4) {
				Text(title)
					.font(StakFont.geist(14, .medium))
					.foregroundStyle(StakColors.textPrimary)
				Text(description)
					.font(StakFont.geist(11))
					.foregroundStyle(Auth.subtitleGray)
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			StakToggle(isOn: $isOn)
		}
		.padding(16)
		.background(Auth.inputBg, in: RoundedRectangle(cornerRadius: 14))
	}
}

/// 42x24 Figma toggle — #2c9dbc track when on, white 18pt thumb 3pt from
/// the edge. The design's metrics deviate from the 51x31 system Toggle,
/// so this is deliberately custom (same as the Android build).
struct StakToggle: View {
	@Binding var isOn: Bool

	var body: some View {
		Button {
			withAnimation(.easeInOut(duration: 0.15)) { isOn.toggle() }
		} label: {
			ZStack(alignment: isOn ? .trailing : .leading) {
				RoundedRectangle(cornerRadius: 12)
					.fill(isOn ? StakColors.accentBlue : Color(argb: 0xFF242B3D))
				Circle()
					.fill(Color.white)
					.frame(width: 18, height: 18)
					.padding(3)
			}
			.frame(width: 42, height: 24)
		}
		.buttonStyle(.plain)
		.accessibilityAddTraits(.isToggle)
		.accessibilityValue(isOn ? "On" : "Off")
	}
}
