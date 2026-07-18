package com.stak.demo.ui.onboarding

import androidx.compose.runtime.Composable
import com.stak.demo.R

/**
 * Onboarding · 04 Goal · matrix — Figma node 1554:8828 ("STEP 4 OF 6").
 * Uses the shared 2x2 matrix quiz layout.
 */
@Composable
fun GoalScreen(onBack: () -> Unit, onContinue: () -> Unit) {
	MatrixQuizScreen(
		stepLabel = "STEP 4 OF 6",
		headline = "What brings you here?",
		subtitle = "Pick one. You can change it any time.",
		options = listOf(
			MatrixOption("Learn how investing works", "Start from the basics, no shame", R.drawable.ic_goal_learn),
			MatrixOption("Grow my money long-term", "Slow and steady wealth", R.drawable.ic_goal_grow),
			MatrixOption("Find my first stocks", "I want to understand what to watch", R.drawable.ic_goal_first),
			MatrixOption("Just exploring", "Curious, no plan yet", R.drawable.ic_goal_explore),
		),
		onBack = onBack,
		onContinue = onContinue,
	)
}
