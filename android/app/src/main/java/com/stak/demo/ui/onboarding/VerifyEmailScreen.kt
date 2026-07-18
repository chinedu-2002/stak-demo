package com.stak.demo.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.ui.components.BackChevron
import com.stak.demo.ui.components.StakLinkRow
import com.stak.demo.ui.components.StakPrimaryButton
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors

/** Number of digits in the verification code. */
private const val CODE_LENGTH = 6

/**
 * Onboarding · Verify email — Figma node 22:1522
 * (file tzBCgpuPAFGnipu7NtwH3z).
 *
 * "Check your email" heading, 6-digit code entry boxes, a "Didn't get it?
 * Resend" link and a bottom-pinned gradient "Verify" CTA.
 */
@Composable
fun VerifyEmailScreen(onBack: () -> Unit, onContinue: () -> Unit) {
	var code by rememberSaveable { mutableStateOf("") }

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(StakColors.Bg)
			.systemBarsPadding(),
	) {
		Column(
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth()
				.verticalScroll(rememberScrollState())
				.padding(horizontal = 20.dp),
		) {
			// Back chevron — Figma y55 (~8dp below the system bar).
			Spacer(modifier = Modifier.height(8.dp))
			BackChevron(onClick = onBack)
			Spacer(modifier = Modifier.height(8.dp))

			// Heading — Sora SemiBold 28 white (Figma y104).
			Text(
				text = "Check your email",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 28.sp),
				color = StakColors.TextPrimary,
			)
			Spacer(modifier = Modifier.height(9.dp))

			// Subtitle — Sora Regular 14 muted, 1.4 line height (Figma y148, width 330).
			Text(
				text = "Enter the 6-digit code we sent to you@email.com",
				style = TextStyle(
					fontFamily = Sora,
					fontWeight = FontWeight.Normal,
					fontSize = 14.sp,
					lineHeight = 19.6.sp,
				),
				color = StakColors.Muted,
				modifier = Modifier.width(330.dp),
			)
			Spacer(modifier = Modifier.height(43.dp))

			// Code entry — six 46x58 boxes (Figma y230), invisible field captures input.
			CodeEntryField(
				code = code,
				onCodeChange = { new -> code = new.filter { it.isDigit() }.take(CODE_LENGTH) },
			)
			Spacer(modifier = Modifier.height(28.dp))

			// "Didn't get it? Resend" — centered (Figma y316).
			Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
				StakLinkRow(prefix = "Didn't get it?", link = "Resend", onClick = { /* no-op */ })
			}
			Spacer(modifier = Modifier.height(24.dp))
		}

		// Bottom-pinned gradient CTA (Figma y760, 30px bottom margin).
		StakPrimaryButton(
			text = "Verify",
			onClick = onContinue,
			modifier = Modifier.padding(horizontal = 20.dp),
		)
		Spacer(modifier = Modifier.height(30.dp))
	}
}

/**
 * Six-box code entry from Figma node 22:1522. A transparent [BasicTextField]
 * holds the digits; each box is #172037 r12 with a rgba(255,255,255,0.08)
 * hairline, and the next box to fill gets the 2dp #39c5cb accent border.
 */
@Composable
private fun CodeEntryField(
	code: String,
	onCodeChange: (String) -> Unit,
	modifier: Modifier = Modifier,
) {
	BasicTextField(
		value = code,
		onValueChange = onCodeChange,
		keyboardOptions = KeyboardOptions(
			keyboardType = KeyboardType.NumberPassword,
			imeAction = ImeAction.Done,
		),
		singleLine = true,
		textStyle = TextStyle(color = Color.Transparent, fontSize = 1.sp),
		cursorBrush = SolidColor(Color.Transparent),
		modifier = modifier.fillMaxWidth(),
		decorationBox = { innerTextField ->
			Box {
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween,
				) {
					repeat(CODE_LENGTH) { index ->
						CodeDigitBox(
							digit = code.getOrNull(index),
							active = index == code.length,
						)
					}
				}
				// Keep the editable field present but invisible over the boxes.
				Box(modifier = Modifier.matchParentSize().alpha(0f)) { innerTextField() }
			}
		},
	)
}

/** One 46x58 code box; [active] draws the 2dp accent border from the design. */
@Composable
private fun CodeDigitBox(digit: Char?, active: Boolean) {
	Box(
		modifier = Modifier
			.size(width = 46.dp, height = 58.dp)
			.background(StakColors.SurfaceAlt, RoundedCornerShape(12.dp))
			.border(
				width = if (active) 2.dp else 1.dp,
				color = if (active) StakColors.Accent else StakColors.Divider,
				shape = RoundedCornerShape(12.dp),
			),
		contentAlignment = Alignment.Center,
	) {
		if (digit != null) {
			Text(
				text = digit.toString(),
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 22.sp),
				color = StakColors.TextPrimary,
			)
		}
	}
}
