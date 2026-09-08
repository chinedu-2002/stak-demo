import SwiftUI
import UIKit

/// The light palette is a MAPPING of the authored dark tokens - the Figma file has
/// dark frames only (user, 2026-09-07: "there should be white background as well").
/// Ground #0A1020 -> white, the navy surfaces -> light greys, white / grey inks ->
/// navy inks, hairlines keep their alpha over the new ground, accents stay; the deck
/// cards' own art colours stay because the cards are artwork. Mirrors android's
/// stakColor() table in ui/theme/Color.kt.
private let lightPalette: [UInt32: UInt32] = [
	// grounds and surfaces
	0xFF0A1020: 0xFFFFFFFF, 0xFF060C1D: 0xFFF7F8FB, 0xFF060B16: 0xFFF3F5F9, 0xFF10172A: 0xFFF3F5F9,
	0xFF10182B: 0xFFF3F5F9, 0xFF0E1424: 0xFFF3F5F9, 0xFF0C1526: 0xFFF3F5F9, 0xFF0B1430: 0xFFF0F3F8,
	0xFF12203E: 0xFFEAF0F7, 0xFF172037: 0xFFEEF1F6, 0xFF171D2C: 0xFFEEF1F6, 0xFF181F30: 0xFFF0F3F8,
	0xFF192238: 0xFFE9EEF5, 0xFF1A2333: 0xFFEDF1F6, 0xFF1B2030: 0xFFEDF1F6, 0xFF212D4B: 0xFFE3EAF4,
	0xFF242B3D: 0xFFE4E8F0, 0xFF272F40: 0xFFE4E8F0, 0xFF2A3346: 0xFFD6DCE6, 0xFF0F2A38: 0xFFE3F3F7,
	0xFF161B27: 0xFFF0F3F8,
	// scrims over the ground
	0xE6161B27: 0xE6F3F5F9, 0x40242B3D: 0x40E4E8F0, 0x403E4958: 0x40C8D2E0, 0x8C0C1320: 0x8CFFFFFF,
	0x9E02050E: 0x9EFFFFFF, 0xB80A1020: 0xB8FFFFFF, 0x000A1020: 0x00FFFFFF,
	// inks
	0xFFF2F6FC: 0xFF0A1020, 0xFFF5F1F1: 0xFF141A2B, 0xFFF9F9F9: 0xFF0A1020, 0xFFDCE7F7: 0xFF1F2A3F,
	0xFFD3D3DD: 0xFF3A4459, 0xFFD3D3D3: 0xFF3A4459, 0xFFD8CFCF: 0xFF3A4459, 0xFFC8D2E0: 0xFF3D4A63,
	0xFFC4C4C4: 0xFF4A5468, 0xFFAEAEAE: 0xFF6B7280, 0xFFACAFB1: 0xFF5F6B7A, 0xFF9EADC7: 0xFF4F607C,
	0xFF9AA3B5: 0xFF5A6578, 0xFF819ABB: 0xFF5C6B85, 0xFF5C6B85: 0xFF8A97AD, 0xFFD9D9D9: 0xFF0A1020,
	0xFFFFFFFF: 0xFF0A1020,
	// accents that are too pale on white
	0xFFA6E4F7: 0xFF2C7F98, 0xFF7FD4E8: 0xFF2C9DBC, 0xFF5BD7E4: 0xFF2AA3B4, 0xFF2FD08A: 0xFF1F9D68,
	0xFFFF5A6A: 0xFFD7323F, 0xFFE8B86D: 0xFFB88A3E, 0xFFFFE14D: 0xFFC9A400,
]

/// The light counterpart of an authored 0xAARRGGBB: the table, else white-with-alpha
/// inks become navy with the same alpha and navy-with-alpha scrims become white.
func lightArgb(_ argb: UInt32) -> UInt32 {
	if let mapped = lightPalette[argb] { return mapped }
	let alpha = argb >> 24, rgb = argb & 0xFFFFFF
	if alpha < 0xFF && rgb == 0xFFFFFF { return (alpha << 24) | 0x0A1020 }
	if alpha < 0xFF && rgb == 0x0A1020 { return (alpha << 24) | 0xFFFFFF }
	return argb
}

private func uiColor(_ argb: UInt32) -> UIColor {
	UIColor(
		red: CGFloat((argb >> 16) & 0xFF) / 255,
		green: CGFloat((argb >> 8) & 0xFF) / 255,
		blue: CGFloat(argb & 0xFF) / 255,
		alpha: CGFloat((argb >> 24) & 0xFF) / 255
	)
}

extension Color {
	/// 0xAARRGGBB, matching the Android Compose literals verbatim - resolved per
	/// appearance: the authored value in dark, its mapping in light. The app's
	/// colour scheme follows the Appearance setting (StakDemoApp).
	init(argb: UInt32) {
		let dark = uiColor(argb), light = uiColor(lightArgb(argb))
		self.init(uiColor: UIColor { traits in traits.userInterfaceStyle == .light ? light : dark })
	}

	/// The authored value in BOTH appearances - the splash and the launch moment.
	init(fixedArgb argb: UInt32) {
		self.init(
			.sRGB,
			red: Double((argb >> 16) & 0xFF) / 255,
			green: Double((argb >> 8) & 0xFF) / 255,
			blue: Double(argb & 0xFF) / 255,
			opacity: Double((argb >> 24) & 0xFF) / 255
		)
	}
}

/// STAK design tokens — exact values from the Figma app design.
/// Mirrors android/ StakColors (ui/theme/Color.kt). Every token resolves per
/// appearance through Color(argb:) - dark is the authored design, light its mapping.
enum StakColors {
	/// Screen background.
	static let bg = Color(argb: 0xFF0A1020)

	/// Card / tab-bar surface.
	static let surface = Color(argb: 0xFF10172A)

	/// Raised surface: inputs, social buttons, secondary buttons.
	static let surfaceAlt = Color(argb: 0xFF172037)

	/// Teal accent: active tab, links, ghost buttons.
	static let accent = Color(argb: 0xFF39C5CB)

	/// Deep accent blue (borders, secondary emphasis, the 08 toggles).
	static let accentBlue = Color(argb: 0xFF2C9DBC)

	/// Muted blue-gray: secondary text, placeholders, inactive tabs.
	static let muted = Color(argb: 0xFF819ABB)

	/// White ink in dark, navy ink on the light ground.
	static let textPrimary = Color(argb: 0xFFFFFFFF)
	/// rgba(255,255,255,0.62) body copy on cards.
	static let textDim = Color(argb: 0x9EFFFFFF)
	/// rgba(255,255,255,0.4) fine print.
	static let textFaint = Color(argb: 0x66FFFFFF)
	static let textSoft = Color(argb: 0xFFF5F1F1)

	/// Gains green (▲) from the tab screens.
	static let positive = Color(argb: 0xFF2FD08A)

	/// 1px hairline: rgba(255,255,255,0.08).
	static let divider = Color(argb: 0x14FFFFFF)
	/// rgba(255,255,255,0.1) borders on social buttons.
	static let borderSoft = Color(argb: 0x1AFFFFFF)
	/// rgba(255,255,255,0.12) "or" divider strips.
	static let borderMid = Color(argb: 0x1FFFFFFF)

	/// Primary CTA gradient (#a9dbea → #3c98b4, mid stop 44%).
	static let ctaGradTop = Color(argb: 0xFFA9DBEA)
	static let ctaGradBottom = Color(argb: 0xFF3C98B4)
	/// rgba(101,158,173,0.63) border on the landing-style pill button.
	static let ctaBorder = Color(argb: 0xA1659EAD)
	/// Authored CTA hairline (1:921 Inspect): vertical gradient
	/// #659EAD@63% -> #16363F@43%, border 0.36 outside.
	static let ctaBorderGradient = LinearGradient(
		stops: [
			.init(color: Color(argb: 0xA1659EAD), location: 0),
			.init(color: Color(argb: 0x6E16363F), location: 1)
		],
		startPoint: .top, endPoint: .bottom
	)
	/// rgba(44,157,188,0.45) border on secondary pill buttons.
	static let ctaSecondaryBorder = Color(argb: 0x732C9DBC)
	/// rgba(82,170,199,x) glow behind primary CTAs.
	static let ctaGlow = Color(argb: 0xFF52AAC7)
}

/// Shared palette of the Figma "Auth ·" screens — mirrors android/ AuthKit.kt.
enum Auth {
	static let navCircle = Color(argb: 0xFF192238)
	static let subtitleGray = Color(argb: 0xFFACAFB1)
	static let inputBg = Color(argb: 0xFF181F30)
	static let dividerLine = Color(argb: 0xFF2A3346)
	static let faintText = Color(argb: 0xFF5C6B85)
	static let linkTeal = Color(argb: 0xFF69B3CA)
	static let darkOnWhite = Color(argb: 0xFF0E162B)
}
