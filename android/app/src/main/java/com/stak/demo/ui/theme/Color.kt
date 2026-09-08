package com.stak.demo.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Which appearance the app is drawing (user, 2026-09-07: "there should be white
 * background as well"). Set by MainActivity from the Appearance setting before
 * anything composes - it also forces the configuration's uiMode, so
 * `drawable-notnight` glyphs and `isSystemInDarkTheme()` agree with it.
 */
object StakAppearance {
	var light by mutableStateOf(false)
}

/**
 * The light palette is a MAPPING of the authored dark tokens - the Figma file has
 * dark frames only. Ground #0A1020 -> white, the navy surfaces -> light greys,
 * white / grey inks -> navy inks, hairlines keep their alpha over the new ground,
 * accents (teal, green, red, purple, the CTA gradient) stay; the deck cards' own
 * art colours stay because the cards are artwork.
 */
private val LIGHT: Map<Long, Long> = mapOf(
	// grounds and surfaces
	0xFF0A1020L to 0xFFFFFFFFL, 0xFF060C1DL to 0xFFF7F8FBL, 0xFF060B16L to 0xFFF3F5F9L, 0xFF10172AL to 0xFFF3F5F9L,
	0xFF10182BL to 0xFFF3F5F9L, 0xFF0E1424L to 0xFFF3F5F9L, 0xFF0C1526L to 0xFFF3F5F9L, 0xFF0B1430L to 0xFFF0F3F8L,
	0xFF12203EL to 0xFFEAF0F7L, 0xFF172037L to 0xFFEEF1F6L, 0xFF171D2CL to 0xFFEEF1F6L, 0xFF181F30L to 0xFFF0F3F8L,
	0xFF192238L to 0xFFE9EEF5L, 0xFF1A2333L to 0xFFEDF1F6L, 0xFF1B2030L to 0xFFEDF1F6L, 0xFF212D4BL to 0xFFE3EAF4L,
	0xFF242B3DL to 0xFFE4E8F0L, 0xFF272F40L to 0xFFE4E8F0L, 0xFF2A3346L to 0xFFD6DCE6L, 0xFF0F2A38L to 0xFFE3F3F7L,
	0xFF161B27L to 0xFFF0F3F8L,
	// scrims over the ground
	0xE6161B27L to 0xE6F3F5F9L, 0x40242B3DL to 0x40E4E8F0L, 0x403E4958L to 0x40C8D2E0L, 0x8C0C1320L to 0x8CFFFFFFL,
	0x9E02050EL to 0x9EFFFFFFL, 0xB80A1020L to 0xB8FFFFFFL, 0x000A1020L to 0x00FFFFFFL,
	// inks
	0xFFF2F6FCL to 0xFF0A1020L, 0xFFF5F1F1L to 0xFF141A2BL, 0xFFF9F9F9L to 0xFF0A1020L, 0xFFDCE7F7L to 0xFF1F2A3FL,
	0xFFD3D3DDL to 0xFF3A4459L, 0xFFD3D3D3L to 0xFF3A4459L, 0xFFD8CFCFL to 0xFF3A4459L, 0xFFC8D2E0L to 0xFF3D4A63L,
	0xFFC4C4C4L to 0xFF4A5468L, 0xFFAEAEAEL to 0xFF6B7280L, 0xFFACAFB1L to 0xFF5F6B7AL, 0xFF9EADC7L to 0xFF4F607CL,
	0xFF9AA3B5L to 0xFF5A6578L, 0xFF819ABBL to 0xFF5C6B85L, 0xFF5C6B85L to 0xFF8A97ADL, 0xFFD9D9D9L to 0xFF0A1020L,
	// accents that are too pale on white
	0xFFA6E4F7L to 0xFF2C7F98L, 0xFF7FD4E8L to 0xFF2C9DBCL, 0xFF5BD7E4L to 0xFF2AA3B4L, 0xFF2FD08AL to 0xFF1F9D68L,
	0xFFFF5A6AL to 0xFFD7323FL, 0xFFE8B86DL to 0xFFB88A3EL, 0xFFFFE14DL to 0xFFC9A400L,
)

/**
 * Every colour literal in the UI goes through here: the authored dark value, or its
 * light counterpart while [StakAppearance.light]. White-with-alpha inks become navy
 * with the same alpha and navy-with-alpha scrims become white with the same alpha,
 * so hairlines and scrims keep their weight over the new ground.
 */
fun stakColor(argb: Long): Color {
	if (!StakAppearance.light) return Color(argb)
	LIGHT[argb]?.let { return Color(it) }
	val alpha = argb ushr 24
	val rgb = argb and 0xFFFFFFL
	return when {
		alpha < 0xFF && rgb == 0xFFFFFFL -> Color((alpha shl 24) or 0x0A1020L)
		alpha < 0xFF && rgb == 0x0A1020L -> Color((alpha shl 24) or 0xFFFFFFL)
		else -> Color(argb)
	}
}

/**
 * STAK design tokens — exact values from the Figma app design
 * (file tzBCgpuPAFGnipu7NtwH3z, sections "STAK · Components" / app screens).
 */
object StakColors {
	/** Screen background. */
	val Bg: Color get() = stakColor(0xFF0A1020)

	/** Card / tab-bar surface. */
	val Surface: Color get() = stakColor(0xFF10172A)

	/** Raised surface: inputs, social buttons, secondary buttons. */
	val SurfaceAlt: Color get() = stakColor(0xFF172037)

	/** Teal accent: active tab, links, ghost buttons. */
	val Accent: Color get() = stakColor(0xFF39C5CB)

	/** Deep accent blue (borders, secondary emphasis). */
	val AccentBlue: Color get() = stakColor(0xFF2C9DBC)

	/** Muted blue-gray: secondary text, placeholders, inactive tabs. */
	val Muted: Color get() = stakColor(0xFF819ABB)

	/** Navy ink on the light ground. */
	val TextPrimary: Color get() = if (StakAppearance.light) Color(0xFF0A1020) else Color.White
	/** rgba(255,255,255,0.62) body copy on cards. */
	val TextDim: Color get() = stakColor(0x9EFFFFFF)
	/** rgba(255,255,255,0.4) fine print. */
	val TextFaint: Color get() = stakColor(0x66FFFFFF)
	val TextSoft: Color get() = stakColor(0xFFF5F1F1)

	/** Gains green (▲) from the tab screens. */
	val Positive: Color get() = stakColor(0xFF2FD08A)

	/** 1px hairline: rgba(255,255,255,0.08). */
	val Divider: Color get() = stakColor(0x14FFFFFF)
	/** rgba(255,255,255,0.1) borders on social buttons. */
	val BorderSoft: Color get() = stakColor(0x1AFFFFFF)
	/** rgba(255,255,255,0.12) "or" divider strips. */
	val BorderMid: Color get() = stakColor(0x1FFFFFFF)

	/** Primary CTA gradient (#a9dbea → #3c98b4, mid stop 44%). */
	val CtaGradTop: Color get() = stakColor(0xFFA9DBEA)
	val CtaGradBottom: Color get() = stakColor(0xFF3C98B4)
	/** rgba(101,158,173,0.63) border on the landing-style pill button. */
	val CtaBorder: Color get() = stakColor(0xA1659EAD)
	// Authored CTA hairline (1:921 Inspect): a vertical gradient
	// #659EAD@63% -> #16363F@43%, border 0.36 outside.
	val CtaBorderBrush = Brush.verticalGradient(
		0f to Color(0xA1659EAD),
		1f to Color(0x6E16363F),
	)
	/** rgba(44,157,188,0.45) border on secondary pill buttons. */
	val CtaSecondaryBorder: Color get() = stakColor(0x732C9DBC)
	/** rgba(82,170,199,x) glow behind primary CTAs. */
	val CtaGlow: Color get() = stakColor(0xFF52AAC7)

	fun ctaGradient() = Brush.verticalGradient(
		0f to CtaGradTop,
		0.44f to CtaGradBottom,
		1f to CtaGradBottom,
	)
}
