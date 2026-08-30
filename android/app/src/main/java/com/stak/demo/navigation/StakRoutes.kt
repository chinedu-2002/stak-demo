package com.stak.demo.navigation

/** Route names for the whole app. Onboarding runs once, then the tabs. */
object StakRoutes {
	const val SPLASH = "splash"

	/** 01 Welcome — `via` picks the prototype push direction (forward/back). */
	const val INTRO = "onboarding/intro?via={via}"
	fun intro(via: String) = "onboarding/intro?via=$via"
	const val BRAND_PICKS = "onboarding/brand-picks"
	const val SWIPE_TUTORIAL = "onboarding/swipe-tutorial"
	const val GOAL = "onboarding/goal"
	const val RISK = "onboarding/risk"
	const val PREPARING_DECK = "onboarding/preparing-deck"
	const val TASTE_REVEAL = "onboarding/taste-reveal"
	const val SIGN_IN = "auth/sign-in"
	/** Sign up — `via` = "back" when sign-in's post-logout back circle re-opens it (B21). */
	const val CREATE_ACCOUNT = "onboarding/create-account?via={via}"
	fun createAccount(via: String) = "onboarding/create-account?via=$via"
	const val PERMISSIONS = "onboarding/permissions"
	const val PROFILE_SETUP = "onboarding/profile-setup"
	const val MAIN = "main" // bottom-tab shell (Home/News/Discover/My STAK/Simulate)
	const val NEWS_DETAIL = "news/detail/{articleId}"
	fun newsDetail(articleId: String) = "news/detail/$articleId"
	const val COLLECTION = "mystak/collection"
	const val MYSTAK_STOCK = "mystak/stock"
	const val PROFILE = "profile"
	const val SIM_PORTFOLIO = "simulate/portfolio"
	const val SIM_PICK = "simulate/pick"
	const val LEADERBOARD = "simulate/leaderboard"

	const val STOCK_DETAIL = "stock/{symbol}"
	fun stockDetail(symbol: String) = "stock/$symbol"
	const val TRADE_TICKET = "trade/{symbol}"
	fun tradeTicket(symbol: String) = "trade/$symbol"
	const val ORDER_CONFIRMATION = "order-confirmation/{symbol}"
	fun orderConfirmation(symbol: String) = "order-confirmation/$symbol"
	const val ADD_CASH = "add-cash"
	const val SEARCH = "search"
}
