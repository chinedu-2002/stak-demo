package com.stak.demo.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * The stocks in the user's My STAK (user, 2026-08-23): a story earns the
 * "In your STAK" chip only when it relates to a stock the user actually
 * holds - Google news + Google in My STAK -> chip; otherwise none.
 *
 * Codex audit (2026-09-04): this store is the single source of truth for
 * every My STAK number - the chip counts, "Across N stocks", the
 * Breakdown buckets and the Collection page's tiles all derive from it,
 * and Unsave drops a ticker from all of them at once. The frame (1:3155)
 * authored three different totals (chips 5/3/3/2/4/2, "Across 14 stocks",
 * "6 stocks" in the breakdown); one store cannot honour three, so the
 * seed is every ticker the six authored collections list plus the
 * Overview's Best/Worst (TSLA/SNOW) = 21, which keeps the chips reading
 * 5/3/3/2/4/2 at rest. Saves from the Discover deck and the article
 * bookmark add to it. In production the backend serves the holdings.
 * Mirrors ios/StakDemo/MyStakHoldings.swift.
 */
object MyStakHoldings {
	var tickers by mutableStateOf(
		setOf(
			"NVDA", "AAPL", "MSFT", "GOOGL", "AMD", // AI & Tech
			"JPM", "V", "GS", // Finance
			"ENPH", "NEE", "FSLR", // Green Energy
			"PLD", "O", // Real Estate
			"LLY", "UNH", "JNJ", "PFE", // Healthcare
			"COST", "NKE", // Consumer
			"TSLA", "SNOW", // Overview Best/Worst - no collection lists them
		),
	)
		private set

	/** How many stocks the user holds - the Overview's "Across N stocks". */
	val count: Int get() = tickers.size

	/** True when any of the story's related tickers is held. */
	fun holdsAny(related: List<String>): Boolean = related.any { it in tickers }

	fun add(ticker: String) {
		tickers = tickers + symbolOf(ticker)
	}

	/** Unsave (Stock Detail from My STAK) - the same bare-symbol normalisation as add. */
	fun remove(ticker: String) {
		tickers = tickers - symbolOf(ticker)
	}

	// Deck cards carry "NVDA · NVIDIA Corp" - hold the bare symbol.
	private fun symbolOf(ticker: String): String = ticker.substringBefore(" · ").trim()
}
