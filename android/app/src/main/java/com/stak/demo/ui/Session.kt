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

	private var prefs: SharedPreferences? = null

	var signedIn by mutableStateOf(false)
		private set

	/** True when this launch started already signed in - the returning-user path. */
	var resumedSignedIn = false
		private set

	/** Load once per process; restores the profile the user set before. */
	fun init(context: Context) {
		if (prefs != null) return
		val p = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
		prefs = p
		signedIn = p.getBoolean(KEY_SIGNED_IN, false)
		resumedSignedIn = signedIn
		UserProfile.displayName = p.getString(KEY_NAME, "") ?: ""
		UserProfile.photoUri = p.getString(KEY_PHOTO, null)
		UserProfile.riskStyle = p.getString(KEY_RISK, UserProfile.riskStyle) ?: UserProfile.riskStyle
	}

	/** Sign-in CTA or account creation (09 Proceed) - remembered across launches. */
	fun signIn() {
		signedIn = true
		persist()
	}

	/** Profile edits after sign-in (name/photo) stay with the session. */
	fun saveProfile() = persist()

	/** Log out: forget the session and the profile; next launch asks to sign in. */
	fun signOut() {
		signedIn = false
		resumedSignedIn = false
		UserProfile.displayName = ""
		UserProfile.photoUri = null
		UserProfile.riskStyle = "Growth-Oriented"
		prefs?.edit()?.clear()?.apply()
	}

	private fun persist() {
		prefs?.edit()
			?.putBoolean(KEY_SIGNED_IN, signedIn)
			?.putString(KEY_NAME, UserProfile.displayName)
			?.putString(KEY_PHOTO, UserProfile.photoUri)
			?.putString(KEY_RISK, UserProfile.riskStyle)
			?.apply()
	}
}
