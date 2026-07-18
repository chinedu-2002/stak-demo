package com.stak.demo.ui.onboarding

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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.ui.components.StakPrimaryButton
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors

/**
 * Onboarding · Welcome screen.
 *
 * Figma node 23:325 (file tzBCgpuPAFGnipu7NtwH3z).
 * Centered STAK badge pill, "Welcome to STAK" heading, body copy,
 * three outlined feature chips (Discover / Watchlist / Simulate) and a
 * bottom-pinned "Enter STAK" gradient CTA.
 */
@Composable
fun WelcomeScreen(onEnter: () -> Unit) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(StakColors.Bg)
			.systemBarsPadding()
			.padding(horizontal = 20.dp)
	) {
		Column(
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth()
				.verticalScroll(rememberScrollState()),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			// Figma y150 minus the ~44px status area.
			Spacer(modifier = Modifier.height(106.dp))

			// STAK badge pill — solid #2C9DBC 110x42 r21 (node 23:328 / 23:329).
			Box(
				modifier = Modifier
					.width(110.dp)
					.height(42.dp)
					.background(color = StakColors.AccentBlue, shape = RoundedCornerShape(21.dp)),
				contentAlignment = Alignment.Center
			) {
				Text(
					text = "STAK",
					style = TextStyle(
						fontFamily = Sora,
						fontWeight = FontWeight.SemiBold,
						fontSize = 18.sp,
						letterSpacing = 0.36.sp,
						color = StakColors.Bg
					)
				)
			}

			// Pill bottom (y192) to heading top (y336).
			Spacer(modifier = Modifier.height(144.dp))

			Text(
				text = "Welcome to STAK",
				modifier = Modifier.fillMaxWidth(),
				textAlign = TextAlign.Center,
				style = TextStyle(
					fontFamily = Sora,
					fontWeight = FontWeight.SemiBold,
					fontSize = 32.sp,
					color = StakColors.TextPrimary
				)
			)

			Spacer(modifier = Modifier.height(14.dp))

			Text(
				text = "Your first deck is ready. Swipe to discover, save what you like, and learn as you go, all with virtual money.",
				modifier = Modifier.width(320.dp),
				textAlign = TextAlign.Center,
				style = TextStyle(
					fontFamily = Sora,
					fontWeight = FontWeight.Normal,
					fontSize = 15.sp,
					lineHeight = 21.6.sp,
					color = StakColors.Muted
				)
			)

			Spacer(modifier = Modifier.height(31.dp))

			Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
				WelcomeChip(label = "Discover")
				WelcomeChip(label = "Watchlist")
				WelcomeChip(label = "Simulate")
			}

			Spacer(modifier = Modifier.height(24.dp))
		}

		StakPrimaryButton(
			text = "Enter STAK",
			onClick = onEnter,
			modifier = Modifier
				.fillMaxWidth()
				.padding(bottom = 24.dp)
		)
	}
}

/**
 * Outlined feature chip (nodes 23:332..23:337): 32dp tall r16 pill,
 * AccentBlue 12% fill, AccentBlue 40% border, Accent 13sp SemiBold label.
 */
@Composable
private fun WelcomeChip(label: String) {
	Box(
		modifier = Modifier
			.height(32.dp)
			.background(
				color = StakColors.AccentBlue.copy(alpha = 0.12f),
				shape = RoundedCornerShape(16.dp)
			)
			.border(
				width = 1.dp,
				color = StakColors.AccentBlue.copy(alpha = 0.4f),
				shape = RoundedCornerShape(16.dp)
			)
			.padding(horizontal = 16.dp),
		contentAlignment = Alignment.Center
	) {
		Text(
			text = label,
			style = TextStyle(
				fontFamily = Sora,
				fontWeight = FontWeight.SemiBold,
				fontSize = 13.sp,
				color = StakColors.Accent
			)
		)
	}
}
