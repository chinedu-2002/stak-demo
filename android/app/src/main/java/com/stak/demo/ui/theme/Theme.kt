package com.stak.demo.ui.theme

import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Figma's line box: a text block is exactly lines x lineHeight, the extra
 * leading split evenly above and below every line. Compose's default
 * LineHeightStyle (Proportional + Trim.Both) trims the first line's top and
 * the last line's bottom leading, so every multi-line block came out
 * (lineHeight - natural height) shorter than its frame box and long pages
 * crept upward - measured on StakTest vs the 2x export of 1:1495
 * (2026-09-04): gist bullet pitch 48 vs the authored 50, paragraph gap 3
 * short. Every TextStyle literal that pins a lineHeight carries this.
 */
val FIGMA_LINE_BOX = LineHeightStyle(LineHeightStyle.Alignment.Center, LineHeightStyle.Trim.None)

private val StakColorScheme = darkColorScheme(
	primary = androidx.compose.ui.graphics.Color(0xFF39C5CB),
	onPrimary = androidx.compose.ui.graphics.Color(0xFF0A1020),
	secondary = androidx.compose.ui.graphics.Color(0xFF2C9DBC),
	onSecondary = androidx.compose.ui.graphics.Color.White,
	background = androidx.compose.ui.graphics.Color(0xFF0A1020),
	onBackground = androidx.compose.ui.graphics.Color.White,
	surface = androidx.compose.ui.graphics.Color(0xFF10172A),
	onSurface = androidx.compose.ui.graphics.Color.White,
	surfaceVariant = androidx.compose.ui.graphics.Color(0xFF172037),
	onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFF819ABB),
	outline = androidx.compose.ui.graphics.Color(0x1AFFFFFF),
	error = androidx.compose.ui.graphics.Color(0xFFFF6B6B),
)

/** The light appearance (user, 2026-09-07): the same tokens through the light mapping. */
private val StakLightColorScheme = lightColorScheme(
	primary = androidx.compose.ui.graphics.Color(0xFF2C9DBC),
	onPrimary = androidx.compose.ui.graphics.Color.White,
	secondary = androidx.compose.ui.graphics.Color(0xFF2C9DBC),
	onSecondary = androidx.compose.ui.graphics.Color.White,
	background = androidx.compose.ui.graphics.Color.White,
	onBackground = androidx.compose.ui.graphics.Color(0xFF0A1020),
	surface = androidx.compose.ui.graphics.Color(0xFFF3F5F9),
	onSurface = androidx.compose.ui.graphics.Color(0xFF0A1020),
	surfaceVariant = androidx.compose.ui.graphics.Color(0xFFEEF1F6),
	onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFF5C6B85),
	outline = androidx.compose.ui.graphics.Color(0x1A0A1020),
	error = androidx.compose.ui.graphics.Color(0xFFD7323F),
)


private val StakTypography = Typography(
	// Screen H1 ("Create your account") — Sora SemiBold 28.
	headlineMedium = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 28.sp),
	// Section titles — Sora SemiBold 20.
	titleLarge = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 20.sp),
	titleMedium = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
	// Body / subtitles — Sora Regular 14 (muted by callers).
	bodyLarge = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 15.sp),
	bodyMedium = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 14.sp),
	bodySmall = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 13.sp),
	labelLarge = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 15.sp),
	labelMedium = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 13.sp),
	labelSmall = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 11.sp),
)

@Composable
fun StakTheme(content: @Composable () -> Unit) {
	// Dark is the authored design; Light (or Match system on a light phone) is
	// its mapping - MainActivity decides from the Appearance setting.
	MaterialTheme(
		colorScheme = if (StakAppearance.light) StakLightColorScheme else StakColorScheme,
		typography = StakTypography,
		content = content,
	)
}
