import SwiftUI
import PhotosUI

private let nameMax = 20

/// Onboarding · 09 Profile setup — Figma node 1:793 (CHINEDU file,
/// "STEP · LAST ONE"). Mirrors android ui/onboarding/ProfileSetupScreen.kt.
///
/// Final onboarding step: 96pt #242b3d avatar circle with the teal ring
/// and the display-name initial, "Add a photo" link, the DISPLAY NAME
/// input card with its live "n / 20" counter, and the gradient
/// "Proceed to home" CTA. Both the avatar circle and the link open the
/// system photo picker and the chosen image becomes the avatar; the
/// picker carries its own permission flow, so the app requests none.
struct ProfileSetupView: View {
	let onBack: () -> Void
	let onProceed: () -> Void

	@State private var name = ""
	@State private var showPhotoPicker = false
	@State private var pickedItem: PhotosPickerItem? = nil
	@State private var photoData: Data? = nil

	var body: some View {
		let u = figmaUnit
		Artboard {
			HStack {
				AuthBackCircle(action: onBack)
				Spacer()
			}
			.padding(.horizontal, 20 * u)
			.padding(.top, 10 * u)
			.padding(.bottom, 4 * u)
			OnboardingKicker(text: "STEP · LAST ONE")

			VStack(alignment: .leading, spacing: 18 * u) {
				VStack(alignment: .leading, spacing: 12 * u) {
					Text("Make it yours")
						.font(StakFont.sora(26 * u, .semiBold))
						.foregroundStyle(StakColors.textPrimary)
					Text("Pick a name and photo. This is how you’ll show up on leaderboards.")
						.font(StakFont.geist(12 * u))
						.foregroundStyle(Auth.subtitleGray)
						.frame(width: 276 * u, alignment: .leading)
				}

				// Avatar — 96pt #242b3d circle, 2pt teal ring, Sora 36 initial.
				// Tapping the circle (or the link below) opens the photo picker;
				// the picked image fills the circle under the ring.
				VStack(spacing: 10 * u) {
					Button(action: { showPhotoPicker = true }) {
						ZStack {
							Circle().fill(Color(argb: 0xFF242B3D))
							if let photoData, let photo = UIImage(data: photoData) {
								Image(uiImage: photo)
									.resizable()
									.scaledToFill()
									.frame(width: 96 * u, height: 96 * u)
									.clipShape(Circle())
							} else {
								Text(name.first.map { String($0).uppercased() } ?? "")
									.font(StakFont.sora(36 * u, .semiBold))
									.foregroundStyle(Color(argb: 0xFF9EADC7))
							}
							Circle().strokeBorder(Auth.linkTeal, lineWidth: 2 * u)
						}
						.frame(width: 96 * u, height: 96 * u)
					}
					.buttonStyle(.plain)
					.accessibilityLabel(photoData == nil ? "Add a photo" : "Profile photo")
					Button(action: { showPhotoPicker = true }) {
						Text("Add a photo")
							.font(StakFont.geist(12 * u, .medium))
							.foregroundStyle(Auth.linkTeal)
					}
					.buttonStyle(.plain)
				}
				.frame(maxWidth: .infinity)
				.padding(.vertical, 6 * u)

				Text("DISPLAY NAME")
					.font(StakFont.geist(10 * u, .medium))
					.tracking(1.2 * u)
					.foregroundStyle(Auth.faintText)

				// Name input — #181f30 r14 card with the live "n / 20" counter.
				HStack {
					TextField("", text: $name)
						.font(StakFont.geist(14 * u))
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
						.font(StakFont.geist(11 * u))
						.foregroundStyle(Auth.faintText)
				}
				.padding(16 * u)
				.background(Auth.inputBg, in: RoundedRectangle(cornerRadius: 14 * u))

				Text("You can change this anytime in Profile.")
					.font(StakFont.geist(11 * u))
					.foregroundStyle(Auth.faintText)

				Spacer(minLength: 0)
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			.padding(.horizontal, 24 * u)
			.padding(.top, 14 * u)

			VStack(spacing: 0) {
				AuthCta(text: "Proceed to home", action: {
					UserProfile.shared.displayName = name.trimmingCharacters(in: .whitespaces).capitalizedWords
					UserProfile.shared.photoData = photoData
					onProceed()
				})
			}
			.padding(.top, 8 * u)
			.padding(.bottom, 26 * u)
		}
		.background(StakColors.bg.ignoresSafeArea())
		.photosPicker(isPresented: $showPhotoPicker, selection: $pickedItem, matching: .images)
		.onChange(of: pickedItem) { _, item in
			guard let item else { return }
			Task {
				if let data = try? await item.loadTransferable(type: Data.self) {
					photoData = data
				}
			}
		}
	}
}
