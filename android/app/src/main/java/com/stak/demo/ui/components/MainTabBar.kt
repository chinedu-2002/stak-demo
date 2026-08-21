package com.stak.demo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.R
import com.stak.demo.ui.theme.Inter

/** The five CHINEDU tabs. Home and News are built; the rest land later. */
enum class MainTab(val label: String, val activeIcon: Int, val inactiveIcon: Int, val built: Boolean) {
	Home("Home", R.drawable.ic_tab_home, R.drawable.ic_tab_home_inactive, true),
	News("News", R.drawable.ic_tab_news_active, R.drawable.ic_tab_news, true),
	Discover("Discover", R.drawable.tab_discover_active, R.drawable.ic_tab_discover, true),
	MySTAK("My STAK", R.drawable.ic_tab_mystak_active, R.drawable.ic_tab_mystak, true),
	Simulate("Simulate", R.drawable.tab_simulate_active, R.drawable.ic_tab_simulate, true),
}

/**
 * CHINEDU tab bar (Home Main 1:1220 / News listing 1:1352) — 86px
 * #060c1d, five 24px icons with Inter 12 white labels, gap 28. Active
 * tabs use their filled/bright glyph; switching is the prototype's
 * "Swap overlay · Instant".
 */
@Composable
fun MainTabBar(selected: MainTab, onSelect: (MainTab) -> Unit, compact: Boolean = false) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	// The authored bar (86, compact 75) INCLUDES the home-indicator zone -
	// the tab row sits at its authored top inset and the system gesture
	// area overlays the bar's lower band, exactly like the frame.
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.background(Color(0xFF060C1D))
			.height(((if (compact) 75 else 86) * u).dp),
	) {
		Row(
			// Authored gaps: 28 on the 86 bar (1:1221), 30 on the compact 75 bar (1:1788).
			horizontalArrangement = Arrangement.spacedBy(((if (compact) 30 else 28) * u).dp),
			verticalAlignment = Alignment.Top,
			modifier = Modifier.align(Alignment.TopCenter).padding(top = ((if (compact) 13 else 18) * u).dp),
		) {
			MainTab.entries.forEach { tab ->
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.spacedBy((10 * u).dp),
					modifier = Modifier.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
					) { if (tab.built) onSelect(tab) },
				) {
					Image(
						painter = painterResource(if (tab == selected) tab.activeIcon else tab.inactiveIcon),
						contentDescription = tab.label,
						modifier = Modifier.size((24 * u).dp),
					)
					Text(
						text = tab.label,
						style = TextStyle(fontFamily = Inter, fontWeight = FontWeight.Normal, fontSize = (12 * u).sp),
						color = Color.White,
					)
				}
			}
		}
	}
}
