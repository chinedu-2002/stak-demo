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
import androidx.compose.foundation.layout.navigationBarsPadding
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
	MySTAK("My STAK", R.drawable.ic_tab_mystak, R.drawable.ic_tab_mystak, false),
	Simulate("Simulate", R.drawable.ic_tab_simulate, R.drawable.ic_tab_simulate, false),
}

/**
 * CHINEDU tab bar (Home Main 1:1220 / News listing 1:1352) — 86px
 * #060c1d, five 24px icons with Inter 12 white labels, gap 28. Active
 * tabs use their filled/bright glyph; switching is the prototype's
 * "Swap overlay · Instant".
 */
@Composable
fun MainTabBar(selected: MainTab, onSelect: (MainTab) -> Unit) {
	Box(modifier = Modifier.fillMaxWidth().background(Color(0xFF060C1D)).navigationBarsPadding()) {
		Row(
			horizontalArrangement = Arrangement.spacedBy(28.dp),
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.align(Alignment.Center).height(86.dp),
		) {
			MainTab.entries.forEach { tab ->
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.spacedBy(10.dp),
					modifier = Modifier.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
					) { if (tab.built) onSelect(tab) },
				) {
					Image(
						painter = painterResource(if (tab == selected) tab.activeIcon else tab.inactiveIcon),
						contentDescription = tab.label,
						modifier = Modifier.size(24.dp),
					)
					Text(
						text = tab.label,
						style = TextStyle(fontFamily = Inter, fontWeight = FontWeight.Normal, fontSize = 12.sp),
						color = Color.White,
					)
				}
			}
		}
	}
}
