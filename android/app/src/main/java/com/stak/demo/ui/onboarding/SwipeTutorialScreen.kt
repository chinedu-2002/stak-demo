package com.stak.demo.ui.onboarding

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.R
import com.stak.demo.ui.discover.DECK
import com.stak.demo.ui.discover.DeckRowTweaks
import com.stak.demo.ui.discover.FrontDeckCard
import com.stak.demo.ui.theme.Geist
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors
import kotlin.math.roundToInt
import kotlinx.coroutines.launch
import com.stak.demo.ui.theme.ADVANCE_ROUNDING

// The tutorial deck (1:344) is the Discover deck at 87.4% — the same
// authored queue slabs behind a live front card built from the shared
// Discover card template. Slab poses template-matched to the frame.
private const val DECK_SCALE = 305.75f / 350f

/**
 * Onboarding · 03 Swipe tutorial — Figma node 1:344 (CHINEDU file, "STEP 3 OF 6").
 *
 * The stacked swipe deck: the authored queue slabs (AAPL and GOOGL at
 * their designed tilts) behind a live front card from the Discover
 * template at the frame's 87.4% scale. Swiping down reshuffles: the
 * front card flies off to the back of the queue and the next design
 * takes the front row, cycling in order. Chevrons + "Swipe down" hint
 * under the stack, Continue/Back below.
 */
@Composable
fun SwipeTutorialScreen(onBack: () -> Unit, onContinue: () -> Unit) {
	var swiped by rememberSaveable { mutableIntStateOf(0) }
	val topOffset = remember(swiped) { Animatable(0f) }
	val promote = remember(swiped) { Animatable(0f) }
	val enter = remember(swiped) { Animatable(if (swiped == 0) 1f else 0f) }
	LaunchedEffect(swiped) { if (enter.value < 1f) enter.animateTo(1f, tween(200, easing = EaseOut)) }
	val scope = rememberCoroutineScope()
	val density = LocalDensity.current
	val u = figmaUnit()
	val u2 = u * DECK_SCALE

	Artboard(modifier = Modifier.background(StakColors.Bg)) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.fillMaxWidth().padding(horizontal = (20 * u).dp).padding(top = (10 * u).dp, bottom = (4 * u).dp),
		) {
			AuthBackCircle(onClick = onBack)
			Spacer(modifier = Modifier.weight(1f))
			Text(
				text = "STEP 3 OF 6",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, letterSpacing = (0.9 * u + ADVANCE_ROUNDING.value).sp),
				color = Auth.FaintText,
			)
		}

		Column(
			verticalArrangement = Arrangement.spacedBy((18 * u).dp),
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth()
				.padding(horizontal = (24 * u).dp)
				.padding(top = (14 * u).dp),
		) {
			Column(verticalArrangement = Arrangement.spacedBy((12 * u).dp)) {
				Text(
					text = "Now try a few swipes.",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (24 * u).sp, lineHeight = (31 * u).sp, letterSpacing = ADVANCE_ROUNDING),
					color = StakColors.TextPrimary,
				)
				Text(
					text = "Swipe down for the next card. Save what you like.",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp, letterSpacing = ADVANCE_ROUNDING),
					color = Auth.SubtitleGray,
				)
			}

			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				modifier = Modifier.weight(1f).fillMaxWidth().padding(top = (10 * u).dp),
			) {
				// The deck — the authored queue slabs stay put while the live
				// front card cycles. User's motion (2026-08-21): swipe down
				// reshuffles the front card to the back and the next takes the
				// front row in an organized sequence — the Discover grammar.
				Box(
					modifier = Modifier
						.size((306 * u).dp, (423.07 * u).dp)
						.clipToBounds()
						.pointerInput(swiped) {
							detectVerticalDragGestures(
								onDragEnd = {
									scope.launch {
										if (topOffset.value > with(density) { (110 * u2).dp.toPx() }) {
											launch { topOffset.animateTo(with(density) { (500 * u2).dp.toPx() }, tween(280, easing = EaseOut)) }
											promote.animateTo(1f, tween(300, easing = EaseOut))
											swiped += 1
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
					val p = promote.value
					Image(
						painter = painterResource(R.drawable.tutorial_card_googl),
						contentDescription = null,
						modifier = Modifier.offset(x = (33.5 * u).dp, y = 0.dp).size((238.75 * u).dp, (290.75 * u).dp),
					)
					Image(
						painter = painterResource(R.drawable.tutorial_card_aapl),
						contentDescription = null,
						modifier = Modifier.offset(x = (15.5 * u).dp, y = (21 * u).dp).size((273.5 * u).dp, (309.5 * u).dp),
					)
					FrontDeckCard(
						card = DECK[swiped % 3],
						onSave = {},
						u = u2,
						// This frame's card authors looser text gaps than a
						// uniform 87.4% scale of the Discover card (1:1627) —
						// render-fitted against the 2x export of 1:344.
						rows = DeckRowTweaks(overlay = 0.85f, headline = 0.85f, price = 1.65f, tip = 0.4f),
						modifier = Modifier
							.align(Alignment.TopCenter)
							.offset(y = (47.5 * u).dp)
							.offset { IntOffset(0, topOffset.value.roundToInt()) }
							.graphicsLayer {
								alpha = (1f - p) * enter.value
								val s = 0.97f + 0.03f * enter.value
								scaleX = s
								scaleY = s
							},
					)
				}
				Spacer(modifier = Modifier.size((9 * u).dp))
				// Gesture hint — twin chevrons at 50% + "Swipe down".
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.spacedBy((4 * u).dp),
				) {
					Column(
						verticalArrangement = Arrangement.spacedBy((1 * u).dp),
						modifier = Modifier.alpha(0.5f),
					) {
						ChevronDown(u)
						ChevronDown(u)
					}
					Text(
						text = "Swipe down",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (8.73 * u).sp, letterSpacing = ADVANCE_ROUNDING),
						color = Auth.FaintText,
					)
				}
			}
		}

		Column(
			verticalArrangement = Arrangement.spacedBy((10 * u).dp),
			modifier = Modifier.fillMaxWidth().padding(top = (8 * u).dp, bottom = (26 * u).dp),
		) {
			AuthCta(text = "Continue", onClick = onContinue)
			AuthSecondaryButton(text = "Back", onClick = onBack)
		}
	}
}

/**
 * The authored 13.97x6.98 down-chevron (1:487 / 1:489): the exported
 * vector - stroke #5C6B85 at 1.75, round caps, the glyph inset inside
 * its frame. Was a hand-drawn corner-to-corner #819ABB 1.6 stroke
 * (exact-design audit 2026-09-04).
 */
@Composable
private fun ChevronDown(u: Float) {
	Image(
		painter = painterResource(R.drawable.ic_swipe_chevron),
		contentDescription = null,
		modifier = Modifier.size((13.97 * u).dp, (6.98 * u).dp),
	)
}
