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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.LaunchedEffect
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
	// Signals from pushed routes back into the shell: the Save-success
	// sheet's View-in-My-STAK pops WITH a Push Right (101:1005 Motion)
	// and lands on the My STAK tab.
	var newsPopPush by remember { mutableStateOf(false) }
	val pendingShellTab = remember { mutableStateOf<MainTab?>(null) }
	// Persisted session: restores sign-in state + profile before the
	// splash decides where to go.
	val appContext = androidx.compose.ui.platform.LocalContext.current
	remember { com.stak.demo.ui.Session.init(appContext) }
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
					// Returning user (signed in before) goes straight to Home;
					// a first-time user is taken to create an account
					// (user, 2026-08-23).
					val next = if (com.stak.demo.ui.Session.signedIn) StakRoutes.MAIN else StakRoutes.CREATE_ACCOUNT
					navController.navigate(next) {
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
					// Account created - remembered across launches.
					com.stak.demo.ui.Session.signIn()
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
					// Signed in - remembered across launches.
					com.stak.demo.ui.Session.signIn()
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
				pendingTab = pendingShellTab,
				onOpenArticle = { id -> newsPopPush = false; navController.navigate(StakRoutes.newsDetail(id)) },
				onOpenStock = { navController.navigate(StakRoutes.stockDetail("AAPL")) },
				onOpenCollection = { navController.navigate(StakRoutes.COLLECTION) },
				onOpenProfile = { navController.navigate(StakRoutes.PROFILE) },
				onOpenSimPortfolio = { navController.navigate(StakRoutes.SIM_PORTFOLIO) },
				onOpenSimPick = { navController.navigate(StakRoutes.SIM_PICK) },
				onOpenLeaderboard = { navController.navigate(StakRoutes.LEADERBOARD) },
			)
		}
		composable(
			StakRoutes.STOCK_DETAIL,
			// Authored (1:1785 Motion): Learn more -> Stock Detail Unsaved
			// folded is INSTANT. The detail's own back edge is not yet
			// authored - the pop mirrors the instant entry until its panel.
			enterTransition = { androidx.compose.animation.EnterTransition.None },
			exitTransition = { androidx.compose.animation.ExitTransition.None },
			popEnterTransition = { androidx.compose.animation.EnterTransition.None },
			popExitTransition = { androidx.compose.animation.ExitTransition.None },
		) {
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
			ProfileScreen(
				onBack = { navController.popBackStack() },
				onLogOut = {
					com.stak.demo.ui.Session.signOut()
					navController.navigate(StakRoutes.CREATE_ACCOUNT) {
						popUpTo(0) { inclusive = true }
					}
				},
			)
		}
		composable(
			StakRoutes.NEWS_DETAIL,
			// Authored (1:1228 Motion): Story tile -> News detail unsaved is
			// INSTANT; the article's Back -> listing is also Instant (1:1495).
			// The ONE animated exit: View in My STAK pops with the house
			// Push Right 300 EaseOut (101:1005 Motion).
			enterTransition = { androidx.compose.animation.EnterTransition.None },
			exitTransition = { androidx.compose.animation.ExitTransition.None },
			popEnterTransition = {
				if (newsPopPush) slideInHorizontally(tween(300, easing = EaseOut)) { -it }
				else androidx.compose.animation.EnterTransition.None
			},
			popExitTransition = {
				if (newsPopPush) slideOutHorizontally(tween(300, easing = EaseOut)) { it }
				else androidx.compose.animation.ExitTransition.None
			},
		) { backStackEntry ->
			NewsDetailScreen(
				articleId = backStackEntry.arguments?.getString("articleId") ?: com.stak.demo.ui.news.NewsArticleFeed.APPLE,
				onBack = { newsPopPush = false; navController.popBackStack() },
				onViewInMyStak = {
					newsPopPush = true
					pendingShellTab.value = MainTab.MySTAK
					// Pop the whole article chain to the shell - after READ
					// NEXT hops the stack can hold several articles, and a
					// single pop would strand the user on the previous story
					// (audit 2026-08-25).
					navController.popBackStack(StakRoutes.MAIN, false)
				},
				// READ NEXT rows push the next story's article (user, 2026-08-25).
				onOpenArticle = { id -> newsPopPush = false; navController.navigate(StakRoutes.newsDetail(id)) },
			)
		}
	}
}

/**
 * The bottom-tab shell — all five tabs are built; the bar hides while
 * Home is in its first-run state (the scrim + pill own the bottom).
 * Tab switches are the prototype's "Swap overlay · Instant".
 */
@Composable
private fun MainShell(
	pendingTab: MutableState<MainTab?>,
	onOpenArticle: (String) -> Unit,
	onOpenStock: () -> Unit,
	onOpenCollection: () -> Unit,
	onOpenProfile: () -> Unit,
	onOpenSimPortfolio: () -> Unit,
	onOpenSimPick: () -> Unit,
	onOpenLeaderboard: () -> Unit,
) {
	var tab by rememberSaveable { mutableStateOf(MainTab.Home) }
	LaunchedEffect(pendingTab.value) {
		pendingTab.value?.let { tab = it; pendingTab.value = null }
	}
	// First run shows only in the session that signed in / created the
	// account; a launch that resumed a saved session lands on Home Main.
	var homeFirstRun by rememberSaveable { mutableStateOf(!com.stak.demo.ui.Session.resumedSignedIn) }
	var discoverBuy by rememberSaveable { mutableStateOf(false) }
	var simulateBuy by rememberSaveable { mutableStateOf(false) }
	Box(modifier = Modifier.fillMaxSize()) {
		Column(modifier = Modifier.fillMaxSize()) {
			Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
				when (tab) {
					MainTab.Home -> HomeScreen(
						firstRun = homeFirstRun,
						// Authored (1:958/1:1097 Motion): the pill and the deck
						// banner jump to the Discover deck ("first run" frame),
						// the mood card to News, the why-card to My STAK - all
						// Instant; first-run ends once the pick is seen.
						onSeeTodaysPick = { homeFirstRun = false; tab = MainTab.Discover },
						onProfile = onOpenProfile,
						onOpenNews = { tab = MainTab.News },
						onOpenMyStak = { tab = MainTab.MySTAK },
						onOpenDeck = { homeFirstRun = false; tab = MainTab.Discover },
					)
					MainTab.News -> NewsScreen(onOpenArticle = onOpenArticle)
					MainTab.Discover -> DiscoverScreen(
						onLearnMore = onOpenStock,
						onPracticeBuy = { discoverBuy = true },
					)
					MainTab.Simulate -> SimulateScreen(
						onPracticeBuy = { simulateBuy = true },
						onOpenPortfolio = onOpenSimPortfolio,
						onOpenPick = onOpenSimPick,
						onOpenLeaderboard = onOpenLeaderboard,
					)
					MainTab.MySTAK -> MyStakScreen(
						onOpenCollection = onOpenCollection,
						onStartSwiping = { tab = MainTab.Discover },
					)
				}
			}
			if (!(tab == MainTab.Home && homeFirstRun)) {
				// One stable bar on every tab (user, 2026-08-23: only the
				// tabs should change when switching) - the Discover frame's
				// compact 75 bar is a recorded standing deviation.
				MainTabBar(selected = tab, onSelect = { tab = it })
			}
		}
		// Practice-buy flow overlays the whole shell — in frame 1:1970 the
		// ticket's scrim covers the tab bar and the sheet meets the screen
		// bottom, so it cannot live inside the tab content area.
		if (discoverBuy) {
			DiscoverBuyFlow(onClose = { discoverBuy = false })
		}
		// 1:4232 / 85:895: the Simulate ticket also covers the tab bar.
		if (simulateBuy) {
			DiscoverBuyFlow(
				onClose = { simulateBuy = false },
				spec = com.stak.demo.ui.simulate.PLTR_BUY,
				filledPrimary = "View portfolio",
				filledSecondary = "Done",
				ticketSecondary = "Back",
			)
		}
	}
}
