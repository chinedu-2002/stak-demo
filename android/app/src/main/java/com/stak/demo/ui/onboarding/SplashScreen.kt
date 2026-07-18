package com.stak.demo.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
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
 * Entry splash — Figma node 1554:11348 ("iPhone 13 & 14 - 5").
 *
 * Renders the frame's exact 390x844 composition — the tilted 2K glass
 * ball (394 square rotated 48.43°, center 380.1/217.1), swoosh logo,
 * "Welcome to STAK" and the Geist Light subtitle — as one canvas
 * scaled uniformly to the device width, so the balance between ball,
 * logo and type matches the design on every screen instead of
 * drifting apart on taller phones.
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
		val scale = maxWidth / 390.dp

		// The design canvas: fixed 390x844, scaled from its top-center.
		Box(
			modifier = Modifier
				.align(Alignment.Center)
				.requiredSize(390.dp, 844.dp)
				.graphicsLayer(
					scaleX = scale,
					scaleY = scale,
					transformOrigin = TransformOrigin(0.5f, 0.5f),
				),
		) {
			// Glass ball — 394 square rotated 48.43°, center (380.1, 217.1).
			Image(
				painter = painterResource(R.drawable.splash_glass_ball),
				contentDescription = null,
				modifier = Modifier
					.size(394.dp)
					.offset(x = 183.1.dp, y = 20.1.dp)
					.rotate(48.43f),
			)

			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.spacedBy(10.dp),
				modifier = Modifier.align(Alignment.Center).offset(x = 0.3.dp, y = 0.71.dp),
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
}
