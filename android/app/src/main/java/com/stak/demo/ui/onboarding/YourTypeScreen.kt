package com.stak.demo.ui.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.ui.components.StakPrimaryButton
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors

/**
 * Onboarding · Your type — Figma node 23:307.
 *
 * The investor-type result screen: a teal-ringed check badge, "YOUR INVESTOR
 * TYPE" eyebrow, "The Growth Explorer" headline, a short description, three
 * accent trait chips (Tech / High growth / Speculative), a footnote about the
 * deck being tuned, and a gradient Continue CTA pinned to the bottom.
 *
 * The design has no back chevron, so [onBack] is accepted for the standard
 * onboarding signature but not rendered.
 */
@Composable
fun YourTypeScreen(
	onBack: () -> Unit,
	onContinue: () -> Unit,
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(StakColors.Bg)
			.systemBarsPadding(),
	) {
		Column(
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth()
				.verticalScroll(rememberScrollState())
				.padding(horizontal = 20.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Spacer(modifier = Modifier.height(52.dp))
			// Figma 23:310..312 — 100dp check badge at y96.
			TypeCheckBadge()
			Spacer(modifier = Modifier.height(30.dp))
			// Figma 23:313 — eyebrow label at y226, tracking 0.96.
			Text(
				text = "YOUR INVESTOR TYPE",
				style = TextStyle(
					fontFamily = Sora,
					fontWeight = FontWeight.SemiBold,
					fontSize = 12.sp,
					letterSpacing = 0.96.sp,
				),
				color = StakColors.Muted,
				textAlign = TextAlign.Center,
				modifier = Modifier.fillMaxWidth(),
			)
			Spacer(modifier = Modifier.height(9.dp))
			// Figma 23:314 — headline at y250.
			Text(
				text = "The Growth Explorer",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 30.sp),
				color = StakColors.TextPrimary,
				textAlign = TextAlign.Center,
				modifier = Modifier.fillMaxWidth(),
			)
			Spacer(modifier = Modifier.height(10.dp))
			// Figma 23:315 — description at y298, width 330, line height 1.4.
			Text(
				text = "You lean into momentum and big ideas, with room to learn as you go.",
				style = TextStyle(
					fontFamily = Sora,
					fontWeight = FontWeight.Normal,
					fontSize = 14.sp,
					lineHeight = 19.6.sp,
				),
				color = StakColors.Muted,
				textAlign = TextAlign.Center,
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 10.dp),
			)
			Spacer(modifier = Modifier.height(34.dp))
			// Figma 23:316..321 — trait chips at y372, 32dp pills 10dp apart.
			Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
				TraitChip(text = "Tech")
				TraitChip(text = "High growth")
				TraitChip(text = "Speculative")
			}
			Spacer(modifier = Modifier.height(32.dp))
			// Figma 23:322 — footnote at y436, width 310, line height 1.4.
			Text(
				text = "Your deck is tuned to this. You can change it anytime in Profile.",
				style = TextStyle(
					fontFamily = Sora,
					fontWeight = FontWeight.Normal,
					fontSize = 13.sp,
					lineHeight = 18.2.sp,
				),
				color = StakColors.Muted,
				textAlign = TextAlign.Center,
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 20.dp),
			)
			Spacer(modifier = Modifier.height(24.dp))
		}
		// Figma 23:323/23:324 — gradient CTA pill at y760.
		StakPrimaryButton(
			text = "Continue",
			onClick = onContinue,
			modifier = Modifier.padding(horizontal = 20.dp),
		)
		Spacer(modifier = Modifier.height(16.dp))
	}
}

/**
 * The result badge — Figma 23:310 (100dp ring, #39C5CB stroke at 35%),
 * 23:311 (76dp #2C9DBC disc), and 23:312 (dark 22x16 check, 3px round
 * stroke, drawn slightly up-left of the disc center per the design).
 */
@Composable
private fun TypeCheckBadge(modifier: Modifier = Modifier) {
	Canvas(modifier = modifier.size(100.dp)) {
		val ringStroke = 2.dp.toPx()
		drawCircle(
			color = StakColors.Accent.copy(alpha = 0.35f),
			radius = size.minDimension / 2f - ringStroke / 2f,
			style = Stroke(width = ringStroke),
		)
		drawCircle(
			color = StakColors.AccentBlue,
			radius = 38.dp.toPx(),
		)
		// Check path: Figma vector at (27, 26) within the 100dp badge box.
		val originX = 27.dp.toPx()
		val originY = 26.dp.toPx()
		val check = Path().apply {
			moveTo(originX, originY + 8.dp.toPx())
			lineTo(originX + 8.dp.toPx(), originY + 16.dp.toPx())
			lineTo(originX + 22.dp.toPx(), originY)
		}
		drawPath(
			path = check,
			color = StakColors.Bg,
			style = Stroke(
				width = 3.dp.toPx(),
				cap = StrokeCap.Round,
				join = StrokeJoin.Round,
			),
		)
	}
}

/**
 * One trait pill — Figma 23:317 pattern: 32dp tall, radius 16, #2C9DBC at
 * 12% fill with a 40% border, SemiBold 13 accent label, 16dp side padding.
 */
@Composable
private fun TraitChip(
	text: String,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier
			.height(32.dp)
			.background(StakColors.AccentBlue.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
			.border(1.dp, StakColors.AccentBlue.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
			.padding(horizontal = 16.dp),
		contentAlignment = Alignment.Center,
	) {
		Text(
			text = text,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 13.sp),
			color = StakColors.Accent,
		)
	}
}
