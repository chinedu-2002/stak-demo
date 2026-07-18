package com.stak.demo.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.ui.components.BackChevron
import com.stak.demo.ui.components.StakPrimaryButton
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors

/**
 * Onboarding · Notifications — Figma node 22:1541.
 *
 * "Stay in the loop" permission-priming screen: three sample notification
 * cards (price alert, daily brief, price target), a gradient CTA to turn
 * notifications on, and a muted "Maybe later" skip link.
 */
@Composable
fun NotificationsScreen(
	onBack: () -> Unit,
	onAllow: () -> Unit,
	onSkip: () -> Unit,
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
				.verticalScroll(rememberScrollState()),
		) {
			Spacer(modifier = Modifier.height(8.dp))
			// Figma 22:1544 — back chevron at x22/y55.
			BackChevron(onClick = onBack, modifier = Modifier.padding(start = 8.dp))
			Spacer(modifier = Modifier.height(8.dp))
			// Figma 22:1545 — heading at y104.
			Text(
				text = "Stay in the loop",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 28.sp),
				color = StakColors.TextPrimary,
				modifier = Modifier.padding(horizontal = 20.dp),
			)
			Spacer(modifier = Modifier.height(9.dp))
			// Figma 22:1546 — subtitle at y148, width 340, line height 1.4.
			Text(
				text = "Price alerts and your daily brief, only when they matter. You're always in control.",
				style = TextStyle(
					fontFamily = Sora,
					fontWeight = FontWeight.Normal,
					fontSize = 14.sp,
					lineHeight = 19.6.sp,
				),
				color = StakColors.Muted,
				modifier = Modifier.padding(start = 20.dp, end = 30.dp),
			)
			Spacer(modifier = Modifier.height(49.dp))
			// Figma 22:1547..1558 — three sample notification cards, 64dp, 12dp apart.
			NotificationPreviewCard(
				title = "AAPL is up 3.2% today",
				time = "now",
				modifier = Modifier.padding(horizontal = 20.dp),
			)
			Spacer(modifier = Modifier.height(12.dp))
			NotificationPreviewCard(
				title = "Your daily brief is ready",
				time = "8:00 AM",
				modifier = Modifier.padding(horizontal = 20.dp),
			)
			Spacer(modifier = Modifier.height(12.dp))
			NotificationPreviewCard(
				title = "NVDA hit your price target",
				time = "2h",
				modifier = Modifier.padding(horizontal = 20.dp),
			)
			Spacer(modifier = Modifier.height(24.dp))
		}
		// Figma 22:1559/22:1560 — gradient CTA pill at y700.
		StakPrimaryButton(
			text = "Turn on notifications",
			onClick = onAllow,
			modifier = Modifier.padding(horizontal = 20.dp),
		)
		Spacer(modifier = Modifier.height(22.dp))
		// Figma 22:1561 — muted skip link at y776.
		Text(
			text = "Maybe later",
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 15.sp),
			color = StakColors.Muted,
			textAlign = TextAlign.Center,
			modifier = Modifier
				.fillMaxWidth()
				.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null,
					onClick = onSkip,
				)
				.padding(vertical = 4.dp),
		)
		Spacer(modifier = Modifier.height(16.dp))
	}
}

/**
 * One sample notification row — Figma 22:1547 pattern: 64dp #172037 card,
 * radius 14, white-6% border, 10dp accent dot, SemiBold 14 title, muted
 * 12sp timestamp on the right.
 */
@Composable
private fun NotificationPreviewCard(
	title: String,
	time: String,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier
			.fillMaxWidth()
			.height(64.dp)
			.background(StakColors.SurfaceAlt, RoundedCornerShape(14.dp))
			.border(1.dp, Color(0x0FFFFFFF), RoundedCornerShape(14.dp)),
	) {
		// Figma 22:1548 — 10dp accent dot at x38/y258 (18/22 within the card).
		Box(
			modifier = Modifier
				.padding(start = 18.dp, top = 22.dp)
				.size(10.dp)
				.background(StakColors.Accent, CircleShape),
		)
		Text(
			text = title,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
			color = StakColors.TextPrimary,
			modifier = Modifier
				.align(Alignment.TopStart)
				.padding(start = 42.dp, top = 15.dp, end = 78.dp),
		)
		Text(
			text = time,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 12.sp),
			color = StakColors.Muted,
			modifier = Modifier
				.align(Alignment.TopEnd)
				.padding(end = 16.dp, top = 16.dp),
		)
	}
}
