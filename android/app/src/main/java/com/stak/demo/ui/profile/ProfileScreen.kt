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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.ui.onboarding.AuthBackCircle
import com.stak.demo.ui.theme.Geist
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors

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
fun ProfileScreen(onBack: () -> Unit) {
	Column(modifier = Modifier.fillMaxSize().background(StakColors.Bg)) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.background(StakColors.Bg)
				.statusBarsPadding()
				.height(56.dp),
		) {
			AuthBackCircle(
				onClick = onBack,
				modifier = Modifier.align(Alignment.CenterStart).padding(start = 20.dp),
			)
			Text(
				text = "Profile",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
				color = Color.White,
				modifier = Modifier.align(Alignment.Center),
			)
		}
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(16.dp),
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth()
				.verticalScroll(rememberScrollState())
				.padding(horizontal = 20.dp)
				.padding(top = 16.dp, bottom = 40.dp),
		) {
			Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier.size(64.dp).background(Color(0xFF242B3D), CircleShape),
				) {
					Text(
						text = "H",
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 22.sp),
						color = Color(0xFF9EADC7),
					)
				}
				Text(
					text = "Hamza",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 20.sp),
					color = Color.White,
				)
				Text(
					text = "Paper investor · joined July 2026",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 12.sp),
					color = Muted,
				)
			}
			// YOUR TASTE card.
			Column(
				verticalArrangement = Arrangement.spacedBy(10.dp),
				modifier = Modifier
					.fillMaxWidth()
					.clip(RoundedCornerShape(16.dp))
					.background(CardBg)
					.padding(14.dp),
			) {
				Text(
					text = "YOUR TASTE",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 11.sp),
					color = Muted,
				)
				Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
					listOf("Tech Curious", "High Growth", "Consumer Brands").forEach { label ->
						Box(
							modifier = Modifier
								.clip(RoundedCornerShape(14.dp))
								.background(ChipBg)
								.border(1.dp, ChipBorder, RoundedCornerShape(14.dp))
								.padding(horizontal = 12.dp, vertical = 6.dp),
						) {
							Text(
								text = label,
								style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp),
								color = ChipInk,
							)
						}
					}
				}
				Text(
					text = "Your taste graph sharpens with every swipe.",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 12.sp),
					color = Body,
				)
			}
			// Paper stats card.
			Column(
				verticalArrangement = Arrangement.spacedBy(10.dp),
				modifier = Modifier
					.fillMaxWidth()
					.clip(RoundedCornerShape(16.dp))
					.background(CardBg)
					.padding(14.dp),
			) {
				Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 0.dp)) {
					ProfileStat("$10,240", "Portfolio")
					Spacer(modifier = Modifier.weight(1f))
					ProfileStat("$8,800.00", "Cash")
					Spacer(modifier = Modifier.weight(1f))
					ProfileStat("12", "Picks")
				}
				Text(
					text = "▲ +$240.00 all time on $10,000 paper",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp),
					color = Green,
				)
			}
			// Settings card.
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.clip(RoundedCornerShape(16.dp))
					.background(CardBg)
					.padding(vertical = 4.dp),
			) {
				listOf("Notifications", "Appearance", "Linked accounts", "Help & support").forEach { label ->
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.fillMaxWidth()
							.height(48.dp)
							.clickable(
								interactionSource = remember { MutableInteractionSource() },
								indication = null,
							) { /* Settings screens land in a later phase. */ }
							.padding(horizontal = 14.dp),
					) {
						Text(
							text = label,
							style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 13.sp),
							color = Color.White,
						)
						Spacer(modifier = Modifier.weight(1f))
						Text(
							text = "›",
							style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 14.sp),
							color = Muted,
						)
					}
				}
			}
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.fillMaxWidth()
					.height(52.dp)
					.border(0.36.dp, Color(0x54343B4F), RoundedCornerShape(6.dp))
					.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
					) { /* Sign-out wiring comes with Firebase. */ },
			) {
				Text(
					text = "Log out",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 14.sp),
					color = Muted,
				)
			}
		}
	}
}

@Composable
private fun ProfileStat(value: String, label: String) {
	Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
		Text(
			text = value,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
			color = Bright,
		)
		Text(
			text = label,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 11.sp),
			color = Muted,
		)
	}
}
