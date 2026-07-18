package com.stak.demo.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.stak.demo.ui.theme.StakColors

/**
 * Onboarding back chevron — the small "<" glyph at the top-left of the
 * Figma onboarding screens. 44dp touch target around a 7x12 stroke.
 */
@Composable
fun BackChevron(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	tint: Color = StakColors.TextPrimary,
) {
	Box(
		modifier = modifier
			.size(44.dp)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			),
		contentAlignment = Alignment.Center,
	) {
		Canvas(modifier = Modifier.size(8.dp, 14.dp)) {
			val p = Path().apply {
				moveTo(size.width, 0f)
				lineTo(0f, size.height / 2f)
				lineTo(size.width, size.height)
			}
			drawPath(p, tint, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))
		}
	}
}
