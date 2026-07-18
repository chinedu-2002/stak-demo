package com.stak.demo.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors

enum class StakTab(val label: String) {
	Home("Home"),
	Discover("Discover"),
	Watchlist("Watchlist"),
	Simulate("Simulate"),
	Profile("Profile"),
}

/**
 * Bottom tab bar — Figma "Tab bar" (node 36:379): #10172a, 1px top hairline,
 * 56dp of content, 10sp labels (active #39c5cb SemiBold, inactive #819abb).
 * Icons are drawn from the component's exact vector geometry.
 */
@Composable
fun StakTabBar(
	selected: StakTab,
	onSelect: (StakTab) -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(modifier = modifier.fillMaxWidth().background(StakColors.Surface)) {
		Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(StakColors.Divider))
		Row(modifier = Modifier.fillMaxWidth().navigationBarsPadding()) {
			StakTab.entries.forEach { tab ->
				val active = tab == selected
				val tint = if (active) StakColors.Accent else StakColors.Muted
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					modifier = Modifier
						.weight(1f)
						.height(56.dp)
						.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
						) { onSelect(tab) },
				) {
					Canvas(modifier = Modifier.padding(top = 10.dp).size(22.dp, 18.dp)) {
						drawTabIcon(tab, tint)
					}
					Text(
						text = tab.label,
						style = TextStyle(
							fontFamily = Sora,
							fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
							fontSize = 10.sp,
						),
						color = tint,
						modifier = Modifier.padding(top = 2.dp),
					)
				}
			}
		}
	}
}

/** Icon geometry lifted from Figma node 36:379 (a 22x18 box, centered). */
private fun DrawScope.drawTabIcon(tab: StakTab, tint: Color) {
	val u = size.height / 18f // 1 Figma px
	fun rr(x: Float, y: Float, w: Float, h: Float, r: Float) {
		drawRoundRect(tint, Offset(x * u, y * u), Size(w * u, h * u), CornerRadius(r * u, r * u))
	}
	when (tab) {
		// House: M0.5 7.63 L9.5 0.63 L18.5 7.63 V16.63 H0.5 Z (19x17), centered in 22.
		StakTab.Home -> {
			val ox = (size.width - 19f * u) / 2f
			val p = Path().apply {
				moveTo(ox + 0.5f * u, 8f * u)
				lineTo(ox + 9.5f * u, 1f * u)
				lineTo(ox + 18.5f * u, 8f * u)
				lineTo(ox + 18.5f * u, 17f * u)
				lineTo(ox + 0.5f * u, 17f * u)
				close()
			}
			drawPath(p, tint)
		}
		// 2x2 grid of 8px rounded squares.
		StakTab.Discover -> {
			val ox = (size.width - 16f * u) / 2f / u
			rr(ox, 1f, 8f, 8f, 2f); rr(ox + 8f, 1f, 8f, 8f, 2f)
			rr(ox, 9f, 8f, 8f, 2f); rr(ox + 8f, 9f, 8f, 8f, 2f)
		}
		// Bookmark: M0.5 0.5 H12.5 V17.5 L6.5 12.5 L0.5 17.5 Z (13x18).
		StakTab.Watchlist -> {
			val ox = (size.width - 13f * u) / 2f
			val p = Path().apply {
				moveTo(ox + 0.5f * u, 0.5f * u)
				lineTo(ox + 12.5f * u, 0.5f * u)
				lineTo(ox + 12.5f * u, 17.5f * u)
				lineTo(ox + 6.5f * u, 12.5f * u)
				lineTo(ox + 0.5f * u, 17.5f * u)
				close()
			}
			drawPath(p, tint)
		}
		// Four bars: widths 4, heights 6/12/9/16 sharing a 28-baseline (icon-local 18).
		StakTab.Simulate -> {
			val ox = (size.width - 22f * u) / 2f / u
			rr(ox, 12f, 4f, 6f, 1f)
			rr(ox + 6f, 6f, 4f, 12f, 1f)
			rr(ox + 12f, 9f, 4f, 9f, 1f)
			rr(ox + 18f, 2f, 4f, 16f, 1f)
		}
		// Head circle (14) + rounded-top body (22x11 with 8 top radius).
		StakTab.Profile -> {
			val cx = size.width / 2f
			drawCircle(tint, radius = 5f * u, center = Offset(cx, 5f * u))
			val body = Path().apply {
				addRoundRect(
					RoundRect(
						left = cx - 9f * u, top = 12f * u, right = cx + 9f * u, bottom = 18f * u,
						topLeftCornerRadius = CornerRadius(8f * u, 8f * u),
						topRightCornerRadius = CornerRadius(8f * u, 8f * u),
						bottomLeftCornerRadius = CornerRadius.Zero,
						bottomRightCornerRadius = CornerRadius.Zero,
					),
				)
			}
			drawPath(body, tint)
		}
	}
}
