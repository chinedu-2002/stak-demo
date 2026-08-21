package com.stak.demo.navigation

import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.navArgument
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stak.demo.ui.components.MainTab
import com.stak.demo.ui.components.MainTabBar
import com.stak.demo.ui.discover.DiscoverBuyFlow
import com.stak.demo.ui.discover.DiscoverScreen
import com.stak.demo.ui.discover.StockDetailScreen
import com.stak.demo.ui.home.HomeScreen
import com.stak.demo.ui.mystak.CollectionScreen
import com.stak.demo.ui.mystak.MyStakScreen
import com.stak.demo.ui.news.NewsDetailScreen
import com.stak.demo.ui.profile.ProfileScreen
import com.stak.demo.ui.simulate.LeaderboardScreen
import com.stak.demo.ui.simulate.PickDetailScreen
import com.stak.demo.ui.simulate.SimPortfolioScreen
import com.stak.demo.ui.simulate.SimulateScreen
import com.stak.demo.ui.news.NewsScreen
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
		// Prototype house style (confirmed on the splash/sign-up/sign-in/
		// 01 Welcome frames): forward = Push Right (in from the left),
		// back = Push Left (in from the right), ease out 300ms. These are
		// the defaults; frames with their own wiring override per edge.
		enterTransition = { slideInHorizontally(tween(300, easing = EaseOut)) { -it } },
		exitTransition = { slideOutHorizontally(tween(300, easing = EaseOut)) { it } },
		popEnterTransition = { slideInHorizontally(tween(300, easing = EaseOut)) { it } },
		popExitTransition = { slideOutHorizontally(tween(300, easing = EaseOut)) { -it } },
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
		composable(
			StakRoutes.INTRO,
			arguments = listOf(navArgument("via") { defaultValue = "forward" }),
			// Prototype (sign-up frame): socials/CTA arrive as Push Right
			// (in from the left), the back circle as Push Left (in from
			// the right) — both ease out, 300ms.
			enterTransition = {
				when (targetState.arguments?.getString("via")) {
					"back" -> slideInHorizontally(tween(300, easing = EaseOut)) { it }
					else -> slideInHorizontally(tween(300, easing = EaseOut)) { -it }
				}
			},
		) {
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
		composable(
			StakRoutes.PREPARING_DECK,
			// Prototype: the loader dissolves into the taste reveal (350ms
			// ease out) after its 1800ms hold.
			exitTransition = {
				if (targetState.destination.route == StakRoutes.TASTE_REVEAL) {
					fadeOut(tween(350, easing = EaseOut))
				} else {
					null
				}
			},
		) {
			PreparingDeckScreen(
				onDone = {
					navController.navigate(StakRoutes.TASTE_REVEAL) {
						popUpTo(StakRoutes.PREPARING_DECK) { inclusive = true }
					}
				},
			)
		}
		composable(
			StakRoutes.TASTE_REVEAL,
			enterTransition = {
				if (initialState.destination.route == StakRoutes.PREPARING_DECK) {
					fadeIn(tween(350, easing = EaseOut))
				} else {
					null
				}
			},
		) {
			TasteRevealScreen(
				onBack = { navController.popBackStack() },
				onLetsGo = { navController.navigate(StakRoutes.PERMISSIONS) },
			)
		}
		composable(
			StakRoutes.CREATE_ACCOUNT,
			// Prototype: splash and the sign-in "Create account" link both
			// dissolve into sign up (350ms ease out).
			enterTransition = {
				when (initialState.destination.route) {
					StakRoutes.SPLASH, StakRoutes.SIGN_IN -> fadeIn(tween(350, easing = EaseOut))
					else -> null
				}
			},
			// Prototype (sign-in frame): its back circle returns here as
			// Push Left — sign up slides in from the right (300ms).
			popEnterTransition = {
				if (initialState.destination.route == StakRoutes.SIGN_IN) {
					slideInHorizontally(tween(300, easing = EaseOut)) { it }
				} else {
					null
				}
			},
			// Prototype (sign-up frame): leaving toward 01 Welcome pushes
			// with the arriving screen; toward Sign in it dissolves.
			exitTransition = {
				val target = targetState.destination.route.orEmpty()
				when {
					target.startsWith("onboarding/intro") ->
						if (targetState.arguments?.getString("via") == "back") {
							slideOutHorizontally(tween(300, easing = EaseOut)) { -it }
						} else {
							slideOutHorizontally(tween(300, easing = EaseOut)) { it }
						}
					target == StakRoutes.SIGN_IN -> fadeOut(tween(350, easing = EaseOut))
					else -> null
				}
			},
		) {
			CreateAccountScreen(
				onBack = { navController.navigate(StakRoutes.intro(via = "back")) },
				onCreateAccount = { navController.navigate(StakRoutes.intro(via = "forward")) },
				onSignIn = { navController.navigate(StakRoutes.SIGN_IN) },
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
		composable(
			StakRoutes.SIGN_IN,
			// Prototype: the "Sign in" link dissolves over (350ms ease out).
			enterTransition = {
				if (initialState.destination.route == StakRoutes.CREATE_ACCOUNT) {
					fadeIn(tween(350, easing = EaseOut))
				} else {
					null
				}
			},
			// Prototype (sign-in frame): socials/CTA leave toward Home
			// first run as Push Right; the "Create account" link dissolves
			// back over sign up.
			exitTransition = {
				when (targetState.destination.route) {
					StakRoutes.MAIN -> slideOutHorizontally(tween(300, easing = EaseOut)) { it }
					StakRoutes.CREATE_ACCOUNT -> fadeOut(tween(350, easing = EaseOut))
					else -> null
				}
			},
			// Prototype (sign-in frame): back circle exits as Push Left.
			popExitTransition = {
				slideOutHorizontally(tween(300, easing = EaseOut)) { -it }
			},
		) {
			SignInScreen(
				onBack = { navController.popBackStack() },
				onSignIn = {
					navController.navigate(StakRoutes.MAIN) {
						popUpTo(0) { inclusive = true }
					}
				},
				onCreateAccount = {
					navController.navigate(StakRoutes.CREATE_ACCOUNT) {
						popUpTo(StakRoutes.CREATE_ACCOUNT) { inclusive = true }
					}
				},
			)
		}
		composable(
			StakRoutes.MAIN,
			// Prototype (sign-in frame): "Home first run" arrives as Push
			// Right — in from the left, 300ms ease out.
			enterTransition = {
				if (initialState.destination.route == StakRoutes.SIGN_IN) {
					slideInHorizontally(tween(300, easing = EaseOut)) { -it }
				} else {
					null
				}
			},
		) {
			MainShell(
				onOpenArticle = { navController.navigate(StakRoutes.NEWS_DETAIL) },
				onOpenStock = { navController.navigate(StakRoutes.stockDetail("AAPL")) },
				onOpenCollection = { navController.navigate(StakRoutes.COLLECTION) },
				onOpenProfile = { navController.navigate(StakRoutes.PROFILE) },
				onOpenSimPortfolio = { navController.navigate(StakRoutes.SIM_PORTFOLIO) },
				onOpenSimPick = { navController.navigate(StakRoutes.SIM_PICK) },
				onOpenLeaderboard = { navController.navigate(StakRoutes.LEADERBOARD) },
			)
		}
		composable(StakRoutes.STOCK_DETAIL) {
			StockDetailScreen(onBack = { navController.popBackStack() })
		}
		composable(StakRoutes.COLLECTION) {
			CollectionScreen(
				onBack = { navController.popBackStack() },
				onOpenStock = { navController.navigate(StakRoutes.MYSTAK_STOCK) },
			)
		}
		composable(StakRoutes.MYSTAK_STOCK) {
			StockDetailScreen(onBack = { navController.popBackStack() }, fromMyStak = true)
		}
		composable(StakRoutes.SIM_PORTFOLIO) {
			SimPortfolioScreen(
				onBack = { navController.popBackStack() },
				onOpenPick = { navController.navigate(StakRoutes.SIM_PICK) },
			)
		}
		composable(StakRoutes.SIM_PICK) {
			PickDetailScreen(onBack = { navController.popBackStack() })
		}
		composable(StakRoutes.LEADERBOARD) {
			LeaderboardScreen(onBack = { navController.popBackStack() })
		}
		composable(StakRoutes.PROFILE) {
			ProfileScreen(onBack = { navController.popBackStack() })
		}
		composable(StakRoutes.NEWS_DETAIL) {
			NewsDetailScreen(onBack = { navController.popBackStack() })
		}
	}
}

/**
 * The bottom-tab shell — Home and News are built; the bar hides while
 * Home is in its first-run state (the scrim + pill own the bottom).
 * Tab switches are the prototype's "Swap overlay · Instant".
 */
@Composable
private fun MainShell(
	onOpenArticle: () -> Unit,
	onOpenStock: () -> Unit,
	onOpenCollection: () -> Unit,
	onOpenProfile: () -> Unit,
	onOpenSimPortfolio: () -> Unit,
	onOpenSimPick: () -> Unit,
	onOpenLeaderboard: () -> Unit,
) {
	var tab by rememberSaveable { mutableStateOf(MainTab.Home) }
	var homeFirstRun by rememberSaveable { mutableStateOf(true) }
	var discoverBuy by rememberSaveable { mutableStateOf(false) }
	Box(modifier = Modifier.fillMaxSize()) {
		Column(modifier = Modifier.fillMaxSize()) {
			Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
				when (tab) {
					MainTab.Home -> HomeScreen(
						firstRun = homeFirstRun,
						onSeeTodaysPick = { homeFirstRun = false },
						onProfile = onOpenProfile,
						// Authored (1:958 Motion): Deck promo -> 03 News, Instant.
						onOpenNews = { tab = MainTab.News },
					)
					MainTab.News -> NewsScreen(onOpenArticle = onOpenArticle)
					MainTab.Discover -> DiscoverScreen(
						onLearnMore = onOpenStock,
						onPracticeBuy = { discoverBuy = true },
					)
					MainTab.Simulate -> SimulateScreen(
						onOpenPortfolio = onOpenSimPortfolio,
						onOpenPick = onOpenSimPick,
						onOpenLeaderboard = onOpenLeaderboard,
					)
					MainTab.MySTAK -> MyStakScreen(
						onOpenCollection = onOpenCollection,
						onStartSwiping = { tab = MainTab.Discover },
					)
					else -> HomeScreen(firstRun = false, onSeeTodaysPick = {})
				}
			}
			if (!(tab == MainTab.Home && homeFirstRun)) {
				MainTabBar(selected = tab, onSelect = { tab = it }, compact = tab == MainTab.Discover)
			}
		}
		// Practice-buy flow overlays the whole shell — in frame 1:1970 the
		// ticket's scrim covers the tab bar and the sheet meets the screen
		// bottom, so it cannot live inside the tab content area.
		if (discoverBuy) {
			DiscoverBuyFlow(onClose = { discoverBuy = false })
		}
	}
}
