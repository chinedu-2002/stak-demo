package com.stak.demo.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * The stocks in the user's My STAK (user, 2026-08-23): a story earns the
 * "In your STAK" chip only when it relates to a stock the user actually
 * holds - Google news + Google in My STAK -> chip; otherwise none.
 *
 * Demo seed = the authored My STAK (Collection NVDA/AAPL/MSFT/GOOGL/AMD
 * + Overview TSLA/SNOW). Saves from the Discover deck and the article
 * bookmark add to it. In production the backend serves the holdings.
 */
object MyStakHoldings {
	var tickers by mutableStateOf(setOf("NVDA", "AAPL", "MSFT", "GOOGL", "AMD", "TSLA", "SNOW"))
		private set

	/** True when any of the story's related tickers is held. */
	fun holdsAny(related: List<String>): Boolean = related.any { it in tickers }

	fun add(ticker: String) {
		tickers = tickers + ticker
	}
}
