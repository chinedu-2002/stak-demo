import SwiftUI

/// Onboarding · 06 Preparing deck — Figma node 1:634 (CHINEDU file).
///
/// The closing loader: the box-and-coins hero (same flattened render as
/// the intro), "Building your first deck..." in Sora SemiBold 22, and a
/// live spinner with "Reading your brand picks". Auto-advances into the
/// account flow once the deck is "ready".
struct PreparingDeckView: View {
	let onDone: () -> Void

	var body: some View {
		VStack(spacing: 18) {
			Image("IntroHeroBox")
				.resizable()
				.scaledToFit()
				.frame(maxWidth: 342, maxHeight: 460)
			Text("Building your first deck...")
				.font(StakFont.sora(22, .semiBold))
				.multilineTextAlignment(.center)
				.foregroundStyle(StakColors.textPrimary)
			HStack(spacing: 5) {
				Spinner()
				Text("Reading your brand picks")
					.font(StakFont.geist(12))
					.foregroundStyle(Auth.faintText)
			}
		}
		.padding(.horizontal, 24)
		.frame(maxWidth: .infinity, maxHeight: .infinity)
		.background(StakColors.bg.ignoresSafeArea())
		.task {
			try? await Task.sleep(for: .seconds(2.6))
			onDone()
		}
	}
}

/// 14pt rotating three-quarter arc in the caption gray.
private struct Spinner: View {
	@State private var spinning = false

	var body: some View {
		Circle()
			.trim(from: 0, to: 0.75)
			.stroke(Auth.faintText, style: StrokeStyle(lineWidth: 1.6, lineCap: .round))
			.frame(width: 14, height: 14)
			.rotationEffect(.degrees(spinning ? 360 : 0))
			.animation(.linear(duration: 0.9).repeatForever(autoreverses: false), value: spinning)
			.onAppear { spinning = true }
	}
}
