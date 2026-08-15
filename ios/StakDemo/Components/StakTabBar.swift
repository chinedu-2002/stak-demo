import SwiftUI

enum StakTab: String, CaseIterable, Identifiable {
	case home = "Home"
	case discover = "Discover"
	case watchlist = "Watchlist"
	case simulate = "Simulate"
	case profile = "Profile"

	var id: String { rawValue }
}

/// Bottom tab bar — Figma "Tab bar" (node 36:379): #10172a, 1px top hairline,
/// 56pt of content, 10pt labels (active #39c5cb SemiBold, inactive #819abb).
/// Icons are drawn from the component's exact vector geometry, same as the
/// Android Canvas port — the design deviates from the system tab bar
/// (custom face, colors, geometry), so this is deliberately custom chrome.
struct StakTabBar: View {
	@Binding var selected: StakTab

	var body: some View {
		VStack(spacing: 0) {
			Rectangle().fill(StakColors.divider).frame(height: 1)
			HStack(spacing: 0) {
				ForEach(StakTab.allCases) { tab in
					let active = tab == selected
					let tint = active ? StakColors.accent : StakColors.muted
					Button {
						selected = tab
					} label: {
						VStack(spacing: 2) {
							TabIcon(tab: tab)
								.fill(tint)
								.frame(width: 22, height: 18)
								.padding(.top, 10)
							Text(tab.rawValue)
								.font(StakFont.sora(10, active ? .semiBold : .regular))
								.foregroundStyle(tint)
						}
						.frame(maxWidth: .infinity)
						.frame(height: 56, alignment: .top)
					}
					.buttonStyle(.plain)
				}
			}
		}
		.background(StakColors.surface)
	}
}

/// Icon geometry lifted from Figma node 36:379 (a 22x18 box, centered).
struct TabIcon: Shape {
	let tab: StakTab

	func path(in rect: CGRect) -> Path {
		let u = rect.height / 18 // 1 Figma px
		var p = Path()
		func rr(_ x: CGFloat, _ y: CGFloat, _ w: CGFloat, _ h: CGFloat, _ r: CGFloat) {
			p.addRoundedRect(
				in: CGRect(x: x * u, y: y * u, width: w * u, height: h * u),
				cornerSize: CGSize(width: r * u, height: r * u)
			)
		}
		switch tab {
		case .home:
			// House: 19x17, centered in 22.
			let ox = (rect.width - 19 * u) / 2
			p.move(to: CGPoint(x: ox + 0.5 * u, y: 8 * u))
			p.addLine(to: CGPoint(x: ox + 9.5 * u, y: 1 * u))
			p.addLine(to: CGPoint(x: ox + 18.5 * u, y: 8 * u))
			p.addLine(to: CGPoint(x: ox + 18.5 * u, y: 17 * u))
			p.addLine(to: CGPoint(x: ox + 0.5 * u, y: 17 * u))
			p.closeSubpath()
		case .discover:
			// 2x2 grid of 8px rounded squares.
			let ox = (rect.width - 16 * u) / 2 / u
			rr(ox, 1, 8, 8, 2); rr(ox + 8, 1, 8, 8, 2)
			rr(ox, 9, 8, 8, 2); rr(ox + 8, 9, 8, 8, 2)
		case .watchlist:
			// Bookmark: 13x18.
			let ox = (rect.width - 13 * u) / 2
			p.move(to: CGPoint(x: ox + 0.5 * u, y: 0.5 * u))
			p.addLine(to: CGPoint(x: ox + 12.5 * u, y: 0.5 * u))
			p.addLine(to: CGPoint(x: ox + 12.5 * u, y: 17.5 * u))
			p.addLine(to: CGPoint(x: ox + 6.5 * u, y: 12.5 * u))
			p.addLine(to: CGPoint(x: ox + 0.5 * u, y: 17.5 * u))
			p.closeSubpath()
		case .simulate:
			// Four bars: widths 4, heights 6/12/9/16 sharing the baseline.
			let ox = (rect.width - 22 * u) / 2 / u
			rr(ox, 12, 4, 6, 1)
			rr(ox + 6, 6, 4, 12, 1)
			rr(ox + 12, 9, 4, 9, 1)
			rr(ox + 18, 2, 4, 16, 1)
		case .profile:
			// Head circle (r5) + rounded-top body (18x6 with 8 top radius).
			let cx = rect.width / 2
			p.addEllipse(in: CGRect(x: cx - 5 * u, y: 0, width: 10 * u, height: 10 * u))
			var body = Path(
				roundedRect: CGRect(x: cx - 9 * u, y: 12 * u, width: 18 * u, height: 12 * u),
				cornerRadius: 8 * u
			)
			body = body.intersection(Path(CGRect(x: cx - 9 * u, y: 12 * u, width: 18 * u, height: 6 * u)))
			p.addPath(body)
		}
		return p
	}
}
