import SwiftUI

/// Onboarding · 01 Welcome — Figma node 1554:8475 (CHINEDU file).
///
/// Value-prop intro after the splash: headline + subcopy, the blue box
/// spilling glass brand coins (the whole Figma group flattened into one
/// bundled render, glows included), and the sharp gradient "Get started"
/// CTA with the "Takes about a minute" caption.
struct IntroView: View {
	let onGetStarted: () -> Void

	var body: some View {
		let u = figmaUnit
		Artboard {
			VStack(alignment: .leading, spacing: 16 * u) {
				VStack(alignment: .leading, spacing: 12 * u) {
					Text("Find stocks you actually understand.")
						.font(StakFont.sora(26 * u, .semiBold))
						.stakLineHeight(31 * u, size: 26 * u, face: .sora)
						.foregroundStyle(StakColors.textPrimary)
					Text("STAK turns brands you already know into simple, clear stock ideas, so you can invest with confidence.")
						// Wrap-true 14x0.95: the rendered Geist shapes ~5% wider
						// than authored, and the authored break is
						// "...into simple, clear / stock ideas...".
						.font(StakFont.geist(14 * u))
						.stakLineHeight(21 * u, size: 14 * u, face: .geist)
					// RENDER-measured: the authored break is after "simple," -
					// the 342u (authored column) width pin forces the authored wrap (mirrors android).
					.frame(width: 342 * u, alignment: .leading)
						.foregroundStyle(Auth.subtitleGray)
				}
				.frame(maxWidth: .infinity, alignment: .leading)

				// Hero — flattened Figma group (render bounds 342x488 at 1x),
				// scaled to the artboard unit so proportions hold on wide devices.
				// The hero art render sits at authored y235 (template-matched
				// against 1:179); this slot tops out at 200, so offset 35 —
				// plus 3.25 measured on-device so text and art shift as one.
				Image("IntroHeroBox")
					.resizable()
					.scaledToFit()
					.frame(width: 342 * u, height: 488 * u)
					.padding(.top, 38.25 * u)
					.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
			}
			.padding(.horizontal, 24 * u)
			.padding(.top, 24 * u)

			VStack(spacing: 10 * u) {
				AuthCta(text: "Get started", action: onGetStarted)
				Text("Takes about a minute")
					.font(StakFont.geist(11 * u))
					.foregroundStyle(Auth.faintText)
			}
			.padding(.top, 8 * u)
			.padding(.bottom, 26 * u)
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}
