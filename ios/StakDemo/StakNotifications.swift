import SwiftUI

/// The notification inbox behind the Home bell (product audit, 2026-09-05: the
/// bell only cleared its dot and opened nothing). The demo account carries its
/// authored activity; a new account gets its welcome. Read state persists per
/// account (StakStore). The orange dot on the bell marks UNREAD items
/// (designer, 2026-08-22) and clears once the inbox has been opened.
/// Mirrors android StakNotifications.kt.
final class StakNotifications: ObservableObject {
	static let shared = StakNotifications()

	struct Item: Identifiable {
		let id: String
		let title: String
		let body: String
		let time: String
	}

	private static let demo: [Item] = [
		Item(id: "nvda-up", title: "NVDA is up 4.2% today", body: "Chip demand keeps outrunning supply. Your biggest pick is leading the deck.", time: "2h"),
		Item(id: "deck-ready", title: "Your deck is ready", body: "Twelve fresh cards, tuned to your taste. Swipe when you have a minute.", time: "8h"),
		Item(id: "weekly-recap", title: "Weekly recap", body: "You’re up +1.9% this week and #47 on the board. Nice.", time: "1d")
	]

	private static func welcome() -> [Item] {
		[
			Item(id: "welcome", title: "Welcome to STAK, \(UserProfile.shared.greetingName)", body: "Your first deck is waiting in Discover. Swipe down for the next card, save what you like.", time: "Just now"),
			Item(id: "first-save", title: "Save a stock to start your STAK", body: "Saved stocks power My STAK and the Simulate leaderboard.", time: "Just now")
		]
	}

	@Published private(set) var items: [Item] = []
	@Published private(set) var readIds: Set<String> = []

	var unreadCount: Int { items.filter { !readIds.contains($0.id) }.count }
	/// The Home bell's dot.
	var hasUnread: Bool { unreadCount > 0 }

	/// Restores the inbox for the current account.
	func load() {
		items = Session.shared.demoAccount ? Self.demo : Self.welcome()
		readIds = StakStore.stringSet("notif.read") ?? []
	}

	/// Opening the inbox reads everything - like an activity feed.
	func markAllRead() {
		readIds = Set(items.map(\.id))
		StakStore.set(readIds, for: "notif.read")
	}

	private init() {}
}
