package com.stak.demo.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.ui.components.ProviderDot
import com.stak.demo.ui.components.StakLinkRow
import com.stak.demo.ui.components.StakPrimaryButton
import com.stak.demo.ui.components.StakSurfaceButton
import com.stak.demo.ui.components.StakTextField
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors

/**
 * Onboarding · Create account — Figma node 22:1498.
 *
 * Entry screen of the sign-up flow: heading + subtitle, two social
 * sign-in rows, an "or" divider, email/password fields, the gradient
 * "Create account" CTA, a "Log in" link row, and bottom-anchored fine
 * print. The Figma back chevron (y55) is intentionally omitted — this
 * is the flow entry and the screen exposes no back callback.
 */
@Composable
fun CreateAccountScreen(
	onCreateAccount: () -> Unit,
	onLogIn: () -> Unit,
) {
	var email by rememberSaveable { mutableStateOf("") }
	var password by rememberSaveable { mutableStateOf("") }

	BoxWithConstraints(
		modifier = Modifier
			.fillMaxSize()
			.background(StakColors.Bg)
			.systemBarsPadding(),
	) {
		val minHeight = maxHeight
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.verticalScroll(rememberScrollState())
				.heightIn(min = minHeight)
				.padding(horizontal = 20.dp),
		) {
			// H1 — Figma y104 (~60dp below the system bar).
			Spacer(modifier = Modifier.height(60.dp))
			Text(
				text = "Create your account",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 28.sp),
				color = StakColors.TextPrimary,
			)
			Spacer(modifier = Modifier.height(9.dp))
			Text(
				text = "Practice with virtual money. No card, no risk.",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 14.sp),
				color = StakColors.Muted,
			)

			// Social sign-in rows — Figma y196 / y260.
			Spacer(modifier = Modifier.height(30.dp))
			StakSurfaceButton(
				text = "Continue with Google",
				onClick = onCreateAccount,
				leading = { ProviderDot() },
			)
			Spacer(modifier = Modifier.height(12.dp))
			StakSurfaceButton(
				text = "Continue with Apple",
				onClick = onCreateAccount,
				leading = { ProviderDot() },
			)

			// "or" divider — Figma y344 strips flanking 13sp muted text.
			Spacer(modifier = Modifier.height(24.dp))
			Row(
				modifier = Modifier.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically,
			) {
				Box(
					modifier = Modifier
						.weight(1f)
						.height(1.dp)
						.background(StakColors.BorderMid),
				)
				Text(
					text = "or",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 13.sp),
					color = StakColors.Muted,
					modifier = Modifier.padding(horizontal = 16.dp),
				)
				Box(
					modifier = Modifier
						.weight(1f)
						.height(1.dp)
						.background(StakColors.BorderMid),
				)
			}

			// Email + password inputs — Figma y372 / y438.
			Spacer(modifier = Modifier.height(20.dp))
			StakTextField(
				value = email,
				onValueChange = { email = it },
				placeholder = "Email address",
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
			)
			Spacer(modifier = Modifier.height(12.dp))
			StakTextField(
				value = password,
				onValueChange = { password = it },
				placeholder = "Create a password",
				isPassword = true,
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
			)

			// Primary CTA — Figma y512.
			Spacer(modifier = Modifier.height(20.dp))
			StakPrimaryButton(
				text = "Create account",
				onClick = onCreateAccount,
			)

			// Log-in link — Figma y592, centered.
			Spacer(modifier = Modifier.height(26.dp))
			StakLinkRow(
				prefix = "Already have an account?",
				link = "Log in",
				onClick = onLogIn,
				modifier = Modifier.align(Alignment.CenterHorizontally),
			)

			// Fine print — Figma y792, anchored to the bottom of the screen.
			Spacer(modifier = Modifier.weight(1f))
			Spacer(modifier = Modifier.height(24.dp))
			Text(
				text = "By continuing you agree to our Terms and Privacy.",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 11.sp),
				color = StakColors.TextFaint,
				textAlign = TextAlign.Center,
				modifier = Modifier.fillMaxWidth(),
			)
			Spacer(modifier = Modifier.height(12.dp))
		}
	}
}
