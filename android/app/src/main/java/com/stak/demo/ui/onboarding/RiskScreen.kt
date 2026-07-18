package com.stak.demo.ui.onboarding

import androidx.compose.runtime.Composable
import com.stak.demo.R

/**
 * Onboarding · 05 Risk matrix — Figma node 1554:8913 ("STEP 5 OF 6").
 * Shares the matrix layout with step 04; the book and sprout icons are
 * the same drawables, plus the pause and shield-check icons.
 */
@Composable
fun RiskScreen(onBack: () -> Unit, onContinue: () -> Unit) {
	MatrixQuizScreen(
		stepLabel = "STEP 5 OF 6",
		headline = "A stock you’re watching drops 10% overnight.",
		subtitle = "No wrong answer. This helps STAK understand your risk style.",
		options = listOf(
			MatrixOption("Buy more after checking why", "Comfortable with dips if the story holds", R.drawable.ic_goal_learn),
			MatrixOption("Hold and watch it closely", "I can handle short-term drops", R.drawable.ic_goal_grow),
			MatrixOption("Step away for now", "Big drops make me uncomfortable", R.drawable.ic_risk_pause),
			MatrixOption("Sell some, reduce risk", "I’d rather protect part of my money", R.drawable.ic_risk_shield),
		),
		onBack = onBack,
		onContinue = onContinue,
	)
}
