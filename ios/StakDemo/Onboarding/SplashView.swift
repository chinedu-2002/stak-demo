import SwiftUI

/// Onboarding · 00 Splash — Figma node 1:926 (CHINEDU file).
///
/// Renders the frame's exact 390x844 composition — the tilted 2K glass
/// ball (394 square rotated 48.43°), swoosh logo, "Welcome to STAK" and
/// the Geist Light subtitle — as one canvas scaled uniformly to the
/// device width, so the balance between ball, logo and type matches the
/// design on every screen. Auto-advances after a short hold; tapping
/// anywhere skips ahead.
struct SplashView: View {
	let onContinue: () -> Void

	var body: some View {
		GeometryReader { proxy in
			let scale = proxy.size.width / 390

			ZStack {
				// Glass ball — 394 square rotated 48.43°, top-left (183.1, 20.1).
				Image("SplashGlassBall")
					.resizable()
					.frame(width: 394, height: 394)
					.rotationEffect(.degrees(48.43))
					.position(x: 183.1 + 197, y: 20.1 + 197)

				VStack(spacing: 10) {
					VStack(spacing: 37) {
						Image("StakLogoMark")
							.resizable()
							.frame(width: 49.43, height: 49.43)
							.accessibilityLabel("STAK")
						Text("Welcome to STAK")
							.font(StakFont.sora(29.87, .semiBold))
							.foregroundStyle(StakColors.textPrimary)
					}
					Text("The stock market finally speaks\nyour language")
						.font(StakFont.geist(16, .light))
						.multilineTextAlignment(.center)
						.foregroundStyle(StakColors.textPrimary.opacity(0.7))
				}
				.position(x: 195.3, y: 422.71)
			}
			.frame(width: 390, height: 844)
			.scaleEffect(scale)
			.position(x: proxy.size.width / 2, y: proxy.size.height / 2)
		}
		.background(StakColors.bg)
		.ignoresSafeArea()
		.contentShape(Rectangle())
		.onTapGesture(perform: onContinue)
		.task {
			// Prototype: "After delay 1200ms" → Auth · Sign up.
			try? await Task.sleep(for: .seconds(1.2))
			onContinue()
		}
	}
}
