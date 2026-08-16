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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import com.stak.demo.ui.theme.Inter
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

private val HOME_TABS = listOf(
	"Home" to R.drawable.ic_tab_home,
	"News" to R.drawable.ic_tab_news,
	"Discover" to R.drawable.ic_tab_discover,
	"My STAK" to R.drawable.ic_tab_mystak,
	"Simulate" to R.drawable.ic_tab_simulate,
)

/**
 * 02 · Home — CHINEDU "Home first run" (1:958) and "Home Main" (1:1097).
 *
 * Both frames share the whole content stack: fixed top nav (STAK logo,
 * bell + profile circles, "Good Morning, Hamza"), the Market Mood card
 * with its clipped news-deck stack, the "Why this matters" row and the
 * teal deck banner. First run replaces the tab bar with a bottom scrim
 * and the frosted "See Todays Pick" pill; tapping it reveals Home Main
 * (prototype: Swap overlay · Instant).
 */
@Composable
fun HomeScreen(onProfile: () -> Unit = {}) {
	var firstRun by rememberSaveable { mutableStateOf(true) }

	BoxWithConstraints(modifier = Modifier.fillMaxSize().background(StakColors.Bg)) {
		val statusPad = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
		Column(modifier = Modifier.fillMaxSize()) {
			TopNav(onProfile = onProfile)
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				modifier = Modifier
					.weight(1f)
					.fillMaxWidth()
					.verticalScroll(rememberScrollState())
					.padding(horizontal = 20.dp),
			) {
				Spacer(modifier = Modifier.height(21.dp))
				MarketMoodCard()
				Spacer(modifier = Modifier.height(10.dp))
				WhyThisMattersCard()
				Spacer(modifier = Modifier.height(20.dp))
				DeckBanner()
				Spacer(modifier = Modifier.height(if (firstRun) 140.dp else 20.dp))
			}
			if (!firstRun) {
				HomeTabBar()
			}
		}
		if (firstRun) {
			// The frame pins the scrim 41px above the deck banner (Tab bar
			// y629 vs banner y670) — anchor to the same content geometry:
			// banner top = status inset + 626, so the scrim starts 585 below
			// the inset and runs to the physical bottom of the screen.
			FirstRunOverlay(
				onSeeTodaysPick = { firstRun = false },
				modifier = Modifier
					.align(Alignment.BottomCenter)
					.height(this@BoxWithConstraints.maxHeight - statusPad - 585.dp),
			)
		}
	}
}

/** Fixed top nav — logo row with bell/profile circles + greeting (Figma 131px block). */
@Composable
private fun TopNav(onProfile: () -> Unit) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.background(StakColors.Bg)
			.statusBarsPadding()
			.padding(horizontal = 17.dp)
			.padding(top = 22.dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().height(35.dp)) {
			Image(
				painter = painterResource(R.drawable.ic_stak_logo_mark),
				contentDescription = null,
				modifier = Modifier.size(26.48.dp),
			)
			Spacer(modifier = Modifier.width(4.49.dp))
			Image(
				painter = painterResource(R.drawable.ic_stak_wordmark),
				contentDescription = "STAK",
				modifier = Modifier.size(78.16.dp, 14.98.dp),
			)
			Spacer(modifier = Modifier.weight(1f))
			Image(
				painter = painterResource(R.drawable.ic_nav_bell),
				contentDescription = "Notifications",
				modifier = Modifier.size(35.dp),
			)
			Spacer(modifier = Modifier.width(4.dp))
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.size(35.dp)
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
					modifier = Modifier.size(12.99.dp, 13.64.dp),
				)
			}
		}
		Spacer(modifier = Modifier.height(10.dp))
		Text(
			text = "Good Morning, Hamza",
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
			color = Color.White,
		)
	}
}

/** Market Mood — 350x397 #171d2c card with the clipped news-deck stack. */
@Composable
private fun MarketMoodCard() {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.height(397.dp)
			.clip(RoundedCornerShape(8.dp))
			.background(Home.CardBg),
	) {
		Box(modifier = Modifier.matchParentSize()) { NewsDeck() }
		// The frame's bottom strip (1:1175, 30px) backdrop-blurs the stack —
		// redraw the same deck blurred, clipped to the card's last 30dp.
		Box(
			modifier = Modifier
				.align(Alignment.BottomCenter)
				.fillMaxWidth()
				.height(30.dp)
				.clipToBounds(),
		) {
			// An oversized child gets centered in the 30dp band; shift it up
			// by (397-30)/2 so the stack's bottom edge lines up with the band.
			Box(
				modifier = Modifier
					.offset(y = (-183.5).dp)
					.fillMaxWidth()
					.requiredHeight(397.dp)
					.blur(4.dp),
			) { NewsDeck() }
		}
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Center,
			modifier = Modifier.fillMaxWidth().padding(top = 25.dp),
		) {
			Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.width(180.dp)) {
				Text(
					text = "Market Mood",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Medium, fontSize = 20.sp),
					color = Color.White,
				)
				Text(
					text = buildAnnotatedString {
						withStyle(SpanStyle(color = Home.Teal)) { append("High volatility") }
						append(", you should consider being cautious.")
					},
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 12.sp),
					color = Color.White,
				)
			}
			Spacer(modifier = Modifier.width(46.dp))
			Image(
				painter = painterResource(R.drawable.home_mood_gauge),
				contentDescription = null,
				modifier = Modifier.size(56.9.dp, 28.84.dp),
			)
		}
	}
}

/**
 * The stacked news cards in their frame poses — front card straight, the
 * two behind rotated; offsets are from the parent card's center (350x397).
 */
@Composable
private fun BoxScope.NewsDeck() {
	NewsDeckCard(
		bg = Home.PaperWhite,
		title = "Wall Street's fear gauge reads 32",
		body = "The Fear & Greed Index is firmly in Fear territory. Money is rotating out of the ....",
		bodyWeight = FontWeight.Light,
		bodySize = 12.sp,
		titleBodyGap = 12.dp,
		offsetX = 6.2.dp,
		offsetY = 59.73.dp,
		rotation = 0f,
	)
	NewsDeckCard(
		bg = Home.Teal,
		title = "Fed meeting notes drop Wednesday",
		body = "Minutes from the last Fed meeting land July 8. A market this tense moves on every word....",
		bodyWeight = FontWeight.Normal,
		bodySize = 11.89.sp,
		titleBodyGap = 17.dp,
		offsetX = 8.33.dp,
		offsetY = 189.44.dp,
		rotation = -3.72f,
	)
	NewsDeckCard(
		bg = Home.PaperWhite,
		title = "The OpenAI IPO is reportedly delayed",
		body = "The year's most anticipated listing just slipped. Markets riding a wave of IPO excitement...",
		bodyWeight = FontWeight.Light,
		bodySize = 12.sp,
		titleBodyGap = 12.dp,
		offsetX = (-0.02).dp,
		offsetY = 264.57.dp,
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
	Column(
		verticalArrangement = Arrangement.spacedBy(titleBodyGap),
		modifier = Modifier
			.align(Alignment.Center)
			.offset(x = offsetX, y = offsetY)
			.graphicsLayer { rotationZ = rotation }
			.size(236.86.dp, 278.45.dp)
			.clip(RoundedCornerShape(6.79.dp))
			.background(bg)
			.padding(start = 14.43.dp, top = 23.77.dp),
	) {
		Text(
			text = title,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Medium, fontSize = 16.sp),
			color = Home.CardInk,
			modifier = Modifier.width(202.9.dp),
		)
		Text(
			text = body,
			style = TextStyle(fontFamily = Geist, fontWeight = bodyWeight, fontSize = bodySize),
			color = Home.CardInk,
			modifier = Modifier.width(189.31.dp),
		)
	}
}

/** "Why this matters to you" — 350x91 card with the glass caution ball art. */
@Composable
private fun WhyThisMattersCard() {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.height(91.dp)
			.clip(RoundedCornerShape(8.dp))
			.background(Home.CardBg),
	) {
		Image(
			painter = painterResource(R.drawable.home_caution_ball),
			contentDescription = null,
			modifier = Modifier.offset(x = 3.dp, y = (-7).dp).size(105.dp),
		)
		Column(
			verticalArrangement = Arrangement.spacedBy(6.dp),
			modifier = Modifier.align(Alignment.CenterStart).padding(start = 127.dp),
		) {
			Text(
				text = "Why this matters to you",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 15.sp),
				color = Color.White,
			)
			Text(
				text = "Your STAK collections houses 80% of stocks from effected industries.",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Light, fontSize = 12.sp, lineHeight = 15.sp),
				color = Color.White,
				modifier = Modifier.width(198.dp),
			)
		}
	}
}

/** Teal deck banner — 350x116 with the box-and-coins art and Go to Deck chip. */
@Composable
private fun DeckBanner() {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.height(116.dp)
			.clip(RoundedCornerShape(8.dp))
			.background(Home.Teal),
	) {
		// The illustration zone of the frame (box + coins + shadow), cropped
		// from the banner render so its pose is exact; the teal it carries is
		// the same banner fill it sits on.
		Image(
			painter = painterResource(R.drawable.home_banner_illustration),
			contentDescription = null,
			contentScale = ContentScale.Fit,
			modifier = Modifier.align(Alignment.CenterStart).size(172.dp, 116.dp),
		)
		Column(
			verticalArrangement = Arrangement.spacedBy(10.dp),
			modifier = Modifier
				.align(Alignment.CenterStart)
				.padding(start = 184.dp)
				.offset(y = 0.5.dp)
				.width(156.dp),
		) {
			Text(
				text = "Take your first deck to build your taste",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Light, fontSize = 12.sp, lineHeight = 15.sp),
				color = Color.Black,
			)
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.size(123.dp, 32.dp)
					.background(StakColors.Bg, RoundedCornerShape(15.dp))
					.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
					) { /* Deck screen lands in a later phase. */ },
			) {
				Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
					Text(
						text = "Go to Deck",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 11.49.sp),
						color = Color.White,
					)
					Image(
						painter = painterResource(R.drawable.ic_arrow_right_small),
						contentDescription = null,
						modifier = Modifier.size(16.dp),
					)
				}
			}
		}
	}
}

/** Home Main tab bar — 86px #060c1d, five 24px icons with Inter 12 labels, gap 28. */
@Composable
private fun HomeTabBar() {
	Box(modifier = Modifier.fillMaxWidth().background(Home.TabBg).navigationBarsPadding()) {
		Row(
			horizontalArrangement = Arrangement.spacedBy(28.dp),
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.align(Alignment.Center).height(86.dp),
		) {
			HOME_TABS.forEach { (label, iconRes) ->
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.spacedBy(10.dp),
					modifier = Modifier.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
					) { /* Other tabs land in later phases. */ },
				) {
					Image(
						painter = painterResource(iconRes),
						contentDescription = null,
						modifier = Modifier.size(24.dp),
					)
					Text(
						text = label,
						style = TextStyle(fontFamily = Inter, fontWeight = FontWeight.Normal, fontSize = 12.sp),
						color = Color.White,
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
	Box(modifier = modifier.fillMaxWidth()) {
		Column(modifier = Modifier.matchParentSize()) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(98.7.dp)
					.background(Brush.verticalGradient(0f to Color(0x000A1020), 1f to StakColors.Bg)),
			)
			Box(modifier = Modifier.fillMaxWidth().weight(1f).background(StakColors.Bg))
		}
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.align(Alignment.BottomCenter)
				.padding(bottom = 70.dp)
				.size(136.dp, 51.dp)
				.clip(RoundedCornerShape(50))
				.background(Color(0x0FFFFFFF))
				.border(0.94.dp, Color(0x66FFFFFF), RoundedCornerShape(50))
				.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null,
					onClick = onSeeTodaysPick,
				),
		) {
			Text(
				text = "See Todays Pick",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp),
				color = Color.White,
			)
		}
	}
}
