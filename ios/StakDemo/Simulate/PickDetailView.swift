import SwiftUI

/// 07 · Simulate — "Pick detail · paper" (CHINEDU 1:4631). The NVDA
/// position page: picked line, the +$24.00 gain hero, chart with range
/// pills, the This-week / vs-the-market duo, Price then/now, the WHY?
/// insight and the dark Sell CTA (raises the sell flow from here too).
/// Ported from android/ ui/simulate/PickDetailScreen.kt. Every metric
/// is scaled by the 390pt artboard unit (`figmaUnit`), exactly like
/// the Android build's `u` scaling.
struct PickDetailView: View {
	let onBack: () -> Void

	@State private var showSell = false

	var body: some View {
		let u = figmaUnit
		ZStack {
			VStack(spacing: 0) {
				HStack {
					AuthBackCircle(action: onBack)
					Spacer()
					Text("NVDA")
						.font(StakFont.sora(16 * u, .semiBold))
						.foregroundStyle(Color.white)
					Spacer()
					ZStack {
						Circle().fill(Sim.cardBg)
						Image("IcNewsShare")
							.resizable()
							.frame(width: 17 * u, height: 17 * u)
					}
					.frame(width: 40 * u, height: 40 * u)
				}
				.padding(.horizontal, 18 * u)
				.padding(.vertical, 8 * u)

				ScrollView(showsIndicators: false) {
					VStack(alignment: .leading, spacing: 14 * u) {
						HStack(spacing: 10 * u) {
							ZStack {
								Circle().fill(Sim.chipBg)
								Text("N")
									.font(StakFont.sora(15 * u, .semiBold))
									.foregroundStyle(Sim.badgeInk)
							}
							.frame(width: 38 * u, height: 38 * u)
							Text("Picked May 8 at $98.50")
								.font(StakFont.geist(12 * u))
								.foregroundStyle(Sim.muted)
						}
						VStack(alignment: .leading, spacing: 4 * u) {
							HStack(alignment: .bottom, spacing: 0) {
								Text("+$24")
									.font(StakFont.sora(44 * u, .semiBold))
									.tracking(-0.44 * u)
									.foregroundStyle(Color.white)
								Text(".00")
									.font(StakFont.sora(18 * u, .semiBold))
									.foregroundStyle(Sim.muted)
									// Same authored offsets as the Simulate hero: 8 gap, 8 up.
									.padding(.leading, 8 * u)
									.padding(.bottom, 8 * u)
							}
							Text("That is up 24.0% on a $100 paper stake")
								.font(StakFont.geist(12 * u))
								.foregroundStyle(Sim.muted)
						}
						Image("SimChartLine")
							.resizable()
							.scaledToFit()
							.frame(width: 345 * u, height: 76 * u)
							.frame(maxWidth: .infinity)
						HStack(spacing: 37 * u) {
							ForEach(["1D", "1W", "1M", "3M", "YTD", "1Y"], id: \.self) { label in
								if label == "3M" {
									Text(label)
										.font(StakFont.geist(12 * u, .medium))
										.foregroundStyle(Sim.teal)
										.frame(width: 39 * u, height: 22.5 * u)
										.background(Color(argb: 0x292C9DBC), in: RoundedRectangle(cornerRadius: 11.25 * u))
										.overlay(
											RoundedRectangle(cornerRadius: 11.25 * u)
												.strokeBorder(Color(argb: 0x662C9DBC), lineWidth: 0.75 * u)
										)
								} else {
									Text(label)
										.font(StakFont.geist(12 * u))
										.foregroundStyle(Sim.muted)
								}
							}
						}
						.frame(maxWidth: .infinity)
						.padding(.top, 12 * u)
						HStack(spacing: 10 * u) {
							StatBox(label: "This week", value: "+$3.80", valueColor: Sim.green)
							StatBox(label: "vs the market", value: "+20.8% ahead", valueColor: Sim.green)
						}
						.padding(.top, 6 * u)
						HStack(spacing: 10 * u) {
							StatBox(label: "Price then", value: "$98.50", valueColor: Sim.bright)
							StatBox(label: "Price now", value: "$122.10", valueColor: Sim.bright)
						}
						// WHY / insight card — teal-tinted like the deck tips.
						VStack(alignment: .leading, spacing: 8 * u) {
							HStack(spacing: 7 * u) {
								Image("IcGistSparkle")
									.resizable()
									.frame(width: 16 * u, height: 16 * u)
								Text("INSIGHT")
									.font(StakFont.geist(10 * u, .medium))
									.tracking(0.9 * u)
									.foregroundStyle(Sim.faint)
							}
							Text("Your stake tracks the move live. If NVDA gives back gains, the dollars follow it down.")
								.font(StakFont.geist(12 * u))
								.lineSpacing((20 - 12) * u)
								.foregroundStyle(Sim.body)
						}
						.padding(.horizontal, 16 * u)
						.padding(.vertical, 15 * u)
						.frame(maxWidth: .infinity, alignment: .leading)
						.background(Sim.tealTint, in: RoundedRectangle(cornerRadius: 16 * u))
						Spacer().frame(height: 4 * u)
						Button { showSell = true } label: {
							Text("Sell")
								.font(StakFont.geist(14 * u, .medium))
								.foregroundStyle(Color.white)
								.frame(maxWidth: .infinity)
								.frame(height: 52 * u)
								.background(Sim.darkCta, in: RoundedRectangle(cornerRadius: 6 * u))
						}
						.buttonStyle(.plain)
						Button(action: onBack) {
							Text("Back")
								.font(StakFont.sora(14 * u))
								.foregroundStyle(Sim.muted)
								.frame(maxWidth: .infinity)
								.frame(height: 52 * u)
								.overlay(
									RoundedRectangle(cornerRadius: 6 * u)
										.strokeBorder(Color(argb: 0x54343B4F), lineWidth: 0.36 * u)
								)
						}
						.buttonStyle(.plain)
					}
					.padding(.horizontal, 20 * u)
					.padding(.top, 10 * u)
					.padding(.bottom, 20 * u)
				}
			}
			if showSell {
				// Selling from here routes back through the portfolio page.
				SellFlowHost(onClose: { showSell = false; onBack() })
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}

private struct StatBox: View {
	let label: String
	let value: String
	let valueColor: Color

	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 4 * u) {
			Text(label)
				.font(StakFont.geist(11 * u))
				.foregroundStyle(Sim.faint)
			Text(value)
				.font(StakFont.sora(15 * u, .semiBold))
				.foregroundStyle(valueColor)
		}
		.padding(.horizontal, 14 * u)
		.padding(.vertical, 12 * u)
		.frame(maxWidth: .infinity, alignment: .leading)
		.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 12 * u))
	}
}
