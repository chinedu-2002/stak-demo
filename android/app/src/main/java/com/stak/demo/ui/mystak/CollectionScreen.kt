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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
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

private val CardBg = Color(0xFF181F30)
private val Muted = Color(0xFF819ABB)
private val Faint = Color(0xFF5C6B85)
private val Green = Color(0xFF2FD08A)
private val RedDown = Color(0xFFE5484D)
private val BadgeInk = Color(0xFF9EADC7)

private data class CollStock(
	val badge: String, val change: String, val up: Boolean,
	val ticker: String, val company: String, val price: String,
)

private val STOCKS = listOf(
	CollStock("N", "▲ 2.4%", true, "NVDA", "NVIDIA", "$122.10"),
	CollStock("A", "▲ 1.2%", true, "AAPL", "Apple", "$229.35"),
	CollStock("M", "▼ 0.4%", false, "MSFT", "Microsoft", "$438.20"),
	CollStock("G", "▲ 0.8%", true, "GOOGL", "Alphabet", "$178.90"),
	CollStock("A", "▲ 2.1%", true, "AMD", "Adv Micro", "$164.30"),
)

/**
 * 06 · My STAK — "Collection · Cards A · corrected" (CHINEDU 1:3333).
 * The AI & Tech collection: hero (glass art, title, meta, blurb) and
 * the stock-tile grid with the dashed Add-stock card. Tapping AAPL
 * opens the saved Stock Detail.
 */
@Composable
fun CollectionScreen(onBack: () -> Unit, onOpenStock: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(modifier = Modifier.fillMaxSize().background(StakColors.Bg)) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
				.background(StakColors.Bg)
				.statusBarsPadding()
				.padding(start = (16 * u).dp, end = (18 * u).dp, top = (8 * u).dp, bottom = (8 * u).dp),
		) {
			AuthBackCircle(onClick = onBack)
			Spacer(modifier = Modifier.weight(1f))
			Text(
				text = "AI & Tech",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (16 * u).sp),
				color = Color.White,
			)
			Spacer(modifier = Modifier.weight(1f))
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier.size((40 * u).dp).background(CardBg, CircleShape),
			) {
				Image(painterResource(R.drawable.ic_more_dots), null, modifier = Modifier.size((24 * u).dp))
			}
		}
		Column(
			verticalArrangement = Arrangement.spacedBy((20 * u).dp),
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth()
				.verticalScroll(rememberScrollState())
				.padding(horizontal = (20 * u).dp)
				.padding(top = (16 * u).dp, bottom = (26 * u).dp),
		) {
			Column(verticalArrangement = Arrangement.spacedBy((10 * u).dp)) {
				Image(
					painter = painterResource(R.drawable.ms_coll_aitech),
					contentDescription = null,
					contentScale = ContentScale.Crop,
					modifier = Modifier.size((60 * u).dp),
				)
				Text(
					text = "AI & Tech",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (26 * u).sp),
					color = Color.White,
				)
				Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((7 * u).dp)) {
					Text("5 stocks", style = TextStyle(fontFamily = Geist, fontSize = (13 * u).sp), color = Muted)
					Text("·", style = TextStyle(fontFamily = Geist, fontSize = (13 * u).sp), color = Faint)
					Text(
						"+2.4% this week",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (13 * u).sp),
						color = Green,
					)
				}
				Text(
					text = "Your highest-conviction growth and AI names.",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (13 * u).sp),
					color = Color(0xFFC8D2E0),
				)
			}
			Column(verticalArrangement = Arrangement.spacedBy((10 * u).dp), modifier = Modifier.fillMaxWidth()) {
				STOCKS.chunked(2).forEachIndexed { rowIndex, row ->
					Row(horizontalArrangement = Arrangement.spacedBy((10 * u).dp), modifier = Modifier.fillMaxWidth().height(androidx.compose.foundation.layout.IntrinsicSize.Min)) {
						row.forEach { stock ->
							StockTile(
								stock = stock,
								onClick = { if (stock.ticker == "AAPL") onOpenStock() },
								modifier = Modifier.weight(1f).fillMaxSize(),
							)
						}
						if (row.size == 1) {
							AddStockTile(modifier = Modifier.weight(1f).fillMaxSize())
						}
					}
				}
			}
		}
	}
}

@Composable
private fun StockTile(stock: CollStock, onClick: () -> Unit, modifier: Modifier = Modifier) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((10 * u).dp),
		modifier = modifier
			.clip(RoundedCornerShape((16 * u).dp))
			.background(CardBg)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			)
			.padding((14 * u).dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
			Box(contentAlignment = Alignment.Center, modifier = Modifier.size((36 * u).dp).background(Color(0xFF242B3D), CircleShape)) {
				Text(
					stock.badge,
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (14 * u).sp),
					color = BadgeInk,
				)
			}
			Spacer(modifier = Modifier.weight(1f))
			Text(
				stock.change,
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp),
				color = if (stock.up) Green else RedDown,
			)
		}
		Column(verticalArrangement = Arrangement.spacedBy((2 * u).dp)) {
			Text(
				stock.ticker,
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (16 * u).sp),
				color = Color.White,
			)
			Text(stock.company, style = TextStyle(fontFamily = Geist, fontSize = (11 * u).sp), color = Muted)
		}
		Text(
			stock.price,
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Medium, fontSize = (15 * u).sp),
			color = Color.White,
		)
	}
}

/** Dashed 1.5dp #2a3346 r16 add card. */
@Composable
private fun AddStockTile(modifier: Modifier = Modifier) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy((8 * u).dp, Alignment.CenterVertically),
		modifier = modifier
			.drawBehind {
				drawRoundRect(
					color = Color(0xFF2A3346),
					cornerRadius = CornerRadius((16 * u).dp.toPx()),
					style = Stroke(
						width = (1.5 * u).dp.toPx(),
						pathEffect = PathEffect.dashPathEffect(
							floatArrayOf((6 * u).dp.toPx(), (5 * u).dp.toPx()),
						),
					),
				)
			}
			.padding((14 * u).dp),
	) {
		Image(painterResource(R.drawable.ic_plus_circle), null, modifier = Modifier.size((24 * u).dp))
		Text(
			"Add stock",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (13 * u).sp),
			color = Muted,
		)
	}
}
