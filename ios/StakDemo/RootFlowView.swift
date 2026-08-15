import SwiftUI

/// Route names for the onboarding/auth flow — mirrors the Android
/// StakNavHost: splash → 01 intro → 02 brand picks → 03 swipe tutorial →
/// 04 goal → 05 risk → 06 preparing deck → 07 taste reveal → auth
/// (sign up ⇄ sign in) → 08 permissions → 09 profile setup → tab shell.
enum OnboardingRoute: Hashable {
	case brandPicks
	case swipeTutorial
	case goal
	case risk
	case preparingDeck
	case tasteReveal
	case createAccount
	case signIn
	case permissions
	case profileSetup
}

/// Root of the app: onboarding flow first, then the bottom-tab shell.
struct RootFlowView: View {
	private enum Phase {
		case splash
		case onboarding
		case main
	}

	@State private var phase = Phase.splash
	@State private var path: [OnboardingRoute] = []

	var body: some View {
		switch phase {
		case .splash:
			SplashView { phase = .onboarding }
		case .main:
			MainTabsView()
		case .onboarding:
			NavigationStack(path: $path) {
				IntroView { path.append(.brandPicks) }
					.toolbar(.hidden, for: .navigationBar)
					.navigationDestination(for: OnboardingRoute.self) { route in
						destination(for: route)
							.toolbar(.hidden, for: .navigationBar)
					}
			}
		}
	}

	@ViewBuilder
	private func destination(for route: OnboardingRoute) -> some View {
		switch route {
		case .brandPicks:
			BrandPicksView(
				onBack: pop,
				onContinue: { path.append(.swipeTutorial) }
			)
		case .swipeTutorial:
			SwipeTutorialView(
				onBack: pop,
				onContinue: { path.append(.goal) }
			)
		case .goal:
			GoalView(
				onBack: pop,
				onContinue: { path.append(.risk) }
			)
		case .risk:
			RiskView(
				onBack: pop,
				onContinue: { path.append(.preparingDeck) }
			)
		case .preparingDeck:
			PreparingDeckView {
				// Replace the loader so Back from the reveal skips it.
				path.removeLast()
				path.append(.tasteReveal)
			}
		case .tasteReveal:
			TasteRevealView(
				onBack: pop,
				onLetsGo: { path = [.createAccount] }
			)
		case .createAccount:
			CreateAccountView(
				onCreateAccount: { path.append(.permissions) },
				onLogIn: { path.append(.signIn) }
			)
		case .signIn:
			SignInView(
				onBack: pop,
				onSignIn: { phase = .main },
				onCreateAccount: pop
			)
		case .permissions:
			PermissionsView(
				onBack: pop,
				onContinue: { path.append(.profileSetup) }
			)
		case .profileSetup:
			ProfileSetupView(
				onBack: pop,
				onProceed: { phase = .main }
			)
		}
	}

	private func pop() {
		if !path.isEmpty { path.removeLast() }
	}
}
