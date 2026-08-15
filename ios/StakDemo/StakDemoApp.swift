import SwiftUI

@main
struct StakDemoApp: App {
	var body: some Scene {
		WindowGroup {
			RootFlowView()
				// The STAK app is dark-only by design; ignore the system light theme.
				.preferredColorScheme(.dark)
		}
	}
}
