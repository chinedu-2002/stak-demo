package com.stak.demo.ui.simulate

import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.stak.demo.ui.theme.ADVANCE_ROUNDING

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
	val CtaBorder = androidx.compose.ui.graphics.Brush.verticalGradient(
		0f to Color(0xA1659EAD),
		1f to Color(0x6E16363F),
	)
}

internal val PLTR_BUY = BuySpec(
	"Buy PLTR?", "P", "Palantir Technologies", "$28.40 today", "▲ 1.1%",
	"$8,800.00", "$8,775.00", "0.8803", "PLTR",
)

// Codex parity audit (2026-09-04): the COST row's Buy serves COST into the
// same 1:4232 ticket template - a $25 paper order like PLTR's. Mirrors
// ios/StakDemo/Simulate/SimulateView.swift.
internal val COST_BUY = BuySpec(
	"Buy COST?", "C", "Costco Wholesale", "$947.20 today", "▲ 0.7%",
	"$8,800.00", "$8,775.00", "0.0264", "COST",
)

/** The Saved-staks tickets, by symbol; an unknown symbol falls back to the frame's PLTR. */
internal fun simBuySpec(symbol: String): BuySpec = listOf(PLTR_BUY, COST_BUY).firstOrNull { it.symbol == symbol } ?: PLTR_BUY

// A BuySpec is not Saveable - a raised ticket survives by its symbol and
// is re-served from simBuySpec on restore.
internal val SimBuySpecSaver: Saver<BuySpec?, String> = Saver(
	save = { it?.symbol },
	restore = { simBuySpec(it) },
)

/**
 * 07 · Simulate — "Simulate home · paper" (CHINEDU 1:3898) with the
 * Buy-PLTR ticket (1:4232) and Order filled (85:895). The paper-money
 * tab: portfolio-value hero with chart, saved staks with Buy pills,
 * insight, best/worst duo, how-paper-trading-works, portfolio rows,
 * the allocation breakdown and this week's board.
 */
// `internal` because the hoisted ticket callback carries the internal BuySpec.
@Composable
internal fun SimulateScreen(
	onOpenPortfolio: () -> Unit,
	// Codex parity audit (2026-09-04): pick rows and the best/worst duo
	// open THEIR pick - the tapped ticker rides to PickDetailScreen.
	onOpenPick: (String) -> Unit,
	onOpenLeaderboard: () -> Unit,
	// When the shell hosts the ticket (1:4232: the sheet covers the tab bar),
	// it raises it here with the tapped row's spec (PLTR_BUY / COST_BUY).
	onPracticeBuy: ((BuySpec) -> Unit)? = null,
	// B14 (1:3964 Motion): "All saved staks ›" hops to the My STAK tab.
	onOpenMyStak: () -> Unit = {},
) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	// The in-page ticket: null = closed, else the tapped row's spec.
	var buySpec by rememberSaveable(stateSaver = SimBuySpecSaver) { mutableStateOf<BuySpec?>(null) }
	val practiceBuy: (BuySpec) -> Unit = onPracticeBuy ?: { buySpec = it }

	Box(modifier = Modifier.fillMaxSize().background(StakColors.Bg)) {
		Column(modifier = Modifier.fillMaxSize()) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.fillMaxWidth()
					.background(StakColors.Bg)
					.statusBarsPadding()
					.padding(horizontal = (20 * u).dp)
					.padding(top = (8 * u).dp, bottom = (8 * u).dp),
			) {
				Column(verticalArrangement = Arrangement.spacedBy((3 * u).dp)) {
					Text(
						text = "Simulate",
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (26 * u).sp),
						color = Color.White,
					)
					Text(
						text = "Pick from your saves. Paper money does the talking.",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp),
						color = Sim.Muted,
					)
				}
				Spacer(modifier = Modifier.weight(1f))
				// Codex audit (2026-09-04): the clock (1:3918 "btn" / icon/clock)
				// is the pick history - it opens the portfolio page, where the
				// SOLD · REALIZED rows live. Same plumbing as AuthBackCircle.
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier
						.size((40 * u).dp)
						.background(Sim.CardBg, CircleShape)
						.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
							onClick = onOpenPortfolio,
						),
				) {
					Image(painterResource(R.drawable.ic_sim_clock), "History", modifier = Modifier.size((18 * u).dp))
				}
			}
			Column(
				verticalArrangement = Arrangement.spacedBy((18 * u).dp),
				modifier = Modifier
					.weight(1f)
					.fillMaxWidth()
					.verticalScroll(rememberScrollState())
					.padding(horizontal = (20 * u).dp)
					.padding(top = (18 * u).dp, bottom = (26 * u).dp),
			) {
				ScoreHero(onOpenLeaderboard = onOpenLeaderboard)
				SectionHeader("Saved staks")
				// Codex audit (2026-09-04): a saved stak that has been bought says
				// so - the authored "not in portfolio yet" only while it is not.
				SavedStakRow("P", "PLTR", savedStakSub("PLTR", "Saved Jun 30 · not in portfolio yet"), spec = PLTR_BUY, onBuy = practiceBuy)
				SavedStakRow("C", "COST", savedStakSub("COST", "Saved Jul 2 · not in portfolio yet"), spec = COST_BUY, onBuy = practiceBuy)
				CenterLink("All saved staks", onClick = onOpenMyStak)
				InsightCard()
				// Review 2026-09-04: best / worst come from the live ledger (max /
				// min gain dollars); the seeded rows still render the authored
				// NVDA +24.0% / +$24 on $100 and MSFT -3.0% / -$3 on $100. Fewer
				// than two positions and the duo has nothing to compare - hidden.
				val positions = PaperPortfolio.positions
				val best = positions.maxByOrNull { it.gainDollars }
				val worst = positions.minByOrNull { it.gainDollars }
				if (positions.size >= 2 && best != null && worst != null) {
					Row(horizontalArrangement = Arrangement.spacedBy((10 * u).dp)) {
						PickDuo("BEST PICK", best.row.pct, if (best.row.up) Sim.Green else Sim.Red, best.row.badge, best.row.ticker, best.duoLine, Modifier.weight(1f)) { onOpenPick(best.row.ticker) }
						PickDuo("WORST PICK", worst.row.pct, if (worst.row.up) Sim.Green else Sim.Red, worst.row.badge, worst.row.ticker, worst.duoLine, Modifier.weight(1f)) { onOpenPick(worst.row.ticker) }
					}
				}
				HowItWorksCard()
				SectionHeader("Your portfolio")
				// Codex audit (2026-09-04): the first three held positions, from
				// the shared PaperPortfolio - a fresh buy lands at the top.
				PaperPortfolio.positions.take(3).forEach { pos ->
					val p = pos.row
					PortfolioRow(p.badge, p.ticker, p.sub, p.amount, p.pct, p.up, onClick = { onOpenPick(p.ticker) })
				}
				// Authored copy (user, 2026-09-04 (CHINEDU 07 · Simulate 423:1007): the authored look wins); the ledger still drives the rows above.
				CenterLink("See all 12 picks", onClick = onOpenPortfolio)
				Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
					Text(
						text = "Portfolio breakdown",
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (16 * u).sp),
						color = Sim.HeaderGray,
					)
					Spacer(modifier = Modifier.weight(1f))
					Row(
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.spacedBy((5 * u).dp),
						modifier = Modifier.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
							onClick = onOpenPortfolio,
						),
					) {
						Text(
							text = "Portfolio",
							style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (14 * u).sp),
							color = Color.White,
						)
						Text(
							text = "›",
							style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (14 * u).sp),
							color = Sim.Muted,
						)
					}
				}
				SimAllocationCard()
				BoardCard(onOpenLeaderboard = onOpenLeaderboard)
			}
		}
		buySpec?.let { spec ->
			// 85:895 authors "View portfolio" / "Done" on the Simulate add-success sheet.
			DiscoverBuyFlow(onClose = { buySpec = null }, spec = spec, filledPrimary = "View portfolio", filledSecondary = "Done", ticketSecondary = "Back")
		}
	}
}

@Composable
private fun SectionHeader(title: String) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Text(
		text = title,
		style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (16 * u).sp, lineHeight = (20 * u).sp),
		color = Sim.HeaderGray,
	)
}

/** "In portfolio · N shares" once a saved stak is held, else the authored saved line. */
private fun savedStakSub(symbol: String, authored: String): String =
	PaperPortfolio.pickSpec(symbol)?.let { "In portfolio · ${it.shares} shares" } ?: authored

/**
 * Codex audit (2026-09-04): demo stand-ins until the backend serves price
 * history - each range's line as fractions of the chart height from the
 * bottom (0 = bottom), spread evenly across the width. "3M" keeps the
 * authored sim_chart_line (1:3935). Mirrors
 * ios/StakDemo/Simulate/SimulateView.swift.
 */
private val RANGE_SERIES: Map<String, List<Float>> = mapOf(
	"1D" to listOf(0.45f, 0.50f, 0.42f, 0.55f, 0.60f, 0.52f, 0.58f, 0.66f, 0.62f, 0.70f),
	"1W" to listOf(0.30f, 0.38f, 0.35f, 0.50f, 0.46f, 0.60f, 0.72f),
	"1M" to listOf(0.25f, 0.30f, 0.28f, 0.42f, 0.38f, 0.52f, 0.48f, 0.60f, 0.55f, 0.68f, 0.75f),
	"YTD" to listOf(0.20f, 0.35f, 0.30f, 0.45f, 0.40f, 0.55f, 0.50f, 0.62f, 0.70f, 0.66f, 0.80f),
	"1Y" to listOf(0.15f, 0.22f, 0.30f, 0.26f, 0.40f, 0.36f, 0.50f, 0.55f, 0.48f, 0.62f, 0.70f, 0.82f),
)

/** The chart line for a non-3M range: Sim.Teal 2-wide round stroke over a 22%-to-clear teal fill. */
@Composable
private fun RangeChart(series: List<Float>, modifier: Modifier) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Canvas(modifier = modifier) {
		val w = size.width
		val h = size.height
		val last = series.lastIndex
		val points = series.mapIndexed { i, f -> Offset(if (last == 0) 0f else w * i / last, h * (1f - f)) }
		val line = Path().apply {
			moveTo(points.first().x, points.first().y)
			points.drop(1).forEach { lineTo(it.x, it.y) }
		}
		val fill = Path().apply {
			addPath(line)
			lineTo(points.last().x, h)
			lineTo(points.first().x, h)
			close()
		}
		drawPath(fill, Brush.verticalGradient(0f to Sim.Teal.copy(alpha = 0.22f), 1f to Color.Transparent, startY = 0f, endY = h))
		drawPath(line, Sim.Teal, style = Stroke(width = (2 * u).dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
	}
}

/** Portfolio value hero — $10,240.00, cash, weekly change, chart + pills. */
@Composable
private fun ScoreHero(onOpenLeaderboard: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	// Codex audit (2026-09-04): the range pills select; "3M" is the authored
	// default (1:3935) and keeps the authored chart image.
	var range by rememberSaveable { mutableStateOf("3M") }
	// "$10,240.00" split at the point - the figure in the 44 box, the cents in the 18.
	val valueText = PaperPortfolio.usd(PaperPortfolio.portfolioValue)
	Column(
		verticalArrangement = Arrangement.spacedBy((11 * u).dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((18 * u).dp))
			.background(Sim.CardBg)
			.padding(vertical = (20 * u).dp),
	) {
		Text(
			text = "PORTFOLIO VALUE",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, lineHeight = (13 * u).sp, letterSpacing = (0.9 * u + ADVANCE_ROUNDING.value).sp),
			color = Sim.Faint,
			modifier = Modifier.padding(horizontal = (20 * u).dp),
		)
		Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(horizontal = (20 * u).dp)) {
			Text(
				text = valueText.substringBefore('.'),
				// Authored box (1:3924) is 55 tall — pin it so the stack sums.
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (44 * u).sp, lineHeight = (55 * u).sp, letterSpacing = (-0.44 * u).sp),
				color = Color.White,
			)
			Text(
				text = "." + valueText.substringAfter('.'),
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (18 * u).sp, lineHeight = (23 * u).sp),
				color = Sim.Muted,
				// Authored (1:3923): ".00" starts 8 after the figure and its box
				// bottom sits 8 above the figure's (55 vs y24+h23). The gap is
				// the design (user, 2026-09-04 (CHINEDU 07 · Simulate 423:1007): the authored look wins).
				modifier = Modifier.padding(start = (8 * u).dp, bottom = (8 * u).dp),
			)
		}
		Text(
			text = "+${PaperPortfolio.usd(PaperPortfolio.allTimeGain)} all time on $" + String.format(java.util.Locale.US, "%,.0f", PaperPortfolio.PAPER_START) + " paper · 12 picks",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Light, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
			color = Sim.Muted,
			modifier = Modifier.padding(horizontal = (20 * u).dp),
		)
		Row(horizontalArrangement = Arrangement.spacedBy((6 * u).dp), modifier = Modifier.padding(horizontal = (20 * u).dp)) {
			Text(
				text = "Cash available",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
				color = Sim.Muted,
			)
			Text(
				text = PaperPortfolio.usd(PaperPortfolio.cash),
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
				color = Sim.Bright,
			)
		}
		Text(
			text = "▲ ${PaperPortfolio.WEEK_GAIN} (${PaperPortfolio.WEEK_PCT}) this week",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
			color = Sim.Green,
			modifier = Modifier.padding(horizontal = (20 * u).dp),
		)
		Box(
			modifier = Modifier
				.padding(horizontal = (20 * u).dp)
				.clip(RoundedCornerShape((13 * u).dp))
				.background(Sim.TealTint)
				.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null,
					onClick = onOpenLeaderboard,
				)
				.padding(horizontal = (11 * u).dp, vertical = (6 * u).dp),
		) {
			Text(
				text = "#${PaperPortfolio.WEEK_RANK} this week",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
				color = Sim.Teal,
			)
		}
		// Authored: ranks→chart gap is exactly the column's 11 (1:3935).
		val chartModifier = Modifier.align(Alignment.CenterHorizontally).size((343 * u).dp, (73.56 * u).dp)
		val series = RANGE_SERIES[range]
		if (series == null) {
			Image(
				painter = painterResource(R.drawable.sim_chart_line),
				contentDescription = null,
				contentScale = ContentScale.Fit,
				modifier = chartModifier,
			)
		} else {
			RangeChart(series = series, modifier = chartModifier)
		}
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy((37 * u).dp),
			// Authored chart→pills gap 40; the column gap contributes 11.
			modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = (29 * u).dp),
		) {
			listOf("1D", "1W", "1M", "3M", "YTD", "1Y").forEach { label ->
				val select = Modifier.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null,
				) { range = label }
				if (label == range) {
					Box(
						contentAlignment = Alignment.Center,
						modifier = Modifier
							.size((39 * u).dp, (22.5 * u).dp)
							.clip(RoundedCornerShape((11.25 * u).dp))
							.background(Color(0x292C9DBC))
							.border((0.75 * u).dp, Color(0x662C9DBC), RoundedCornerShape((11.25 * u).dp))
							.then(select),
					) {
						Text(label, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp, lineHeight = (16 * u).sp), color = Sim.Teal)
					}
				} else {
					Text(label, style = TextStyle(fontFamily = Geist, fontSize = (12 * u).sp, lineHeight = (16 * u).sp), color = Sim.Muted, modifier = select)
				}
			}
		}
	}
}

/** Saved stak row — badge, ticker + saved line, teal Buy pill (60x30); Buy raises the row's own ticket. */
@Composable
private fun SavedStakRow(badge: String, ticker: String, sub: String, spec: BuySpec, onBuy: (BuySpec) -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy((12 * u).dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((12 * u).dp))
			.background(Sim.CardBg)
			.padding(horizontal = (14 * u).dp, vertical = (11 * u).dp),
	) {
		Box(contentAlignment = Alignment.Center, modifier = Modifier.size((38 * u).dp).background(Sim.ChipBg, CircleShape)) {
			Text(badge, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp), color = Sim.BadgeInk)
		}
		Column(verticalArrangement = Arrangement.spacedBy((3 * u).dp), modifier = Modifier.weight(1f)) {
			Text(ticker, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp), color = Color.White)
			Text(sub, style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp), color = Sim.Muted)
		}
		BuyPill(onClick = { onBuy(spec) })
	}
}

/** 60x30 gradient Buy pill with the CTA hairline. */
@Composable
internal fun BuyPill(text: String = "Buy", onClick: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.size((60 * u).dp, (30 * u).dp)
			.background(
				androidx.compose.ui.graphics.Brush.verticalGradient(
					0.0889f to Color(0xFFA6E4F7),
					0.3919f to Color(0xFF5DA8BF),
					0.7255f to Color(0xFF3C98B4),
					1f to Color(0xFF3C98B4),
				),
				RoundedCornerShape((6 * u).dp),
			)
			.border((0.36 * u).dp, Sim.CtaBorder, RoundedCornerShape((6 * u).dp))
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			),
	) {
		Text(text, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp), color = Color.White)
	}
}

@Composable
internal fun CenterLink(text: String, onClick: () -> Unit = {}) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy((6 * u).dp, Alignment.CenterHorizontally),
		modifier = Modifier
			.fillMaxWidth()
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			),
	) {
		Text(text, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (13 * u).sp), color = Color.White)
		Text("›", style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (14 * u).sp), color = Sim.Muted)
	}
}

@Composable
private fun InsightCard() {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((8 * u).dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((16 * u).dp))
			.background(Sim.CardBg)
			.padding(horizontal = (16 * u).dp, vertical = (15 * u).dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((7 * u).dp)) {
			Image(painterResource(R.drawable.ic_gist_sparkle), null, modifier = Modifier.size((16 * u).dp))
			Text(
				text = "INSIGHT",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, letterSpacing = (0.9 * u + ADVANCE_ROUNDING.value).sp),
				color = Sim.Faint,
			)
		}
		Text(
			text = "Three chip stocks drove 70% of your gains this month. Your taste has a type.",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp, lineHeight = (20 * u).sp),
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
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((7 * u).dp),
		modifier = modifier
			.clip(RoundedCornerShape((16 * u).dp))
			.background(Sim.CardBg)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			)
			.padding((14 * u).dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
			Text(
				text = kicker,
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, letterSpacing = (0.9 * u + ADVANCE_ROUNDING.value).sp),
				color = Sim.Faint,
			)
			Spacer(modifier = Modifier.weight(1f))
			Text(pct, style = TextStyle(fontFamily = Geist, fontSize = (12 * u).sp), color = pctColor)
		}
		Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((9 * u).dp)) {
			Box(contentAlignment = Alignment.Center, modifier = Modifier.size((34 * u).dp).background(Sim.ChipBg, CircleShape)) {
				Text(badge, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (14 * u).sp), color = Sim.BadgeInk)
			}
			Column(verticalArrangement = Arrangement.spacedBy((2 * u).dp)) {
				Text(ticker, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp), color = Color.White)
				Text(sub, style = TextStyle(fontFamily = Geist, fontSize = (11 * u).sp), color = Sim.Faint)
			}
		}
	}
}

@Composable
private fun HowItWorksCard() {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((10 * u).dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((16 * u).dp))
			.background(Sim.CardBg)
			.padding(horizontal = (16 * u).dp, vertical = (15 * u).dp),
	) {
		Text(
			text = "HOW PAPER TRADING WORKS",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, letterSpacing = (0.9 * u + ADVANCE_ROUNDING.value).sp),
			color = Sim.Faint,
		)
		listOf(
			"1" to "Buy a stock with paper dollars. It starts that day.",
			"2" to "Your shares move with the real price, up or down.",
			"3" to "Sell anytime and the cash returns to your balance.",
		).forEach { (n, rule) ->
			Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((10 * u).dp)) {
				Box(contentAlignment = Alignment.Center, modifier = Modifier.size((20 * u).dp).background(Sim.TealTint, RoundedCornerShape((10 * u).dp))) {
					Text(n, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (11 * u).sp), color = Sim.Teal)
				}
				Text(
					text = rule,
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp, lineHeight = (18 * u).sp),
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
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy((12 * u).dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((12 * u).dp))
			.background(Sim.CardBg)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			)
			.padding(horizontal = (14 * u).dp, vertical = (12 * u).dp),
	) {
		Box(contentAlignment = Alignment.Center, modifier = Modifier.size((40 * u).dp).background(Sim.ChipBg, CircleShape)) {
			Text(badge, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (16 * u).sp, lineHeight = (20 * u).sp), color = Sim.BadgeInk)
		}
		Column(verticalArrangement = Arrangement.spacedBy((3 * u).dp), modifier = Modifier.weight(1f)) {
			Text(ticker, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp, lineHeight = (15 * u).sp), color = Color.White)
			Text(sub, style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp, lineHeight = (13 * u).sp), color = Sim.Muted)
		}
		Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy((2 * u).dp)) {
			Text(
				amount,
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
				color = if (up) Sim.Green else Sim.Red,
			)
			Text(pct, style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp, lineHeight = (13 * u).sp), color = Sim.Faint)
		}
		if (trailing != null) {
			trailing()
		}
	}
}

/** Allocation — Simulate's donut + the 42/25/17/8/8 sector bars. */
@Composable
private fun SimAllocationCard() {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy((16 * u).dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((16 * u).dp))
			.background(Sim.CardBg)
			.padding((18 * u).dp),
	) {
		Text("Allocation", style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp), color = Color.White)
		Image(painterResource(R.drawable.sim_donut), null, modifier = Modifier.size((150 * u).dp))
		Column(verticalArrangement = Arrangement.spacedBy((12 * u).dp), modifier = Modifier.fillMaxWidth()) {
			SimSector("Tech & AI", "42% · 5 stocks", Sim.Teal, (132 * u).dp)
			SimSector("Finance", "25% · 3 stocks", Color(0xFF7AB3F0), (66 * u).dp)
			SimSector("Green Energy", "17% · 2 stocks", Sim.Green, (63 * u).dp)
			SimSector("Real Estate", "8% · 1 stock", Color(0xFF9E8CE5), (38 * u).dp)
			SimSector("Other", "8% · 1 stock", Sim.Faint, (16 * u).dp)
		}
	}
}

@Composable
private fun SimSector(name: String, share: String, color: Color, fill: Dp) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(verticalArrangement = Arrangement.spacedBy((6 * u).dp), modifier = Modifier.fillMaxWidth()) {
		Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
			Box(modifier = Modifier.size((9 * u).dp).background(color, CircleShape))
			Spacer(modifier = Modifier.width((8 * u).dp))
			Text(name, style = TextStyle(fontFamily = Geist, fontSize = (13 * u).sp), color = Color.White)
			Spacer(modifier = Modifier.weight(1f))
			Text(share, style = TextStyle(fontFamily = Geist, fontSize = (12 * u).sp), color = Sim.Muted)
		}
		Box(modifier = Modifier.fillMaxWidth().height((7 * u).dp).clip(RoundedCornerShape((4 * u).dp)).background(Sim.Track)) {
			Box(modifier = Modifier.width(fill).height((7 * u).dp).background(color, RoundedCornerShape((4 * u).dp)))
		}
	}
}

/** THIS WEEK'S BOARD mini-leaderboard. */
@Composable
private fun BoardCard(onOpenLeaderboard: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((10 * u).dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((16 * u).dp))
			.background(Sim.CardBg)
			.padding((16 * u).dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
			Text(
				text = "THIS WEEK’S BOARD",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, letterSpacing = (0.9 * u + ADVANCE_ROUNDING.value).sp),
				color = Sim.Faint,
			)
			Spacer(modifier = Modifier.weight(1f))
			Text("Trailing 7 days", style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp), color = Sim.Faint)
		}
		BoardRow("1", "Maya A.", "+9.4%", you = false)
		BoardRow("2", "Jide O.", "+8.8%", you = false)
		// Codex audit (2026-09-04): You reads the shared week figures (the
		// hero's +1.9% / #47) instead of its own contradicting +4.2%.
		// Authored board figures (1:4111 +4.2%; user, 2026-09-04 (CHINEDU 07 · Simulate 423:1007): the authored look wins).
		BoardRow("47", "You", "+4.2%", you = true)
		CenterLink("Full leaderboard", onClick = onOpenLeaderboard)
	}
}

@Composable
private fun BoardRow(rank: String, name: String, pct: String, you: Boolean) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy((10 * u).dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((10 * u).dp))
			.background(if (you) Sim.TealTint else Color.Transparent)
			.padding(horizontal = (10 * u).dp, vertical = (7 * u).dp),
	) {
		Text(
			rank,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (12 * u).sp),
			color = if (you) Sim.Teal else Sim.Faint,
		)
		Text(
			name,
			style = TextStyle(fontFamily = Geist, fontWeight = if (you) FontWeight.SemiBold else FontWeight.Medium, fontSize = (13 * u).sp),
			color = Color.White,
			modifier = Modifier.weight(1f),
		)
		Text(
			pct,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = if (you) (13 * u).sp else (12 * u).sp),
			color = if (you) Sim.Teal else Sim.HeaderGray,
		)
	}
}
