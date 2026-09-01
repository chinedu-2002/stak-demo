package com.stak.demo.ui.news

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PictureInPictureAlt
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.R
import com.stak.demo.ui.onboarding.AuthBackCircle
import com.stak.demo.ui.theme.Geist
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors
import com.stak.demo.ui.theme.ADVANCE_ROUNDING

/** The CHINEDU CTA gradient (Add to STAK / View in My STAK). */
private val CtaGradient = Brush.verticalGradient(
	0.0889f to Color(0xFFA6E4F7),
	0.3919f to Color(0xFF5DA8BF),
	0.7255f to Color(0xFF3C98B4),
	1f to Color(0xFF3C98B4),
)
private val CtaBorder = Brush.verticalGradient(
	0f to Color(0xA1659EAD),
	1f to Color(0x6E16363F),
)

/**
 * 03 · News — the article page in its three frames: "News detail page
 * unsaved" (1:1495), "News detail · Save success" (101:1005, the bottom
 * sheet over a scrim) and "News detail page saved" (1:1359 — hero toast,
 * View-in-My-STAK row on the stock card, Apple + Tech tags).
 */
@Composable
fun NewsDetailScreen(articleId: String = NewsArticleFeed.APPLE, onBack: () -> Unit, onViewInMyStak: () -> Unit = {}, onOpenArticle: (String) -> Unit = {}) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	// The served article for the tapped story (user, 2026-08-25); the
	// Apple article is the authored one and renders frame-exact.
	val article = NewsArticleFeed.article(articleId)
	val isAuthored = article.id == NewsArticleFeed.APPLE
	var saved by rememberSaveable { mutableStateOf(false) }
	var showSuccess by rememberSaveable { mutableStateOf(false) }

	Box(modifier = Modifier.fillMaxSize().background(StakColors.Bg)) {
		Column(modifier = Modifier.fillMaxSize()) {
			// Fixed top bar — back circle + share.
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.fillMaxWidth()
					.background(StakColors.Bg)
					.statusBarsPadding()
					.padding(start = (16 * u).dp, end = (18 * u).dp, top = (10 * u).dp, bottom = (12 * u).dp),
			) {
				AuthBackCircle(onClick = onBack)
				Spacer(modifier = Modifier.weight(1f))
				// Designer's call (2026-08-22): share creates a link that takes
				// a co-app user to the shared info - the system share sheet.
				val context = androidx.compose.ui.platform.LocalContext.current
				Image(
					painter = painterResource(R.drawable.ic_news_share),
					contentDescription = "Share",
					modifier = Modifier
						.size((24 * u).dp)
						.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
						) {
							val send = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
								type = "text/plain"
								putExtra(android.content.Intent.EXTRA_TEXT, article.shareText)
							}
							context.startActivity(android.content.Intent.createChooser(send, "Share article"))
						},
				)
			}
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				modifier = Modifier
					.weight(1f)
					.fillMaxWidth()
					.verticalScroll(rememberScrollState()),
			) {
				HeroImage(media = article.media, category = article.category, saved = saved, onBookmark = { saved = true; article.ticker?.let { com.stak.demo.ui.MyStakHoldings.add(it) } })
				Column(
					verticalArrangement = Arrangement.spacedBy((15 * u).dp),
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = (20 * u).dp)
						// Render-measured vs 1:1495: headline cap-top 68 below the hero.
						.padding(top = (26 * u).dp, bottom = (28 * u).dp),
				) {
					Text(
						text = article.headline,
						// RENDER-measured: the frame draws the headline ~800 device px
					// wide (≈20sp), not the metadata's 24 — lh32 box stands.
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (20 * u).sp, lineHeight = (32 * u).sp),
						color = Color.White,
					)
					Text(
						text = article.subtitle,
						// 14.3: at 14 Compose pulls "in" up to line 1; the frame
						// breaks after "lineup" (authored 3-line shape, lh22).
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (14.3 * u).sp, lineHeight = (22 * u).sp),
						color = News.Muted,
						// 1:1495: 26 of ink gap under the headline (the column's 15 + 5).
						modifier = Modifier.padding(top = (5 * u).dp),
					)
					Byline(source = article.source, meta = article.sourceMeta)
					if (!saved) {
						AddToStakButton(onClick = { showSuccess = true })
					}
					Divider()
					// ONE template for every story (user, 2026-08-25: the Apple
					// article is the section's PLACEHOLDER - each block renders
					// per story from served data; stock blocks appear whenever
					// the story has a related ticker).
					article.ticker?.let { StockCard(saved = saved, ticker = it, facts = NewsArticleFeed.stockFacts(it)) }
					if (article.gist.isNotEmpty()) GistCard(bullets = article.gist)
					article.paragraphs.getOrNull(0)?.let { Paragraph(it, size = 15.sp, line = 24.sp) }
					article.paragraphs.getOrNull(1)?.let { Paragraph(it) }
					article.pullQuote?.let { PullQuote(it) }
					article.explainer?.let { NewToThisCard(it) }
					article.paragraphs.drop(2).forEach { Paragraph(it) }
					SourceRow()
					article.ticker?.let { KeyStatsCard(facts = NewsArticleFeed.stockFacts(it)) }
					Divider()
					Row(horizontalArrangement = Arrangement.spacedBy((8 * u).dp)) {
						article.tags.getOrNull(0)?.let { ArticleTag(it) }
						if (saved) {
							article.tags.getOrNull(1)?.let { ArticleTag(it) }
						}
					}
					ReadNext(currentId = article.id, onOpen = onOpenArticle)
				}
			}
		}
		// Authored (101:1005 Motion): Back -> News detail page saved,
		// DISSOLVE 300 EaseOut; View in My STAK -> My STAK Overview,
		// Push Right 300 (hoisted to the nav). Entry stays instant (its
		// authored animate type is still unreadable from the file).
		AnimatedVisibility(
			visible = showSuccess,
			// Designer's call (2026-08-22): the sheet appears with a
			// SCALE-IN animation; it still dissolves out per 101:1005.
			enter = scaleIn(initialScale = 0.92f, animationSpec = tween(300, easing = EaseOut)) +
				fadeIn(tween(300, easing = EaseOut)),
			exit = fadeOut(tween(300, easing = EaseOut)),
		) {
			SaveSuccessOverlay(
				facts = NewsArticleFeed.stockFacts(article.ticker ?: "AAPL"),
				onViewInMyStak = { saved = true; article.ticker?.let { com.stak.demo.ui.MyStakHoldings.add(it) }; onViewInMyStak() },
				onDismiss = { showSuccess = false; saved = true; article.ticker?.let { com.stak.demo.ui.MyStakHoldings.add(it) } },
			)
		}
	}
}

/** 360x208 r10 hero — phone art, Tech & Ai toast, play badge, bookmark/saved chip. */
@Composable
// CONTRACT (user, 2026-08-25): this screen is the News info page TEMPLATE.
// The design authors exactly ONE article (1:1495, the Apple foldable
// story), so every authored news tap lands here in the demo. In
// production the backend serves each story's own headline, subtitle,
// body and media into this page - same slot pattern as NewsMedia.
private fun HeroImage(media: NewsMedia, category: String, saved: Boolean, onBookmark: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	// The hero is a media slot: poster + play glyph at rest (frame-exact),
	// the served video playing IN PLACE once tapped (user, 2026-08-23).
	var playing by remember { mutableStateOf(false) }
	// The play button toggles: tap the running clip to pause (the glyph returns
	// over the paused frame), tap the glyph to resume (user, 2026-08-30).
	var paused by remember { mutableStateOf(false) }
	// Cinema-fast start (user, 2026-08-30): the clip starts buffering the
	// moment the article opens (playWhenReady=false), streams through a
	// disk cache so replays begin instantly, and the poster holds until
	// the first real frame is rendered - never a gray box. Rate stays 1x.
	val heroVideo = media as? NewsMedia.Video
	val directUrl = if (heroVideo != null && heroVideo.youTubeEmbedUrl == null) heroVideo.url else null
	val heroCtx = androidx.compose.ui.platform.LocalContext.current
	val exo = remember(directUrl) { if (directUrl != null) NewsVideoCache.preparedPlayer(heroCtx, directUrl) else null }
	var firstFrame by remember { mutableStateOf(false) }
	androidx.compose.runtime.DisposableEffect(exo) {
		val listener = object : androidx.media3.common.Player.Listener {
			override fun onRenderedFirstFrame() { firstFrame = true }
			override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
				android.util.Log.w("NewsMedia", "direct playback error " + error.errorCodeName + " for " + directUrl)
				playing = false; paused = false
			}
			override fun onPlaybackStateChanged(playbackState: Int) {
				if (playbackState == androidx.media3.common.Player.STATE_ENDED) {
					playing = false; paused = false; firstFrame = false
					exo?.seekTo(0); exo?.playWhenReady = false
				}
			}
		}
		exo?.addListener(listener)
		// The active hero player registers for the OS PiP window's remote
		// controls + video-only render (user, 2026-08-31 video_app reference).
		if (exo != null) NewsPip.player = exo
		onDispose {
			if (NewsPip.player === exo) NewsPip.player = null
			exo?.removeListener(listener); exo?.release()
		}
	}
	// The controls own pause/seek once playing; this starts playback on the
	// play-glyph tap and stops it when the hero returns to the poster.
	androidx.compose.runtime.LaunchedEffect(playing) { exo?.playWhenReady = playing }
	Box(
		modifier = Modifier
			.padding(horizontal = (15 * u).dp)
			.fillMaxWidth()
			.height((208 * u).dp)
			// Authored radius 10 (1:1517 Inspect) - the earlier 24 was wrong.
			.clip(RoundedCornerShape((10 * u).dp))
			.background(Color(0xFFC4C4C4)),
	) {
		val video = media as? NewsMedia.Video
		if (playing && video != null) {
			// A failed OR FINISHED stream returns to the poster + glyph
			// instead of stranding a frame (user, 2026-08-25/26).
			if (exo != null) {
				// Direct clip: the pre-buffered cached player with the full
				// reference controller (user, 2026-08-31 video_app shots:
				// +/-15s skips, times, seek bar, captions/fullscreen/menu,
				// audio-rates-quality sheet, system PiP).
				var fullscreen by remember { mutableStateOf(false) }
				val activity = remember(heroCtx) {
					generateSequence(heroCtx) { (it as? android.content.ContextWrapper)?.baseContext }
						.filterIsInstance<android.app.Activity>().firstOrNull()
				}
				val onPip: () -> Unit = { fullscreen = false; activity?.let { NewsPip.enter(it) } }
				if (NewsPip.inPip) {
					// Reference image 3: while the clip floats in the OS
					// mini-player the hero holds the PiP placeholder
					// (MainActivity renders the video into the PiP window).
					PipPlaceholder(u = u, modifier = Modifier.matchParentSize())
				} else if (!fullscreen) {
					androidx.compose.ui.viewinterop.AndroidView(
						modifier = Modifier.matchParentSize(),
						factory = { c -> android.view.TextureView(c).also { exo.setVideoTextureView(it) } },
						// Per-view clear (never clearVideoSurface): PiP's overlay
						// or the fullscreen view may already own the surface.
						onRelease = { v -> exo.clearVideoTextureView(v) },
					)
					HeroControls(exo = exo, u = u, onFullscreen = { fullscreen = true }, onPip = onPip, modifier = Modifier.matchParentSize())
				} else {
					androidx.compose.ui.window.Dialog(
						onDismissRequest = { fullscreen = false },
						properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
					) {
						Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
							androidx.compose.ui.viewinterop.AndroidView(
								modifier = Modifier.matchParentSize(),
								factory = { c -> android.view.TextureView(c).also { exo.setVideoTextureView(it) } },
								onRelease = { v -> exo.clearVideoTextureView(v) },
							)
							HeroControls(exo = exo, u = u, onFullscreen = { fullscreen = false }, onPip = onPip, fullscreen = true, modifier = Modifier.matchParentSize())
						}
					}
				}
				if (!firstFrame && !NewsPip.inPip) {
					// Poster holds until onRenderedFirstFrame.
				val posterRes2 = (media as NewsMedia.Video).posterRes
				if (posterRes2 != null) {
					Image(
						painter = painterResource(posterRes2),
						contentDescription = null,
						contentScale = ContentScale.Crop,
						modifier = Modifier
							.align(Alignment.Center)
							.offset(x = (-0.5 * u).dp, y = (16.59 * u).dp)
							.requiredSize((407 * u).dp, (271.18 * u).dp),
					)
				} else if (media.posterUrl != null) {
					coil.compose.AsyncImage(
						model = media.posterUrl,
						contentDescription = null,
						contentScale = ContentScale.Crop,
						modifier = Modifier.matchParentSize(),
					)
				}
				}
			} else {
				NewsVideoPlayer(
					video = video,
					paused = paused,
					modifier = Modifier
						.matchParentSize()
						.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
						) { paused = true },
					onDone = { playing = false; paused = false },
				)
			}
			if (exo == null && paused) {
				Image(
					painter = painterResource(R.drawable.ic_hero_play),
					contentDescription = "Play",
					modifier = Modifier
						.align(Alignment.Center)
						.offset(x = (-0.5 * u).dp, y = (12.5 * u).dp)
						.rotate(90f)
						.size((59.92 * u).dp, (53.75 * u).dp)
						.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
						) { paused = false },
				)
			}
		} else {
			val posterRes = when (media) {
				is NewsMedia.Image -> media.posterRes
				is NewsMedia.Video -> media.posterRes
			}
			// The authored image is oversized (407x271.18 in the 360x208 card,
			// top-left at -24,-15) — requiredSize so the card's constraints
			// don't shrink it back to 360x208 and letterbox the art.
			val posterUrl = when (media) {
				is NewsMedia.Image -> media.url
				is NewsMedia.Video -> media.posterUrl
			}
			if (posterRes != null) {
				Image(
					painter = painterResource(posterRes),
					contentDescription = null,
					contentScale = ContentScale.Crop,
					modifier = Modifier
						.align(Alignment.Center)
						.offset(x = (-0.5 * u).dp, y = (16.59 * u).dp)
						.requiredSize((407 * u).dp, (271.18 * u).dp),
				)
			} else if (posterUrl != null) {
				// Served poster/image: fills the authored box.
				coil.compose.AsyncImage(
					model = posterUrl,
					contentDescription = null,
					contentScale = ContentScale.Crop,
					modifier = Modifier.matchParentSize(),
				)
			}
			if (video != null) {
				Image(
					painter = painterResource(R.drawable.ic_hero_play),
					contentDescription = "Play",
					modifier = Modifier
						.align(Alignment.Center)
						.offset(x = (-0.5 * u).dp, y = (12.5 * u).dp)
						.rotate(90f)
						.size((59.92 * u).dp, (53.75 * u).dp)
						.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
						) { playing = true; paused = false },
				)
			}
			// An image with an embedded link (served contract): tapping the
			// hero opens the story's own dynamic link.
			val imageLink = (media as? NewsMedia.Image)?.sourceLink
			if (imageLink != null) {
				Box(
					modifier = Modifier
						.matchParentSize()
						.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
						) {
							runCatching {
								heroCtx.startActivity(
									android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(imageLink)),
								)
							}
						},
				)
			}
		}
		// The category chip is the REST state's (frame-authored); the playing
		// canvas stays clean for the controls (user, 2026-08-30 sleek bar).
		if (!(playing && video != null)) {
			Box(
				modifier = Modifier
					.align(Alignment.BottomStart)
					.padding(start = (9 * u).dp, bottom = (10 * u).dp)
					.clip(RoundedCornerShape((7.88 * u).dp))
					.background(Color(0x40242B3D))
					.padding(horizontal = (7 * u).dp, vertical = (5 * u).dp),
			) {
				Text(
					text = category,
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, lineHeight = (13 * u).sp),
					color = Color.White,
				)
			}
		}
		// The bookmark/saved chip is also REST-state chrome: while playing,
		// the hero's top-right corner belongs to the player's PiP button
		// (2026-08-31: the chip sat OVER it and swallowed the tap).
		if (playing && video != null) {
			// player chrome owns the canvas
		} else if (saved) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy((4 * u).dp),
				modifier = Modifier
					.align(Alignment.TopEnd)
					.padding(top = (8 * u).dp, end = (7 * u).dp)
					.clip(RoundedCornerShape((7.88 * u).dp))
					.background(Color(0x40242B3D))
					// Authored toast is 21 tall (1:1386): 13 text + 4/4 pads.
					.padding(horizontal = (7 * u).dp, vertical = (4 * u).dp),
			) {
				Image(
					painter = painterResource(R.drawable.ic_saved_bookmark),
					contentDescription = null,
					modifier = Modifier.size((11 * u).dp),
				)
				Text(
					text = "Saved to My STAK",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp),
					color = Color.White,
				)
			}
		} else {
			// Authored motion (1:1495): the hero bookmark -> News detail page
			// saved, Instant - a direct save that skips the success sheet.
			Image(
				painter = painterResource(R.drawable.ic_hero_bookmark),
				contentDescription = "Save",
				modifier = Modifier
					.align(Alignment.TopEnd)
					.padding(top = (8 * u).dp, end = (11 * u).dp)
					.size((17.79 * u).dp, (18.27 * u).dp)
					.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
						onClick = onBookmark,
					),
			)
		}
	}
}

/** Templated per story - the authored sample is "Bloomberg · Jul 2 · 3 min read". */
@Composable
private fun Byline(source: String, meta: String) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy((8 * u).dp),
		modifier = Modifier.padding(vertical = (2 * u).dp),
	) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier.size((24 * u).dp).background(News.ChipBg, CircleShape),
		) {
			Text(
				text = source.take(1),
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (10 * u).sp, lineHeight = (13 * u).sp),
				color = Color(0xFF9EADC7),
			)
		}
		Row(horizontalArrangement = Arrangement.spacedBy((5 * u).dp), verticalAlignment = Alignment.CenterVertically) {
			Text(
				text = source,
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
				color = Color.White,
			)
			Text(
				text = meta,
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
				color = News.Faint,
			)
		}
	}
}

/** 150x52 gradient CTA with the plus mark. */
@Composable
private fun AddToStakButton(onClick: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy((8 * u).dp, Alignment.CenterHorizontally),
		modifier = Modifier
			.size((150 * u).dp, (52 * u).dp)
			.background(CtaGradient, RoundedCornerShape((6 * u).dp))
			.border((0.36 * u).dp, CtaBorder, RoundedCornerShape((6 * u).dp))
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			),
	) {
		Text(
			text = "Add to STAK",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (14 * u).sp, lineHeight = (21 * u).sp),
			color = Color.White,
		)
		Image(
			painter = painterResource(R.drawable.ic_plus_small),
			contentDescription = null,
			modifier = Modifier.size((14 * u).dp),
		)
	}
}

@Composable
private fun Divider() {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Box(modifier = Modifier.fillMaxWidth().height((1 * u).dp).background(News.Divider))
}

/**
 * The story's stock card - EVERY article renders it with its own
 * stock's served facts (user, 2026-08-25: "use the apple features...
 * replace the placeholder"). The sparkline stays the authored demo
 * asset until the backend serves chart data.
 */
@Composable
private fun StockCard(saved: Boolean, ticker: String, facts: NewsArticleFeed.StockFacts) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((13 * u).dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((16 * u).dp))
			.background(News.CardBg)
			.padding(start = (16 * u).dp, end = (16 * u).dp, top = (16 * u).dp, bottom = (14 * u).dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier.size((44 * u).dp).background(News.ChipBg, CircleShape),
			) {
				Text(
					text = facts.name.take(1),
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (18 * u).sp, lineHeight = (23 * u).sp),
					color = Color(0xFF9EADC7),
				)
			}
			Spacer(modifier = Modifier.width((12 * u).dp))
			Column(verticalArrangement = Arrangement.spacedBy((3 * u).dp)) {
				Text(
					text = facts.name,
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp, lineHeight = (19 * u).sp),
					color = Color.White,
				)
				Text(
					text = ticker,
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
					color = News.Muted,
				)
			}
			Spacer(modifier = Modifier.weight(1f))
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy((5 * u).dp),
				modifier = Modifier
					.clip(RoundedCornerShape((6 * u).dp))
					.background(Color(0x403E4958))
					.padding(horizontal = (10 * u).dp, vertical = (4 * u).dp),
			) {
				Text(
					text = "Daily",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (8 * u).sp, lineHeight = (10 * u).sp, letterSpacing = (0.4 * u + ADVANCE_ROUNDING.value).sp),
					color = Color.White,
				)
				Image(
					painter = painterResource(R.drawable.ic_daily_chevron),
					contentDescription = null,
					modifier = Modifier.size((6.53 * u).dp, (3.56 * u).dp),
				)
			}
		}
		Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.fillMaxWidth()) {
			Column(verticalArrangement = Arrangement.spacedBy((3 * u).dp)) {
				Text(
					text = facts.price,
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (26 * u).sp, lineHeight = (33 * u).sp),
					color = Color.White,
				)
				Text(
					text = facts.change,
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (13 * u).sp, lineHeight = (17 * u).sp),
					// The app's authored up/down pair (green here, the
					// My STAK / Simulate red for down moves).
					color = if (facts.up) News.Green else Color(0xFFFF5A6A),
				)
			}
			Spacer(modifier = Modifier.weight(1f))
			Image(
				// The chart must agree with the move (user, 2026-08-25:
				// "showing red then the graph is green") - the down variant
				// is the authored line flipped + recolored to the app red.
				painter = painterResource(if (facts.up) R.drawable.news_sparkline else R.drawable.news_sparkline_down),
				contentDescription = null,
				modifier = Modifier.size((110 * u).dp, (40 * u).dp),
			)
		}
		if (saved) {
			Box(modifier = Modifier.fillMaxWidth().height((1 * u).dp).background(News.Divider))
			Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
				Text(
					text = "View $ticker in My STAK",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (13 * u).sp, lineHeight = (17 * u).sp),
					color = News.Teal,
				)
				Spacer(modifier = Modifier.weight(1f))
				Image(
					painter = painterResource(R.drawable.ic_daily_chevron),
					contentDescription = null,
					modifier = Modifier.rotate(-90f).size((6.88 * u).dp, (3.75 * u).dp),
				)
			}
		}
	}
}

/** "The gist" — sparkle header + three check bullets. */
@Composable
private fun GistCard(bullets: List<String>) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((12 * u).dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((14 * u).dp))
			.background(News.CardBg)
			.padding((16 * u).dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((8 * u).dp)) {
			Image(painterResource(R.drawable.ic_gist_sparkle), null, modifier = Modifier.size((18 * u).dp))
			Text(
				text = "The gist",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (14 * u).sp, lineHeight = (18 * u).sp),
				color = Color.White,
			)
		}
		bullets.forEach { GistBullet(it) }
	}
}

@Composable
private fun GistBullet(text: String) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Row(horizontalArrangement = Arrangement.spacedBy((10 * u).dp), modifier = Modifier.fillMaxWidth()) {
		Image(painterResource(R.drawable.ic_gist_check), null, modifier = Modifier.size((16 * u).dp))
		Text(
			text = text,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (13 * u).sp, lineHeight = (19 * u).sp),
			color = News.Body,
			modifier = Modifier.weight(1f),
		)
	}
}

@Composable
private fun Paragraph(text: String, size: androidx.compose.ui.unit.TextUnit = 14.sp, line: androidx.compose.ui.unit.TextUnit = 23.sp) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Text(
		text = text,
		style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = size * u, lineHeight = line * u),
		color = News.Body,
	)
}

@Composable
private fun PullQuote(text: String) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Row(
		horizontalArrangement = Arrangement.spacedBy((14 * u).dp),
		modifier = Modifier.fillMaxWidth().padding(start = (2 * u).dp, top = (6 * u).dp, bottom = (6 * u).dp).height(androidx.compose.foundation.layout.IntrinsicSize.Min),
	) {
		Box(
			modifier = Modifier
				.width((3 * u).dp)
				.fillMaxSize()
				.background(News.Teal, RoundedCornerShape((2 * u).dp)),
		)
		Text(
			text = text,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (16 * u).sp, lineHeight = (26 * u).sp),
			color = Color(0xFFD3D3DD),
			modifier = Modifier.weight(1f),
		)
	}
}

@Composable
private fun NewToThisCard(body: String) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((9 * u).dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((14 * u).dp))
			.background(News.CardBg)
			.padding((16 * u).dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((8 * u).dp)) {
			Image(painterResource(R.drawable.ic_gist_help), null, modifier = Modifier.size((18 * u).dp))
			Text(
				text = "New to this?",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (13 * u).sp, lineHeight = (18 * u).sp),
				color = News.Teal,
			)
		}
		Text(
			text = body,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (13 * u).sp, lineHeight = (20 * u).sp),
			color = News.Body,
		)
	}
}

@Composable
private fun SourceRow() {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier.width((81 * u).dp),
	) {
		Text(
			text = "Source",
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = (13 * u).sp),
			color = Color.White,
		)
		Spacer(modifier = Modifier.weight(1f))
		Image(painterResource(R.drawable.ic_news_external), null, modifier = Modifier.size((19 * u).dp))
	}
}

@Composable
private fun KeyStatsCard(facts: NewsArticleFeed.StockFacts) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((13 * u).dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((14 * u).dp))
			.background(News.CardBg)
			.padding((16 * u).dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((8 * u).dp)) {
			Image(painterResource(R.drawable.ic_gist_info), null, modifier = Modifier.size((18 * u).dp))
			Text(
				text = "Key stats",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (14 * u).sp, lineHeight = (18 * u).sp),
				color = Color.White,
			)
		}
		StatRow("Market cap", facts.marketCap, "P/E ratio", facts.peRatio)
		StatRow("Day range", facts.dayRange, "Volume", facts.volume)
		StatRow("52-wk range", facts.week52, "Div yield", facts.divYield)
	}
}

@Composable
private fun StatRow(l1: String, v1: String, l2: String, v2: String) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Row(horizontalArrangement = Arrangement.spacedBy((14 * u).dp), modifier = Modifier.fillMaxWidth()) {
		StatCell(l1, v1, modifier = Modifier.width((212 * u).dp))
		StatCell(l2, v2)
	}
}

@Composable
private fun StatCell(label: String, value: String, modifier: Modifier = Modifier) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(verticalArrangement = Arrangement.spacedBy((3 * u).dp), modifier = modifier) {
		Text(
			text = label,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (11 * u).sp, lineHeight = (14 * u).sp),
			color = News.Faint,
		)
		Text(
			text = value,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (13 * u).sp, lineHeight = (16 * u).sp),
			color = Color.White,
		)
	}
}

/** rgba(105,179,202,0.1) r12 tag chip. */
@Composable
private fun ArticleTag(text: String) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Box(
		modifier = Modifier
			.clip(RoundedCornerShape((12 * u).dp))
			.background(Color(0x1A69B3CA))
			.padding(horizontal = (11 * u).dp, vertical = (5 * u).dp),
	) {
		Text(
			text = text,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp, lineHeight = (14 * u).sp),
			color = News.Muted,
		)
	}
}

/**
 * READ NEXT - the authored sample repeats the Tesla row twice as its
 * placeholder; the served rows are two other stories, and each opens
 * its own article (user, 2026-08-25).
 */
@Composable
private fun ReadNext(currentId: String, onOpen: (String) -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(verticalArrangement = Arrangement.spacedBy((10 * u).dp), modifier = Modifier.fillMaxWidth().padding(top = (6 * u).dp)) {
		Text(
			text = "READ NEXT",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, lineHeight = (13 * u).sp, letterSpacing = (0.5 * u + ADVANCE_ROUNDING.value).sp),
			color = News.Muted,
		)
		NewsArticleFeed.readNext(excluding = currentId).forEach { next ->
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy((12 * u).dp),
				modifier = Modifier
					.fillMaxWidth()
					.clip(RoundedCornerShape((14 * u).dp))
					.background(News.CardBg)
					.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
						onClick = { onOpen(next.id) },
					)
					.padding((12 * u).dp),
			) {
				next.thumbRes?.let { thumbRes ->
					Image(
						painter = painterResource(thumbRes),
						contentDescription = null,
						contentScale = ContentScale.Crop,
						modifier = Modifier.size((60 * u).dp).clip(RoundedCornerShape((10 * u).dp)),
					)
				}
				Column(verticalArrangement = Arrangement.spacedBy((5 * u).dp), modifier = Modifier.weight(1f)) {
					Text(
						text = "${next.source} · ${next.age}",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (11 * u).sp, lineHeight = (14 * u).sp),
						color = News.Muted,
					)
					Text(
						text = next.headline,
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = (14 * u).sp, lineHeight = (19 * u).sp),
						color = Color.White,
					)
				}
			}
		}
	}
}

/**
 * Save success — rgba(12,19,32,0.55) scrim + the r24 #181f30 bottom
 * sheet (101:1169). The stock row shows the SAVED stock's served facts
 * (user, 2026-08-25: every article = the full Apple page with the
 * story's own content; the frame's Apple row was placeholder).
 */
@Composable
private fun SaveSuccessOverlay(facts: NewsArticleFeed.StockFacts, onViewInMyStak: () -> Unit, onDismiss: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Box(modifier = Modifier.fillMaxSize()) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				// Authored scrim rgba(12,19,32,0.55) (101:1168); it has NO
				// prototype connection - tapping it does not dismiss.
				.background(Color(0x8C0C1320)),
		)
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy((14 * u).dp),
			modifier = Modifier
				.align(Alignment.BottomCenter)
				.fillMaxWidth()
				.clip(RoundedCornerShape(topStart = (24 * u).dp, topEnd = (24 * u).dp))
				.background(News.CardBg)
				.padding(horizontal = (20 * u).dp)
				.padding(top = (10 * u).dp)
				// Authored sheet is 388 tall with a 30 bottom pad that INCLUDES
				// the home-indicator zone (101:1169) — no extra system inset.
				.padding(bottom = (30 * u).dp),
		) {
			Box(
				modifier = Modifier
					.padding(bottom = (4 * u).dp)
					.size((40 * u).dp, (4 * u).dp)
					.background(News.Divider, RoundedCornerShape((2 * u).dp)),
			)
			Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy((14 * u).dp)) {
				Image(painterResource(R.drawable.ic_sheet_check), null, modifier = Modifier.size((47 * u).dp))
				Text(
					text = "Saved to My STAK",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (18 * u).sp),
					color = Color.White,
				)
			}
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy((11 * u).dp),
				modifier = Modifier
					.fillMaxWidth()
					.clip(RoundedCornerShape((6 * u).dp))
					.background(Color(0x1A69B3CA))
					.padding(horizontal = (14 * u).dp, vertical = (12 * u).dp),
			) {
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier.size((38 * u).dp).background(News.ChipBg, CircleShape),
				) {
					Text(
						text = facts.shortName.take(1),
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp),
						color = Color(0xFF9EADC7),
					)
				}
				Column(verticalArrangement = Arrangement.spacedBy((2 * u).dp), modifier = Modifier.weight(1f)) {
					Text(
						text = facts.shortName,
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (13 * u).sp),
						color = Color.White,
					)
					Text(
						text = "${facts.price} today",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (10 * u).sp),
						color = News.Muted,
					)
				}
				Text(
					// "+4.84% today" -> "▲ 4.84%" (the sheet's authored format).
					text = "${if (facts.up) "▲" else "▼"} ${facts.change.drop(1).removeSuffix(" today")}",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp),
					color = if (facts.up) News.Green else Color(0xFFFF5A6A),
				)
			}
			Text(
				text = "Watching from today · no money committed",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp, lineHeight = (18 * u).sp),
				color = News.Body,
				modifier = Modifier.fillMaxWidth(),
			)
			Column(verticalArrangement = Arrangement.spacedBy((16 * u).dp), modifier = Modifier.fillMaxWidth()) {
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier
						.fillMaxWidth()
						.height((52 * u).dp)
						.background(CtaGradient, RoundedCornerShape((6 * u).dp))
						.border((0.36 * u).dp, CtaBorder, RoundedCornerShape((6 * u).dp))
						.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
							onClick = onViewInMyStak,
						),
				) {
					Text(
						text = "View in My STAK",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (14 * u).sp),
						color = Color.White,
					)
				}
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier
						.fillMaxWidth()
						.height((52 * u).dp)
						.border((0.36 * u).dp, Color(0x54343B4F), RoundedCornerShape((6 * u).dp))
						.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
							onClick = onDismiss,
						),
				) {
					Text(
						text = "Back",
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = (14 * u).sp),
						color = News.Muted,
					)
				}
			}
		}
	}
}

/**
 * Plays the served video inside the hero box: YouTube links through the
 * embeddable player (WebView), any other link through the platform
 * VideoView. Both autoplay once the user tapped the play glyph.
 */
@Composable
private fun NewsVideoPlayer(video: NewsMedia.Video, modifier: Modifier = Modifier, paused: Boolean = false, onDone: () -> Unit = {}) {
	val embed = video.youTubeEmbedUrl
	if (embed != null) {
		androidx.compose.ui.viewinterop.AndroidView(
			modifier = modifier,
			factory = { ctx ->
				android.webkit.WebView(ctx).apply {
					settings.javaScriptEnabled = true
					settings.mediaPlaybackRequiresUserGesture = false
					settings.domStorageEnabled = true
					webChromeClient = android.webkit.WebChromeClient()
					webViewClient = object : android.webkit.WebViewClient() {
						override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
							android.util.Log.i("NewsMedia", "embed loaded: $url")
						}
						override fun onReceivedError(view: android.webkit.WebView?, request: android.webkit.WebResourceRequest?, error: android.webkit.WebResourceError?) {
							android.util.Log.w("NewsMedia", "embed error ${error?.errorCode} ${error?.description} for ${request?.url}")
						}
					}
					// YouTube's player refuses embeds with no HTTP Referer -
					// a bare loadUrl(embed) dies with "configuration error
					// 153" (user, 2026-08-25; reproduced + fix verified in
					// the browser). The iframe wrapper + base URL presents
					// an embedding origin.
					val html = """
						<!doctype html><html><head>
						<meta name="viewport" content="width=device-width, initial-scale=1">
						<style>html,body{margin:0;padding:0;background:#000;height:100%;overflow:hidden}iframe{position:absolute;top:0;left:0;width:100%;height:100%;border:0}</style>
						</head><body>
						<iframe src="$embed" allow="autoplay; encrypted-media; picture-in-picture" allowfullscreen></iframe>
						</body></html>
					""".trimIndent()
					loadDataWithBaseURL("https://stak.app", html, "text/html", "utf-8", null)
				}
			},
			// Backing out of the article must stop playback and free the
			// JS-enabled WebView (audit 2026-08-25).
			onRelease = { web ->
				web.loadUrl("about:blank")
				web.destroy()
			},
		)
	} else {
		// ExoPlayer, not VideoView: VideoView inside Compose failed
		// silently on-device (surface never created / prepare aborted -
		// stuck gray hero, 2026-08-25). ExoPlayer owns its surface and
		// reports errors; a failed stream bounces back to the poster.
		// TextureView, not SurfaceView (2026-08-26): SurfaceView frames
		// composite OUTSIDE the app and long-running emulators stop
		// showing them (decoder ran, screen stayed black); TextureView
		// draws through the view pipeline and also respects the hero's
		// r10 corner clip, which SurfaceView punches through.
		androidx.compose.ui.viewinterop.AndroidView(
			modifier = modifier,
			factory = { ctx ->
				val texture = android.view.TextureView(ctx)
				val player = androidx.media3.exoplayer.ExoPlayer.Builder(ctx).build().apply {
					setMediaItem(androidx.media3.common.MediaItem.fromUri(video.url))
					addListener(object : androidx.media3.common.Player.Listener {
						override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
							android.util.Log.w("NewsMedia", "direct playback error ${error.errorCodeName} for ${video.url}")
							onDone()
						}
						// A finished clip returns to the poster + glyph so
						// the hero reads as replayable, not a stuck frame
						// (user, 2026-08-26: "i cant see the play icon").
						override fun onPlaybackStateChanged(playbackState: Int) {
							if (playbackState == androidx.media3.common.Player.STATE_ENDED) onDone()
						}
					})
					setVideoTextureView(texture)
					// Exactly 1x (user, 2026-08-26: "put it on 1x speed" -
					// not sluggish, not fast). Clip SOURCES must also be
					// real-time footage; see NewsArticleFeed's media notes.
					playbackParameters = androidx.media3.common.PlaybackParameters(1f)
					prepare()
					playWhenReady = true
				}
				texture.tag = player
				texture
			},
			// Play/pause toggle: ExoPlayer.pause keeps the position, so
			// resuming continues where the clip stopped.
			update = { v -> (v.tag as? androidx.media3.exoplayer.ExoPlayer)?.playWhenReady = !paused },
			onRelease = { v -> (v.tag as? androidx.media3.exoplayer.ExoPlayer)?.release(); v.tag = null },
		)
	}
}


/**
 * Disk-cached, pre-buffered hero clips (user, 2026-08-30: "the speed
 * should look like watching a movie"): a clip streams through a 200MB
 * LRU cache so a replay starts instantly, playback may begin once 300ms
 * is buffered, and players are prepared before the play tap.
 */
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
private object NewsVideoCache {
	private var cache: androidx.media3.datasource.cache.SimpleCache? = null

	@Synchronized
	private fun cache(ctx: android.content.Context): androidx.media3.datasource.cache.SimpleCache =
		cache ?: androidx.media3.datasource.cache.SimpleCache(
			java.io.File(ctx.applicationContext.cacheDir, "news_video"),
			androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor(200L * 1024 * 1024),
			androidx.media3.database.StandaloneDatabaseProvider(ctx.applicationContext),
		).also { cache = it }

	fun preparedPlayer(ctx: android.content.Context, url: String): androidx.media3.exoplayer.ExoPlayer {
		val dataSources = androidx.media3.datasource.cache.CacheDataSource.Factory()
			.setCache(cache(ctx))
			.setUpstreamDataSourceFactory(androidx.media3.datasource.DefaultDataSource.Factory(ctx))
			.setFlags(androidx.media3.datasource.cache.CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
		return androidx.media3.exoplayer.ExoPlayer.Builder(ctx)
			.setMediaSourceFactory(androidx.media3.exoplayer.source.DefaultMediaSourceFactory(dataSources))
			.setLoadControl(
				androidx.media3.exoplayer.DefaultLoadControl.Builder()
					.setBufferDurationsMs(20000, 60000, 600, 1500)
					.build(),
			)
			.build().apply {
				// Audible by default: media usage + audio focus (user,
				// 2026-08-30 "no audio?").
				setAudioAttributes(
					androidx.media3.common.AudioAttributes.Builder()
						.setUsage(androidx.media3.common.C.USAGE_MEDIA)
						.setContentType(androidx.media3.common.C.AUDIO_CONTENT_TYPE_MOVIE)
						.build(),
					true,
				)
				volume = 1f
				setMediaItem(androidx.media3.common.MediaItem.fromUri(url))
				// Exactly 1x (user, 2026-08-26).
				playbackParameters = androidx.media3.common.PlaybackParameters(1f)
				prepare()
				playWhenReady = false
			}
	}
}


/**
 * The reference player (user, 2026-08-31, Downloads/video_app shots -
 * "this is the way i wanted the video to be"): tap the video to
 * show/hide the chrome (auto-hide 3s); center cluster = back-15 /
 * play-pause / forward-15 (plain white glyphs, no puck); bottom-left
 * "02:57 / 05:49"; bottom-right captions + fullscreen + "..." menu; a
 * seek bar with an always-there thumb above the bottom edge; PiP
 * top-right; the buffering spinner takes the center slot. The "..."
 * opens the reference's dark sheet: Audio & Subtitles / Playback Rates /
 * Quality - rates actually retime the player; the demo MP4s are
 * single-rendition with no text tracks, so Quality lists Default and
 * Subtitles lists Off (production HLS serves real renditions/tracks
 * into the same rows). Progress stays STAK teal - the reference's red
 * is that app's brand accent, ours is 69B3CA; same anatomy.
 */
@Composable
private fun HeroControls(
	exo: androidx.media3.exoplayer.ExoPlayer,
	u: Float,
	onFullscreen: () -> Unit,
	onPip: () -> Unit,
	modifier: Modifier = Modifier,
	fullscreen: Boolean = false,
) {
	var visible by remember { mutableStateOf(true) }
	var playing by remember { mutableStateOf(exo.playWhenReady) }
	var buffering by remember { mutableStateOf(exo.playbackState == androidx.media3.common.Player.STATE_BUFFERING) }
	var muted by remember { mutableStateOf(exo.volume == 0f) }
	var rate by remember { mutableStateOf(exo.playbackParameters.speed) }
	var position by remember { mutableStateOf(0L) }
	var duration by remember { mutableStateOf(0L) }
	var scrubbing by remember { mutableStateOf(false) }
	var scrubTo by remember { mutableStateOf(0f) }
	var sheet by remember { mutableStateOf<PlayerSheet?>(null) }
	androidx.compose.runtime.DisposableEffect(exo) {
		val l = object : androidx.media3.common.Player.Listener {
			override fun onPlayWhenReadyChanged(p: Boolean, reason: Int) { playing = p }
			override fun onPlaybackStateChanged(state: Int) { buffering = state == androidx.media3.common.Player.STATE_BUFFERING }
		}
		exo.addListener(l)
		onDispose { exo.removeListener(l) }
	}
	androidx.compose.runtime.LaunchedEffect(Unit) {
		while (true) {
			if (!scrubbing) position = exo.currentPosition.coerceAtLeast(0L)
			duration = if (exo.duration > 0) exo.duration else 0L
			kotlinx.coroutines.delay(250)
		}
	}
	// Auto-hide 3s after the last interaction while playing.
	var interactedAt by remember { mutableStateOf(0L) }
	fun poke() { interactedAt = android.os.SystemClock.elapsedRealtime() }
	androidx.compose.runtime.LaunchedEffect(visible, playing, interactedAt) {
		if (visible && playing) { kotlinx.coroutines.delay(3000); visible = false }
	}
	// Reference time style: zero-padded "02:57 / 05:49".
	fun ts(ms: Long): String {
		val t = ms / 1000; return "%02d:%02d".format(t / 60, t % 60)
	}
	fun skip(deltaMs: Long) {
		val target = exo.currentPosition + deltaMs
		exo.seekTo(if (duration > 0) target.coerceIn(0L, duration) else target.coerceAtLeast(0L))
		poke()
	}
	Box(
		modifier = modifier.clickable(
			interactionSource = remember { MutableInteractionSource() },
			indication = null,
		) { visible = !visible; poke() },
	) {
		if (!visible && buffering) {
			androidx.compose.material3.CircularProgressIndicator(
				color = Color.White,
				strokeWidth = (2.5f * u).dp,
				modifier = Modifier.align(Alignment.Center).size((30 * u).dp),
			)
		}
		if (visible) {
			// The reference dims the frame behind the white chrome.
			Box(modifier = Modifier.matchParentSize().background(Color(0x3D000000)))
			// Top-right: picture in picture (reference image 1's top cluster;
			// cast/AirPlay are platform services, PiP is the app's own).
			androidx.compose.material3.Icon(
				imageVector = Icons.Filled.PictureInPictureAlt,
				contentDescription = "Play in picture in picture",
				tint = Color(0xE6FFFFFF),
				modifier = Modifier
					.align(Alignment.TopEnd)
					.padding(top = (9 * u).dp, end = (10 * u).dp)
					.size((15 * u).dp)
					.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
					) { onPip() },
			)
			// Center cluster: back-15 / play-pause / forward-15.
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy((30 * u).dp),
				modifier = Modifier.align(Alignment.Center),
			) {
				SkipGlyph(forward = false, u = u) { skip(-15_000L) }
				Box(contentAlignment = Alignment.Center, modifier = Modifier.size((34 * u).dp)) {
					if (buffering) {
						androidx.compose.material3.CircularProgressIndicator(
							color = Color.White,
							strokeWidth = (2.5f * u).dp,
							modifier = Modifier.size((30 * u).dp),
						)
					} else {
						androidx.compose.material3.Icon(
							imageVector = if (playing) Icons.Filled.Pause else Icons.Filled.PlayArrow,
							contentDescription = if (playing) "Pause" else "Play",
							tint = Color.White,
							modifier = Modifier
								.size((34 * u).dp)
								.clickable(
									interactionSource = remember { MutableInteractionSource() },
									indication = null,
								) { exo.playWhenReady = !playing; poke() },
						)
					}
				}
				SkipGlyph(forward = true, u = u) { skip(15_000L) }
			}
			// Bottom chrome: time row + seek bar over the gradient.
			Column(
				modifier = Modifier
					.align(Alignment.BottomCenter)
					.fillMaxWidth()
					.background(
						androidx.compose.ui.graphics.Brush.verticalGradient(
							listOf(Color.Transparent, Color(0x99000000)),
						),
					),
			) {
				Row(
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.fillMaxWidth()
						.padding(start = (12 * u).dp, end = (12 * u).dp, top = (10 * u).dp),
				) {
					Text(
						text = ts(if (scrubbing) scrubTo.toLong() else position) + " / " + ts(duration),
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, letterSpacing = (0.2f * u).sp),
						color = Color.White,
					)
					Spacer(modifier = Modifier.weight(1f))
					// Captions sits dimmed like the reference (the demo clips
					// carry no text tracks); it opens Audio & Subtitles.
					androidx.compose.material3.Icon(
						imageVector = Icons.Filled.ClosedCaption,
						contentDescription = "Audio and subtitles",
						tint = Color(0x66FFFFFF),
						modifier = Modifier
							.size((15 * u).dp)
							.clickable(
								interactionSource = remember { MutableInteractionSource() },
								indication = null,
							) { sheet = PlayerSheet.AudioSubs; poke() },
					)
					androidx.compose.material3.Icon(
						imageVector = if (fullscreen) Icons.Filled.FullscreenExit else Icons.Filled.Fullscreen,
						contentDescription = if (fullscreen) "Exit fullscreen" else "Fullscreen",
						tint = Color(0xE6FFFFFF),
						modifier = Modifier
							.padding(start = (14 * u).dp)
							.size((15 * u).dp)
							.clickable(
								interactionSource = remember { MutableInteractionSource() },
								indication = null,
							) { onFullscreen() },
					)
					androidx.compose.material3.Icon(
						imageVector = Icons.Filled.MoreHoriz,
						contentDescription = "More options",
						tint = Color(0xE6FFFFFF),
						modifier = Modifier
							.padding(start = (14 * u).dp)
							.size((16 * u).dp)
							.clickable(
								interactionSource = remember { MutableInteractionSource() },
								indication = null,
							) { sheet = PlayerSheet.Menu; poke() },
					)
				}
				SleekScrubber(
					fraction = if (duration > 0) ((if (scrubbing) scrubTo else position.toFloat()) / duration.toFloat()).coerceIn(0f, 1f) else 0f,
					scrubbing = scrubbing,
					u = u,
					onScrub = { f ->
						scrubbing = true
						scrubTo = (f * duration.toFloat()).coerceIn(0f, duration.toFloat())
						poke()
					},
					onCommit = {
						exo.seekTo(scrubTo.toLong()); position = scrubTo.toLong(); scrubbing = false
					},
					modifier = Modifier.fillMaxWidth().padding(horizontal = (12 * u).dp),
				)
			}
		}
	}
	sheet?.let { page ->
		PlayerSheetDialog(
			page = page,
			u = u,
			rate = rate,
			muted = muted,
			onPage = { sheet = it },
			onDismiss = { sheet = null; visible = true; poke() },
			onRate = { r ->
				rate = r
				exo.playbackParameters = androidx.media3.common.PlaybackParameters(r)
				sheet = null
			},
			onMuted = { m -> muted = m; exo.volume = if (m) 0f else 1f },
		)
	}
}


/** The reference's circular-arrow skip glyph with the seconds in the middle. */
@Composable
private fun SkipGlyph(forward: Boolean, u: Float, onTap: () -> Unit) {
	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.size((30 * u).dp)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
			) { onTap() },
	) {
		androidx.compose.foundation.Canvas(modifier = Modifier.matchParentSize()) {
			val stroke = (1.8f * u).dp.toPx()
			val inset = stroke * 1.9f
			val d = size.minDimension - inset * 2f
			val r = d / 2f
			val cx = size.width / 2f
			val cy = size.height / 2f
			// Gap at the top for the arrowhead; the back glyph is the mirror.
			scale(scaleX = if (forward) 1f else -1f, scaleY = 1f) {
				drawArc(
					color = Color.White,
					startAngle = -60f,
					sweepAngle = 300f,
					useCenter = false,
					topLeft = androidx.compose.ui.geometry.Offset(cx - r, cy - r),
					size = androidx.compose.ui.geometry.Size(d, d),
					style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke, cap = androidx.compose.ui.graphics.StrokeCap.Round),
				)
				// Arrowhead at the gap's right edge, pointing clockwise.
				val tip = Math.toRadians(-60.0)
				val px = cx + r * kotlin.math.cos(tip).toFloat()
				val py = cy + r * kotlin.math.sin(tip).toFloat()
				val tx = -kotlin.math.sin(tip).toFloat()
				val ty = kotlin.math.cos(tip).toFloat()
				val s = (3.2f * u).dp.toPx()
				val head = androidx.compose.ui.graphics.Path().apply {
					moveTo(px + tx * s, py + ty * s)
					lineTo(px - ty * s * 0.8f, py + tx * s * 0.8f)
					lineTo(px + ty * s * 0.8f, py - tx * s * 0.8f)
					close()
				}
				drawPath(head, Color.White)
			}
		}
		Text(
			text = "15",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.SemiBold, fontSize = (8.5f * u).sp),
			color = Color.White,
		)
	}
}


/** Reference image 3: the hero while its clip floats in the OS PiP window. */
@Composable
private fun PipPlaceholder(u: Float, modifier: Modifier = Modifier) {
	Box(contentAlignment = Alignment.Center, modifier = modifier.background(Color(0xFF0F1115))) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy((8 * u).dp),
		) {
			androidx.compose.material3.Icon(
				imageVector = Icons.Filled.PictureInPictureAlt,
				contentDescription = null,
				tint = Color(0x8AFFFFFF),
				modifier = Modifier.size((26 * u).dp),
			)
			Text(
				text = "This video is playing in picture in picture.",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10.5f * u).sp),
				color = Color(0x99FFFFFF),
			)
		}
	}
}


/** The "..." sheet's pages (reference image 2). */
private enum class PlayerSheet { Menu, AudioSubs, Rates, Quality }

private fun rateLabel(r: Float): String =
	if (r == 1f) "Normal" else if (r == r.toInt().toFloat()) "${r.toInt()}x" else "${r}x"

/**
 * The reference's dark player sheet (image 2): Audio & Subtitles /
 * Playback Rates · Normal / Quality · Default, each drilling into its
 * page. Rendered in its own full-screen Dialog window so it overlays the
 * whole screen even when opened from the inline 360x208 hero (and above
 * the fullscreen player's dialog, being the later window).
 */
@Composable
private fun PlayerSheetDialog(
	page: PlayerSheet,
	u: Float,
	rate: Float,
	muted: Boolean,
	onPage: (PlayerSheet) -> Unit,
	onDismiss: () -> Unit,
	onRate: (Float) -> Unit,
	onMuted: (Boolean) -> Unit,
) {
	androidx.compose.ui.window.Dialog(
		onDismissRequest = onDismiss,
		properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
	) {
		Box(modifier = Modifier.fillMaxSize()) {
			Box(
				modifier = Modifier
					.matchParentSize()
					.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
					) { onDismiss() },
			)
			Column(
				modifier = Modifier
					.align(Alignment.BottomCenter)
					.fillMaxWidth()
					.clip(RoundedCornerShape(topStart = (14 * u).dp, topEnd = (14 * u).dp))
					.background(Color(0xF2191B20))
					.padding(top = (8 * u).dp, bottom = (18 * u).dp),
			) {
				when (page) {
					PlayerSheet.Menu -> {
						SheetRow(u = u, label = "Audio & Subtitles", icon = Icons.Filled.Subtitles) { onPage(PlayerSheet.AudioSubs) }
						SheetDivider(u)
						SheetRow(u = u, label = "Playback Rates", icon = Icons.Filled.Speed, value = rateLabel(rate)) { onPage(PlayerSheet.Rates) }
						SheetDivider(u)
						SheetRow(u = u, label = "Quality", icon = Icons.Filled.SignalCellularAlt, value = "Default") { onPage(PlayerSheet.Quality) }
					}
					PlayerSheet.Rates -> {
						SheetTitle("Playback Rates", u)
						listOf(0.5f, 0.75f, 1f, 1.25f, 1.5f, 2f).forEach { r ->
							SheetRow(u = u, label = rateLabel(r), checked = r == rate) { onRate(r) }
						}
					}
					PlayerSheet.Quality -> {
						SheetTitle("Quality", u)
						// Demo stories stream single-rendition MP4s; production
						// HLS serves its real rendition ladder into these rows.
						SheetRow(u = u, label = "Default", checked = true) { onDismiss() }
					}
					PlayerSheet.AudioSubs -> {
						SheetTitle("Audio & Subtitles", u)
						SheetLabel("Audio", u)
						SheetRow(u = u, label = "Original", checked = !muted) { onMuted(false) }
						SheetRow(u = u, label = "Muted", checked = muted) { onMuted(true) }
						SheetLabel("Subtitles", u)
						// The demo newscast MP4s carry no text tracks.
						SheetRow(u = u, label = "Off", checked = true) { onDismiss() }
					}
				}
			}
		}
	}
}

@Composable
private fun SheetRow(
	u: Float,
	label: String,
	icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
	value: String? = null,
	checked: Boolean? = null,
	onClick: () -> Unit,
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.height((44 * u).dp)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
			) { onClick() }
			.padding(horizontal = (18 * u).dp),
	) {
		if (icon != null) {
			androidx.compose.material3.Icon(
				imageVector = icon,
				contentDescription = null,
				tint = Color(0xCCFFFFFF),
				modifier = Modifier.size((17 * u).dp),
			)
			Spacer(modifier = Modifier.width((14 * u).dp))
		}
		Text(
			text = label,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (13 * u).sp),
			color = Color.White,
		)
		if (value != null) {
			Text(
				text = "  ·  $value",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (13 * u).sp),
				color = Color(0x8AFFFFFF),
			)
		}
		Spacer(modifier = Modifier.weight(1f))
		if (checked == true) {
			androidx.compose.material3.Icon(
				imageVector = Icons.Filled.Check,
				contentDescription = "Selected",
				tint = Color(0xFF69B3CA),
				modifier = Modifier.size((15 * u).dp),
			)
		}
	}
}

@Composable
private fun SheetTitle(text: String, u: Float) {
	Text(
		text = text,
		style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.SemiBold, fontSize = (11 * u).sp, letterSpacing = (0.3f * u).sp),
		color = Color(0x8AFFFFFF),
		modifier = Modifier.padding(start = (18 * u).dp, top = (4 * u).dp, bottom = (4 * u).dp),
	)
}

@Composable
private fun SheetLabel(text: String, u: Float) {
	Text(
		text = text,
		style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp),
		color = Color(0x66FFFFFF),
		modifier = Modifier.padding(start = (18 * u).dp, top = (8 * u).dp, bottom = (2 * u).dp),
	)
}

@Composable
private fun SheetDivider(u: Float) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(start = (49 * u).dp)
			.height(0.5.dp)
			.background(Color(0x1FFFFFFF)),
	)
}


/** Hairline seek bar: rounded track, teal progress, always-there thumb that grows on touch. */
@Composable
private fun SleekScrubber(
	fraction: Float,
	scrubbing: Boolean,
	u: Float,
	onScrub: (Float) -> Unit,
	onCommit: () -> Unit,
	modifier: Modifier = Modifier,
) {
	androidx.compose.foundation.Canvas(
		modifier = modifier
			.height((16 * u).dp)
			.pointerInput(Unit) {
				detectHorizontalDragGestures(
					onDragStart = { o -> onScrub((o.x / size.width).coerceIn(0f, 1f)) },
					onHorizontalDrag = { change, _ -> change.consume(); onScrub((change.position.x / size.width).coerceIn(0f, 1f)) },
					onDragEnd = { onCommit() },
					onDragCancel = { onCommit() },
				)
			}
			.pointerInput(Unit) {
				detectTapGestures { o ->
					onScrub((o.x / size.width).coerceIn(0f, 1f)); onCommit()
				}
			},
	) {
		val y = size.height - (5 * u).dp.toPx()
		val trackH = (2.5f * u).dp.toPx()
		val r = androidx.compose.ui.geometry.CornerRadius(trackH / 2f)
		drawRoundRect(
			color = Color(0x4DFFFFFF),
			topLeft = androidx.compose.ui.geometry.Offset(0f, y - trackH / 2f),
			size = androidx.compose.ui.geometry.Size(size.width, trackH),
			cornerRadius = r,
		)
		drawRoundRect(
			color = Color(0xFF69B3CA),
			topLeft = androidx.compose.ui.geometry.Offset(0f, y - trackH / 2f),
			size = androidx.compose.ui.geometry.Size(size.width * fraction, trackH),
			cornerRadius = r,
		)
		drawCircle(
			color = Color.White,
			radius = (if (scrubbing) 5f else 3.4f) * u.dp.toPx(),
			center = androidx.compose.ui.geometry.Offset(size.width * fraction, y),
		)
	}
}
