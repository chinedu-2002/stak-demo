import SwiftUI

/// The stories the user saved (bookmark / Add to STAK), shared by every article
/// page and persisted across relaunches (product audit, 2026-09-05: the flag used
/// to live in one view's @State). Mirrors android NewsSaves.kt.
final class NewsSaves: ObservableObject {
	static let shared = NewsSaves()
	@Published private(set) var ids: Set<String> = []

	func load() {
		ids = StakStore.stringSet("news.saved") ?? []
	}

	func add(_ id: String) {
		guard !ids.contains(id) else { return }
		ids.insert(id)
		StakStore.set(ids, for: "news.saved")
	}

	private init() {}
}
