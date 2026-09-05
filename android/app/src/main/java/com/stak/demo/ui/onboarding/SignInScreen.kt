package com.stak.demo.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.R
import com.stak.demo.ui.theme.Geist
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors
import com.stak.demo.ui.theme.ADVANCE_ROUNDING

/**
 * Auth · Sign in — Figma node 1554:11288. Same kit as Sign up:
 * "Welcome back" header, social pills, two inputs (password with
 * Show/Hide), a teal "Forgot password?" link, the 10% glass-ball
 * watermark and the sharp gradient "Sign in" CTA with the
 * "New to STAK? Create account" switch row.
 */
@Composable
fun SignInScreen(
	onBack: () -> Unit,
	onSignIn: () -> Unit,
	onCreateAccount: () -> Unit,
) {
	val u = figmaUnit()
	var email by rememberSaveable { mutableStateOf("") }
	var password by rememberSaveable { mutableStateOf("") }
	var showPassword by rememberSaveable { mutableStateOf(false) }

	Box(modifier = Modifier.fillMaxSize().background(StakColors.Bg)) {
		AuthWatermark()

		Artboard {
			Row(modifier = Modifier.fillMaxWidth().padding(start = (20 * u).dp, top = (10 * u).dp, bottom = (4 * u).dp)) {
				AuthBackCircle(onClick = onBack)
			}

			Column(
				verticalArrangement = Arrangement.spacedBy((14 * u).dp),
				modifier = Modifier
					.weight(1f)
					.fillMaxWidth()
					.verticalScroll(rememberScrollState())
					.padding(horizontal = (24 * u).dp)
					.padding(top = (14 * u).dp),
			) {
				Column(verticalArrangement = Arrangement.spacedBy((12 * u).dp)) {
					Text(
						text = "Welcome back",
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (26 * u).sp, lineHeight = (33 * u).sp, letterSpacing = ADVANCE_ROUNDING),
						color = StakColors.TextPrimary,
					)
					Text(
						text = "Your deck kept learning while you were away.",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp, lineHeight = (16 * u).sp, letterSpacing = ADVANCE_ROUNDING),
						color = Auth.SubtitleGray,
					)
				}
				Spacer(modifier = Modifier.height((4 * u).dp))

				SocialPill(text = "Continue with Google", iconRes = R.drawable.ic_google_g, onClick = onSignIn)
				SocialPill(text = "Continue with Apple", iconRes = R.drawable.ic_apple_logo, onClick = onSignIn)

				AuthOrDivider()

				AuthInput(value = email, onValueChange = { email = it }, placeholder = "Email address", keyboardType = KeyboardType.Email)
				AuthInput(
					value = password,
					onValueChange = { password = it },
					placeholder = "Password",
					keyboardType = KeyboardType.Password,
					hidden = !showPassword,
					trailing = { ShowHideToggle(shown = showPassword, onToggle = { showPassword = !showPassword }) },
				)
				Text(
					text = "Forgot password?",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp, letterSpacing = ADVANCE_ROUNDING),
					color = Auth.LinkTeal,
					modifier = Modifier.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
					) { /* recovery flow not designed yet */ },
				)
			}

			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.spacedBy((12 * u).dp),
				modifier = Modifier.fillMaxWidth().padding(top = (8 * u).dp, bottom = (26 * u).dp),
			) {
				AuthCta(text = "Sign in", onClick = onSignIn)
				AuthSwitchRow(prefix = "New to STAK?", link = "Create account", onClick = onCreateAccount)
			}
		}
	}
}
