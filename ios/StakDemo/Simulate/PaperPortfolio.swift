import SwiftUI

/// Codex audit (2026-09-04): the paper portfolio behind Simulate - ONE
/// store the hero (1:3898), the Portfolio page (1:4496), Pick detail
/// (1:4631), the board / Leaderboard You rows and every practice-buy
/// ticket read, so a buy moves cash into a position and a sell moves it
/// back. Demo-seeded from the authored numbers; in production the backend
/// serves the ledger. Mirrors android ui/simulate/PaperPortfolio.kt.

/// One Portfolio row (1:4548 template). Lives here, not in
/// SimPortfolioView.swift, so Simulate home's three rows and the Portfolio
/// page list the same table.
struct SimPick: Codable {
	let badge: String
	let ticker: String
	let sub: String
	let amount: String
	let pct: String
	let up: Bool
}

final class PaperPortfolio: ObservableObject {
	static let shared = PaperPortfolio()

	/// Authored (1:3898): "$10,000 paper", "+$240.00 all time", "Cash available $8,800.00".
	static let paperStart = 10000.0
	/// All-time gain = today's value over the paper start (the demo's authored $240 falls out of its $10,240).
	var allTimeGain: Double { portfolioValue - PaperPortfolio.paperStart }
	@Published var cash: Double = 8800

	/// The authored demo account, or a fresh one (product audit, 2026-09-05; mirrors Android).
	@Published private(set) var demo = true
	private var baseValue = 10240.0
	private var baseCash = 8800.0

	/// The leaderboard rank - the demo's authored #47; a new account is unranked until it has moves.
	var rank: Int? { demo ? PaperPortfolio.weekRank : nil }
	var weekUp: Bool { demo ? true : allTimeGain >= 0 }
	var weekGainText: String { demo ? PaperPortfolio.weekGain : PaperPortfolio.signedWhole(allTimeGain) }
	var weekPctText: String { demo ? PaperPortfolio.weekPct : PaperPortfolio.signedPct(allTimeGain / PaperPortfolio.paperStart * 100) }
	/// "12 picks" is authored for the demo (its rows list six); a new account counts its own.
	var pickCountLabel: Int { demo ? 12 + (positions.count - PaperPortfolio.authoredRows.count) : positions.count }

	/// "1 pick" / "12 picks" (product audit, 2026-09-05: a first buy read "1 picks").
	var pickCountText: String { pickCountLabel == 1 ? "1 pick" : "\(pickCountLabel) picks" }

	/// Seeds the authored demo history or clears everything to $10,000 of untouched paper cash.
	func reset(demo: Bool) {
		self.demo = demo
		newStake = 0
		if demo {
			cash = 8800
			positions = PaperPortfolio.authoredRows.compactMap { row in
				PickSpecs.all.first { $0.symbol == row.ticker }.map { Position(spec: $0, row: row) }
			}
			realized = [
				Realized(badge: "S", ticker: "SHOP", sub: "Sold May 30 · profit banked", amount: "+$12.00", up: true),
				Realized(badge: "C", ticker: "COIN", sub: "Sold Jun 15 · loss realized", amount: "-$8.00", up: false)
			]
			baseValue = 10240
			baseCash = 8800
		} else {
			cash = PaperPortfolio.paperStart
			positions = []
			realized = []
			baseValue = PaperPortfolio.paperStart
			baseCash = PaperPortfolio.paperStart
		}
		// The persisted ledger (buys, sells, cash) wins over the seed - product audit
		// 2026-09-05; the seed baseline above is what value grows from.
		if let data = StakStore.data("portfolio"), let saved = try? JSONDecoder().decode(Ledger.self, from: data) {
			positions = saved.positions
			realized = saved.realized
			cash = saved.cash
			newStake = saved.newStake
		}
	}

	private struct Ledger: Codable {
		let cash: Double
		let newStake: Double
		let positions: [Position]
		let realized: [Realized]
	}

	private func persist() {
		let ledger = Ledger(cash: cash, newStake: newStake, positions: positions, realized: realized)
		if let data = try? JSONEncoder().encode(ledger) { StakStore.set(data, for: "portfolio") }
	}

	static func signedWhole(_ value: Double) -> String { (value < 0 ? "-$" : "+$") + wholeDollars(abs(value)).replacingOccurrences(of: "$", with: "") }
	static func signedMoney(_ value: Double) -> String { (value < 0 ? "-" : "+") + money(abs(value)) }
	static func signedPct(_ pct: Double) -> String { String(format: "%+.1f%%", pct) }

	/// Audit item 6 - one week, quoted everywhere: the hero's "▲ +$186
	/// (+1.9%) this week" / "#47 this week" (1:3898), the board's You row
	/// and the Leaderboard's You row (1:4124). The frames author +4.2% on
	/// the two You rows against +1.9% on the hero; the hero wins.
	static let weekRank = 47
	static let weekGain = "+$186"
	static let weekPct = "+1.9%"

	/// A held pick: the detail page's spec + its Portfolio row.
	struct Position: Identifiable, Codable {
		let spec: PickSpec
		let row: SimPick
		var id: String { spec.symbol }
	}

	/// A SOLD · REALIZED row (1:4496).
	struct Realized: Identifiable, Codable {
		let badge: String
		let ticker: String
		let sub: String
		let amount: String
		let up: Bool
		/// A ticker can be sold more than once - rows carry their own identity.
		let id = UUID()
	}

	/// The authored six rows (1:4496) in the authored order - the tickers
	/// PickSpecs.all serves to Pick detail.
	static let authoredRows: [SimPick] = [
		SimPick(badge: "N", ticker: "NVDA", sub: "Picked May 8 · up 24% since", amount: "+$24.00", pct: "+24.0%", up: true),
		SimPick(badge: "T", ticker: "TSLA", sub: "Picked Jun 3 · up 18% since", amount: "+$18.00", pct: "+18.0%", up: true),
		SimPick(badge: "A", ticker: "AMD", sub: "Picked May 29 · up 11% since", amount: "+$11.00", pct: "+11.0%", up: true),
		SimPick(badge: "A", ticker: "AAPL", sub: "Picked Apr 22 · up 6% since", amount: "+$6.00", pct: "+6.0%", up: true),
		SimPick(badge: "J", ticker: "JPM", sub: "Picked Jun 20 · up 2% since", amount: "+$2.00", pct: "+2.0%", up: true),
		SimPick(badge: "M", ticker: "MSFT", sub: "Picked Jun 26 · down 3% since", amount: "-$3.00", pct: "-3.0%", up: false)
	]

	/// Seeded NVDA, TSLA, AMD, AAPL, JPM, MSFT - each authored row paired
	/// with its PickSpecs entry (no second copy of either table).
	@Published var positions: [Position] = PaperPortfolio.authoredRows.compactMap { row in
		PickSpecs.all.first { $0.symbol == row.ticker }.map { Position(spec: $0, row: row) }
	}

	/// Authored SOLD · REALIZED rows (1:4496); a sell prepends to them.
	@Published var realized: [Realized] = [
		Realized(badge: "S", ticker: "SHOP", sub: "Sold May 30 · profit banked", amount: "+$12.00", up: true),
		Realized(badge: "C", ticker: "COIN", sub: "Sold Jun 15 · loss realized", amount: "-$8.00", up: false)
	]

	/// Net stake moved from cash into stock since launch, at cost. The demo
	/// has no live prices, so a buy moves cash into stock 1:1 (and a sell
	/// moves the position's value back) and the shown value stays
	/// $10,240.00 until prices move.
	private var newStake = 0.0

	/// The authored $10,240.00 (1:3898) plus whatever cash moved since.
	var portfolioValue: Double { baseValue + (cash - baseCash) + newStake }
	var pickCount: Int { positions.count }

	func holds(_ symbol: String) -> Bool {
		positions.contains { $0.spec.symbol == symbol }
	}

	func pickSpec(_ symbol: String) -> PickSpec? {
		positions.first { $0.spec.symbol == symbol }?.spec
	}

	/// A practice buy of `amount` paper dollars at the ticket's price: cash
	/// moves into a new position at the front of the list. A symbol already
	/// held adds to its position (shares and stake grow) - no duplicate row.
	/// True when the cash on hand covers the stake - the ticket's pills and the confirm both read it (Codex review, PR #166).
	func canBuy(_ amount: Double) -> Bool { amount > 0 && amount <= cash }

	func buy(_ spec: BuySpec, amount: Double) {
		guard canBuy(amount) else { return }
		let price = spec.price
		cash -= amount
		newStake += amount
		let newShares = amount / price
		if let i = positions.firstIndex(where: { $0.spec.symbol == spec.symbol }) {
			let held = positions[i]
			positions[i] = Position(
				spec: held.spec.holding(
					shares: (Double(held.spec.shares) ?? 0) + newShares,
					stakeValue: PaperPortfolio.amount(held.spec.stakeValue) + amount,
					// Review (2026-09-04): the basis grows by the stake put in ($100 + $25 -> "$125").
					stakeBasis: PaperPortfolio.amount(held.spec.stakeBasis) + amount
				),
				row: held.row
			)
			persist()
			return
		}
		let day = PaperPortfolio.today()
		let priceText = PaperPortfolio.money(price)
		let pick = PickSpec(
			symbol: spec.symbol, badge: spec.badge, company: spec.name,
			priceNow: priceText, priceThen: priceText, pickedLine: "Picked \(day) at \(priceText)",
			gainWhole: "+$0", gainCents: ".00", gainSigned: "+$0.00", gainPct: "0.0%", up: true,
			shares: PaperPortfolio.shares(newShares), vsMarket: "Even with the market", ahead: true,
			// The ticket's day move ("▲ 1.1%") is the sell row's change.
			dayChange: spec.change, dayUp: !spec.change.hasPrefix("▼"),
			stakeValue: PaperPortfolio.money(amount),
			// Review (2026-09-04): the ticket's stake is the cost basis; no week move yet.
			stakeBasis: PaperPortfolio.stakeLabel(amount), weekGain: "+$0.00"
		)
		let row = SimPick(
			badge: spec.badge, ticker: spec.symbol, sub: "Picked \(day) · just bought",
			amount: "+$0.00", pct: "+0.0%", up: true
		)
		positions.insert(Position(spec: pick, row: row), at: 0)
		persist()
	}

	/// Closes the position: its stake value returns to cash and the pick
	/// joins SOLD · REALIZED, dated today. Review (2026-09-04): false when
	/// the symbol is not held - no phantom sell; the hosts only morph to
	/// "Position closed" on true.
	@discardableResult
	func sell(_ symbol: String) -> Bool {
		guard let i = positions.firstIndex(where: { $0.spec.symbol == symbol }) else { return false }
		let spec = positions.remove(at: i).spec
		let stake = PaperPortfolio.amount(spec.stakeValue)
		cash += stake
		newStake -= stake
		realized.insert(
			Realized(
				badge: spec.badge, ticker: symbol,
				sub: "Sold \(PaperPortfolio.today()) · \(spec.up ? "profit banked" : "loss realized")",
				amount: spec.gainSigned, up: spec.up
			),
			at: 0
		)
		persist()
		return true
	}

	/// Review (2026-09-04): the BEST PICK / WORST PICK tiles (1:3898) -
	/// the largest and smallest dollar gain across the ledger (the row
	/// amount, "+$24.00"). Seeded: NVDA +$24 / MSFT -$3, as authored.
	var best: Position? {
		positions.max { PaperPortfolio.amount($0.row.amount) < PaperPortfolio.amount($1.row.amount) }
	}
	var worst: Position? {
		positions.min { PaperPortfolio.amount($0.row.amount) < PaperPortfolio.amount($1.row.amount) }
	}

	/// Review (2026-09-04): a stake as its label - whole dollars "$25"
	/// (grouped, no decimals), otherwise "$25.50".
	static func stakeLabel(_ amount: Double) -> String {
		amount == amount.rounded() ? wholeDollars(amount) : money(amount)
	}

	/// Review (2026-09-04): a gain to whole dollars with its sign - the
	/// tiles' "+$24" / "-$3".
	static func gainLabel(_ gain: Double) -> String {
		(gain < 0 ? "-" : "+") + wholeDollars(abs(gain).rounded())
	}

	static func wholeDollars(_ value: Double) -> String {
		"$" + (wholeFormatter.string(from: NSNumber(value: value)) ?? String(format: "%.0f", value))
	}

	private static let wholeFormatter: NumberFormatter = {
		let f = NumberFormatter()
		f.locale = Locale(identifier: "en_US_POSIX")
		f.numberStyle = .decimal
		f.usesGroupingSeparator = true
		f.minimumFractionDigits = 0
		f.maximumFractionDigits = 0
		f.roundingMode = .halfUp
		return f
	}()

	/// "$1,234.56" - every cash figure formats through here: "$" +
	/// en_US_POSIX, two decimals, grouping, half-up (Java's %,.2f rounds
	/// half up: android parity).
	static func money(_ value: Double) -> String {
		"$" + (moneyFormatter.string(from: NSNumber(value: value)) ?? String(format: "%.2f", value))
	}

	private static let moneyFormatter: NumberFormatter = {
		let f = NumberFormatter()
		f.locale = Locale(identifier: "en_US_POSIX")
		f.numberStyle = .decimal
		f.usesGroupingSeparator = true
		f.minimumFractionDigits = 2
		f.maximumFractionDigits = 2
		f.roundingMode = .halfUp
		return f
	}()

	/// "$1,234.56" -> 1234.56: a PickSpec's stake value read back.
	static func amount(_ money: String) -> Double {
		Double(
			money.replacingOccurrences(of: "$", with: "")
				.replacingOccurrences(of: ",", with: "")
				.replacingOccurrences(of: "+", with: "")
		) ?? 0
	}

	/// Shares to four places - the tickets' "0.8803".
	static func shares(_ value: Double) -> String {
		String(format: "%.4f", value)
	}

	/// "Sep 4" - the picked / sold date (en_US_POSIX "MMM d").
	static func today() -> String {
		let f = DateFormatter()
		f.locale = Locale(identifier: "en_US_POSIX")
		f.dateFormat = "MMM d"
		return f.string(from: Date())
	}

	private init() {}
}

private extension PickSpec {
	/// The same pick holding more: a top-up buy adds shares and stake at
	/// cost; the authored gain lines (and the week move) stand until
	/// prices move. Review (2026-09-04): the basis follows the stake.
	func holding(shares: Double, stakeValue: Double, stakeBasis: Double) -> PickSpec {
		PickSpec(
			symbol: symbol, badge: badge, company: company, priceNow: priceNow, priceThen: priceThen,
			pickedLine: pickedLine, gainWhole: gainWhole, gainCents: gainCents, gainSigned: gainSigned,
			gainPct: gainPct, up: up, shares: PaperPortfolio.shares(shares), vsMarket: vsMarket, ahead: ahead,
			dayChange: dayChange, dayUp: dayUp, stakeValue: PaperPortfolio.money(stakeValue),
			stakeBasis: PaperPortfolio.stakeLabel(stakeBasis), weekGain: weekGain
		)
	}
}
