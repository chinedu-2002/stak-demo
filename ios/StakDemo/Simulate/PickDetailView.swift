import SwiftUI

/// 07 · Simulate — "Pick detail · paper" (CHINEDU 1:4631). The NVDA
/// position page: picked line, the +$24.00 gain hero, chart with range
/// pills, the This-week / vs-the-market duo, Price then/now, the WHY?
/// insight and the dark Sell CTA (raises the sell flow from here too).
/// Ported from android/ ui/simulate/PickDetailScreen.kt.
struct PickDetailView: View {
	let onBack: () -> Void

	@State private var showSell = false

	var body: some View {
		ZStack {
			VStack(spacing: 0) {
				HStack {
					AuthBackCircle(action: onBack)
					Spacer()
					Text("NVDA")
						.font(StakFont.sora(16, .semiBold))
						.foregroundStyle(Color.white)
					Spacer()
					ZStack {
						Circle().fill(Sim.cardBg)
						Image("IcNewsShare")
							.resizable()
							.frame(width: 17, height: 17)
					}
					.frame(width: 40, height: 40)
				}
				.padding(.horizontal, 18)
				.padding(.vertical, 8)

				ScrollView(showsIndicators: false) {
					VStack(alignment: .leading, spacing: 14) {
						HStack(spacing: 10) {
							ZStack {
								Circle().fill(Sim.chipBg)
								Text("N")
									.font(StakFont.sora(15, .semiBold))
									.foregroundStyle(Sim.badgeInk)
							}
							.frame(width: 38, height: 38)
							Text("Picked May 8 at $98.50")
								.font(StakFont.geist(12))
								.foregroundStyle(Sim.muted)
						}
						VStack(alignment: .leading, spacing: 4) {
							HStack(alignment: .bottom, spacing: 0) {
								Text("+$24")
									.font(StakFont.sora(44, .semiBold))
									.tracking(-0.44)
									.foregroundStyle(Color.white)
								Text(".00")
									.font(StakFont.sora(18, .semiBold))
									.foregroundStyle(Sim.muted)
									.padding(.bottom, 6)
							}
							Text("That is up 24.0% on a $100 paper stake")
								.font(StakFont.geist(12))
								.foregroundStyle(Sim.muted)
						}
						Image("SimChartLine")
							.resizable()
							.scaledToFit()
							.frame(width: 345, height: 76)
							.frame(maxWidth: .infinity)
						HStack(spacing: 37) {
							ForEach(["1D", "1W", "1M", "3M", "YTD", "1Y"], id: \.self) { label in
								if label == "3M" {
									Text(label)
										.font(StakFont.geist(12, .medium))
										.foregroundStyle(Sim.teal)
										.frame(width: 39, height: 22.5)
										.background(Color(argb: 0x292C9DBC), in: RoundedRectangle(cornerRadius: 11.25))
										.overlay(
											RoundedRectangle(cornerRadius: 11.25)
												.strokeBorder(Color(argb: 0x662C9DBC), lineWidth: 0.75)
										)
								} else {
									Text(label)
										.font(StakFont.geist(12))
										.foregroundStyle(Sim.muted)
								}
							}
						}
						.frame(maxWidth: .infinity)
						.padding(.top, 12)
						HStack(spacing: 10) {
							StatBox(label: "This week", value: "+$3.80", valueColor: Sim.green)
							StatBox(label: "vs the market", value: "+20.8% ahead", valueColor: Sim.green)
						}
						.padding(.top, 6)
						HStack(spacing: 10) {
							StatBox(label: "Price then", value: "$98.50", valueColor: Sim.bright)
							StatBox(label: "Price now", value: "$122.10", valueColor: Sim.bright)
						}
						// WHY / insight card — teal-tinted like the deck tips.
						VStack(alignment: .leading, spacing: 8) {
							HStack(spacing: 7) {
								Image("IcGistSparkle")
									.resizable()
									.frame(width: 16, height: 16)
								Text("INSIGHT")
									.font(StakFont.geist(10, .medium))
									.tracking(0.9)
									.foregroundStyle(Sim.faint)
							}
							Text("Your stake tracks the move live. If NVDA gives back gains, the dollars follow it down.")
								.font(StakFont.geist(12))
								.lineSpacing(20 - 12)
								.foregroundStyle(Sim.body)
						}
						.padding(.horizontal, 16)
						.padding(.vertical, 15)
						.frame(maxWidth: .infinity, alignment: .leading)
						.background(Sim.tealTint, in: RoundedRectangle(cornerRadius: 16))
						Spacer().frame(height: 4)
						Button { showSell = true } label: {
							Text("Sell")
								.font(StakFont.geist(14, .medium))
								.foregroundStyle(Color.white)
								.frame(maxWidth: .infinity)
								.frame(height: 52)
								.background(Sim.darkCta, in: RoundedRectangle(cornerRadius: 6))
						}
						.buttonStyle(.plain)
						Button(action: onBack) {
							Text("Back")
								.font(StakFont.sora(14))
								.foregroundStyle(Sim.muted)
								.frame(maxWidth: .infinity)
								.frame(height: 52)
								.overlay(RoundedRectangle(cornerRadius: 6).strokeBorder(Color(argb: 0x54343B4F), lineWidth: 0.36))
						}
						.buttonStyle(.plain)
					}
					.padding(.horizontal, 20)
					.padding(.top, 10)
					.padding(.bottom, 20)
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
		VStack(alignment: .leading, spacing: 4) {
			Text(label)
				.font(StakFont.geist(11))
				.foregroundStyle(Sim.faint)
			Text(value)
				.font(StakFont.sora(15, .semiBold))
				.foregroundStyle(valueColor)
		}
		.padding(.horizontal, 14)
		.padding(.vertical, 12)
		.frame(maxWidth: .infinity, alignment: .leading)
		.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 12))
	}
}
