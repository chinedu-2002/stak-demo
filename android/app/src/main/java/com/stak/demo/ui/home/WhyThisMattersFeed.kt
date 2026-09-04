package com.stak.demo.ui.home

/**
 * "Why this matters to you" data source (CHINEDU 1:1176).
 *
 * CONTRACT (user, 2026-08-23): the card is a SUMMARY of why the day's
 * market news matters to THIS user - computed by the STAK BACKEND from
 * (a) the current market news, (b) the stocks the user holds
 * (MyStakHoldings - the same holdings that gate the "In your STAK"
 * chip), and (c) the user's risk profile (UserProfile.riskStyle, from
 * the onboarding answers). The app renders the served summary in the
 * authored card; the title stays authored.
 *
 * LIVE stays false this phase; the demo body is the authored copy so
 * the rest state matches the frame.
 */
object WhyThisMattersFeed {
	/** Production switch - keep false while reviews compare build vs frame. */
	const val LIVE = false

	// Authored copy verbatim (user, 2026-09-04 (CHINEDU 02 · Home dev 118:1633): the exact frame wins, spelling included).
	const val DEMO_BODY = "Your STAK collections houses 80% of stocks from effected industries."

	/** The current summary - the served personalized copy once the backend exists. */
	fun body(): String = DEMO_BODY
}
