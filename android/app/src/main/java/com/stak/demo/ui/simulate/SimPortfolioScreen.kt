package com.stak.demo.ui.simulate

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.stak.demo.ui.theme.ADVANCE_ROUNDING
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.nativeCanvas

// Codex audit (2026-09-04): SimPick and the six authored rows moved to
// PaperPortfolio.kt - the rows are the shared portfolio's positions now.

/**
 * 07 · Simulate — "Final · Portfolio · paper" (CHINEDU 1:4496) with the
 * Sell NVDA? confirm (1:4698) and Position closed (73:855) sheets.
 * Mirrors ios/StakDemo/Simulate/SimPortfolioView.swift.
 */
@Composable
fun SimPortfolioScreen(
	onBack: () -> Unit,
	// Codex parity audit (2026-09-04): every row (and its Sell pill) opens
	// ITS pick - the row's ticker rides to PickDetailScreen.
	onOpenPick: (String) -> Unit,
) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
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
					.padding(horizontal = (18 * u).dp, vertical = (8 * u).dp),
			) {
				AuthBackCircle(onClick = onBack)
				Spacer(modifier = Modifier.weight(1f))
				Text(
					text = "Your portfolio",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (16 * u).sp, lineHeight = (20 * u).sp),
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
				verticalArrangement = Arrangement.spacedBy((16 * u).dp),
				modifier = Modifier
					.weight(1f)
					.fillMaxWidth()
					.verticalScroll(rememberScrollState())
					.padding(horizontal = (20 * u).dp)
					.padding(top = (6 * u).dp, bottom = (20 * u).dp),
			) {
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier
						.align(Alignment.CenterHorizontally)
						.size((158 * u).dp, (32 * u).dp)
						.background(Sim.CardBg, RoundedCornerShape((16 * u).dp)),
				) {
					Text(
						// Authored copy (user, 2026-09-04 (CHINEDU 07 · Simulate 423:1007): the authored look wins).
						"12 picks · +$240.00 all time",
						style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp, lineHeight = (13 * u).sp),
						color = Sim.Muted,
					)
				}
				Row(horizontalArrangement = Arrangement.spacedBy((8 * u).dp)) {
					FilterChip("Top gainers", selected = true)
					FilterChip("Newest", selected = false)
					FilterChip("Worst", selected = false)
				}
				// Codex audit (2026-09-04): the live positions - a fresh buy sits
				// at the top, a sold one drops to SOLD · REALIZED below.
				PaperPortfolio.positions.forEach { pos ->
					val p = pos.row
					PortfolioRow(
						badge = p.badge, ticker = p.ticker, sub = p.sub,
						amount = p.amount, pct = p.pct, up = p.up,
						onClick = { onOpenPick(p.ticker) },
						// B16 (1:4496 Motion): the Sell pill opens the Pick detail
						// - the authored sell flow lives there; the in-page
						// sheets below stay built but unwired.
						trailing = { SellPill(onClick = { onOpenPick(p.ticker) }) },
					)
				}
				Box(
					contentAlignment = Alignment.CenterStart,
					modifier = Modifier.fillMaxWidth().height((17 * u).dp).padding(start = (2 * u).dp),
				) {
					Text(
						text = "SOLD · REALIZED",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, lineHeight = (13 * u).sp, letterSpacing = (0.9 * u + ADVANCE_ROUNDING.value).sp),
						color = Sim.Faint,
					)
				}
				PaperPortfolio.realized.forEach { r ->
					RealizedRow(r.badge, r.ticker, r.sub, r.amount, r.up)
				}
				Text(
					text = "Sell a pick and the cash returns to your balance, gain or loss.",
					style = TextStyle(fontFamily = Geist, fontSize = (11 * u).sp, lineHeight = (14 * u).sp),
					color = Sim.Faint,
					modifier = Modifier.align(Alignment.CenterHorizontally),
				)
			}
		}
		// The unwired in-page host keeps the frame's NVDA (1:4698 / 73:855).
		if (showSell) {
			SellConfirmSheet(
				pick = pickSpec("NVDA"),
				// Review 2026-09-04: stays unwired (as on iOS) - the live sell
				// runs in SellFlowHost from the Pick detail.
				onConfirm = { showSell = false; showClosed = true },
				onDismiss = { showSell = false },
			)
		}
		if (showClosed) {
			PositionClosedSheet(
				pick = pickSpec("NVDA"),
				onBackToSimulate = { showClosed = false; onBack() },
				onViewPortfolio = { showClosed = false },
			)
		}
	}
}

@Composable
private fun FilterChip(label: String, selected: Boolean) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Box(
		modifier = Modifier
			.clip(RoundedCornerShape((14 * u).dp))
			.background(if (selected) Sim.TealTint else Sim.CardBg)
			.padding(horizontal = (12 * u).dp, vertical = (6 * u).dp),
	) {
		Text(
			text = label,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
			color = if (selected) Sim.Teal else Sim.Muted,
		)
	}
}

/** 60x30 outlined Sell pill — transparent bg with the app's secondary hairline. */
@Composable
private fun SellPill(onClick: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.size((60 * u).dp, (30 * u).dp)
			.border((0.36 * u).dp, Color(0x54343B4F), RoundedCornerShape((6 * u).dp))
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			),
	) {
		Text(
			"Sell",
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp, lineHeight = (15 * u).sp),
			color = Color(0xFFDCE7F7),
		)
	}
}

@Composable
private fun RealizedRow(badge: String, ticker: String, sub: String, amount: String, up: Boolean) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy((12 * u).dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((12 * u).dp))
			.background(Sim.CardBg)
			.padding(horizontal = (14 * u).dp, vertical = (12 * u).dp),
	) {
		Box(contentAlignment = Alignment.Center, modifier = Modifier.size((36 * u).dp).background(Sim.ChipBg, CircleShape)) {
			Text(badge, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (14 * u).sp, lineHeight = (18 * u).sp), color = Sim.BadgeInk)
		}
		Column(verticalArrangement = Arrangement.spacedBy((2 * u).dp), modifier = Modifier.weight(1f)) {
			Text(ticker, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp, lineHeight = (15 * u).sp), color = Color.White)
			Text(sub, style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp, lineHeight = (13 * u).sp), color = Sim.Muted)
		}
		Text(
			amount,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (13 * u).sp, lineHeight = (18 * u).sp),
			color = if (up) Sim.Green else Sim.Red,
		)
	}
}

/** Shared sheet scaffold for the sell flow. */
@Composable
private fun SimSheet(onDismiss: () -> Unit, content: @Composable () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Box(modifier = Modifier.fillMaxSize()) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(Color(0x9E02050E))
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
				.clip(RoundedCornerShape(topStart = (24 * u).dp, topEnd = (24 * u).dp))
				.background(Sim.CardBg)
				.padding(horizontal = (20 * u).dp)
				.padding(top = (10 * u).dp)
				.padding(bottom = (30 * u).dp),
		) {
			Box(
				modifier = Modifier
					.align(Alignment.CenterHorizontally)
					.padding(bottom = (18 * u).dp)
					.size((40 * u).dp, (4 * u).dp)
					.background(Sim.Track, RoundedCornerShape((2 * u).dp)),
			)
			content()
		}
	}
}

/** The sell sheets' stock row (1:4698) - the tapped pick's name, price and day move. */
@Composable
private fun PickSellRow(pick: PickSpec) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy((11 * u).dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((6 * u).dp))
			.background(Sim.TealTint)
			.padding(horizontal = (14 * u).dp, vertical = (12 * u).dp),
	) {
		Box(contentAlignment = Alignment.Center, modifier = Modifier.size((38 * u).dp).background(Sim.ChipBg, CircleShape)) {
			Text(pick.badge, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp), color = Sim.BadgeInk)
		}
		Column(verticalArrangement = Arrangement.spacedBy((2 * u).dp), modifier = Modifier.weight(1f)) {
			Text(pick.company, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (13 * u).sp), color = Color.White)
			Text("${pick.priceNow} today", style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp), color = Sim.Muted)
		}
		Text(
			pick.dayChange,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp),
			color = if (pick.dayChange.startsWith("▼")) Sim.Red else Sim.Green,
		)
	}
}

/** "Sell NVDA?" confirm sheet content (1:4698), templated on the tapped pick. */
@Composable
private fun SellConfirmContent(pick: PickSpec, onConfirm: () -> Unit, onDismiss: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	var mode by rememberSaveable { mutableIntStateOf(0) }
	Column(verticalArrangement = Arrangement.spacedBy((14 * u).dp), modifier = Modifier.fillMaxWidth()) {
		Text(
			"Sell ${pick.symbol}?",
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (18 * u).sp),
			color = Color.White,
		)
		PickSellRow(pick)
		Row(horizontalArrangement = Arrangement.spacedBy((6 * u).dp)) {
			Text(
				"You hold ${pick.shares} shares from your ${pick.stakeBasis} stake.",
				style = TextStyle(fontFamily = Geist, fontSize = (12 * u).sp, lineHeight = (18 * u).sp),
				color = Sim.Body,
			)
		}
		Row(horizontalArrangement = Arrangement.spacedBy((6 * u).dp)) {
			Text("Position value", style = TextStyle(fontFamily = Geist, fontSize = (12 * u).sp), color = Sim.Muted)
			Text(
				pick.stakeValue,
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp),
				color = Sim.Bright,
			)
		}
		Row(horizontalArrangement = Arrangement.spacedBy((8 * u).dp), modifier = Modifier.fillMaxWidth()) {
			listOf("All", "Half", "Custom").forEachIndexed { i, label ->
				val sel = i == mode
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier
						.weight(1f)
						.clip(RoundedCornerShape((10 * u).dp))
						.background(if (sel) Color(0xFF0F2A38) else Color(0xFF0B1430))
						.border(
							if (sel) (0.5 * u).dp else (1 * u).dp,
							if (sel) Color(0xFF5DA8BF) else Color(0x1FFFFFFF),
							RoundedCornerShape((10 * u).dp),
						)
						.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
						) { mode = i }
						.padding(vertical = (8 * u).dp),
				) {
					Text(
						label,
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp),
						color = if (sel) Color(0xFFA6E4F7) else Color(0xFFDCE7F7),
					)
				}
			}
		}
		Row(
			horizontalArrangement = Arrangement.spacedBy((6 * u).dp, Alignment.CenterHorizontally),
			modifier = Modifier.fillMaxWidth(),
		) {
			Text("Returning", style = TextStyle(fontFamily = Geist, fontSize = (12 * u).sp), color = Sim.Muted)
			Text(
				pick.stakeValue,
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp),
				color = Sim.Bright,
			)
			Text("to your cash", style = TextStyle(fontFamily = Geist, fontSize = (12 * u).sp), color = Sim.Muted)
		}
		Column(verticalArrangement = Arrangement.spacedBy((16 * u).dp), modifier = Modifier.fillMaxWidth()) {
			// Dark navy confirm — #12203e per the frame.
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.fillMaxWidth()
					.height((52 * u).dp)
					.background(Sim.DarkCta, RoundedCornerShape((6 * u).dp))
					.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
						onClick = onConfirm,
					),
			) {
				Text(
					"Confirm sell",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (14 * u).sp),
					color = Color.White,
				)
			}
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.fillMaxWidth()
					.height((52 * u).dp)
					.background(Color(0x0AFFFFFF), RoundedCornerShape((6 * u).dp))
					.border((0.36 * u).dp, Color(0x54343B4F), RoundedCornerShape((6 * u).dp))
					.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
						onClick = onDismiss,
					),
			) {
				Text("Back", style = TextStyle(fontFamily = Sora, fontSize = (14 * u).sp), color = Sim.Muted)
			}
		}
	}
}

/** "Position closed" success sheet content (73:855), templated on the tapped pick. */
@Composable
private fun PositionClosedContent(pick: PickSpec, onBackToSimulate: () -> Unit, onViewPortfolio: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy((14 * u).dp),
		modifier = Modifier.fillMaxWidth(),
	) {
		Image(painterResource(R.drawable.ic_sheet_check), null, modifier = Modifier.size((47 * u).dp))
		Text(
			"Position closed",
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (18 * u).sp),
			color = Color.White,
		)
		PickSellRow(pick)
		Text(
			"Sold ${pick.shares} shares from your ${pick.stakeBasis} stake.",
			style = TextStyle(fontFamily = Geist, fontSize = (12 * u).sp, lineHeight = (18 * u).sp),
			color = Sim.Body,
			modifier = Modifier.fillMaxWidth(),
		)
		Row(horizontalArrangement = Arrangement.spacedBy((6 * u).dp), modifier = Modifier.fillMaxWidth()) {
			Text("Proceeds", style = TextStyle(fontFamily = Geist, fontSize = (12 * u).sp), color = Sim.Muted)
			Text(
				pick.stakeValue,
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp),
				color = Sim.Bright,
			)
		}
		// 73:855 centres the Returned line (the Proceeds line above stays left-aligned).
		Row(horizontalArrangement = Arrangement.spacedBy((6 * u).dp, Alignment.CenterHorizontally), modifier = Modifier.fillMaxWidth()) {
			Text("Returned", style = TextStyle(fontFamily = Geist, fontSize = (12 * u).sp), color = Sim.Muted)
			Text(
				pick.stakeValue,
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp),
				color = Sim.Bright,
			)
			Text("to your cash (${pick.gain})", style = TextStyle(fontFamily = Geist, fontSize = (12 * u).sp), color = Sim.Muted)
		}
		Column(verticalArrangement = Arrangement.spacedBy((16 * u).dp), modifier = Modifier.fillMaxWidth()) {
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.fillMaxWidth()
					.height((52 * u).dp)
					.drawBehind {
						val r = (6 * u).dp.toPx()
						val paint = android.graphics.Paint().apply { isAntiAlias = true }
						paint.color = android.graphics.Color.argb(23, 82, 170, 199)
						paint.maskFilter = android.graphics.BlurMaskFilter((12.28f * u).dp.toPx(), android.graphics.BlurMaskFilter.Blur.NORMAL)
						drawContext.canvas.nativeCanvas.drawRoundRect(0f, (12.28f * u).dp.toPx(), size.width, (12.28f * u).dp.toPx() + size.height, r, r, paint)
					}
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
						onClick = onBackToSimulate,
					),
			) {
				Text(
					"Back to Simulate",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (14 * u).sp),
					color = Color.White,
				)
			}
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.fillMaxWidth()
					.height((52 * u).dp)
					.background(Color(0x0AFFFFFF), RoundedCornerShape((6 * u).dp))
					.border((0.36 * u).dp, Color(0x54343B4F), RoundedCornerShape((6 * u).dp))
					.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
						onClick = onViewPortfolio,
					),
			) {
				Text("View portfolio", style = TextStyle(fontFamily = Sora, fontSize = (14 * u).sp), color = Sim.Muted)
			}
		}
	}
}

/** "Sell NVDA?" confirm sheet (1:4698) — kept for the in-page host. */
@Composable
private fun SellConfirmSheet(pick: PickSpec, onConfirm: () -> Unit, onDismiss: () -> Unit) {
	SimSheet(onDismiss = onDismiss) { SellConfirmContent(pick = pick, onConfirm = onConfirm, onDismiss = onDismiss) }
}

/** "Position closed" success sheet (73:855) — kept for the in-page host. */
@Composable
private fun PositionClosedSheet(pick: PickSpec, onBackToSimulate: () -> Unit, onViewPortfolio: () -> Unit) {
	SimSheet(onDismiss = onViewPortfolio) { PositionClosedContent(pick = pick, onBackToSimulate = onBackToSimulate, onViewPortfolio = onViewPortfolio) }
}

/**
 * Sell -> Position-closed flow, reused by the pick detail. Confirming
 * morphs the sheet in place - the authored SMART_ANIMATE 350 ease-out
 * (1:4698 -> 73:855): content cross-fades while the height animates,
 * nothing clipped (B18).
 */
@Composable
internal fun SellFlowHost(
	// Codex parity audit (2026-09-04): the sheets sell the TAPPED pick.
	pick: PickSpec,
	onClose: () -> Unit,
	// B19: hosts route the success CTAs to their authored edges; left
	// alone they fall back to a plain close.
	onBackToSimulate: () -> Unit = onClose,
	onViewPortfolio: () -> Unit = onClose,
) {
	var closed by rememberSaveable { mutableStateOf(false) }
	// The scrim tap is unauthored - it keeps the per-state plain dismiss.
	SimSheet(onDismiss = { if (closed) onViewPortfolio() else onClose() }) {
		AnimatedContent(
			targetState = closed,
			transitionSpec = {
				ContentTransform(
					fadeIn(tween(350, easing = EaseOut)),
					fadeOut(tween(350, easing = EaseOut)),
					sizeTransform = SizeTransform(clip = false) { _, _ -> tween(350, easing = EaseOut) },
				)
			},
			contentAlignment = Alignment.BottomCenter,
			label = "sellMorph",
		) { isClosed ->
			if (!isClosed) {
				// Codex audit (2026-09-04): Confirm sells the position exactly
				// once - cash returns, the row moves to SOLD · REALIZED - as
				// the sheet morphs into Position closed (73:855). Review
				// 2026-09-04: only a real sell morphs - a pick no longer held
				// leaves the confirm where it is.
				SellConfirmContent(pick = pick, onConfirm = { if (!closed && PaperPortfolio.sell(pick.symbol)) closed = true }, onDismiss = onClose)
			} else {
				PositionClosedContent(pick = pick, onBackToSimulate = onBackToSimulate, onViewPortfolio = onViewPortfolio)
			}
		}
	}
}
