package com.stak.demo.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Home bell badge state (CHINEDU 151:1207).
 *
 * The orange dot on the bell marks UNTOUCHED notifications: it stays
 * while anything remains unread and goes off once the user has opened
 * and read them (designer, 2026-08-22). In production the backend
 * serves the notification items and per-item read state drives this
 * flag; the demo carries one authored unread set so the rest state
 * matches the frame (dot on), and opening the bell reads it.
 *
 * Notification timing is per user: the backend sends time-sensitive
 * items (the morning deck, mood alerts) at the user's LOCAL hours using
 * the timezone ID the app sends with the session (UserProfile.timeZoneId).
 *
 * The notification PANEL the bell opens has no designed frame yet -
 * the tap only marks the set read until the designer draws it.
 */
object StakNotifications {
	var hasUnread by mutableStateOf(true)
		private set

	fun markAllRead() {
		hasUnread = false
		com.stak.demo.ui.StakStore.putBoolean("notif.unread", false)
	}

	/** Restores the badge for the current account (product audit, 2026-09-05). */
	fun load() {
		hasUnread = com.stak.demo.ui.StakStore.getBoolean("notif.unread", true)
	}
}
