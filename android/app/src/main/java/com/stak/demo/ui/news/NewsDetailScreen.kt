package com.stak.demo.ui.news

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
private val CtaBorder = Color(0xA1659EAD)

/**
 * 03 · News — the article page in its three frames: "News detail page
 * unsaved" (1:1495), "News detail · Save success" (101:1005, the bottom
 * sheet over a scrim) and "News detail page saved" (1:1359 — hero toast,
 * View-in-My-STAK row on the stock card, Apple + Tech tags).
 */
@Composable
fun NewsDetailScreen(onBack: () -> Unit) {
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
					.padding(start = 16.dp, end = 18.dp, top = 10.dp, bottom = 12.dp),
			) {
				AuthBackCircle(onClick = onBack)
				Spacer(modifier = Modifier.weight(1f))
				Image(
					painter = painterResource(R.drawable.ic_news_share),
					contentDescription = "Share",
					modifier = Modifier.size(24.dp),
				)
			}
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				modifier = Modifier
					.weight(1f)
					.fillMaxWidth()
					.verticalScroll(rememberScrollState()),
			) {
				HeroImage(saved = saved)
				Column(
					verticalArrangement = Arrangement.spacedBy(15.dp),
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 20.dp)
						.padding(top = 22.dp, bottom = 28.dp),
				) {
					Text(
						text = "Apple climbs 5% on foldable iPhone push",
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 32.sp),
						color = Color.White,
					)
					Text(
						text = "A bigger foldable order and the widest iPhone lineup in years sent Apple toward a record, and to within touching distance of Nvidia’s crown.",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 22.sp),
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
					Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
						ArticleTag("Apple")
						if (saved) {
							ArticleTag("Tech")
						}
					}
					ReadNext()
				}
			}
		}
		if (showSuccess) {
			SaveSuccessOverlay(
				onViewInMyStak = { showSuccess = false; saved = true },
				onDismiss = { showSuccess = false; saved = true },
			)
		}
	}
}

/** 360x208 r10 hero — phone art, Tech & Ai toast, play badge, bookmark/saved chip. */
@Composable
private fun HeroImage(saved: Boolean) {
	Box(
		modifier = Modifier
			.padding(horizontal = 15.dp)
			.fillMaxWidth()
			.height(208.dp)
			.clip(RoundedCornerShape(10.dp))
			.background(Color(0xFFC4C4C4)),
	) {
		Image(
			painter = painterResource(R.drawable.news_hero_phone),
			contentDescription = null,
			contentScale = ContentScale.Crop,
			modifier = Modifier
				.align(Alignment.Center)
				.offset(x = (-0.5).dp, y = 16.59.dp)
				.size(407.dp, 271.18.dp),
		)
		Image(
			painter = painterResource(R.drawable.ic_hero_play),
			contentDescription = null,
			modifier = Modifier
				.align(Alignment.Center)
				.offset(x = (-0.5).dp, y = 12.5.dp)
				.rotate(90f)
				.size(59.92.dp, 53.75.dp),
		)
		Box(
			modifier = Modifier
				.align(Alignment.BottomStart)
				.padding(start = 9.dp, bottom = 10.dp)
				.clip(RoundedCornerShape(7.88.dp))
				.background(Color(0x40242B3D))
				.padding(horizontal = 7.dp, vertical = 5.dp),
		) {
			Text(
				text = "Tech & Ai",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 10.sp),
				color = Color.White,
			)
		}
		if (saved) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(4.dp),
				modifier = Modifier
					.align(Alignment.TopEnd)
					.padding(top = 8.dp, end = 7.dp)
					.clip(RoundedCornerShape(7.88.dp))
					.background(Color(0x40242B3D))
					.padding(horizontal = 7.dp, vertical = 5.dp),
			) {
				Image(
					painter = painterResource(R.drawable.ic_saved_bookmark),
					contentDescription = null,
					modifier = Modifier.size(11.dp),
				)
				Text(
					text = "Saved to My STAK",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 10.sp),
					color = Color.White,
				)
			}
		} else {
			Image(
				painter = painterResource(R.drawable.ic_hero_bookmark),
				contentDescription = null,
				modifier = Modifier
					.align(Alignment.TopEnd)
					.padding(top = 8.dp, end = 11.dp)
					.size(17.79.dp, 18.27.dp),
			)
		}
	}
}

@Composable
private fun Byline() {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		modifier = Modifier.padding(vertical = 2.dp),
	) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier.size(24.dp).background(News.ChipBg, CircleShape),
		) {
			Text(
				text = "B",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 10.sp),
				color = Color(0xFF9EADC7),
			)
		}
		Row(horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.CenterVertically) {
			Text(
				text = "Bloomberg",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp),
				color = Color.White,
			)
			Text(
				text = "· Jul 2 · 3 min read",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 12.sp),
				color = News.Faint,
			)
		}
	}
}

/** 150x52 gradient CTA with the plus mark. */
@Composable
private fun AddToStakButton(onClick: () -> Unit) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
		modifier = Modifier
			.size(150.dp, 52.dp)
			.background(CtaGradient, RoundedCornerShape(6.dp))
			.border(0.36.dp, CtaBorder, RoundedCornerShape(6.dp))
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			),
	) {
		Text(
			text = "Add to STAK",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 14.sp),
			color = Color.White,
		)
		Image(
			painter = painterResource(R.drawable.ic_plus_small),
			contentDescription = null,
			modifier = Modifier.size(14.dp),
		)
	}
}

@Composable
private fun Divider() {
	Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(News.Divider))
}

/** AAPL price card — badge, Daily chip, $308.63 + sparkline; saved adds View row. */
@Composable
private fun StockCard(saved: Boolean) {
	Column(
		verticalArrangement = Arrangement.spacedBy(13.dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(16.dp))
			.background(News.CardBg)
			.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 14.dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier.size(44.dp).background(News.ChipBg, CircleShape),
			) {
				Text(
					text = "A",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
					color = Color(0xFF9EADC7),
				)
			}
			Spacer(modifier = Modifier.width(12.dp))
			Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
				Text(
					text = "Apple Inc.",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 15.sp),
					color = Color.White,
				)
				Text(
					text = "AAPL",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 12.sp),
					color = News.Muted,
				)
			}
			Spacer(modifier = Modifier.weight(1f))
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(5.dp),
				modifier = Modifier
					.clip(RoundedCornerShape(6.dp))
					.background(Color(0x403E4958))
					.padding(horizontal = 10.dp, vertical = 4.dp),
			) {
				Text(
					text = "Daily",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 8.sp, letterSpacing = 0.4.sp),
					color = Color.White,
				)
				Image(
					painter = painterResource(R.drawable.ic_daily_chevron),
					contentDescription = null,
					modifier = Modifier.size(6.53.dp, 3.56.dp),
				)
			}
		}
		Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.fillMaxWidth()) {
			Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
				Text(
					text = "$308.63",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 26.sp),
					color = Color.White,
				)
				Text(
					text = "+4.84% today",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 13.sp),
					color = News.Green,
				)
			}
			Spacer(modifier = Modifier.weight(1f))
			Image(
				painter = painterResource(R.drawable.news_sparkline),
				contentDescription = null,
				modifier = Modifier.size(110.dp, 40.dp),
			)
		}
		if (saved) {
			Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(News.Divider))
			Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(top = 1.dp)) {
				Text(
					text = "View AAPL in My STAK",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 13.sp),
					color = News.Teal,
				)
				Spacer(modifier = Modifier.weight(1f))
				Image(
					painter = painterResource(R.drawable.ic_daily_chevron),
					contentDescription = null,
					modifier = Modifier.rotate(-90f).size(6.88.dp, 3.75.dp),
				)
			}
		}
	}
}

/** "The gist" — sparkle header + three check bullets. */
@Composable
private fun GistCard() {
	Column(
		verticalArrangement = Arrangement.spacedBy(12.dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(14.dp))
			.background(News.CardBg)
			.padding(16.dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
			Image(painterResource(R.drawable.ic_gist_sparkle), null, modifier = Modifier.size(18.dp))
			Text(
				text = "The gist",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
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
	Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
		Image(painterResource(R.drawable.ic_gist_check), null, modifier = Modifier.size(16.dp))
		Text(
			text = text,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 19.sp),
			color = News.Body,
			modifier = Modifier.weight(1f),
		)
	}
}

@Composable
private fun Paragraph(text: String, size: androidx.compose.ui.unit.TextUnit = 14.sp, line: androidx.compose.ui.unit.TextUnit = 23.sp) {
	Text(
		text = text,
		style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = size, lineHeight = line),
		color = News.Body,
	)
}

@Composable
private fun PullQuote() {
	Row(
		horizontalArrangement = Arrangement.spacedBy(14.dp),
		modifier = Modifier.fillMaxWidth().padding(start = 2.dp, top = 6.dp, bottom = 6.dp).height(androidx.compose.foundation.layout.IntrinsicSize.Min),
	) {
		Box(
			modifier = Modifier
				.width(3.dp)
				.fillMaxSize()
				.background(News.Teal, RoundedCornerShape(2.dp)),
		)
		Text(
			text = "Companies do not quietly double down on a product they expect to flop.",
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 26.sp),
			color = Color(0xFFD3D3DD),
			modifier = Modifier.weight(1f),
		)
	}
}

@Composable
private fun NewToThisCard() {
	Column(
		verticalArrangement = Arrangement.spacedBy(9.dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(14.dp))
			.background(News.CardBg)
			.padding(16.dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
			Image(painterResource(R.drawable.ic_gist_help), null, modifier = Modifier.size(18.dp))
			Text(
				text = "New to this?",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 13.sp),
				color = News.Teal,
			)
		}
		Text(
			text = "A foldable phone opens out into a small tablet. For Apple it means a pricier device to sell, and a way to win back buyers who drifted to Samsung, which has offered foldables for years.",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 20.sp),
			color = News.Body,
		)
	}
}

@Composable
private fun SourceRow() {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier.width(81.dp),
	) {
		Text(
			text = "Source",
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 13.sp),
			color = Color.White,
		)
		Spacer(modifier = Modifier.weight(1f))
		Image(painterResource(R.drawable.ic_news_external), null, modifier = Modifier.size(19.dp))
	}
}

@Composable
private fun KeyStatsCard() {
	Column(
		verticalArrangement = Arrangement.spacedBy(13.dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(14.dp))
			.background(News.CardBg)
			.padding(16.dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
			Image(painterResource(R.drawable.ic_gist_info), null, modifier = Modifier.size(18.dp))
			Text(
				text = "Key stats",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
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
	Row(horizontalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.fillMaxWidth()) {
		StatCell(l1, v1, modifier = Modifier.width(212.dp))
		StatCell(l2, v2)
	}
}

@Composable
private fun StatCell(label: String, value: String, modifier: Modifier = Modifier) {
	Column(verticalArrangement = Arrangement.spacedBy(3.dp), modifier = modifier) {
		Text(
			text = label,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 11.sp),
			color = News.Faint,
		)
		Text(
			text = value,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 13.sp),
			color = Color.White,
		)
	}
}

/** rgba(105,179,202,0.1) r12 tag chip. */
@Composable
private fun ArticleTag(text: String) {
	Box(
		modifier = Modifier
			.clip(RoundedCornerShape(12.dp))
			.background(Color(0x1A69B3CA))
			.padding(horizontal = 11.dp, vertical = 5.dp),
	) {
		Text(
			text = text,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 11.sp),
			color = News.Muted,
		)
	}
}

@Composable
private fun ReadNext() {
	Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
		Text(
			text = "READ NEXT",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 10.sp, letterSpacing = 0.5.sp),
			color = News.Muted,
		)
		repeat(2) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(12.dp),
				modifier = Modifier
					.fillMaxWidth()
					.clip(RoundedCornerShape(14.dp))
					.background(News.CardBg)
					.padding(12.dp),
			) {
				Image(
					painter = painterResource(R.drawable.news_thumb_tsla_rn),
					contentDescription = null,
					contentScale = ContentScale.Crop,
					modifier = Modifier.size(60.dp).clip(RoundedCornerShape(10.dp)),
				)
				Column(verticalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.weight(1f)) {
					Text(
						text = "CNBC · 2d",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 11.sp),
						color = News.Muted,
					)
					Text(
						text = "Tesla drops 7% even after beating deliveries",
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 19.sp),
						color = Color.White,
					)
				}
			}
		}
	}
}

/** Save success — #0a1020 scrim at ~45% + the r24 #181f30 bottom sheet (101:1169). */
@Composable
private fun SaveSuccessOverlay(onViewInMyStak: () -> Unit, onDismiss: () -> Unit) {
	Box(modifier = Modifier.fillMaxSize()) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(Color(0x730A1020))
				.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null,
					onClick = onDismiss,
				),
		)
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(14.dp),
			modifier = Modifier
				.align(Alignment.BottomCenter)
				.fillMaxWidth()
				.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
				.background(News.CardBg)
				.padding(horizontal = 20.dp)
				.padding(top = 10.dp)
				.navigationBarsPadding()
				.padding(bottom = 30.dp),
		) {
			Box(
				modifier = Modifier
					.padding(bottom = 4.dp)
					.size(40.dp, 4.dp)
					.background(News.Divider, RoundedCornerShape(2.dp)),
			)
			Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(14.dp)) {
				Image(painterResource(R.drawable.ic_sheet_check), null, modifier = Modifier.size(47.dp))
				Text(
					text = "Saved to My STAK",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
					color = Color.White,
				)
			}
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(11.dp),
				modifier = Modifier
					.fillMaxWidth()
					.clip(RoundedCornerShape(6.dp))
					.background(Color(0x1A69B3CA))
					.padding(horizontal = 14.dp, vertical = 12.dp),
			) {
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier.size(38.dp).background(News.ChipBg, CircleShape),
				) {
					Text(
						text = "A",
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 15.sp),
						color = Color(0xFF9EADC7),
					)
				}
				Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.weight(1f)) {
					Text(
						text = "Apple",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 13.sp),
						color = Color.White,
					)
					Text(
						text = "$229.35 today",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 10.sp),
						color = News.Muted,
					)
				}
				Text(
					text = "▲ 1.2%",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp),
					color = News.Green,
				)
			}
			Text(
				text = "Watching from today · no money committed",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 18.sp),
				color = News.Body,
				modifier = Modifier.fillMaxWidth(),
			)
			Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier
						.fillMaxWidth()
						.height(52.dp)
						.background(CtaGradient, RoundedCornerShape(6.dp))
						.border(0.36.dp, CtaBorder, RoundedCornerShape(6.dp))
						.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
							onClick = onViewInMyStak,
						),
				) {
					Text(
						text = "View in My STAK",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 14.sp),
						color = Color.White,
					)
				}
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier
						.fillMaxWidth()
						.height(52.dp)
						.border(0.36.dp, Color(0x54343B4F), RoundedCornerShape(6.dp))
						.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
							onClick = onDismiss,
						),
				) {
					Text(
						text = "Back",
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 14.sp),
						color = News.Muted,
					)
				}
			}
		}
	}
}
