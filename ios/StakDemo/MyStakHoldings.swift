import SwiftUI

/// The stocks in the user's My STAK (user, 2026-08-23): a story earns the
/// "In your STAK" chip only when it relates to a stock the user actually
/// holds - Google news + Google in My STAK -> chip; otherwise none.
///
/// Demo seed = the authored My STAK (Collection NVDA/AAPL/MSFT/GOOGL/AMD
/// + Overview TSLA/SNOW). Saves from the Discover deck and the article
/// bookmark add to it. In production the backend serves the holdings.
/// Mirrors android ui/MyStakHoldings.kt.
final class MyStakHoldings: ObservableObject {
	static let shared = MyStakHoldings()

	@Published private(set) var tickers: Set<String> = ["NVDA", "AAPL", "MSFT", "GOOGL", "AMD", "TSLA", "SNOW"]

	/// True when any of the story's related tickers is held.
	func holdsAny(_ related: [String]) -> Bool {
		related.contains { tickers.contains($0) }
	}

	func add(_ ticker: String) {
		tickers.insert(ticker)
	}

	private init() {}
}
