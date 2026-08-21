import SwiftUI

/// Onboarding · 06 Preparing deck — Figma node 1554:8998 (CHINEDU file).
///
/// The closing loader: the box-and-coins hero (same flattened render as
/// the intro), "Building your first deck..." in Sora SemiBold 22, and a
/// live spinner with "Reading your brand picks". Auto-advances into the
/// account flow once the deck is "ready".
struct PreparingDeckView: View {
	let onDone: () -> Void

	var body: some View {
		let u = figmaUnit
		VStack(spacing: 18 * u) {
			Image("IntroHeroBox")
				.resizable()
				.scaledToFit()
				.frame(width: 342 * u, height: 488 * u)
			Text("Building your first deck...")
				.font(StakFont.sora(22 * u, .semiBold))
				.multilineTextAlignment(.center)
				.foregroundStyle(StakColors.textPrimary)
			HStack(spacing: 5 * u) {
				Spinner()
				Text("Reading your brand picks")
					.font(StakFont.geist(12 * u))
					.foregroundStyle(Auth.faintText)
			}
		}
		.padding(.horizontal, 24 * u)
		.frame(maxWidth: .infinity, maxHeight: .infinity)
		.background(StakColors.bg.ignoresSafeArea())
		.task {
			// Prototype: "After delay 1800ms" → 07 Taste reveal (dissolve).
			try? await Task.sleep(for: .seconds(1.8))
			onDone()
		}
	}
}

/// 14pt rotating three-quarter arc in the caption gray.
private struct Spinner: View {
	@State private var spinning = false

	var body: some View {
		let u = figmaUnit
		Circle()
			.trim(from: 0, to: 0.75)
			.stroke(Auth.faintText, style: StrokeStyle(lineWidth: 1.6 * u, lineCap: .round))
			.frame(width: 14 * u, height: 14 * u)
			.rotationEffect(.degrees(spinning ? 360 : 0))
			.animation(.linear(duration: 0.9).repeatForever(autoreverses: false), value: spinning)
			.onAppear { spinning = true }
	}
}
