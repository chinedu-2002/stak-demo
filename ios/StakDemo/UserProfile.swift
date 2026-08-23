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
