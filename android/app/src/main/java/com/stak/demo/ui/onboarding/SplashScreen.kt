package com.stak.demo.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.R
import com.stak.demo.ui.theme.Geist
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors
import kotlinx.coroutines.delay

/**
 * Entry splash — Figma node 1554:11348 ("iPhone 13 & 14 - 5"):
 * the tilted 2K glass STAK ball bleeding off the top-right edge
 * (394px square rotated 48.43°, center at x 380.1 / y 217.1 on the
 * 390-wide frame), the swoosh logo mark, "Welcome to STAK" in Sora
 * SemiBold ~30, and a two-line Geist Light 16 subtitle at 70% white.
 *
 * Auto-advances after a short hold; tapping anywhere skips ahead.
 */
@Composable
fun SplashScreen(onContinue: () -> Unit) {
	LaunchedEffect(Unit) {
		delay(2000)
		onContinue()
	}
	BoxWithConstraints(
		modifier = Modifier
			.fillMaxSize()
			.background(StakColors.Bg)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onContinue,
			),
	) {
		// Glass ball: center sits 9.9dp left of the right edge, 217.1dp down.
		Image(
			painter = painterResource(R.drawable.splash_glass_ball),
			contentDescription = null,
			modifier = Modifier
				.size(394.dp)
				.offset(x = maxWidth - 9.9.dp - 197.dp, y = 20.1.dp)
				.rotate(48.43f),
		)

		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(10.dp),
			modifier = Modifier.align(Alignment.Center),
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.spacedBy(37.dp),
			) {
				Image(
					painter = painterResource(R.drawable.ic_stak_logo_mark),
					contentDescription = "STAK",
					modifier = Modifier.size(49.43.dp),
				)
				Text(
					text = "Welcome to STAK",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 29.87.sp),
					color = StakColors.TextPrimary,
				)
			}
			Text(
				text = "The stock market finally speaks\nyour language",
				style = TextStyle(
					fontFamily = Geist,
					fontWeight = FontWeight.Light,
					fontSize = 16.sp,
					textAlign = TextAlign.Center,
				),
				color = StakColors.TextPrimary.copy(alpha = 0.7f),
			)
		}
	}
}
