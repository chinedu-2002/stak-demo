package com.stak.demo.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.R
import com.stak.demo.ui.theme.Geist
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors

/* Auth-section palette — Figma "Auth · Sign up" (node 1554:9126). */
private val NavCircle = Color(0xFF192238)
private val SubtitleGray = Color(0xFFACAFB1)
private val InputBg = Color(0xFF181F30)
private val DividerLine = Color(0xFF2A3346)
private val FaintText = Color(0xFF5C6B85)
private val LinkTeal = Color(0xFF69B3CA)
private val DarkOnWhite = Color(0xFF0E162B)

/**
 * Onboarding · Create account — Figma node 1554:9126 ("Auth · Sign up").
 *
 * The 10%-opacity glass ball watermark sits behind the lower half; white
 * social pills carry the real Google/Apple marks; three #181f30 inputs
 * (password with a Show/Hide toggle); sharp-cornered 51dp gradient CTA
 * (#a6e4f7 → #5da8bf → #3c98b4) with white Geist Medium label.
 */
@Composable
fun CreateAccountScreen(onCreateAccount: () -> Unit, onLogIn: () -> Unit) {
	var email by rememberSaveable { mutableStateOf("") }
	var password by rememberSaveable { mutableStateOf("") }
	var confirm by rememberSaveable { mutableStateOf("") }
	var showPassword by rememberSaveable { mutableStateOf(false) }

	Box(modifier = Modifier.fillMaxSize().background(StakColors.Bg)) {
		// Watermark — glass ball at 10% alpha, 332.65dp rotated 174.3°,
		// centered 10dp left / 159.8dp below screen center (Figma 1554:9187).
		Image(
			painter = painterResource(R.drawable.splash_glass_ball),
			contentDescription = null,
			modifier = Modifier
				.size(332.65.dp)
				.align(Alignment.Center)
				.offset(x = (-10).dp, y = 159.78.dp)
				.rotate(174.3f)
				.alpha(0.1f),
		)

		Column(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
			// Nav row — 40dp #192238 circle with the #AEAEAE chevron.
			Row(modifier = Modifier.fillMaxWidth().padding(start = 20.dp, top = 10.dp, bottom = 4.dp)) {
				Box(
					modifier = Modifier
						.size(40.dp)
						.background(NavCircle, CircleShape)
						.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
							onClick = onLogIn,
						),
					contentAlignment = Alignment.Center,
				) {
					Image(
						painter = painterResource(R.drawable.ic_back_chevron),
						contentDescription = "Back",
						modifier = Modifier.size(22.dp),
					)
				}
			}

			Column(
				verticalArrangement = Arrangement.spacedBy(14.dp),
				modifier = Modifier
					.weight(1f)
					.fillMaxWidth()
					.verticalScroll(rememberScrollState())
					.padding(horizontal = 24.dp)
					.padding(top = 14.dp),
			) {
				Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
					Text(
						text = "Create your account",
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 26.sp),
						color = StakColors.TextPrimary,
					)
					Text(
						text = "Enter your details below to continue",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 12.sp),
						color = SubtitleGray,
					)
				}
				Spacer(modifier = Modifier.height(4.dp))

				SocialPill(text = "Continue with Google", iconRes = R.drawable.ic_google_g, onClick = onCreateAccount)
				SocialPill(text = "Continue with Apple", iconRes = R.drawable.ic_apple_logo, onClick = onCreateAccount)

				Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
					Box(modifier = Modifier.weight(1f).height(1.dp).background(DividerLine))
					Text(
						text = "or",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 11.sp),
						color = FaintText,
					)
					Box(modifier = Modifier.weight(1f).height(1.dp).background(DividerLine))
				}

				AuthInput(value = email, onValueChange = { email = it }, placeholder = "Email address", keyboardType = KeyboardType.Email)
				AuthInput(
					value = password,
					onValueChange = { password = it },
					placeholder = "Password",
					keyboardType = KeyboardType.Password,
					hidden = !showPassword,
					trailing = {
						Text(
							text = if (showPassword) "Hide" else "Show",
							style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 11.sp),
							color = LinkTeal,
							modifier = Modifier.clickable(
								interactionSource = remember { MutableInteractionSource() },
								indication = null,
							) { showPassword = !showPassword },
						)
					},
				)
				AuthInput(
					value = confirm,
					onValueChange = { confirm = it },
					placeholder = "Confirm Password",
					keyboardType = KeyboardType.Password,
					hidden = !showPassword,
				)
			}

			// CTA block — sharp-cornered gradient button, switch link, fine print.
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.spacedBy(12.dp),
				modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 26.dp),
			) {
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 20.dp)
						.height(51.dp)
						.background(
							Brush.verticalGradient(
								0.0889f to Color(0xFFA6E4F7),
								0.3919f to Color(0xFF5DA8BF),
								0.7255f to Color(0xFF3C98B4),
								1f to Color(0xFF3C98B4),
							),
							RoundedCornerShape(5.78.dp),
						)
						.border(0.36.dp, StakColors.CtaBorder, RoundedCornerShape(5.78.dp))
						.clickable(onClick = onCreateAccount),
					contentAlignment = Alignment.Center,
				) {
					Text(
						text = "Create account",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 14.sp),
						color = StakColors.TextPrimary,
					)
				}
				Row(horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.CenterVertically) {
					Text(
						text = "Already have an account?",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 12.sp),
						color = StakColors.Muted,
					)
					Text(
						text = "Sign in",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp),
						color = LinkTeal,
						modifier = Modifier.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
							onClick = onLogIn,
						),
					)
				}
				Text(
					text = "By continuing you agree to the Terms and Privacy Policy.",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 10.sp, textAlign = TextAlign.Center),
					color = FaintText,
					modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
				)
			}
		}
	}
}

/** White social pill — radius 24, 13dp vertical padding, 18dp brand mark. */
@Composable
private fun SocialPill(text: String, iconRes: Int, onClick: () -> Unit) {
	Row(
		horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.background(Color.White, RoundedCornerShape(24.dp))
			.clickable(onClick = onClick)
			.padding(vertical = 13.dp),
	) {
		Image(painter = painterResource(iconRes), contentDescription = null, modifier = Modifier.size(18.dp))
		Text(
			text = text,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 14.sp),
			color = DarkOnWhite,
		)
	}
}

/** Auth input — #181f30, radius 14, 16dp padding, Geist 13, optional trailing. */
@Composable
private fun AuthInput(
	value: String,
	onValueChange: (String) -> Unit,
	placeholder: String,
	keyboardType: KeyboardType = KeyboardType.Text,
	hidden: Boolean = false,
	trailing: (@Composable () -> Unit)? = null,
) {
	val textStyle = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 13.sp, color = StakColors.TextPrimary)
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.background(InputBg, RoundedCornerShape(14.dp))
			.padding(16.dp),
	) {
		Box(modifier = Modifier.weight(1f)) {
			if (value.isEmpty()) {
				Text(text = placeholder, style = textStyle, color = StakColors.Muted)
			}
			BasicTextField(
				value = value,
				onValueChange = onValueChange,
				textStyle = textStyle,
				singleLine = true,
				cursorBrush = SolidColor(StakColors.Accent),
				visualTransformation = if (hidden) PasswordVisualTransformation() else VisualTransformation.None,
				keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
				modifier = Modifier.fillMaxWidth(),
			)
		}
		if (trailing != null) {
			trailing()
		}
	}
}
