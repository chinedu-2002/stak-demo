package com.stak.demo.ui.mystak

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.R
import com.stak.demo.ui.theme.Geist
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors

private val CardBg = Color(0xFF181F30)
private val Muted = Color(0xFF819ABB)
private val Faint = Color(0xFF5C6B85)
private val Body = Color(0xFFC8D2E0)
private val Green = Color(0xFF2FD08A)
private val Red = Color(0xFFFF5A6A)
private val Teal = Color(0xFF69B3CA)
private val Ink = Color(0xFF0E162B)
private val Track = Color(0xFF2A3346)
private val HeaderGray = Color(0xFFD3D3DD)

private val CtaGradient = Brush.verticalGradient(
	0.0889f to Color(0xFFA6E4F7),
	0.3919f to Color(0xFF5DA8BF),
	0.7255f to Color(0xFF3C98B4),
	1f to Color(0xFF3C98B4),
)

/**
 * 06 · My STAK — "My STAK Overview · corrected" (CHINEDU 1:3155).
 * Collections grid with the Add more CTA, the "Your read" insight,
 * the performance summary (chart, range pills, best/worst), the
 * Breakdown allocation donut with sector bars, and the teal
 * "More like your STAK" Discover banner. Tab bar via MainShell.
 */
@Composable
fun MyStakScreen(onOpenCollection: () -> Unit, onStartSwiping: () -> Unit) {
	Column(modifier = Modifier.fillMaxSize().background(StakColors.Bg)) {
		Column(
			verticalArrangement = Arrangement.spacedBy(4.dp),
			modifier = Modifier
				.fillMaxWidth()
				.background(StakColors.Bg)
				.statusBarsPadding()
				.padding(horizontal = 20.dp)
				.padding(top = 20.dp),
		) {
			Text(
				text = "My STAK",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 26.sp),
				color = Color.White,
			)
			Text(
				text = "Your saved stocks, live.",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 13.sp),
				color = Muted,
			)
		}
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(20.dp),
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth()
				.verticalScroll(rememberScrollState())
				.padding(horizontal = 20.dp)
				.padding(top = 20.dp),
		) {
			SectionHeader("Collections")
			Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
				Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
					CollectionChip("AI & Tech", "5 stocks", imageRes = R.drawable.ms_coll_aitech, onClick = onOpenCollection, modifier = Modifier.weight(1f))
					CollectionChip("Finance", "3 stocks", imageRes = R.drawable.ms_coll_finance, modifier = Modifier.weight(1f))
				}
				Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
					CollectionChip("Green Energy", "3 stocks", iconRes = R.drawable.ic_cat_green, modifier = Modifier.weight(1f))
					CollectionChip("Real Estate", "2 stocks", iconRes = R.drawable.ic_cat_realestate, modifier = Modifier.weight(1f))
				}
				Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
					CollectionChip("Healthcare", "4 stocks", iconRes = R.drawable.ic_cat_health, modifier = Modifier.weight(1f))
					CollectionChip("Consumer", "2 stocks", iconRes = R.drawable.ic_cat_consumer, modifier = Modifier.weight(1f))
				}
			}
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
				modifier = Modifier
					.size(150.dp, 52.dp)
					.background(CtaGradient, RoundedCornerShape(6.dp))
					.border(0.36.dp, Color(0xA1659EAD), RoundedCornerShape(6.dp))
					.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
						onClick = onStartSwiping,
					),
			) {
				Text(
					text = "Add more",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 14.sp),
					color = Color.White,
				)
				Image(painterResource(R.drawable.ic_plus_small), null, modifier = Modifier.size(14.dp))
			}
			// Your read insight card.
			Column(
				verticalArrangement = Arrangement.spacedBy(7.dp),
				modifier = Modifier
					.fillMaxWidth()
					.clip(RoundedCornerShape(14.dp))
					.background(CardBg)
					.padding(16.dp),
			) {
				Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
					Image(painterResource(R.drawable.ic_gist_sparkle), null, modifier = Modifier.size(24.dp))
					Text(
						text = "Your read",
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
						color = Color.White,
					)
				}
				Text(
					text = "You lean into growth and tech.",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 15.sp),
					color = Color.White,
				)
				Text(
					text = "Six of your fourteen picks are tech or AI names. Your STAK skews high-growth, with a small hedge in real estate.",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 19.sp),
					color = Body,
				)
			}
			PortfolioSummary()
			SectionHeader("Breakdown")
			AllocationCard()
			// Discover banner.
			Column(
				verticalArrangement = Arrangement.spacedBy(9.dp),
				modifier = Modifier
					.fillMaxWidth()
					.clip(RoundedCornerShape(16.dp))
					.background(Teal)
					.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
						onClick = onStartSwiping,
					)
					.padding(18.dp),
			) {
				Text(
					text = "DISCOVER",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 10.sp, letterSpacing = 0.6.sp),
					color = Ink,
				)
				Text(
					text = "More like your STAK",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
					color = Ink,
				)
				Text(
					text = "Based on your taste, 8 fresh picks are waiting in the deck.",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 17.sp),
					color = Ink,
				)
				Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(top = 4.dp)) {
					Text(
						text = "Start swiping",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 13.sp),
						color = Color(0xB80A1020),
					)
					Text(
						text = "›",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 14.sp),
						color = Ink,
					)
				}
			}
			Spacer(modifier = Modifier.height(0.dp))
		}
	}
}

@Composable
private fun SectionHeader(title: String) {
	Text(
		text = title,
		style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
		color = HeaderGray,
		modifier = Modifier.fillMaxWidth(),
	)
}

/** One collection chip — art/icon, name + count, chevron (#181f30 r12). */
@Composable
private fun CollectionChip(
	name: String,
	count: String,
	modifier: Modifier = Modifier,
	imageRes: Int? = null,
	iconRes: Int? = null,
	onClick: () -> Unit = {},
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(10.dp),
		modifier = modifier
			.clip(RoundedCornerShape(12.dp))
			.background(CardBg)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			)
			.padding(12.dp),
	) {
		if (imageRes != null) {
			Image(
				painter = painterResource(imageRes),
				contentDescription = null,
				contentScale = ContentScale.Crop,
				modifier = Modifier.size(34.dp),
			)
		} else if (iconRes != null) {
			Image(painterResource(iconRes), null, modifier = Modifier.size(36.dp))
		}
		Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.weight(1f)) {
			Text(
				text = name,
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 13.sp),
				color = Color.White,
				maxLines = 1,
			)
			Text(
				text = count,
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 11.sp),
				color = Muted,
			)
		}
		Text(
			text = "›",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 16.sp),
			color = Faint,
		)
	}
}

/** Performance this week — +4.9%, chart, range pills, best/worst. */
@Composable
private fun PortfolioSummary() {
	Column(
		verticalArrangement = Arrangement.spacedBy(14.dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(8.dp))
			.background(CardBg)
			.padding(vertical = 18.dp),
	) {
		Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(start = 20.dp)) {
			Text(
				text = "Performance this week",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 12.sp),
				color = Muted,
			)
			Text(
				text = "+4.9%",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 44.sp, letterSpacing = (-0.44).sp),
				color = Color.White,
			)
			Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
				Text(
					text = "Across 14 stocks",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 14.sp),
					color = Green,
				)
				Text(
					text = ".",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = 14.sp),
					color = Muted,
				)
				Text(
					text = "3M",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = 14.sp),
					color = Muted,
				)
			}
		}
		Image(
			painter = painterResource(R.drawable.ms_chart_line),
			contentDescription = null,
			contentScale = ContentScale.Fit,
			modifier = Modifier.align(Alignment.CenterHorizontally).size(345.dp, 76.dp),
		)
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(37.dp),
			modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 26.dp),
		) {
			listOf("1D", "1W", "1M", "3M", "YTD", "1Y").forEach { label ->
				if (label == "3M") {
					Box(
						contentAlignment = Alignment.Center,
						modifier = Modifier
							.size(39.dp, 22.5.dp)
							.clip(RoundedCornerShape(11.25.dp))
							.background(Color(0x292C9DBC))
							.border(0.75.dp, Color(0x662C9DBC), RoundedCornerShape(11.25.dp)),
					) {
						Text(label, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp), color = Teal)
					}
				} else {
					Text(label, style = TextStyle(fontFamily = Geist, fontSize = 12.sp), color = Muted)
				}
			}
		}
		Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Track))
		Row(horizontalArrangement = Arrangement.spacedBy(151.dp, Alignment.CenterHorizontally), modifier = Modifier.fillMaxWidth()) {
			Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
				Text("Best this week", style = TextStyle(fontFamily = Geist, fontSize = 11.sp), color = Faint)
				Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
					Text("TSLA", style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 13.sp), color = Color.White)
					Text("+3.4%", style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp), color = Green)
				}
			}
			Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
				Text("Worst", style = TextStyle(fontFamily = Geist, fontSize = 11.sp), color = Faint)
				Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
					Text("SNOW", style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 13.sp), color = Color.White)
					Text("-0.5%", style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp), color = Red)
				}
			}
		}
	}
}

/** Allocation — the 150dp donut render + sector bars. */
@Composable
private fun AllocationCard() {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(16.dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(16.dp))
			.background(CardBg)
			.padding(18.dp),
	) {
		Text(
			text = "Allocation",
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 15.sp),
			color = Color.White,
		)
		Image(painterResource(R.drawable.ms_donut), null, modifier = Modifier.size(150.dp))
		Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
			SectorBar("Tech & AI", "42% · 6 stocks", Teal, 132.dp)
			SectorBar("Finance", "21% · 3 stocks", Color(0xFF7AB3F0), 66.dp)
			SectorBar("Green Energy", "20% · 3 stocks", Green, 63.dp)
			SectorBar("Real Estate", "12% · 2 stocks", Color(0xFF9E8CE5), 38.dp)
			SectorBar("Other", "5% · 1 stock", Faint, 16.dp)
		}
	}
}

@Composable
private fun SectorBar(name: String, share: String, color: Color, fill: Dp) {
	Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
		Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
			Box(modifier = Modifier.size(9.dp).background(color, CircleShape))
			Spacer(modifier = Modifier.width(8.dp))
			Text(name, style = TextStyle(fontFamily = Geist, fontSize = 13.sp), color = Color.White)
			Spacer(modifier = Modifier.weight(1f))
			Text(share, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp), color = Muted)
		}
		Box(modifier = Modifier.fillMaxWidth().height(7.dp).clip(RoundedCornerShape(4.dp)).background(Track)) {
			Box(modifier = Modifier.width(fill).height(7.dp).background(color, RoundedCornerShape(4.dp)))
		}
	}
}
