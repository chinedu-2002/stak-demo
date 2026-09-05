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
	/// Codex audit (2026-09-04): the paper ledger - rows, pick count and
	/// the saved rows' "In portfolio" line follow it.
	@ObservedObject private var portfolio = PaperPortfolio.shared

	var body: some View {
		let u = figmaUnit
		ZStack {
			VStack(spacing: 0) {
				HStack {
					VStack(alignment: .leading, spacing: 3 * u) {
						Text("Simulate")
							.font(StakFont.sora(26 * u, .semiBold))
							.foregroundStyle(Color.white)
							.frame(height: 33 * u) // 1:3916 line box (exact-design audit 2026-09-04)
						Text("Pick from your saves. Paper money does the talking.")
							.font(StakFont.geist(12 * u))
							.foregroundStyle(Sim.muted)
							.frame(height: 16 * u) // 1:3917 line box
					}
					Spacer()
					// Codex audit (2026-09-04): the clock (1:3918 "btn") opens the
					// pick history - SOLD · REALIZED lives on the portfolio page.
					Button(action: onOpenPortfolio) {
						ZStack {
							Circle().fill(Sim.cardBg)
							Image("IcSimClock")
								.resizable()
								.frame(width: 18 * u, height: 18 * u)
						}
						.frame(width: 40 * u, height: 40 * u)
					}
					.buttonStyle(.plain)
					.accessibilityLabel("History")
				}
				.padding(.horizontal, 20 * u)
				// 1:3914 (exact-design audit 2026-09-04): the 52-tall header sits 8 below the
				// status bar with no bottom inset - the 18 above the hero is the Main column's own.
				.padding(.top, 8 * u)

				ScrollView(showsIndicators: false) {
					VStack(spacing: 18 * u) {
						ScoreHero(onOpenLeaderboard: onOpenLeaderboard)
						sectionHeader("Saved staks")
						// 1:3947 slist (exact-design audit 2026-09-04): the saved rows sit 10 apart, not the column's 18.
						VStack(spacing: 10 * u) {
							SavedStakRow(badge: "P", ticker: "PLTR", sub: savedSub("PLTR", authored: "Saved Jun 30 · not in portfolio yet"), spec: pltrBuy, onBuy: { practiceBuy($0) })
							SavedStakRow(badge: "C", ticker: "COST", sub: savedSub("COST", authored: "Saved Jul 2 · not in portfolio yet"), spec: costBuy, onBuy: { practiceBuy($0) })
						}
						CenterLink(text: "All saved staks", action: onOpenMyStak)
						InsightCard()
						// Review (2026-09-04): the tiles follow the ledger - largest and
						// smallest dollar gain (seeded: NVDA +24.0% / "+$24 on $100",
						// MSFT -3.0% / "-$3 on $100", as authored). Hidden under two picks.
						if portfolio.pickCount >= 2, let best = portfolio.best, let worst = portfolio.worst {
							HStack(spacing: 10 * u) {
								PickDuo(kicker: "BEST PICK", pct: best.row.pct, pctColor: best.row.up ? Sim.green : Sim.red, badge: best.row.badge, ticker: best.row.ticker, sub: "\(PaperPortfolio.gainLabel(PaperPortfolio.amount(best.row.amount))) on \(best.spec.stakeBasis)", action: { onOpenPick(best.row.ticker) })
								PickDuo(kicker: "WORST PICK", pct: worst.row.pct, pctColor: worst.row.up ? Sim.green : Sim.red, badge: worst.row.badge, ticker: worst.row.ticker, sub: "\(PaperPortfolio.gainLabel(PaperPortfolio.amount(worst.row.amount))) on \(worst.spec.stakeBasis)", action: { onOpenPick(worst.row.ticker) })
							}
						}
						HowItWorksCard()
						sectionHeader("Your portfolio")
						// Codex audit (2026-09-04): the ledger's first three rows - a
						// fresh buy lands at the top (1:3898 authored NVDA/TSLA/MSFT
						// from a 12-pick sample; the seeded six lead NVDA/TSLA/AMD).
						// 1:4009 plist (exact-design audit 2026-09-04): the three rows sit 10 apart, not the column's 18.
						VStack(spacing: 10 * u) {
							ForEach(Array(portfolio.positions.prefix(3))) { position in
								let p = position.row
								PortfolioRow(badge: p.badge, ticker: p.ticker, sub: p.sub, amount: p.amount, pct: p.pct, up: p.up, action: { onOpenPick(p.ticker) })
							}
						}
						// Authored copy (user, 2026-09-04 (CHINEDU 07 · Simulate 423:1007): the authored look wins); the ledger still drives the rows above.
						CenterLink(text: "See all 12 picks", action: onOpenPortfolio)
						// 1:4040 Points breakdown (exact-design audit 2026-09-04): the section header
						// and the Allocation card are 15 apart, not the column's 18.
						VStack(spacing: 15 * u) {
							HStack {
								Text("Portfolio breakdown")
									.font(StakFont.sora(16 * u, .semiBold))
									.foregroundStyle(Sim.headerGray)
								Spacer()
								Button(action: onOpenPortfolio) {
									HStack(spacing: 5 * u) {
										Text("Portfolio")
											.font(StakFont.geist(14 * u))
											// 1:4044 (exact-design audit 2026-09-04): teal at 80% - was white.
											.foregroundStyle(Color(argb: 0xCC69B3CA))
										// 1:4045 (exact-design audit 2026-09-04): the exported 4.909x9 chevron asset, not a "›" glyph.
										Image("IcSimChevron")
											.resizable()
											.frame(width: 4.909 * u, height: 9 * u)
									}
								}
								.buttonStyle(.plain)
							}
							.frame(height: 21 * u) // 1:4041 header row (Sora 16 on a 1.34 line)
							SimAllocationCard()
						}
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

	/// Codex audit (2026-09-04): a saved stak that has been bought reads
	/// "In portfolio · 0.8803 shares" in place of the authored
	/// "not in portfolio yet" line (1:3964 template).
	private func savedSub(_ symbol: String, authored: String) -> String {
		guard let held = portfolio.pickSpec(symbol) else { return authored }
		return "In portfolio · \(held.shares) shares"
	}

	private func sectionHeader(_ title: String) -> some View {
		Text(title)
			.font(StakFont.sora(16 * figmaUnit, .semiBold))
			.foregroundStyle(Sim.headerGray)
			.frame(maxWidth: .infinity, alignment: .leading)
	}
}

/// Portfolio value hero — $10,240.00, cash, weekly change, chart + pills.
/// Codex audit (2026-09-04): reads PaperPortfolio - value, cash and pick
/// count follow the ledger; the week figures are the shared constants.
private struct ScoreHero: View {
	let onOpenLeaderboard: () -> Void

	@ObservedObject private var portfolio = PaperPortfolio.shared
	/// Codex audit (2026-09-04): the live range - 3M as authored (1:3935).
	@State private var range = "3M"

	/// "$10,240.00" split at the point: the 44 figure and the 18 cents.
	private var figure: (whole: String, cents: String) {
		let value = PaperPortfolio.money(portfolio.portfolioValue)
		guard let dot = value.lastIndex(of: ".") else { return (value, "") }
		return (String(value[..<dot]), String(value[dot...]))
	}

	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 11 * u) {
			Group {
				Text("PORTFOLIO VALUE")
					.font(StakFont.geist(10 * u, .medium))
					.tracking(0.9 * u)
					.foregroundStyle(Sim.faint)
				HStack(alignment: .bottom, spacing: 0) {
					Text(figure.whole)
						.font(StakFont.sora(44 * u, .semiBold))
						// 1:3924 (exact-design audit 2026-09-04): no tracking - the -0.44 was never authored.
						// Authored box (1:3924) is 55 tall — pin it so the stack sums.
						.frame(height: 55 * u)
						.foregroundStyle(Color.white)
					Text(figure.cents)
						.font(StakFont.sora(18 * u, .semiBold))
						.foregroundStyle(Sim.muted)
						// Authored (1:3923): ".00" starts 8 after the figure and its
						// box bottom sits 8 above the figure's (55 vs y24+h23). The
						// gap is the design (user, 2026-09-04 (CHINEDU 07 · Simulate 423:1007): the authored look wins).
						.padding(.leading, 8 * u)
						.padding(.bottom, 8 * u)
				}
				Text("+\(PaperPortfolio.money(portfolio.allTimeGain)) all time on $10,000 paper · 12 picks")
					.font(StakFont.geist(12 * u, .light))
					.foregroundStyle(Sim.muted)
				HStack(spacing: 6 * u) {
					Text("Cash available")
						.font(StakFont.geist(12 * u))
						.foregroundStyle(Sim.muted)
					Text(PaperPortfolio.money(portfolio.cash))
						.font(StakFont.geist(12 * u, .medium))
						.foregroundStyle(Sim.bright)
				}
				Text("▲ \(PaperPortfolio.weekGain) (\(PaperPortfolio.weekPct)) this week")
					.font(StakFont.geist(12 * u, .medium))
					.foregroundStyle(Sim.green)
				// The "#47 this week" chip sits on its own row of the column.
				Button(action: onOpenLeaderboard) {
					Text("#\(PaperPortfolio.weekRank) this week")
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
			SimRangeChart(range: range)
				.frame(maxWidth: .infinity)
			HStack(spacing: 37 * u) {
				ForEach(["1D", "1W", "1M", "3M", "YTD", "1Y"], id: \.self) { label in
					Button { range = label } label: {
						if label == range {
							// The authored 39x22.5 teal chrome follows the selection.
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
					.buttonStyle(.plain)
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

/// The hero chart at a range (343x73.56, 1:3935). Codex audit
/// (2026-09-04): "3M" keeps the authored SimChartLine; every other range
/// draws its series in the same box - stroke Sim.teal 2 wide, round
/// caps/joins, a teal-to-clear fill beneath. The series are demo
/// stand-ins until the backend serves price history. Mirrors android
/// ui/simulate/SimulateScreen.kt.
struct SimRangeChart: View {
	let range: String

	/// Height fraction from the bottom (0 = bottom) per point, spread
	/// evenly across the width.
	static let series: [String: [CGFloat]] = [
		"1D": [0.45, 0.50, 0.42, 0.55, 0.60, 0.52, 0.58, 0.66, 0.62, 0.70],
		"1W": [0.30, 0.38, 0.35, 0.50, 0.46, 0.60, 0.72],
		"1M": [0.25, 0.30, 0.28, 0.42, 0.38, 0.52, 0.48, 0.60, 0.55, 0.68, 0.75],
		"YTD": [0.20, 0.35, 0.30, 0.45, 0.40, 0.55, 0.50, 0.62, 0.70, 0.66, 0.80],
		"1Y": [0.15, 0.22, 0.30, 0.26, 0.40, 0.36, 0.50, 0.55, 0.48, 0.62, 0.70, 0.82]
	]

	var body: some View {
		let u = figmaUnit
		Group {
			if let points = SimRangeChart.series[range], range != "3M" {
				GeometryReader { geo in
					let line = SimRangeChart.linePath(points, in: geo.size)
					ZStack {
						SimRangeChart.fillPath(line, in: geo.size)
							.fill(LinearGradient(colors: [Sim.teal.opacity(0.22), Color.clear], startPoint: .top, endPoint: .bottom))
						line.stroke(Sim.teal, style: StrokeStyle(lineWidth: 2 * u, lineCap: .round, lineJoin: .round))
					}
				}
			} else {
				Image("SimChartLine")
					.resizable()
					.scaledToFit()
			}
		}
		.frame(width: 343 * u, height: 73.56 * u)
	}

	/// The series as a polyline across the box.
	private static func linePath(_ points: [CGFloat], in size: CGSize) -> Path {
		var path = Path()
		let steps = CGFloat(max(points.count - 1, 1))
		for (i, fraction) in points.enumerated() {
			let point = CGPoint(x: size.width * CGFloat(i) / steps, y: size.height * (1 - fraction))
			if i == 0 { path.move(to: point) } else { path.addLine(to: point) }
		}
		return path
	}

	/// The polyline closed down to the box's bottom edge for the gradient.
	private static func fillPath(_ line: Path, in size: CGSize) -> Path {
		var path = line
		path.addLine(to: CGPoint(x: size.width, y: size.height))
		path.addLine(to: CGPoint(x: 0, y: size.height))
		path.closeSubpath()
		return path
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
				// 1:3954 (exact-design audit 2026-09-04): the authored drop shadow - #52AAC7 at 4%, dy 12.285, blur 12.285.
				.tealShadow(dy: 12.285, blur: 12.285, alpha: 0.04)
		}
		.buttonStyle(.plain)
	}
}

/// The authored teal link (1:3964 / 1:4037 / 1:4112 - exact-design audit
/// 2026-09-04): Geist Medium 13 + "›" 14, both #69b3ca (were white / muted
/// Regular). Centred across the column unless the host lays it at its own
/// left edge (the board card's "Full leaderboard"). `centered` is declared
/// last with a default - memberwise order.
struct CenterLink: View {
	let text: String
	let action: () -> Void
	var centered: Bool = true

	var body: some View {
		let u = figmaUnit
		Button(action: action) {
			HStack(spacing: 6 * u) {
				Text(text)
					.font(StakFont.geist(13 * u, .medium))
					.foregroundStyle(Sim.teal)
				Text("›")
					.font(StakFont.geist(14 * u, .medium))
					.foregroundStyle(Sim.teal)
			}
			.frame(height: 18 * u)
			.frame(maxWidth: centered ? CGFloat.infinity : nil)
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
				.stakLineHeight(20 * u, size: 12 * u, face: .geist)
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
				// 1:3977 / 1:3982 (exact-design audit 2026-09-04): the badge row carries only the
				// ticker; the "+$24 on $100" line is the card's own third row, 7 below it.
				HStack(spacing: 9 * u) {
					ZStack {
						Circle().fill(Sim.chipBg)
						Text(badge)
							.font(StakFont.sora(14 * u, .semiBold))
							.foregroundStyle(Sim.badgeInk)
					}
					.frame(width: 34 * u, height: 34 * u)
					Text(ticker)
						.font(StakFont.sora(12 * u, .medium))
						.foregroundStyle(Color.white)
				}
				Text(sub)
					.font(StakFont.geist(11 * u))
					.foregroundStyle(Sim.faint)
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
				// 1:3995 (exact-design audit 2026-09-04): pill and line share the row's top edge.
				HStack(alignment: .top, spacing: 10 * u) {
					ZStack {
						RoundedRectangle(cornerRadius: 10 * u).fill(Sim.tealTint)
						Text(n)
							.font(StakFont.sora(11 * u, .semiBold))
							.foregroundStyle(Sim.teal)
					}
					.frame(width: 20 * u, height: 20 * u)
					Text(rule)
						.font(StakFont.geist(12 * u))
						.stakLineHeight(18 * u, size: 12 * u, face: .geist)
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
	/// 1:4539 (exact-design audit 2026-09-04): the Portfolio page's picked line is
	/// Geist Light; Simulate home's (1:4015) is Regular. Declared last with a
	/// default - memberwise order.
	var subLight: Bool = false

	init(
		badge: String, ticker: String, sub: String,
		amount: String, pct: String, up: Bool,
		action: @escaping () -> Void = {},
		@ViewBuilder trailing: () -> Trailing,
		subLight: Bool = false
	) {
		self.badge = badge
		self.ticker = ticker
		self.sub = sub
		self.amount = amount
		self.pct = pct
		self.up = up
		self.action = action
		self.trailing = trailing()
		self.subLight = subLight
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
						.font(StakFont.geist(10 * u, subLight ? .light : .regular))
						.foregroundStyle(Sim.muted)
				}
				.frame(maxWidth: .infinity, alignment: .leading)
				VStack(alignment: .trailing, spacing: 2 * u) {
					Text(amount)
						// 1:4017 (exact-design audit 2026-09-04): the P&L is Geist Regular, not Medium.
						.font(StakFont.geist(12 * u))
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
			// Audit item 6: the You row quotes the hero's week (1:3898 authored +4.2% here, +1.9% above).
			// Authored board figures (1:4111 +4.2%; user, 2026-09-04 (CHINEDU 07 · Simulate 423:1007): the authored look wins).
			boardRow(rank: "47", name: "You", pct: "+4.2%", you: true)
			// 1:4112 (exact-design audit 2026-09-04): the frame lays this link at the card's
			// left edge (x16, hug width), not centred like the column links.
			CenterLink(text: "Full leaderboard", action: onOpenLeaderboard, centered: false)
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
				// 1:4101 (exact-design audit 2026-09-04): the rank sits in a 22-wide box so the names line up.
				.frame(width: 22 * u, alignment: .leading)
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

/// The authored teal drop shadow under the CTAs (#52AAC7) - `dy` / `blur`
/// in artboard units, `alpha` 0..1 - laid behind the r6 box the way the
/// Discover deck CTA does it (exact-design audit 2026-09-04). Chain it
/// after the fill so the wash sits under it.
extension View {
	func tealShadow(dy: CGFloat, blur: CGFloat, alpha: Double, radius: CGFloat = 6) -> some View {
		let u = figmaUnit
		return background {
			RoundedRectangle(cornerRadius: radius * u)
				.fill(Color(argb: 0xFF52AAC7))
				.opacity(alpha)
				.blur(radius: blur * u)
				.offset(y: dy * u)
		}
	}
}
