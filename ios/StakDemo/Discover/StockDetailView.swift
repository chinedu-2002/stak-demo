import SwiftUI

/// Discover · Stock Detail (CHINEDU 1:2382 folded / 1:2579 open, save
/// success 92:969; My STAK entry 16:1012) — the AAPL page: price hero,
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

	@State private var saved: Bool
	@State private var showSuccess = false
	@State private var showBuy = false

	init(onBack: @escaping () -> Void, fromMyStak: Bool = false) {
		self.onBack = onBack
		self.fromMyStak = fromMyStak
		self._saved = State(initialValue: fromMyStak)
	}

	var body: some View {
		let u = figmaUnit
		ZStack {
			VStack(spacing: 0) {
				HStack {
					AuthBackCircle(action: onBack)
					Spacer()
					Text("AAPL")
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
							Text("AAPL · Apple Inc")
								.font(StakFont.geist(11 * u))
								.foregroundStyle(muted)
							Text("$229.35")
								.font(StakFont.sora(26 * u, .semiBold))
								.foregroundStyle(bright)
							Text("▲ 1.2% today")
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
							RiskFitCard()
							NumbersCard()
							AnalystCard()
							NewsSignalCard()
							CompareCard()
							HStack(alignment: .top, spacing: 8 * u) {
								Text("TIP")
									.font(StakFont.geist(11 * u, .medium))
									.foregroundStyle(Color(argb: 0xFF5BD7E4))
								Text("Steady giants move slower. Stable stocks often do.")
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
								.overlay(RoundedRectangle(cornerRadius: 6 * u).strokeBorder(Color(argb: 0xA1659EAD), lineWidth: 0.36 * u))
								DetailSecondary(text: "Practice buy") { showBuy = true }
							} else {
								DetailCta(text: "Save") { showSuccess = true }
								DetailSecondary(text: "Practice buy") { showBuy = true }
							}
						}
						.padding(.horizontal, 20 * u)
						.padding(.top, 4 * u)
						.padding(.bottom, 16 * u)
					}
				}
			}
			if showSuccess {
				DetailSavedSheet(onDone: { showSuccess = false; saved = true })
			}
			if showBuy {
				DiscoverBuyFlow(spec: aaplBuy, onClose: { showBuy = false })
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
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
				.overlay(RoundedRectangle(cornerRadius: 6 * u).strokeBorder(Color(argb: 0xA1659EAD), lineWidth: 0.36 * u))
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
			ZStack(alignment: .topLeading) {
				RoundedRectangle(cornerRadius: 4 * u)
					.fill(wellBg)
					.frame(height: 8 * u)
				Image("IcSdMarker")
					.resizable()
					.frame(width: 14 * u, height: 14 * u)
					.offset(x: 88 * u, y: -3 * u)
			}
			HStack {
				Text("Low").font(StakFont.geist(10 * u)).foregroundStyle(muted)
				Spacer()
				Text("High").font(StakFont.geist(10 * u)).foregroundStyle(muted)
			}
			Text("Low volatility. Fits the steady side of your profile.")
				.font(StakFont.geist(11 * u))
				.foregroundStyle(muted)
		}
		.padding(.horizontal, 16 * u)
		.padding(.vertical, 14 * u)
		.background(card, in: RoundedRectangle(cornerRadius: 16 * u))
	}
}

private struct NumbersCard: View {
	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 12 * u) {
			Text("Numbers that matter")
				.font(StakFont.sora(15 * u, .semiBold))
				.foregroundStyle(bright)
			HStack(spacing: 8 * u) {
				StatCell(label: "P/E ratio", value: "31.2", verdict: "In line", verdictColor: muted)
				StatCell(label: "Revenue growth", value: "6.1%", verdict: "Slower", verdictColor: muted)
				StatCell(label: "Profit margin", value: "24.3%", verdict: "Excellent", verdictColor: green, border: true)
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

/// Analyst view (collapsed 1:2454 / open 1:2651) — caret toggles.
private struct AnalystCard: View {
	@State private var open = false

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
				Text("↑ 6.7% upside")
					.font(StakFont.geist(11 * u, .medium))
					.foregroundStyle(green)
			} else {
				Kicker(text: "PRICE TARGET RANGE")
				ZStack(alignment: .topLeading) {
					RoundedRectangle(cornerRadius: 4 * u).fill(wellBg).frame(height: 8 * u)
					RoundedRectangle(cornerRadius: 4 * u)
						.fill(Color(argb: 0x8C5DA8BF))
						.frame(width: 180 * u, height: 8 * u)
					Image("IcSdMarker")
						.resizable()
						.frame(width: 14 * u, height: 14 * u)
						.offset(x: 173 * u, y: -3 * u)
				}
				HStack {
					VStack(alignment: .leading, spacing: 1 * u) {
						Text("Low").font(StakFont.geist(10 * u)).foregroundStyle(muted)
						Text("$180").font(StakFont.geist(12 * u, .medium)).foregroundStyle(bright)
					}
					Spacer()
					VStack(spacing: 1 * u) {
						Text("Avg").font(StakFont.geist(10 * u)).foregroundStyle(muted)
						Text("$248").font(StakFont.geist(12 * u, .medium)).foregroundStyle(bright)
					}
					Spacer()
					VStack(alignment: .trailing, spacing: 1 * u) {
						Text("High").font(StakFont.geist(10 * u)).foregroundStyle(muted)
						Text("$300").font(StakFont.geist(12 * u, .medium)).foregroundStyle(bright)
					}
				}
				Text("↑ 6.7% upside")
					.font(StakFont.geist(11 * u, .medium))
					.foregroundStyle(green)
				Kicker(text: "WALL ST. CONSENSUS · 42 ANALYSTS")
				ZStack(alignment: .leading) {
					RoundedRectangle(cornerRadius: 4 * u).fill(wellBg).frame(height: 8 * u)
					RoundedRectangle(cornerRadius: 4 * u).fill(green).frame(width: 212 * u, height: 8 * u)
				}
				HStack {
					Text("● Buy 28").font(StakFont.geist(11 * u, .medium)).foregroundStyle(green)
					Spacer()
					Text("Hold 12").font(StakFont.geist(11 * u, .medium)).foregroundStyle(muted)
					Spacer()
					Text("Sell 2").font(StakFont.geist(11 * u, .medium)).foregroundStyle(muted)
				}
				Kicker(text: "RECENT ACTIONS")
				ForEach(
					[("Morgan Stanley", "Buy", "$260"), ("Wedbush", "Buy", "$285"),
					 ("Goldman Sachs", "Buy", "$256"), ("UBS", "Hold", "$236"),
					 ("Barclays", "Hold", "$230")],
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
	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 12 * u) {
			Text("News signal")
				.font(StakFont.sora(15 * u, .semiBold))
				.foregroundStyle(bright)
			Text("▲ +0.8% at yesterday’s close")
				.font(StakFont.geist(11 * u, .medium))
				.foregroundStyle(green)
			Text("Foldable iPhone reports point to a premium fall lineup.")
				.font(StakFont.geist(11 * u))
				.foregroundStyle(muted)
			Text("Q3 earnings land July 30.")
				.font(StakFont.geist(11 * u))
				.foregroundStyle(muted)
			ScrollView(.horizontal, showsIndicators: false) {
				HStack(spacing: 12 * u) {
					newsChip(source: "Yahoo · 13h ago")
					newsChip(source: "CNN · 1h ago")
				}
			}
		}
		.padding(.horizontal, 16 * u)
		.padding(.vertical, 14 * u)
		.background(card, in: RoundedRectangle(cornerRadius: 16 * u))
	}

	private func newsChip(source: String) -> some View {
		let u = figmaUnit
		return VStack(alignment: .leading, spacing: 8 * u) {
			HStack {
				Text(source).font(StakFont.geist(10 * u)).foregroundStyle(muted)
				Spacer()
				Text("Neutral")
					.font(StakFont.geist(10 * u, .medium))
					.foregroundStyle(muted)
					.padding(.horizontal, 8 * u)
					.padding(.vertical, 3 * u)
					.background(Color(argb: 0x14FFFFFF), in: Capsule())
			}
			Text("The rally leaves Apple about 4 percent shy of the market-cap crown")
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
				Text("vs MSFT · GOOGL")
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
						compareRow("", "AAPL", "MSFT", "GOOGL", header: true)
						compareRow("P/E ratio", "31.2", "36x", "24x")
						compareRow("Rev growth", "+6.1%", "+15%", "+12%", valueColor: green)
						compareRow("Profit margin", "24.3%", "36%", "29%")
						compareRow("Market cap", "$3.5T", "$3.4T", "$2.3T")
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
				Spacer().frame(width: 8 * u)
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
	let onDone: () -> Void

	var body: some View {
		let u = figmaUnit
		ZStack(alignment: .bottom) {
			Color(argb: 0x730A1020)
				.ignoresSafeArea()
				.onTapGesture(perform: onDone)
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
						Text("A")
							.font(StakFont.sora(15 * u, .semiBold))
							.foregroundStyle(Color(argb: 0xFF9EADC7))
					}
					.frame(width: 38 * u, height: 38 * u)
					VStack(alignment: .leading, spacing: 2 * u) {
						Text("Apple")
							.font(StakFont.geist(13 * u, .medium))
							.foregroundStyle(Color.white)
						Text("$229.35 today")
							.font(StakFont.geist(10 * u))
							.foregroundStyle(muted)
					}
					.frame(maxWidth: .infinity, alignment: .leading)
					Text("▲ 1.2%")
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
					DetailCta(text: "View in My STAK", action: onDone)
					DetailSecondary(text: "Keep exploring", action: onDone)
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
