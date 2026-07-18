package com.stak.demo.ui.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.ui.components.BackChevron
import com.stak.demo.ui.components.StakPrimaryButton
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors

/**
 * Onboarding · Quiz 1-4 — Figma nodes 22:1562, 22:1582, 22:1611, 22:1631
 * (file tzBCgpuPAFGnipu7NtwH3z).
 *
 * One composable renders all four quiz steps: back chevron, a 4px
 * accent progress bar filled to step/4, "Step N of 4" label, the
 * question, selectable option rows (step 2 is multi-select with a
 * "Pick a few." subtitle) and a bottom-pinned gradient "Continue" CTA.
 * Steps start unselected; Continue enables once the user picks.
 */
@Composable
fun QuizScreen(step: Int, onBack: () -> Unit, onContinue: () -> Unit) {
	val stepIndex = (step - 1).coerceIn(0, QUIZ_STEPS.lastIndex)
	val data = QUIZ_STEPS[stepIndex]
	var selection by rememberSaveable(stepIndex) { mutableStateOf(emptyList<Int>()) }

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(StakColors.Bg)
			.systemBarsPadding(),
	) {
		Column(
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth()
				.verticalScroll(rememberScrollState())
				.padding(horizontal = 20.dp),
		) {
			// Back chevron — Figma y55 (~8dp below the system bar).
			Spacer(modifier = Modifier.height(8.dp))
			BackChevron(onClick = onBack)
			Spacer(modifier = Modifier.height(4.dp))

			// Progress — 4px track white12%, accent fill at step/4 (Figma y84).
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(4.dp)
					.background(StakColors.BorderMid, RoundedCornerShape(2.dp)),
			) {
				Box(
					modifier = Modifier
						.fillMaxWidth((stepIndex + 1) / QUIZ_STEPS.size.toFloat())
						.height(4.dp)
						.background(StakColors.Accent, RoundedCornerShape(2.dp)),
				)
			}
			Spacer(modifier = Modifier.height(10.dp))

			// "Step N of 4" — Sora Regular 12 muted (Figma y98).
			Text(
				text = "Step ${stepIndex + 1} of ${QUIZ_STEPS.size}",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 12.sp),
				color = StakColors.Muted,
			)
			Spacer(modifier = Modifier.height(13.dp))

			// Question — Sora SemiBold 22 white, 1.28 line height (Figma y126).
			Text(
				text = data.question,
				style = TextStyle(
					fontFamily = Sora,
					fontWeight = FontWeight.SemiBold,
					fontSize = 22.sp,
					lineHeight = 28.16.sp,
				),
				color = StakColors.TextPrimary,
				modifier = Modifier.fillMaxWidth(),
			)
			Spacer(modifier = Modifier.height(data.gapAfterQuestion))

			// Optional "Pick a few." subtitle — Sora Regular 14 muted (quiz 2, Figma y186).
			if (data.subtitle != null) {
				Text(
					text = data.subtitle,
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 14.sp),
					color = StakColors.Muted,
				)
				Spacer(modifier = Modifier.height(10.dp))
			}

			// Option rows — 56dp r14 #172037, 12dp apart (Figma y206 / y214).
			data.options.forEachIndexed { index, option ->
				if (index > 0) {
					Spacer(modifier = Modifier.height(12.dp))
				}
				QuizOptionRow(
					text = option,
					selected = index in selection,
					onClick = {
						selection = if (data.multiSelect) {
							if (index in selection) selection - index else selection + index
						} else {
							listOf(index)
						}
					},
				)
			}
			Spacer(modifier = Modifier.height(24.dp))
		}

		// Bottom-pinned gradient CTA (Figma y760, 30px bottom margin).
		StakPrimaryButton(
			text = "Continue",
			onClick = onContinue,
			enabled = selection.isNotEmpty(),
			modifier = Modifier.padding(horizontal = 20.dp),
		)
		Spacer(modifier = Modifier.height(30.dp))
	}
}

/**
 * One selectable answer row. Selected: 2dp accent border, SemiBold label
 * and a filled accent circle with a dark check. Unselected: 1dp
 * rgba(255,255,255,0.08) hairline, Regular label and a hollow circle.
 */
@Composable
private fun QuizOptionRow(
	text: String,
	selected: Boolean,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val shape = RoundedCornerShape(14.dp)
	Row(
		modifier = modifier
			.fillMaxWidth()
			.height(56.dp)
			.background(StakColors.SurfaceAlt, shape)
			.border(
				width = if (selected) 2.dp else 1.dp,
				color = if (selected) StakColors.Accent else StakColors.Divider,
				shape = shape,
			)
			.clickable(onClick = onClick)
			.padding(start = 22.dp, end = 18.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		Text(
			text = text,
			style = TextStyle(
				fontFamily = Sora,
				fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
				fontSize = 15.sp,
			),
			color = StakColors.TextPrimary,
			modifier = Modifier.weight(1f),
		)
		SelectionCircle(selected = selected)
	}
}

/**
 * 24dp trailing selector from the quiz frames: filled #39c5cb circle
 * with a 9x7 dark check when selected, dim hollow ring otherwise.
 */
@Composable
private fun SelectionCircle(selected: Boolean, modifier: Modifier = Modifier) {
	Canvas(modifier = modifier.size(24.dp)) {
		if (selected) {
			drawCircle(color = StakColors.Accent)
			val check = Path().apply {
				moveTo(center.x - 4.5f.dp.toPx(), center.y - 0.5f.dp.toPx())
				lineTo(center.x - 1.5f.dp.toPx(), center.y + 2.5f.dp.toPx())
				lineTo(center.x + 4.5f.dp.toPx(), center.y - 4.5f.dp.toPx())
			}
			drawPath(
				path = check,
				color = StakColors.Bg,
				style = Stroke(
					width = 2.dp.toPx(),
					cap = StrokeCap.Round,
					join = StrokeJoin.Round,
				),
			)
		} else {
			drawCircle(
				color = StakColors.BorderMid,
				radius = size.minDimension / 2f - 0.75f.dp.toPx(),
				style = Stroke(width = 1.5f.dp.toPx()),
			)
		}
	}
}

/** One quiz step: question, options and the layout deltas that differ per frame. */
private data class QuizStep(
	val question: String,
	val options: List<String>,
	val gapAfterQuestion: Dp,
	val subtitle: String? = null,
	val multiSelect: Boolean = false,
)

/** The four onboarding quiz frames, in order (Figma 22:1562 / 22:1582 / 22:1611 / 22:1631). */
private val QUIZ_STEPS = listOf(
	QuizStep(
		question = "How much investing have you done?",
		options = listOf(
			"I'm brand new to this",
			"I've dabbled a bit",
			"I know my way around",
		),
		gapAfterQuestion = 24.dp,
	),
	QuizStep(
		question = "What catches your eye?",
		options = listOf(
			"Tech & innovation",
			"Brands you know",
			"High growth",
			"Income & dividends",
			"Speculative plays",
		),
		gapAfterQuestion = 32.dp,
		subtitle = "Pick a few.",
		multiSelect = true,
	),
	QuizStep(
		question = "A stock you own drops 20% in a week. You…",
		options = listOf(
			"Sell to cut the loss",
			"Hold and wait it out",
			"Buy more on the dip",
		),
		gapAfterQuestion = 24.dp,
	),
	QuizStep(
		question = "What are you here for?",
		options = listOf(
			"Learn the ropes",
			"Grow over time",
			"Find the next big winner",
		),
		gapAfterQuestion = 52.dp,
	),
)
