package com.stak.demo.navigation

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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.stak.demo.ui.components.StakTab
import com.stak.demo.ui.components.StakTabBar
import com.stak.demo.ui.onboarding.CreateAccountScreen
import com.stak.demo.ui.onboarding.SplashScreen
import com.stak.demo.ui.onboarding.SignInScreen
import com.stak.demo.ui.onboarding.NotificationsScreen
import com.stak.demo.ui.onboarding.QuizScreen
import com.stak.demo.ui.onboarding.VerifyEmailScreen
import com.stak.demo.ui.onboarding.WelcomeScreen
import com.stak.demo.ui.onboarding.YourTypeScreen
import com.stak.demo.ui.theme.StakColors

/** Root of the app: onboarding flow first, then the bottom-tab shell. */
@Composable
fun StakRoot(navController: NavHostController = rememberNavController()) {
	NavHost(
		navController = navController,
		startDestination = StakRoutes.SPLASH,
	) {
		composable(StakRoutes.SPLASH) {
			SplashScreen(
				onContinue = {
					navController.navigate(StakRoutes.CREATE_ACCOUNT) {
						popUpTo(StakRoutes.SPLASH) { inclusive = true }
					}
				},
			)
		}
		composable(StakRoutes.CREATE_ACCOUNT) {
			CreateAccountScreen(
				onCreateAccount = { navController.navigate(StakRoutes.VERIFY_EMAIL) },
				onLogIn = { navController.navigate(StakRoutes.SIGN_IN) },
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
		composable(StakRoutes.VERIFY_EMAIL) {
			VerifyEmailScreen(
				onBack = { navController.popBackStack() },
				onContinue = { navController.navigate(StakRoutes.NOTIFICATIONS) },
			)
		}
		composable(StakRoutes.NOTIFICATIONS) {
			NotificationsScreen(
				onBack = { navController.popBackStack() },
				onAllow = { navController.navigate(StakRoutes.quiz(1)) },
				onSkip = { navController.navigate(StakRoutes.quiz(1)) },
			)
		}
		composable(
			StakRoutes.QUIZ,
			arguments = listOf(navArgument("step") { type = NavType.IntType }),
		) { entry ->
			val step = entry.arguments?.getInt("step") ?: 1
			QuizScreen(
				step = step,
				onBack = { navController.popBackStack() },
				onContinue = {
					if (step < 4) navController.navigate(StakRoutes.quiz(step + 1))
					else navController.navigate(StakRoutes.YOUR_TYPE)
				},
			)
		}
		composable(StakRoutes.YOUR_TYPE) {
			YourTypeScreen(
				onBack = { navController.popBackStack() },
				onContinue = { navController.navigate(StakRoutes.WELCOME) },
			)
		}
		composable(StakRoutes.WELCOME) {
			WelcomeScreen(
				onEnter = {
					navController.navigate(StakRoutes.MAIN) {
						popUpTo(0) { inclusive = true }
					}
				},
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
