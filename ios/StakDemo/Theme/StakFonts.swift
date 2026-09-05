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

	/// Inter — the authored tab-bar label face (static Regular instance cut
	/// from the same variable font the Android app ships).
	static func inter(_ size: CGFloat) -> Font {
		.custom("Inter-Regular", size: size)
	}

	/// Squarish Sans CT — display face for the STAK wordmark / big numerals.
	static func squarish(_ size: CGFloat, smallCaps: Bool = false) -> Font {
		.custom(smallCaps ? "SquarishSansCTRegularSC" : "SquarishSansCTRegular", size: size)
	}
}

/// The bundled faces' natural (font-designed) line height as a multiple of the
/// point size, measured on the shipped static instances (2026-09-04): the
/// hhea ascender + |descender| + lineGap over unitsPerEm, which is the pitch
/// SwiftUI lays a single line out at. Weight cuts of a face share the metric.
enum StakFace {
	case sora, geist, inter

	var naturalLineHeightFactor: CGFloat {
		switch self {
		case .sora: return 1.26
		case .geist: return 1.30
		case .inter: return 1.21
		}
	}
}

extension View {
	/// Lays a text run out at its authored Figma line height.
	///
	/// SwiftUI's `.lineSpacing(_:)` is ADDITIVE over the face's natural line
	/// height - it does not set the pitch. The old `.lineSpacing((L - S) * u)`
	/// form therefore rendered `natural(S) + (L - S)` per line: a Geist 12 run
	/// authored at 16 came out at 15.6 + 4 = 19.6, so every multi-line
	/// paragraph ran ~3-4 pt per line too loose (proven on Home, 2026-09-04).
	/// The correct extra is `L - natural(face, S)`, clamped at zero.
	///
	/// Residual: additive spacing cannot go negative, so an authored pitch
	/// BELOW the natural height keeps the natural height - Geist 11 at 14
	/// renders 14.3, Geist 12 at 15 renders 15.6, Sora 20 at 25 renders 25.2,
	/// Sora 26 at 31 renders 32.76. Those sites are flagged, not hacked.
	///
	/// - Parameters:
	///   - lineHeight: the authored line height, already scaled (`L * u`).
	///   - size: the point size the preceding `.font(...)` set, already scaled (`S * u`).
	///   - face: the bundled face that `.font(StakFont.<face>(...))` used.
	func stakLineHeight(_ lineHeight: CGFloat, size: CGFloat, face: StakFace) -> some View {
		lineSpacing(max(0, lineHeight - size * face.naturalLineHeightFactor))
	}
}
