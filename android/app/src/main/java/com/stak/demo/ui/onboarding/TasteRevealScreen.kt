package com.stak.demo.ui.onboarding

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.ui.theme.Geist
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors
import com.stak.demo.ui.theme.ADVANCE_ROUNDING

/** One taste bar — label, strength word (its own gray), fill fraction of the track. */
private data class TasteBar(val label: String, val strength: String, val strengthColor: Color, val fraction: Float)

/** Figma fills 286/248/165/70 over the 310px track. */
private val BARS = listOf(
	TasteBar("Tech curious", "Strong", Color(0xFF69B3CA), 286f / 310f),
	TasteBar("Growth seeking", "Strong", Color(0xFF69B3CA), 248f / 310f),
	TasteBar("Consumer brands", "Medium", Color(0xFF819ABB), 165f / 310f),
	TasteBar("Income & dividends", "Light", Color(0xFF5C6B85), 70f / 310f),
)

/**
 * Onboarding · 07 Taste reveal — Figma node 1554:9051 ("STEP 6 OF 6").
 *
 * The quiz result: teal eyebrow, "Here's what you're into." headline,
 * the #181f30 taste card with four strength bars (fills animate in on
 * entry, landing on the exact Figma widths), the Risk style chip
 * ("Growth-Oriented" ›) and the "Lets go!" / Back CTAs.
 */
@Composable
fun TasteRevealScreen(onBack: () -> Unit, onLetsGo: () -> Unit) {
	val u = figmaUnit()
	var revealed by remember { mutableStateOf(false) }
	LaunchedEffect(Unit) { revealed = true }

	Artboard(modifier = Modifier.background(StakColors.Bg)) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.fillMaxWidth().padding(horizontal = (20 * u).dp).padding(top = (10 * u).dp, bottom = (4 * u).dp),
		) {
			AuthBackCircle(onClick = onBack)
			Spacer(modifier = Modifier.weight(1f))
			Text(
				text = "STEP 6 OF 6",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, letterSpacing = (0.9 * u + ADVANCE_ROUNDING.value).sp),
				color = Auth.FaintText,
			)
		}

		Column(
			verticalArrangement = Arrangement.spacedBy((16 * u).dp),
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth()
				.verticalScroll(rememberScrollState())
				.padding(horizontal = (24 * u).dp)
				.padding(top = (14 * u).dp),
		) {
			Text(
				text = "YOUR STARTING STAK TASTE",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, letterSpacing = (0.9 * u + ADVANCE_ROUNDING.value).sp),
				color = Auth.LinkTeal,
			)
			Column(verticalArrangement = Arrangement.spacedBy((12 * u).dp)) {
				Text(
					text = "Here’s what you’re into.",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (26 * u).sp, lineHeight = (33 * u).sp),
					color = StakColors.TextPrimary,
				)
				Text(
					text = "Built from your picks. It gets smarter with every swipe.",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp),
					color = Auth.SubtitleGray,
				)
			}

			// Taste card — four strength bars.
			Column(
				verticalArrangement = Arrangement.spacedBy((14 * u).dp),
				modifier = Modifier
					.fillMaxWidth()
					.background(Auth.InputBg, RoundedCornerShape((16 * u).dp))
					.padding((16 * u).dp),
			) {
				BARS.forEachIndexed { index, bar ->
					val fill by animateFloatAsState(
						targetValue = if (revealed) bar.fraction else 0f,
						animationSpec = tween(durationMillis = 550, delayMillis = 120 * index),
						label = "tasteFill$index",
					)
					Column(verticalArrangement = Arrangement.spacedBy((6 * u).dp)) {
						Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
							Text(
								text = bar.label,
								style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
								color = StakColors.TextPrimary,
								modifier = Modifier.weight(1f),
							)
							Text(
								text = bar.strength,
								style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, lineHeight = (16 * u).sp),
								color = bar.strengthColor,
							)
						}
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.height((5 * u).dp)
								.background(Auth.DividerLine, RoundedCornerShape((2.5 * u).dp)),
						) {
							Box(
								modifier = Modifier
									.fillMaxWidth(fill)
									.height((5 * u).dp)
									.background(Auth.LinkTeal, RoundedCornerShape((2.5 * u).dp)),
							)
						}
					}
				}
			}

			// Risk style chip.
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy((10 * u).dp),
				modifier = Modifier
					.fillMaxWidth()
					.background(Auth.InputBg, RoundedCornerShape((14 * u).dp))
					.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
					) { /* risk detail not designed yet */ }
					.padding(start = (16 * u).dp, end = (14 * u).dp, top = (13 * u).dp, bottom = (13 * u).dp),
			) {
				Column(verticalArrangement = Arrangement.spacedBy((2 * u).dp), modifier = Modifier.weight(1f)) {
					Text(
						text = "Risk style",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (11 * u).sp),
						color = StakColors.Muted,
					)
					Text(
						text = "Growth-Oriented",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (14 * u).sp),
						color = StakColors.TextPrimary,
					)
				}
				Text(
					text = "›",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (15 * u).sp),
					color = Auth.LinkTeal,
				)
			}

			Text(
				text = "Your deck adjusts as you swipe.",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (11 * u).sp, textAlign = TextAlign.Center),
				color = Auth.FaintText,
				modifier = Modifier.fillMaxWidth(),
			)
		}

		Column(
			verticalArrangement = Arrangement.spacedBy((10 * u).dp),
			modifier = Modifier.fillMaxWidth().padding(top = (8 * u).dp, bottom = (26 * u).dp),
		) {
			AuthCta(text = "Lets go!", onClick = onLetsGo)
			AuthSecondaryButton(text = "Back", onClick = onBack)
		}
	}
}
