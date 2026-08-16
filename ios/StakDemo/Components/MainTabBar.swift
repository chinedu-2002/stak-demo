import SwiftUI

/// The five CHINEDU tabs. Home and News are built; the rest land later.
/// Mirrors android/ ui/components/MainTabBar.kt (MainTab).
enum MainTab: String, CaseIterable, Identifiable {
	case home = "Home"
	case news = "News"
	case discover = "Discover"
	case myStak = "My STAK"
	case simulate = "Simulate"

	var id: String { rawValue }

	/// Filled/bright glyph shown while the tab is selected.
	var activeIcon: String {
		switch self {
		case .home: return "IcTabHome"
		case .news: return "IcTabNewsActive"
		case .discover: return "TabDiscoverActive"
		case .myStak: return "IcTabMystakActive"
		case .simulate: return "TabSimulateActive"
		}
	}

	/// Muted outline glyph for the resting state.
	var inactiveIcon: String {
		switch self {
		case .home: return "IcTabHomeInactive"
		case .news: return "IcTabNews"
		case .discover: return "IcTabDiscover"
		case .myStak: return "IcTabMystak"
		case .simulate: return "IcTabSimulate"
		}
	}

	var built: Bool {
		true
	}
}

/// CHINEDU tab bar (Home Main 1:1220 / News listing 1:1352) — 86pt
/// #060c1d, five 24pt icons with 12pt white labels (Inter in the frame;
/// that role maps onto Geist here), gap 28. Active tabs use their
/// filled/bright glyph; switching is the prototype's "Swap overlay ·
/// Instant". Ported from android/ ui/components/MainTabBar.kt;
/// supersedes the pre-CHINEDU StakTabBar.
struct MainTabBar: View {
	@Binding var selected: MainTab

	var body: some View {
		HStack(spacing: 28) {
			ForEach(MainTab.allCases) { tab in
				Button {
					if tab.built { selected = tab }
				} label: {
					VStack(spacing: 10) {
						Image(tab == selected ? tab.activeIcon : tab.inactiveIcon)
							.resizable()
							.frame(width: 24, height: 24)
						Text(tab.rawValue)
							.font(StakFont.geist(12))
							.foregroundStyle(Color.white)
					}
				}
				.buttonStyle(.plain)
			}
		}
		.frame(maxWidth: .infinity)
		.frame(height: 86)
		.background(Color(argb: 0xFF060C1D).ignoresSafeArea(edges: .bottom))
	}
}
