import SwiftUI

/// Bottom-tab shell. Tab screens land here in the next build phase.
struct MainTabsView: View {
	@State private var tab = StakTab.home

	var body: some View {
		VStack(spacing: 0) {
			// Placeholder until the tab screens are implemented (phase 3).
			Text(tab.rawValue)
				.foregroundStyle(Color.white)
				.frame(maxWidth: .infinity, maxHeight: .infinity)
			StakTabBar(selected: $tab)
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}
