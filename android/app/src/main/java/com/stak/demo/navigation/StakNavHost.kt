package com.stak.demo.navigation

import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stak.demo.ui.components.StakTab
import com.stak.demo.ui.components.StakTabBar
import com.stak.demo.ui.onboarding.CreateAccountScreen
import com.stak.demo.ui.onboarding.SplashScreen
import com.stak.demo.ui.onboarding.SignInScreen
import com.stak.demo.ui.onboarding.IntroScreen
import com.stak.demo.ui.onboarding.BrandPicksScreen
import com.stak.demo.ui.onboarding.SwipeTutorialScreen
import com.stak.demo.ui.onboarding.GoalScreen
import com.stak.demo.ui.onboarding.RiskScreen
import com.stak.demo.ui.onboarding.PermissionsScreen
import com.stak.demo.ui.onboarding.PreparingDeckScreen
import com.stak.demo.ui.onboarding.ProfileSetupScreen
import com.stak.demo.ui.onboarding.TasteRevealScreen
import com.stak.demo.ui.theme.StakColors

/**
 * Root of the app: splash → auth → onboarding → the bottom-tab shell.
 *
 * Prototype-confirmed edges (CHINEDU proto panel): splash auto-advances
 * to Auth · Sign up after 1200ms with a 350ms ease-out dissolve. The
 * post-auth ordering (create account → 01 Welcome → … → 07 Taste reveal
 * → 08 Permissions → 09 Profile setup → shell) follows the canvas order
 * pending per-frame prototype confirmation.
 */
@Composable
fun StakRoot(navController: NavHostController = rememberNavController()) {
	NavHost(
		navController = navController,
		startDestination = StakRoutes.SPLASH,
	) {
		composable(
			StakRoutes.SPLASH,
			// Prototype: dissolve, ease out, 350ms.
			exitTransition = { fadeOut(tween(350, easing = EaseOut)) },
		) {
			SplashScreen(
				onContinue = {
					navController.navigate(StakRoutes.CREATE_ACCOUNT) {
						popUpTo(StakRoutes.SPLASH) { inclusive = true }
					}
				},
			)
		}
		composable(StakRoutes.INTRO) {
			IntroScreen(onGetStarted = { navController.navigate(StakRoutes.BRAND_PICKS) })
		}
		composable(StakRoutes.BRAND_PICKS) {
			BrandPicksScreen(
				onBack = { navController.popBackStack() },
				onContinue = { navController.navigate(StakRoutes.SWIPE_TUTORIAL) },
			)
		}
		composable(StakRoutes.SWIPE_TUTORIAL) {
			SwipeTutorialScreen(
				onBack = { navController.popBackStack() },
				onContinue = { navController.navigate(StakRoutes.GOAL) },
			)
		}
		composable(StakRoutes.GOAL) {
			GoalScreen(
				onBack = { navController.popBackStack() },
				onContinue = { navController.navigate(StakRoutes.RISK) },
			)
		}
		composable(StakRoutes.RISK) {
			RiskScreen(
				onBack = { navController.popBackStack() },
				onContinue = { navController.navigate(StakRoutes.PREPARING_DECK) },
			)
		}
		composable(StakRoutes.PREPARING_DECK) {
			PreparingDeckScreen(
				onDone = {
					navController.navigate(StakRoutes.TASTE_REVEAL) {
						popUpTo(StakRoutes.PREPARING_DECK) { inclusive = true }
					}
				},
			)
		}
		composable(StakRoutes.TASTE_REVEAL) {
			TasteRevealScreen(
				onBack = { navController.popBackStack() },
				onLetsGo = { navController.navigate(StakRoutes.PERMISSIONS) },
			)
		}
		composable(
			StakRoutes.CREATE_ACCOUNT,
			// Prototype: splash dissolves into sign up (350ms ease out).
			enterTransition = {
				if (initialState.destination.route == StakRoutes.SPLASH) {
					fadeIn(tween(350, easing = EaseOut))
				} else {
					null
				}
			},
		) {
			CreateAccountScreen(
				onCreateAccount = { navController.navigate(StakRoutes.INTRO) },
				onLogIn = { navController.navigate(StakRoutes.SIGN_IN) },
			)
		}
		composable(StakRoutes.PERMISSIONS) {
			PermissionsScreen(
				onBack = { navController.popBackStack() },
				onContinue = { navController.navigate(StakRoutes.PROFILE_SETUP) },
			)
		}
		composable(StakRoutes.PROFILE_SETUP) {
			ProfileSetupScreen(
				onBack = { navController.popBackStack() },
				onProceed = {
					navController.navigate(StakRoutes.MAIN) {
						popUpTo(0) { inclusive = true }
					}
				},
			)
		}
		composable(StakRoutes.SIGN_IN) {
			SignInScreen(
				onBack = { navController.popBackStack() },
				onSignIn = {
					navController.navigate(StakRoutes.MAIN) {
						popUpTo(0) { inclusive = true }
					}
				},
				onCreateAccount = { navController.popBackStack() },
			)
		}
		composable(StakRoutes.MAIN) { MainTabsShell() }
	}
}

/** Bottom-tab shell. Tab screens land here in the next build phase. */
@Composable
fun MainTabsShell() {
	var tab by rememberSaveable { mutableStateOf(StakTab.Home) }
	Scaffold(
		containerColor = StakColors.Bg,
		bottomBar = { StakTabBar(selected = tab, onSelect = { tab = it }) },
	) { padding ->
		Box(
			modifier = Modifier.fillMaxSize().padding(padding),
			contentAlignment = Alignment.Center,
		) {
			// Placeholder until the tab screens are implemented (phase 3).
			Text(text = tab.label, color = Color.White)
		}
	}
}
