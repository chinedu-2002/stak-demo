package com.stak.demo.ui.mystak

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.R
import com.stak.demo.ui.MyStakHoldings
import com.stak.demo.ui.theme.Geist
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors
import com.stak.demo.ui.theme.ADVANCE_ROUNDING
import kotlin.math.roundToInt

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
fun MyStakScreen(onOpenCollection: (String) -> Unit, onStartSwiping: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(modifier = Modifier.fillMaxSize().background(StakColors.Bg)) {
		Column(
			verticalArrangement = Arrangement.spacedBy((4 * u).dp),
			modifier = Modifier
				.fillMaxWidth()
				.background(StakColors.Bg)
				.statusBarsPadding()
				.padding(horizontal = (20 * u).dp)
				.padding(top = (20 * u).dp),
		) {
			Text(
				text = "My STAK",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (26 * u).sp, lineHeight = (33 * u).sp),
				color = Color.White,
			)
			Text(
				text = "Your saved stocks, live.",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (13 * u).sp, lineHeight = (17 * u).sp),
				color = Muted,
			)
		}
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy((20 * u).dp),
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth()
				.verticalScroll(rememberScrollState())
				.padding(horizontal = (20 * u).dp)
				.padding(top = (20 * u).dp),
		) {
			SectionHeader("Collections")
			Column(verticalArrangement = Arrangement.spacedBy((10 * u).dp), modifier = Modifier.fillMaxWidth()) {
				// B10 (1:3180 Motion): the sample card's edge is the TEMPLATE -
				// every collection opens the authored Collection page, Instant.
				// Codex parity audit (2026-09-04): each chip carries ITS
				// catalogue id (Collections.kt) so the page serves that
				// collection, the way the deck's Learn more serves its stock.
				COLLECTIONS.chunked(2).forEach { pair ->
					Row(horizontalArrangement = Arrangement.spacedBy((10 * u).dp), modifier = Modifier.fillMaxWidth()) {
						pair.forEach { c ->
							// Codex audit (2026-09-04): the count is the HELD count from
							// the holdings store, not the authored countLabel - Unsave
							// drops it here too.
							CollectionChip(c.name, heldCountLabel(c.held().size), imageRes = c.imageRes, iconRes = c.iconRes, onClick = { onOpenCollection(c.id) }, modifier = Modifier.weight(1f))
						}
					}
				}
			}
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy((8 * u).dp, Alignment.CenterHorizontally),
				modifier = Modifier
					.size((150 * u).dp, (52 * u).dp)
					.background(CtaGradient, RoundedCornerShape((6 * u).dp))
					.border((0.36 * u).dp, StakColors.CtaBorderBrush, RoundedCornerShape((6 * u).dp))
					.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
						onClick = onStartSwiping,
					),
			) {
				Text(
					text = "Add more",
					// Codex parity audit (2026-09-04): 1:3155 sets the CTA at 14.
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (14 * u).sp, lineHeight = (21 * u).sp),
					color = Color.White,
				)
				Image(painterResource(R.drawable.ic_plus_small), null, modifier = Modifier.size((14 * u).dp))
			}
			// Your read insight card.
			Column(
				verticalArrangement = Arrangement.spacedBy((7 * u).dp),
				modifier = Modifier
					.fillMaxWidth()
					.clip(RoundedCornerShape((14 * u).dp))
					.background(CardBg)
					.padding((16 * u).dp),
			) {
				Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((8 * u).dp)) {
					Image(painterResource(R.drawable.ic_gist_sparkle), null, modifier = Modifier.size((24 * u).dp))
					Text(
						text = "Your read",
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (14 * u).sp, lineHeight = (18 * u).sp),
						color = Color.White,
					)
				}
				Text(
					text = "You lean into growth and tech.",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp, lineHeight = (19 * u).sp),
					color = Color.White,
				)
				// Codex audit (2026-09-04): the authored "six of fourteen" was a
				// third count that agreed with nothing; the sentence now reads
				// the store - Tech & AI held of all held, as words.
				val techAi = numberWord(collection("aitech").held().size).replaceFirstChar { it.uppercase() }
				Text(
					text = "$techAi of your ${numberWord(MyStakHoldings.count)} picks are tech or AI names. Your STAK skews high-growth, with a small hedge in real estate.",
					// Codex parity audit (2026-09-04): 1:3155 sets the body at 13.
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (13 * u).sp, lineHeight = (19 * u).sp),
					color = Body,
				)
			}
			PortfolioSummary()
			SectionHeader("Breakdown")
			AllocationCard()
			// Discover banner.
			Column(
				verticalArrangement = Arrangement.spacedBy((9 * u).dp),
				modifier = Modifier
					.fillMaxWidth()
					.clip(RoundedCornerShape((16 * u).dp))
					.background(Teal)
					.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
						onClick = onStartSwiping,
					)
					.padding((18 * u).dp),
			) {
				Text(
					text = "DISCOVER",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, lineHeight = (13 * u).sp, letterSpacing = (0.6 * u + ADVANCE_ROUNDING.value).sp),
					color = Ink,
				)
				Text(
					text = "More like your STAK",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (18 * u).sp, lineHeight = (23 * u).sp),
					color = Ink,
				)
				Text(
					text = "Based on your taste, 8 fresh picks are waiting in the deck.",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (13 * u).sp, lineHeight = (17 * u).sp),
					color = Ink,
				)
				Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((4 * u).dp)) {
					Text(
						text = "Start swiping",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (13 * u).sp, lineHeight = (22 * u).sp),
						color = Color(0xB80A1020),
					)
					Text(
						text = "›",
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (14 * u).sp, lineHeight = (22 * u).sp),
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
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Text(
		text = title,
		style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (16 * u).sp, lineHeight = (20 * u).sp),
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
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy((10 * u).dp),
		modifier = modifier
			.clip(RoundedCornerShape((12 * u).dp))
			.background(CardBg)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			)
			.padding((12 * u).dp),
	) {
		if (imageRes != null) {
			Image(
				painter = painterResource(imageRes),
				contentDescription = null,
				contentScale = ContentScale.Crop,
				modifier = Modifier.size((34 * u).dp),
			)
		} else if (iconRes != null) {
			Image(painterResource(iconRes), null, modifier = Modifier.size((36 * u).dp))
		}
		Column(verticalArrangement = Arrangement.spacedBy((2 * u).dp), modifier = Modifier.weight(1f)) {
			Text(
				text = name,
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (13 * u).sp, lineHeight = (16 * u).sp),
				color = Color.White,
				maxLines = 1,
				softWrap = false,
				// Authored: "Green Energy" (box 90) overflows its 84 column —
				// the frame draws it past the column, so don't clip it.
				overflow = TextOverflow.Visible,
			)
			Text(
				text = count,
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (11 * u).sp, lineHeight = (14 * u).sp),
				color = Muted,
			)
		}
		Text(
			text = "›",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (16 * u).sp),
			color = Faint,
		)
	}
}

/** Performance this week — +4.9%, chart, range pills, best/worst. */
@Composable
private fun PortfolioSummary() {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((14 * u).dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((8 * u).dp))
			.background(CardBg)
			.padding(vertical = (13.5 * u).dp),
	) {
		Column(verticalArrangement = Arrangement.spacedBy((8 * u).dp), modifier = Modifier.padding(start = (20 * u).dp)) {
			Text(
				text = "Performance this week",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp, lineHeight = (15 * u).sp),
				color = Muted,
			)
			Text(
				text = "+4.9%",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (44 * u).sp, lineHeight = (55 * u).sp, letterSpacing = (-0.44 * u).sp),
				color = Color.White,
			)
			Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((10 * u).dp)) {
				Text(
					// Codex audit (2026-09-04): the authored "14" was a third
					// total; the holdings store is the one count.
					text = "Across ${MyStakHoldings.count} stocks",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (14 * u).sp, lineHeight = (18 * u).sp),
					color = Green,
				)
				Text(
					text = ".",
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = (14 * u).sp, lineHeight = (18 * u).sp),
					color = Muted,
				)
				Text(
					text = "3M",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (14 * u).sp, lineHeight = (18 * u).sp),
					color = Muted,
				)
			}
		}
		Image(
			painter = painterResource(R.drawable.ms_chart_line),
			contentDescription = null,
			contentScale = ContentScale.Fit,
			modifier = Modifier.align(Alignment.CenterHorizontally).size((343 * u).dp, (73.56 * u).dp),
		)
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy((37 * u).dp),
			modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = (26 * u).dp),
		) {
			listOf("1D", "1W", "1M", "3M", "YTD", "1Y").forEach { label ->
				if (label == "3M") {
					Box(
						contentAlignment = Alignment.Center,
						modifier = Modifier
							.size((39 * u).dp, (22.5 * u).dp)
							.clip(RoundedCornerShape((11.25 * u).dp))
							.background(Color(0x292C9DBC))
							.border((0.75 * u).dp, Color(0x662C9DBC), RoundedCornerShape((11.25 * u).dp)),
					) {
						Text(label, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp), color = Teal)
					}
				} else {
					Text(label, style = TextStyle(fontFamily = Geist, fontSize = (12 * u).sp), color = Muted)
				}
			}
		}
		Box(modifier = Modifier.fillMaxWidth().height((1 * u).dp).background(Track))
		Row(horizontalArrangement = Arrangement.spacedBy((151 * u).dp, Alignment.CenterHorizontally), modifier = Modifier.fillMaxWidth()) {
			Column(verticalArrangement = Arrangement.spacedBy((3 * u).dp)) {
				Text("Best this week", style = TextStyle(fontFamily = Geist, fontSize = (11 * u).sp, lineHeight = (14 * u).sp), color = Faint)
				Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((6 * u).dp)) {
					Text("TSLA", style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (13 * u).sp, lineHeight = (16 * u).sp), color = Color.White)
					Text("+3.4%", style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp, lineHeight = (16 * u).sp), color = Green)
				}
			}
			Column(verticalArrangement = Arrangement.spacedBy((3 * u).dp)) {
				Text("Worst", style = TextStyle(fontFamily = Geist, fontSize = (11 * u).sp, lineHeight = (14 * u).sp), color = Faint)
				Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((6 * u).dp)) {
					Text("SNOW", style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (13 * u).sp, lineHeight = (16 * u).sp), color = Color.White)
					Text("-0.5%", style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp, lineHeight = (16 * u).sp), color = Red)
				}
			}
		}
	}
}

/**
 * Allocation — the 150 ring + sector bars. Codex audit (2026-09-04): the
 * authored 1:3155 buckets ("Tech & AI 42% · 6 stocks" ... "Other 5% · 1
 * stock") were yet another set of numbers the holdings store could not
 * follow, so they derive from it now - see allocationBuckets().
 */
@Composable
private fun AllocationCard() {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	val buckets = allocationBuckets()
	val pcts = bucketPercents(buckets)
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy((16 * u).dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((16 * u).dp))
			.background(CardBg)
			.padding((18 * u).dp),
	) {
		Text(
			text = "Allocation",
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp, lineHeight = (19 * u).sp),
			color = Color.White,
		)
		AllocationRing(buckets, pcts)
		Column(verticalArrangement = Arrangement.spacedBy((12 * u).dp), modifier = Modifier.fillMaxWidth()) {
			buckets.forEachIndexed { i, b ->
				// The authored Tech & AI fill, 132 of the 314 track, IS its
				// 42% - so fill = track * pct for every bucket.
				SectorBar(b.name, "${pcts[i]}% · ${heldCountLabel(b.count)}", b.color, (314 * u).dp * pcts[i] / 100)
			}
		}
	}
}

/**
 * Codex audit (2026-09-04): 0...30 as words for the "Your read" sentence
 * (zero, one ... twenty, twenty-one ... thirty); anything beyond stays
 * digits. At rest: "Five of your twenty-one picks".
 * Mirrors ios/StakDemo/MyStak/MyStakView.swift.
 */
private fun numberWord(n: Int): String {
	val small = listOf(
		"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
		"eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen", "twenty",
	)
	return when {
		n < 0 || n > 30 -> n.toString()
		n <= 20 -> small[n]
		n == 30 -> "thirty"
		else -> "twenty-" + small[n - 20]
	}
}

/** One Breakdown bucket - the authored name and swatch, and the held count behind it. */
private data class Bucket(val name: String, val color: Color, val count: Int)

/**
 * Codex audit (2026-09-04): Tech & AI / Finance / Green Energy / Real
 * Estate are those collections' HELD counts; Other = Healthcare + Consumer
 * held + any held ticker no collection lists (TSLA, SNOW). Buckets with no
 * stocks are skipped. Mirrors ios/StakDemo/MyStak/MyStakView.swift.
 */
private fun allocationBuckets(): List<Bucket> {
	val listed = COLLECTIONS.flatMap { c -> c.stocks.map { it.ticker } }.toSet()
	val unlisted = MyStakHoldings.tickers.count { it !in listed }
	fun heldIn(id: String) = collection(id).held().size
	return listOf(
		Bucket("Tech & AI", Teal, heldIn("aitech")),
		Bucket("Finance", Color(0xFF7AB3F0), heldIn("finance")),
		Bucket("Green Energy", Green, heldIn("green")),
		Bucket("Real Estate", Color(0xFF9E8CE5), heldIn("realestate")),
		Bucket("Other", Faint, heldIn("health") + heldIn("consumer") + unlisted),
	).filter { it.count > 0 }
}

/**
 * round(count / total * 100) per bucket; the LAST non-zero bucket absorbs
 * the rounding remainder so the shares sum to 100 and the ring closes.
 */
private fun bucketPercents(buckets: List<Bucket>): List<Int> {
	val total = buckets.sumOf { it.count }
	if (total == 0) return emptyList()
	val pcts = buckets.map { (it.count * 100f / total).roundToInt() }
	val last = pcts.indexOfLast { it > 0 }
	return pcts.mapIndexed { i, p -> if (i == last) p + 100 - pcts.sum() else p }
}

/**
 * The 150 allocation ring (1:3155). Codex audit (2026-09-04): the baked
 * ms_donut render is gone - a PNG could not follow the store once Unsave
 * drops a stock. Drawn in the same 150 box: stroke 22, butt caps, segments
 * in bucket order from -90 degrees (12 o'clock) clockwise with 2-degree
 * gaps between them; a single bucket draws one full ring.
 * Mirrors ios/StakDemo/MyStak/MyStakView.swift.
 */
@Composable
private fun AllocationRing(buckets: List<Bucket>, pcts: List<Int>) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Canvas(modifier = Modifier.size((150 * u).dp)) {
		val stroke = Stroke(width = (22 * u).dp.toPx(), cap = StrokeCap.Butt)
		val inset = stroke.width / 2
		val topLeft = Offset(inset, inset)
		val arc = Size(size.width - stroke.width, size.height - stroke.width)
		if (buckets.size == 1) {
			drawArc(buckets[0].color, -90f, 360f, useCenter = false, topLeft = topLeft, size = arc, style = stroke)
			return@Canvas
		}
		var start = -90f
		buckets.forEachIndexed { i, b ->
			val slot = 360f * pcts[i] / 100f
			drawArc(b.color, start, (slot - 2f).coerceAtLeast(0f), useCenter = false, topLeft = topLeft, size = arc, style = stroke)
			start += slot
		}
	}
}

@Composable
private fun SectorBar(name: String, share: String, color: Color, fill: Dp) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(verticalArrangement = Arrangement.spacedBy((6 * u).dp), modifier = Modifier.fillMaxWidth()) {
		Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
			Box(modifier = Modifier.size((9 * u).dp).background(color, CircleShape))
			Spacer(modifier = Modifier.width((8 * u).dp))
			Text(name, style = TextStyle(fontFamily = Geist, fontSize = (13 * u).sp, lineHeight = (17 * u).sp), color = Color.White)
			Spacer(modifier = Modifier.weight(1f))
			Text(share, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp, lineHeight = (16 * u).sp), color = Muted)
		}
		Box(modifier = Modifier.fillMaxWidth().height((7 * u).dp).clip(RoundedCornerShape((4 * u).dp)).background(Track)) {
			Box(modifier = Modifier.width(fill).height((7 * u).dp).background(color, RoundedCornerShape((4 * u).dp)))
		}
	}
}
