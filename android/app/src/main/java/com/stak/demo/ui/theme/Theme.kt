package com.stak.demo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val StakColorScheme = darkColorScheme(
	primary = StakColors.Accent,
	onPrimary = StakColors.Bg,
	secondary = StakColors.AccentBlue,
	onSecondary = StakColors.TextPrimary,
	background = StakColors.Bg,
	onBackground = StakColors.TextPrimary,
	surface = StakColors.Surface,
	onSurface = StakColors.TextPrimary,
	surfaceVariant = StakColors.SurfaceAlt,
	onSurfaceVariant = StakColors.Muted,
	outline = StakColors.BorderSoft,
	error = androidx.compose.ui.graphics.Color(0xFFFF6B6B),
)

/**
 * Android rounds every glyph advance to a whole pixel while Figma (and
 * iOS) position glyphs fractionally: measured on the 2026-08-30 audit as
 * a constant ~0.6 device px lost per glyph across Sora 26 / Geist 13 /
 * Geist 10 (runs 1.5-3% tighter than the frame with identical glyphs;
 * the bundled fonts shape to Figma's widths in HarfBuzz). A constant
 * 0.24sp of tracking, inherited by every Text through LocalTextStyle,
 * gives the frame's run widths back. Android-only — do not mirror to iOS.
 */
val ADVANCE_ROUNDING = 0.24.sp

private val StakTypography = Typography(
	// Screen H1 ("Create your account") — Sora SemiBold 28.
	headlineMedium = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 28.sp, letterSpacing = ADVANCE_ROUNDING),
	// Section titles — Sora SemiBold 20.
	titleLarge = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, letterSpacing = ADVANCE_ROUNDING),
	titleMedium = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, letterSpacing = ADVANCE_ROUNDING),
	// Body / subtitles — Sora Regular 14 (muted by callers).
	bodyLarge = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 15.sp, letterSpacing = ADVANCE_ROUNDING),
	bodyMedium = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 14.sp, letterSpacing = ADVANCE_ROUNDING),
	bodySmall = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 13.sp, letterSpacing = ADVANCE_ROUNDING),
	labelLarge = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, letterSpacing = ADVANCE_ROUNDING),
	labelMedium = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, letterSpacing = ADVANCE_ROUNDING),
	labelSmall = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 11.sp, letterSpacing = ADVANCE_ROUNDING),
)

@Composable
fun StakTheme(content: @Composable () -> Unit) {
	// The STAK app is dark-only by design; ignore the system light theme.
	MaterialTheme(
		colorScheme = StakColorScheme,
		typography = StakTypography,
		content = content,
	)
}
