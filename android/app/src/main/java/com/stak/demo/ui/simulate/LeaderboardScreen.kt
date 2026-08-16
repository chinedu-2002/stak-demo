package com.stak.demo.ui.simulate

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

private data class Rank(
	val rank: String, val initial: String, val name: String,
	val picks: String, val pct: String, val you: Boolean = false,
)

private val TOP = listOf(
	Rank("1", "M", "Maya A.", "9 picks", "+9.4%"),
	Rank("2", "J", "Jide O.", "14 picks", "+8.8%"),
	Rank("3", "T", "Tunde K.", "7 picks", "+8.1%"),
	Rank("4", "Z", "Zara M.", "22 picks", "+7.6%"),
	Rank("5", "K", "Kofi B.", "11 picks", "+7.0%"),
)
private val NEAR = listOf(
	Rank("46", "L", "Lena S.", "8 picks", "+4.3%"),
	Rank("48", "D", "Dami F.", "15 picks", "+4.1%"),
)

/**
 * 07 · Simulate — "Final · Leaderboard · % return" (CHINEDU 1:4124).
 * This-week/All-time chips, RANKED BY RETURN, the highlighted You row
 * (#47, ▲ 12 spots) above the top five and the neighbors around you.
 */
@Composable
fun LeaderboardScreen(onBack: () -> Unit) {
	Column(modifier = Modifier.fillMaxSize().background(StakColors.Bg)) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
				.background(StakColors.Bg)
				.statusBarsPadding()
				.padding(horizontal = 18.dp, vertical = 8.dp),
		) {
			AuthBackCircle(onClick = onBack)
			Spacer(modifier = Modifier.weight(1f))
			Text(
				text = "Leaderboard",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
				color = Color.White,
			)
			Spacer(modifier = Modifier.weight(1f))
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier.size(40.dp).background(Sim.CardBg, CircleShape),
			) {
				Image(painterResource(R.drawable.ic_news_share), null, modifier = Modifier.size(17.dp))
			}
		}
		Column(
			verticalArrangement = Arrangement.spacedBy(10.dp),
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth()
				.verticalScroll(rememberScrollState())
				.padding(horizontal = 20.dp)
				.padding(top = 10.dp, bottom = 24.dp),
		) {
			Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
				PeriodChip("This week", selected = true)
				PeriodChip("All time", selected = false)
			}
			Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
				Text(
					text = "RANKED BY RETURN",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 10.sp, letterSpacing = 0.9.sp),
					color = Sim.Faint,
				)
				Spacer(modifier = Modifier.weight(1f))
				Text("Trailing 7 days", style = TextStyle(fontFamily = Geist, fontSize = 10.sp), color = Sim.Faint)
			}
			// You — highlighted with the ▲ spots delta.
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(10.dp),
				modifier = Modifier
					.fillMaxWidth()
					.clip(RoundedCornerShape(12.dp))
					.background(Sim.TealTint)
					.border(0.75.dp, Color(0x662C9DBC), RoundedCornerShape(12.dp))
					.padding(horizontal = 12.dp, vertical = 10.dp),
			) {
				Text("47", style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 12.sp), color = Sim.Teal)
				Box(contentAlignment = Alignment.Center, modifier = Modifier.size(32.dp).background(Sim.ChipBg, CircleShape)) {
					Text("E", style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 13.sp), color = Sim.BadgeInk)
				}
				Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.weight(1f)) {
					Text("You", style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.SemiBold, fontSize = 13.sp), color = Color.White)
					Text("12 picks this week", style = TextStyle(fontFamily = Geist, fontSize = 10.sp), color = Sim.Muted)
				}
				Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(2.dp)) {
					Text("+4.2%", style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 13.sp), color = Sim.Teal)
					Text("▲ 12 spots", style = TextStyle(fontFamily = Geist, fontSize = 10.sp), color = Sim.Green)
				}
			}
			TOP.forEach { LeaderRow(it) }
			Text(
				text = "· · ·",
				style = TextStyle(fontFamily = Geist, fontSize = 12.sp),
				color = Sim.Faint,
				modifier = Modifier.align(Alignment.CenterHorizontally),
			)
			NEAR.forEach { LeaderRow(it) }
			Text(
				text = "Percentage return, not dollar size, so everyone competes on the same scale. This week ranks are trailing 7 days.",
				style = TextStyle(fontFamily = Geist, fontSize = 10.sp, lineHeight = 15.sp),
				color = Sim.Faint,
				modifier = Modifier.padding(top = 8.dp),
			)
		}
	}
}

@Composable
private fun PeriodChip(label: String, selected: Boolean) {
	Box(
		modifier = Modifier
			.clip(RoundedCornerShape(14.dp))
			.background(if (selected) Sim.TealTint else Sim.CardBg)
			.then(if (selected) Modifier.border(0.75.dp, Color(0x662C9DBC), RoundedCornerShape(14.dp)) else Modifier)
			.padding(horizontal = 12.dp, vertical = 6.dp),
	) {
		Text(
			text = label,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 12.sp),
			color = if (selected) Sim.Teal else Sim.Muted,
		)
	}
}

@Composable
private fun LeaderRow(r: Rank) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(10.dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(12.dp))
			.background(Sim.CardBg)
			.padding(horizontal = 12.dp, vertical = 10.dp),
	) {
		Text(r.rank, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 12.sp), color = Sim.Faint)
		Box(contentAlignment = Alignment.Center, modifier = Modifier.size(32.dp).background(Sim.ChipBg, CircleShape)) {
			Text(r.initial, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 13.sp), color = Sim.BadgeInk)
		}
		Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.weight(1f)) {
			Text(r.name, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = 13.sp), color = Color.White)
			Text(r.picks, style = TextStyle(fontFamily = Geist, fontSize = 10.sp), color = Sim.Muted)
		}
		Text(r.pct, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = 12.sp), color = Sim.HeaderGray)
	}
}
