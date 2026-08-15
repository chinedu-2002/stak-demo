import SwiftUI

/// Auth · Sign in — Figma node 1:879 (CHINEDU file). Same kit as Sign up:
/// "Welcome back" header, social pills, two inputs (password with
/// Show/Hide), a teal "Forgot password?" link, the 10% glass-ball
/// watermark and the sharp gradient "Sign in" CTA with the
/// "New to STAK? Create account" switch row.
struct SignInView: View {
	let onBack: () -> Void
	let onSignIn: () -> Void
	let onCreateAccount: () -> Void

	@State private var email = ""
	@State private var password = ""
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
							Text("Welcome back")
								.font(StakFont.sora(26, .semiBold))
								.foregroundStyle(StakColors.textPrimary)
							Text("Your deck kept learning while you were away.")
								.font(StakFont.geist(12))
								.foregroundStyle(Auth.subtitleGray)
						}
						Spacer().frame(height: 4)

						SocialPill(text: "Continue with Google", icon: "IcGoogleG", action: onSignIn)
						SocialPill(text: "Continue with Apple", icon: "IcAppleLogo", action: onSignIn)

						AuthOrDivider()

						AuthInput("Email address", text: $email, keyboard: .emailAddress)
						AuthInput("Password", text: $password, hidden: !showPassword) {
							ShowHideToggle(shown: $showPassword)
						}
						Button(action: { /* recovery flow not designed yet */ }) {
							Text("Forgot password?")
								.font(StakFont.geist(12, .medium))
								.foregroundStyle(Auth.linkTeal)
						}
						.buttonStyle(.plain)
					}
					.frame(maxWidth: .infinity, alignment: .leading)
					.padding(.horizontal, 24)
					.padding(.top, 14)
				}

				VStack(spacing: 12) {
					AuthCta(text: "Sign in", action: onSignIn)
					AuthSwitchRow(prefix: "New to STAK?", link: "Create account", action: onCreateAccount)
				}
				.padding(.top, 8)
				.padding(.bottom, 26)
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}
