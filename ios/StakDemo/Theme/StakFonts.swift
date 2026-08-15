import SwiftUI

/// Bundled brand faces — static instances cut from the same Sora/Geist
/// variable fonts the Android app ships, plus Squarish Sans CT for the
/// wordmark. PostScript names are baked into the TTF name tables
/// (Sora-SemiBold, Geist-Medium, SquarishSansCTRegular, …) and listed in
/// Info.plist under UIAppFonts.
enum StakFont {
	enum Weight: String {
		case light = "Light"
		case regular = "Regular"
		case medium = "Medium"
		case semiBold = "SemiBold"
		case bold = "Bold"
	}

	/// Sora — the UI face across the app (headlines, buttons, tab labels).
	static func sora(_ size: CGFloat, _ weight: Weight = .regular) -> Font {
		.custom("Sora-\(weight.rawValue)", size: size)
	}

	/// Geist — the in-app data/UI face (body copy, captions, inputs).
	static func geist(_ size: CGFloat, _ weight: Weight = .regular) -> Font {
		.custom("Geist-\(weight.rawValue)", size: size)
	}

	/// Squarish Sans CT — display face for the STAK wordmark / big numerals.
	static func squarish(_ size: CGFloat, smallCaps: Bool = false) -> Font {
		.custom(smallCaps ? "SquarishSansCTRegularSC" : "SquarishSansCTRegular", size: size)
	}
}
