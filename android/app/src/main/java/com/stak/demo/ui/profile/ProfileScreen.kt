package com.stak.demo.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.ui.onboarding.AuthBackCircle
import com.stak.demo.ui.theme.Geist
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors
import androidx.compose.foundation.layout.requiredSize

private val CardBg = Color(0xFF10182B)
private val Muted = Color(0xFF819ABB)
private val Body = Color(0xFFC8D2E0)
private val Bright = Color(0xFFF2F6FC)
private val Green = Color(0xFF2FD08A)
private val ChipBg = Color(0xFF1A2333)
private val ChipBorder = Color(0xFF2C9DBC)
private val ChipInk = Color(0xFF7FD4E8)

/**
 * 05 · Profile — "Profile · hub" (CHINEDU 171:995), reached from the
 * Home nav circle (prototype: Push Right 300ms). Avatar block, the
 * YOUR TASTE chips, the paper stats card, the settings list and the
 * Log out hairline button.
 */
@Composable
fun ProfileScreen(onBack: () -> Unit, onLogOut: () -> Unit = {}) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(modifier = Modifier.fillMaxSize().background(StakColors.Bg)) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.background(StakColors.Bg)
				.statusBarsPadding()
				.height((56 * u).dp),
		) {
			AuthBackCircle(
				onClick = onBack,
				modifier = Modifier.align(Alignment.CenterStart).padding(start = (20 * u).dp),
			)
			Text(
				text = "Profile",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (16 * u).sp, lineHeight = (20 * u).sp),
				color = Color.White,
				modifier = Modifier.align(Alignment.Center),
			)
		}
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy((16 * u).dp),
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth()
				.verticalScroll(rememberScrollState())
				.padding(horizontal = (20 * u).dp)
				.padding(top = (16 * u).dp, bottom = (40 * u).dp),
		) {
			Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy((8 * u).dp)) {
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier.size((64 * u).dp).background(Color(0xFF242B3D), CircleShape),
				) {
					// The picked photo when one exists; else the live initial of
					// the display name ("H" was hardcoded - audit 2026-08-25).
					val photo = com.stak.demo.ui.UserProfile.photoUri
					if (photo != null) {
						coil.compose.AsyncImage(
							model = photo,
							contentDescription = null,
							contentScale = androidx.compose.ui.layout.ContentScale.Crop,
							modifier = Modifier.matchParentSize().clip(CircleShape),
						)
					} else {
						Text(
							text = com.stak.demo.ui.UserProfile.greetingName.take(1).uppercase(),
							style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (22 * u).sp, lineHeight = (28 * u).sp),
							color = Color(0xFF9EADC7),
						)
					}
				}
				Text(
					text = com.stak.demo.ui.UserProfile.greetingName,
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (20 * u).sp, lineHeight = (25 * u).sp),
					color = Color.White,
				)
				Text(
					text = "Paper investor · joined July 2026",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
					color = Muted,
				)
			}
			// YOUR TASTE card.
			Column(
				verticalArrangement = Arrangement.spacedBy((10 * u).dp),
				modifier = Modifier
					.fillMaxWidth()
					.clip(RoundedCornerShape((16 * u).dp))
					.background(CardBg)
					.padding((14 * u).dp),
			) {
				Text(
					text = "YOUR TASTE",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp, lineHeight = (14 * u).sp),
					color = Muted,
				)
				Row(horizontalArrangement = Arrangement.spacedBy((8 * u).dp)) {
				// Authored chip widths (171:1004): 97 / 94 / 125 — pinned so the
				// row fills the 332 content width and the labels never wrap.
				listOf("Tech Curious" to 97, "High Growth" to 94, "Consumer Brands" to 125).forEach { (label, w) ->
					Box(
						contentAlignment = Alignment.Center,
						modifier = Modifier
							.requiredSize((w * u).dp, (28 * u).dp)
							.clip(RoundedCornerShape((14 * u).dp))
							.background(ChipBg)
							.border((1 * u).dp, ChipBorder, RoundedCornerShape((14 * u).dp)),
					) {
						Text(
							text = label,
							style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
							color = ChipInk,
							maxLines = 1,
							softWrap = false,
						)
					}
				}
			}
			Text(
					text = "Your taste graph sharpens with every swipe.",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
					color = Body,
				)
			}
			// Paper stats card.
			Column(
				verticalArrangement = Arrangement.spacedBy((10 * u).dp),
				modifier = Modifier
					.fillMaxWidth()
					.clip(RoundedCornerShape((16 * u).dp))
					.background(CardBg)
					.padding((14 * u).dp),
			) {
				Row(modifier = Modifier.fillMaxWidth().height((40 * u).dp).padding(horizontal = 0.dp)) {
					ProfileStat("$10,240", "Portfolio")
					Spacer(modifier = Modifier.weight(1f))
					ProfileStat("$8,800.00", "Cash")
					Spacer(modifier = Modifier.weight(1f))
					ProfileStat("12", "Picks")
				}
				Text(
					text = "▲ +$240.00 all time on $10,000 paper",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
					color = Green,
				)
			}
			// Settings card.
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.clip(RoundedCornerShape((16 * u).dp))
					.background(CardBg)
					.padding(vertical = (4 * u).dp),
			) {
				listOf("Notifications", "Appearance", "Linked accounts", "Help & support").forEach { label ->
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.fillMaxWidth()
							.height((48 * u).dp)
							.clickable(
								interactionSource = remember { MutableInteractionSource() },
								indication = null,
							) { /* Settings screens land in a later phase. */ }
							.padding(horizontal = (14 * u).dp),
					) {
						Text(
							text = label,
							style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (13 * u).sp, lineHeight = (17 * u).sp),
							color = Color.White,
						)
						Spacer(modifier = Modifier.weight(1f))
						// Chevron line height 18 as authored (171:1027 - exact-design audit 2026-09-04).
						Text(
							text = "›",
							style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (14 * u).sp, lineHeight = (18 * u).sp),
							color = Muted,
						)
					}
				}
			}
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.fillMaxWidth()
					.height((52 * u).dp)
					// Authored glow (171:1037): the teal drop-shadow stack cast
					// downward, same as the auth CTAs (user, 2026-09-04: exact frame).
					.drawBehind {
						val r = (6 * u).dp.toPx()
						val fw = drawContext.canvas.nativeCanvas
						val paint = android.graphics.Paint().apply { isAntiAlias = true }
						for ((dy, blur, a) in listOf(
							Triple(2.89f, 3.25f, 0.10f),
							Triple(12.29f, 6.14f, 0.09f),
							Triple(28.18f, 8.31f, 0.05f),
							Triple(49.86f, 9.76f, 0.01f),
						)) {
							paint.color = android.graphics.Color.argb((a * 255).toInt(), 82, 170, 199)
							paint.maskFilter = android.graphics.BlurMaskFilter((blur * u).dp.toPx(), android.graphics.BlurMaskFilter.Blur.NORMAL)
							fw.drawRoundRect(0f, (dy * u).dp.toPx(), size.width, (dy * u).dp.toPx() + size.height, r, r, paint)
						}
					}
					.border((0.36 * u).dp, Color(0x54343B4F), RoundedCornerShape((6 * u).dp))
					.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
					) { onLogOut() },
			) {
				Text(
					text = "Log out",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = (14 * u).sp, lineHeight = (18 * u).sp),
					color = Muted,
				)
			}
		}
	}
}

@Composable
private fun ProfileStat(value: String, label: String) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy((4 * u).dp)) {
		Text(
			text = value,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (16 * u).sp, lineHeight = (20 * u).sp),
			color = Bright,
		)
		Text(
			text = label,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (11 * u).sp, lineHeight = (14 * u).sp),
			color = Muted,
		)
	}
}
