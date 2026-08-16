import SwiftUI

// Palette of the Profile hub. Card surfaces here are #10182B — deliberately
// not the #181F30 onboarding card color. Mirrors android/ ProfileScreen.kt.
private let cardBg = Color(argb: 0xFF10182B)
private let bodyInk = Color(argb: 0xFFC8D2E0)
private let brightInk = Color(argb: 0xFFF2F6FC)
private let chipBg = Color(argb: 0xFF1A2333)
private let chipInk = Color(argb: 0xFF7FD4E8)

private let tasteChips = ["Tech Curious", "High Growth", "Consumer Brands"]
private let settingsRows = ["Notifications", "Appearance", "Linked accounts", "Help & support"]

/// 05 · Profile — "Profile · hub" (CHINEDU 171:995), reached from the
/// Home nav circle (prototype: Push Right 300ms). Avatar block, the
/// YOUR TASTE chips, the paper stats card, the settings list and the
/// Log out hairline button. Ports android/ ui/profile/ProfileScreen.kt.
struct ProfileView: View {
	let onBack: () -> Void

	var body: some View {
		VStack(spacing: 0) {
			// Centered top bar — the back circle overlays the true-centered title.
			ZStack {
				Text("Profile")
					.font(StakFont.sora(16, .semiBold))
					.foregroundStyle(StakColors.textPrimary)
				HStack {
					AuthBackCircle(action: onBack)
					Spacer()
				}
				.padding(.leading, 20)
			}
			.frame(maxWidth: .infinity)
			.frame(height: 56)

			ScrollView {
				VStack(spacing: 16) {
					// Avatar block.
					VStack(spacing: 8) {
						ZStack {
							Circle().fill(Color(argb: 0xFF242B3D))
							Text("H")
								.font(StakFont.sora(22, .semiBold))
								.foregroundStyle(Color(argb: 0xFF9EADC7))
						}
						.frame(width: 64, height: 64)
						Text("Hamza")
							.font(StakFont.sora(20, .semiBold))
							.foregroundStyle(StakColors.textPrimary)
						Text("Paper investor · joined July 2026")
							.font(StakFont.geist(12))
							.foregroundStyle(StakColors.muted)
					}

					// YOUR TASTE card.
					VStack(alignment: .leading, spacing: 10) {
						Text("YOUR TASTE")
							.font(StakFont.geist(11, .medium))
							.foregroundStyle(StakColors.muted)
						HStack(spacing: 8) {
							ForEach(tasteChips, id: \.self) { label in
								Text(label)
									.font(StakFont.geist(12, .medium))
									.foregroundStyle(chipInk)
									.padding(.horizontal, 12)
									.padding(.vertical, 6)
									.background(chipBg, in: RoundedRectangle(cornerRadius: 14))
									.overlay(
										RoundedRectangle(cornerRadius: 14)
											.strokeBorder(StakColors.accentBlue, lineWidth: 1)
									)
							}
						}
						Text("Your taste graph sharpens with every swipe.")
							.font(StakFont.geist(12))
							.foregroundStyle(bodyInk)
					}
					.frame(maxWidth: .infinity, alignment: .leading)
					.padding(14)
					.background(cardBg, in: RoundedRectangle(cornerRadius: 16))

					// Paper stats card.
					VStack(alignment: .leading, spacing: 10) {
						HStack {
							ProfileStat(value: "$10,240", label: "Portfolio")
							Spacer()
							ProfileStat(value: "$8,800.00", label: "Cash")
							Spacer()
							ProfileStat(value: "12", label: "Picks")
						}
						.frame(maxWidth: .infinity)
						Text("▲ +$240.00 all time on $10,000 paper")
							.font(StakFont.geist(12, .medium))
							.foregroundStyle(StakColors.positive)
					}
					.frame(maxWidth: .infinity, alignment: .leading)
					.padding(14)
					.background(cardBg, in: RoundedRectangle(cornerRadius: 16))

					// Settings card.
					VStack(spacing: 0) {
						ForEach(settingsRows, id: \.self) { label in
							Button {
								// Settings screens land in a later phase.
							} label: {
								HStack {
									Text(label)
										.font(StakFont.geist(13, .medium))
										.foregroundStyle(StakColors.textPrimary)
									Spacer()
									Text("›")
										.font(StakFont.geist(14))
										.foregroundStyle(StakColors.muted)
								}
								.padding(.horizontal, 14)
								.frame(maxWidth: .infinity)
								.frame(height: 48)
								.contentShape(Rectangle())
							}
							.buttonStyle(.plain)
						}
					}
					.padding(.vertical, 4)
					.background(cardBg, in: RoundedRectangle(cornerRadius: 16))

					// Log out — hairline r6 button, same 0.36pt rgba(52,59,79,0.33)
					// stroke as AuthSecondaryButton (which brings its own 20pt
					// h-padding, so it is rebuilt inline here).
					Button {
						// Sign-out wiring comes with Firebase.
					} label: {
						Text("Log out")
							.font(StakFont.sora(14))
							.foregroundStyle(StakColors.muted)
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
				.padding(.horizontal, 20)
				.padding(.top, 16)
				.padding(.bottom, 40)
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}

/// One stat column — Sora SemiBold 16 value over a Geist 11 muted label.
private struct ProfileStat: View {
	let value: String
	let label: String

	var body: some View {
		VStack(spacing: 4) {
			Text(value)
				.font(StakFont.sora(16, .semiBold))
				.foregroundStyle(brightInk)
			Text(label)
				.font(StakFont.geist(11))
				.foregroundStyle(StakColors.muted)
		}
	}
}
