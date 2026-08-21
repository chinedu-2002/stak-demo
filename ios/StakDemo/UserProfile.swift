import SwiftUI

/// Session-scoped user identity captured in Onboarding · 09 Profile setup
/// (Figma node 1:793). Mirrors android ui/UserProfile.kt: the display name
/// feeds the Home greeting and the Profile hub; "Hamza" is the design's
/// demo persona shown until the user sets a name.
final class UserProfile: ObservableObject {
	static let shared = UserProfile()

	@Published var displayName: String = ""
	@Published var photoData: Data? = nil

	var greetingName: String {
		let name = displayName.trimmingCharacters(in: .whitespaces)
		return name.isEmpty ? "Hamza" : name
	}

	private init() {}
}
