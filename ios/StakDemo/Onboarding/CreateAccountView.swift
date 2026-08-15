import SwiftUI

/// Auth · Sign up — Figma node 1:830 (CHINEDU file).
///
/// The 10%-opacity glass ball watermark sits behind the lower half; white
/// social pills carry the real Google/Apple marks; three #181f30 inputs
/// (password with a Show/Hide toggle); sharp-cornered 51pt gradient CTA
/// (#a6e4f7 → #5da8bf → #3c98b4) with white Geist Medium label.
struct CreateAccountView: View {
	let onBack: () -> Void
	let onCreateAccount: () -> Void
	let onSignIn: () -> Void

	@State private var email = ""
	@State private var password = ""
	@State private var confirm = ""
	@State private var showPassword = false

	var body: some View {
		ZStack {
			AuthWatermark()

			VStack(spacing: 0) {
				HStack {
					AuthBackCircle(action: onBack)
					Spacer()
				}
				.padding(.leading, 20)
				.padding(.top, 10)
				.padding(.bottom, 4)

				ScrollView {
					VStack(alignment: .leading, spacing: 14) {
						VStack(alignment: .leading, spacing: 12) {
							Text("Create your account")
								.font(StakFont.sora(26, .semiBold))
								.foregroundStyle(StakColors.textPrimary)
							Text("Enter your details below to continue")
								.font(StakFont.geist(12))
								.foregroundStyle(Auth.subtitleGray)
						}
						Spacer().frame(height: 4)

						SocialPill(text: "Continue with Google", icon: "IcGoogleG", action: onCreateAccount)
						SocialPill(text: "Continue with Apple", icon: "IcAppleLogo", action: onCreateAccount)

						AuthOrDivider()

						AuthInput("Email address", text: $email, keyboard: .emailAddress)
						AuthInput("Password", text: $password, hidden: !showPassword) {
							ShowHideToggle(shown: $showPassword)
						}
						AuthInput("Confirm Password", text: $confirm, hidden: !showPassword)
					}
					.frame(maxWidth: .infinity, alignment: .leading)
					.padding(.horizontal, 24)
					.padding(.top, 14)
				}

				// CTA block — sharp-cornered gradient button, switch link, fine print.
				VStack(spacing: 12) {
					AuthCta(text: "Create account", action: onCreateAccount)
					AuthSwitchRow(prefix: "Already have an account?", link: "Sign in", action: onSignIn)
					Text("By continuing you agree to the Terms and Privacy Policy.")
						.font(StakFont.geist(10))
						.multilineTextAlignment(.center)
						.foregroundStyle(Auth.faintText)
						.frame(maxWidth: .infinity)
						.padding(.horizontal, 24)
				}
				.padding(.top, 8)
				.padding(.bottom, 26)
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}
