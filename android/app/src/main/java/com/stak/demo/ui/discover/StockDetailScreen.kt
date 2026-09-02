package com.stak.demo.ui.discover

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
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
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stak.demo.R
import com.stak.demo.ui.components.MainTab
import com.stak.demo.ui.components.MainTabBar
import com.stak.demo.ui.onboarding.AuthBackCircle
import com.stak.demo.ui.theme.Geist
import com.stak.demo.ui.theme.Sora
import com.stak.demo.ui.theme.StakColors
import com.stak.demo.ui.theme.ADVANCE_ROUNDING

private val Card = Color(0xFF181F30)
private val Bright = Color(0xFFF2F6FC)
private val Muted = Color(0xFF819ABB)
private val Green = Color(0xFF2FD08A)
private val Teal = Color(0xFF69B3CA)

/**
 * Discover · Stock Detail (CHINEDU 1:2382 folded, 92:969 save success)
 * — reached from the deck's Learn more, serving the TAPPED stock's
 * facts (AAPL carries the authored values verbatim): price hero, the
 * performance chart with range pills, Risk fit, Numbers that matter,
 * Analyst view / Compare and learn (collapsed), News signal, TIP and
 * the Save / Practice buy CTAs. Saving raises the Saved-to-My-STAK
 * sheet and flips the CTA area to the saved state.
 */
@Composable
fun StockDetailScreen(
	onBack: () -> Unit,
	// The stock the page serves - deck taps route their card here (user,
	// 2026-09-01: the NVIDIA card must open NVIDIA, not AAPL).
	symbol: String = "AAPL",
	fromMyStak: Boolean = false,
	// B5 (1:2382 Motion): the Discover entry's Practice buy leaves the
	// detail for the Simulate tab; null keeps the in-page ticket.
	onPracticeBuy: (() -> Unit)? = null,
	// B7/B13: the success sheets' "View in My STAK" pop (92:969/71:949).
	onViewInMyStak: (() -> Unit)? = null,
	// B8 (92:969 Motion): "Keep exploring" dissolves back to the deck.
	onKeepExploring: (() -> Unit)? = null,
	// B9 (1:2579): the Discover-entry OPEN state composes the shell tab
	// bar; each tab pops the detail Instant and lands on that tab.
	onTab: ((MainTab) -> Unit)? = null,
) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	val f = DETAIL_FACTS[symbol] ?: DETAIL_FACTS.getValue("AAPL")
	var saved by rememberSaveable { mutableStateOf(fromMyStak) }
	var showSuccess by rememberSaveable { mutableStateOf(false) }
	var showBuy by rememberSaveable { mutableStateOf(false) }
	// B9/B13: hoisted Analyst state - the open state carries the tab bar
	// (Discover entry) and the buy-success "Done" folds the section.
	var analystOpen by rememberSaveable { mutableStateOf(false) }

	Box(modifier = Modifier.fillMaxSize().background(StakColors.Bg)) {
		Column(modifier = Modifier.fillMaxSize()) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.fillMaxWidth()
					.background(StakColors.Bg)
					.statusBarsPadding()
					.padding(horizontal = (18 * u).dp, vertical = (8 * u).dp),
			) {
				AuthBackCircle(onClick = onBack)
				Spacer(modifier = Modifier.weight(1f))
				Text(
					text = f.symbol,
					style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (16 * u).sp),
					color = Color.White,
				)
				Spacer(modifier = Modifier.weight(1f))
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier.size((40 * u).dp).background(Card, CircleShape),
				) {
					Image(painterResource(R.drawable.ic_news_share), null, modifier = Modifier.size((17 * u).dp))
				}
			}
			Column(modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())) {
				Column(
					verticalArrangement = Arrangement.spacedBy((4 * u).dp),
					modifier = Modifier.fillMaxWidth().padding(horizontal = (20 * u).dp).padding(top = (10 * u).dp, bottom = (6 * u).dp),
				) {
					Text(f.title, style = TextStyle(fontFamily = Geist, fontSize = (11 * u).sp), color = Muted)
					Text(
						f.price,
						style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (26 * u).sp),
						color = Bright,
					)
					Text(
						f.change,
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp),
						color = Green,
					)
				}
				Image(
					painter = painterResource(R.drawable.sd_chart_line),
					contentDescription = null,
					contentScale = ContentScale.Fit,
					modifier = Modifier.align(Alignment.CenterHorizontally).size((345 * u).dp, (76 * u).dp),
				)
				Spacer(modifier = Modifier.height((40 * u).dp))
				Row(
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.spacedBy((37 * u).dp),
					modifier = Modifier.align(Alignment.CenterHorizontally),
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
								Text(
									label,
									style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp),
									color = Teal,
								)
							}
						} else {
							Text(label, style = TextStyle(fontFamily = Geist, fontSize = (12 * u).sp), color = Muted)
						}
					}
				}
				Column(
					verticalArrangement = Arrangement.spacedBy((14 * u).dp),
					modifier = Modifier.fillMaxWidth().padding(horizontal = (20 * u).dp, vertical = (12 * u).dp),
				) {
					if (fromMyStak) {
						SinceYouSavedCard()
					}
					RiskFitCard(f)
					NumbersCard(f)
					AnalystCard(f, open = analystOpen, onToggle = { analystOpen = !analystOpen })
					NewsSignalCard(f)
					CompareCard(f)
					Row(
						horizontalArrangement = Arrangement.spacedBy((8 * u).dp),
						modifier = Modifier
							.fillMaxWidth()
							.clip(RoundedCornerShape((12 * u).dp))
							.background(Card)
							.padding(horizontal = (12 * u).dp, vertical = (10 * u).dp),
					) {
						Text(
							"TIP",
							style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp),
							color = Color(0xFF5BD7E4),
						)
						Text(
							f.tip,
							style = TextStyle(fontFamily = Geist, fontSize = (11 * u).sp),
							color = Muted,
							modifier = Modifier.width((260 * u).dp),
						)
					}
				}
				Column(
					verticalArrangement = Arrangement.spacedBy((10 * u).dp),
					modifier = Modifier.fillMaxWidth().padding(horizontal = (20 * u).dp).padding(top = (4 * u).dp, bottom = (16 * u).dp),
				) {
					if (fromMyStak) {
						DetailCta("Practice buy") { showBuy = true }
						DetailSecondary("Unsave") { onBack() }
					} else if (saved) {
						Box(
							contentAlignment = Alignment.Center,
							modifier = Modifier
								.fillMaxWidth()
								.height((52 * u).dp)
								.border((0.36 * u).dp, com.stak.demo.ui.theme.StakColors.CtaBorderBrush, RoundedCornerShape((6 * u).dp)),
						) {
							Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((6 * u).dp)) {
								Image(painterResource(R.drawable.ic_saved_bookmark), null, modifier = Modifier.size((12 * u).dp))
								Text(
									"Saved to My STAK",
									style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (14 * u).sp),
									color = Color.White,
								)
							}
						}
						DetailSecondary("Practice buy") { if (onPracticeBuy != null) onPracticeBuy() else showBuy = true }
					} else {
						DetailCta("Save") { showSuccess = true }
						DetailSecondary("Practice buy") { if (onPracticeBuy != null) onPracticeBuy() else showBuy = true }
					}
				}
			}
			if (!fromMyStak && analystOpen && onTab != null) {
				// B9: ONLY the Discover-entry OPEN frame (1:2579) authors the
				// 86 shell bar pinned at the bottom - SWAP taps pop the detail.
				MainTabBar(selected = MainTab.Discover, onSelect = onTab)
			}
		}
		// B6: the save-success sheet enters like the News one - scale
		// 0.92 -> 1 + fade - at the authored 350 ease-out (92:969).
		AnimatedVisibility(
			visible = showSuccess,
			enter = scaleIn(initialScale = 0.92f, animationSpec = tween(350, easing = EaseOut)) +
				fadeIn(tween(350, easing = EaseOut)),
			// The scrim dismiss is unauthored - it stays instant.
			exit = ExitTransition.None,
		) {
			DetailSavedSheet(
				f = f,
				onDone = { showSuccess = false; saved = true },
				// B7/B8: both CTAs mark the stock saved, then leave the page
				// (forward push to My STAK / dissolve back to the deck).
				onViewInMyStak = {
					saved = true
					com.stak.demo.ui.MyStakHoldings.add(f.symbol)
					if (onViewInMyStak != null) onViewInMyStak() else { showSuccess = false }
				},
				onKeepExploring = {
					saved = true
					com.stak.demo.ui.MyStakHoldings.add(f.symbol)
					if (onKeepExploring != null) onKeepExploring() else { showSuccess = false }
				},
			)
		}
		// B13 (1:3423 Motion): the buy sheet's Back and the success "Done"
		// both dismiss with a 300 dissolve over the detail.
		AnimatedVisibility(
			visible = showBuy,
			enter = EnterTransition.None,
			exit = fadeOut(tween(300, easing = EaseOut)),
		) {
			DetailBuyHost(
				spec = f.buySpec,
				onClose = { showBuy = false },
				onViewInMyStak = { if (onViewInMyStak != null) onViewInMyStak() else { showBuy = false } },
				// B13: "Done" also folds the Analyst section - the authored
				// destination is the FOLDED detail (16:1012).
				onDone = { analystOpen = false; showBuy = false },
			)
		}
	}
}

@Composable
private fun RiskFitCard(f: DetailFacts) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((12 * u).dp),
		modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape((16 * u).dp)).background(Card)
			.padding(horizontal = (16 * u).dp, vertical = (14 * u).dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
			Text(
				"Risk fit",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp),
				color = Bright,
			)
			Spacer(modifier = Modifier.weight(1f))
			Box(
				modifier = Modifier
					.clip(RoundedCornerShape((999 * u).dp))
					.background(Color(0x1F5DA8BF))
					.padding(horizontal = (10 * u).dp, vertical = (4 * u).dp),
			) {
				Text(
					"Matches you",
					style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp),
					color = Color(0xFFA6E4F7),
				)
			}
		}
		// Authored (1:2382): a lone 14x8 pill indicator - the frame draws no track.
		Box(modifier = Modifier.fillMaxWidth().height((8 * u).dp)) {
			Box(modifier = Modifier.offset(x = (f.riskPillX * u).dp).size((14 * u).dp, (8 * u).dp).background(Color(0xFFA6E4F7), RoundedCornerShape((4 * u).dp)))
		}
		Row(modifier = Modifier.fillMaxWidth()) {
			Text("Low", style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp), color = Muted)
			Spacer(modifier = Modifier.weight(1f))
			Text("High", style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp), color = Muted)
		}
		Text(
			f.riskCopy,
			style = TextStyle(fontFamily = Geist, fontSize = (11 * u).sp),
			color = Muted,
		)
	}
}

@Composable
private fun NumbersCard(f: DetailFacts) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((12 * u).dp),
		modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape((16 * u).dp)).background(Card)
			.padding(horizontal = (16 * u).dp, vertical = (14 * u).dp),
	) {
		Text(
			"Numbers that matter",
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp),
			color = Bright,
		)
		Row(horizontalArrangement = Arrangement.spacedBy((8 * u).dp), modifier = Modifier.fillMaxWidth()) {
			f.stats.forEach { st ->
				StatCell(st.label, st.value, st.verdict, if (st.good) Green else Muted, Modifier.weight(1f), border = st.border)
			}
		}
		Text(
			"Tap a stat for sector and peer benchmarks",
			style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp),
			color = Muted,
		)
	}
}

@Composable
private fun StatCell(label: String, value: String, verdict: String, verdictColor: Color, modifier: Modifier, border: Boolean = false) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((4 * u).dp),
		modifier = modifier
			.clip(RoundedCornerShape((12 * u).dp))
			.background(Card)
			.then(if (border) Modifier.border((1 * u).dp, Color(0xFF212D4B), RoundedCornerShape((12 * u).dp)) else Modifier)
			.padding((10 * u).dp),
	) {
		Text(label, style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp), color = Muted)
		Text(value, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (17 * u).sp), color = Bright)
		Text(verdict, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp), color = verdictColor)
	}
}

@Composable
private fun CollapsedCard(title: String, sub: String, subColor: Color) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((12 * u).dp),
		modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape((16 * u).dp)).background(Card)
			.padding(horizontal = (16 * u).dp, vertical = (14 * u).dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
			Text(
				title,
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp),
				color = Bright,
			)
			Spacer(modifier = Modifier.weight(1f))
			Image(painterResource(R.drawable.ic_sd_caret), null, modifier = Modifier.size((20 * u).dp))
		}
		Text(
			sub,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp),
			color = subColor,
		)
	}
}

@Composable
private fun NewsSignalCard(f: DetailFacts) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((12 * u).dp),
		modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape((16 * u).dp)).background(Card)
			.padding(horizontal = (16 * u).dp, vertical = (14 * u).dp),
	) {
		Text(
			"News signal",
			style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp),
			color = Bright,
		)
		Text(
			f.newsClose,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp),
			color = Green,
		)
		Text(
			f.newsSignal,
			style = TextStyle(fontFamily = Geist, fontSize = (11 * u).sp),
			color = Muted,
		)
		Text(f.newsEarnings, style = TextStyle(fontFamily = Geist, fontSize = (11 * u).sp), color = Muted)
		Row(
			horizontalArrangement = Arrangement.spacedBy((12 * u).dp),
			modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
		) {
			f.newsSources.forEach { (src, tag) ->
				Column(
					verticalArrangement = Arrangement.spacedBy((8 * u).dp),
					modifier = Modifier
						.width((205 * u).dp)
						.clip(RoundedCornerShape((12 * u).dp))
						.background(Card)
						.padding((12 * u).dp),
				) {
					Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
						Text(
							src,
							style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp),
							color = Muted,
						)
						Spacer(modifier = Modifier.weight(1f))
						Box(
							modifier = Modifier
								.clip(RoundedCornerShape((999 * u).dp))
								.background(Color(0x14FFFFFF))
								.padding(horizontal = (8 * u).dp, vertical = (3 * u).dp),
						) {
							Text(
								tag,
								style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp),
								color = Muted,
							)
						}
					}
					Text(
						f.newsHeadline,
						style = TextStyle(fontFamily = Geist, fontSize = (12 * u).sp),
						color = Bright,
						modifier = Modifier.width((173 * u).dp),
					)
				}
			}
		}
	}
}

@Composable
private fun DetailCta(text: String, onClick: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.fillMaxWidth()
			.height((52 * u).dp)
			.background(
				androidx.compose.ui.graphics.Brush.verticalGradient(
					0.0889f to Color(0xFFA6E4F7),
					0.3919f to Color(0xFF5DA8BF),
					0.7255f to Color(0xFF3C98B4),
					1f to Color(0xFF3C98B4),
				),
				RoundedCornerShape((6 * u).dp),
			)
			.border((0.36 * u).dp, com.stak.demo.ui.theme.StakColors.CtaBorderBrush, RoundedCornerShape((6 * u).dp))
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			),
	) {
		Text(text, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (14 * u).sp), color = Color.White)
	}
}

@Composable
private fun DetailSecondary(text: String, onClick: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.fillMaxWidth()
			.height((52 * u).dp)
			.border((0.36 * u).dp, Color(0x54343B4F), RoundedCornerShape((6 * u).dp))
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			),
	) {
		Text(text, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.Normal, fontSize = (13 * u).sp), color = Muted)
	}
}

/** Saved-to-My-STAK sheet over the detail (92:969) — Apple row variant. */
@Composable
private fun DetailSavedSheet(f: DetailFacts, onDone: () -> Unit, onViewInMyStak: () -> Unit = onDone, onKeepExploring: () -> Unit = onDone) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Box(modifier = Modifier.fillMaxSize()) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				// Authored scrim rgba(12,19,32,0.55) (106:1037).
				.background(Color(0x8C0C1320))
				.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null,
					onClick = onDone,
				),
		)
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy((14 * u).dp),
			modifier = Modifier
				.align(Alignment.BottomCenter)
				.fillMaxWidth()
				.clip(RoundedCornerShape(topStart = (24 * u).dp, topEnd = (24 * u).dp))
				.background(Card)
				.padding(horizontal = (20 * u).dp)
				.padding(top = (10 * u).dp)
				.padding(bottom = (30 * u).dp),
		) {
			Box(
				modifier = Modifier
					.padding(bottom = (4 * u).dp)
					.size((40 * u).dp, (4 * u).dp)
					.background(Color(0xFF2A3346), RoundedCornerShape((2 * u).dp)),
			)
			Image(painterResource(R.drawable.ic_sheet_check), null, modifier = Modifier.size((47 * u).dp))
			Text(
				"Saved to My STAK",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (18 * u).sp),
				color = Color.White,
			)
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy((11 * u).dp),
				modifier = Modifier
					.fillMaxWidth()
					.clip(RoundedCornerShape((6 * u).dp))
					.background(Color(0x1A69B3CA))
					.padding(horizontal = (14 * u).dp, vertical = (12 * u).dp),
			) {
				Box(contentAlignment = Alignment.Center, modifier = Modifier.size((38 * u).dp).background(Color(0xFF242B3D), CircleShape)) {
					Text(f.sheetBadge, style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp), color = Color(0xFF9EADC7))
				}
				Column(verticalArrangement = Arrangement.spacedBy((2 * u).dp), modifier = Modifier.weight(1f)) {
					Text(f.sheetName, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (13 * u).sp), color = Color.White)
					Text(f.sheetPrice, style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp), color = Muted)
				}
				Text(f.sheetChange, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp), color = Green)
			}
			Text(
				"Watching from today · no money committed",
				style = TextStyle(fontFamily = Geist, fontSize = (12 * u).sp, lineHeight = (18 * u).sp),
				color = Color(0xFFC8D2E0),
				modifier = Modifier.fillMaxWidth(),
			)
			Column(verticalArrangement = Arrangement.spacedBy((16 * u).dp), modifier = Modifier.fillMaxWidth()) {
				DetailCta("View in My STAK", onClick = onViewInMyStak)
				DetailSecondary("Keep exploring", onClick = onKeepExploring)
			}
		}
	}
}

/** The practice-buy ticket reused from the deck (public host wrapper). */
@Composable
private fun DetailBuyHost(spec: BuySpec, onClose: () -> Unit, onViewInMyStak: () -> Unit, onDone: () -> Unit) {
	DiscoverBuyFlow(
		onClose = onClose,
		spec = spec,
		filledSecondary = "Done",
		// 1:3460: the My STAK ticket's secondary is authored "Back".
		ticketSecondary = "Back",
		// B13 (71:949/71:994 Motion): the success CTAs leave the sheet.
		onFilledPrimary = onViewInMyStak,
		onFilledSecondary = onDone,
	)
}

/** Kicker label — Geist Medium 10, tracking 0.8, muted. */
@Composable
private fun Kicker(text: String) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Text(
		text,
		style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, letterSpacing = (0.8 * u + ADVANCE_ROUNDING.value).sp),
		color = Muted,
	)
}

/** Analyst view (collapsed 1:2454 / open 1:2651) — caret toggles; state hoisted for B9/B13. */
@Composable
private fun AnalystCard(f: DetailFacts, open: Boolean, onToggle: () -> Unit) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((12 * u).dp),
		modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape((16 * u).dp)).background(Card)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
			) { onToggle() }
			.padding(horizontal = (16 * u).dp, vertical = (14 * u).dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
			Text(
				"Analyst view",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp),
				color = Bright,
			)
			Spacer(modifier = Modifier.weight(1f))
			if (!open) {
				Image(painterResource(R.drawable.ic_sd_caret), null, modifier = Modifier.size((20 * u).dp))
			}
		}
		if (!open) {
			Text(
				f.upside,
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp),
				color = Green,
			)
		} else {
			Kicker("PRICE TARGET RANGE")
			Box(modifier = Modifier.fillMaxWidth().height((8 * u).dp)) {
				Box(modifier = Modifier.width((180 * u).dp).height((8 * u).dp).background(Color(0x8C5DA8BF), RoundedCornerShape((4 * u).dp)))
				Box(modifier = Modifier.offset(x = (f.targetMarkerX * u).dp).size((13 * u).dp, (8 * u).dp).background(Color(0xFFA6E4F7), RoundedCornerShape((4 * u).dp)))
			}
			Row(modifier = Modifier.fillMaxWidth()) {
				Column(verticalArrangement = Arrangement.spacedBy((1 * u).dp)) {
					Text("Low", style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp), color = Muted)
					Text(f.targetLow, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp), color = Bright)
				}
				Spacer(modifier = Modifier.weight(1f))
				Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy((1 * u).dp)) {
					Text("Avg", style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp), color = Muted)
					Text(f.targetAvg, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp), color = Bright)
				}
				Spacer(modifier = Modifier.weight(1f))
				Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy((1 * u).dp)) {
					Text("High", style = TextStyle(fontFamily = Geist, fontSize = (10 * u).sp), color = Muted)
					Text(f.targetHigh, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp), color = Bright)
				}
			}
			Text(
				f.upside,
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp),
				color = Green,
			)
			Kicker(f.consensus)
			Box(modifier = Modifier.fillMaxWidth().height((8 * u).dp).clip(RoundedCornerShape((4 * u).dp)).background(Color(0xFF10182B))) {
				Box(modifier = Modifier.width((f.buyBarW * u).dp).height((8 * u).dp).background(Green, RoundedCornerShape((4 * u).dp)))
			}
			Row(modifier = Modifier.fillMaxWidth()) {
				Text(f.buyCount, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp), color = Green)
				Spacer(modifier = Modifier.weight(1f))
				Text(f.holdCount, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp), color = Muted)
				Spacer(modifier = Modifier.weight(1f))
				Text(f.sellCount, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp), color = Muted)
			}
			Kicker("RECENT ACTIONS")
			f.actions.forEach { (name, action, target) ->
				Row(
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.fillMaxWidth()
						.height((38 * u).dp)
						.clip(RoundedCornerShape((10 * u).dp))
						.background(Color(0xFF10182B))
						.padding(horizontal = (12 * u).dp),
				) {
					Text(name, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp), color = Bright)
					Spacer(modifier = Modifier.weight(1f))
					Text(
						action,
						style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp),
						color = if (action == "Buy") Green else Muted,
					)
					Spacer(modifier = Modifier.width((10 * u).dp))
					Text(target, style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp), color = Bright)
				}
			}
		}
	}
}

/** Compare and learn (collapsed 1:2526 / open 1:2719) — peer table. */
@Composable
private fun CompareCard(f: DetailFacts) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	var open by rememberSaveable { mutableStateOf(false) }
	Column(
		verticalArrangement = Arrangement.spacedBy(if (open) (21 * u).dp else (12 * u).dp),
		modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape((16 * u).dp)).background(Card)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
			) { open = !open }
			.padding(horizontal = (16 * u).dp, vertical = (14 * u).dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
			Text(
				"Compare and learn",
				style = TextStyle(fontFamily = Sora, fontWeight = FontWeight.SemiBold, fontSize = (15 * u).sp),
				color = Bright,
			)
			Spacer(modifier = Modifier.weight(1f))
			if (!open) {
				Image(painterResource(R.drawable.ic_sd_caret), null, modifier = Modifier.size((20 * u).dp))
			}
		}
		if (!open) {
			Text(
				f.peersLabel,
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp),
				color = Muted,
			)
		} else {
			Box(modifier = Modifier.fillMaxWidth()) {
				// AAPL column tint spans the table rows (frame 1:2721).
				Box(
					modifier = Modifier
						.offset(x = (78 * u).dp, y = 0.dp)
						.size((81 * u).dp, (170 * u).dp)
						.background(Color(0x125DA8BF), RoundedCornerShape((8 * u).dp)),
				)
				Column(verticalArrangement = Arrangement.spacedBy((12 * u).dp), modifier = Modifier.fillMaxWidth()) {
					CompareRow("", f.symbol, f.peerA, f.peerB, header = true)
					f.compareRows.forEach { r ->
						CompareRow(r.label, r.a, r.b, r.c, valueColor = if (r.green) Green else null)
					}
				}
			}
			Text(
				"Cultural context only, not financial advice.",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp),
				color = Muted,
			)
		}
	}
}

@Composable
private fun CompareRow(label: String, a: String, m: String, g: String, header: Boolean = false, valueColor: Color? = null) {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Row(horizontalArrangement = Arrangement.spacedBy((8 * u).dp), modifier = Modifier.fillMaxWidth().height((20 * u).dp)) {
		Text(
			label,
			style = TextStyle(fontFamily = Geist, fontSize = (11 * u).sp),
			color = Muted,
			modifier = Modifier.weight(1f),
		)
		Text(
			a,
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (11 * u).sp),
			color = valueColor ?: Bright,
			textAlign = androidx.compose.ui.text.style.TextAlign.Center,
			modifier = Modifier.weight(1f),
		)
		Text(
			m,
			style = TextStyle(fontFamily = Geist, fontWeight = if (header) FontWeight.Medium else FontWeight.Normal, fontSize = (11 * u).sp),
			color = valueColor ?: Bright,
			textAlign = androidx.compose.ui.text.style.TextAlign.Center,
			modifier = Modifier.weight(1f),
		)
		Text(
			g,
			style = TextStyle(fontFamily = Geist, fontWeight = if (header) FontWeight.Medium else FontWeight.Normal, fontSize = (11 * u).sp),
			color = valueColor ?: Bright,
			textAlign = androidx.compose.ui.text.style.TextAlign.Center,
			modifier = Modifier.weight(1f),
		)
	}
}

/** "SINCE YOU SAVED +4.6%" banner (16:1012) for the My STAK entry. */
@Composable
private fun SinceYouSavedCard() {
	val u = com.stak.demo.ui.onboarding.figmaUnit()
	Column(
		verticalArrangement = Arrangement.spacedBy((12 * u).dp),
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape((16 * u).dp))
			.background(Card)
			.padding((16 * u).dp),
	) {
		Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((8 * u).dp)) {
			Image(painterResource(R.drawable.ic_saved_bookmark), null, modifier = Modifier.size((12 * u).dp))
			Text(
				"SINCE YOU SAVED",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (10 * u).sp, letterSpacing = (0.8 * u + ADVANCE_ROUNDING.value).sp),
				color = Muted,
			)
			Text(
				"+4.6%",
				style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Medium, fontSize = (12 * u).sp),
				color = Green,
			)
		}
		Text(
			"Saved 5 weeks ago. AAPL is up 4.6% since, moving roughly with the market. Steady giants tend to.",
			style = TextStyle(fontFamily = Geist, fontWeight = FontWeight.Normal, fontSize = (11 * u).sp, lineHeight = (14 * u).sp),
			color = Muted,
		)
	}
}


/** One stat tile in "Numbers that matter". */
private data class DetailStat(val label: String, val value: String, val verdict: String, val good: Boolean = false, val border: Boolean = false)

/** One "Compare and learn" table row (a = this stock). */
private data class DetailCompare(val label: String, val a: String, val b: String, val c: String, val green: Boolean = false)

/**
 * Everything the detail page serves per stock - backend-shaped like the
 * news feed’s StockFacts. AAPL carries the authored 1:2382/92:969 frame
 * values VERBATIM; NVDA and GOOGL extend their deck cards, priced off
 * the same DECK numbers so every surface agrees.
 */
private data class DetailFacts(
	val symbol: String,
	val title: String,
	val price: String,
	val change: String,
	val tip: String,
	val riskPillX: Float,
	val riskCopy: String,
	val stats: List<DetailStat>,
	val upside: String,
	val targetLow: String,
	val targetAvg: String,
	val targetHigh: String,
	val targetMarkerX: Float,
	val consensus: String,
	val buyCount: String,
	val holdCount: String,
	val sellCount: String,
	val buyBarW: Float,
	val actions: List<Triple<String, String, String>>,
	val newsClose: String,
	val newsSignal: String,
	val newsEarnings: String,
	val newsSources: List<Pair<String, String>>,
	val newsHeadline: String,
	val peersLabel: String,
	val peerA: String,
	val peerB: String,
	val compareRows: List<DetailCompare>,
	val sheetBadge: String,
	val sheetName: String,
	val sheetPrice: String,
	val sheetChange: String,
	val buySpec: BuySpec,
)

private val DETAIL_FACTS = mapOf(
	"AAPL" to DetailFacts(
		symbol = "AAPL",
		title = "AAPL · Apple Inc",
		price = "$229.35",
		change = "▲ 1.2% today",
		tip = "Steady giants move slower. Stable stocks often do.",
		riskPillX = 88f,
		riskCopy = "Low volatility. Fits the steady side of your profile.",
		stats = listOf(
			DetailStat("P/E ratio", "31.2", "In line"),
			DetailStat("Revenue growth", "6.1%", "Slower"),
			DetailStat("Profit margin", "24.3%", "Excellent", good = true, border = true),
		),
		upside = "↑ 6.7% upside",
		targetLow = "$180", targetAvg = "$248", targetHigh = "$300", targetMarkerX = 167f,
		consensus = "WALL ST. CONSENSUS · 42 ANALYSTS",
		buyCount = "● Buy 28", holdCount = "Hold 12", sellCount = "Sell 2", buyBarW = 212f,
		actions = listOf(
			Triple("Morgan Stanley", "Buy", "$260"),
			Triple("Wedbush", "Buy", "$285"),
			Triple("Goldman Sachs", "Buy", "$256"),
			Triple("UBS", "Hold", "$236"),
			Triple("Barclays", "Hold", "$230"),
		),
		newsClose = "▲ +0.8% at yesterday’s close",
		newsSignal = "Foldable iPhone reports point to a premium fall lineup.",
		newsEarnings = "Q3 earnings land July 30.",
		newsSources = listOf("Yahoo · 13h ago" to "Neutral", "CNN · 1h ago" to "Neutral"),
		newsHeadline = "The rally leaves Apple about 4 percent shy of the market-cap crown",
		peersLabel = "vs MSFT · GOOGL",
		peerA = "MSFT", peerB = "GOOGL",
		compareRows = listOf(
			DetailCompare("P/E ratio", "31.2", "36x", "24x"),
			DetailCompare("Rev growth", "+6.1%", "+15%", "+12%", green = true),
			DetailCompare("Profit margin", "24.3%", "36%", "29%"),
			DetailCompare("Market cap", "$3.5T", "$3.4T", "$2.3T"),
		),
		sheetBadge = "A", sheetName = "Apple", sheetPrice = "$229.35 today", sheetChange = "▲ 1.2%",
		buySpec = AAPL_BUY,
	),
	"NVDA" to DetailFacts(
		symbol = "NVDA",
		title = "NVDA · NVIDIA Corp",
		price = "$122.10",
		change = "▲ 2.4% today",
		tip = "Chip stocks swing hard. Small stakes, long views.",
		riskPillX = 238f,
		riskCopy = "High volatility. Fits the bolder side of your profile.",
		stats = listOf(
			DetailStat("P/E ratio", "52.8", "Rich"),
			DetailStat("Revenue growth", "62%", "Explosive", good = true, border = true),
			DetailStat("Profit margin", "48.9%", "Strong"),
		),
		upside = "↑ 17.9% upside",
		targetLow = "$100", targetAvg = "$144", targetHigh = "$200", targetMarkerX = 33f,
		consensus = "WALL ST. CONSENSUS · 63 ANALYSTS",
		buyCount = "● Buy 55", holdCount = "Hold 7", sellCount = "Sell 1", buyBarW = 273f,
		actions = listOf(
			Triple("Morgan Stanley", "Buy", "$152"),
			Triple("BofA", "Buy", "$150"),
			Triple("Goldman Sachs", "Buy", "$145"),
			Triple("Citi", "Buy", "$150"),
			Triple("HSBC", "Hold", "$120"),
		),
		newsClose = "▲ +2.1% at yesterday’s close",
		newsSignal = "Blackwell demand keeps outrunning supply into the fall.",
		newsEarnings = "Q2 earnings land Aug 27.",
		newsSources = listOf("Reuters · 2h ago" to "Bullish", "CNBC · 9h ago" to "Neutral"),
		newsHeadline = "Nvidia lags the chip rally it kicked off as orders pile up",
		peersLabel = "vs AMD · TSM",
		peerA = "AMD", peerB = "TSM",
		compareRows = listOf(
			DetailCompare("P/E ratio", "52.8", "110x", "28x"),
			DetailCompare("Rev growth", "+62%", "+18%", "+33%", green = true),
			DetailCompare("Profit margin", "48.9%", "6.4%", "39%"),
			DetailCompare("Market cap", "$3.0T", "$0.2T", "$1.0T"),
		),
		sheetBadge = "N", sheetName = "NVIDIA", sheetPrice = "$122.10 today", sheetChange = "▲ 2.4%",
		buySpec = NVDA_BUY,
	),
	"GOOGL" to DetailFacts(
		symbol = "GOOGL",
		title = "GOOGL · Alphabet Inc",
		price = "$178.90",
		change = "▲ 0.8% today",
		tip = "Ad money moves with the economy, so some quarters just drift.",
		riskPillX = 150f,
		riskCopy = "Moderate volatility. Sits mid-range for your profile.",
		stats = listOf(
			DetailStat("P/E ratio", "24.1", "Cheaper", good = true, border = true),
			DetailStat("Revenue growth", "12%", "Healthy"),
			DetailStat("Profit margin", "29.5%", "Strong"),
		),
		upside = "↑ 12.4% upside",
		targetLow = "$150", targetAvg = "$201", targetHigh = "$240", targetMarkerX = 52f,
		consensus = "WALL ST. CONSENSUS · 48 ANALYSTS",
		buyCount = "● Buy 40", holdCount = "Hold 8", sellCount = "Sell 0", buyBarW = 261f,
		actions = listOf(
			Triple("Morgan Stanley", "Buy", "$210"),
			Triple("JPMorgan", "Buy", "$208"),
			Triple("Goldman Sachs", "Buy", "$205"),
			Triple("Bernstein", "Hold", "$185"),
			Triple("Wells Fargo", "Hold", "$182"),
		),
		newsClose = "▲ +0.6% at yesterday’s close",
		newsSignal = "A blowout ad quarter pushed the stock to fresh highs.",
		newsEarnings = "Q2 earnings land Jul 22.",
		newsSources = listOf("Bloomberg · 5h ago" to "Bullish", "Yahoo · 1d ago" to "Neutral"),
		newsHeadline = "Alphabet jumps after a blowout ad quarter as cloud accelerates",
		peersLabel = "vs MSFT · META",
		peerA = "MSFT", peerB = "META",
		compareRows = listOf(
			DetailCompare("P/E ratio", "24.1", "36x", "27x"),
			DetailCompare("Rev growth", "+12%", "+15%", "+19%", green = true),
			DetailCompare("Profit margin", "29.5%", "36%", "34%"),
			DetailCompare("Market cap", "$2.3T", "$3.4T", "$1.5T"),
		),
		sheetBadge = "G", sheetName = "Alphabet", sheetPrice = "$178.90 today", sheetChange = "▲ 0.8%",
		buySpec = GOOGL_BUY,
	),
)
