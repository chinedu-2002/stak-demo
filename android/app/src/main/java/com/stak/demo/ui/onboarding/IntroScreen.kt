package com.stak.demo.ui.onboarding

import com.stak.demo.ui.theme.FIGMA_LINE_BOX
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.R
import com.stak.demo.ui.theme.Geist
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors

/**
 * Onboarding · 01 Welcome — Figma node 1554:8475.
 *
 * Value-prop intro after the splash: headline + subcopy, the blue box
 * spilling glass brand coins (the whole Figma group flattened into one
 * bundled render, glows included), and the sharp gradient "Get started"
 * CTA with the "Takes about a minute" caption.
 */
@Composable
fun IntroScreen(onGetStarted: () -> Unit) {
	val u = figmaUnit()
	Artboard(modifier = Modifier.background(StakColors.Bg)) {
		Column(
			verticalArrangement = Arrangement.spacedBy((16 * u).dp),
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth()
				.padding(horizontal = (24 * u).dp)
				.padding(top = (24 * u).dp),
		) {
			Column(verticalArrangement = Arrangement.spacedBy((12 * u).dp)) {
				Text(
					text = "Find stocks you actually understand.",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (26 * u).sp, lineHeight = (31 * u).sp, lineHeightStyle = FIGMA_LINE_BOX),
					color = StakColors.TextPrimary,
				)
				Text(
					text = "STAK turns brands you already know into simple, clear stock ideas, so you can invest with confidence.",
					// RENDER-measured (2x ref): the authored break is after
					// "simple," (frame line1 ink 922, line2 991). At the authored 14 the
					// build pulls "clear" up - the 342u (authored column) width pin forces the
					// authored wrap (the earlier comment had the break backwards).
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (14 * u).sp, lineHeight = (21 * u).sp, lineHeightStyle = FIGMA_LINE_BOX),
					color = Auth.SubtitleGray,
					modifier = Modifier.width((342 * u).dp),
				)
			}
			// Hero — flattened Figma group (render bounds 342x488 at 1x),
			// scaled to the artboard unit so proportions hold on wide devices.
			Box(
				// The hero art render sits at authored y235 (template-matched
				// against 1:179). 33.15 = re-measured on StakTest vs the 2x export
				// after FIGMA_LINE_BOX gave the title/subtitle their full boxes
				// (the old 38.25 left the art 5.1 low, 2026-09-05).
				modifier = Modifier.weight(1f).fillMaxWidth(),
				contentAlignment = Alignment.TopCenter,
			) {
				Image(
					painter = painterResource(R.drawable.intro_hero_box),
					contentDescription = null,
					contentScale = ContentScale.Fit,
					modifier = Modifier.padding(top = (33.15 * u).dp).size((342 * u).dp, (488 * u).dp),
				)
			}
		}
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy((10 * u).dp),
			modifier = Modifier.fillMaxWidth().padding(top = (8 * u).dp, bottom = (26 * u).dp),
		) {
			AuthCta(text = "Get started", onClick = onGetStarted)
			Text(
				text = "Takes about a minute",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (11 * u).sp),
				color = Auth.FaintText,
			)
		}
	}
}
