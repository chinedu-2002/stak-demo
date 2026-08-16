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
	Column(modifier = Modifier.fillMaxSize().background(StakColors.Bg)) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
				.background(StakColors.Bg)
				.statusBarsPadding()
				.padding(horizontal = 20.dp)
				.padding(top = 22.dp),
		) {
			Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
				Text(
					text = "News",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 26.sp),
					color = Color.White,
				)
				Text(
					text = "Saturday, July 4",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 13.sp),
					color = News.Muted,
				)
			}
			Spacer(modifier = Modifier.weight(1f))
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier.size(40.dp).background(News.CardBg, CircleShape),
			) {
				Image(
					painter = painterResource(R.drawable.ic_news_search),
					contentDescription = "Search",
					modifier = Modifier.size(20.dp),
				)
			}
		}
		Column(
			verticalArrangement = Arrangement.spacedBy(22.dp),
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth()
				.verticalScroll(rememberScrollState())
				.padding(horizontal = 20.dp)
				.padding(top = 22.dp),
		) {
			MoodMiniRow()
			BriefCarousel(onRead = onOpenArticle)
			StoryGrid(onOpenArticle = onOpenArticle)
			NewsSection(
				title = "For You",
				rows = listOf(
					Triple(R.drawable.news_thumb_nvda, "Reuters · 2d", "Nvidia lags the chip rally it kicked off"),
					Triple(R.drawable.news_thumb_aapl, "Bloomberg · 2d", "Apple climbs 5% on foldable iPhone push"),
					Triple(R.drawable.news_thumb_tsla, "CNBC · 2d", "Tesla drops 7% even after beating deliveries"),
				),
				onOpenArticle = onOpenArticle,
			)
			NewsSection(
				title = "Markets",
				rows = listOf(
					Triple(R.drawable.news_thumb_jobs, "Reuters · 2d", "June jobs miss eases Fed hike bets"),
					Triple(R.drawable.news_thumb_chips, "Bloomberg · 2d", "Memory chips soar as the AI trade rotates"),
					Triple(R.drawable.news_thumb_oil, "Reuters · 3d", "Oil slips after positive Iran talks"),
				),
				onOpenArticle = onOpenArticle,
			)
			Spacer(modifier = Modifier.height(0.dp))
		}
	}
}

/** Compact Market Mood row — #171d2c r12 with the small low-volatility gauge. */
@Composable
private fun MoodMiniRow() {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(12.dp))
			.background(News.MoodBg)
			.padding(horizontal = 14.dp, vertical = 12.dp),
	) {
		Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
			Text(
				text = "Market Mood",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 13.sp),
				color = Color.White,
			)
			Text(
				text = "Low volatility",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 11.sp),
				color = News.Teal,
			)
		}
		Spacer(modifier = Modifier.weight(1f))
		Image(
			painter = painterResource(R.drawable.news_gauge_small),
			contentDescription = null,
			modifier = Modifier.size(40.97.dp, 20.76.dp),
		)
	}
}

/** TODAY'S BRIEF — teal r18 feature card + pager dots. */
@Composable
private fun BriefCarousel(onRead: () -> Unit) {
	Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
		Column(
			verticalArrangement = Arrangement.spacedBy(9.dp),
			modifier = Modifier
				.fillMaxWidth()
				.clip(RoundedCornerShape(18.dp))
				.background(News.Teal)
				.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null,
					onClick = onRead,
				)
				.padding(start = 18.dp, end = 18.dp, top = 18.dp, bottom = 16.dp),
		) {
			Text(
				text = "TODAY’S BRIEF",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 10.sp, letterSpacing = 0.6.sp),
				color = News.Ink,
			)
			Text(
				text = "Dow closes at a record as chips slide",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 19.sp, lineHeight = 25.sp),
				color = News.Ink,
			)
			Text(
				text = "Wall Street split into the long weekend. The Dow hit an all time high while a memory chip rout pulled the Nasdaq down, and a soft jobs report eased the pressure on the...",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 17.sp),
				color = News.Ink,
			)
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
			) {
				Text(
					text = "Bloomberg · 10h",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 11.sp),
					color = News.Ink.copy(alpha = 0.6f),
				)
				Spacer(modifier = Modifier.weight(1f))
				Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
					Text(
						text = "Read",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp),
						color = News.Ink,
					)
					Text(
						text = "›",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 13.sp),
						color = News.Ink,
					)
				}
			}
		}
		// Pager dots — 16x6 active pill (#69b3ca) + three #5c6b85 dots.
		Canvas(modifier = Modifier.size(52.dp, 6.dp)) {
			drawRoundRect(
				News.Teal,
				size = androidx.compose.ui.geometry.Size(16.dp.toPx(), 6.dp.toPx()),
				cornerRadius = androidx.compose.ui.geometry.CornerRadius(3.dp.toPx()),
			)
			for (i in 0..2) {
				drawCircle(
					Color(0xFF5C6B85),
					radius = 3.dp.toPx(),
					center = androidx.compose.ui.geometry.Offset((25 + i * 12).dp.toPx(), 3.dp.toPx()),
				)
			}
		}
	}
}

/** The two 128dp story tiles ("Markets" / "Your stocks"). */
@Composable
private fun StoryGrid(onOpenArticle: () -> Unit) {
	Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth().height(128.dp)) {
		StoryTile(
			tag = "Markets",
			headline = "Fed minutes land Wednesday",
			source = "Reuters · 2h",
			onClick = {},
			modifier = Modifier.weight(1f),
		)
		StoryTile(
			tag = "Your stocks",
			headline = "Apple Climbs 5% on foldable iphone",
			source = "CNBC · 3h",
			onClick = onOpenArticle,
			modifier = Modifier.weight(1f),
		)
	}
}

@Composable
private fun StoryTile(tag: String, headline: String, source: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
	Column(
		verticalArrangement = Arrangement.spacedBy(8.dp),
		modifier = modifier
			.height(128.dp)
			.clip(RoundedCornerShape(12.dp))
			.background(News.CardBg)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			)
			.padding(14.dp),
	) {
		NewsTag(text = tag, letterSpacing = 0.4.sp)
		Text(
			text = headline,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Light, fontSize = 12.sp, lineHeight = 20.sp),
			color = Color.White,
		)
		Spacer(modifier = Modifier.weight(1f))
		Text(
			text = source,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 10.sp),
			color = News.Muted,
		)
	}
}

/** #242b3d r5 chip — Geist 8 #819abb. */
@Composable
internal fun NewsTag(text: String, letterSpacing: androidx.compose.ui.unit.TextUnit = 0.sp) {
	Box(
		modifier = Modifier
			.clip(RoundedCornerShape(5.dp))
			.background(News.ChipBg)
			.padding(horizontal = 7.dp, vertical = 3.dp),
	) {
		Text(
			text = text,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 8.sp, letterSpacing = letterSpacing),
			color = News.Muted,
		)
	}
}

/** "For You" / "Markets" — Sora 16 #d3d3d3 header + 60dp-thumb cards. */
@Composable
private fun NewsSection(
	title: String,
	rows: List<Triple<Int, String, String>>,
	onOpenArticle: () -> Unit,
) {
	Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
		Text(
			text = title,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
			color = News.HeaderGray,
			modifier = Modifier.padding(bottom = 2.dp),
		)
		rows.forEach { (thumbRes, source, headline) ->
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(12.dp),
				modifier = Modifier
					.fillMaxWidth()
					.clip(RoundedCornerShape(14.dp))
					.background(News.CardBg)
					.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
					) { if ("Apple" in headline) onOpenArticle() }
					.padding(12.dp),
			) {
				Image(
					painter = painterResource(thumbRes),
					contentDescription = null,
					contentScale = ContentScale.Crop,
					modifier = Modifier.size(60.dp).clip(RoundedCornerShape(10.dp)),
				)
				Column(verticalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.weight(1f)) {
					Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
						Text(
							text = source,
							style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 11.sp),
							color = News.Muted,
						)
						Spacer(modifier = Modifier.weight(1f))
						NewsTag(text = "In your STAK")
					}
					Text(
						text = headline,
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Light, fontSize = 12.sp, lineHeight = 19.sp),
						color = Color.White,
					)
				}
			}
		}
	}
}
