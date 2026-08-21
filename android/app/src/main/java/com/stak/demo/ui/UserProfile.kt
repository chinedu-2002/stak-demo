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

	val greetingName: String
		get() = displayName.ifBlank { "Hamza" }
}
