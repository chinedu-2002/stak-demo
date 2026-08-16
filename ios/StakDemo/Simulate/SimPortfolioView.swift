import SwiftUI

/// 07 · Simulate — "Final · Portfolio · paper" (CHINEDU 1:4496) with
/// the Sell NVDA? confirm (1:4698) and Position closed (73:855)
/// sheets. Ported from android/ ui/simulate/SimPortfolioScreen.kt.
private struct SimPick {
	let badge: String
	let ticker: String
	let sub: String
	let amount: String
	let pct: String
	let up: Bool
}

private let picks: [SimPick] = [
	SimPick(badge: "N", ticker: "NVDA", sub: "Picked May 8 · up 24% since", amount: "+$24.00", pct: "+24.0%", up: true),
	SimPick(badge: "T", ticker: "TSLA", sub: "Picked Jun 3 · up 18% since", amount: "+$18.00", pct: "+18.0%", up: true),
	SimPick(badge: "A", ticker: "AMD", sub: "Picked May 29 · up 11% since", amount: "+$11.00", pct: "+11.0%", up: true),
	SimPick(badge: "A", ticker: "AAPL", sub: "Picked Apr 22 · up 6% since", amount: "+$6.00", pct: "+6.0%", up: true),
	SimPick(badge: "J", ticker: "JPM", sub: "Picked Jun 20 · up 2% since", amount: "+$2.00", pct: "+2.0%", up: true),
	SimPick(badge: "M", ticker: "MSFT", sub: "Picked Jun 26 · down 3% since", amount: "-$3.00", pct: "-3.0%", up: false)
]

struct SimPortfolioView: View {
	let onBack: () -> Void
	let onOpenPick: () -> Void

	@State private var showSell = false
	@State private var showClosed = false

	var body: some View {
		ZStack {
			VStack(spacing: 0) {
				HStack {
					AuthBackCircle(action: onBack)
					Spacer()
					Text("Your portfolio")
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
					VStack(spacing: 10) {
						HStack(alignment: .bottom) {
							VStack(alignment: .leading, spacing: 4) {
								Text("Portfolio value")
									.font(StakFont.geist(11))
									.foregroundStyle(Sim.faint)
								Text("12 picks · +$240.00 all time")
									.font(StakFont.geist(10))
									.foregroundStyle(Sim.muted)
								HStack(spacing: 6) {
									Text("Cash available")
										.font(StakFont.geist(12))
										.foregroundStyle(Sim.muted)
									Text("$8,800.00")
										.font(StakFont.geist(12, .medium))
										.foregroundStyle(Sim.bright)
								}
							}
							Spacer()
							Text("$10,240.00")
								.font(StakFont.sora(22, .semiBold))
								.foregroundStyle(Color.white)
						}
						HStack(spacing: 8) {
							FilterChip(label: "Top gainers", selected: true)
							FilterChip(label: "Newest", selected: false)
							FilterChip(label: "Worst", selected: false)
							Spacer()
						}
						.padding(.top, 4)
						ForEach(picks, id: \.ticker) { p in
							PortfolioRow(
								badge: p.badge, ticker: p.ticker, sub: p.sub,
								amount: p.amount, pct: p.pct, up: p.up,
								action: onOpenPick,
								trailing: { BuyPill(text: "Sell", action: { showSell = true }) }
							)
						}
						Text("SOLD · REALIZED")
							.font(StakFont.geist(10, .medium))
							.tracking(0.9)
							.foregroundStyle(Sim.faint)
							.frame(maxWidth: .infinity, alignment: .leading)
							.padding(.top, 8)
							.padding(.leading, 2)
						RealizedRow(badge: "S", ticker: "SHOP", sub: "Sold May 30 · profit banked", amount: "+$12.00", up: true)
						RealizedRow(badge: "C", ticker: "COIN", sub: "Sold Jun 15 · loss realized", amount: "-$8.00", up: false)
						Text("Sell a pick and the cash returns to your balance, gain or loss.")
							.font(StakFont.geist(10))
							.foregroundStyle(Sim.faint)
							.padding(.top, 8)
					}
					.padding(.horizontal, 20)
					.padding(.top, 10)
					.padding(.bottom, 20)
				}
			}
			if showSell {
				SellConfirmSheet(
					onConfirm: { showSell = false; showClosed = true },
					onDismiss: { showSell = false }
				)
			}
			if showClosed {
				PositionClosedSheet(
					onBackToSimulate: { showClosed = false; onBack() },
					onViewPortfolio: { showClosed = false }
				)
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}

private struct FilterChip: View {
	let label: String
	let selected: Bool

	var body: some View {
		Text(label)
			.font(StakFont.geist(12, .medium))
			.foregroundStyle(selected ? Sim.teal : Sim.muted)
			.padding(.horizontal, 12)
			.padding(.vertical, 6)
			.background(selected ? Sim.tealTint : Sim.cardBg, in: RoundedRectangle(cornerRadius: 14))
			.overlay(
				selected
					? RoundedRectangle(cornerRadius: 14).strokeBorder(Color(argb: 0x662C9DBC), lineWidth: 0.75)
					: nil
			)
	}
}

private struct RealizedRow: View {
	let badge: String
	let ticker: String
	let sub: String
	let amount: String
	let up: Bool

	var body: some View {
		HStack(spacing: 12) {
			ZStack {
				Circle().fill(Sim.chipBg)
				Text(badge)
					.font(StakFont.sora(14, .semiBold))
					.foregroundStyle(Sim.badgeInk)
			}
			.frame(width: 36, height: 36)
			VStack(alignment: .leading, spacing: 3) {
				Text(ticker)
					.font(StakFont.sora(12, .medium))
					.foregroundStyle(Color.white)
				Text(sub)
					.font(StakFont.geist(10))
					.foregroundStyle(Sim.muted)
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			Text(amount)
				.font(StakFont.sora(13, .semiBold))
				.foregroundStyle(up ? Sim.green : Sim.red)
		}
		.padding(.horizontal, 14)
		.padding(.vertical, 11)
		.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 12))
	}
}

/// NVDA row used by the sell sheets — teal-tinted, badge + price + change.
private struct NvdaSellRow: View {
	var body: some View {
		HStack(spacing: 11) {
			ZStack {
				Circle().fill(Sim.chipBg)
				Text("N")
					.font(StakFont.sora(15, .semiBold))
					.foregroundStyle(Sim.badgeInk)
			}
			.frame(width: 38, height: 38)
			VStack(alignment: .leading, spacing: 2) {
				Text("NVIDIA Corp")
					.font(StakFont.geist(13, .medium))
					.foregroundStyle(Color.white)
				Text("$122.10 today")
					.font(StakFont.geist(10))
					.foregroundStyle(Sim.muted)
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			Text("▲ 2.4%")
				.font(StakFont.geist(12, .medium))
				.foregroundStyle(Sim.green)
		}
		.padding(.horizontal, 14)
		.padding(.vertical, 12)
		.background(Sim.tealTint, in: RoundedRectangle(cornerRadius: 6))
	}
}

/// "Sell NVDA?" confirm sheet (1:4698).
struct SellConfirmSheet: View {
	let onConfirm: () -> Void
	let onDismiss: () -> Void

	@State private var mode = 0

	var body: some View {
		SheetScaffold(onDismiss: onDismiss) {
			VStack(alignment: .leading, spacing: 14) {
				Text("Sell NVDA?")
					.font(StakFont.sora(18, .semiBold))
					.foregroundStyle(Color.white)
				NvdaSellRow()
				Text("You hold 1.0152 shares from your $100 stake.")
					.font(StakFont.geist(12))
					.lineSpacing(18 - 12)
					.foregroundStyle(Sim.body)
				HStack(spacing: 6) {
					Text("Position value")
						.font(StakFont.geist(12))
						.foregroundStyle(Sim.muted)
					Text("$124.00")
						.font(StakFont.geist(12, .medium))
						.foregroundStyle(Sim.bright)
				}
				HStack(spacing: 8) {
					ForEach(Array(["All", "Half", "Custom"].enumerated()), id: \.offset) { i, label in
						let sel = i == mode
						Button { mode = i } label: {
							Text(label)
								.font(StakFont.geist(12, .medium))
								.foregroundStyle(sel ? Color(argb: 0xFFA6E4F7) : Color(argb: 0xFFDCE7F7))
								.frame(maxWidth: .infinity)
								.padding(.vertical, 8)
								.background(
									sel ? Color(argb: 0xFF0F2A38) : Color(argb: 0xFF0B1430),
									in: RoundedRectangle(cornerRadius: 10)
								)
								.overlay(
									RoundedRectangle(cornerRadius: 10)
										.strokeBorder(
											sel ? Color(argb: 0xFF5DA8BF) : Color(argb: 0x1FFFFFFF),
											lineWidth: sel ? 0.5 : 1
										)
								)
						}
						.buttonStyle(.plain)
					}
				}
				HStack(alignment: .lastTextBaseline, spacing: 6) {
					Spacer()
					Text("Returning")
						.font(StakFont.geist(12))
						.foregroundStyle(Sim.muted)
					Text("$124.00")
						.font(StakFont.sora(15, .semiBold))
						.foregroundStyle(Sim.bright)
					Text("to your cash")
						.font(StakFont.geist(12))
						.foregroundStyle(Sim.muted)
					Spacer()
				}
				VStack(spacing: 16) {
					// Dark navy confirm — #12203e per the frame.
					Button(action: onConfirm) {
						Text("Confirm sell")
							.font(StakFont.geist(14, .medium))
							.foregroundStyle(Color.white)
							.frame(maxWidth: .infinity)
							.frame(height: 52)
							.background(Sim.darkCta, in: RoundedRectangle(cornerRadius: 6))
					}
					.buttonStyle(.plain)
					SheetSecondary(text: "Back", action: onDismiss)
				}
			}
		}
	}
}

/// "Position closed" success sheet (73:855).
struct PositionClosedSheet: View {
	let onBackToSimulate: () -> Void
	let onViewPortfolio: () -> Void

	var body: some View {
		SheetScaffold(onDismiss: onViewPortfolio) {
			VStack(spacing: 14) {
				Image("IcSheetCheck")
					.resizable()
					.frame(width: 47, height: 47)
				Text("Position closed")
					.font(StakFont.sora(18, .semiBold))
					.foregroundStyle(Color.white)
				NvdaSellRow()
				Text("Sold 1.0152 shares from your $100 stake.")
					.font(StakFont.geist(12))
					.lineSpacing(18 - 12)
					.foregroundStyle(Sim.body)
					.frame(maxWidth: .infinity, alignment: .leading)
				HStack(spacing: 6) {
					Text("Proceeds")
						.font(StakFont.geist(12))
						.foregroundStyle(Sim.muted)
					Text("$124.00")
						.font(StakFont.geist(12, .medium))
						.foregroundStyle(Sim.bright)
					Spacer()
				}
				HStack(alignment: .lastTextBaseline, spacing: 6) {
					Text("Returned")
						.font(StakFont.geist(12))
						.foregroundStyle(Sim.muted)
					Text("$124.00")
						.font(StakFont.sora(15, .semiBold))
						.foregroundStyle(Sim.bright)
					Text("to your cash (+$24.00)")
						.font(StakFont.geist(12))
						.foregroundStyle(Sim.muted)
					Spacer()
				}
				VStack(spacing: 16) {
					SheetCta(text: "Back to Simulate", action: onBackToSimulate)
					SheetSecondary(text: "View portfolio", action: onViewPortfolio)
				}
			}
		}
	}
}

/// Sell → Position-closed flow, reused by the pick detail.
struct SellFlowHost: View {
	let onClose: () -> Void

	@State private var closed = false

	var body: some View {
		if !closed {
			SellConfirmSheet(onConfirm: { closed = true }, onDismiss: onClose)
		} else {
			PositionClosedSheet(onBackToSimulate: onClose, onViewPortfolio: onClose)
		}
	}
}
