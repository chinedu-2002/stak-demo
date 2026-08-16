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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.R
import com.stak.demo.ui.discover.BuySpec
import com.stak.demo.ui.discover.DiscoverBuyFlow
import com.stak.demo.ui.theme.Geist
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors

internal object Sim {
	val CardBg = Color(0xFF181F30)
	val Muted = Color(0xFF819ABB)
	val Faint = Color(0xFF5C6B85)
	val Body = Color(0xFFC8D2E0)
	val Green = Color(0xFF2FD08A)
	val Red = Color(0xFFFF5A6A)
	val Teal = Color(0xFF69B3CA)
	val TealTint = Color(0x1A69B3CA)
	val ChipBg = Color(0xFF242B3D)
	val Track = Color(0xFF2A3346)
	val BadgeInk = Color(0xFF9EADC7)
	val Bright = Color(0xFFF2F6FC)
	val HeaderGray = Color(0xFFD3D3DD)
	val DarkCta = Color(0xFF12203E)
	val CtaBorder = Color(0xA1659EAD)
}

internal val PLTR_BUY = BuySpec(
	"Buy PLTR?", "P", "Palantir Technologies", "$28.40 today", "▲ 1.1%",
	"$8,800.00", "$8,775.00", "0.8803", "PLTR",
)

/**
 * 07 · Simulate — "Simulate home · paper" (CHINEDU 1:3898) with the
 * Buy-PLTR ticket (1:4232) and Order filled (85:895). The paper-money
 * tab: portfolio-value hero with chart, saved staks with Buy pills,
 * insight, best/worst duo, how-paper-trading-works, portfolio rows,
 * the allocation breakdown and this week's board.
 */
@Composable
fun SimulateScreen(
	onOpenPortfolio: () -> Unit,
	onOpenPick: () -> Unit,
	onOpenLeaderboard: () -> Unit,
) {
	var showBuy by rememberSaveable { mutableStateOf(false) }

	Box(modifier = Modifier.fillMaxSize().background(StakColors.Bg)) {
		Column(modifier = Modifier.fillMaxSize()) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.fillMaxWidth()
					.background(StakColors.Bg)
					.statusBarsPadding()
					.padding(horizontal = 20.dp)
					.padding(top = 8.dp, bottom = 8.dp),
			) {
				Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
					Text(
						text = "Simulate",
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 26.sp),
						color = Color.White,
					)
					Text(
						text = "Pick from your saves. Paper money does the talking.",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 12.sp),
						color = Sim.Muted,
					)
				}
				Spacer(modifier = Modifier.weight(1f))
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier.size(40.dp).background(Sim.CardBg, CircleShape),
				) {
					Image(painterResource(R.drawable.ic_sim_clock), null, modifier = Modifier.size(18.dp))
				}
			}
			Column(
				verticalArrangement = Arrangement.spacedBy(18.dp),
				modifier = Modifier
					.weight(1f)
					.fillMaxWidth()
					.verticalScroll(rememberScrollState())
					.padding(horizontal = 20.dp)
					.padding(top = 18.dp, bottom = 26.dp),
			) {
				ScoreHero(onOpenLeaderboard = onOpenLeaderboard)
				SectionHeader("Saved staks")
				SavedStakRow("P", "PLTR", "Saved Jun 30 · not in portfolio yet", onBuy = { showBuy = true })
				SavedStakRow("C", "COST", "Saved Jul 2 · not in portfolio yet", onBuy = { showBuy = true })
				CenterLink("All saved staks")
				InsightCard()
				Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
					PickDuo("BEST PICK", "+24.0%", Sim.Green, "N", "NVDA", "+$24 on $100", Modifier.weight(1f), onOpenPick)
					PickDuo("WORST PICK", "-3.0%", Sim.Red, "M", "MSFT", "-$3 on $100", Modifier.weight(1f), onOpenPick)
				}
				HowItWorksCard()
				SectionHeader("Your portfolio")
				PortfolioRow("N", "NVDA", "Picked May 8 · up 24% since", "+$24.00", "+24.0%", true, onOpenPick)
				PortfolioRow("T", "TSLA", "Picked Jun 3 · up 18% since", "+$18.00", "+18.0%", true, onOpenPick)
				PortfolioRow("M", "MSFT", "Picked Jun 26 · down 3% since", "-$3.00", "-3.0%", false, onOpenPick)
				CenterLink("See all 12 picks", onClick = onOpenPortfolio)
				Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
					Text(
						text = "Portfolio breakdown",
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
						color = Sim.HeaderGray,
					)
					Spacer(modifier = Modifier.weight(1f))
					Row(
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.spacedBy(5.dp),
						modifier = Modifier.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
							onClick = onOpenPortfolio,
						),
					) {
						Text(
							text = "Portfolio",
							style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 14.sp),
							color = Color.White,
						)
						Text(
							text = "›",
							style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 14.sp),
							color = Sim.Muted,
						)
					}
				}
				SimAllocationCard()
				BoardCard(onOpenLeaderboard = onOpenLeaderboard)
			}
		}
		if (showBuy) {
			DiscoverBuyFlow(onClose = { showBuy = false }, spec = PLTR_BUY)
		}
	}
}

@Composable
private fun SectionHeader(title: String) {
	Text(
		text = title,
		style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
		color = Sim.HeaderGray,
	)
}

/** Portfolio value hero — $10,240.00, cash, weekly change, chart + pills. */
@Composable
private fun ScoreHero(onOpenLeaderboard: () -> Unit) {
	Column(
		verticalArrangement = Arrangement.spacedBy(11.dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(18.dp))
			.background(Sim.CardBg)
			.padding(20.dp),
	) {
		Text(
			text = "PORTFOLIO VALUE",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 10.sp, letterSpacing = 0.9.sp),
			color = Sim.Faint,
		)
		Row(verticalAlignment = Alignment.Bottom) {
			Text(
				text = "$10,240",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 44.sp, letterSpacing = (-0.44).sp),
				color = Color.White,
			)
			Text(
				text = ".00",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
				color = Sim.Muted,
				modifier = Modifier.padding(bottom = 6.dp),
			)
		}
		Text(
			text = "+$240.00 all time on $10,000 paper · 12 picks",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Light, fontSize = 12.sp),
			color = Sim.Muted,
		)
		Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
			Text(
				text = "Cash available",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 12.sp),
				color = Sim.Muted,
			)
			Text(
				text = "$8,800.00",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp),
				color = Sim.Bright,
			)
		}
		Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
			Text(
				text = "▲ +$186 (+1.9%) this week",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp),
				color = Sim.Green,
			)
			Spacer(modifier = Modifier.weight(1f))
			Box(
				modifier = Modifier
					.clip(RoundedCornerShape(13.dp))
					.background(Sim.TealTint)
					.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
						onClick = onOpenLeaderboard,
					)
					.padding(horizontal = 11.dp, vertical = 6.dp),
			) {
				Text(
					text = "#47 this week",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp),
					color = Sim.Teal,
				)
			}
		}
		Image(
			painter = painterResource(R.drawable.sim_chart_line),
			contentDescription = null,
			contentScale = ContentScale.Fit,
			modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp).size(310.dp, 68.dp),
		)
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(30.dp),
			modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 20.dp),
		) {
			listOf("1D", "1W", "1M", "3M", "YTD", "1Y").forEach { label ->
				if (label == "3M") {
					Box(
						contentAlignment = Alignment.Center,
						modifier = Modifier
							.size(39.dp, 22.5.dp)
							.clip(RoundedCornerShape(11.25.dp))
							.background(Color(0x292C9DBC))
							.border(0.75.dp, Color(0x662C9DBC), RoundedCornerShape(11.25.dp)),
					) {
						Text(label, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp), color = Sim.Teal)
					}
				} else {
					Text(label, style = TextStyle(fontFamily = Geist, fontSize = 12.sp), color = Sim.Muted)
				}
			}
		}
	}
}

/** Saved stak row — badge, ticker + saved line, teal Buy pill (60x30). */
@Composable
private fun SavedStakRow(badge: String, ticker: String, sub: String, onBuy: () -> Unit) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(12.dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(12.dp))
			.background(Sim.CardBg)
			.padding(horizontal = 14.dp, vertical = 11.dp),
	) {
		Box(contentAlignment = Alignment.Center, modifier = Modifier.size(38.dp).background(Sim.ChipBg, CircleShape)) {
			Text(badge, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 15.sp), color = Sim.BadgeInk)
		}
		Column(verticalArrangement = Arrangement.spacedBy(3.dp), modifier = Modifier.weight(1f)) {
			Text(ticker, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Medium, fontSize = 12.sp), color = Color.White)
			Text(sub, style = TextStyle(fontFamily = Geist, fontSize = 10.sp), color = Sim.Muted)
		}
		BuyPill(onClick = onBuy)
	}
}

/** 60x30 gradient Buy pill with the CTA hairline. */
@Composable
internal fun BuyPill(text: String = "Buy", onClick: () -> Unit) {
	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.size(60.dp, 30.dp)
			.background(
				androidx.compose.ui.graphics.Brush.verticalGradient(
					0.0889f to Color(0xFFA6E4F7),
					0.3919f to Color(0xFF5DA8BF),
					0.7255f to Color(0xFF3C98B4),
					1f to Color(0xFF3C98B4),
				),
				RoundedCornerShape(6.dp),
			)
			.border(0.36.dp, Sim.CtaBorder, RoundedCornerShape(6.dp))
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			),
	) {
		Text(text, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 12.sp), color = Color.White)
	}
}

@Composable
internal fun CenterLink(text: String, onClick: () -> Unit = {}) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
		modifier = Modifier
			.fillMaxWidth()
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			),
	) {
		Text(text, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 13.sp), color = Color.White)
		Text("›", style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 14.sp), color = Sim.Muted)
	}
}

@Composable
private fun InsightCard() {
	Column(
		verticalArrangement = Arrangement.spacedBy(8.dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(16.dp))
			.background(Sim.CardBg)
			.padding(horizontal = 16.dp, vertical = 15.dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
			Image(painterResource(R.drawable.ic_gist_sparkle), null, modifier = Modifier.size(16.dp))
			Text(
				text = "INSIGHT",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 10.sp, letterSpacing = 0.9.sp),
				color = Sim.Faint,
			)
		}
		Text(
			text = "Three chip stocks drove 70% of your gains this month. Your taste has a type.",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 20.sp),
			color = Sim.Body,
		)
	}
}

@Composable
private fun PickDuo(
	kicker: String, pct: String, pctColor: Color,
	badge: String, ticker: String, sub: String,
	modifier: Modifier, onClick: () -> Unit,
) {
	Column(
		verticalArrangement = Arrangement.spacedBy(7.dp),
		modifier = modifier
			.clip(RoundedCornerShape(16.dp))
			.background(Sim.CardBg)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			)
			.padding(14.dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
			Text(
				text = kicker,
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 10.sp, letterSpacing = 0.9.sp),
				color = Sim.Faint,
			)
			Spacer(modifier = Modifier.weight(1f))
			Text(pct, style = TextStyle(fontFamily = Geist, fontSize = 12.sp), color = pctColor)
		}
		Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
			Box(contentAlignment = Alignment.Center, modifier = Modifier.size(34.dp).background(Sim.ChipBg, CircleShape)) {
				Text(badge, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 14.sp), color = Sim.BadgeInk)
			}
			Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
				Text(ticker, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Medium, fontSize = 12.sp), color = Color.White)
				Text(sub, style = TextStyle(fontFamily = Geist, fontSize = 11.sp), color = Sim.Faint)
			}
		}
	}
}

@Composable
private fun HowItWorksCard() {
	Column(
		verticalArrangement = Arrangement.spacedBy(10.dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(16.dp))
			.background(Sim.CardBg)
			.padding(horizontal = 16.dp, vertical = 15.dp),
	) {
		Text(
			text = "HOW PAPER TRADING WORKS",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 10.sp, letterSpacing = 0.9.sp),
			color = Sim.Faint,
		)
		listOf(
			"1" to "Buy a stock with paper dollars. It starts that day.",
			"2" to "Your shares move with the real price, up or down.",
			"3" to "Sell anytime and the cash returns to your balance.",
		).forEach { (n, rule) ->
			Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
				Box(contentAlignment = Alignment.Center, modifier = Modifier.size(20.dp).background(Sim.TealTint, RoundedCornerShape(10.dp))) {
					Text(n, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 11.sp), color = Sim.Teal)
				}
				Text(
					text = rule,
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 18.sp),
					color = Sim.Body,
				)
			}
		}
	}
}

/** One portfolio pick row (badge 40, ticker + picked line, P&L right). */
@Composable
internal fun PortfolioRow(
	badge: String, ticker: String, sub: String,
	amount: String, pct: String, up: Boolean,
	onClick: () -> Unit = {},
	trailing: (@Composable () -> Unit)? = null,
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(12.dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(12.dp))
			.background(Sim.CardBg)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			)
			.padding(horizontal = 14.dp, vertical = 12.dp),
	) {
		Box(contentAlignment = Alignment.Center, modifier = Modifier.size(40.dp).background(Sim.ChipBg, CircleShape)) {
			Text(badge, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 16.sp), color = Sim.BadgeInk)
		}
		Column(verticalArrangement = Arrangement.spacedBy(3.dp), modifier = Modifier.weight(1f)) {
			Text(ticker, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Medium, fontSize = 12.sp), color = Color.White)
			Text(sub, style = TextStyle(fontFamily = Geist, fontSize = 10.sp), color = Sim.Muted)
		}
		Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(2.dp)) {
			Text(
				amount,
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp),
				color = if (up) Sim.Green else Sim.Red,
			)
			Text(pct, style = TextStyle(fontFamily = Geist, fontSize = 10.sp), color = Sim.Faint)
		}
		if (trailing != null) {
			trailing()
		}
	}
}

/** Allocation — Simulate's donut + the 42/25/17/8/8 sector bars. */
@Composable
private fun SimAllocationCard() {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(16.dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(16.dp))
			.background(Sim.CardBg)
			.padding(18.dp),
	) {
		Text("Allocation", style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 15.sp), color = Color.White)
		Image(painterResource(R.drawable.sim_donut), null, modifier = Modifier.size(150.dp))
		Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
			SimSector("Tech & AI", "42% · 5 stocks", Sim.Teal, 132.dp)
			SimSector("Finance", "25% · 3 stocks", Color(0xFF7AB3F0), 66.dp)
			SimSector("Green Energy", "17% · 2 stocks", Sim.Green, 63.dp)
			SimSector("Real Estate", "8% · 1 stock", Color(0xFF9E8CE5), 38.dp)
			SimSector("Other", "8% · 1 stock", Sim.Faint, 16.dp)
		}
	}
}

@Composable
private fun SimSector(name: String, share: String, color: Color, fill: Dp) {
	Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
		Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
			Box(modifier = Modifier.size(9.dp).background(color, CircleShape))
			Spacer(modifier = Modifier.width(8.dp))
			Text(name, style = TextStyle(fontFamily = Geist, fontSize = 13.sp), color = Color.White)
			Spacer(modifier = Modifier.weight(1f))
			Text(share, style = TextStyle(fontFamily = Geist, fontSize = 12.sp), color = Sim.Muted)
		}
		Box(modifier = Modifier.fillMaxWidth().height(7.dp).clip(RoundedCornerShape(4.dp)).background(Sim.Track)) {
			Box(modifier = Modifier.width(fill).height(7.dp).background(color, RoundedCornerShape(4.dp)))
		}
	}
}

/** THIS WEEK'S BOARD mini-leaderboard. */
@Composable
private fun BoardCard(onOpenLeaderboard: () -> Unit) {
	Column(
		verticalArrangement = Arrangement.spacedBy(10.dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(16.dp))
			.background(Sim.CardBg)
			.padding(16.dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
			Text(
				text = "THIS WEEK’S BOARD",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 10.sp, letterSpacing = 0.9.sp),
				color = Sim.Faint,
			)
			Spacer(modifier = Modifier.weight(1f))
			Text("Trailing 7 days", style = TextStyle(fontFamily = Geist, fontSize = 10.sp), color = Sim.Faint)
		}
		BoardRow("1", "Maya A.", "+9.4%", you = false)
		BoardRow("2", "Jide O.", "+8.8%", you = false)
		BoardRow("47", "You", "+4.2%", you = true)
		CenterLink("Full leaderboard", onClick = onOpenLeaderboard)
	}
}

@Composable
private fun BoardRow(rank: String, name: String, pct: String, you: Boolean) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(10.dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(10.dp))
			.background(if (you) Sim.TealTint else Color.Transparent)
			.padding(horizontal = 10.dp, vertical = 7.dp),
	) {
		Text(
			rank,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 12.sp),
			color = if (you) Sim.Teal else Sim.Faint,
		)
		Text(
			name,
			style = TextStyle(fontFamily = Geist, fontWeight = if (you) FontWeight.SemiBold else FontWeight.Medium, fontSize = 13.sp),
			color = Color.White,
			modifier = Modifier.weight(1f),
		)
		Text(
			pct,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = if (you) 13.sp else 12.sp),
			color = if (you) Sim.Teal else Sim.HeaderGray,
		)
	}
}
