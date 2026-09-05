package com.stak.demo.ui.news

import android.content.Context
import android.view.TextureView
import android.widget.FrameLayout
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.exoplayer.ExoPlayer
import kotlin.math.roundToInt

/**
 * Hosts a clip's TextureView at the VIDEO's aspect ratio inside whatever
 * box Compose gives it - never stretched (a bare TextureView fills its
 * bounds and distorts; user, 2026-09-05 fullscreen screenshot). `zoom`
 * fills the box and clips the overflow (the inline hero and the mini
 * player, like the frame's cropped hero image); otherwise the video fits
 * inside, centred, on the host's background (fullscreen, letterboxed like
 * a Netflix player). The ratio is the player's reported video size, 16:9
 * until it is known. TextureView, not SurfaceView - see NewsVideoPlayer.
 */
internal class VideoSurfaceHost(context: Context, private val exo: ExoPlayer, private val zoom: Boolean) : FrameLayout(context) {
	private val texture = TextureView(context)
	private var aspect = 16f / 9f
	private val listener = object : Player.Listener {
		override fun onVideoSizeChanged(videoSize: VideoSize) = apply(videoSize)
	}

	init {
		clipChildren = true
		addView(texture, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
		apply(exo.videoSize)
		exo.addListener(listener)
		exo.setVideoTextureView(texture)
	}

	private fun apply(size: VideoSize) {
		if (size.width <= 0 || size.height <= 0) return
		val a = size.width * size.pixelWidthHeightRatio / size.height
		if (a != aspect) {
			aspect = a
			requestLayout()
		}
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		val w = MeasureSpec.getSize(widthMeasureSpec)
		val h = MeasureSpec.getSize(heightMeasureSpec)
		if (w == 0 || h == 0) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec)
			return
		}
		val boxAspect = w.toFloat() / h
		// Fit: the wider box constrains by height. Zoom: the wider box fills its width and overflows vertically.
		val byHeight = if (zoom) boxAspect < aspect else boxAspect > aspect
		val cw = if (byHeight) (h * aspect).roundToInt() else w
		val ch = if (byHeight) h else (w / aspect).roundToInt()
		texture.measure(MeasureSpec.makeMeasureSpec(cw, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(ch, MeasureSpec.EXACTLY))
		setMeasuredDimension(w, h)
	}

	override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
		val cw = texture.measuredWidth
		val ch = texture.measuredHeight
		val x = ((right - left) - cw) / 2
		val y = ((bottom - top) - ch) / 2
		texture.layout(x, y, x + cw, y + ch)
	}

	/** Detaches THIS host's surface only - the mini player or the fullscreen view may already own the player. */
	fun release() {
		exo.removeListener(listener)
		exo.clearVideoTextureView(texture)
	}
}
