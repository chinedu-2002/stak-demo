import SwiftUI

/// 07 · Simulate — "Simulate home · paper" (CHINEDU 1:3898) with the
/// Buy-PLTR ticket (1:4232) and Order filled (85:895). Ported from
/// android/ ui/simulate/SimulateScreen.kt. Every metric is scaled by
/// the 390pt artboard unit (`figmaUnit`), exactly like the Android
/// build's `u` scaling.
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
	static let ctaBorder = StakColors.ctaBorderGradient
}

let pltrBuy = BuySpec(
	title: "Buy PLTR?", badge: "P", name: "Palantir Technologies", priceLine: "$28.40 today",
	change: "▲ 1.1%", cashBefore: "$8,800.00", cashAfter: "$8,775.00", shares: "0.8803", symbol: "PLTR"
)

/// Codex parity audit (2026-09-04): COST's Buy pill serves its own $25
/// paper ticket into the authored 1:4232 template, the way the Discover
/// deck serves the tapped stock. Mirrors android/ ui/simulate/SimulateScreen.kt.
let costBuy = BuySpec(
	title: "Buy COST?", badge: "C", name: "Costco Wholesale", priceLine: "$947.20 today",
	change: "▲ 0.7%", cashBefore: "$8,800.00", cashAfter: "$8,775.00", shares: "0.0264", symbol: "COST"
)

struct SimulateView: View {
	let onOpenPortfolio: () -> Void
	/// Codex parity audit (2026-09-04): every pick tile / row passes its
	/// own ticker, so Pick detail serves the tapped pick.
	let onOpenPick: (String) -> Void
	let onOpenLeaderboard: () -> Void
	/// Authored (1:3964): "All saved staks ›" -> My STAK Overview (tab SWAP, Instant).
	var onOpenMyStak: () -> Void = {}
	/// When the shell hosts the ticket (1:4232: the sheet covers the tab bar),
	/// it raises it here with the tapped row's spec.
	var onPracticeBuy: ((BuySpec) -> Void)? = nil

	/// The locally hosted ticket's spec (nil = no ticket).
	@State private var buy: BuySpec? = nil

	var body: some View {
		let u = figmaUnit
		ZStack {
			VStack(spacing: 0) {
				HStack {
					VStack(alignment: .leading, spacing: 3 * u) {
						Text("Simulate")
							.font(StakFont.sora(26 * u, .semiBold))
							.foregroundStyle(Color.white)
						Text("Pick from your saves. Paper money does the talking.")
							.font(StakFont.geist(12 * u))
							.foregroundStyle(Sim.muted)
					}
					Spacer()
					ZStack {
						Circle().fill(Sim.cardBg)
						Image("IcSimClock")
							.resizable()
							.frame(width: 18 * u, height: 18 * u)
					}
					.frame(width: 40 * u, height: 40 * u)
				}
				.padding(.horizontal, 20 * u)
				.padding(.top, 8 * u)
				.padding(.bottom, 8 * u)

				ScrollView(showsIndicators: false) {
					VStack(spacing: 18 * u) {
						ScoreHero(onOpenLeaderboard: onOpenLeaderboard)
						sectionHeader("Saved staks")
						SavedStakRow(badge: "P", ticker: "PLTR", sub: "Saved Jun 30 · not in portfolio yet", spec: pltrBuy, onBuy: { practiceBuy($0) })
						SavedStakRow(badge: "C", ticker: "COST", sub: "Saved Jul 2 · not in portfolio yet", spec: costBuy, onBuy: { practiceBuy($0) })
						CenterLink(text: "All saved staks", action: onOpenMyStak)
						InsightCard()
						HStack(spacing: 10 * u) {
							PickDuo(kicker: "BEST PICK", pct: "+24.0%", pctColor: Sim.green, badge: "N", ticker: "NVDA", sub: "+$24 on $100", action: { onOpenPick("NVDA") })
							PickDuo(kicker: "WORST PICK", pct: "-3.0%", pctColor: Sim.red, badge: "M", ticker: "MSFT", sub: "-$3 on $100", action: { onOpenPick("MSFT") })
						}
						HowItWorksCard()
						sectionHeader("Your portfolio")
						PortfolioRow(badge: "N", ticker: "NVDA", sub: "Picked May 8 · up 24% since", amount: "+$24.00", pct: "+24.0%", up: true, action: { onOpenPick("NVDA") })
						PortfolioRow(badge: "T", ticker: "TSLA", sub: "Picked Jun 3 · up 18% since", amount: "+$18.00", pct: "+18.0%", up: true, action: { onOpenPick("TSLA") })
						PortfolioRow(badge: "M", ticker: "MSFT", sub: "Picked Jun 26 · down 3% since", amount: "-$3.00", pct: "-3.0%", up: false, action: { onOpenPick("MSFT") })
						CenterLink(text: "See all 12 picks", action: onOpenPortfolio)
						HStack {
							Text("Portfolio breakdown")
								.font(StakFont.sora(16 * u, .semiBold))
								.foregroundStyle(Sim.headerGray)
							Spacer()
							Button(action: onOpenPortfolio) {
								HStack(spacing: 5 * u) {
									Text("Portfolio")
										.font(StakFont.geist(14 * u))
										.foregroundStyle(Color.white)
									Text("›")
										.font(StakFont.geist(14 * u))
										.foregroundStyle(Sim.muted)
								}
							}
							.buttonStyle(.plain)
						}
						SimAllocationCard()
						BoardCard(onOpenLeaderboard: onOpenLeaderboard)
					}
					.padding(.horizontal, 20 * u)
					.padding(.top, 18 * u)
					.padding(.bottom, 26 * u)
				}
			}
			if let spec = buy {
				// 85:895 authors "View portfolio" / "Done" on the Simulate add-success sheet.
				DiscoverBuyFlow(spec: spec, onClose: { buy = nil }, filledPrimary: "View portfolio", filledSecondary: "Done", ticketSecondary: "Back")
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}

	/// The tapped row's ticket: raised to the shell when it hosts the
	/// sheet, else shown here.
	private func practiceBuy(_ spec: BuySpec) {
		if let onPracticeBuy { onPracticeBuy(spec) } else { buy = spec }
	}

	private func sectionHeader(_ title: String) -> some View {
		Text(title)
			.font(StakFont.sora(16 * figmaUnit, .semiBold))
			.foregroundStyle(Sim.headerGray)
			.frame(maxWidth: .infinity, alignment: .leading)
	}
}

/// Portfolio value hero — $10,240.00, cash, weekly change, chart + pills.
private struct ScoreHero: View {
	let onOpenLeaderboard: () -> Void

	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 11 * u) {
			Group {
				Text("PORTFOLIO VALUE")
					.font(StakFont.geist(10 * u, .medium))
					.tracking(0.9 * u)
					.foregroundStyle(Sim.faint)
				HStack(alignment: .bottom, spacing: 0) {
					Text("$10,240")
						.font(StakFont.sora(44 * u, .semiBold))
						.tracking(-0.44 * u)
						// Authored box (1:3924) is 55 tall — pin it so the stack sums.
						.frame(height: 55 * u)
						.foregroundStyle(Color.white)
					Text(".00")
						.font(StakFont.sora(18 * u, .semiBold))
						.foregroundStyle(Sim.muted)
						// Authored (1:3923): ".00" starts 8 after the figure and its
						// box bottom sits 8 above the figure's (55 vs y24+h23).
						.padding(.leading, 8 * u)
						.padding(.bottom, 8 * u)
				}
				Text("+$240.00 all time on $10,000 paper · 12 picks")
					.font(StakFont.geist(12 * u, .light))
					.foregroundStyle(Sim.muted)
				HStack(spacing: 6 * u) {
					Text("Cash available")
						.font(StakFont.geist(12 * u))
						.foregroundStyle(Sim.muted)
					Text("$8,800.00")
						.font(StakFont.geist(12 * u, .medium))
						.foregroundStyle(Sim.bright)
				}
				Text("▲ +$186 (+1.9%) this week")
					.font(StakFont.geist(12 * u, .medium))
					.foregroundStyle(Sim.green)
				// The "#47 this week" chip sits on its own row of the column.
				Button(action: onOpenLeaderboard) {
					Text("#47 this week")
						.font(StakFont.geist(12 * u, .medium))
						.foregroundStyle(Sim.teal)
						.padding(.horizontal, 11 * u)
						.padding(.vertical, 6 * u)
						.background(Sim.tealTint, in: RoundedRectangle(cornerRadius: 13 * u))
				}
				.buttonStyle(.plain)
			}
			.padding(.horizontal, 20 * u)

			// Authored: ranks→chart gap is exactly the column's 11 (1:3935);
			// the chart bleeds outside the 20u text padding.
			Image("SimChartLine")
				.resizable()
				.scaledToFit()
				.frame(width: 343 * u, height: 73.56 * u)
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
			// Authored chart→pills gap 40; the column gap contributes 11.
			.padding(.top, 29 * u)
		}
		.padding(.vertical, 20 * u)
		.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 18 * u))
	}
}

/// Saved stak row — badge, ticker + saved line, teal Buy pill (60x30).
/// The row's Buy hands over ITS ticket spec.
private struct SavedStakRow: View {
	let badge: String
	let ticker: String
	let sub: String
	let spec: BuySpec
	let onBuy: (BuySpec) -> Void

	var body: some View {
		let u = figmaUnit
		HStack(spacing: 12 * u) {
			ZStack {
				Circle().fill(Sim.chipBg)
				Text(badge)
					.font(StakFont.sora(15 * u, .semiBold))
					.foregroundStyle(Sim.badgeInk)
			}
			.frame(width: 38 * u, height: 38 * u)
			VStack(alignment: .leading, spacing: 3 * u) {
				Text(ticker)
					.font(StakFont.sora(12 * u, .medium))
					.foregroundStyle(Color.white)
				Text(sub)
					.font(StakFont.geist(10 * u))
					.foregroundStyle(Sim.muted)
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			BuyPill(text: "Buy", action: { onBuy(spec) })
		}
		.padding(.horizontal, 14 * u)
		.padding(.vertical, 11 * u)
		.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 12 * u))
	}
}

/// 60x30 gradient Buy pill with the CTA hairline.
struct BuyPill: View {
	let text: String
	let action: () -> Void

	var body: some View {
		let u = figmaUnit
		Button(action: action) {
			Text(text)
				.font(StakFont.sora(12 * u))
				.foregroundStyle(Color.white)
				.frame(width: 60 * u, height: 30 * u)
				.background(discCtaGradient, in: RoundedRectangle(cornerRadius: 6 * u))
				.overlay(RoundedRectangle(cornerRadius: 6 * u).strokeBorder(Sim.ctaBorder, lineWidth: 0.36 * u))
		}
		.buttonStyle(.plain)
	}
}

struct CenterLink: View {
	let text: String
	let action: () -> Void

	var body: some View {
		let u = figmaUnit
		Button(action: action) {
			HStack(spacing: 6 * u) {
				Text(text)
					.font(StakFont.geist(13 * u))
					.foregroundStyle(Color.white)
				Text("›")
					.font(StakFont.geist(14 * u))
					.foregroundStyle(Sim.muted)
			}
			.frame(maxWidth: .infinity)
		}
		.buttonStyle(.plain)
	}
}

private struct InsightCard: View {
	var body: some View {
		let u = figmaUnit
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
			Text("Three chip stocks drove 70% of your gains this month. Your taste has a type.")
				.font(StakFont.geist(12 * u))
				.lineSpacing((20 - 12) * u)
				.foregroundStyle(Sim.body)
		}
		.padding(.horizontal, 16 * u)
		.padding(.vertical, 15 * u)
		.frame(maxWidth: .infinity, alignment: .leading)
		.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 16 * u))
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
		let u = figmaUnit
		Button(action: action) {
			VStack(alignment: .leading, spacing: 7 * u) {
				HStack {
					Text(kicker)
						.font(StakFont.geist(10 * u, .medium))
						.tracking(0.9 * u)
						.foregroundStyle(Sim.faint)
					Spacer()
					Text(pct)
						.font(StakFont.geist(12 * u))
						.foregroundStyle(pctColor)
				}
				HStack(spacing: 9 * u) {
					ZStack {
						Circle().fill(Sim.chipBg)
						Text(badge)
							.font(StakFont.sora(14 * u, .semiBold))
							.foregroundStyle(Sim.badgeInk)
					}
					.frame(width: 34 * u, height: 34 * u)
					VStack(alignment: .leading, spacing: 2 * u) {
						Text(ticker)
							.font(StakFont.sora(12 * u, .medium))
							.foregroundStyle(Color.white)
						Text(sub)
							.font(StakFont.geist(11 * u))
							.foregroundStyle(Sim.faint)
					}
				}
			}
			.padding(14 * u)
			.frame(maxWidth: .infinity, alignment: .leading)
			.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 16 * u))
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
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 10 * u) {
			Text("HOW PAPER TRADING WORKS")
				.font(StakFont.geist(10 * u, .medium))
				.tracking(0.9 * u)
				.foregroundStyle(Sim.faint)
			ForEach(rules, id: \.0) { n, rule in
				HStack(spacing: 10 * u) {
					ZStack {
						RoundedRectangle(cornerRadius: 10 * u).fill(Sim.tealTint)
						Text(n)
							.font(StakFont.sora(11 * u, .semiBold))
							.foregroundStyle(Sim.teal)
					}
					.frame(width: 20 * u, height: 20 * u)
					Text(rule)
						.font(StakFont.geist(12 * u))
						.lineSpacing((18 - 12) * u)
						.foregroundStyle(Sim.body)
				}
			}
		}
		.padding(.horizontal, 16 * u)
		.padding(.vertical, 15 * u)
		.frame(maxWidth: .infinity, alignment: .leading)
		.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 16 * u))
	}
}

/// One portfolio pick row (badge 40, ticker + picked line, P&L right).
struct PortfolioRow<Trailing: View>: View {
	let badge: String
	let ticker: String
	let sub: String
	let amount: String
	let pct: String
	let up: Bool
	var action: () -> Void = {}
	var trailing: Trailing

	init(
		badge: String, ticker: String, sub: String,
		amount: String, pct: String, up: Bool,
		action: @escaping () -> Void = {},
		@ViewBuilder trailing: () -> Trailing
	) {
		self.badge = badge
		self.ticker = ticker
		self.sub = sub
		self.amount = amount
		self.pct = pct
		self.up = up
		self.action = action
		self.trailing = trailing()
	}

	var body: some View {
		let u = figmaUnit
		Button(action: action) {
			HStack(spacing: 12 * u) {
				ZStack {
					Circle().fill(Sim.chipBg)
					Text(badge)
						.font(StakFont.sora(16 * u, .semiBold))
						.foregroundStyle(Sim.badgeInk)
				}
				.frame(width: 40 * u, height: 40 * u)
				VStack(alignment: .leading, spacing: 3 * u) {
					Text(ticker)
						.font(StakFont.sora(12 * u, .medium))
						.foregroundStyle(Color.white)
					Text(sub)
						.font(StakFont.geist(10 * u))
						.foregroundStyle(Sim.muted)
				}
				.frame(maxWidth: .infinity, alignment: .leading)
				VStack(alignment: .trailing, spacing: 2 * u) {
					Text(amount)
						.font(StakFont.geist(12 * u, .medium))
						.foregroundStyle(up ? Sim.green : Sim.red)
					Text(pct)
						.font(StakFont.geist(10 * u))
						.foregroundStyle(Sim.faint)
				}
				trailing
			}
			.padding(.horizontal, 14 * u)
			.padding(.vertical, 12 * u)
			.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 12 * u))
		}
		.buttonStyle(.plain)
	}
}

extension PortfolioRow where Trailing == EmptyView {
	init(
		badge: String, ticker: String, sub: String,
		amount: String, pct: String, up: Bool,
		action: @escaping () -> Void = {}
	) {
		self.init(
			badge: badge, ticker: ticker, sub: sub,
			amount: amount, pct: pct, up: up,
			action: action, trailing: { EmptyView() }
		)
	}
}

/// Allocation — Simulate's donut + the 42/25/17/8/8 sector bars.
private struct SimAllocationCard: View {
	var body: some View {
		let u = figmaUnit
		VStack(spacing: 16 * u) {
			Text("Allocation")
				.font(StakFont.sora(15 * u, .semiBold))
				.foregroundStyle(Color.white)
			Image("SimDonut")
				.resizable()
				.frame(width: 150 * u, height: 150 * u)
			VStack(spacing: 12 * u) {
				SimSector(name: "Tech & AI", share: "42% · 5 stocks", color: Sim.teal, fill: 132)
				SimSector(name: "Finance", share: "25% · 3 stocks", color: Color(argb: 0xFF7AB3F0), fill: 66)
				SimSector(name: "Green Energy", share: "17% · 2 stocks", color: Sim.green, fill: 63)
				SimSector(name: "Real Estate", share: "8% · 1 stock", color: Color(argb: 0xFF9E8CE5), fill: 38)
				SimSector(name: "Other", share: "8% · 1 stock", color: Sim.faint, fill: 16)
			}
		}
		.padding(18 * u)
		.frame(maxWidth: .infinity)
		.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 16 * u))
	}
}

private struct SimSector: View {
	let name: String
	let share: String
	let color: Color
	let fill: CGFloat

	var body: some View {
		let u = figmaUnit
		VStack(spacing: 6 * u) {
			HStack {
				Circle().fill(color).frame(width: 9 * u, height: 9 * u)
				Spacer().frame(width: 8 * u)
				Text(name)
					.font(StakFont.geist(13 * u))
					.foregroundStyle(Color.white)
				Spacer()
				Text(share)
					.font(StakFont.geist(12 * u))
					.foregroundStyle(Sim.muted)
			}
			ZStack(alignment: .leading) {
				RoundedRectangle(cornerRadius: 4 * u).fill(Sim.track).frame(height: 7 * u)
				RoundedRectangle(cornerRadius: 4 * u).fill(color).frame(width: fill * u, height: 7 * u)
			}
		}
	}
}

/// THIS WEEK'S BOARD mini-leaderboard.
private struct BoardCard: View {
	let onOpenLeaderboard: () -> Void

	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 10 * u) {
			HStack {
				Text("THIS WEEK’S BOARD")
					.font(StakFont.geist(10 * u, .medium))
					.tracking(0.9 * u)
					.foregroundStyle(Sim.faint)
				Spacer()
				Text("Trailing 7 days")
					.font(StakFont.geist(10 * u))
					.foregroundStyle(Sim.faint)
			}
			boardRow(rank: "1", name: "Maya A.", pct: "+9.4%", you: false)
			boardRow(rank: "2", name: "Jide O.", pct: "+8.8%", you: false)
			boardRow(rank: "47", name: "You", pct: "+4.2%", you: true)
			CenterLink(text: "Full leaderboard", action: onOpenLeaderboard)
		}
		.padding(16 * u)
		.frame(maxWidth: .infinity, alignment: .leading)
		.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 16 * u))
	}

	private func boardRow(rank: String, name: String, pct: String, you: Bool) -> some View {
		let u = figmaUnit
		return HStack(spacing: 10 * u) {
			Text(rank)
				.font(StakFont.sora(12 * u, .semiBold))
				.foregroundStyle(you ? Sim.teal : Sim.faint)
			Text(name)
				.font(StakFont.geist(13 * u, you ? .semiBold : .medium))
				.foregroundStyle(Color.white)
				.frame(maxWidth: .infinity, alignment: .leading)
			Text(pct)
				.font(StakFont.sora((you ? 13 : 12) * u, .semiBold))
				.foregroundStyle(you ? Sim.teal : Sim.headerGray)
		}
		.padding(.horizontal, 10 * u)
		.padding(.vertical, 7 * u)
		.background(you ? Sim.tealTint : Color.clear, in: RoundedRectangle(cornerRadius: 10 * u))
	}
}
