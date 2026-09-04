import SwiftUI

// Palette of the Profile hub. Card surfaces here are #10182B — deliberately
// not the #181F30 onboarding card color. Mirrors android/ ProfileScreen.kt.
private let cardBg = Color(argb: 0xFF10182B)
private let bodyInk = Color(argb: 0xFFC8D2E0)
private let brightInk = Color(argb: 0xFFF2F6FC)
private let chipBg = Color(argb: 0xFF1A2333)
private let chipBorder = Color(argb: 0xFF2C9DBC)
private let chipInk = Color(argb: 0xFF7FD4E8)

private struct TasteChip {
	let label: String
	/// Authored chip width in artboard units (171:1004).
	let width: CGFloat
}

// Authored chip widths (171:1004): 97 / 94 / 125 — pinned so the
// row fills the 332 content width and the labels never wrap.
private let tasteChips = [
	TasteChip(label: "Tech Curious", width: 97),
	TasteChip(label: "High Growth", width: 94),
	TasteChip(label: "Consumer Brands", width: 125)
]

private let settingsRows = ["Notifications", "Appearance", "Linked accounts", "Help & support"]

/// 05 · Profile — "Profile · hub" (CHINEDU 171:995), reached from the
/// Home nav circle (prototype: Push Right 300ms). Avatar block, the
/// YOUR TASTE chips, the paper stats card, the settings list and the
/// Log out hairline button.
/// Every metric is scaled by the 390pt artboard unit (`figmaUnit`),
/// exactly like the Android build's `u` scaling.
/// Ports android/ ui/profile/ProfileScreen.kt.
struct ProfileView: View {
	let onBack: () -> Void
	var onLogOut: () -> Void = {}
	/// Observed (like HomeView's TopNav) so a photo picked / name typed in
	/// 09 Profile setup re-renders the avatar block.
	@ObservedObject var profile = UserProfile.shared

	var body: some View {
		let u = figmaUnit
		VStack(spacing: 0) {
			// Centered top bar — the back circle overlays the true-centered title.
			ZStack {
				Text("Profile")
					.font(StakFont.sora(16 * u, .semiBold))
					.foregroundStyle(StakColors.textPrimary)
				HStack {
					AuthBackCircle(action: onBack)
					Spacer()
				}
				.padding(.leading, 20 * u)
			}
			.frame(maxWidth: .infinity)
			.frame(height: 56 * u)

			ScrollView {
				VStack(spacing: 16 * u) {
					// Avatar block.
					VStack(spacing: 8 * u) {
						ZStack {
							Circle().fill(Color(argb: 0xFF242B3D))
							// The picked photo when one exists; else the live initial of
							// the display name - "H" for the demo persona Hamza, so the
							// authored 171:995 frame is unchanged (Codex parity audit
							// 2026-09-04; mirrors android ProfileScreen.kt).
							if let data = profile.photoData, let photo = UIImage(data: data) {
								Image(uiImage: photo)
									.resizable()
									.scaledToFill()
									.frame(width: 64 * u, height: 64 * u)
									.clipShape(Circle())
							} else {
								Text(profile.greetingName.prefix(1).uppercased())
									.font(StakFont.sora(22 * u, .semiBold))
									.foregroundStyle(Color(argb: 0xFF9EADC7))
							}
						}
						.frame(width: 64 * u, height: 64 * u)
						Text(profile.greetingName)
							.font(StakFont.sora(20 * u, .semiBold))
							.foregroundStyle(StakColors.textPrimary)
						Text("Paper investor · joined July 2026")
							.font(StakFont.geist(12 * u))
							.foregroundStyle(StakColors.muted)
					}

					// YOUR TASTE card.
					VStack(alignment: .leading, spacing: 10 * u) {
						Text("YOUR TASTE")
							.font(StakFont.geist(11 * u, .medium))
							.foregroundStyle(StakColors.muted)
						HStack(spacing: 8 * u) {
							ForEach(tasteChips, id: \.label) { chip in
								Text(chip.label)
									.font(StakFont.geist(12 * u, .medium))
									.foregroundStyle(chipInk)
									.lineLimit(1)
									.fixedSize(horizontal: true, vertical: false)
									.frame(width: chip.width * u, height: 28 * u)
									.background(chipBg, in: RoundedRectangle(cornerRadius: 14 * u))
									.overlay(
										RoundedRectangle(cornerRadius: 14 * u)
											.strokeBorder(chipBorder, lineWidth: 1 * u)
									)
							}
						}
						Text("Your taste graph sharpens with every swipe.")
							.font(StakFont.geist(12 * u))
							.lineSpacing((16 - 12) * u)
							.foregroundStyle(bodyInk)
					}
					.frame(maxWidth: .infinity, alignment: .leading)
					.padding(14 * u)
					.background(cardBg, in: RoundedRectangle(cornerRadius: 16 * u))

					// Paper stats card.
					VStack(alignment: .leading, spacing: 10 * u) {
						HStack {
							ProfileStat(value: "$10,240", label: "Portfolio")
							Spacer()
							ProfileStat(value: "$8,800.00", label: "Cash")
							Spacer()
							ProfileStat(value: "12", label: "Picks")
						}
						.frame(maxWidth: .infinity)
						.frame(height: 40 * u)
						Text("▲ +$240.00 all time on $10,000 paper")
							.font(StakFont.geist(12 * u, .medium))
							.foregroundStyle(StakColors.positive)
					}
					.frame(maxWidth: .infinity, alignment: .leading)
					.padding(14 * u)
					.background(cardBg, in: RoundedRectangle(cornerRadius: 16 * u))

					// Settings card.
					VStack(spacing: 0) {
						ForEach(settingsRows, id: \.self) { label in
							Button {
								// Settings screens land in a later phase.
							} label: {
								HStack {
									Text(label)
										.font(StakFont.geist(13 * u, .medium))
										.foregroundStyle(StakColors.textPrimary)
									Spacer()
									Text("›")
										.font(StakFont.geist(14 * u))
										.foregroundStyle(StakColors.muted)
								}
								.padding(.horizontal, 14 * u)
								.frame(maxWidth: .infinity)
								.frame(height: 48 * u)
								.contentShape(Rectangle())
							}
							.buttonStyle(.plain)
						}
					}
					.padding(.vertical, 4 * u)
					.background(cardBg, in: RoundedRectangle(cornerRadius: 16 * u))

					// Log out — hairline r6 button, same 0.36u rgba(52,59,79,0.33)
					// stroke as AuthSecondaryButton (which brings its own 20pt
					// h-padding, so it is rebuilt inline here).
					Button {
						onLogOut()
					} label: {
						Text("Log out")
							.font(StakFont.sora(14 * u))
							.foregroundStyle(StakColors.muted)
							.frame(maxWidth: .infinity)
							.frame(height: 52 * u)
							.overlay(
								RoundedRectangle(cornerRadius: 6 * u)
									.strokeBorder(Color(argb: 0x54343B4F), lineWidth: 0.36 * u)
							)
							.contentShape(Rectangle())
					}
					.buttonStyle(.plain)
				}
				.padding(.horizontal, 20 * u)
				.padding(.top, 16 * u)
				.padding(.bottom, 40 * u)
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
		let u = figmaUnit
		VStack(spacing: 4 * u) {
			Text(value)
				.font(StakFont.sora(16 * u, .semiBold))
				.foregroundStyle(brightInk)
			Text(label)
				.font(StakFont.geist(11 * u))
				.foregroundStyle(StakColors.muted)
		}
	}
}
