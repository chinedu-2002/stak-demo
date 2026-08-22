package com.stak.demo.ui.discover

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.horizontalScroll
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

private val Card = Color(0xFF181F30)
private val Bright = Color(0xFFF2F6FC)
private val Muted = Color(0xFF819ABB)
private val Green = Color(0xFF2FD08A)
private val Teal = Color(0xFF69B3CA)

/**
 * Discover · Stock Detail (CHINEDU 1:2382 folded, 92:969 save success)
 * — the AAPL page reached from the deck's Learn more: price hero, the
 * performance chart with range pills, Risk fit, Numbers that matter,
 * Analyst view / Compare and learn (collapsed), News signal, TIP and
 * the Save / Practice buy CTAs. Saving raises the Saved-to-My-STAK
 * sheet and flips the CTA area to the saved state.
 */
@Composable
fun StockDetailScreen(onBack: () -> Unit, fromMyStak: Boolean = false) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	var saved by rememberSaveable { mutableStateOf(fromMyStak) }
	var showSuccess by rememberSaveable { mutableStateOf(false) }
	var showBuy by rememberSaveable { mutableStateOf(false) }

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
					text = "AAPL",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (16 * u).sp),
					color = Color.White,
				)
				Spacer(modifier = Modifier.weight(1f))
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier.size((40 * u).dp).background(Card, CircleShape),
				) {
					Image(painterResource(R.drawable.ic_news_share), null, modifier = Modifier.size((17 * u).dp))
				}
			}
			Column(modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())) {
				Column(
					verticalArrangement = Arrangement.spacedBy((4 * u).dp),
					modifier = Modifier.fillMaxWidth().padding(horizontal = (20 * u).dp).padding(top = (10 * u).dp, bottom = (6 * u).dp),
				) {
					Text("AAPL · Apple Inc", style = TextStyle(fontFamily = Geist, fontSize = (11 * u).sp), color = Muted)
					Text(
						"$229.35",
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (26 * u).sp),
						color = Bright,
					)
					Text(
						"▲ 1.2% today",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp),
						color = Green,
					)
				}
				Image(
					painter = painterResource(R.drawable.sd_chart_line),
					contentDescription = null,
					contentScale = ContentScale.Fit,
					modifier = Modifier.align(Alignment.CenterHorizontally).size((345 * u).dp, (76 * u).dp),
				)
				Spacer(modifier = Modifier.height((40 * u).dp))
				Row(
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.spacedBy((37 * u).dp),
					modifier = Modifier.align(Alignment.CenterHorizontally),
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
								Text(
									label,
									style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp),
									color = Teal,
								)
							}
						} else {
							Text(label, style = TextStyle(fontFamily = Geist, fontSize = (12 * u).sp), color = Muted)
						}
					}
				}
				Column(
					verticalArrangement = Arrangement.spacedBy((14 * u).dp),
					modifier = Modifier.fillMaxWidth().padding(horizontal = (20 * u).dp, vertical = (12 * u).dp),
				) {
					if (fromMyStak) {
						SinceYouSavedCard()
					}
					RiskFitCard()
					NumbersCard()
					AnalystCard()
					NewsSignalCard()
					CompareCard()
					Row(
						horizontalArrangement = Arrangement.spacedBy((8 * u).dp),
						modifier = Modifier
							.fillMaxWidth()
							.clip(RoundedCornerShape((12 * u).dp))
							.background(Card)
							.padding(horizontal = (12 * u).dp, vertical = (10 * u).dp),
					) {
						Text(
							"TIP",
							style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp),
							color = Color(0xFF5BD7E4),
						)
						Text(
							"Steady giants move slower. Stable stocks often do.",
							style = TextStyle(fontFamily = Geist, fontSize = (11 * u).sp),
							color = Muted,
							modifier = Modifier.width((260 * u).dp),
						)
					}
				}
				Column(
					verticalArrangement = Arrangement.spacedBy((10 * u).dp),
					modifier = Modifier.fillMaxWidth().padding(horizontal = (20 * u).dp).padding(top = (4 * u).dp, bottom = (16 * u).dp),
				) {
					if (fromMyStak) {
						DetailCta("Practice buy") { showBuy = true }
						DetailSecondary("Unsave") { onBack() }
					} else if (saved) {
						Box(
							contentAlignment = Alignment.Center,
							modifier = Modifier
								.fillMaxWidth()
								.height((52 * u).dp)
								.border((0.36 * u).dp, Color(0xA1659EAD), RoundedCornerShape((6 * u).dp)),
						) {
							Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((6 * u).dp)) {
								Image(painterResource(R.drawable.ic_saved_bookmark), null, modifier = Modifier.size((12 * u).dp))
								Text(
									"Saved to My STAK",
									style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (14 * u).sp),
									color = Color.White,
								)
							}
						}
						DetailSecondary("Practice buy") { showBuy = true }
					} else {
						DetailCta("Save") { showSuccess = true }
						DetailSecondary("Practice buy") { showBuy = true }
					}
				}
			}
		}
		if (showSuccess) {
			DetailSavedSheet(onDone = { showSuccess = false; saved = true })
		}
		if (showBuy) {
			DetailBuyHost(onClose = { showBuy = false })
		}
	}
}

@Composable
private fun RiskFitCard() {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((12 * u).dp),
		modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape((16 * u).dp)).background(Card)
			.padding(horizontal = (16 * u).dp, vertical = (14 * u).dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
			Text(
				"Risk fit",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp),
				color = Bright,
			)
			Spacer(modifier = Modifier.weight(1f))
			Box(
				modifier = Modifier
					.clip(RoundedCornerShape((999 * u).dp))
					.background(Color(0x1F5DA8BF))
					.padding(horizontal = (10 * u).dp, vertical = (4 * u).dp),
			) {
				Text(
					"Matches you",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp),
					color = Color(0xFFA6E4F7),
				)
			}
		}
		Box(modifier = Modifier.fillMaxWidth().height((8 * u).dp).clip(RoundedCornerShape((4 * u).dp)).background(Color(0xFF10182B))) {
			Image(
				painterResource(R.drawable.ic_sd_marker), null,
				modifier = Modifier.offset(x = (88 * u).dp, y = (-3 * u).dp).size((14 * u).dp),
			)
		}
		Row(modifier = Modifier.fillMaxWidth()) {
			Text("Low", style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp), color = Muted)
			Spacer(modifier = Modifier.weight(1f))
			Text("High", style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp), color = Muted)
		}
		Text(
			"Low volatility. Fits the steady side of your profile.",
			style = TextStyle(fontFamily = Geist, fontSize = (11 * u).sp),
			color = Muted,
		)
	}
}

@Composable
private fun NumbersCard() {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((12 * u).dp),
		modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape((16 * u).dp)).background(Card)
			.padding(horizontal = (16 * u).dp, vertical = (14 * u).dp),
	) {
		Text(
			"Numbers that matter",
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp),
			color = Bright,
		)
		Row(horizontalArrangement = Arrangement.spacedBy((8 * u).dp), modifier = Modifier.fillMaxWidth()) {
			StatCell("P/E ratio", "31.2", "In line", Muted, Modifier.weight(1f))
			StatCell("Revenue growth", "6.1%", "Slower", Muted, Modifier.weight(1f))
			StatCell("Profit margin", "24.3%", "Excellent", Green, Modifier.weight(1f), border = true)
		}
		Text(
			"Tap a stat for sector and peer benchmarks",
			style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp),
			color = Muted,
		)
	}
}

@Composable
private fun StatCell(label: String, value: String, verdict: String, verdictColor: Color, modifier: Modifier, border: Boolean = false) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((4 * u).dp),
		modifier = modifier
			.clip(RoundedCornerShape((12 * u).dp))
			.background(Card)
			.then(if (border) Modifier.border((1 * u).dp, Color(0xFF212D4B), RoundedCornerShape((12 * u).dp)) else Modifier)
			.padding((10 * u).dp),
	) {
		Text(label, style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp), color = Muted)
		Text(value, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (17 * u).sp), color = Bright)
		Text(verdict, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp), color = verdictColor)
	}
}

@Composable
private fun CollapsedCard(title: String, sub: String, subColor: Color) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((12 * u).dp),
		modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape((16 * u).dp)).background(Card)
			.padding(horizontal = (16 * u).dp, vertical = (14 * u).dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
			Text(
				title,
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp),
				color = Bright,
			)
			Spacer(modifier = Modifier.weight(1f))
			Image(painterResource(R.drawable.ic_sd_caret), null, modifier = Modifier.size((20 * u).dp))
		}
		Text(
			sub,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp),
			color = subColor,
		)
	}
}

@Composable
private fun NewsSignalCard() {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((12 * u).dp),
		modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape((16 * u).dp)).background(Card)
			.padding(horizontal = (16 * u).dp, vertical = (14 * u).dp),
	) {
		Text(
			"News signal",
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp),
			color = Bright,
		)
		Text(
			"▲ +0.8% at yesterday’s close",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp),
			color = Green,
		)
		Text(
			"Foldable iPhone reports point to a premium fall lineup.",
			style = TextStyle(fontFamily = Geist, fontSize = (11 * u).sp),
			color = Muted,
		)
		Text("Q3 earnings land July 30.", style = TextStyle(fontFamily = Geist, fontSize = (11 * u).sp), color = Muted)
		Row(
			horizontalArrangement = Arrangement.spacedBy((12 * u).dp),
			modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
		) {
			repeat(2) { i ->
				Column(
					verticalArrangement = Arrangement.spacedBy((8 * u).dp),
					modifier = Modifier
						.width((205 * u).dp)
						.clip(RoundedCornerShape((12 * u).dp))
						.background(Card)
						.padding((12 * u).dp),
				) {
					Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
						Text(
							if (i == 0) "Yahoo · 13h ago" else "CNN · 1h ago",
							style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp),
							color = Muted,
						)
						Spacer(modifier = Modifier.weight(1f))
						Box(
							modifier = Modifier
								.clip(RoundedCornerShape((999 * u).dp))
								.background(Color(0x14FFFFFF))
								.padding(horizontal = (8 * u).dp, vertical = (3 * u).dp),
						) {
							Text(
								"Neutral",
								style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp),
								color = Muted,
							)
						}
					}
					Text(
						"The rally leaves Apple about 4 percent shy of the market-cap crown",
						style = TextStyle(fontFamily = Geist, fontSize = (12 * u).sp),
						color = Bright,
						modifier = Modifier.width((173 * u).dp),
					)
				}
			}
		}
	}
}

@Composable
private fun DetailCta(text: String, onClick: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.fillMaxWidth()
			.height((52 * u).dp)
			.background(
				androidx.compose.ui.graphics.Brush.verticalGradient(
					0.0889f to Color(0xFFA6E4F7),
					0.3919f to Color(0xFF5DA8BF),
					0.7255f to Color(0xFF3C98B4),
					1f to Color(0xFF3C98B4),
				),
				RoundedCornerShape((6 * u).dp),
			)
			.border((0.36 * u).dp, Color(0xA1659EAD), RoundedCornerShape((6 * u).dp))
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			),
	) {
		Text(text, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (14 * u).sp), color = Color.White)
	}
}

@Composable
private fun DetailSecondary(text: String, onClick: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.fillMaxWidth()
			.height((52 * u).dp)
			.border((0.36 * u).dp, Color(0x54343B4F), RoundedCornerShape((6 * u).dp))
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			),
	) {
		Text(text, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = (13 * u).sp), color = Muted)
	}
}

/** Saved-to-My-STAK sheet over the detail (92:969) — Apple row variant. */
@Composable
private fun DetailSavedSheet(onDone: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Box(modifier = Modifier.fillMaxSize()) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				// Authored scrim rgba(12,19,32,0.55) (106:1037).
				.background(Color(0x8C0C1320))
				.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null,
					onClick = onDone,
				),
		)
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy((14 * u).dp),
			modifier = Modifier
				.align(Alignment.BottomCenter)
				.fillMaxWidth()
				.clip(RoundedCornerShape(topStart = (24 * u).dp, topEnd = (24 * u).dp))
				.background(Card)
				.padding(horizontal = (20 * u).dp)
				.padding(top = (10 * u).dp)
				.padding(bottom = (30 * u).dp),
		) {
			Box(
				modifier = Modifier
					.padding(bottom = (4 * u).dp)
					.size((40 * u).dp, (4 * u).dp)
					.background(Color(0xFF2A3346), RoundedCornerShape((2 * u).dp)),
			)
			Image(painterResource(R.drawable.ic_sheet_check), null, modifier = Modifier.size((47 * u).dp))
			Text(
				"Saved to My STAK",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (18 * u).sp),
				color = Color.White,
			)
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy((11 * u).dp),
				modifier = Modifier
					.fillMaxWidth()
					.clip(RoundedCornerShape((6 * u).dp))
					.background(Color(0x1A69B3CA))
					.padding(horizontal = (14 * u).dp, vertical = (12 * u).dp),
			) {
				Box(contentAlignment = Alignment.Center, modifier = Modifier.size((38 * u).dp).background(Color(0xFF242B3D), CircleShape)) {
					Text("A", style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp), color = Color(0xFF9EADC7))
				}
				Column(verticalArrangement = Arrangement.spacedBy((2 * u).dp), modifier = Modifier.weight(1f)) {
					Text("Apple", style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (13 * u).sp), color = Color.White)
					Text("$229.35 today", style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp), color = Muted)
				}
				Text("▲ 1.2%", style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp), color = Green)
			}
			Text(
				"Watching from today · no money committed",
				style = TextStyle(fontFamily = Geist, fontSize = (12 * u).sp, lineHeight = (18 * u).sp),
				color = Color(0xFFC8D2E0),
				modifier = Modifier.fillMaxWidth(),
			)
			Column(verticalArrangement = Arrangement.spacedBy((16 * u).dp), modifier = Modifier.fillMaxWidth()) {
				DetailCta("View in My STAK", onClick = onDone)
				DetailSecondary("Keep exploring", onClick = onDone)
			}
		}
	}
}

/** The practice-buy ticket reused from the deck (public host wrapper). */
@Composable
private fun DetailBuyHost(onClose: () -> Unit) {
	DiscoverBuyFlow(onClose = onClose, spec = AAPL_BUY)
}

/** Kicker label — Geist Medium 10, tracking 0.8, muted. */
@Composable
private fun Kicker(text: String) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Text(
		text,
		style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, letterSpacing = (0.8 * u).sp),
		color = Muted,
	)
}

/** Analyst view (collapsed 1:2454 / open 1:2651) — caret toggles. */
@Composable
private fun AnalystCard() {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	var open by rememberSaveable { mutableStateOf(false) }
	Column(
		verticalArrangement = Arrangement.spacedBy((12 * u).dp),
		modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape((16 * u).dp)).background(Card)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
			) { open = !open }
			.padding(horizontal = (16 * u).dp, vertical = (14 * u).dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
			Text(
				"Analyst view",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp),
				color = Bright,
			)
			Spacer(modifier = Modifier.weight(1f))
			if (!open) {
				Image(painterResource(R.drawable.ic_sd_caret), null, modifier = Modifier.size((20 * u).dp))
			}
		}
		if (!open) {
			Text(
				"↑ 6.7% upside",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp),
				color = Green,
			)
		} else {
			Kicker("PRICE TARGET RANGE")
			Box(modifier = Modifier.fillMaxWidth().height((8 * u).dp).clip(RoundedCornerShape((4 * u).dp)).background(Color(0xFF10182B))) {
				Box(modifier = Modifier.width((180 * u).dp).height((8 * u).dp).background(Color(0x8C5DA8BF), RoundedCornerShape((4 * u).dp)))
				Image(painterResource(R.drawable.ic_sd_marker), null, modifier = Modifier.offset(x = (173 * u).dp, y = (-3 * u).dp).size((14 * u).dp))
			}
			Row(modifier = Modifier.fillMaxWidth()) {
				Column(verticalArrangement = Arrangement.spacedBy((1 * u).dp)) {
					Text("Low", style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp), color = Muted)
					Text("$180", style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp), color = Bright)
				}
				Spacer(modifier = Modifier.weight(1f))
				Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy((1 * u).dp)) {
					Text("Avg", style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp), color = Muted)
					Text("$248", style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp), color = Bright)
				}
				Spacer(modifier = Modifier.weight(1f))
				Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy((1 * u).dp)) {
					Text("High", style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp), color = Muted)
					Text("$300", style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp), color = Bright)
				}
			}
			Text(
				"↑ 6.7% upside",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp),
				color = Green,
			)
			Kicker("WALL ST. CONSENSUS · 42 ANALYSTS")
			Box(modifier = Modifier.fillMaxWidth().height((8 * u).dp).clip(RoundedCornerShape((4 * u).dp)).background(Color(0xFF10182B))) {
				Box(modifier = Modifier.width((212 * u).dp).height((8 * u).dp).background(Green, RoundedCornerShape((4 * u).dp)))
			}
			Row(modifier = Modifier.fillMaxWidth()) {
				Text("● Buy 28", style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp), color = Green)
				Spacer(modifier = Modifier.weight(1f))
				Text("Hold 12", style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp), color = Muted)
				Spacer(modifier = Modifier.weight(1f))
				Text("Sell 2", style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp), color = Muted)
			}
			Kicker("RECENT ACTIONS")
			listOf(
				Triple("Morgan Stanley", "Buy", "$260"),
				Triple("Wedbush", "Buy", "$285"),
				Triple("Goldman Sachs", "Buy", "$256"),
				Triple("UBS", "Hold", "$236"),
				Triple("Barclays", "Hold", "$230"),
			).forEach { (name, action, target) ->
				Row(
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.fillMaxWidth()
						.height((38 * u).dp)
						.clip(RoundedCornerShape((10 * u).dp))
						.background(Color(0xFF10182B))
						.padding(horizontal = (12 * u).dp),
				) {
					Text(name, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp), color = Bright)
					Spacer(modifier = Modifier.weight(1f))
					Text(
						action,
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp),
						color = if (action == "Buy") Green else Muted,
					)
					Spacer(modifier = Modifier.width((10 * u).dp))
					Text(target, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp), color = Bright)
				}
			}
		}
	}
}

/** Compare and learn (collapsed 1:2526 / open 1:2719) — peer table. */
@Composable
private fun CompareCard() {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	var open by rememberSaveable { mutableStateOf(false) }
	Column(
		verticalArrangement = Arrangement.spacedBy(if (open) (21 * u).dp else (12 * u).dp),
		modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape((16 * u).dp)).background(Card)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
			) { open = !open }
			.padding(horizontal = (16 * u).dp, vertical = (14 * u).dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
			Text(
				"Compare and learn",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp),
				color = Bright,
			)
			Spacer(modifier = Modifier.weight(1f))
			if (!open) {
				Image(painterResource(R.drawable.ic_sd_caret), null, modifier = Modifier.size((20 * u).dp))
			}
		}
		if (!open) {
			Text(
				"vs MSFT · GOOGL",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp),
				color = Muted,
			)
		} else {
			Box(modifier = Modifier.fillMaxWidth()) {
				// AAPL column tint spans the table rows (frame 1:2721).
				Box(
					modifier = Modifier
						.offset(x = (78 * u).dp, y = 0.dp)
						.size((81 * u).dp, (170 * u).dp)
						.background(Color(0x125DA8BF), RoundedCornerShape((8 * u).dp)),
				)
				Column(verticalArrangement = Arrangement.spacedBy((12 * u).dp), modifier = Modifier.fillMaxWidth()) {
					CompareRow("", "AAPL", "MSFT", "GOOGL", header = true)
					CompareRow("P/E ratio", "31.2", "36x", "24x")
					CompareRow("Rev growth", "+6.1%", "+15%", "+12%", valueColor = Green)
					CompareRow("Profit margin", "24.3%", "36%", "29%")
					CompareRow("Market cap", "$3.5T", "$3.4T", "$2.3T")
				}
			}
			Text(
				"Cultural context only, not financial advice.",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp),
				color = Muted,
			)
		}
	}
}

@Composable
private fun CompareRow(label: String, a: String, m: String, g: String, header: Boolean = false, valueColor: Color? = null) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Row(horizontalArrangement = Arrangement.spacedBy((8 * u).dp), modifier = Modifier.fillMaxWidth().height((20 * u).dp)) {
		Text(
			label,
			style = TextStyle(fontFamily = Geist, fontSize = (11 * u).sp),
			color = Muted,
			modifier = Modifier.weight(1f),
		)
		Text(
			a,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp),
			color = valueColor ?: Bright,
			textAlign = androidx.compose.ui.text.style.TextAlign.Center,
			modifier = Modifier.weight(1f),
		)
		Text(
			m,
			style = TextStyle(fontFamily = Geist, fontWeight = if (header) FontWeight.Medium else FontWeight.Normal, fontSize = (11 * u).sp),
			color = valueColor ?: Bright,
			textAlign = androidx.compose.ui.text.style.TextAlign.Center,
			modifier = Modifier.weight(1f),
		)
		Text(
			g,
			style = TextStyle(fontFamily = Geist, fontWeight = if (header) FontWeight.Medium else FontWeight.Normal, fontSize = (11 * u).sp),
			color = valueColor ?: Bright,
			textAlign = androidx.compose.ui.text.style.TextAlign.Center,
			modifier = Modifier.weight(1f),
		)
	}
}

/** "SINCE YOU SAVED +4.6%" banner (16:1012) for the My STAK entry. */
@Composable
private fun SinceYouSavedCard() {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((12 * u).dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((16 * u).dp))
			.background(Card)
			.padding((16 * u).dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((8 * u).dp)) {
			Image(painterResource(R.drawable.ic_saved_bookmark), null, modifier = Modifier.size((12 * u).dp))
			Text(
				"SINCE YOU SAVED",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, letterSpacing = (0.8 * u).sp),
				color = Muted,
			)
			Spacer(modifier = Modifier.width((8 * u).dp))
			Text(
				"+4.6%",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp),
				color = Green,
			)
		}
		Text(
			"Saved 5 weeks ago. AAPL is up 4.6% since, moving roughly with the market. Steady giants tend to.",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (11 * u).sp, lineHeight = (14 * u).sp),
			color = Muted,
		)
	}
}
