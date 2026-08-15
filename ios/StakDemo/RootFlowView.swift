import SwiftUI

/// Route names for the auth + onboarding flow — mirrors the Android
/// StakNavHost: splash → sign up (⇄ sign in) → 01 welcome →
/// 02 brand picks → 03 swipe tutorial → 04 goal → 05 risk →
/// 06 preparing deck → 07 taste reveal → 08 permissions →
/// 09 profile setup → tab shell.
enum OnboardingRoute: Hashable {
	case signIn
	case welcome
	case brandPicks
	case swipeTutorial
	case goal
	case risk
	case preparingDeck
	case tasteReveal
	case permissions
	case profileSetup
}

/// Root of the app: splash → auth → onboarding → the bottom-tab shell.
///
/// Prototype-confirmed edges (CHINEDU proto panel): splash auto-advances
/// to Auth · Sign up after 1200ms with a 350ms ease-out dissolve. The
/// post-auth ordering (create account → 01 Welcome → … → 07 Taste reveal
/// → 08 Permissions → 09 Profile setup → shell) follows the canvas order
/// pending per-frame prototype confirmation.
struct RootFlowView: View {
	private enum Phase {
		case splash
		case flow
		case main
	}

	@State private var phase = Phase.splash
	@State private var path: [OnboardingRoute] = []

	var body: some View {
		ZStack {
			switch phase {
			case .splash:
				SplashView {
					// Prototype: dissolve, ease out, 350ms.
					withAnimation(.easeOut(duration: 0.35)) { phase = .flow }
				}
				.transition(.opacity)
			case .main:
				MainTabsView()
					.transition(.opacity)
			case .flow:
				NavigationStack(path: $path) {
					CreateAccountView(
						onCreateAccount: { path.append(.welcome) },
						onLogIn: { path.append(.signIn) }
					)
					.toolbar(.hidden, for: .navigationBar)
					.navigationDestination(for: OnboardingRoute.self) { route in
						destination(for: route)
							.toolbar(.hidden, for: .navigationBar)
					}
				}
				.transition(.opacity)
			}
		}
	}

	@ViewBuilder
	private func destination(for route: OnboardingRoute) -> some View {
		switch route {
		case .signIn:
			SignInView(
				onBack: pop,
				onSignIn: { phase = .main },
				onCreateAccount: pop
			)
		case .welcome:
			IntroView { path.append(.brandPicks) }
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
				onLetsGo: { path.append(.permissions) }
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
