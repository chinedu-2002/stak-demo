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
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

private data class SimPick(
	val badge: String, val ticker: String, val sub: String,
	val amount: String, val pct: String, val up: Boolean,
)

private val PICKS = listOf(
	SimPick("N", "NVDA", "Picked May 8 · up 24% since", "+$24.00", "+24.0%", true),
	SimPick("T", "TSLA", "Picked Jun 3 · up 18% since", "+$18.00", "+18.0%", true),
	SimPick("A", "AMD", "Picked May 29 · up 11% since", "+$11.00", "+11.0%", true),
	SimPick("A", "AAPL", "Picked Apr 22 · up 6% since", "+$6.00", "+6.0%", true),
	SimPick("J", "JPM", "Picked Jun 20 · up 2% since", "+$2.00", "+2.0%", true),
	SimPick("M", "MSFT", "Picked Jun 26 · down 3% since", "-$3.00", "-3.0%", false),
)

/**
 * 07 · Simulate — "Final · Portfolio · paper" (CHINEDU 1:4496) with the
 * Sell NVDA? confirm (1:4698) and Position closed (73:855) sheets.
 */
@Composable
fun SimPortfolioScreen(onBack: () -> Unit, onOpenPick: () -> Unit) {
	var showSell by rememberSaveable { mutableStateOf(false) }
	var showClosed by rememberSaveable { mutableStateOf(false) }

	Box(modifier = Modifier.fillMaxSize().background(StakColors.Bg)) {
		Column(modifier = Modifier.fillMaxSize()) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.fillMaxWidth()
					.background(StakColors.Bg)
					.statusBarsPadding()
					.padding(horizontal = 18.dp, vertical = 8.dp),
			) {
				AuthBackCircle(onClick = onBack)
				Spacer(modifier = Modifier.weight(1f))
				Text(
					text = "Your portfolio",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
					color = Color.White,
				)
				Spacer(modifier = Modifier.weight(1f))
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier.size(40.dp).background(Sim.CardBg, CircleShape),
				) {
					Image(painterResource(R.drawable.ic_news_share), null, modifier = Modifier.size(17.dp))
				}
			}
			Column(
				verticalArrangement = Arrangement.spacedBy(10.dp),
				modifier = Modifier
					.weight(1f)
					.fillMaxWidth()
					.verticalScroll(rememberScrollState())
					.padding(horizontal = 20.dp)
					.padding(top = 10.dp, bottom = 20.dp),
			) {
				Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.fillMaxWidth()) {
					Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
						Text("Portfolio value", style = TextStyle(fontFamily = Geist, fontSize = 11.sp), color = Sim.Faint)
						Text(
							"12 picks · +$240.00 all time",
							style = TextStyle(fontFamily = Geist, fontSize = 10.sp),
							color = Sim.Muted,
						)
						Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
							Text("Cash available", style = TextStyle(fontFamily = Geist, fontSize = 12.sp), color = Sim.Muted)
							Text(
								"$8,800.00",
								style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp),
								color = Sim.Bright,
							)
						}
					}
					Spacer(modifier = Modifier.weight(1f))
					Text(
						"$10,240.00",
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 22.sp),
						color = Color.White,
					)
				}
				Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
					FilterChip("Top gainers", selected = true)
					FilterChip("Newest", selected = false)
					FilterChip("Worst", selected = false)
				}
				PICKS.forEach { p ->
					PortfolioRow(
						badge = p.badge, ticker = p.ticker, sub = p.sub,
						amount = p.amount, pct = p.pct, up = p.up,
						onClick = onOpenPick,
						trailing = { BuyPill(text = "Sell", onClick = { showSell = true }) },
					)
				}
				Text(
					text = "SOLD · REALIZED",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 10.sp, letterSpacing = 0.9.sp),
					color = Sim.Faint,
					modifier = Modifier.padding(top = 8.dp, start = 2.dp),
				)
				RealizedRow("S", "SHOP", "Sold May 30 · profit banked", "+$12.00", true)
				RealizedRow("C", "COIN", "Sold Jun 15 · loss realized", "-$8.00", false)
				Text(
					text = "Sell a pick and the cash returns to your balance, gain or loss.",
					style = TextStyle(fontFamily = Geist, fontSize = 10.sp),
					color = Sim.Faint,
					modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp),
				)
			}
		}
		if (showSell) {
			SellConfirmSheet(
				onConfirm = { showSell = false; showClosed = true },
				onDismiss = { showSell = false },
			)
		}
		if (showClosed) {
			PositionClosedSheet(
				onBackToSimulate = { showClosed = false; onBack() },
				onViewPortfolio = { showClosed = false },
			)
		}
	}
}

@Composable
private fun FilterChip(label: String, selected: Boolean) {
	Box(
		modifier = Modifier
			.clip(RoundedCornerShape(14.dp))
			.background(if (selected) Sim.TealTint else Sim.CardBg)
			.then(if (selected) Modifier.border(0.75.dp, Color(0x662C9DBC), RoundedCornerShape(14.dp)) else Modifier)
			.padding(horizontal = 12.dp, vertical = 6.dp),
	) {
		Text(
			text = label,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp),
			color = if (selected) Sim.Teal else Sim.Muted,
		)
	}
}

@Composable
private fun RealizedRow(badge: String, ticker: String, sub: String, amount: String, up: Boolean) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(12.dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(12.dp))
			.background(Sim.CardBg)
			.padding(horizontal = 14.dp, vertical = 11.dp),
	) {
		Box(contentAlignment = Alignment.Center, modifier = Modifier.size(36.dp).background(Sim.ChipBg, CircleShape)) {
			Text(badge, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 14.sp), color = Sim.BadgeInk)
		}
		Column(verticalArrangement = Arrangement.spacedBy(3.dp), modifier = Modifier.weight(1f)) {
			Text(ticker, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Medium, fontSize = 12.sp), color = Color.White)
			Text(sub, style = TextStyle(fontFamily = Geist, fontSize = 10.sp), color = Sim.Muted)
		}
		Text(
			amount,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 13.sp),
			color = if (up) Sim.Green else Sim.Red,
		)
	}
}

/** Shared sheet scaffold for the sell flow. */
@Composable
private fun SimSheet(onDismiss: () -> Unit, content: @Composable () -> Unit) {
	Box(modifier = Modifier.fillMaxSize()) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(Color(0x730A1020))
				.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null,
					onClick = onDismiss,
				),
		)
		Column(
			modifier = Modifier
				.align(Alignment.BottomCenter)
				.fillMaxWidth()
				.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
				.background(Sim.CardBg)
				.padding(horizontal = 20.dp)
				.padding(top = 10.dp)
				.navigationBarsPadding()
				.padding(bottom = 30.dp),
		) {
			Box(
				modifier = Modifier
					.align(Alignment.CenterHorizontally)
					.padding(bottom = 4.dp)
					.size(40.dp, 4.dp)
					.background(Sim.Track, RoundedCornerShape(2.dp)),
			)
			content()
		}
	}
}

@Composable
private fun NvdaSellRow() {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(11.dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(6.dp))
			.background(Sim.TealTint)
			.padding(horizontal = 14.dp, vertical = 12.dp),
	) {
		Box(contentAlignment = Alignment.Center, modifier = Modifier.size(38.dp).background(Sim.ChipBg, CircleShape)) {
			Text("N", style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 15.sp), color = Sim.BadgeInk)
		}
		Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.weight(1f)) {
			Text("NVIDIA Corp", style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 13.sp), color = Color.White)
			Text("$122.10 today", style = TextStyle(fontFamily = Geist, fontSize = 10.sp), color = Sim.Muted)
		}
		Text("▲ 2.4%", style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp), color = Sim.Green)
	}
}

/** "Sell NVDA?" confirm sheet (1:4698). */
@Composable
private fun SellConfirmSheet(onConfirm: () -> Unit, onDismiss: () -> Unit) {
	var mode by rememberSaveable { mutableIntStateOf(0) }
	SimSheet(onDismiss = onDismiss) {
		Column(verticalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.fillMaxWidth()) {
			Text(
				"Sell NVDA?",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
				color = Color.White,
			)
			NvdaSellRow()
			Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
				Text(
					"You hold 1.0152 shares from your $100 stake.",
					style = TextStyle(fontFamily = Geist, fontSize = 12.sp, lineHeight = 18.sp),
					color = Sim.Body,
				)
			}
			Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
				Text("Position value", style = TextStyle(fontFamily = Geist, fontSize = 12.sp), color = Sim.Muted)
				Text(
					"$124.00",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp),
					color = Sim.Bright,
				)
			}
			Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
				listOf("All", "Half", "Custom").forEachIndexed { i, label ->
					val sel = i == mode
					Box(
						contentAlignment = Alignment.Center,
						modifier = Modifier
							.weight(1f)
							.clip(RoundedCornerShape(10.dp))
							.background(if (sel) Color(0xFF0F2A38) else Color(0xFF0B1430))
							.border(
								if (sel) 0.5.dp else 1.dp,
								if (sel) Color(0xFF5DA8BF) else Color(0x1FFFFFFF),
								RoundedCornerShape(10.dp),
							)
							.clickable(
								interactionSource = remember { MutableInteractionSource() },
								indication = null,
							) { mode = i }
							.padding(vertical = 8.dp),
					) {
						Text(
							label,
							style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp),
							color = if (sel) Color(0xFFA6E4F7) else Color(0xFFDCE7F7),
						)
					}
				}
			}
			Row(
				horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
				modifier = Modifier.fillMaxWidth(),
			) {
				Text("Returning", style = TextStyle(fontFamily = Geist, fontSize = 12.sp), color = Sim.Muted)
				Text(
					"$124.00",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 15.sp),
					color = Sim.Bright,
				)
				Text("to your cash", style = TextStyle(fontFamily = Geist, fontSize = 12.sp), color = Sim.Muted)
			}
			Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
				// Dark navy confirm — #12203e per the frame.
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier
						.fillMaxWidth()
						.height(52.dp)
						.background(Sim.DarkCta, RoundedCornerShape(6.dp))
						.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
							onClick = onConfirm,
						),
				) {
					Text(
						"Confirm sell",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 14.sp),
						color = Color.White,
					)
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
							onClick = onDismiss,
						),
				) {
					Text("Back", style = TextStyle(fontFamily = Sora, fontSize = 14.sp), color = Sim.Muted)
				}
			}
		}
	}
}

/** "Position closed" success sheet (73:855). */
@Composable
private fun PositionClosedSheet(onBackToSimulate: () -> Unit, onViewPortfolio: () -> Unit) {
	SimSheet(onDismiss = onViewPortfolio) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(14.dp),
			modifier = Modifier.fillMaxWidth(),
		) {
			Image(painterResource(R.drawable.ic_sheet_check), null, modifier = Modifier.size(47.dp))
			Text(
				"Position closed",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
				color = Color.White,
			)
			NvdaSellRow()
			Text(
				"Sold 1.0152 shares from your $100 stake.",
				style = TextStyle(fontFamily = Geist, fontSize = 12.sp, lineHeight = 18.sp),
				color = Sim.Body,
				modifier = Modifier.fillMaxWidth(),
			)
			Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
				Text("Proceeds", style = TextStyle(fontFamily = Geist, fontSize = 12.sp), color = Sim.Muted)
				Text(
					"$124.00",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp),
					color = Sim.Bright,
				)
			}
			Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
				Text("Returned", style = TextStyle(fontFamily = Geist, fontSize = 12.sp), color = Sim.Muted)
				Text(
					"$124.00",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 15.sp),
					color = Sim.Bright,
				)
				Text("to your cash (+$24.00)", style = TextStyle(fontFamily = Geist, fontSize = 12.sp), color = Sim.Muted)
			}
			Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier
						.fillMaxWidth()
						.height(52.dp)
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
							onClick = onBackToSimulate,
						),
				) {
					Text(
						"Back to Simulate",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 14.sp),
						color = Color.White,
					)
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
							onClick = onViewPortfolio,
						),
				) {
					Text("View portfolio", style = TextStyle(fontFamily = Sora, fontSize = 14.sp), color = Sim.Muted)
				}
			}
		}
	}
}

/** Sell -> Position-closed flow, reused by the pick detail. */
@Composable
internal fun SellFlowHost(onClose: () -> Unit) {
	var closed by rememberSaveable { mutableStateOf(false) }
	if (!closed) {
		SellConfirmSheet(onConfirm = { closed = true }, onDismiss = onClose)
	} else {
		PositionClosedSheet(onBackToSimulate = onClose, onViewPortfolio = onClose)
	}
}
