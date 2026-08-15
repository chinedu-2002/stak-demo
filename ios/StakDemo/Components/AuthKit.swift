import SwiftUI

// Shared pieces of the Figma "Auth ·" screens (CHINEDU file: Sign up 1:830,
// Sign in 1:879) — watermark, nav circle, social pills, inputs, the
// sharp-cornered gradient CTA and the switch link row. Mirrors the Android
// AuthKit.kt so both apps speak the same dialect. 1 Figma px = 1 pt.

/// 10%-alpha glass ball rotated 174.3°, centered 10pt left / 159.8pt below screen center.
struct AuthWatermark: View {
	var body: some View {
		Image("SplashGlassBall")
			.resizable()
			.frame(width: 332.65, height: 332.65)
			.rotationEffect(.degrees(174.3))
			.opacity(0.1)
			.offset(x: -10, y: 159.78)
	}
}

/// 40pt #192238 circle with the #AEAEAE back chevron.
struct AuthBackCircle: View {
	let action: () -> Void

	var body: some View {
		Button(action: action) {
			ZStack {
				Circle().fill(Auth.navCircle)
				Image("BackChevron")
					.resizable()
					.frame(width: 22, height: 22)
			}
			.frame(width: 40, height: 40)
		}
		.buttonStyle(.plain)
		.accessibilityLabel("Back")
	}
}

/// The "STEP · …" kicker row — Geist Medium 10, 1.2 tracking, #5c6b85.
struct OnboardingKicker: View {
	let text: String

	var body: some View {
		Text(text)
			.font(StakFont.geist(10, .medium))
			.tracking(1.2)
			.foregroundStyle(Auth.faintText)
			.frame(maxWidth: .infinity, alignment: .leading)
			.padding(.horizontal, 20)
			.padding(.top, 6)
	}
}

/// Right-aligned "STEP n OF 6" label used in the nav rows.
struct StepLabel: View {
	let text: String

	var body: some View {
		Text(text)
			.font(StakFont.geist(10, .medium))
			.tracking(0.9)
			.foregroundStyle(Auth.faintText)
	}
}

/// White social pill — radius 24, 13pt vertical padding, 18pt brand mark.
struct SocialPill: View {
	let text: String
	let icon: String
	let action: () -> Void

	var body: some View {
		Button(action: action) {
			HStack(spacing: 10) {
				Image(icon)
					.resizable()
					.frame(width: 18, height: 18)
				Text(text)
					.font(StakFont.geist(14, .medium))
					.foregroundStyle(Auth.darkOnWhite)
			}
			.frame(maxWidth: .infinity)
			.padding(.vertical, 13)
			.background(Color.white, in: RoundedRectangle(cornerRadius: 24))
		}
		.buttonStyle(.plain)
	}
}

/// The 1px #2a3346 "or" divider row.
struct AuthOrDivider: View {
	var body: some View {
		HStack(spacing: 10) {
			Rectangle().fill(Auth.dividerLine).frame(height: 1)
			Text("or")
				.font(StakFont.geist(11))
				.foregroundStyle(Auth.faintText)
			Rectangle().fill(Auth.dividerLine).frame(height: 1)
		}
	}
}

/// Auth input — #181f30, radius 14, 16pt padding, Geist 13, optional trailing.
struct AuthInput<Trailing: View>: View {
	let placeholder: String
	@Binding var text: String
	var keyboard: UIKeyboardType = .default
	var hidden = false
	var trailing: Trailing

	init(
		_ placeholder: String,
		text: Binding<String>,
		keyboard: UIKeyboardType = .default,
		hidden: Bool = false,
		@ViewBuilder trailing: () -> Trailing
	) {
		self.placeholder = placeholder
		self._text = text
		self.keyboard = keyboard
		self.hidden = hidden
		self.trailing = trailing()
	}

	var body: some View {
		HStack {
			Group {
				if hidden {
					SecureField("", text: $text, prompt: prompt)
				} else {
					TextField("", text: $text, prompt: prompt)
				}
			}
			.font(StakFont.geist(13))
			.foregroundStyle(StakColors.textPrimary)
			.tint(StakColors.accent)
			.keyboardType(keyboard)
			.textInputAutocapitalization(.never)
			.autocorrectionDisabled()
			trailing
		}
		.padding(16)
		.background(Auth.inputBg, in: RoundedRectangle(cornerRadius: 14))
	}

	private var prompt: Text {
		Text(placeholder).font(StakFont.geist(13)).foregroundStyle(StakColors.muted)
	}
}

extension AuthInput where Trailing == EmptyView {
	init(
		_ placeholder: String,
		text: Binding<String>,
		keyboard: UIKeyboardType = .default,
		hidden: Bool = false
	) {
		self.init(placeholder, text: text, keyboard: keyboard, hidden: hidden) { EmptyView() }
	}
}

/// The teal Show/Hide toggle used inside password inputs.
struct ShowHideToggle: View {
	@Binding var shown: Bool

	var body: some View {
		Button { shown.toggle() } label: {
			Text(shown ? "Hide" : "Show")
				.font(StakFont.geist(11, .medium))
				.foregroundStyle(Auth.linkTeal)
		}
		.buttonStyle(.plain)
	}
}

/// Sharp-cornered 51pt CTA — 3-stop a6e4f7/5da8bf/3c98b4 gradient, white Geist Medium 14.
struct AuthCta: View {
	let text: String
	let action: () -> Void

	var body: some View {
		Button(action: action) {
			Text(text)
				.font(StakFont.geist(14, .medium))
				.foregroundStyle(StakColors.textPrimary)
				.frame(maxWidth: .infinity)
				.frame(height: 51)
				.background(
					LinearGradient(
						stops: [
							.init(color: Color(argb: 0xFFA6E4F7), location: 0.0889),
							.init(color: Color(argb: 0xFF5DA8BF), location: 0.3919),
							.init(color: Color(argb: 0xFF3C98B4), location: 0.7255),
							.init(color: Color(argb: 0xFF3C98B4), location: 1)
						],
						startPoint: .top,
						endPoint: .bottom
					),
					in: RoundedRectangle(cornerRadius: 5.78)
				)
				.overlay(
					RoundedRectangle(cornerRadius: 5.78)
						.strokeBorder(StakColors.ctaBorder, lineWidth: 0.36)
				)
		}
		.buttonStyle(.plain)
		.padding(.horizontal, 20)
	}
}

/// Secondary flow button — h51, r5.78, rgba(52,59,79,0.33) hairline, Sora 14.45 muted.
struct AuthSecondaryButton: View {
	let text: String
	let action: () -> Void

	var body: some View {
		Button(action: action) {
			Text(text)
				.font(StakFont.sora(14.45))
				.foregroundStyle(StakColors.muted)
				.frame(maxWidth: .infinity)
				.frame(height: 51)
				.overlay(
					RoundedRectangle(cornerRadius: 5.78)
						.strokeBorder(Color(argb: 0x54343B4F), lineWidth: 0.36)
				)
				.contentShape(Rectangle())
		}
		.buttonStyle(.plain)
		.padding(.horizontal, 20)
	}
}

/// "Already have an account? Sign in" / "New to STAK? Create account" row.
struct AuthSwitchRow: View {
	let prefix: String
	let link: String
	let action: () -> Void

	var body: some View {
		HStack(spacing: 5) {
			Text(prefix)
				.font(StakFont.geist(12))
				.foregroundStyle(StakColors.muted)
			Button(action: action) {
				Text(link)
					.font(StakFont.geist(12, .medium))
					.foregroundStyle(Auth.linkTeal)
			}
			.buttonStyle(.plain)
		}
	}
}
