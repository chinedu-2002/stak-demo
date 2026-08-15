import SwiftUI

/// Screens of the auth + onboarding flow — mirrors the Android
/// StakNavHost: splash → sign up (⇄ sign in) → 01 welcome →
/// 02 brand picks → 03 swipe tutorial → 04 goal → 05 risk →
/// 06 preparing deck → 07 taste reveal → 08 permissions →
/// 09 profile setup → tab shell.
enum FlowScreen: Hashable {
	case createAccount
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

/// Figma prototype animations, mapped onto SwiftUI transitions.
/// Push Left = everything moves left (new screen in from the right);
/// Push Right = everything moves right (new screen in from the left).
enum FlowAnim {
	case pushLeft
	case pushRight
	case dissolve

	var transition: AnyTransition {
		switch self {
		case .pushLeft: .asymmetric(insertion: .move(edge: .trailing), removal: .move(edge: .leading))
		case .pushRight: .asymmetric(insertion: .move(edge: .leading), removal: .move(edge: .trailing))
		case .dissolve: .opacity
		}
	}

	var animation: Animation {
		switch self {
		case .dissolve: .easeOut(duration: 0.35)
		case .pushLeft, .pushRight: .easeOut(duration: 0.3)
		}
	}
}

/// Root of the app: splash → auth → onboarding → the bottom-tab shell.
///
/// The flow runs in a custom stack container (not NavigationStack) so
/// each edge can play its exact Figma prototype animation. House style
/// confirmed across the splash/sign-up/sign-in/01 Welcome proto panels:
/// forward = Push Right (in from the left), back = Push Left (in from
/// the right), dissolves for auth switches — all ease out, 300ms pushes
/// / 350ms dissolves. Unconfirmed edges follow the house style until
/// their frames say otherwise.
struct RootFlowView: View {
	private enum Phase {
		case splash
		case flow
		case main
	}

	@State private var phase = Phase.splash
	@State private var stack: [FlowScreen] = [.createAccount]
	@State private var anim = FlowAnim.dissolve

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
					.transition(anim.transition)
			case .flow:
				ZStack {
					screen(for: stack.last ?? .createAccount)
						.transition(anim.transition)
				}
				.transition(anim.transition)
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}

	private func push(_ screen: FlowScreen, _ a: FlowAnim) {
		anim = a
		withAnimation(a.animation) { stack.append(screen) }
	}

	private func pop(_ a: FlowAnim = .pushLeft) {
		anim = a
		withAnimation(a.animation) {
			if stack.count > 1 { stack.removeLast() }
		}
	}

	@ViewBuilder
	private func screen(for screen: FlowScreen) -> some View {
		switch screen {
		case .createAccount:
			CreateAccountView(
				onBack: { push(.welcome, .pushLeft) },
				onCreateAccount: { push(.welcome, .pushRight) },
				onSignIn: { push(.signIn, .dissolve) }
			)
			.id(FlowScreen.createAccount)
		case .signIn:
			SignInView(
				// Prototype (sign-in frame): back circle returns to sign up
				// as Push Left; socials/CTA leave to Home first run as Push
				// Right; the "Create account" link dissolves back.
				onBack: { pop(.pushLeft) },
				onSignIn: {
					anim = .pushRight
					withAnimation(FlowAnim.pushRight.animation) { phase = .main }
				},
				onCreateAccount: { pop(.dissolve) }
			)
			.id(FlowScreen.signIn)
		case .welcome:
			IntroView { push(.brandPicks, .pushRight) }
				.id(FlowScreen.welcome)
		case .brandPicks:
			BrandPicksView(
				onBack: { pop() },
				onContinue: { push(.swipeTutorial, .pushRight) }
			)
			.id(FlowScreen.brandPicks)
		case .swipeTutorial:
			SwipeTutorialView(
				onBack: { pop() },
				onContinue: { push(.goal, .pushRight) }
			)
			.id(FlowScreen.swipeTutorial)
		case .goal:
			GoalView(
				onBack: { pop() },
				onContinue: { push(.risk, .pushRight) }
			)
			.id(FlowScreen.goal)
		case .risk:
			RiskView(
				onBack: { pop() },
				onContinue: { push(.preparingDeck, .pushRight) }
			)
			.id(FlowScreen.risk)
		case .preparingDeck:
			PreparingDeckView {
				// Replace the loader so Back from the reveal skips it.
				anim = .dissolve
				withAnimation(FlowAnim.dissolve.animation) {
					stack.removeLast()
					stack.append(.tasteReveal)
				}
			}
			.id(FlowScreen.preparingDeck)
		case .tasteReveal:
			TasteRevealView(
				onBack: { pop() },
				onLetsGo: { push(.permissions, .pushRight) }
			)
			.id(FlowScreen.tasteReveal)
		case .permissions:
			PermissionsView(
				onBack: { pop() },
				onContinue: { push(.profileSetup, .pushRight) }
			)
			.id(FlowScreen.permissions)
		case .profileSetup:
			ProfileSetupView(
				onBack: { pop() },
				// Prototype: "Proceed to home" → Home first run, Push Right.
				onProceed: {
					anim = .pushRight
					withAnimation(FlowAnim.pushRight.animation) { phase = .main }
				}
			)
			.id(FlowScreen.profileSetup)
		}
	}
}
