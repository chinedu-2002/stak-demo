import SwiftUI

/// Home bell badge state (CHINEDU 151:1207). Mirrors android
/// ui/StakNotifications.kt.
///
/// The orange dot on the bell marks UNTOUCHED notifications: it stays
/// while anything remains unread and goes off once the user has opened
/// and read them (designer, 2026-08-22). In production the backend
/// serves the notification items and per-item read state drives this
/// flag; the demo carries one authored unread set so the rest state
/// matches the frame (dot on), and opening the bell reads it.
///
/// The notification PANEL the bell opens has no designed frame yet -
/// the tap only marks the set read until the designer draws it.
final class StakNotifications: ObservableObject {
	static let shared = StakNotifications()

	@Published private(set) var hasUnread = true

	func markAllRead() {
		hasUnread = false
	}

	private init() {}
}
