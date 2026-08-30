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
	/// Authored (73:855): the sell receipt's exits, raised to the shell -
	/// Back to Simulate (the forward push) and View portfolio (dissolve 300).
	var onSellBackToSimulate: (() -> Void)? = nil
	var onSellViewPortfolio: (() -> Void)? = nil

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
					VStack(alignment: .leading, spacing: 16 * u) {
						// Authored hero card (1:4654): 350x307 r24 with 18 padding - avatar row,
						// +$24 in a 48-tall box with the .00 at 16/20, subtitle, the 343x73.5 chart
						// line bleeding 14.5 past the padding, range tabs 40 below the line.
						VStack(alignment: .leading, spacing: 0) {
							HStack(spacing: 9 * u) {
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
							HStack(alignment: .bottom, spacing: 0) {
								Text("+$24")
									.font(StakFont.sora(38 * u, .semiBold))
									.tracking(-0.38 * u)
									.foregroundStyle(Color.white)
								Text(".00")
									.font(StakFont.sora(16 * u, .semiBold))
									.foregroundStyle(Sim.muted)
									.padding(.leading, 7 * u)
									.padding(.bottom, 6 * u)
							}
							.frame(height: 48 * u, alignment: .bottom)
							.padding(.top, 11 * u)
							Text("That is up 24.0% on a $100 paper stake")
								.font(StakFont.geist(12 * u))
								.foregroundStyle(Sim.muted)
								.frame(height: 16 * u) // Authored line box is 16 — pin it so the card sums to 271
								.padding(.top, 11 * u)
							// Compose `requiredSize`: the line measures as the 314-wide content
							// row but draws its full 343x73.5, bleeding 14.5 past each side.
							Color.clear
								.frame(maxWidth: .infinity)
								.frame(height: 73.5 * u)
								.overlay(
									Image("SimChartLine")
										.resizable()
										.scaledToFit()
										.frame(width: 343 * u, height: 73.5 * u)
								)
								.padding(.top, 11 * u)
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
							.padding(.top, 40 * u)
						}
						.padding(18 * u)
						.frame(maxWidth: .infinity, alignment: .leading)
						.frame(height: 307 * u, alignment: .top)
						.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 24 * u))
						.clipShape(RoundedRectangle(cornerRadius: 24 * u))
						// Stats (1:4673): two 61-tall rows, 10 apart, 170-wide cells.
						VStack(alignment: .leading, spacing: 10 * u) {
							HStack(spacing: 10 * u) {
								StatBox(label: "This week", value: "+$3.80", valueColor: Sim.green)
								StatBox(label: "vs the market", value: "+20.8% ahead", valueColor: Sim.green)
							}
							HStack(spacing: 10 * u) {
								StatBox(label: "Price then", value: "$98.50", valueColor: Sim.bright)
								StatBox(label: "Price now", value: "$122.10", valueColor: Sim.bright)
							}
						}
						// WHY / insight card — teal-tinted like the deck tips.
						VStack(alignment: .leading, spacing: 9 * u) {
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
								.lineSpacing((17 - 12) * u)
								.foregroundStyle(Sim.body)
						}
						// Live note (1:4688): 83 tall, x14, kicker row at 12, body at 37.
						.padding(.horizontal, 14 * u)
						.padding(.vertical, 12 * u)
						.frame(maxWidth: .infinity, alignment: .leading)
						.background(Sim.tealTint, in: RoundedRectangle(cornerRadius: 16 * u))
						Button { showSell = true } label: {
							Text("Sell")
								.font(StakFont.geist(14 * u, .medium))
								.foregroundStyle(Color.white)
								.frame(maxWidth: .infinity)
								.frame(height: 51 * u)
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
					.padding(.top, 6 * u)
					.padding(.bottom, 20 * u)
				}
			}
			if showSell {
				SellFlowHost(
					// Authored (1:4698): the confirm's Back -> Pick detail, Instant.
					onClose: { showSell = false },
					onBackToSimulate: { if let onSellBackToSimulate { onSellBackToSimulate() } else { showSell = false; onBack() } },
					onViewPortfolio: { if let onSellViewPortfolio { onSellViewPortfolio() } else { showSell = false; onBack() } }
				)
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
				.font(StakFont.geist(10 * u))
				.foregroundStyle(Sim.faint)
				.frame(height: 13 * u) // Authored 10/13 line box — pin so the cell sums to 35
			Text(value)
				.font(StakFont.sora(15 * u, .semiBold))
				.foregroundStyle(valueColor)
				.frame(height: 18 * u) // Authored 15/18 line box
		}
		.padding(.horizontal, 14 * u)
		.padding(.vertical, 13 * u)
		.frame(maxWidth: .infinity, alignment: .leading)
		.frame(height: 61 * u, alignment: .top)
		.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 12 * u))
	}
}
