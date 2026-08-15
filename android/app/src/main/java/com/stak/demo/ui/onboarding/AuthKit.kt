package com.stak.demo.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.R
import com.stak.demo.ui.theme.Geist
import com.stak.demo.ui.theme.StakColors

/**
 * Shared pieces of the Figma "Auth ·" screens (Sign up 1554:9126,
 * Sign in 1554:11288) — palette, watermark, nav circle, social pills,
 * inputs, the sharp-cornered gradient CTA and the switch link row.
 */
internal object Auth {
	val NavCircle = Color(0xFF192238)
	val SubtitleGray = Color(0xFFACAFB1)
	val InputBg = Color(0xFF181F30)
	val DividerLine = Color(0xFF2A3346)
	val FaintText = Color(0xFF5C6B85)
	val LinkTeal = Color(0xFF69B3CA)
	val DarkOnWhite = Color(0xFF0E162B)
}

/**
 * Figma-artboard scale: 1 design px = `figmaUnit()` dp. The CHINEDU
 * frames are fixed 390dp artboards; fixed compositions (hero renders,
 * the swipe deck) multiply by this so their proportions hold on wider
 * devices (e.g. the 411dp Pixel 7) instead of shrinking relative to
 * the screen. Text/paddings stay plain dp.
 */
@Composable
internal fun figmaUnit(): Float =
	androidx.compose.ui.platform.LocalConfiguration.current.screenWidthDp / 390f

/** 10%-alpha glass ball rotated 174.3°, centered 10dp left / 159.8dp below screen center. */
@Composable
internal fun BoxScope.AuthWatermark() {
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
}

/** 40dp #192238 circle with the #AEAEAE back chevron. */
@Composable
internal fun AuthBackCircle(onClick: () -> Unit, modifier: Modifier = Modifier) {
	Box(
		modifier = modifier
			.size(40.dp)
			.background(Auth.NavCircle, CircleShape)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
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

/** White social pill — radius 24, 13dp vertical padding, 18dp brand mark. */
@Composable
internal fun SocialPill(text: String, iconRes: Int, onClick: () -> Unit) {
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
			color = Auth.DarkOnWhite,
		)
	}
}

/** The 1px #2a3346 "or" divider row. */
@Composable
internal fun AuthOrDivider() {
	Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
		Box(modifier = Modifier.weight(1f).height(1.dp).background(Auth.DividerLine))
		Text(
			text = "or",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 11.sp),
			color = Auth.FaintText,
		)
		Box(modifier = Modifier.weight(1f).height(1.dp).background(Auth.DividerLine))
	}
}

/** Auth input — #181f30, radius 14, 16dp padding, Geist 13, optional trailing. */
@Composable
internal fun AuthInput(
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
			.background(Auth.InputBg, RoundedCornerShape(14.dp))
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

/** The teal Show/Hide toggle used inside password inputs. */
@Composable
internal fun ShowHideToggle(shown: Boolean, onToggle: () -> Unit) {
	Text(
		text = if (shown) "Hide" else "Show",
		style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 11.sp),
		color = Auth.LinkTeal,
		modifier = Modifier.clickable(
			interactionSource = remember { MutableInteractionSource() },
			indication = null,
			onClick = onToggle,
		),
	)
}

/** Sharp-cornered 52dp CTA — 3-stop a6e4f7/5da8bf/3c98b4 gradient, white Geist Medium 14 (CHINEDU 1:873). */
@Composable
internal fun AuthCta(text: String, onClick: () -> Unit) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 20.dp)
			.height(52.dp)
			.background(
				Brush.verticalGradient(
					0.0889f to Color(0xFFA6E4F7),
					0.3919f to Color(0xFF5DA8BF),
					0.7255f to Color(0xFF3C98B4),
					1f to Color(0xFF3C98B4),
				),
				RoundedCornerShape(6.dp),
			)
			.border(0.36.dp, StakColors.CtaBorder, RoundedCornerShape(6.dp))
			.clickable(onClick = onClick),
		contentAlignment = Alignment.Center,
	) {
		Text(
			text = text,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 14.sp),
			color = StakColors.TextPrimary,
		)
	}
}

/** Secondary flow button — h52, r6, rgba(52,59,79,0.33) hairline, Sora 14 muted (CHINEDU 1:791). */
@Composable
internal fun AuthSecondaryButton(text: String, onClick: () -> Unit) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 20.dp)
			.height(52.dp)
			.border(0.36.dp, Color(0x54343B4F), RoundedCornerShape(6.dp))
			.clickable(onClick = onClick),
		contentAlignment = Alignment.Center,
	) {
		Text(
			text = text,
			style = TextStyle(fontFamily = com.stak.demo.ui.theme.Sora, fontWeight = FontWeight.Normal, fontSize = 14.sp),
			color = StakColors.Muted,
		)
	}
}

/** "Already have an account? Sign in" / "New to STAK? Create account" row. */
@Composable
internal fun AuthSwitchRow(prefix: String, link: String, onClick: () -> Unit) {
	Row(horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.CenterVertically) {
		Text(
			text = prefix,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 12.sp),
			color = StakColors.Muted,
		)
		Text(
			text = link,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp),
			color = Auth.LinkTeal,
			modifier = Modifier.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			),
		)
	}
}
