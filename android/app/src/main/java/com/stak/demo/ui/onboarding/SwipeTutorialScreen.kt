package com.stak.demo.ui.onboarding

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.R
import com.stak.demo.ui.theme.Geist
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

/** The three designed tutorial cards, front to back, in their Figma poses. */
private data class TutorialCard(val res: Int, val x: Float, val y: Float, val w: Float, val h: Float)

private val CARDS = listOf(
	TutorialCard(R.drawable.tutorial_card_nvda, 0f, 44.35f, 284f, 350.67f),
	TutorialCard(R.drawable.tutorial_card_aapl, 14.49f, 19.64f, 254.99f, 308.48f),
	TutorialCard(R.drawable.tutorial_card_googl, 31.21f, 0f, 222.08f, 273.53f),
)

/**
 * Onboarding · 03 Swipe tutorial — Figma node 1554:8666 ("STEP 3 OF 6").
 *
 * The stacked swipe deck: NVDA in front with AAPL and GOOGL peeking
 * behind at their designed tilts (each card is its full Figma render).
 * Swiping down flings the front card away to reveal the next; after all
 * three the deck resets so the user can keep practicing. Chevrons +
 * "Swipe down" hint under the stack, Continue/Back below.
 */
@Composable
fun SwipeTutorialScreen(onBack: () -> Unit, onContinue: () -> Unit) {
	var swiped by rememberSaveable { mutableIntStateOf(0) }
	val topOffset = remember(swiped) { Animatable(0f) }
	val scope = rememberCoroutineScope()
	val density = LocalDensity.current

	Column(modifier = Modifier.fillMaxSize().background(StakColors.Bg).systemBarsPadding()) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(top = 10.dp, bottom = 4.dp),
		) {
			AuthBackCircle(onClick = onBack)
			Spacer(modifier = Modifier.weight(1f))
			Text(
				text = "STEP 3 OF 6",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 10.sp, letterSpacing = 0.9.sp),
				color = Auth.FaintText,
			)
		}

		Column(
			verticalArrangement = Arrangement.spacedBy(18.dp),
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth()
				.padding(horizontal = 24.dp)
				.padding(top = 14.dp),
		) {
			Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
				Text(
					text = "Now try a few swipes.",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 24.sp, lineHeight = 31.sp),
					color = StakColors.TextPrimary,
				)
				Text(
					text = "Swipe down for the next card. Save what you like.",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 12.sp),
					color = Auth.SubtitleGray,
				)
			}

			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				modifier = Modifier.weight(1f).fillMaxWidth().padding(top = 6.dp),
			) {
				Spacer(modifier = Modifier.weight(1f))
				// The deck — cards keep their designed poses; the front one drags down.
				Box(
					modifier = Modifier
						.size(284.dp, 394.dp)
						.pointerInput(swiped) {
							detectVerticalDragGestures(
								onDragEnd = {
									scope.launch {
										if (topOffset.value > with(density) { 110.dp.toPx() }) {
											topOffset.animateTo(with(density) { 700.dp.toPx() }, tween(220))
											swiped = (swiped + 1) % (CARDS.size + 1) // 3 swipes, then reset
										} else {
											topOffset.animateTo(0f, tween(180))
										}
									}
								},
							) { change, dragAmount ->
								change.consume()
								if (dragAmount > 0f || topOffset.value > 0f) {
									scope.launch {
										topOffset.snapTo((topOffset.value + dragAmount).coerceAtLeast(0f))
									}
								}
							}
						},
				) {
					val remaining = CARDS.drop(swiped)
					remaining.asReversed().forEachIndexed { index, card ->
						val isTop = index == remaining.lastIndex
						Image(
							painter = painterResource(card.res),
							contentDescription = null,
							modifier = Modifier
								.offset(x = card.x.dp, y = card.y.dp)
								.size(card.w.dp, card.h.dp)
								.then(
									if (isTop) Modifier.offset { IntOffset(0, topOffset.value.roundToInt()) } else Modifier,
								),
						)
					}
				}
				Spacer(modifier = Modifier.size(8.dp))
				// Gesture hint — twin chevrons at 50% + "Swipe down".
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.spacedBy(6.dp),
				) {
					Column(
						verticalArrangement = Arrangement.spacedBy(0.8.dp),
						modifier = Modifier.alpha(0.5f),
					) {
						ChevronDown()
						ChevronDown()
					}
					Text(
						text = "Swipe down",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 12.sp),
						color = Auth.FaintText,
					)
				}
				Spacer(modifier = Modifier.weight(1f))
			}
		}

		Column(
			verticalArrangement = Arrangement.spacedBy(10.dp),
			modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 26.dp),
		) {
			AuthCta(text = "Continue", onClick = onContinue)
			AuthSecondaryButton(text = "Back", onClick = onBack)
		}
	}
}

/** 12.98x6.49 down-chevron stroke, StakColors.Muted. */
@Composable
private fun ChevronDown() {
	Canvas(modifier = Modifier.size(12.98.dp, 6.49.dp)) {
		val p = Path().apply {
			moveTo(0f, 0f)
			lineTo(size.width / 2f, size.height)
			lineTo(size.width, 0f)
		}
		drawPath(
			p,
			StakColors.Muted,
			style = Stroke(width = 1.6.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
		)
	}
}
