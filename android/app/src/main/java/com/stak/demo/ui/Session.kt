package com.stak.demo.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Persisted sign-in state (user, 2026-08-23): a user who already signed
 * in is not asked to sign in again - the splash goes straight to Home;
 * a first-time user is taken to create an account. Backed by
 * SharedPreferences until the real auth backend lands (then this is
 * the seam that holds the token + the profile it returns).
 */
object Session {
	private const val PREFS = "stak_session"
	private const val KEY_SIGNED_IN = "signed_in"
	private const val KEY_NAME = "display_name"
	private const val KEY_PHOTO = "photo_uri"
	private const val KEY_RISK = "risk_style"
	private const val KEY_DEMO = "demo_account"
	private const val KEY_PICKS = "brand_picks"
	private const val KEY_GOAL = "goal_answer"
	private const val KEY_RISK_ANSWER = "risk_answer"

	private var prefs: SharedPreferences? = null

	var signedIn by mutableStateOf(false)
		private set

	/** True when this launch started already signed in - the returning-user path. */
	var resumedSignedIn = false
		private set

	/**
	 * Which account this is (product audit, 2026-09-05). Sign in = the DEMO
	 * account with the authored history (19 saved stocks, $10,240, #47);
	 * Create account = a NEW account that starts empty and earns its numbers.
	 * Persisted with the sign-in so a relaunch restores the same account.
	 */
	var demoAccount by mutableStateOf(true)
		private set

	/** Load once per process; restores the profile the user set before. */
	fun init(context: Context) {
		if (prefs != null) return
		val p = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
		prefs = p
		StakStore.init(context)
		signedIn = p.getBoolean(KEY_SIGNED_IN, false)
		resumedSignedIn = signedIn
		demoAccount = p.getBoolean(KEY_DEMO, true)
		UserProfile.displayName = p.getString(KEY_NAME, "") ?: ""
		UserProfile.photoUri = p.getString(KEY_PHOTO, null)
		UserProfile.riskStyle = p.getString(KEY_RISK, UserProfile.riskStyle) ?: UserProfile.riskStyle
		UserProfile.brandPicks = p.getString(KEY_PICKS, "")?.split(",")?.filter { it.isNotBlank() }?.toSet() ?: emptySet()
		UserProfile.goal = p.getInt(KEY_GOAL, -1)
		UserProfile.risk = p.getInt(KEY_RISK_ANSWER, -1)
		applyAccount()
	}

	/** Sign-in CTA or account creation (09 Proceed) - remembered across launches. */
	/** `demo` = the authored demo account (Sign in); false = a fresh account (Create account). */
	fun signIn(demo: Boolean) {
		signedIn = true
		demoAccount = demo
		persist()
		// A brand-new account starts from nothing; the demo account keeps
		// whatever it did last time it was signed in.
		if (!demo) StakStore.clearAccount(demo = false)
		applyAccount()
	}

	/** Seeds (demo) or clears (new account) every user-data singleton for the current account. */
	fun applyAccount() {
		MyStakHoldings.reset(demo = demoAccount)
		com.stak.demo.ui.simulate.PaperPortfolio.reset(demo = demoAccount)
		com.stak.demo.ui.discover.DeckSession.load()
		StakNotifications.load()
		com.stak.demo.ui.news.NewsSaves.load()
	}

	/** Profile edits after sign-in (name/photo) stay with the session. */
	fun saveProfile() = persist()

	/** Log out: forget the session and the profile; next launch asks to sign in. */
	fun signOut() {
		signedIn = false
		resumedSignedIn = false
		demoAccount = true
		UserProfile.displayName = ""
		UserProfile.photoUri = null
		UserProfile.riskStyle = "Growth-Oriented"
		UserProfile.brandPicks = emptySet()
		UserProfile.goal = -1
		UserProfile.risk = -1
		prefs?.edit()?.clear()?.apply()
		applyAccount()
	}

	private fun persist() {
		prefs?.edit()
			?.putBoolean(KEY_SIGNED_IN, signedIn)
			?.putBoolean(KEY_DEMO, demoAccount)
			?.putString(KEY_NAME, UserProfile.displayName)
			?.putString(KEY_PHOTO, UserProfile.photoUri)
			?.putString(KEY_RISK, UserProfile.riskStyle)
			?.putString(KEY_PICKS, UserProfile.brandPicks.joinToString(","))
			?.putInt(KEY_GOAL, UserProfile.goal)
			?.putInt(KEY_RISK_ANSWER, UserProfile.risk)
			?.apply()
	}
}
