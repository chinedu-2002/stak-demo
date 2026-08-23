package com.stak.demo.ui.news

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.R
import com.stak.demo.ui.theme.Geist
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors

/** Palette of the CHINEDU "03 · News" frames. */
internal object News {
	val MoodBg = Color(0xFF171D2C)
	val CardBg = Color(0xFF181F30)
	val Teal = Color(0xFF69B3CA)
	val Ink = Color(0xFF0E162B)
	val Muted = Color(0xFF819ABB)
	val Faint = Color(0xFF5C6B85)
	val ChipBg = Color(0xFF242B3D)
	val HeaderGray = Color(0xFFD3D3D3)
	val Body = Color(0xFFC8D2E0)
	val Green = Color(0xFF2FD08A)
	val Divider = Color(0xFF2A3346)
}

/**
 * 03 · News — "News listing tab" (CHINEDU 1:1228). Fixed header ("News",
 * date, search circle), then the scrolling stack: the compact Market
 * Mood row, the teal TODAY'S BRIEF carousel card with pager dots, the
 * two-tile story grid, and the For You / Markets card lists. The tab
 * bar comes from the MainShell.
 */
@Composable
fun NewsScreen(onOpenArticle: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	// Designer's call (2026-08-22): the search icon opens a search bar that
	// word-matches the news content; the list is empty when nothing matches.
	var searching by rememberSaveable { mutableStateOf(false) }
	var query by rememberSaveable { mutableStateOf("") }
	val q = query.trim()
	fun matches(text: String) = q.isEmpty() || text.contains(q, ignoreCase = true)
	Column(modifier = Modifier.fillMaxSize().background(StakColors.Bg)) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
				.background(StakColors.Bg)
				.statusBarsPadding()
				.padding(horizontal = (20 * u).dp)
				.padding(top = (22 * u).dp),
		) {
			Column(verticalArrangement = Arrangement.spacedBy((4 * u).dp)) {
				Text(
					text = "News",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (26 * u).sp, lineHeight = (33 * u).sp),
					color = Color.White,
				)
				Text(
					text = "Saturday, July 4",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (13 * u).sp, lineHeight = (17 * u).sp),
					color = News.Muted,
				)
			}
			Spacer(modifier = Modifier.weight(1f))
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.size((40 * u).dp)
					.background(News.CardBg, CircleShape)
					.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
					) {
						searching = !searching
						if (!searching) query = ""
					},
			) {
				Image(
					painter = painterResource(R.drawable.ic_news_search),
					contentDescription = "Search",
					modifier = Modifier.size((20 * u).dp),
				)
			}
		}
		if (searching) {
			BasicTextField(
				value = query,
				onValueChange = { query = it },
				singleLine = true,
				textStyle = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (13 * u).sp, color = Color.White),
				cursorBrush = SolidColor(News.Teal),
				decorationBox = { inner ->
					Box(contentAlignment = Alignment.CenterStart) {
						if (query.isEmpty()) {
							Text(
								text = "Search news",
								style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (13 * u).sp),
								color = News.Faint,
							)
						}
						inner()
					}
				},
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = (20 * u).dp)
					.padding(top = (12 * u).dp)
					.clip(RoundedCornerShape((12 * u).dp))
					.background(News.CardBg)
					.padding(horizontal = (16 * u).dp, vertical = (13 * u).dp),
			)
		}
		Column(
			verticalArrangement = Arrangement.spacedBy((22 * u).dp),
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth()
				.verticalScroll(rememberScrollState())
				.padding(horizontal = (20 * u).dp)
				.padding(top = (22 * u).dp),
		) {
			if (q.isEmpty()) MoodMiniRow()
			// Designer's call (2026-08-22): today's brief on tap leads to the
			// News info page (the Story tile stays wired per panel 1:1228).
			if (NewsBriefFeed.briefs().any { matches(it.title) }) {
				BriefCarousel(onRead = onOpenArticle)
			}
			StoryGrid(onOpenArticle = onOpenArticle, query = q)
			val forYou = listOf(
				Triple(R.drawable.news_thumb_nvda, "Reuters · 2d", "Nvidia lags the chip rally it kicked off"),
				Triple(R.drawable.news_thumb_aapl, "Bloomberg · 2d", "Apple climbs 5% on foldable iPhone push"),
				Triple(R.drawable.news_thumb_tsla, "CNBC · 2d", "Tesla drops 7% even after beating deliveries"),
			).filter { matches(it.third) }
			if (forYou.isNotEmpty()) NewsSection(title = "For You", rows = forYou)
			val markets = listOf(
				Triple(R.drawable.news_thumb_jobs, "Reuters · 2d", "June jobs miss eases Fed hike bets"),
				Triple(R.drawable.news_thumb_chips, "Bloomberg · 2d", "Memory chips soar as the AI trade rotates"),
				Triple(R.drawable.news_thumb_oil, "Reuters · 3d", "Oil slips after positive Iran talks"),
			).filter { matches(it.third) }
			if (markets.isNotEmpty()) NewsSection(title = "Markets", rows = markets)
			Spacer(modifier = Modifier.height(0.dp))
		}
	}
}

/** Compact Market Mood row — #171d2c r12 with the small low-volatility gauge. */
@Composable
private fun MoodMiniRow() {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((12 * u).dp))
			.background(News.MoodBg)
			.padding(horizontal = (14 * u).dp, vertical = (12 * u).dp),
	) {
		Column(verticalArrangement = Arrangement.spacedBy((2 * u).dp)) {
			Text(
				text = "Market Mood",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (13 * u).sp, lineHeight = (16 * u).sp),
				color = Color.White,
			)
			Text(
				text = "Low volatility",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (11 * u).sp, lineHeight = (14 * u).sp),
				color = News.Teal,
			)
		}
		Spacer(modifier = Modifier.weight(1f))
		Image(
			painter = painterResource(R.drawable.news_gauge_small),
			contentDescription = null,
			modifier = Modifier.size((40.97 * u).dp, (20.76 * u).dp),
		)
	}
}

/**
 * TODAY'S BRIEF — teal r18 feature card + pager dots. The user swipes
 * left and right through the served briefs (user, 2026-08-22); the
 * active dot follows the page. Text comes from NewsBriefFeed.
 */
@Composable
private fun BriefCarousel(onRead: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	val briefs = NewsBriefFeed.briefs()
	val pager = androidx.compose.foundation.pager.rememberPagerState(pageCount = { briefs.size })
	Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy((12 * u).dp)) {
		androidx.compose.foundation.pager.HorizontalPager(
			state = pager,
			modifier = Modifier.fillMaxWidth(),
		) { page ->
			BriefCard(brief = briefs[page], onRead = onRead)
		}
		// Pager dots — the active page is the 16x6 teal pill, the rest
		// 6px #5c6b85 dots, 6px gaps: the authored 52x6 strip.
		Row(
			horizontalArrangement = Arrangement.spacedBy((6 * u).dp),
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.height((6 * u).dp),
		) {
			for (i in briefs.indices) {
				if (i == pager.currentPage) {
					Box(modifier = Modifier.size((16 * u).dp, (6 * u).dp).background(News.Teal, RoundedCornerShape((3 * u).dp)))
				} else {
					Box(modifier = Modifier.size((6 * u).dp).background(Color(0xFF5C6B85), CircleShape))
				}
			}
		}
	}
}

/** One brief card in the carousel slot. */
@Composable
private fun BriefCard(brief: NewsBriefFeed.Brief, onRead: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((9 * u).dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((18 * u).dp))
			.background(News.Teal)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onRead,
			)
			.padding(start = (18 * u).dp, end = (18 * u).dp, top = (18 * u).dp, bottom = (16 * u).dp),
	) {
		Text(
			text = "TODAY\u2019S BRIEF",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, lineHeight = (13 * u).sp, letterSpacing = (0.6 * u).sp),
			color = News.Ink,
		)
		Text(
			text = brief.title,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (19 * u).sp, lineHeight = (25 * u).sp),
			color = News.Ink,
		)
		Text(
			text = brief.body,
			// Authored 13/lh17 wraps to 3 lines in the 314 box; Compose
			// shapes Geist wider — 12.2 restores the authored 3-line wrap.
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (12.2 * u).sp, lineHeight = (17 * u).sp),
			color = News.Ink,
		)
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.fillMaxWidth().height((21 * u).dp),
		) {
			Text(
				text = brief.source,
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (11 * u).sp, lineHeight = (14 * u).sp),
				color = News.Ink.copy(alpha = 0.6f),
			)
			Spacer(modifier = Modifier.weight(1f))
			Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((4 * u).dp)) {
				Text(
					text = "Read",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp),
					color = News.Ink,
				)
				Text(
					text = "\u203a",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (13 * u).sp),
					color = News.Ink,
				)
			}
		}
	}
}

/** The two 128dp story tiles ("Markets" / "Your stocks"). */
@Composable
private fun StoryGrid(onOpenArticle: () -> Unit, query: String = "") {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	val showFed = query.isEmpty() || "Fed minutes land Wednesday".contains(query, ignoreCase = true)
	val showApple = query.isEmpty() || "Apple Climbs 5% on foldable iphone".contains(query, ignoreCase = true)
	if (!showFed && !showApple) return
	Row(horizontalArrangement = Arrangement.spacedBy((12 * u).dp), modifier = Modifier.fillMaxWidth().height((128 * u).dp)) {
		if (showFed) {
			StoryTile(
				tag = "Markets",
				headline = "Fed minutes land Wednesday",
				source = "Reuters · 2h",
				onClick = {},
				modifier = Modifier.weight(1f),
			)
		}
		if (showApple) {
			StoryTile(
				tag = "Your stocks",
				headline = "Apple Climbs 5% on foldable iphone",
				source = "CNBC · 3h",
				onClick = onOpenArticle,
				modifier = Modifier.weight(1f),
			)
		}
	}
}

@Composable
private fun StoryTile(tag: String, headline: String, source: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((8 * u).dp),
		modifier = modifier
			.height((128 * u).dp)
			.clip(RoundedCornerShape((12 * u).dp))
			.background(News.CardBg)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			)
			.padding((14 * u).dp),
	) {
		NewsTag(text = tag, letterSpacing = (0.4 * u).sp)
		Text(
			text = headline,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Light, fontSize = (14 * u).sp, lineHeight = (20 * u).sp),
			color = Color.White,
		)
		Spacer(modifier = Modifier.weight(1f))
		Text(
			text = source,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (10 * u).sp, lineHeight = (13 * u).sp),
			color = News.Muted,
		)
	}
}

/** #242b3d r5 chip — Geist 8 #819abb. */
@Composable
internal fun NewsTag(text: String, letterSpacing: androidx.compose.ui.unit.TextUnit = 0.sp) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Box(
		modifier = Modifier
			.clip(RoundedCornerShape((5 * u).dp))
			.background(News.ChipBg)
			.padding(horizontal = (7 * u).dp, vertical = (3 * u).dp),
	) {
		Text(
			text = text,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (8 * u).sp, lineHeight = (10 * u).sp, letterSpacing = letterSpacing),
			color = News.Muted,
		)
	}
}

/** "For You" / "Markets" — Sora 16 #d3d3d3 header + 60dp-thumb cards. */
@Composable
private fun NewsSection(
	title: String,
	rows: List<Triple<Int, String, String>>,
) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(verticalArrangement = Arrangement.spacedBy((10 * u).dp), modifier = Modifier.fillMaxWidth()) {
		Text(
			text = title,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (16 * u).sp, lineHeight = (22 * u).sp),
			color = News.HeaderGray,
		)
		rows.forEach { (thumbRes, source, headline) ->
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy((12 * u).dp),
				modifier = Modifier
					.fillMaxWidth()
					.clip(RoundedCornerShape((14 * u).dp))
					.background(News.CardBg)
					.padding((12 * u).dp),
			) {
				Image(
					painter = painterResource(thumbRes),
					contentDescription = null,
					contentScale = ContentScale.Crop,
					modifier = Modifier.size((60 * u).dp).clip(RoundedCornerShape((10 * u).dp)),
				)
				Column(verticalArrangement = Arrangement.spacedBy((5 * u).dp), modifier = Modifier.weight(1f)) {
					Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
						Text(
							text = source,
							style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (11 * u).sp, lineHeight = (14 * u).sp),
							color = News.Muted,
						)
						Spacer(modifier = Modifier.weight(1f))
						NewsTag(text = "In your STAK")
					}
					Text(
						text = headline,
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Light, fontSize = (14 * u).sp, lineHeight = (19 * u).sp),
						color = Color.White,
					)
				}
			}
		}
	}
}
