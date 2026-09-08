import SwiftUI

@main
struct StakDemoApp: App {
	@ObservedObject private var profile = UserProfile.shared
	// Answers the per-moment orientation mask (portrait, landscape only in the article player's fullscreen).
	@UIApplicationDelegateAdaptor(AppDelegate.self) private var appDelegate

	var body: some Scene {
		WindowGroup {
			RootFlowView()
				// The STAK app is dark-only by design; ignore the system light theme.
				// Dark is the authored design; Light (or Match system) is its mapping,
				// from the Appearance setting (user, 2026-09-07).
				.preferredColorScheme(StakAppearance.scheme(for: profile.appearance))
		}
	}
}

/// The Appearance setting -> SwiftUI colour scheme ("system" = follow the phone).
enum StakAppearance {
	static func scheme(for appearance: String) -> ColorScheme? {
		switch appearance {
		case "light": return .light
		case "system": return nil
		default: return .dark
		}
	}
}
