package com.stak.demo.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.animation.core.animateFloat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.R
import com.stak.demo.ui.theme.Geist
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors

/** Palette of the CHINEDU "02 · Home" frames. */
private object Home {
	val CardBg = Color(0xFF171D2C)
	val TabBg = Color(0xFF060C1D)
	val Teal = Color(0xFF69B3CA)
	val CardInk = Color(0xFF0E162B)
	val PaperWhite = Color(0xFFF9F9F9)
	val NavCircle = Color(0xFF192238)
}

/**
 * 02 · Home — CHINEDU "Home first run" (1:958) and "Home Main" (1:1097).
 *
 * Both frames share the whole content stack: fixed top nav (STAK logo,
 * bell + profile circles, "Good Morning, Hamza"), the Market Mood card
 * with its clipped news-deck stack, the "Why this matters" row and the
 * teal deck banner. First run replaces the tab bar with a bottom scrim
 * and the frosted "See Todays Pick" pill; tapping it reveals Home Main
 * (prototype: Swap overlay · Instant). The tab bar itself lives in the
 * MainShell so the other tabs share it.
 */
@Composable
fun HomeScreen(firstRun: Boolean, onSeeTodaysPick: () -> Unit, onProfile: () -> Unit = {}, onOpenNews: () -> Unit = {}, onOpenMyStak: () -> Unit = {}, onOpenDeck: () -> Unit = {}) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	BoxWithConstraints(modifier = Modifier.fillMaxSize().background(StakColors.Bg)) {
		val statusPad = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
		Column(modifier = Modifier.fillMaxSize()) {
			// Dev-ready Home Main (118:1633): the top nav SCROLLS with the
			// content - the greeting block lives inside Scroll content.
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				modifier = Modifier
					.weight(1f)
					.fillMaxWidth()
					.verticalScroll(rememberScrollState()),
			) {
				TopNav(onProfile = onProfile, modifier = Modifier.fillMaxWidth().padding(horizontal = (17 * u).dp))
				Spacer(modifier = Modifier.height((21 * u).dp))
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					modifier = Modifier.fillMaxWidth().padding(horizontal = (20 * u).dp),
				) {
					MarketMoodCard(onOpenNews = onOpenNews)
					Spacer(modifier = Modifier.height((10 * u).dp))
					WhyThisMattersCard(onOpenMyStak = onOpenMyStak)
					Spacer(modifier = Modifier.height((20 * u).dp))
					DeckBanner(onOpenDeck = onOpenDeck)
					// Authored scroll content (118:1634) ends exactly at the
					// banner's bottom edge - no trailing gap. First run keeps
					// room for the scrim pill.
					if (firstRun) Spacer(modifier = Modifier.height((140 * u).dp))
				}
			}
		}
		if (firstRun) {
			// The frame pins the scrim 41px above the deck banner (Tab bar
			// y629 vs banner y670) — anchor to the same content geometry:
			// banner top = status inset + 626, so the scrim starts 585 below
			// the inset and runs to the physical bottom of the screen.
			FirstRunOverlay(
				onSeeTodaysPick = onSeeTodaysPick,
				modifier = Modifier
					.align(Alignment.BottomCenter)
					.height(this@BoxWithConstraints.maxHeight - statusPad - (585 * u).dp),
			)
		}
	}
}

/** Fixed top nav — logo row with bell/profile circles + greeting (Figma 131px block). */
@Composable
private fun TopNav(onProfile: () -> Unit, modifier: Modifier = Modifier) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		// Scrolls with the content (118:1633); authored side inset 17.
		modifier = modifier
			.background(StakColors.Bg)
			.statusBarsPadding()
			.padding(top = (22 * u).dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().height((35 * u).dp)) {
			Image(
				painter = painterResource(R.drawable.ic_stak_logo_mark),
				contentDescription = null,
				modifier = Modifier.size((26.48 * u).dp),
			)
			Spacer(modifier = Modifier.width((4.49 * u).dp))
			Image(
				painter = painterResource(R.drawable.ic_stak_wordmark),
				contentDescription = "STAK",
				modifier = Modifier.size((78.16 * u).dp, (14.98 * u).dp),
			)
			Spacer(modifier = Modifier.weight(1f))
			// Bell + stateful unread dot (151:1207): the authored badge
			// (cx26.25 cy11.667 r2.917 #FF8030) shows while untouched
			// notifications exist and clears once they're opened and read.
			Box(
				modifier = Modifier
					.size((35 * u).dp)
					.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
					) { com.stak.demo.ui.StakNotifications.markAllRead() },
			) {
				Image(
					painter = painterResource(R.drawable.ic_nav_bell),
					contentDescription = "Notifications",
					modifier = Modifier.size((35 * u).dp),
				)
				if (com.stak.demo.ui.StakNotifications.hasUnread) {
					Box(
						modifier = Modifier
							.offset(x = (23.333 * u).dp, y = (8.75 * u).dp)
							.size((5.833 * u).dp)
							.background(Color(0xFFFF8030), CircleShape),
					)
				}
			}
			Spacer(modifier = Modifier.width((4 * u).dp))
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.size((35 * u).dp)
					.background(Home.NavCircle, CircleShape)
					.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
						onClick = onProfile,
					),
			) {
				Image(
					painter = painterResource(R.drawable.ic_nav_person),
					contentDescription = "Profile",
					modifier = Modifier.size((12.99 * u).dp, (13.64 * u).dp),
				)
			}
		}
		Spacer(modifier = Modifier.height((10 * u).dp))
		Text(
			text = "Good Morning, ${com.stak.demo.ui.UserProfile.greetingName}",
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (16 * u).sp, lineHeight = (20 * u).sp),
			color = Color.White,
		)
	}
}

/** Market Mood — 350x397 #171d2c card with the clipped news-deck stack. */
@Composable
private fun MarketMoodCard(onOpenNews: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.height((397 * u).dp)
			.clip(RoundedCornerShape((8 * u).dp))
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onOpenNews,
			)
			.background(Home.CardBg),
	) {
		Box(modifier = Modifier.matchParentSize()) { NewsDeck() }
		// The frame's bottom strip (1:1175, 30px) backdrop-blurs the stack —
		// redraw the same deck blurred, clipped to the card's last 30dp.
		Box(
			modifier = Modifier
				.align(Alignment.BottomCenter)
				.fillMaxWidth()
				.height((30 * u).dp)
				.clipToBounds(),
		) {
			// An oversized child gets centered in the 30dp band; shift it up
			// by (397-30)/2 so the stack's bottom edge lines up with the band.
			Box(
				modifier = Modifier
					.offset(y = (-183.5 * u).dp)
					.fillMaxWidth()
					.requiredHeight((397 * u).dp)
					// Render-calibrated: band diff vs the frame is minimal on the
					// 2.4-2.8u plateau (8.26 vs 9.55 at the old 4u). The strip's
					// blurred ink stays ~15% lighter than the frame's - that's the
					// app-wide glyph-coverage class, not the kernel.
					.blur((2.6 * u).dp),
			) {
				// Opaque ground: backdrop blur replaces everything behind the
				// strip. Without it the blurred cards' soft alpha edges let the
				// crisp deck below show through (sharp+soft union at the edges).
				Box(modifier = Modifier.matchParentSize().background(Home.CardBg))
				NewsDeck()
			}
		}
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Center,
			modifier = Modifier.fillMaxWidth().padding(top = (25 * u).dp),
		) {
			Column(verticalArrangement = Arrangement.spacedBy((4 * u).dp), modifier = Modifier.width((180 * u).dp)) {
				Text(
					text = "Market Mood",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Medium, fontSize = (20 * u).sp, lineHeight = (25 * u).sp),
					color = Color.White,
				)
				Text(
					// Backend-served with the mood score in production (the words
					// change with the market); authored demo copy this phase.
					text = buildAnnotatedString {
						withStyle(SpanStyle(color = Home.Teal)) { append(MarketMoodFeed.DEMO_STATUS_LEAD) }
						append(MarketMoodFeed.DEMO_STATUS_REST)
					},
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
					color = Color.White,
				)
			}
			Spacer(modifier = Modifier.width((46 * u).dp))
			MarketMoodGauge(u = u)
		}
	}
}

/**
 * The stacked news cards in their frame poses — front card straight, the
 * two behind rotated; offsets are from the parent card's center (350x397).
 */
@Composable
private fun BoxScope.NewsDeck() {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	NewsDeckCard(
		bg = Home.PaperWhite,
		title = "Wall Street's fear gauge reads 32",
		body = "The Fear & Greed Index is firmly in Fear territory. Money is rotating out of the ....",
		bodyWeight = FontWeight.Light,
		bodySize = (12 * u).sp,
		titleBodyGap = (12 * u).dp,
		offsetX = (6.2 * u).dp,
		offsetY = (59.73 * u).dp,
		rotation = 0f,
	)
	NewsDeckCard(
		bg = Home.Teal,
		title = "Fed meeting notes drop Wednesday",
		body = "Minutes from the last Fed meeting land July 8. A market this tense moves on every word....",
		bodyWeight = FontWeight.Normal,
		bodySize = (11.89 * u).sp,
		titleBodyGap = (17 * u).dp,
		offsetX = (8.33 * u).dp,
		offsetY = (189.44 * u).dp,
		rotation = -3.72f,
	)
	NewsDeckCard(
		bg = Home.PaperWhite,
		title = "The OpenAI IPO is reportedly delayed",
		body = "The year's most anticipated listing just slipped. Markets riding a wave of IPO excitement...",
		bodyWeight = FontWeight.Light,
		bodySize = (12 * u).sp,
		titleBodyGap = (12 * u).dp,
		offsetX = (-0.02 * u).dp,
		offsetY = (264.57 * u).dp,
		rotation = -7.68f,
	)
}

/** One 236.86x278.45 news card of the deck, placed by its rotated-bounds center. */
@Composable
private fun BoxScope.NewsDeckCard(
	bg: Color,
	title: String,
	body: String,
	bodyWeight: FontWeight,
	bodySize: TextUnit,
	titleBodyGap: Dp,
	offsetX: Dp,
	offsetY: Dp,
	rotation: Float,
) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy(titleBodyGap),
		modifier = Modifier
			.align(Alignment.Center)
			.offset(x = offsetX, y = offsetY)
			.graphicsLayer { rotationZ = rotation }
			.size((236.86 * u).dp, (278.45 * u).dp)
			.clip(RoundedCornerShape((6.79 * u).dp))
			.background(bg)
			.padding(start = (14.43 * u).dp, top = (23.77 * u).dp),
	) {
		Text(
			text = title,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Medium, fontSize = (16 * u).sp),
			color = Home.CardInk,
			modifier = Modifier.width((202.9 * u).dp),
		)
		Text(
			text = body,
			style = TextStyle(fontFamily = Geist, fontWeight = bodyWeight, fontSize = bodySize),
			color = Home.CardInk,
			modifier = Modifier.width((189.31 * u).dp),
		)
	}
}

/** "Why this matters to you" — 350x91 card with the glass caution ball art. */
@Composable
private fun WhyThisMattersCard(onOpenMyStak: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.height((91 * u).dp)
			// Shaped background, no clip needed — the visible ball never
			// reaches the card edges, only transparent padding overhangs.
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onOpenMyStak,
			)
			.background(Home.CardBg, RoundedCornerShape((8 * u).dp)),
	) {
		// Authored (1:1043/1:1044): the 105x105 image box sits at (3, -7) with
		// the source mapped 1:1 (no crop) - the ball itself stays inside the
		// card; only the box's transparent padding overhangs. unbounded, or the
		// card's 91 height clamps the box and Fit shrinks the ball to 87%.
		Image(
			painter = painterResource(R.drawable.home_caution_glass),
			contentDescription = null,
			modifier = Modifier
				.offset(x = (3 * u).dp, y = (-7 * u).dp)
				.wrapContentSize(align = Alignment.TopStart, unbounded = true)
				.size((105 * u).dp),
		)
		Column(
			verticalArrangement = Arrangement.spacedBy((6 * u).dp),
			modifier = Modifier.align(Alignment.CenterStart).padding(start = (127 * u).dp),
		) {
			Text(
				text = "Why this matters to you",
				// Authored (1:1040): Sora Regular 14 / lh15.
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = (14 * u).sp, lineHeight = (15 * u).sp),
				color = Color.White,
			)
			Text(
				text = "Your STAK collections houses 80% of stocks from effected industries.",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Light, fontSize = (12 * u).sp, lineHeight = (15 * u).sp),
				color = Color.White,
				modifier = Modifier.width((198 * u).dp),
			)
		}
	}
}

/** Teal deck banner — 350x116 with the box-and-coins art and Go to Deck chip. */
@Composable
private fun DeckBanner(onOpenDeck: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.height((116 * u).dp)
			.clip(RoundedCornerShape((8 * u).dp))
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onOpenDeck,
			)
			.background(Home.Teal),
	) {
		// The illustration zone of the frame (box + coins + shadow), cropped
		// from the banner render so its pose is exact; the teal it carries is
		// the same banner fill it sits on.
		Image(
			painter = painterResource(R.drawable.home_banner_illustration),
			contentDescription = null,
			contentScale = ContentScale.Fit,
			// The 1:1191 node's in-banner slice (121.5x116 at x13), baked
			// from the 2x frame render.
			modifier = Modifier.align(Alignment.TopStart).offset(x = (13 * u).dp).size((121.5 * u).dp, (116 * u).dp),
		)
		Column(
			verticalArrangement = Arrangement.spacedBy((10 * u).dp),
			modifier = Modifier
				.align(Alignment.CenterStart)
				.padding(start = (184 * u).dp)
				.offset(y = (0.5 * u).dp)
				.width((156 * u).dp),
		) {
			Text(
				text = "Take your first deck to build your taste",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Light, fontSize = (12 * u).sp, lineHeight = (15 * u).sp),
				color = Color.Black,
			)
			// No clickable of its own: the authored connection is on the whole
				// banner (1:1184), whose tap target includes this chip.
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier
						.size((123 * u).dp, (32 * u).dp)
						.background(StakColors.Bg, RoundedCornerShape((15 * u).dp)),
				) {
				Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((3 * u).dp)) {
					Text(
						text = "Go to Deck",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11.49 * u).sp, lineHeight = (15 * u).sp),
						color = Color.White,
					)
					Image(
						painter = painterResource(R.drawable.ic_arrow_right_small),
						contentDescription = null,
						modifier = Modifier.size((16 * u).dp),
					)
				}
			}
		}
	}
}

/**
 * First-run bottom treatment — scrim fading to bg over its first 98.7px
 * (43.68% of the frame's 226px overlay) + the frosted "See Todays Pick"
 * pill, whose bottom sits 70px above the screen edge as in the frame.
 */
@Composable
private fun FirstRunOverlay(onSeeTodaysPick: () -> Unit, modifier: Modifier = Modifier) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Box(modifier = modifier.fillMaxWidth()) {
		Column(modifier = Modifier.matchParentSize()) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height((98.7 * u).dp)
					.background(Brush.verticalGradient(0f to Color(0x000A1020), 1f to StakColors.Bg)),
			)
			Box(modifier = Modifier.fillMaxWidth().weight(1f).background(StakColors.Bg))
		}
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.align(Alignment.BottomCenter)
				.padding(bottom = (70 * u).dp)
				.size((136 * u).dp, (51 * u).dp)
				.clip(RoundedCornerShape(50))
				.background(Color(0x0FFFFFFF))
				.border((0.94 * u).dp, Color(0x66FFFFFF), RoundedCornerShape(50))
				.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null,
					onClick = onSeeTodaysPick,
				),
		) {
			Text(
				text = "See Todays Pick",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp),
				color = Color.White,
			)
		}
	}
}

/**
 * The Market Mood gauge, drawn live from the authored geometry (1:1158):
 * three 60-degree segments (green 0x61A57F, neutral 0xD8CFCF, red
 * 0xDE4E71), centerline r25.6, stroke 6.75, white tapered needle. The
 * needle sweeps in from the green end to the mood value on entry and
 * breathes gently so the market feels live; the value maps 0..100 onto
 * 180..0 degrees and is demo-pinned to the frame's "high volatility"
 * 33.4 until the worldwide market feed is wired in the data phase.
 */
@Composable
private fun MarketMoodGauge(u: Float) {
	// Starts at the design's default pose; moves to the live worldwide
	// reading once it arrives (user's call, 2026-08-21).
	val sweep = androidx.compose.runtime.remember { androidx.compose.animation.core.Animatable(MarketMoodFeed.DEMO_ANGLE_DEG) }
	androidx.compose.runtime.LaunchedEffect(Unit) { MarketMoodFeed.refresh() }
	val live = MarketMoodFeed.score
	val moodAngleDeg = if (live != null) MarketMoodFeed.angleFor(live) else MarketMoodFeed.DEMO_ANGLE_DEG
	androidx.compose.runtime.LaunchedEffect(moodAngleDeg) {
		sweep.animateTo(moodAngleDeg, androidx.compose.animation.core.tween(900, easing = androidx.compose.animation.core.EaseOut))
	}
	val idle = androidx.compose.animation.core.rememberInfiniteTransition(label = "gaugeIdle")
	val wobble by idle.animateFloat(
		initialValue = -0.8f,
		targetValue = 0.8f,
		animationSpec = androidx.compose.animation.core.infiniteRepeatable<Float>(
			animation = androidx.compose.animation.core.tween(2400, easing = androidx.compose.animation.core.EaseInOutSine),
			repeatMode = androidx.compose.animation.core.RepeatMode.Reverse,
		),
		label = "gaugeWobble",
	)
	androidx.compose.foundation.Canvas(modifier = Modifier.size((56.9 * u).dp, (28.84 * u).dp)) {
		val cx = size.width / 2f
		val cy = size.height
		val r = (25.6 * u).dp.toPx()
		val stroke = (6.75 * u).dp.toPx()
		val rect = androidx.compose.ui.geometry.Rect(cx - r, cy - r, cx + r, cy + r)
		for ((start, color) in listOf(
			180f to Color(0xFF61A57F),
			240f to Color(0xFFD8CFCF),
			300f to Color(0xFFDE4E71),
		)) {
			drawArc(
				color = color,
				startAngle = start,
				sweepAngle = 60f,
				useCenter = false,
				topLeft = rect.topLeft,
				size = rect.size,
				style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke),
			)
		}
		// Needle — round blob at the pivot tapering to a fine point (1:1159).
		val a = Math.toRadians((sweep.value + wobble).toDouble())
		val len = (19.8 * u).dp.toPx()
		val tipX = cx + (len * Math.cos(a)).toFloat()
		val tipY = cy - (len * Math.sin(a)).toFloat()
		val px = (Math.sin(a)).toFloat()   // unit perpendicular
		val py = (Math.cos(a)).toFloat()
		val wBase = (1.6 * u).dp.toPx()
		val wTip = (0.35 * u).dp.toPx()
		val needle = androidx.compose.ui.graphics.Path().apply {
			moveTo(cx + px * wBase, cy + py * wBase)
			lineTo(tipX + px * wTip, tipY + py * wTip)
			lineTo(tipX - px * wTip, tipY - py * wTip)
			lineTo(cx - px * wBase, cy - py * wBase)
			close()
		}
		drawPath(needle, Color.White)
		drawCircle(color = Color.White, radius = (2.2 * u).dp.toPx(), center = androidx.compose.ui.geometry.Offset(cx, cy))
	}
}
