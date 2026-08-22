package com.stak.demo.ui.discover

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.R
import com.stak.demo.ui.theme.Geist
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors
import kotlin.math.roundToInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Palette of the CHINEDU "04 · Discover" frames. */
private object Disc {
	val SheetBg = Color(0xFF181F30)
	val Muted = Color(0xFF819ABB)
	val Faint = Color(0xFF5C6B85)
	val Body = Color(0xFFC8D2E0)
	val Teal = Color(0xFF69B3CA)
	val Green = Color(0xFF2FD08A)
	val ChipBg = Color(0xFF242B3D)
	val Divider = Color(0xFF2A3346)
	val BadgeInk = Color(0xFF9EADC7)
	val TipBg = Color(0x1A69B3CA)
	val SaveChipBg = Color(0x26FFFFFF)
	val AmountBg = Color(0xFF0B1430)
	val AmountBorder = Color(0x1FFFFFFF)
	val AmountInk = Color(0xFFDCE7F7)
	val AmountSelBg = Color(0xFF0F2A38)
	val AmountSelBorder = Color(0xFF5DA8BF)
	val AmountSelInk = Color(0xFFA6E4F7)
	val BrightInk = Color(0xFFF2F6FC)
}

private val CtaGradient = Brush.verticalGradient(
	0.0889f to Color(0xFFA6E4F7),
	0.3919f to Color(0xFF5DA8BF),
	0.7255f to Color(0xFF3C98B4),
	1f to Color(0xFF3C98B4),
)
private val CtaBorder = Color(0xA1659EAD)

/** A practice-buy ticket's stock values (Buy NVDA? 1:2159 / Buy AAPL? 1:3423). */
internal data class BuySpec(
	val title: String,
	val badge: String,
	val name: String,
	val priceLine: String,
	val change: String,
	val cashBefore: String,
	val cashAfter: String,
	val shares: String,
	val symbol: String,
)

internal val NVDA_BUY = BuySpec("Buy NVDA?", "N", "NVIDIA Corp", "$122.10 today", "\u25b2 2.4%", "$8,800.00", "$8,775.00", "0.2048", "NVDA")
internal val AAPL_BUY = BuySpec("Buy AAPL?", "A", "Apple", "$229.35 today", "\u25b2 1.2%", "$8,800.00", "$8,775.00", "0.1090", "AAPL")

/** One deck card's designed content (art + copy at the front-card scale). */
internal data class DeckCard(
	val artRes: Int,
	val ticker: String,
	val headline: String,
	val price: String,
	val change: String,
	val tip: String,
	val cardTop: Color,
	val artBg: Color,
)

internal val DECK = listOf(
	DeckCard(
		R.drawable.disc_card_nvda, "NVDA · NVIDIA Corp",
		"Chip demand is outrunning supply, and NVIDIA sets the prices.",
		"$122.10", "▲ 2.4% today", "Chip stocks swing hard. Small stakes, long views.",
		Color(0xFF152A47), Color(0xFF142844),
	),
	DeckCard(
		R.drawable.disc_card_aapl, "AAPL · Apple Inc",
		"Two billion devices, and every one of them keeps paying Apple.",
		"$229.35", "▲ 1.2% today", "Steady giants move slower. Stable stocks often do.",
		Color(0xFF283E5D), Color(0xFF253A59),
	),
	DeckCard(
		R.drawable.disc_card_googl, "GOOGL · Alphabet Inc",
		"Search pays for everything, and nine billion-user products ride behind it.",
		"$178.90", "▲ 0.8% today", "Ad money moves with the economy, so some quarters just drift.",
		Color(0xFF263D5D), Color(0xFF2F486E),
	),
)

/**
 * 04 · Discover — "first run" (CHINEDU 1:1627) with its states: the
 * swipe deck (12 cards cycling the three designed ones), the Save chip
 * toast (1:1796), the Buy-NVDA practice sheet (1:1970) and the Order
 * filled sheet (85:1205). Swiping down advances the deck and the ring
 * counts along. Practice buy raises the ticket; confirming fills the
 * paper order.
 */
@Composable
fun DiscoverScreen(onLearnMore: () -> Unit = {}, onPracticeBuy: () -> Unit = {}) {
	// Prototype: tapping the front card itself also opens the Stock Detail.
	// The buy ticket itself is raised by the shell (over the tab bar).
	var seen by rememberSaveable { mutableIntStateOf(0) }
	var savedToast by remember { mutableStateOf(false) }
	val topOffset = remember(seen) { Animatable(0f) }
	val promote = remember(seen) { Animatable(0f) }
	val enter = remember(seen) { Animatable(if (seen == 0) 1f else 0f) }
	LaunchedEffect(seen) { if (enter.value < 1f) enter.animateTo(1f, tween(200, easing = EaseOut)) }
	val scope = rememberCoroutineScope()
	val density = LocalDensity.current
	val u = com.stak.demo.ui.onboarding.figmaUnit()

	LaunchedEffect(savedToast) {
		if (savedToast) {
			delay(2200)
			savedToast = false
		}
	}

	Box(modifier = Modifier.fillMaxSize().background(StakColors.Bg)) {
		Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
			// Header — Discover + progress ring, kicker below.
			Column(
				verticalArrangement = Arrangement.spacedBy((5 * u).dp),
				modifier = Modifier.fillMaxWidth().padding(horizontal = (20 * u).dp).padding(top = (10 * u).dp),
			) {
				Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
					Text(
						text = "Discover",
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (26 * u).sp, lineHeight = (33 * u).sp),
						color = Color.White,
					)
					Spacer(modifier = Modifier.weight(1f))
					val count = (seen + 1).coerceAtMost(12)
					Box(contentAlignment = Alignment.Center, modifier = Modifier.size((44 * u).dp)) {
						ProgressRing(progress = count / 12f, u = u)
						Text(
							text = "$count/12",
							style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = (11 * u).sp, lineHeight = (14 * u).sp),
							color = Color.White,
						)
					}
				}
				Text(
					text = "TODAY · AI & CHIPS",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, lineHeight = (13 * u).sp, letterSpacing = (0.9 * u).sp),
					color = Disc.Faint,
					modifier = Modifier.padding(horizontal = (2 * u).dp),
				)
			}
			Spacer(modifier = Modifier.height((27 * u).dp))
			if (seen >= 12) {
				EndOfDeck(
					onPracticeBuySaves = onPracticeBuy,
					onSwipeAgain = { seen = 0 },
				)
			} else {
			// Deck — a fixed composition: every dimension scales by the 390dp
			// artboard unit so proportions match the frame on any device.
			Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f).fillMaxWidth()) {
				Box(
					modifier = Modifier
						.padding(horizontal = (20 * u).dp)
						.fillMaxWidth()
						.height((484.65 * u).dp)
						// The shuffle lives inside the deck bounds — the
						// diving card must never cover the gesture/CTA zone.
						.clipToBounds()
						.pointerInput(seen) {
							detectVerticalDragGestures(
								onDragEnd = {
									scope.launch {
										if (topOffset.value > with(density) { (110 * u).dp.toPx() }) {
											// The frame's card shuffle: the swiped card flies
											// off fading while the queue steps forward and the
											// cycled card fades in at the back (1:1627).
											launch { topOffset.animateTo(with(density) { (500 * u).dp.toPx() }, tween(280, easing = EaseOut)) }
											promote.animateTo(1f, tween(300, easing = EaseOut))
											seen += 1
										} else {
											topOffset.animateTo(0f, tween(180))
										}
									}
								},
							) { change, dragAmount ->
								change.consume()
								if (promote.value == 0f && enter.value == 1f && (dragAmount > 0f || topOffset.value > 0f)) {
									scope.launch {
										topOffset.snapTo((topOffset.value + dragAmount).coerceAtLeast(0f))
									}
								}
							}
						},
				) {
					// The authored deck (1:1627): the queued cards behind are
					// the DESIGNED ILLUSION — the exact authored slabs, always
					// (the user's spec: they give the illusion of a queue).
					val p = promote.value
					Image(
						painter = painterResource(R.drawable.disc_peek_top),
						contentDescription = null,
						modifier = Modifier
							.align(Alignment.TopStart)
							.offset(x = (39 * u).dp, y = 0.dp)
							.size((273.66 * u).dp, (336.66 * u).dp),
					)
					Image(
						painter = painterResource(R.drawable.disc_peek_mid),
						contentDescription = null,
						modifier = Modifier
							.align(Alignment.TopStart)
							.offset(x = (18 * u).dp, y = (24 * u).dp)
							.size((313.14 * u).dp, (352.87 * u).dp),
					)
					FrontDeckCard(
						card = DECK[seen % 3],
						onSave = { savedToast = true },
						u = u,
						modifier = Modifier
							.align(Alignment.TopCenter)
							.offset(y = (54.65 * u).dp)
							.offset { androidx.compose.ui.unit.IntOffset(0, topOffset.value.roundToInt()) }
							.graphicsLayer {
								alpha = (1f - p) * enter.value
								val s = 0.97f + 0.03f * enter.value
								scaleX = s
								scaleY = s
							}
							.clickable(
								interactionSource = remember { MutableInteractionSource() },
								indication = null,
								onClick = onLearnMore,
							),
					)
				}
				Spacer(modifier = Modifier.height((10 * u).dp))
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.spacedBy((5 * u).dp),
				) {
					Column(verticalArrangement = Arrangement.spacedBy((1 * u).dp), modifier = Modifier.alpha(0.5f)) {
						GestureChevron(u)
						GestureChevron(u)
					}
					Text(
						text = "Swipe down",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (10 * u).sp, lineHeight = (13 * u).sp),
						color = Disc.Faint,
					)
				}
				Spacer(modifier = Modifier.height((19 * u).dp))
				Row(horizontalArrangement = Arrangement.spacedBy((36 * u).dp)) {
					Box(
						contentAlignment = Alignment.Center,
						modifier = Modifier
							.size((120 * u).dp, (52 * u).dp)
							.background(CtaGradient, RoundedCornerShape((6 * u).dp))
							.border((0.36 * u).dp, CtaBorder, RoundedCornerShape((6 * u).dp))
							.clickable(
								interactionSource = remember { MutableInteractionSource() },
								indication = null,
							) { onPracticeBuy() },
					) {
						Text(
							text = "Practice buy",
							style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (14 * u).sp, lineHeight = (21 * u).sp),
							color = Color.White,
						)
					}
					Box(
						contentAlignment = Alignment.Center,
						modifier = Modifier
							.size((120 * u).dp, (52 * u).dp)
							.border((0.36 * u).dp, Color(0x54343B4F), RoundedCornerShape((6 * u).dp))
							.clickable(
								interactionSource = remember { MutableInteractionSource() },
								indication = null,
								onClick = onLearnMore,
							),
					) {
						Text(
							text = "Learn more",
							style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp, lineHeight = (15 * u).sp),
							color = Disc.Muted,
						)
					}
				}
				Spacer(modifier = Modifier.height((19 * u).dp))
			}
			}
		}
		// Saved toast (frame 1:1796) — centered pill under the header.
		if (savedToast) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy((6 * u).dp),
				modifier = Modifier
					.align(Alignment.TopCenter)
					.statusBarsPadding()
					.padding(top = (78 * u).dp)
					.clip(RoundedCornerShape((19.5 * u).dp))
					.background(Disc.ChipBg)
					.padding(horizontal = (14 * u).dp, vertical = (10 * u).dp),
			) {
				Image(painterResource(R.drawable.ic_saved_bookmark), null, modifier = Modifier.size((12 * u).dp))
				Text(
					text = "Saved to My STAK",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp),
					color = Color.White,
				)
			}
		}
	}
}

/** The full-size front card (350 wide) with its live Save chip. */
@Composable
internal fun FrontDeckCard(card: DeckCard, onSave: () -> Unit, u: Float, modifier: Modifier = Modifier) {
	Box(
		modifier = modifier
			.width((350 * u).dp)
			// The deck's layer structure: every boundary in the frame is a
			// brightness step plus a thin dark rim (the authored exports
			// carry it). NVDA's dark chrome makes its own step; light-topped
			// cards need the rim — a tight dark seam hugging the edge — so
			// the card reads as its own layer over the queue in EVERY state.
			.drawBehind {
				val corner = (22 * u).dp.toPx()
				val reach = (6 * u).dp.toPx()
				val step = 1.dp.toPx()
				var d = 0f
				while (d < reach) {
					val t = d / reach
					drawRoundRect(
						color = Color(0xFF060B16).copy(alpha = 0.5f * (1f - t) * (1f - t)),
						topLeft = Offset(-d, -d),
						size = Size(size.width + 2 * d, size.height + 2 * d),
						cornerRadius = CornerRadius(corner + d),
						style = Stroke(width = step),
					)
					d += step
				}
			},
	) {
		DeckCardBody(card = card, onSave = onSave, u = u)
	}
}

@Composable
private fun DeckCardBody(card: DeckCard, onSave: (() -> Unit)?, u: Float) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		// The authored card template (1:1740, shared by all three designs):
		// art 340x229 at y4, overlay at 258 -> gap 25.
		verticalArrangement = Arrangement.spacedBy((25 * u).dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((22 * u).dp))
			.background(Brush.verticalGradient(0f to card.cardTop, 1f to Color(0xFF0C1526)))
			.padding(top = (4 * u).dp, bottom = (4 * u).dp),
	) {
		Box(
			modifier = Modifier
				.size((340 * u).dp, (229 * u).dp)
				.clip(RoundedCornerShape((18 * u).dp))
				.background(card.artBg),
		) {
			Image(
				painter = painterResource(card.artRes),
				contentDescription = null,
				contentScale = ContentScale.Crop,
				modifier = Modifier.size((340 * u).dp, (229 * u).dp),
			)
			if (card.artRes != R.drawable.disc_card_nvda) {
				// NVDA's chip is baked into its art; the others draw it live
				// at the template's authored spot (art x264 y6, 340-264-72=4).
				SaveChip(u = u, modifier = Modifier.align(Alignment.TopEnd).padding(top = (6 * u).dp, end = (4 * u).dp))
			}
			if (onSave != null) {
				Box(
					modifier = Modifier
						.align(Alignment.TopEnd)
						.padding(top = (2 * u).dp, end = (6 * u).dp)
						.size((86 * u).dp, (38 * u).dp)
						.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
							onClick = onSave,
						),
				)
			}
		}
		Column(
			verticalArrangement = Arrangement.spacedBy((19 * u).dp),
			modifier = Modifier.fillMaxWidth().padding(horizontal = (18 * u).dp).padding(bottom = (16 * u).dp),
		) {
			Column(verticalArrangement = Arrangement.spacedBy((8 * u).dp)) {
				Text(
					text = card.ticker,
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (10 * u).sp, lineHeight = (13 * u).sp),
					color = Disc.Muted,
				)
				Text(
					text = card.headline,
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (16 * u).sp, lineHeight = (23 * u).sp),
					color = Color.White,
				)
				Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy((9 * u).dp)) {
					Text(
						text = card.price,
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (20 * u).sp, lineHeight = (25 * u).sp),
						color = Color.White,
					)
					Text(
						text = card.change,
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp, lineHeight = (14 * u).sp),
						color = Disc.Green,
						modifier = Modifier.padding(bottom = (2 * u).dp),
					)
				}
			}
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy((8 * u).dp),
				modifier = Modifier
					.fillMaxWidth()
					.clip(RoundedCornerShape((10 * u).dp))
					.background(Disc.TipBg)
					.padding(horizontal = (12 * u).dp, vertical = (9 * u).dp),
			) {
				Text(
					text = "TIP",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, letterSpacing = (0.9 * u).sp),
					color = Disc.Teal,
				)
				Text(
					text = card.tip,
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (11 * u).sp, lineHeight = (15 * u).sp),
					color = Disc.Body,
					modifier = Modifier.weight(1f),
				)
			}
		}
	}
}

/** rgba(255,255,255,0.09) Save pill with the small bookmark. */
@Composable
private fun SaveChip(u: Float, modifier: Modifier = Modifier) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy((6 * u).dp),
		modifier = modifier
			.clip(RoundedCornerShape((16 * u).dp))
			.background(Disc.SaveChipBg)
			.padding(horizontal = (13 * u).dp, vertical = (7 * u).dp),
	) {
		Text(
			text = "Save",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
			color = Color.White,
		)
		Image(painterResource(R.drawable.ic_hero_bookmark), null, modifier = Modifier.size((12 * u).dp))
	}
}

/** 16x8 down-chevron stroke (Muted). */
@Composable
private fun GestureChevron(u: Float) {
	Canvas(modifier = Modifier.size((16 * u).dp, (8 * u).dp)) {
		val p = Path().apply {
			moveTo(0f, 0f)
			lineTo(size.width / 2f, size.height)
			lineTo(size.width, 0f)
		}
		drawPath(p, StakColors.Muted, style = Stroke(width = (1.6 * u).dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
	}
}

/** Shared sheet scaffold — #0a1020 scrim at 45% + r24 #181f30 sheet. */
@Composable
private fun SheetScaffold(onDismiss: () -> Unit, content: @Composable () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Box(modifier = Modifier.fillMaxSize()) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				// Authored ticket scrim rgba(0,0,0,0.6) (1:2158).
				.background(Color(0x99000000))
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
				.background(Disc.SheetBg)
				.padding(horizontal = (20 * u).dp)
				.padding(top = (10 * u).dp)
				// Authored sheets (1:2159 et al) are bottom-anchored with a 30
				// pad that INCLUDES the home-indicator zone - no extra inset.
				.padding(bottom = (30 * u).dp),
		) {
			Box(
				modifier = Modifier
					.align(Alignment.CenterHorizontally)
					// Authored (1:2159): handle at y12–16, title at y32 — so 2
					// above the rect and 16 below it after the 10 top padding.
					.padding(top = (2 * u).dp, bottom = (16 * u).dp)
					.size((40 * u).dp, (4 * u).dp)
					.background(Disc.Divider, RoundedCornerShape((2 * u).dp)),
			)
			content()
		}
	}
}

/** NVDA row used by both sheets — teal-tinted, badge + price + change. */
@Composable
private fun NvdaStockRow(spec: BuySpec = NVDA_BUY) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy((12 * u).dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((6 * u).dp))
			.background(Disc.TipBg)
			.padding(horizontal = (14 * u).dp, vertical = (12 * u).dp),
	) {
		Box(contentAlignment = Alignment.Center, modifier = Modifier.size((38 * u).dp).background(Disc.ChipBg, CircleShape)) {
			Text(
				text = spec.badge,
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp),
				color = Disc.BadgeInk,
			)
		}
		Column(verticalArrangement = Arrangement.spacedBy((2 * u).dp), modifier = Modifier.weight(1f)) {
			Text(
				text = spec.name,
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (13 * u).sp, lineHeight = (17 * u).sp),
				color = Color.White,
			)
			Text(
				text = spec.priceLine,
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (10 * u).sp, lineHeight = (13 * u).sp),
				color = Disc.Muted,
			)
		}
		Text(
			text = spec.change,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
			color = Disc.Green,
		)
	}
}

@Composable
private fun SheetCta(text: String, onClick: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.fillMaxWidth()
			.height((52 * u).dp)
			.background(CtaGradient, RoundedCornerShape((6 * u).dp))
			.border((0.36 * u).dp, CtaBorder, RoundedCornerShape((6 * u).dp))
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			),
	) {
		Text(
			text = text,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (14 * u).sp),
			color = Color.White,
		)
	}
}

@Composable
private fun SheetSecondary(text: String, onClick: () -> Unit) {
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
		Text(
			text = text,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = (14 * u).sp),
			color = Disc.Muted,
		)
	}
}

/** "Buy NVDA?" practice ticket (frame 1:1970, sheet 1:2159). */
@Composable
private fun PracticeBuySheet(onConfirm: () -> Unit, onDismiss: () -> Unit, spec: BuySpec = NVDA_BUY) {
	var selected by rememberSaveable { mutableIntStateOf(1) }
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	SheetScaffold(onDismiss = onDismiss) {
		Column(verticalArrangement = Arrangement.spacedBy((14 * u).dp), modifier = Modifier.fillMaxWidth()) {
			Text(
				text = spec.title,
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (18 * u).sp, lineHeight = (23 * u).sp),
				color = Color.White,
			)
			NvdaStockRow(spec)
			Text(
				text = "Your paper stake starts at today’s price and tracks the real move live, in either direction.",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp, lineHeight = (18 * u).sp),
				color = Disc.Body,
			)
			Column(verticalArrangement = Arrangement.spacedBy((12 * u).dp), modifier = Modifier.fillMaxWidth()) {
				Row(horizontalArrangement = Arrangement.spacedBy((6 * u).dp)) {
					Text(
						text = "Cash available",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
						color = Disc.Muted,
					)
					Text(
						text = spec.cashBefore,
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
						color = Disc.BrightInk,
					)
				}
				Row(horizontalArrangement = Arrangement.spacedBy((8 * u).dp), modifier = Modifier.fillMaxWidth()) {
					listOf("$10", "$25", "$50", "$100", "Custom").forEachIndexed { i, label ->
						val sel = i == selected
						Box(
							contentAlignment = Alignment.Center,
							modifier = Modifier
								.weight(1f)
								.clip(RoundedCornerShape((10 * u).dp))
								.background(if (sel) Disc.AmountSelBg else Disc.AmountBg)
								.border(
									if (sel) (0.5 * u).dp else (1 * u).dp,
									if (sel) Disc.AmountSelBorder else Disc.AmountBorder,
									RoundedCornerShape((10 * u).dp),
								)
								.clickable(
									interactionSource = remember { MutableInteractionSource() },
									indication = null,
								) { selected = i }
								.padding(vertical = (8 * u).dp),
						) {
							Text(
								text = label,
								style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
								color = if (sel) Disc.AmountSelInk else Disc.AmountInk,
							)
						}
					}
				}
			}
			Row(
				horizontalArrangement = Arrangement.spacedBy((6 * u).dp, Alignment.CenterHorizontally),
				verticalAlignment = Alignment.Bottom,
				// Authored: chips → shares line is a 24 gap (14 + 10).
				modifier = Modifier.fillMaxWidth().padding(top = (10 * u).dp),
			) {
				Text(
					text = "You get",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
					color = Disc.Muted,
				)
				Text(
					text = spec.shares,
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp, lineHeight = (19 * u).sp),
					color = Disc.BrightInk,
				)
				Text(
					text = "shares of ${spec.symbol}",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
					color = Disc.Muted,
				)
			}
			Column(verticalArrangement = Arrangement.spacedBy((16 * u).dp), modifier = Modifier.fillMaxWidth()) {
				SheetCta(text = "Confirm practice buy", onClick = onConfirm)
				SheetSecondary(text = "Not yet", onClick = onDismiss)
			}
		}
	}
}

/** "Order filled" sheet (frame 85:1205, sheet 85:1394). */
@Composable
private fun OrderFilledSheet(onDismiss: () -> Unit, spec: BuySpec = NVDA_BUY) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	SheetScaffold(onDismiss = onDismiss) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy((14 * u).dp),
			modifier = Modifier.fillMaxWidth(),
		) {
			Image(painterResource(R.drawable.ic_sheet_check), null, modifier = Modifier.size((47 * u).dp))
			Text(
				text = "Order filled",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (18 * u).sp, lineHeight = (23 * u).sp),
				color = Color.White,
			)
			NvdaStockRow(spec)
			Row(horizontalArrangement = Arrangement.spacedBy((6 * u).dp), modifier = Modifier.fillMaxWidth()) {
				Text(
					text = "Cash available",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
					color = Disc.Muted,
				)
				Text(
					text = spec.cashAfter,
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
					color = Disc.BrightInk,
				)
			}
			Row(
				horizontalArrangement = Arrangement.spacedBy((6 * u).dp, Alignment.CenterHorizontally),
				verticalAlignment = Alignment.Bottom,
				modifier = Modifier.fillMaxWidth(),
			) {
				Text(
					text = "You now hold",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
					color = Disc.Muted,
				)
				Text(
					text = spec.shares,
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp, lineHeight = (19 * u).sp),
					color = Disc.BrightInk,
				)
				Text(
					text = "shares of ${spec.symbol}",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
					color = Disc.Muted,
				)
			}
			Column(verticalArrangement = Arrangement.spacedBy((16 * u).dp), modifier = Modifier.fillMaxWidth()) {
				SheetCta(text = "View in My STAK", onClick = onDismiss)
				SheetSecondary(text = "Keep exploring", onClick = onDismiss)
			}
		}
	}
}

/** 44dp progress ring — #2a3346 track + #69b3ca arc from 12 o'clock. */
@Composable
private fun ProgressRing(progress: Float, u: Float) {
	Canvas(modifier = Modifier.size((44 * u).dp)) {
		val stroke = (4 * u).dp.toPx()
		val inset = (4 * u).dp.toPx()
		val arcSize = androidx.compose.ui.geometry.Size(size.width - inset * 2f, size.height - inset * 2f)
		drawArc(
			color = Color(0xFF2A3346),
			startAngle = 0f, sweepAngle = 360f, useCenter = false,
			topLeft = Offset(inset, inset), size = arcSize,
			style = Stroke(width = stroke, cap = StrokeCap.Round),
		)
		drawArc(
			color = Disc.Teal,
			startAngle = -90f, sweepAngle = 360f * progress, useCenter = false,
			topLeft = Offset(inset, inset), size = arcSize,
			style = Stroke(width = stroke, cap = StrokeCap.Round),
		)
	}
}

/** Discover · End of deck (CHINEDU 1:2330) — receipt stats + CTAs. */
@Composable
private fun EndOfDeck(onPracticeBuySaves: () -> Unit, onSwipeAgain: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier.fillMaxWidth().padding(horizontal = (20 * u).dp),
	) {
		// Authored column (1:2330): title box 190-218 (lh28), subtitle 226-242
		// (lh16), stats 274, CTA 396, review 457, footnote ~523.5, swipe 577.
		Spacer(modifier = Modifier.height((34 * u).dp))
		Text(
			text = "Deck complete",
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (22 * u).sp, lineHeight = (28 * u).sp),
			color = Disc.BrightInk,
		)
		Spacer(modifier = Modifier.height((8 * u).dp))
		Text(
			text = "Twelve cards, twelve signals. Your taste graph got smarter.",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
			color = Disc.Muted,
		)
		Spacer(modifier = Modifier.height((32 * u).dp))
		Row(horizontalArrangement = Arrangement.spacedBy((10 * u).dp)) {
			listOf("Seen" to "12", "Saved" to "7", "Bought" to "2").forEach { (label, value) ->
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.spacedBy((4 * u).dp),
					modifier = Modifier
						.width((110 * u).dp)
						.clip(RoundedCornerShape((12 * u).dp))
						.background(Disc.SheetBg)
						.padding(horizontal = (10 * u).dp, vertical = (14 * u).dp),
				) {
					Text(
						text = label,
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (10 * u).sp, lineHeight = (13 * u).sp),
						color = Disc.Muted,
					)
					Text(
						text = value,
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (20 * u).sp, lineHeight = (25 * u).sp),
						color = Disc.BrightInk,
					)
				}
			}
		}
		Spacer(modifier = Modifier.height((52 * u).dp))
		SheetCta(text = "Practice buy your saves", onClick = onPracticeBuySaves)
		Spacer(modifier = Modifier.height((9 * u).dp))
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.fillMaxWidth()
				.height((52 * u).dp)
				.border((0.36 * u).dp, Color(0x54343B4F), RoundedCornerShape((6 * u).dp))
				.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null,
				) { /* My STAK lands in a later phase. */ },
		) {
			Text(
				text = "Review saves in My STAK",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = (13 * u).sp),
				color = Disc.Muted,
			)
		}
		Spacer(modifier = Modifier.height((14 * u).dp))
		Text(
			text = "A new deck lands tomorrow with your morning brief.",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (10 * u).sp, lineHeight = (13 * u).sp),
			color = Disc.Muted,
		)
		Spacer(modifier = Modifier.height((40.5 * u).dp))
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.fillMaxWidth()
				.height((32 * u).dp)
				.clip(RoundedCornerShape((14 * u).dp))
				.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null,
					onClick = onSwipeAgain,
				),
		) {
			Text(
				text = "Swipe today’s deck again",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = (13 * u).sp),
				color = Disc.Muted,
			)
		}
	}
}

/** Buy → Order-filled flow, reused by the Stock Detail page. */
@Composable
internal fun DiscoverBuyFlow(onClose: () -> Unit, spec: BuySpec = NVDA_BUY) {
	var filled by rememberSaveable { mutableStateOf(false) }
	if (!filled) {
		PracticeBuySheet(onConfirm = { filled = true }, onDismiss = onClose, spec = spec)
	} else {
		OrderFilledSheet(onDismiss = onClose, spec = spec)
	}
}
