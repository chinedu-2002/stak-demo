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

/// CHINEDU tab bar (Home Main 1:1220 / News listing 1:1352) — 86px
/// #060c1d, five 24px icons with 12px white labels (Inter in the frame;
/// that role maps onto Geist here). The authored bar (86, compact 75)
/// INCLUDES the home-indicator zone — the tab row sits at its authored
/// top inset (18) and the system gesture area overlays the
/// bar's lower band, exactly like the frame; the shell runs the bar to
/// the physical bottom of the screen (no safe-area padding beneath it).
/// Active tabs use their filled/bright glyph; switching is the
/// prototype's "Swap overlay · Instant". Ported from android/
/// ui/components/MainTabBar.kt; supersedes the pre-CHINEDU StakTabBar.
struct MainTabBar: View {
	@Binding var selected: MainTab
	/// True only while the Discover tab is up — its 75-tall bar (1:1788).

	var body: some View {
		let u = figmaUnit
		ZStack(alignment: .top) {
			Color(argb: 0xFF060C1D)
			// Authored gaps: 28 on the 86 bar (1:1221), 30 on the compact 75 bar (1:1788).
			HStack(alignment: .top, spacing: 28 * u) {
				ForEach(MainTab.allCases) { tab in
					Button {
						if tab.built { selected = tab }
					} label: {
						VStack(spacing: 10 * u) {
							Image(tab == selected ? tab.activeIcon : tab.inactiveIcon)
								.resizable()
								.frame(width: 24 * u, height: 24 * u)
							Text(tab.rawValue)
								.font(StakFont.geist(12 * u))
								.foregroundStyle(Color.white)
						}
					}
					.buttonStyle(.plain)
				}
			}
			.padding(.top, 18 * u)
		}
		.frame(maxWidth: .infinity)
		.frame(height: 86 * u)
	}
}
