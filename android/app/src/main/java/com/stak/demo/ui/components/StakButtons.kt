package com.stak.demo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors

/**
 * In-screen primary CTA — Figma "Create account" / "Continue" style:
 * 54dp gradient pill (#a9dbea → #3c98b4), Sora SemiBold 16, dark text.
 */
@Composable
fun StakPrimaryButton(
	text: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
) {
	Box(
		modifier = modifier
			.fillMaxWidth()
			.height(54.dp)
			.shadow(10.dp, RoundedCornerShape(27.dp), spotColor = StakColors.CtaGlow, ambientColor = StakColors.CtaGlow)
			.background(StakColors.ctaGradient(), RoundedCornerShape(27.dp))
			.clickable(enabled = enabled, onClick = onClick),
		contentAlignment = Alignment.Center,
	) {
		Text(
			text = text,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
			color = StakColors.Bg,
		)
	}
}

/**
 * Secondary surface button — Figma social sign-in style: #172037,
 * rgba(255,255,255,0.1) border, 52dp, radius 13, Sora SemiBold 15 white.
 * [leading] renders at the start (e.g. a provider logo), label stays centered.
 */
@Composable
fun StakSurfaceButton(
	text: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	leading: (@Composable () -> Unit)? = null,
) {
	Box(
		modifier = modifier
			.fillMaxWidth()
			.height(52.dp)
			.background(StakColors.SurfaceAlt, RoundedCornerShape(13.dp))
			.border(1.dp, StakColors.BorderSoft, RoundedCornerShape(13.dp))
			.clickable(onClick = onClick),
	) {
		if (leading != null) {
			Box(modifier = Modifier.align(Alignment.CenterStart).padding(start = 22.dp)) { leading() }
		}
		Text(
			text = text,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 15.sp),
			color = StakColors.TextPrimary,
			modifier = Modifier.align(Alignment.Center),
		)
	}
}

/** Placeholder provider dot used where the design shows a 20dp logo circle. */
@Composable
fun ProviderDot(color: Color = Color.White) {
	Box(modifier = Modifier.size(20.dp).background(color, CircleShape))
}

/** Text-link pair — "Already have an account? Log in". */
@Composable
fun StakLinkRow(
	prefix: String,
	link: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
		Text(
			text = prefix,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 13.sp),
			color = StakColors.Muted,
		)
		Text(
			text = " $link",
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 13.sp),
			color = StakColors.Accent,
			modifier = Modifier.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			),
		)
	}
}
