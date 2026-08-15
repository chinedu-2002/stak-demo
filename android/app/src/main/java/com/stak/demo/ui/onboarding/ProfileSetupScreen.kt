package com.stak.demo.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.ui.theme.Geist
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors

private const val NAME_MAX = 20

/**
 * Onboarding · 09 Profile setup — Figma node 1:793 (CHINEDU file,
 * "STEP · LAST ONE").
 *
 * Final onboarding step: 96dp #242b3d avatar circle with the teal ring
 * and the display-name initial, "Add a photo" link, the DISPLAY NAME
 * input card with its live "n / 20" counter, and the gradient
 * "Proceed to home" CTA.
 */
@Composable
fun ProfileSetupScreen(onBack: () -> Unit, onProceed: () -> Unit) {
	var name by rememberSaveable { mutableStateOf("") }

	Column(modifier = Modifier.fillMaxSize().background(StakColors.Bg).systemBarsPadding()) {
		Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(top = 10.dp, bottom = 4.dp)) {
			AuthBackCircle(onClick = onBack)
		}
		OnboardingKicker(text = "STEP · LAST ONE")

		Column(
			verticalArrangement = Arrangement.spacedBy(18.dp),
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth()
				.padding(horizontal = 24.dp)
				.padding(top = 14.dp),
		) {
			Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
				Text(
					text = "Make it yours",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 26.sp),
					color = StakColors.TextPrimary,
				)
				Text(
					text = "Pick a name and photo. This is how you’ll show up on leaderboards.",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 12.sp),
					color = Auth.SubtitleGray,
					modifier = Modifier.width(276.dp),
				)
			}

			// Avatar — 96dp #242b3d circle, 2dp teal ring, Sora 36 initial.
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.spacedBy(10.dp),
				modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
			) {
				Box(
					modifier = Modifier
						.size(96.dp)
						.background(Color(0xFF242B3D), CircleShape)
						.border(2.dp, Auth.LinkTeal, CircleShape),
					contentAlignment = Alignment.Center,
				) {
					Text(
						text = name.firstOrNull()?.uppercase() ?: "",
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 36.sp),
						color = Color(0xFF9EADC7),
					)
				}
				Text(
					text = "Add a photo",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp),
					color = Auth.LinkTeal,
					modifier = Modifier.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
						onClick = {},
					),
				)
			}

			Text(
				text = "DISPLAY NAME",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 10.sp, letterSpacing = 1.2.sp),
				color = Auth.FaintText,
			)

			// Name input — #181f30 r14 card with the live "n / 20" counter.
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.fillMaxWidth()
					.background(Auth.InputBg, RoundedCornerShape(14.dp))
					.padding(16.dp),
			) {
				BasicTextField(
					value = name,
					onValueChange = { name = it.take(NAME_MAX) },
					textStyle = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 14.sp, color = StakColors.TextPrimary),
					singleLine = true,
					cursorBrush = SolidColor(StakColors.Accent),
					modifier = Modifier.weight(1f),
				)
				Text(
					text = "${name.length} / $NAME_MAX",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 11.sp),
					color = Auth.FaintText,
				)
			}

			Text(
				text = "You can change this anytime in Profile.",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 11.sp),
				color = Auth.FaintText,
			)
		}

		Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 26.dp)) {
			AuthCta(text = "Proceed to home", onClick = onProceed)
		}
	}
}
