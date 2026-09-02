import SwiftUI

/// Discover · Stock Detail (CHINEDU 1:2382 folded / 1:2579 open, save
/// success 92:969; My STAK entry 16:1012) — serves the TAPPED stock’s
/// DetailFacts (AAPL carries the authored values verbatim): price hero,
/// chart with range pills, Risk fit, Numbers that matter, expandable
/// Analyst view / Compare and learn, News signal, TIP and the CTAs.
/// Every authored metric is multiplied by `figmaUnit` (390px artboard).
/// Ported from android/ ui/discover/StockDetailScreen.kt.
private let card = Color(argb: 0xFF181F30)
private let bright = Color(argb: 0xFFF2F6FC)
private let muted = Color(argb: 0xFF819ABB)
private let green = Color(argb: 0xFF2FD08A)
private let teal = Color(argb: 0xFF69B3CA)
private let wellBg = Color(argb: 0xFF10182B)

struct StockDetailView: View {
	let onBack: () -> Void
	var fromMyStak: Bool = false
	/// The stock the page serves - deck taps route their card here (user,
	/// 2026-09-01: the NVIDIA card must open NVIDIA, not AAPL).
	var symbol: String = "AAPL"
	/// Authored exits raised to the shell (nil keeps the local fallback):
	/// success "View in My STAK" (92:969 / 71:949, the forward push), "Keep
	/// exploring" (deck dissolve 300), the Discover entry's "Practice buy"
	/// (1:2382, to the Simulate tab, Instant) and the open state's tab-bar
	/// SWAPs (1:2579).
	var onViewInMyStak: (() -> Void)? = nil
	var onKeepExploring: (() -> Void)? = nil
	var onPracticeBuyToSimulate: (() -> Void)? = nil
	var onTab: ((MainTab) -> Void)? = nil

	@State private var saved: Bool
	@State private var showSuccess = false
	@State private var showBuy = false
	/// Hoisted from AnalystCard - drives the 1:2579 tab bar and the fold
	/// back to 16:1012 when the buy receipt's Done fires.
	@State private var analystOpen = false

	init(
		onBack: @escaping () -> Void,
		fromMyStak: Bool = false,
		symbol: String = "AAPL",
		onViewInMyStak: (() -> Void)? = nil,
		onKeepExploring: (() -> Void)? = nil,
		onPracticeBuyToSimulate: (() -> Void)? = nil,
		onTab: ((MainTab) -> Void)? = nil
	) {
		self.onBack = onBack
		self.fromMyStak = fromMyStak
		self.symbol = symbol
		self.onViewInMyStak = onViewInMyStak
		self.onKeepExploring = onKeepExploring
		self.onPracticeBuyToSimulate = onPracticeBuyToSimulate
		self.onTab = onTab
		self._saved = State(initialValue: fromMyStak)
	}

	var body: some View {
		let u = figmaUnit
		let f = detailFacts[symbol] ?? detailFacts["AAPL"]!
		// Authored (1:2579): ONLY the Discover-entry open state composes the
		// shell tab bar (an authored inconsistency - matched per frame).
		let showsBar = !fromMyStak && analystOpen && onTab != nil
		ZStack {
			VStack(spacing: 0) {
				HStack {
					AuthBackCircle(action: onBack)
					Spacer()
					Text(f.symbol)
						.font(StakFont.sora(16 * u, .semiBold))
						.foregroundStyle(Color.white)
					Spacer()
					ZStack {
						Circle().fill(card)
						Image("IcNewsShare")
							.resizable()
							.frame(width: 17 * u, height: 17 * u)
					}
					.frame(width: 40 * u, height: 40 * u)
				}
				.padding(.horizontal, 18 * u)
				.padding(.vertical, 8 * u)

				ScrollView(showsIndicators: false) {
					VStack(spacing: 0) {
						VStack(alignment: .leading, spacing: 4 * u) {
							Text(f.title)
								.font(StakFont.geist(11 * u))
								.foregroundStyle(muted)
							Text(f.price)
								.font(StakFont.sora(26 * u, .semiBold))
								.foregroundStyle(bright)
							Text(f.change)
								.font(StakFont.geist(12 * u, .medium))
								.foregroundStyle(green)
						}
						.frame(maxWidth: .infinity, alignment: .leading)
						.padding(.horizontal, 20 * u)
						.padding(.top, 10 * u)
						.padding(.bottom, 6 * u)

						Image("SdChartLine")
							.resizable()
							.scaledToFit()
							.frame(width: 345 * u, height: 76 * u)
						Spacer().frame(height: 40 * u)
						HStack(spacing: 37 * u) {
							ForEach(["1D", "1W", "1M", "3M", "YTD", "1Y"], id: \.self) { label in
								if label == "3M" {
									Text(label)
										.font(StakFont.geist(12 * u, .medium))
										.foregroundStyle(teal)
										.frame(width: 39 * u, height: 22.5 * u)
										.background(Color(argb: 0x292C9DBC), in: RoundedRectangle(cornerRadius: 11.25 * u))
										.overlay(
											RoundedRectangle(cornerRadius: 11.25 * u)
												.strokeBorder(Color(argb: 0x662C9DBC), lineWidth: 0.75 * u)
										)
								} else {
									Text(label)
										.font(StakFont.geist(12 * u))
										.foregroundStyle(muted)
								}
							}
						}

						VStack(spacing: 14 * u) {
							if fromMyStak {
								SinceYouSavedCard()
							}
							RiskFitCard(f: f)
							NumbersCard(f: f)
							AnalystCard(f: f, open: $analystOpen)
							NewsSignalCard(f: f)
							CompareCard(f: f)
							HStack(alignment: .top, spacing: 8 * u) {
								Text("TIP")
									.font(StakFont.geist(11 * u, .medium))
									.foregroundStyle(Color(argb: 0xFF5BD7E4))
								Text(f.tip)
									.font(StakFont.geist(11 * u))
									.foregroundStyle(muted)
									.frame(width: 260 * u, alignment: .leading)
								Spacer(minLength: 0)
							}
							.padding(.horizontal, 12 * u)
							.padding(.vertical, 10 * u)
							.background(card, in: RoundedRectangle(cornerRadius: 12 * u))
						}
						.padding(.horizontal, 20 * u)
						.padding(.vertical, 12 * u)

						VStack(spacing: 10 * u) {
							if fromMyStak {
								DetailCta(text: "Practice buy") { showBuy = true }
								DetailSecondary(text: "Unsave", action: onBack)
							} else if saved {
								HStack(spacing: 6 * u) {
									Image("IcSavedBookmark")
										.resizable()
										.frame(width: 12 * u, height: 12 * u)
									Text("Saved to My STAK")
										.font(StakFont.geist(14 * u, .medium))
										.foregroundStyle(Color.white)
								}
								.frame(maxWidth: .infinity)
								.frame(height: 52 * u)
								.overlay(RoundedRectangle(cornerRadius: 6 * u).strokeBorder(StakColors.ctaBorderGradient, lineWidth: 0.36 * u))
								DetailSecondary(text: "Practice buy", action: practiceBuy)
							} else {
								// Authored (1:2382 -> 92:969, SMART_ANIMATE 350): the
								// save-success sheet scale-fades in like the News one.
								DetailCta(text: "Save") { withAnimation(.easeOut(duration: 0.35)) { showSuccess = true } }
								DetailSecondary(text: "Practice buy", action: practiceBuy)
							}
						}
						.padding(.horizontal, 20 * u)
						.padding(.top, 4 * u)
						.padding(.bottom, 16 * u)
					}
				}
				if showsBar {
					// Authored (1:2579): the 86-tall shell bar sits fixed at
					// the bottom of the viewport; its taps SWAP - pop the
					// detail instantly and land on the tapped tab.
					MainTabBar(selected: Binding<MainTab>(
						get: { .discover },
						set: { tapped in onTab?(tapped) }
					))
				}
			}
			.ignoresSafeArea(edges: showsBar ? .bottom : [])
			if showSuccess {
				DetailSavedSheet(
					f: f,
					// Unauthored scrim tap - keeps its instant dismiss-and-mark.
					onDismiss: { showSuccess = false; saved = true },
					// Authored (92:969): View in My STAK -> Overview, the
					// forward push; Keep exploring -> deck, dissolve 300 -
					// the stock is marked saved before the page leaves.
					onViewInMyStak: {
						saved = true
						MyStakHoldings.shared.add(f.symbol)
						if let onViewInMyStak { onViewInMyStak() } else { showSuccess = false }
					},
					onKeepExploring: {
						saved = true
						MyStakHoldings.shared.add(f.symbol)
						if let onKeepExploring { onKeepExploring() } else { showSuccess = false }
					}
				)
				// Authored entry (SMART_ANIMATE 350): the News sheet's
				// scale-in - 0.92 -> 1 + fade, 350 ease-out.
				.transition(.asymmetric(insertion: .scale(scale: 0.92).combined(with: .opacity), removal: .opacity))
			}
			if showBuy {
				DiscoverBuyFlow(
					spec: f.buySpec,
					onClose: { showBuy = false },
					filledSecondary: "Done", ticketSecondary: "Back",
					// Authored (71:949 / 71:994): View in My STAK -> Overview,
					// the forward push.
					onFilledPrimary: {
						if let onViewInMyStak { onViewInMyStak() } else { showBuy = false }
					},
					// Authored: Done -> the FOLDED detail (16:1012) - the
					// sheet fades 300 and the Analyst section closes.
					onFilledSecondary: {
						analystOpen = false
						withAnimation(.easeOut(duration: 0.3)) { showBuy = false }
					},
					// Authored (1:3423): the ticket's secondary -> detail,
					// DISSOLVE 300.
					onTicketSecondary: { withAnimation(.easeOut(duration: 0.3)) { showBuy = false } }
				)
				.transition(.opacity)
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}

	/// Authored (1:2382): the Discover entry's Practice buy leaves the
	/// detail for the Simulate tab, Instant - not a ticket; only the
	/// My STAK entry raises the in-page ticket (16:1012).
	private func practiceBuy() {
		if !fromMyStak, let onPracticeBuyToSimulate {
			onPracticeBuyToSimulate()
		} else {
			showBuy = true
		}
	}
}

private struct DetailCta: View {
	let text: String
	let action: () -> Void

	var body: some View {
		let u = figmaUnit
		Button(action: action) {
			Text(text)
				.font(StakFont.geist(14 * u, .medium))
				.foregroundStyle(Color.white)
				.frame(maxWidth: .infinity)
				.frame(height: 52 * u)
				.background(discCtaGradient, in: RoundedRectangle(cornerRadius: 6 * u))
				.overlay(RoundedRectangle(cornerRadius: 6 * u).strokeBorder(StakColors.ctaBorderGradient, lineWidth: 0.36 * u))
		}
		.buttonStyle(.plain)
	}
}

private struct DetailSecondary: View {
	let text: String
	let action: () -> Void

	var body: some View {
		let u = figmaUnit
		Button(action: action) {
			Text(text)
				.font(StakFont.sora(13 * u))
				.foregroundStyle(muted)
				.frame(maxWidth: .infinity)
				.frame(height: 52 * u)
				.overlay(RoundedRectangle(cornerRadius: 6 * u).strokeBorder(Color(argb: 0x54343B4F), lineWidth: 0.36 * u))
		}
		.buttonStyle(.plain)
	}
}

/// Kicker label — Geist Medium 10, tracking 0.8, muted.
private struct Kicker: View {
	let text: String

	var body: some View {
		let u = figmaUnit
		Text(text)
			.font(StakFont.geist(10 * u, .medium))
			.tracking(0.8 * u)
			.foregroundStyle(muted)
	}
}

private struct RiskFitCard: View {
	let f: DetailFacts

	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 12 * u) {
			HStack {
				Text("Risk fit")
					.font(StakFont.sora(15 * u, .semiBold))
					.foregroundStyle(bright)
				Spacer()
				Text("Matches you")
					.font(StakFont.geist(11 * u, .medium))
					.foregroundStyle(Color(argb: 0xFFA6E4F7))
					.padding(.horizontal, 10 * u)
					.padding(.vertical, 4 * u)
					.background(Color(argb: 0x1F5DA8BF), in: Capsule())
			}
			// Authored (1:2382): a lone 14x8 pill indicator - the frame draws no track.
			ZStack(alignment: .topLeading) {
				Color.clear.frame(height: 8 * u)
				RoundedRectangle(cornerRadius: 4 * u)
					.fill(Color(argb: 0xFFA6E4F7))
					.frame(width: 14 * u, height: 8 * u)
					.offset(x: f.riskPillX * u)
			}
			HStack {
				Text("Low").font(StakFont.geist(10 * u)).foregroundStyle(muted)
				Spacer()
				Text("High").font(StakFont.geist(10 * u)).foregroundStyle(muted)
			}
			Text(f.riskCopy)
				.font(StakFont.geist(11 * u))
				.foregroundStyle(muted)
		}
		.padding(.horizontal, 16 * u)
		.padding(.vertical, 14 * u)
		.background(card, in: RoundedRectangle(cornerRadius: 16 * u))
	}
}

private struct NumbersCard: View {
	let f: DetailFacts

	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 12 * u) {
			Text("Numbers that matter")
				.font(StakFont.sora(15 * u, .semiBold))
				.foregroundStyle(bright)
			HStack(spacing: 8 * u) {
				ForEach(f.stats, id: \.label) { st in
					StatCell(label: st.label, value: st.value, verdict: st.verdict, verdictColor: st.good ? green : muted, border: st.border)
				}
			}
			Text("Tap a stat for sector and peer benchmarks")
				.font(StakFont.geist(10 * u))
				.foregroundStyle(muted)
		}
		.padding(.horizontal, 16 * u)
		.padding(.vertical, 14 * u)
		.background(card, in: RoundedRectangle(cornerRadius: 16 * u))
	}
}

private struct StatCell: View {
	let label: String
	let value: String
	let verdict: String
	let verdictColor: Color
	var border: Bool = false

	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 4 * u) {
			Text(label).font(StakFont.geist(10 * u)).foregroundStyle(muted)
			Text(value).font(StakFont.sora(17 * u, .semiBold)).foregroundStyle(bright)
			Text(verdict).font(StakFont.geist(10 * u, .medium)).foregroundStyle(verdictColor)
		}
		.padding(10 * u)
		.frame(maxWidth: .infinity, alignment: .leading)
		.background(card, in: RoundedRectangle(cornerRadius: 12 * u))
		.overlay(
			border
				? RoundedRectangle(cornerRadius: 12 * u).strokeBorder(Color(argb: 0xFF212D4B), lineWidth: 1 * u)
				: nil
		)
	}
}

/// Analyst view (collapsed 1:2454 / open 1:2651) — caret toggles; the open
/// flag is hoisted so the page can compose the 1:2579 tab bar and fold the
/// section when the buy receipt's Done lands on the folded frame (16:1012).
private struct AnalystCard: View {
	let f: DetailFacts
	@Binding var open: Bool

	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 12 * u) {
			HStack {
				Text("Analyst view")
					.font(StakFont.sora(15 * u, .semiBold))
					.foregroundStyle(bright)
				Spacer()
				if !open {
					Image("IcSdCaret")
						.resizable()
						.frame(width: 20 * u, height: 20 * u)
				}
			}
			if !open {
				Text(f.upside)
					.font(StakFont.geist(11 * u, .medium))
					.foregroundStyle(green)
			} else {
				Kicker(text: "PRICE TARGET RANGE")
				// Authored (16:1253): 180-wide teal fill plus a 13x8 end cap - no track.
				ZStack(alignment: .topLeading) {
					Color.clear.frame(height: 8 * u)
					RoundedRectangle(cornerRadius: 4 * u)
						.fill(Color(argb: 0x8C5DA8BF))
						.frame(width: 180 * u, height: 8 * u)
					RoundedRectangle(cornerRadius: 4 * u)
						.fill(Color(argb: 0xFFA6E4F7))
						.frame(width: 13 * u, height: 8 * u)
						.offset(x: f.targetMarkerX * u)
				}
				HStack {
					VStack(alignment: .leading, spacing: 1 * u) {
						Text("Low").font(StakFont.geist(10 * u)).foregroundStyle(muted)
						Text(f.targetLow).font(StakFont.geist(12 * u, .medium)).foregroundStyle(bright)
					}
					Spacer()
					VStack(spacing: 1 * u) {
						Text("Avg").font(StakFont.geist(10 * u)).foregroundStyle(muted)
						Text(f.targetAvg).font(StakFont.geist(12 * u, .medium)).foregroundStyle(bright)
					}
					Spacer()
					VStack(alignment: .trailing, spacing: 1 * u) {
						Text("High").font(StakFont.geist(10 * u)).foregroundStyle(muted)
						Text(f.targetHigh).font(StakFont.geist(12 * u, .medium)).foregroundStyle(bright)
					}
				}
				Text(f.upside)
					.font(StakFont.geist(11 * u, .medium))
					.foregroundStyle(green)
				Kicker(text: f.consensus)
				ZStack(alignment: .leading) {
					RoundedRectangle(cornerRadius: 4 * u).fill(wellBg).frame(height: 8 * u)
					RoundedRectangle(cornerRadius: 4 * u).fill(green).frame(width: f.buyBarW * u, height: 8 * u)
				}
				HStack {
					Text(f.buyCount).font(StakFont.geist(11 * u, .medium)).foregroundStyle(green)
					Spacer()
					Text(f.holdCount).font(StakFont.geist(11 * u, .medium)).foregroundStyle(muted)
					Spacer()
					Text(f.sellCount).font(StakFont.geist(11 * u, .medium)).foregroundStyle(muted)
				}
				Kicker(text: "RECENT ACTIONS")
				ForEach(
					f.actions,
					id: \.0
				) { name, action, target in
					HStack {
						Text(name).font(StakFont.geist(12 * u, .medium)).foregroundStyle(bright)
						Spacer()
						Text(action)
							.font(StakFont.geist(11 * u, .medium))
							.foregroundStyle(action == "Buy" ? green : muted)
						Spacer().frame(width: 10 * u)
						Text(target).font(StakFont.geist(12 * u, .medium)).foregroundStyle(bright)
					}
					.padding(.horizontal, 12 * u)
					.frame(height: 38 * u)
					.background(wellBg, in: RoundedRectangle(cornerRadius: 10 * u))
				}
			}
		}
		.padding(.horizontal, 16 * u)
		.padding(.vertical, 14 * u)
		.background(card, in: RoundedRectangle(cornerRadius: 16 * u))
		.contentShape(Rectangle())
		.onTapGesture { open.toggle() }
	}
}

private struct NewsSignalCard: View {
	let f: DetailFacts

	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 12 * u) {
			Text("News signal")
				.font(StakFont.sora(15 * u, .semiBold))
				.foregroundStyle(bright)
			Text(f.newsClose)
				.font(StakFont.geist(11 * u, .medium))
				.foregroundStyle(green)
			Text(f.newsSignal)
				.font(StakFont.geist(11 * u))
				.foregroundStyle(muted)
			Text(f.newsEarnings)
				.font(StakFont.geist(11 * u))
				.foregroundStyle(muted)
			ScrollView(.horizontal, showsIndicators: false) {
				HStack(spacing: 12 * u) {
					ForEach(f.newsSources, id: \.0) { src, tag in
						newsChip(source: src, tag: tag)
					}
				}
			}
		}
		.padding(.horizontal, 16 * u)
		.padding(.vertical, 14 * u)
		.background(card, in: RoundedRectangle(cornerRadius: 16 * u))
	}

	private func newsChip(source: String, tag: String) -> some View {
		let u = figmaUnit
		return VStack(alignment: .leading, spacing: 8 * u) {
			HStack {
				Text(source).font(StakFont.geist(10 * u)).foregroundStyle(muted)
				Spacer()
				Text(tag)
					.font(StakFont.geist(10 * u, .medium))
					.foregroundStyle(muted)
					.padding(.horizontal, 8 * u)
					.padding(.vertical, 3 * u)
					.background(Color(argb: 0x14FFFFFF), in: Capsule())
			}
			Text(f.newsHeadline)
				.font(StakFont.geist(12 * u))
				.foregroundStyle(bright)
				.frame(width: 173 * u, alignment: .leading)
		}
		.padding(12 * u)
		.frame(width: 205 * u, alignment: .leading)
		.background(card, in: RoundedRectangle(cornerRadius: 12 * u))
	}
}

/// Compare and learn (collapsed 1:2526 / open 1:2719) — peer table.
private struct CompareCard: View {
	let f: DetailFacts

	@State private var open = false

	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: open ? 21 * u : 12 * u) {
			HStack {
				Text("Compare and learn")
					.font(StakFont.sora(15 * u, .semiBold))
					.foregroundStyle(bright)
				Spacer()
				if !open {
					Image("IcSdCaret")
						.resizable()
						.frame(width: 20 * u, height: 20 * u)
				}
			}
			if !open {
				Text(f.peersLabel)
					.font(StakFont.geist(11 * u, .medium))
					.foregroundStyle(muted)
			} else {
				ZStack(alignment: .topLeading) {
					// AAPL column tint spans the table rows (frame 1:2721).
					RoundedRectangle(cornerRadius: 8 * u)
						.fill(Color(argb: 0x125DA8BF))
						.frame(width: 81 * u, height: 170 * u)
						.offset(x: 78 * u)
					VStack(spacing: 12 * u) {
						compareRow("", f.symbol, f.peerA, f.peerB, header: true)
						ForEach(f.compareRows, id: \.label) { r in
							compareRow(r.label, r.a, r.b, r.c, valueColor: r.green ? green : nil)
						}
					}
				}
				Text("Cultural context only, not financial advice.")
					.font(StakFont.geist(10 * u, .medium))
					.foregroundStyle(muted)
			}
		}
		.padding(.horizontal, 16 * u)
		.padding(.vertical, 14 * u)
		.background(card, in: RoundedRectangle(cornerRadius: 16 * u))
		.contentShape(Rectangle())
		.onTapGesture { open.toggle() }
	}

	private func compareRow(_ label: String, _ a: String, _ m: String, _ g: String, header: Bool = false, valueColor: Color? = nil) -> some View {
		let u = figmaUnit
		return HStack(spacing: 8 * u) {
			Text(label)
				.font(StakFont.geist(11 * u))
				.foregroundStyle(muted)
				.frame(maxWidth: .infinity, alignment: .leading)
			Text(a)
				.font(StakFont.geist(11 * u, .medium))
				.foregroundStyle(valueColor ?? bright)
				.frame(maxWidth: .infinity)
			Text(m)
				.font(StakFont.geist(11 * u, header ? .medium : .regular))
				.foregroundStyle(valueColor ?? bright)
				.frame(maxWidth: .infinity)
			Text(g)
				.font(StakFont.geist(11 * u, header ? .medium : .regular))
				.foregroundStyle(valueColor ?? bright)
				.frame(maxWidth: .infinity)
		}
		.frame(height: 20 * u)
	}
}

/// "SINCE YOU SAVED +4.6%" banner (16:1012) for the My STAK entry.
private struct SinceYouSavedCard: View {
	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 12 * u) {
			HStack(spacing: 8 * u) {
				Image("IcSavedBookmark")
					.resizable()
					.frame(width: 12 * u, height: 12 * u)
				Text("SINCE YOU SAVED")
					.font(StakFont.geist(10 * u, .medium))
					.tracking(0.8 * u)
					.foregroundStyle(muted)
				Text("+4.6%")
					.font(StakFont.geist(12 * u, .medium))
					.foregroundStyle(green)
			}
			Text("Saved 5 weeks ago. AAPL is up 4.6% since, moving roughly with the market. Steady giants tend to.")
				.font(StakFont.geist(11 * u))
				.lineSpacing((14 - 11) * u)
				.foregroundStyle(muted)
		}
		.padding(16 * u)
		.frame(maxWidth: .infinity, alignment: .leading)
		.background(card, in: RoundedRectangle(cornerRadius: 16 * u))
	}
}

/// Saved-to-My-STAK sheet over the detail (92:969) — Apple row variant.
/// Built inline (not on SheetScaffold) to pin the Android geometry: 14u
/// item spacing, 11u stock-row spacing, and the Detail CTAs (sora 13).
private struct DetailSavedSheet: View {
	let f: DetailFacts
	let onDismiss: () -> Void
	let onViewInMyStak: () -> Void
	let onKeepExploring: () -> Void

	var body: some View {
		let u = figmaUnit
		ZStack(alignment: .bottom) {
			// Authored scrim rgba(12,19,32,0.55) (106:1037).
			Color(argb: 0x8C0C1320)
				.ignoresSafeArea()
				.onTapGesture(perform: onDismiss)
			VStack(spacing: 14 * u) {
				RoundedRectangle(cornerRadius: 2 * u)
					.fill(Color(argb: 0xFF2A3346))
					.frame(width: 40 * u, height: 4 * u)
					.padding(.bottom, 4 * u)
				Image("IcSheetCheck")
					.resizable()
					.frame(width: 47 * u, height: 47 * u)
				Text("Saved to My STAK")
					.font(StakFont.sora(18 * u, .semiBold))
					.foregroundStyle(Color.white)
				HStack(spacing: 11 * u) {
					ZStack {
						Circle().fill(Color(argb: 0xFF242B3D))
						Text(f.sheetBadge)
							.font(StakFont.sora(15 * u, .semiBold))
							.foregroundStyle(Color(argb: 0xFF9EADC7))
					}
					.frame(width: 38 * u, height: 38 * u)
					VStack(alignment: .leading, spacing: 2 * u) {
						Text(f.sheetName)
							.font(StakFont.geist(13 * u, .medium))
							.foregroundStyle(Color.white)
						Text(f.sheetPrice)
							.font(StakFont.geist(10 * u))
							.foregroundStyle(muted)
					}
					.frame(maxWidth: .infinity, alignment: .leading)
					Text(f.sheetChange)
						.font(StakFont.geist(12 * u, .medium))
						.foregroundStyle(green)
				}
				.padding(.horizontal, 14 * u)
				.padding(.vertical, 12 * u)
				.background(Color(argb: 0x1A69B3CA), in: RoundedRectangle(cornerRadius: 6 * u))
				Text("Watching from today · no money committed")
					.font(StakFont.geist(12 * u))
					.lineSpacing((18 - 12) * u)
					.foregroundStyle(Color(argb: 0xFFC8D2E0))
					.frame(maxWidth: .infinity, alignment: .leading)
				VStack(spacing: 16 * u) {
					DetailCta(text: "View in My STAK", action: onViewInMyStak)
					DetailSecondary(text: "Keep exploring", action: onKeepExploring)
				}
			}
			.padding(.horizontal, 20 * u)
			.padding(.top, 10 * u)
			.padding(.bottom, 30 * u)
			.frame(maxWidth: .infinity)
			.background(card, in: UnevenRoundedRectangle(topLeadingRadius: 24 * u, topTrailingRadius: 24 * u))
			.ignoresSafeArea(edges: .bottom)
		}
	}
}


/// One stat tile in "Numbers that matter".
private struct DetailStat {
	let label: String
	let value: String
	let verdict: String
	var good = false
	var border = false
}

/// One "Compare and learn" table row (a = this stock).
private struct DetailCompareRow {
	let label: String
	let a: String
	let b: String
	let c: String
	var green = false
}

/// Everything the detail page serves per stock - backend-shaped like the
/// news feed’s StockFacts, mirroring android/ StockDetailScreen.kt.
/// AAPL carries the authored 1:2382/92:969 frame values VERBATIM;
/// NVDA and GOOGL extend their deck cards off the same DECK numbers.
private struct DetailFacts {
	let symbol: String
	let title: String
	let price: String
	let change: String
	let tip: String
	let riskPillX: CGFloat
	let riskCopy: String
	let stats: [DetailStat]
	let upside: String
	let targetLow: String
	let targetAvg: String
	let targetHigh: String
	let targetMarkerX: CGFloat
	let consensus: String
	let buyCount: String
	let holdCount: String
	let sellCount: String
	let buyBarW: CGFloat
	let actions: [(String, String, String)]
	let newsClose: String
	let newsSignal: String
	let newsEarnings: String
	let newsSources: [(String, String)]
	let newsHeadline: String
	let peersLabel: String
	let peerA: String
	let peerB: String
	let compareRows: [DetailCompareRow]
	let sheetBadge: String
	let sheetName: String
	let sheetPrice: String
	let sheetChange: String
	let buySpec: BuySpec
}

private let detailFacts: [String: DetailFacts] = [
	"AAPL": DetailFacts(
		symbol: "AAPL",
		title: "AAPL · Apple Inc",
		price: "$229.35",
		change: "▲ 1.2% today",
		tip: "Steady giants move slower. Stable stocks often do.",
		riskPillX: 88,
		riskCopy: "Low volatility. Fits the steady side of your profile.",
		stats: [
			DetailStat(label: "P/E ratio", value: "31.2", verdict: "In line"),
			DetailStat(label: "Revenue growth", value: "6.1%", verdict: "Slower"),
			DetailStat(label: "Profit margin", value: "24.3%", verdict: "Excellent", good: true, border: true),
		],
		upside: "↑ 6.7% upside",
		targetLow: "$180", targetAvg: "$248", targetHigh: "$300", targetMarkerX: 167,
		consensus: "WALL ST. CONSENSUS · 42 ANALYSTS",
		buyCount: "● Buy 28", holdCount: "Hold 12", sellCount: "Sell 2", buyBarW: 212,
		actions: [
			("Morgan Stanley", "Buy", "$260"),
			("Wedbush", "Buy", "$285"),
			("Goldman Sachs", "Buy", "$256"),
			("UBS", "Hold", "$236"),
			("Barclays", "Hold", "$230"),
		],
		newsClose: "▲ +0.8% at yesterday’s close",
		newsSignal: "Foldable iPhone reports point to a premium fall lineup.",
		newsEarnings: "Q3 earnings land July 30.",
		newsSources: [("Yahoo · 13h ago", "Neutral"), ("CNN · 1h ago", "Neutral")],
		newsHeadline: "The rally leaves Apple about 4 percent shy of the market-cap crown",
		peersLabel: "vs MSFT · GOOGL",
		peerA: "MSFT", peerB: "GOOGL",
		compareRows: [
			DetailCompareRow(label: "P/E ratio", a: "31.2", b: "36x", c: "24x"),
			DetailCompareRow(label: "Rev growth", a: "+6.1%", b: "+15%", c: "+12%", green: true),
			DetailCompareRow(label: "Profit margin", a: "24.3%", b: "36%", c: "29%"),
			DetailCompareRow(label: "Market cap", a: "$3.5T", b: "$3.4T", c: "$2.3T"),
		],
		sheetBadge: "A", sheetName: "Apple", sheetPrice: "$229.35 today", sheetChange: "▲ 1.2%",
		buySpec: aaplBuy
	),
	"NVDA": DetailFacts(
		symbol: "NVDA",
		title: "NVDA · NVIDIA Corp",
		price: "$122.10",
		change: "▲ 2.4% today",
		tip: "Chip stocks swing hard. Small stakes, long views.",
		riskPillX: 238,
		riskCopy: "High volatility. Fits the bolder side of your profile.",
		stats: [
			DetailStat(label: "P/E ratio", value: "52.8", verdict: "Rich"),
			DetailStat(label: "Revenue growth", value: "62%", verdict: "Explosive", good: true, border: true),
			DetailStat(label: "Profit margin", value: "48.9%", verdict: "Strong"),
		],
		upside: "↑ 17.9% upside",
		targetLow: "$100", targetAvg: "$144", targetHigh: "$200", targetMarkerX: 33,
		consensus: "WALL ST. CONSENSUS · 63 ANALYSTS",
		buyCount: "● Buy 55", holdCount: "Hold 7", sellCount: "Sell 1", buyBarW: 273,
		actions: [
			("Morgan Stanley", "Buy", "$152"),
			("BofA", "Buy", "$150"),
			("Goldman Sachs", "Buy", "$145"),
			("Citi", "Buy", "$150"),
			("HSBC", "Hold", "$120"),
		],
		newsClose: "▲ +2.1% at yesterday’s close",
		newsSignal: "Blackwell demand keeps outrunning supply into the fall.",
		newsEarnings: "Q2 earnings land Aug 27.",
		newsSources: [("Reuters · 2h ago", "Bullish"), ("CNBC · 9h ago", "Neutral")],
		newsHeadline: "Nvidia lags the chip rally it kicked off as orders pile up",
		peersLabel: "vs AMD · TSM",
		peerA: "AMD", peerB: "TSM",
		compareRows: [
			DetailCompareRow(label: "P/E ratio", a: "52.8", b: "110x", c: "28x"),
			DetailCompareRow(label: "Rev growth", a: "+62%", b: "+18%", c: "+33%", green: true),
			DetailCompareRow(label: "Profit margin", a: "48.9%", b: "6.4%", c: "39%"),
			DetailCompareRow(label: "Market cap", a: "$3.0T", b: "$0.2T", c: "$1.0T"),
		],
		sheetBadge: "N", sheetName: "NVIDIA", sheetPrice: "$122.10 today", sheetChange: "▲ 2.4%",
		buySpec: nvdaBuy
	),
	"GOOGL": DetailFacts(
		symbol: "GOOGL",
		title: "GOOGL · Alphabet Inc",
		price: "$178.90",
		change: "▲ 0.8% today",
		tip: "Ad money moves with the economy, so some quarters just drift.",
		riskPillX: 150,
		riskCopy: "Moderate volatility. Sits mid-range for your profile.",
		stats: [
			DetailStat(label: "P/E ratio", value: "24.1", verdict: "Cheaper", good: true, border: true),
			DetailStat(label: "Revenue growth", value: "12%", verdict: "Healthy"),
			DetailStat(label: "Profit margin", value: "29.5%", verdict: "Strong"),
		],
		upside: "↑ 12.4% upside",
		targetLow: "$150", targetAvg: "$201", targetHigh: "$240", targetMarkerX: 52,
		consensus: "WALL ST. CONSENSUS · 48 ANALYSTS",
		buyCount: "● Buy 40", holdCount: "Hold 8", sellCount: "Sell 0", buyBarW: 261,
		actions: [
			("Morgan Stanley", "Buy", "$210"),
			("JPMorgan", "Buy", "$208"),
			("Goldman Sachs", "Buy", "$205"),
			("Bernstein", "Hold", "$185"),
			("Wells Fargo", "Hold", "$182"),
		],
		newsClose: "▲ +0.6% at yesterday’s close",
		newsSignal: "A blowout ad quarter pushed the stock to fresh highs.",
		newsEarnings: "Q2 earnings land Jul 22.",
		newsSources: [("Bloomberg · 5h ago", "Bullish"), ("Yahoo · 1d ago", "Neutral")],
		newsHeadline: "Alphabet jumps after a blowout ad quarter as cloud accelerates",
		peersLabel: "vs MSFT · META",
		peerA: "MSFT", peerB: "META",
		compareRows: [
			DetailCompareRow(label: "P/E ratio", a: "24.1", b: "36x", c: "27x"),
			DetailCompareRow(label: "Rev growth", a: "+12%", b: "+15%", c: "+19%", green: true),
			DetailCompareRow(label: "Profit margin", a: "29.5%", b: "36%", c: "34%"),
			DetailCompareRow(label: "Market cap", a: "$2.3T", b: "$3.4T", c: "$1.5T"),
		],
		sheetBadge: "G", sheetName: "Alphabet", sheetPrice: "$178.90 today", sheetChange: "▲ 0.8%",
		buySpec: googlBuy
	),
]
