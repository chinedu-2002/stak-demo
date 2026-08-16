import SwiftUI

/// 07 · Simulate — "Simulate home · paper" (CHINEDU 1:3898) with the
/// Buy-PLTR ticket (1:4232) and Order filled (85:895). Ported from
/// android/ ui/simulate/SimulateScreen.kt.
enum Sim {
	static let cardBg = Color(argb: 0xFF181F30)
	static let muted = Color(argb: 0xFF819ABB)
	static let faint = Color(argb: 0xFF5C6B85)
	static let body = Color(argb: 0xFFC8D2E0)
	static let green = Color(argb: 0xFF2FD08A)
	static let red = Color(argb: 0xFFFF5A6A)
	static let teal = Color(argb: 0xFF69B3CA)
	static let tealTint = Color(argb: 0x1A69B3CA)
	static let chipBg = Color(argb: 0xFF242B3D)
	static let track = Color(argb: 0xFF2A3346)
	static let badgeInk = Color(argb: 0xFF9EADC7)
	static let bright = Color(argb: 0xFFF2F6FC)
	static let headerGray = Color(argb: 0xFFD3D3DD)
	static let darkCta = Color(argb: 0xFF12203E)
	static let ctaBorder = Color(argb: 0xA1659EAD)
}

let pltrBuy = BuySpec(
	title: "Buy PLTR?", badge: "P", name: "Palantir Technologies", priceLine: "$28.40 today",
	change: "▲ 1.1%", cashBefore: "$8,800.00", cashAfter: "$8,775.00", shares: "0.8803", symbol: "PLTR"
)

struct SimulateView: View {
	let onOpenPortfolio: () -> Void
	let onOpenPick: () -> Void
	let onOpenLeaderboard: () -> Void

	@State private var showBuy = false

	var body: some View {
		ZStack {
			VStack(spacing: 0) {
				HStack {
					VStack(alignment: .leading, spacing: 3) {
						Text("Simulate")
							.font(StakFont.sora(26, .semiBold))
							.foregroundStyle(Color.white)
						Text("Pick from your saves. Paper money does the talking.")
							.font(StakFont.geist(12))
							.foregroundStyle(Sim.muted)
					}
					Spacer()
					ZStack {
						Circle().fill(Sim.cardBg)
						Image("IcSimClock")
							.resizable()
							.frame(width: 18, height: 18)
					}
					.frame(width: 40, height: 40)
				}
				.padding(.horizontal, 20)
				.padding(.vertical, 8)

				ScrollView(showsIndicators: false) {
					VStack(spacing: 18) {
						ScoreHero(onOpenLeaderboard: onOpenLeaderboard)
						sectionHeader("Saved staks")
						SavedStakRow(badge: "P", ticker: "PLTR", sub: "Saved Jun 30 · not in portfolio yet", onBuy: { showBuy = true })
						SavedStakRow(badge: "C", ticker: "COST", sub: "Saved Jul 2 · not in portfolio yet", onBuy: { showBuy = true })
						CenterLink(text: "All saved staks", action: {})
						InsightCard()
						HStack(spacing: 10) {
							PickDuo(kicker: "BEST PICK", pct: "+24.0%", pctColor: Sim.green, badge: "N", ticker: "NVDA", sub: "+$24 on $100", action: onOpenPick)
							PickDuo(kicker: "WORST PICK", pct: "-3.0%", pctColor: Sim.red, badge: "M", ticker: "MSFT", sub: "-$3 on $100", action: onOpenPick)
						}
						HowItWorksCard()
						sectionHeader("Your portfolio")
						PortfolioRow(badge: "N", ticker: "NVDA", sub: "Picked May 8 · up 24% since", amount: "+$24.00", pct: "+24.0%", up: true, action: onOpenPick)
						PortfolioRow(badge: "T", ticker: "TSLA", sub: "Picked Jun 3 · up 18% since", amount: "+$18.00", pct: "+18.0%", up: true, action: onOpenPick)
						PortfolioRow(badge: "M", ticker: "MSFT", sub: "Picked Jun 26 · down 3% since", amount: "-$3.00", pct: "-3.0%", up: false, action: onOpenPick)
						CenterLink(text: "See all 12 picks", action: onOpenPortfolio)
						HStack {
							Text("Portfolio breakdown")
								.font(StakFont.sora(16, .semiBold))
								.foregroundStyle(Sim.headerGray)
							Spacer()
							Button(action: onOpenPortfolio) {
								HStack(spacing: 5) {
									Text("Portfolio")
										.font(StakFont.geist(14))
										.foregroundStyle(Color.white)
									Text("›")
										.font(StakFont.geist(14))
										.foregroundStyle(Sim.muted)
								}
							}
							.buttonStyle(.plain)
						}
						SimAllocationCard()
						BoardCard(onOpenLeaderboard: onOpenLeaderboard)
					}
					.padding(.horizontal, 20)
					.padding(.top, 18)
					.padding(.bottom, 26)
				}
			}
			if showBuy {
				DiscoverBuyFlow(spec: pltrBuy, onClose: { showBuy = false })
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}

	private func sectionHeader(_ title: String) -> some View {
		Text(title)
			.font(StakFont.sora(16, .semiBold))
			.foregroundStyle(Sim.headerGray)
			.frame(maxWidth: .infinity, alignment: .leading)
	}
}

/// Portfolio value hero — $10,240.00, cash, weekly change, chart + pills.
private struct ScoreHero: View {
	let onOpenLeaderboard: () -> Void

	var body: some View {
		VStack(alignment: .leading, spacing: 11) {
			Group {
				Text("PORTFOLIO VALUE")
					.font(StakFont.geist(10, .medium))
					.tracking(0.9)
					.foregroundStyle(Sim.faint)
				HStack(alignment: .bottom, spacing: 0) {
					Text("$10,240")
						.font(StakFont.sora(44, .semiBold))
						.tracking(-0.44)
						.foregroundStyle(Color.white)
					Text(".00")
						.font(StakFont.sora(18, .semiBold))
						.foregroundStyle(Sim.muted)
						.padding(.bottom, 6)
				}
				Text("+$240.00 all time on $10,000 paper · 12 picks")
					.font(StakFont.geist(12, .light))
					.foregroundStyle(Sim.muted)
				HStack(spacing: 6) {
					Text("Cash available")
						.font(StakFont.geist(12))
						.foregroundStyle(Sim.muted)
					Text("$8,800.00")
						.font(StakFont.geist(12, .medium))
						.foregroundStyle(Sim.bright)
				}
				Text("▲ +$186 (+1.9%) this week")
					.font(StakFont.geist(12, .medium))
					.foregroundStyle(Sim.green)
				Button(action: onOpenLeaderboard) {
					Text("#47 this week")
						.font(StakFont.geist(12, .medium))
						.foregroundStyle(Sim.teal)
						.padding(.horizontal, 11)
						.padding(.vertical, 6)
						.background(Sim.tealTint, in: RoundedRectangle(cornerRadius: 13))
				}
				.buttonStyle(.plain)
			}
			.padding(.horizontal, 20)

			Image("SimChartLine")
				.resizable()
				.scaledToFit()
				.frame(width: 343, height: 73.56)
				.frame(maxWidth: .infinity)
				.padding(.top, 8)
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
			.padding(.top, 40)
		}
		.padding(.vertical, 20)
		.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 18))
	}
}

/// Saved stak row — badge, ticker + saved line, gradient Buy pill.
private struct SavedStakRow: View {
	let badge: String
	let ticker: String
	let sub: String
	let onBuy: () -> Void

	var body: some View {
		HStack(spacing: 12) {
			ZStack {
				Circle().fill(Sim.chipBg)
				Text(badge)
					.font(StakFont.sora(15, .semiBold))
					.foregroundStyle(Sim.badgeInk)
			}
			.frame(width: 38, height: 38)
			VStack(alignment: .leading, spacing: 3) {
				Text(ticker)
					.font(StakFont.sora(12, .medium))
					.foregroundStyle(Color.white)
				Text(sub)
					.font(StakFont.geist(10))
					.foregroundStyle(Sim.muted)
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			BuyPill(text: "Buy", action: onBuy)
		}
		.padding(.horizontal, 14)
		.padding(.vertical, 11)
		.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 12))
	}
}

/// 60x30 gradient Buy/Sell pill with the CTA hairline.
struct BuyPill: View {
	let text: String
	let action: () -> Void

	var body: some View {
		Button(action: action) {
			Text(text)
				.font(StakFont.sora(12))
				.foregroundStyle(Color.white)
				.frame(width: 60, height: 30)
				.background(discCtaGradient, in: RoundedRectangle(cornerRadius: 6))
				.overlay(RoundedRectangle(cornerRadius: 6).strokeBorder(Sim.ctaBorder, lineWidth: 0.36))
		}
		.buttonStyle(.plain)
	}
}

struct CenterLink: View {
	let text: String
	let action: () -> Void

	var body: some View {
		Button(action: action) {
			HStack(spacing: 6) {
				Text(text)
					.font(StakFont.geist(13))
					.foregroundStyle(Color.white)
				Text("›")
					.font(StakFont.geist(14))
					.foregroundStyle(Sim.muted)
			}
			.frame(maxWidth: .infinity)
		}
		.buttonStyle(.plain)
	}
}

private struct InsightCard: View {
	var body: some View {
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
			Text("Three chip stocks drove 70% of your gains this month. Your taste has a type.")
				.font(StakFont.geist(12))
				.lineSpacing(20 - 12)
				.foregroundStyle(Sim.body)
		}
		.padding(.horizontal, 16)
		.padding(.vertical, 15)
		.frame(maxWidth: .infinity, alignment: .leading)
		.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 16))
	}
}

private struct PickDuo: View {
	let kicker: String
	let pct: String
	let pctColor: Color
	let badge: String
	let ticker: String
	let sub: String
	let action: () -> Void

	var body: some View {
		Button(action: action) {
			VStack(alignment: .leading, spacing: 7) {
				HStack {
					Text(kicker)
						.font(StakFont.geist(10, .medium))
						.tracking(0.9)
						.foregroundStyle(Sim.faint)
					Spacer()
					Text(pct)
						.font(StakFont.geist(12))
						.foregroundStyle(pctColor)
				}
				HStack(spacing: 9) {
					ZStack {
						Circle().fill(Sim.chipBg)
						Text(badge)
							.font(StakFont.sora(14, .semiBold))
							.foregroundStyle(Sim.badgeInk)
					}
					.frame(width: 34, height: 34)
					VStack(alignment: .leading, spacing: 2) {
						Text(ticker)
							.font(StakFont.sora(12, .medium))
							.foregroundStyle(Color.white)
						Text(sub)
							.font(StakFont.geist(11))
							.foregroundStyle(Sim.faint)
					}
				}
			}
			.padding(14)
			.frame(maxWidth: .infinity, alignment: .leading)
			.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 16))
		}
		.buttonStyle(.plain)
	}
}

private struct HowItWorksCard: View {
	private let rules = [
		("1", "Buy a stock with paper dollars. It starts that day."),
		("2", "Your shares move with the real price, up or down."),
		("3", "Sell anytime and the cash returns to your balance.")
	]

	var body: some View {
		VStack(alignment: .leading, spacing: 10) {
			Text("HOW PAPER TRADING WORKS")
				.font(StakFont.geist(10, .medium))
				.tracking(0.9)
				.foregroundStyle(Sim.faint)
			ForEach(rules, id: \.0) { n, rule in
				HStack(spacing: 10) {
					ZStack {
						RoundedRectangle(cornerRadius: 10).fill(Sim.tealTint)
						Text(n)
							.font(StakFont.sora(11, .semiBold))
							.foregroundStyle(Sim.teal)
					}
					.frame(width: 20, height: 20)
					Text(rule)
						.font(StakFont.geist(12))
						.lineSpacing(18 - 12)
						.foregroundStyle(Sim.body)
				}
			}
		}
		.padding(.horizontal, 16)
		.padding(.vertical, 15)
		.frame(maxWidth: .infinity, alignment: .leading)
		.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 16))
	}
}

/// One portfolio pick row (badge 40, ticker + picked line, P&L right).
struct PortfolioRow: View {
	let badge: String
	let ticker: String
	let sub: String
	let amount: String
	let pct: String
	let up: Bool
	var action: () -> Void = {}
	var trailing: (() -> BuyPill)? = nil

	var body: some View {
		Button(action: action) {
			HStack(spacing: 12) {
				ZStack {
					Circle().fill(Sim.chipBg)
					Text(badge)
						.font(StakFont.sora(16, .semiBold))
						.foregroundStyle(Sim.badgeInk)
				}
				.frame(width: 40, height: 40)
				VStack(alignment: .leading, spacing: 3) {
					Text(ticker)
						.font(StakFont.sora(12, .medium))
						.foregroundStyle(Color.white)
					Text(sub)
						.font(StakFont.geist(10))
						.foregroundStyle(Sim.muted)
				}
				.frame(maxWidth: .infinity, alignment: .leading)
				VStack(alignment: .trailing, spacing: 2) {
					Text(amount)
						.font(StakFont.geist(12, .medium))
						.foregroundStyle(up ? Sim.green : Sim.red)
					Text(pct)
						.font(StakFont.geist(10))
						.foregroundStyle(Sim.faint)
				}
				if let trailing {
					trailing()
				}
			}
			.padding(.horizontal, 14)
			.padding(.vertical, 12)
			.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 12))
		}
		.buttonStyle(.plain)
	}
}

/// Allocation — Simulate's donut + the 42/25/17/8/8 sector bars.
private struct SimAllocationCard: View {
	var body: some View {
		VStack(spacing: 16) {
			Text("Allocation")
				.font(StakFont.sora(15, .semiBold))
				.foregroundStyle(Color.white)
			Image("SimDonut")
				.resizable()
				.frame(width: 150, height: 150)
			VStack(spacing: 12) {
				SimSector(name: "Tech & AI", share: "42% · 5 stocks", color: Sim.teal, fill: 132)
				SimSector(name: "Finance", share: "25% · 3 stocks", color: Color(argb: 0xFF7AB3F0), fill: 66)
				SimSector(name: "Green Energy", share: "17% · 2 stocks", color: Sim.green, fill: 63)
				SimSector(name: "Real Estate", share: "8% · 1 stock", color: Color(argb: 0xFF9E8CE5), fill: 38)
				SimSector(name: "Other", share: "8% · 1 stock", color: Sim.faint, fill: 16)
			}
		}
		.padding(18)
		.frame(maxWidth: .infinity)
		.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 16))
	}
}

private struct SimSector: View {
	let name: String
	let share: String
	let color: Color
	let fill: CGFloat

	var body: some View {
		VStack(spacing: 6) {
			HStack {
				Circle().fill(color).frame(width: 9, height: 9)
				Spacer().frame(width: 8)
				Text(name)
					.font(StakFont.geist(13))
					.foregroundStyle(Color.white)
				Spacer()
				Text(share)
					.font(StakFont.geist(12))
					.foregroundStyle(Sim.muted)
			}
			ZStack(alignment: .leading) {
				RoundedRectangle(cornerRadius: 4).fill(Sim.track).frame(height: 7)
				RoundedRectangle(cornerRadius: 4).fill(color).frame(width: fill, height: 7)
			}
		}
	}
}

/// THIS WEEK'S BOARD mini-leaderboard.
private struct BoardCard: View {
	let onOpenLeaderboard: () -> Void

	var body: some View {
		VStack(alignment: .leading, spacing: 10) {
			HStack {
				Text("THIS WEEK’S BOARD")
					.font(StakFont.geist(10, .medium))
					.tracking(0.9)
					.foregroundStyle(Sim.faint)
				Spacer()
				Text("Trailing 7 days")
					.font(StakFont.geist(10))
					.foregroundStyle(Sim.faint)
			}
			boardRow(rank: "1", name: "Maya A.", pct: "+9.4%", you: false)
			boardRow(rank: "2", name: "Jide O.", pct: "+8.8%", you: false)
			boardRow(rank: "47", name: "You", pct: "+4.2%", you: true)
			CenterLink(text: "Full leaderboard", action: onOpenLeaderboard)
		}
		.padding(16)
		.frame(maxWidth: .infinity, alignment: .leading)
		.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 16))
	}

	private func boardRow(rank: String, name: String, pct: String, you: Bool) -> some View {
		HStack(spacing: 10) {
			Text(rank)
				.font(StakFont.sora(12, .semiBold))
				.foregroundStyle(you ? Sim.teal : Sim.faint)
			Text(name)
				.font(StakFont.geist(13, you ? .semiBold : .medium))
				.foregroundStyle(Color.white)
				.frame(maxWidth: .infinity, alignment: .leading)
			Text(pct)
				.font(StakFont.sora(you ? 13 : 12, .semiBold))
				.foregroundStyle(you ? Sim.teal : Sim.headerGray)
		}
		.padding(.horizontal, 10)
		.padding(.vertical, 7)
		.background(you ? Sim.tealTint : Color.clear, in: RoundedRectangle(cornerRadius: 10))
	}
}
