package com.stak.demo.ui.simulate

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.R
import com.stak.demo.ui.onboarding.AuthBackCircle
import com.stak.demo.ui.theme.Geist
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors
import com.stak.demo.ui.theme.ADVANCE_ROUNDING

/**
 * 07 · Simulate — "Pick detail · paper" (CHINEDU 1:4631). The NVDA
 * position page: picked line, the +$24.00 gain hero, chart with range
 * pills, the This-week / vs-the-market duo, Price then/now, the WHY?
 * insight and the dark Sell CTA (raises the sell flow from here too).
 */
@Composable
fun PickDetailScreen(onBack: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	var showSell by rememberSaveable { mutableStateOf(false) }

	Box(modifier = Modifier.fillMaxSize().background(StakColors.Bg)) {
		Column(modifier = Modifier.fillMaxSize()) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.fillMaxWidth()
					.background(StakColors.Bg)
					.statusBarsPadding()
					.padding(horizontal = (18 * u).dp, vertical = (8 * u).dp),
			) {
				AuthBackCircle(onClick = onBack)
				Spacer(modifier = Modifier.weight(1f))
				Text(
					text = "NVDA",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (16 * u).sp),
					color = Color.White,
				)
				Spacer(modifier = Modifier.weight(1f))
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier.size((40 * u).dp).background(Sim.CardBg, CircleShape),
				) {
					Image(painterResource(R.drawable.ic_news_share), null, modifier = Modifier.size((17 * u).dp))
				}
			}
			Column(
				verticalArrangement = Arrangement.spacedBy((14 * u).dp),
				modifier = Modifier
					.weight(1f)
					.fillMaxWidth()
					.verticalScroll(rememberScrollState())
					.padding(horizontal = (20 * u).dp)
					.padding(top = (10 * u).dp, bottom = (20 * u).dp),
			) {
				Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((10 * u).dp)) {
					Box(contentAlignment = Alignment.Center, modifier = Modifier.size((38 * u).dp).background(Sim.ChipBg, CircleShape)) {
						Text("N", style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp), color = Sim.BadgeInk)
					}
					Text(
						"Picked May 8 at $98.50",
						style = TextStyle(fontFamily = Geist, fontSize = (12 * u).sp),
						color = Sim.Muted,
					)
				}
				Column(verticalArrangement = Arrangement.spacedBy((4 * u).dp)) {
					Row(verticalAlignment = Alignment.Bottom) {
						Text(
							"+$24",
							style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (44 * u).sp, letterSpacing = (-0.44 * u).sp),
							color = Color.White,
						)
						Text(
							".00",
							style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (18 * u).sp),
							color = Sim.Muted,
							// Same authored offsets as the Simulate hero: 8 gap, 8 up.
							modifier = Modifier.padding(start = (8 * u).dp, bottom = (8 * u).dp),
						)
					}
					Text(
						"That is up 24.0% on a $100 paper stake",
						style = TextStyle(fontFamily = Geist, fontSize = (12 * u).sp),
						color = Sim.Muted,
					)
				}
				Image(
					painter = painterResource(R.drawable.sim_chart_line),
					contentDescription = null,
					contentScale = ContentScale.Fit,
					modifier = Modifier.align(Alignment.CenterHorizontally).size((345 * u).dp, (76 * u).dp),
				)
				Row(
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.spacedBy((37 * u).dp),
					modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = (12 * u).dp),
				) {
					listOf("1D", "1W", "1M", "3M", "YTD", "1Y").forEach { label ->
						if (label == "3M") {
							Box(
								contentAlignment = Alignment.Center,
								modifier = Modifier
									.size((39 * u).dp, (22.5 * u).dp)
									.clip(RoundedCornerShape((11.25 * u).dp))
									.background(Color(0x292C9DBC))
									.border((0.75 * u).dp, Color(0x662C9DBC), RoundedCornerShape((11.25 * u).dp)),
							) {
								Text(label, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp), color = Sim.Teal)
							}
						} else {
							Text(label, style = TextStyle(fontFamily = Geist, fontSize = (12 * u).sp), color = Sim.Muted)
						}
					}
				}
				Row(horizontalArrangement = Arrangement.spacedBy((10 * u).dp), modifier = Modifier.padding(top = (6 * u).dp)) {
					StatBox("This week", "+$3.80", Sim.Green, Modifier.weight(1f))
					StatBox("vs the market", "+20.8% ahead", Sim.Green, Modifier.weight(1f))
				}
				Row(horizontalArrangement = Arrangement.spacedBy((10 * u).dp)) {
					StatBox("Price then", "$98.50", Sim.Bright, Modifier.weight(1f))
					StatBox("Price now", "$122.10", Sim.Bright, Modifier.weight(1f))
				}
				// WHY / insight card — teal-tinted like the deck tips.
				Column(
					verticalArrangement = Arrangement.spacedBy((8 * u).dp),
					modifier = Modifier
						.fillMaxWidth()
						.clip(RoundedCornerShape((16 * u).dp))
						.background(Sim.TealTint)
						.padding(horizontal = (16 * u).dp, vertical = (15 * u).dp),
				) {
					Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((7 * u).dp)) {
						Image(painterResource(R.drawable.ic_gist_sparkle), null, modifier = Modifier.size((16 * u).dp))
						Text(
							"INSIGHT",
							style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, letterSpacing = (0.9 * u + ADVANCE_ROUNDING.value).sp),
							color = Sim.Faint,
						)
					}
					Text(
						"Your stake tracks the move live. If NVDA gives back gains, the dollars follow it down.",
						style = TextStyle(fontFamily = Geist, fontSize = (12 * u).sp, lineHeight = (20 * u).sp),
						color = Sim.Body,
					)
				}
				Spacer(modifier = Modifier.height((4 * u).dp))
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier
						.fillMaxWidth()
						.height((52 * u).dp)
						.background(Sim.DarkCta, RoundedCornerShape((6 * u).dp))
						.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
						) { showSell = true },
				) {
					Text(
						"Sell",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (14 * u).sp),
						color = Color.White,
					)
				}
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier
						.fillMaxWidth()
						.height((52 * u).dp)
						.border((0.36 * u).dp, Color(0x54343B4F), RoundedCornerShape((6 * u).dp))
						.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
							onClick = onBack,
						),
				) {
					Text("Back", style = TextStyle(fontFamily = Sora, fontSize = (14 * u).sp), color = Sim.Muted)
				}
			}
		}
		if (showSell) {
			// The sell flow is hosted by the portfolio screen's sheets; from
			// here selling routes back through the portfolio page.
			SellFlowHost(onClose = { showSell = false; onBack() })
		}
	}
}

@Composable
private fun StatBox(label: String, value: String, valueColor: Color, modifier: Modifier) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((4 * u).dp),
		modifier = modifier
			.clip(RoundedCornerShape((12 * u).dp))
			.background(Sim.CardBg)
			.padding(horizontal = (14 * u).dp, vertical = (12 * u).dp),
	) {
		Text(label, style = TextStyle(fontFamily = Geist, fontSize = (11 * u).sp), color = Sim.Faint)
		Text(
			value,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp),
			color = valueColor,
		)
	}
}
