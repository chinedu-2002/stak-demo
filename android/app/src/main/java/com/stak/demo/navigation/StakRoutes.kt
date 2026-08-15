package com.stak.demo.navigation

/** Route names for the whole app. Onboarding runs once, then the tabs. */
object StakRoutes {
	const val SPLASH = "splash"
	const val INTRO = "onboarding/intro"
	const val BRAND_PICKS = "onboarding/brand-picks"
	const val SWIPE_TUTORIAL = "onboarding/swipe-tutorial"
	const val GOAL = "onboarding/goal"
	const val RISK = "onboarding/risk"
	const val PREPARING_DECK = "onboarding/preparing-deck"
	const val TASTE_REVEAL = "onboarding/taste-reveal"
	const val SIGN_IN = "auth/sign-in"
	const val CREATE_ACCOUNT = "onboarding/create-account"
	const val PERMISSIONS = "onboarding/permissions"
	const val PROFILE_SETUP = "onboarding/profile-setup"
	const val MAIN = "main" // bottom-tab shell (Home/Discover/Watchlist/Simulate/Profile)

	const val STOCK_DETAIL = "stock/{symbol}"
	fun stockDetail(symbol: String) = "stock/$symbol"
	const val TRADE_TICKET = "trade/{symbol}"
	fun tradeTicket(symbol: String) = "trade/$symbol"
	const val ORDER_CONFIRMATION = "order-confirmation/{symbol}"
	fun orderConfirmation(symbol: String) = "order-confirmation/$symbol"
	const val ADD_CASH = "add-cash"
	const val SEARCH = "search"
}
