package com.stak.demo.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * What the user calls themself — set on Onboarding 09 Profile setup and
 * read wherever the app addresses them. Falls back to the design's demo
 * persona when no name was set (e.g. the sign-in path).
 */
object UserProfile {
	var displayName by mutableStateOf("")
	var photoUri by mutableStateOf<String?>(null)

	/** The name as the app addresses the user - always capitalized. */
	val greetingName: String
		get() = displayName.ifBlank { "Hamza" }.capitalizeWords()

	/**
	 * BACKEND CONTRACT (user, 2026-08-22): the app sends the device's IANA
	 * timezone ID (e.g. "Africa/Lagos", "Europe/Berlin", "Asia/Shanghai")
	 * with the session/profile so the backend can schedule per-user
	 * LOCAL-time deliveries - the day's breaking deck and mood at the
	 * user's morning, notification timing, daily resets. The device clock
	 * is the source of truth (auto time zone from the network); nothing is
	 * inferred from IP or location. Read live so travel is reflected.
	 */
	val timeZoneId: String
		get() = java.time.ZoneId.systemDefault().id
}

/**
 * "fish" -> "Fish", "mary ann" -> "Mary Ann": the first letter of every
 * word uppercased, the rest left as typed (user, 2026-08-22: names typed
 * in lower case must still read capitalized everywhere).
 */
fun String.capitalizeWords(): String =
	split(" ").joinToString(" ") { w -> w.replaceFirstChar { c -> c.titlecase() } }
