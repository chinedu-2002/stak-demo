import SwiftUI

/// The stocks in the user's My STAK (user, 2026-08-23): a story earns the
/// "In your STAK" chip only when it relates to a stock the user actually
/// holds - Google news + Google in My STAK -> chip; otherwise none.
///
/// Codex audit (2026-09-04): this store is the single source of truth for
/// every My STAK count. The frame (1:3155) authored three numbers that never
/// agreed - chips 5/3/3/2/4/2, "Across 14 stocks", allocation 6+3+3+2+1 -
/// so the seed grew to every ticker the six authored collections list plus
/// the Overview's Best/Worst (TSLA, SNOW) = 21: the chips still read their
/// authored counts at rest and every other number derives from here.
/// Saves from the Discover deck and the article bookmark add to it; the
/// saved Stock Detail's Unsave removes. In production the backend serves
/// the holdings. Mirrors android ui/MyStakHoldings.kt.
final class MyStakHoldings: ObservableObject {
	static let shared = MyStakHoldings()

	@Published private(set) var tickers: Set<String> = [
		// AI & Tech
		"NVDA", "AAPL", "MSFT", "GOOGL", "AMD",
		// Finance
		"JPM", "V", "GS",
		// Green Energy
		"ENPH", "NEE", "FSLR",
		// Real Estate
		"PLD", "O",
		// Healthcare
		"LLY", "UNH", "JNJ", "PFE",
		// Consumer
		"COST", "NKE",
		// Overview Best / Worst - no collection lists them
		"TSLA", "SNOW"
	]

	/// Every held ticker - the Overview's "Across N stocks".
	var count: Int { tickers.count }

	/// True when any of the story's related tickers is held.
	func holdsAny(_ related: [String]) -> Bool {
		related.contains { tickers.contains($0) }
	}

	func add(_ ticker: String) {
		tickers.insert(bare(ticker))
	}

	/// The saved Stock Detail's Unsave - the collection page and every
	/// count drop the stock together (Codex audit 2026-09-04).
	func remove(_ ticker: String) {
		tickers.remove(bare(ticker))
	}

	/// Deck cards carry "NVDA · NVIDIA Corp" - hold the bare symbol.
	private func bare(_ ticker: String) -> String {
		ticker.components(separatedBy: " · ").first ?? ticker
	}

	private init() {}
}
