package com.stak.demo.ui.home

/**
 * Market Mood news deck data source (CHINEDU 1:1162).
 *
 * CONTRACT (user, 2026-08-22): the deck is proxied to REAL-TIME news -
 * "news don't stay the same, it changes everyday". The STAK BACKEND
 * propagates the day's breaking stories, the kind that makes a user
 * want to open them, and the app renders whatever is served into the
 * authored deck slots (poses, colors and typography stay authored).
 *
 * LIVE stays false this phase so the deck always matches the frame;
 * the demo stories below are the authored copy.
 */
object NewsDeckFeed {
	/** Production switch - keep false while reviews compare build vs frame. */
	const val LIVE = false

	data class Story(val title: String, val body: String)

	val DEMO_STORIES = listOf(
		Story(
			"Wall Street's fear gauge reads 32",
			"The Fear & Greed Index is firmly in Fear territory. Money is rotating out of the ....",
		),
		Story(
			"Fed meeting notes drop Wednesday",
			"Minutes from the last Fed meeting land July 8. A market this tense moves on every word....",
		),
		Story(
			"The OpenAI IPO is reportedly delayed",
			"The year's most anticipated listing just slipped. Markets riding a wave of IPO excitement...",
		),
	)

	/** The current stories - the served breaking news once the backend exists. */
	fun stories(): List<Story> = DEMO_STORIES
}
