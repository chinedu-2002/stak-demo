import Foundation

/// The dates the demo shows, on the real calendar (product audit, 2026-09-05: the
/// app was frozen on "Saturday, July 4" while the greeting used the live clock).
/// Story and save dates are relative ages, so they stay fresh. Mirrors android
/// StakClock.kt.
enum StakClock {
	private static func formatter(_ pattern: String) -> DateFormatter {
		let f = DateFormatter(); f.locale = Locale(identifier: "en_US"); f.dateFormat = pattern; return f
	}

	/// "Saturday, July 4" for today.
	static func todayLong() -> String { formatter("EEEE, MMMM d").string(from: Date()) }

	/// "Jul 2" for N days ago.
	static func monthDay(daysAgo: Int) -> String {
		formatter("MMM d").string(from: Calendar.current.date(byAdding: .day, value: -daysAgo, to: Date()) ?? Date())
	}

	/// "· Jul 2 · 3 min read" from a story's age ("2d", "5h") and its read time.
	static func byline(_ age: String, _ read: String) -> String {
		let days = age.hasSuffix("d") ? Int(age.dropLast()) ?? 0 : 0
		return "· \(monthDay(daysAgo: days)) · \(read)"
	}

	/// "Saved Jul 2" / "Saved today".
	static func savedLabel(daysAgo: Int) -> String { daysAgo == 0 ? "Saved today" : "Saved \(monthDay(daysAgo: daysAgo))" }

	/// "September 2026" for the month a new account was created.
	static func monthYear() -> String { formatter("MMMM yyyy").string(from: Date()) }
}
