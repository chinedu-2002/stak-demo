package com.stak.demo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors

/**
 * STAK input — Figma onboarding fields: #172037, rgba(255,255,255,0.08)
 * border, 54dp, radius 13, Sora Regular 15, muted placeholder, 18dp inset.
 */
@Composable
fun StakTextField(
	value: String,
	onValueChange: (String) -> Unit,
	placeholder: String,
	modifier: Modifier = Modifier,
	isPassword: Boolean = false,
	keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
	val textStyle = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 15.sp, color = StakColors.TextPrimary)
	Box(
		modifier = modifier
			.fillMaxWidth()
			.height(54.dp)
			.background(StakColors.SurfaceAlt, RoundedCornerShape(13.dp))
			.border(1.dp, StakColors.Divider, RoundedCornerShape(13.dp))
			.padding(horizontal = 18.dp),
		contentAlignment = Alignment.CenterStart,
	) {
		if (value.isEmpty()) {
			Text(text = placeholder, style = textStyle, color = StakColors.Muted)
		}
		BasicTextField(
			value = value,
			onValueChange = onValueChange,
			textStyle = textStyle,
			singleLine = true,
			cursorBrush = SolidColor(StakColors.Accent),
			visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
			keyboardOptions = keyboardOptions,
			modifier = Modifier.fillMaxWidth(),
		)
	}
}
