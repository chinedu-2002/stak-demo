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
	// The STAK app is dark-only by design; ignore the system light theme.
	MaterialTheme(
		colorScheme = StakColorScheme,
		typography = StakTypography,
		content = content,
	)
}
