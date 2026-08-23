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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
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
fun NewsDetailScreen(onBack: () -> Unit, onViewInMyStak: () -> Unit = {}) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
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
								putExtra(
									android.content.Intent.EXTRA_TEXT,
									"Apple climbs 5% on foldable iPhone push - read it on STAK: https://stak.app/news/apple-foldable-iphone-push",
								)
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
				HeroImage(media = NewsMedia.demo(), saved = saved, onBookmark = { saved = true; com.stak.demo.ui.MyStakHoldings.add("AAPL") })
				Column(
					verticalArrangement = Arrangement.spacedBy((15 * u).dp),
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = (20 * u).dp)
						.padding(top = (22 * u).dp, bottom = (28 * u).dp),
				) {
					Text(
						text = "Apple climbs 5% on foldable iPhone push",
						// RENDER-measured: the frame draws the headline ~800 device px
					// wide (≈20sp), not the metadata's 24 — lh32 box stands.
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (20 * u).sp, lineHeight = (32 * u).sp),
						color = Color.White,
					)
					Text(
						text = "A bigger foldable order and the widest iPhone lineup in years sent Apple toward a record, and to within touching distance of Nvidia’s crown.",
						// 14.3: at 14 Compose pulls "in" up to line 1; the frame
						// breaks after "lineup" (authored 3-line shape, lh22).
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (14.3 * u).sp, lineHeight = (22 * u).sp),
						color = News.Muted,
					)
					Byline()
					if (!saved) {
						AddToStakButton(onClick = { showSuccess = true })
					}
					Divider()
					StockCard(saved = saved)
					GistCard()
					Paragraph(
						"Apple had one of its best days in months on Thursday, climbing almost 5 percent after word got out that the company is planning its widest iPhone lineup in years. Nikkei Asia reported that Apple has asked suppliers to prepare at least five new models, and to lift output of its first foldable to around 10 million units, well above the seven to eight million it had penciled in.",
						size = 15.sp, line = 24.sp,
					)
					Paragraph(
						"That last number is the tell. Companies do not quietly double down on a product they expect to flop, and the foldable, which the rumor mill has taken to calling the iPhone Ultra, is now expected to land between late 2026 and the first half of 2027. Traders read the order size as confidence and bought accordingly. Apple gained about 182 billion dollars in market value on the day, nearly enough on its own to paper over a sell-off tearing through chip stocks.",
					)
					PullQuote()
					NewToThisCard()
					Paragraph(
						"The rally leaves Apple roughly 4 percent shy of retaking the title of most valuable company in the world from Nvidia, a crown the two have passed back and forth all year. It also lets the stock shake off a rough June, when a rare mid-cycle price increase on Macs and iPads, blamed on climbing memory costs, sent shares lower and rattled investors who had grown used to Apple holding the line.",
					)
					Paragraph(
						"The real verdict comes on July 30, when Apple reports fiscal third quarter results. Wall Street is penciling in revenue of around 108 billion dollars, but the number everyone will hunt for is any early read on how the new lineup, and its price tags, are actually selling.",
					)
					SourceRow()
					KeyStatsCard()
					Divider()
					Row(horizontalArrangement = Arrangement.spacedBy((8 * u).dp)) {
						ArticleTag("Apple")
						if (saved) {
							ArticleTag("Tech")
						}
					}
					ReadNext()
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
				onViewInMyStak = { saved = true; com.stak.demo.ui.MyStakHoldings.add("AAPL"); onViewInMyStak() },
				onDismiss = { showSuccess = false; saved = true; com.stak.demo.ui.MyStakHoldings.add("AAPL") },
			)
		}
	}
}

/** 360x208 r10 hero — phone art, Tech & Ai toast, play badge, bookmark/saved chip. */
@Composable
private fun HeroImage(media: NewsMedia, saved: Boolean, onBookmark: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	// The hero is a media slot: poster + play glyph at rest (frame-exact),
	// the served video playing IN PLACE once tapped (user, 2026-08-23).
	var playing by remember { mutableStateOf(false) }
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
			NewsVideoPlayer(video = video, modifier = Modifier.matchParentSize())
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
						) { playing = true },
				)
			}
		}
		Box(
			modifier = Modifier
				.align(Alignment.BottomStart)
				.padding(start = (9 * u).dp, bottom = (10 * u).dp)
				.clip(RoundedCornerShape((7.88 * u).dp))
				.background(Color(0x40242B3D))
				.padding(horizontal = (7 * u).dp, vertical = (5 * u).dp),
		) {
			Text(
				text = "Tech & Ai",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, lineHeight = (13 * u).sp),
				color = Color.White,
			)
		}
		if (saved) {
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

@Composable
private fun Byline() {
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
				text = "B",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (10 * u).sp, lineHeight = (13 * u).sp),
				color = Color(0xFF9EADC7),
			)
		}
		Row(horizontalArrangement = Arrangement.spacedBy((5 * u).dp), verticalAlignment = Alignment.CenterVertically) {
			Text(
				text = "Bloomberg",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp, lineHeight = (16 * u).sp),
				color = Color.White,
			)
			Text(
				text = "· Jul 2 · 3 min read",
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

/** AAPL price card — badge, Daily chip, $308.63 + sparkline; saved adds View row. */
@Composable
private fun StockCard(saved: Boolean) {
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
					text = "A",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (18 * u).sp, lineHeight = (23 * u).sp),
					color = Color(0xFF9EADC7),
				)
			}
			Spacer(modifier = Modifier.width((12 * u).dp))
			Column(verticalArrangement = Arrangement.spacedBy((3 * u).dp)) {
				Text(
					text = "Apple Inc.",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp, lineHeight = (19 * u).sp),
					color = Color.White,
				)
				Text(
					text = "AAPL",
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
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (8 * u).sp, lineHeight = (10 * u).sp, letterSpacing = (0.4 * u).sp),
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
					text = "$308.63",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (26 * u).sp, lineHeight = (33 * u).sp),
					color = Color.White,
				)
				Text(
					text = "+4.84% today",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (13 * u).sp, lineHeight = (17 * u).sp),
					color = News.Green,
				)
			}
			Spacer(modifier = Modifier.weight(1f))
			Image(
				painter = painterResource(R.drawable.news_sparkline),
				contentDescription = null,
				modifier = Modifier.size((110 * u).dp, (40 * u).dp),
			)
		}
		if (saved) {
			Box(modifier = Modifier.fillMaxWidth().height((1 * u).dp).background(News.Divider))
			Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
				Text(
					text = "View AAPL in My STAK",
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
private fun GistCard() {
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
		GistBullet("Apple rose about 5% on plans for its widest iPhone lineup yet.")
		GistBullet("It raised foldable orders to 10 million units, a show of confidence.")
		GistBullet("The stock sits about 4% from passing Nvidia as the most valuable company.")
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
private fun PullQuote() {
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
			text = "Companies do not quietly double down on a product they expect to flop.",
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (16 * u).sp, lineHeight = (26 * u).sp),
			color = Color(0xFFD3D3DD),
			modifier = Modifier.weight(1f),
		)
	}
}

@Composable
private fun NewToThisCard() {
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
			text = "A foldable phone opens out into a small tablet. For Apple it means a pricier device to sell, and a way to win back buyers who drifted to Samsung, which has offered foldables for years.",
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
private fun KeyStatsCard() {
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
		StatRow("Market cap", "$4.58T", "P/E ratio", "34.2")
		StatRow("Day range", "$301.20–$309.80", "Volume", "82.4M")
		StatRow("52-wk range", "$201.50–$317.40", "Div yield", "0.42%")
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

@Composable
private fun ReadNext() {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(verticalArrangement = Arrangement.spacedBy((10 * u).dp), modifier = Modifier.fillMaxWidth().padding(top = (6 * u).dp)) {
		Text(
			text = "READ NEXT",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, lineHeight = (13 * u).sp, letterSpacing = (0.5 * u).sp),
			color = News.Muted,
		)
		repeat(2) {
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
					painter = painterResource(R.drawable.news_thumb_tsla_rn),
					contentDescription = null,
					contentScale = ContentScale.Crop,
					modifier = Modifier.size((60 * u).dp).clip(RoundedCornerShape((10 * u).dp)),
				)
				Column(verticalArrangement = Arrangement.spacedBy((5 * u).dp), modifier = Modifier.weight(1f)) {
					Text(
						text = "CNBC · 2d",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (11 * u).sp, lineHeight = (14 * u).sp),
						color = News.Muted,
					)
					Text(
						text = "Tesla drops 7% even after beating deliveries",
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = (14 * u).sp, lineHeight = (19 * u).sp),
						color = Color.White,
					)
				}
			}
		}
	}
}

/** Save success — rgba(12,19,32,0.55) scrim + the r24 #181f30 bottom sheet (101:1169). */
@Composable
private fun SaveSuccessOverlay(onViewInMyStak: () -> Unit, onDismiss: () -> Unit) {
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
						text = "A",
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp),
						color = Color(0xFF9EADC7),
					)
				}
				Column(verticalArrangement = Arrangement.spacedBy((2 * u).dp), modifier = Modifier.weight(1f)) {
					Text(
						text = "Apple",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (13 * u).sp),
						color = Color.White,
					)
					Text(
						text = "$229.35 today",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (10 * u).sp),
						color = News.Muted,
					)
				}
				Text(
					text = "▲ 1.2%",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp),
					color = News.Green,
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
private fun NewsVideoPlayer(video: NewsMedia.Video, modifier: Modifier = Modifier) {
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
					loadUrl(embed)
				}
			},
		)
	} else {
		androidx.compose.ui.viewinterop.AndroidView(
			modifier = modifier,
			factory = { ctx ->
				android.widget.VideoView(ctx).apply {
					setVideoURI(android.net.Uri.parse(video.url))
					setOnPreparedListener { mp -> mp.isLooping = false; start() }
				}
			},
		)
	}
}
