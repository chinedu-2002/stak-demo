package com.stak.demo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.view.TextureView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.stak.demo.navigation.StakRoot
import com.stak.demo.ui.news.NewsPip
import com.stak.demo.ui.theme.StakTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
// A FragmentActivity so BiometricPrompt can attach (the account lock, PR #167 mirror); setContent and edge-to-edge are unchanged.
class MainActivity : androidx.fragment.app.FragmentActivity() {
	/** The PiP window's rewind/play-pause/forward actions (user, 2026-08-31 video_app reference). */
	private val pipControls = object : BroadcastReceiver() {
		override fun onReceive(context: Context?, intent: Intent?) {
			if (intent?.action != NewsPip.ACTION) return
			val exo = NewsPip.player ?: return
			when (intent.getIntExtra(NewsPip.EXTRA, 0)) {
				NewsPip.PLAY -> exo.playWhenReady = true
				NewsPip.PAUSE -> exo.playWhenReady = false
				NewsPip.BACK15 -> exo.seekTo((exo.currentPosition - 15_000L).coerceAtLeast(0L))
				NewsPip.FWD15 -> exo.seekTo(
					if (exo.duration > 0) (exo.currentPosition + 15_000L).coerceAtMost(exo.duration)
					else exo.currentPosition + 15_000L,
				)
			}
			NewsPip.refresh(this@MainActivity)
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// The app is navy on every screen: force LIGHT status/navigation-bar
		// icons. The parameterless call picks icon colours from the device
		// theme, so a light-themed phone got black icons on the navy header
		// (Codex audit 2026-09-04). iOS pins the same via UIUserInterfaceStyle.
		enableEdgeToEdge(
			statusBarStyle = androidx.activity.SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
			navigationBarStyle = androidx.activity.SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
		)
		ContextCompat.registerReceiver(
			this, pipControls, IntentFilter(NewsPip.ACTION), ContextCompat.RECEIVER_NOT_EXPORTED,
		)
		setContent {
			StakTheme {
				Box(modifier = Modifier.fillMaxSize()) {
					StakRoot()
					// The OS PiP window shows this activity scaled down: while
					// shrunk, only the video renders. The article stays composed
					// beneath (it owns the player); composed AFTER StakRoot, this
					// TextureView claims the player's surface, and leaving PiP
					// removes it so the hero's own view re-claims (both sides
					// clear per-view, never blindly).
					val exo = NewsPip.player
					if (NewsPip.inPip && exo != null) {
						key(exo) {
							Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
								AndroidView(
									modifier = Modifier.fillMaxSize(),
									factory = { c -> TextureView(c).also { exo.setVideoTextureView(it) } },
									onRelease = { v -> exo.clearVideoTextureView(v) },
								)
							}
						}
					}
				}
			}
		}
	}

	override fun onStop() {
		super.onStop()
		// Closing the PiP window's X (or leaving the app with no PiP up,
		// e.g. via the task switcher) stops this activity - the newscast
		// must not keep talking invisibly in the background. In PiP the
		// video is visible, so it keeps playing.
		if (!NewsPip.inPip) NewsPip.player?.playWhenReady = false
	}

	override fun onDestroy() {
		unregisterReceiver(pipControls)
		super.onDestroy()
	}

	override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
		super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
		NewsPip.inPip = isInPictureInPictureMode
	}

	override fun onUserLeaveHint() {
		super.onUserLeaveHint()
		// Leaving the app while a news clip plays floats it, like the
		// reference app (home gesture -> mini-player keeps going).
		if (!NewsPip.inPip && NewsPip.player?.playWhenReady == true) NewsPip.enter(this)
	}
}
