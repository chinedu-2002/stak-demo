import SwiftUI

/// Onboarding · 01 Welcome — Figma node 1:179 (CHINEDU file).
///
/// Value-prop intro after the splash: headline + subcopy, the blue box
/// spilling glass brand coins (the whole Figma group flattened into one
/// bundled render, glows included), and the sharp gradient "Get started"
/// CTA with the "Takes about a minute" caption.
struct IntroView: View {
	let onGetStarted: () -> Void

	var body: some View {
		VStack(spacing: 0) {
			VStack(alignment: .leading, spacing: 16) {
				VStack(alignment: .leading, spacing: 12) {
					Text("Find stocks you actually understand.")
						.font(StakFont.sora(26, .semiBold))
						.lineSpacing(31 - 26)
						.foregroundStyle(StakColors.textPrimary)
					Text("STAK turns brands you already know into simple, clear stock ideas, so you can invest with confidence.")
						.font(StakFont.geist(14))
						.lineSpacing(21 - 14)
						.foregroundStyle(Auth.subtitleGray)
				}
				.frame(maxWidth: .infinity, alignment: .leading)

				// Hero — flattened Figma group (render bounds 342x488 at 1x).
				Image("IntroHeroBox")
					.resizable()
					.scaledToFit()
					.frame(maxWidth: 342, maxHeight: 488)
					.frame(maxWidth: .infinity, maxHeight: .infinity)
					.padding(.top, 10)
			}
			.padding(.horizontal, 24)
			.padding(.top, 24)

			VStack(spacing: 10) {
				AuthCta(text: "Get started", action: onGetStarted)
				Text("Takes about a minute")
					.font(StakFont.geist(11))
					.foregroundStyle(Auth.faintText)
			}
			.padding(.top, 8)
			.padding(.bottom, 26)
		}
		.frame(maxWidth: .infinity, maxHeight: .infinity)
		.background(StakColors.bg.ignoresSafeArea())
	}
}
