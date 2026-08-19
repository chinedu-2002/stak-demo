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
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.geometry.Offset
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
private data class DeckCard(
	val artRes: Int,
	val ticker: String,
	val headline: String,
	val price: String,
	val change: String,
	val tip: String,
	val cardTop: Color,
	val artBg: Color,
	// Authored art frame at front scale — each card frames its art
	// differently (NVDA inset 340x229; AAPL/GOOGL full-bleed, taller).
	val artW: Float,
	val artH: Float,
	val artY: Float,
	val artRadius: Float,
)

private val DECK = listOf(
	DeckCard(
		R.drawable.disc_card_nvda, "NVDA · NVIDIA Corp",
		"Chip demand is outrunning supply, and NVIDIA sets the prices.",
		"$122.10", "▲ 2.4% today", "Chip stocks swing hard. Small stakes, long views.",
		Color(0xFF152A47), Color(0xFF142844), 340f, 229f, 4f, 18f,
	),
	DeckCard(
		R.drawable.disc_card_aapl, "AAPL · Apple Inc",
		"Two billion devices, and every one of them keeps paying Apple.",
		"$229.35", "▲ 1.2% today", "Steady giants move slower. Stable stocks often do.",
		Color(0xFF283E5D), Color(0xFF253A59), 350f, 243f, 3.5f, 22f,
	),
	DeckCard(
		R.drawable.disc_card_googl, "GOOGL · Alphabet Inc",
		"Search pays for everything, and nine billion-user products ride behind it.",
		"$178.90", "▲ 0.8% today", "Ad money moves with the economy, so some quarters just drift.",
		Color(0xFF263D5D), Color(0xFF2F486E), 350f, 248.6f, 5.6f, 22f,
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
	var releaseY by remember { mutableFloatStateOf(54.65f) }
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
											// Card shuffle: the swiped card dives under the deck
											// and rises into the back of the queue while the
											// others step forward into the vacated slots.
											releaseY = 54.65f + topOffset.value / density.density / u
											promote.animateTo(1f, tween(450, easing = EaseOut))
											seen += 1
										} else {
											topOffset.animateTo(0f, tween(180))
										}
									}
								},
							) { change, dragAmount ->
								change.consume()
								if (promote.value == 0f && (dragAmount > 0f || topOffset.value > 0f)) {
									scope.launch {
										topOffset.snapTo((topOffset.value + dragAmount).coerceAtLeast(0f))
									}
								}
							}
						},
				) {
					val order = listOf(DECK[(seen + 2) % 3], DECK[(seen + 1) % 3], DECK[seen % 3])
					val p = promote.value
					fun step(a: Float, b: Float) = a + (b - a) * p
					// The swiped card's two-segment return flight: dive below
					// the deck, then rise into the back-top slot (behind).
					fun seg(a: Float, m: Float, b: Float) =
						if (p < 0.5f) a + (m - a) * (p / 0.5f) else m + (b - m) * ((p - 0.5f) / 0.5f)
					val flyScale = seg(1f, 0.86f, 251.81f / 350f)
					val flyRot = seg(0f, 2.5f, 4.03f)
					val flyX = seg(0f, 0.15f, 0.29f)
					val flyY = seg(releaseY, 150f, -53.85f)
					val flyH = seg(430f, 437f, 444.4f)
					// The queue always shows the ACTUAL next cards, live —
					// at rest and through the shuffle (no duplicated designs).
					if (p >= 0.5f) {
						// Past halfway the swiped card has tucked under —
						// drawn deepest, slotting in at the back of the queue.
						BackDeckCard(order[2], scale = flyScale, rotation = flyRot, offsetX = (flyX * u).dp, offsetY = (flyY * u).dp, u = u, authoredHeight = flyH)
					}
					BackDeckCard(
						order[0],
						scale = step(251.81f / 350f, 299.51f / 350f),
						rotation = step(4.03f, -2.33f),
						offsetX = (step(0.29f, -0.58f) * u).dp,
						offsetY = (step(-53.85f, 1.4f) * u).dp,
						u = u,
						authoredHeight = step(444.4f, 398.5f),
					)
					BackDeckCard(
						order[1],
						scale = step(299.51f / 350f, 1f),
						rotation = step(-2.33f, 0f),
						offsetX = (step(-0.58f, 0f) * u).dp,
						offsetY = (step(1.4f, 54.65f) * u).dp,
						u = u,
						authoredHeight = step(398.5f, 430f),
					)
					if (p > 0f && p < 0.5f) {
						BackDeckCard(order[2], scale = flyScale, rotation = flyRot, offsetX = (flyX * u).dp, offsetY = (flyY * u).dp, u = u, authoredHeight = flyH)
					}
					if (p == 0f) {
						FrontDeckCard(
							card = order[2],
							onSave = { savedToast = true },
							u = u,
							modifier = Modifier
								.align(Alignment.TopCenter)
								.offset(y = (54.65 * u).dp)
								.offset { androidx.compose.ui.unit.IntOffset(0, topOffset.value.roundToInt()) }
								.clickable(
									interactionSource = remember { MutableInteractionSource() },
									indication = null,
									onClick = onLearnMore,
								),
						)
					}
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

/** One of the two tilted back cards, drawn at its designed scale. */
@Composable
private fun BoxScope.BackDeckCard(card: DeckCard, scale: Float, rotation: Float, offsetX: Dp, offsetY: Dp, u: Float, authoredHeight: Float, alpha: Float = 1f) {
	// The card composable is authored at the 350x444.4 front size and
	// scaled down; the offsets place the rotated bounds so the card tops
	// peek exactly as in the frame (GOOGL at deck-y 0, AAPL at 24.2).
	Box(
		modifier = Modifier
			.align(Alignment.TopStart)
			.offset(x = offsetX, y = offsetY)
			.size((350 * u).dp, (authoredHeight * u).dp)
			.graphicsLayer {
				scaleX = scale
				scaleY = scale
				rotationZ = rotation
				this.alpha = alpha
			},
	) {
		DeckCardBody(card = card, onSave = null, u = u)
	}
}

/** The full-size front card (350 wide) with its live Save chip. */
@Composable
private fun FrontDeckCard(card: DeckCard, onSave: () -> Unit, u: Float, modifier: Modifier = Modifier) {
	Box(modifier = modifier.width((350 * u).dp)) {
		DeckCardBody(card = card, onSave = onSave, u = u)
	}
}

@Composable
private fun DeckCardBody(card: DeckCard, onSave: (() -> Unit)?, u: Float) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		// The overlay sits at the authored 258 for every card, whatever
		// its art height: gap = 258 - artY - artH (NVDA: 25).
		verticalArrangement = Arrangement.spacedBy(((258f - card.artY - card.artH) * u).dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((22 * u).dp))
			.background(Brush.verticalGradient(0f to card.cardTop, 1f to Color(0xFF0C1526)))
			.padding(top = (card.artY * u).dp, bottom = (4 * u).dp),
	) {
		Box(
			modifier = Modifier
				.size((card.artW * u).dp, (card.artH * u).dp)
				.clip(RoundedCornerShape((card.artRadius * u).dp))
				.background(card.artBg),
		) {
			Image(
				painter = painterResource(card.artRes),
				contentDescription = null,
				contentScale = ContentScale.Crop,
				modifier = Modifier.size((card.artW * u).dp, (card.artH * u).dp),
			)
			if (card.artRes != R.drawable.disc_card_nvda) {
				// NVDA's chip is baked into its art; the others draw it live —
				// card-relative (top 10, end 9) like the baked one.
				SaveChip(u = u, modifier = Modifier.align(Alignment.TopEnd).padding(top = ((10f - card.artY) * u).dp, end = (9 * u).dp))
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
				.clip(RoundedCornerShape(topStart = (24 * u).dp, topEnd = (24 * u).dp))
				.background(Disc.SheetBg)
				.padding(horizontal = (20 * u).dp)
				.padding(top = (10 * u).dp)
				.navigationBarsPadding()
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
		Spacer(modifier = Modifier.height((47 * u).dp))
		Text(
			text = "Deck complete",
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (22 * u).sp),
			color = Disc.BrightInk,
		)
		Spacer(modifier = Modifier.height((14 * u).dp))
		Text(
			text = "Twelve cards, twelve signals. Your taste graph got smarter.",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp),
			color = Disc.Muted,
		)
		Spacer(modifier = Modifier.height((33 * u).dp))
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
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (10 * u).sp),
						color = Disc.Muted,
					)
					Text(
						text = value,
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (20 * u).sp),
						color = Disc.BrightInk,
					)
				}
			}
		}
		Spacer(modifier = Modifier.height((56 * u).dp))
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
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (10 * u).sp),
			color = Disc.Muted,
		)
		Spacer(modifier = Modifier.height((22 * u).dp))
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
