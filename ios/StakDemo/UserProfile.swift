import SwiftUI

/// Session-scoped user identity captured in Onboarding · 09 Profile setup
/// (Figma node 1:793). Mirrors android ui/UserProfile.kt: the display name
/// feeds the Home greeting and the Profile hub; "Hamza" is the design's
/// demo persona shown until the user sets a name.
final class UserProfile: ObservableObject {
	static let shared = UserProfile()

	@Published var displayName: String = ""
	@Published var photoData: Data? = nil

	/// The name as the app addresses the user - always capitalized.
	var greetingName: String {
		let name = displayName.trimmingCharacters(in: .whitespaces)
		return (name.isEmpty ? "Hamza" : name).capitalizedWords
	}

	/// BACKEND CONTRACT (user, 2026-08-22): the app sends the device's IANA
	/// timezone ID (e.g. "Africa/Lagos", "Europe/Berlin", "Asia/Shanghai")
	/// with the session/profile so the backend can schedule per-user
	/// LOCAL-time deliveries - the day's breaking deck and mood at the
	/// user's morning, notification timing, daily resets. The device clock
	/// is the source of truth (auto time zone from the network); nothing is
	/// inferred from IP or location. Read live so travel is reflected.
	var timeZoneId: String { TimeZone.current.identifier }

	private init() {}
}

extension String {
	/// "fish" -> "Fish", "mary ann" -> "Mary Ann": the first letter of every
	/// word uppercased, the rest left as typed (user, 2026-08-22: names
	/// typed in lower case must still read capitalized everywhere).
	var capitalizedWords: String {
		split(separator: " ", omittingEmptySubsequences: false)
			.map { w -> String in
				guard let first = w.first else { return String(w) }
				return first.uppercased() + w.dropFirst()
			}
			.joined(separator: " ")
	}
}
