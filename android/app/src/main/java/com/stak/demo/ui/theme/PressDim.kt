package com.stak.demo.ui.theme

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.invalidateDraw
import kotlinx.coroutines.launch

/**
 * The app's press feedback (product audit, 2026-09-05: every tappable was
 * `indication = null`, so nothing acknowledged a touch). While pressed the
 * whole target darkens by 30% - a quiet dim like Instagram's and Spotify's,
 * with no ripple to fight the frames' flat surfaces. Scrims and the video
 * surfaces stay indication-free. Mirrors ios PressDimStyle.
 */
object PressDim : IndicationNodeFactory {
	override fun create(interactionSource: InteractionSource): DelegatableNode = PressDimNode(interactionSource)
	override fun equals(other: Any?): Boolean = other === this
	override fun hashCode(): Int = javaClass.hashCode()
}

private class PressDimNode(private val interactionSource: InteractionSource) : Modifier.Node(), DrawModifierNode {
	private var pressed = false

	override fun onAttach() {
		coroutineScope.launch {
			interactionSource.interactions.collect { interaction ->
				val now = when (interaction) {
					is PressInteraction.Press -> true
					is PressInteraction.Release, is PressInteraction.Cancel -> false
					else -> pressed
				}
				if (now != pressed) {
					pressed = now
					invalidateDraw()
				}
			}
		}
	}

	override fun ContentDrawScope.draw() {
		drawContent()
		// An overlay, not a layer: the fill and border usually sit BEFORE the
		// clickable in the modifier chain, so only a rect drawn on top dims
		// the whole target (a clip earlier in the chain still rounds it).
		if (pressed) drawRect(color = androidx.compose.ui.graphics.Color.Black, alpha = 0.3f)
	}
}
