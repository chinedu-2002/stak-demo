import SwiftUI

/// Discover · Stock Detail (CHINEDU 1:2382 folded / 1:2579 open, save
/// success 92:969; My STAK entry 16:1012) — the AAPL page: price hero,
/// chart with range pills, Risk fit, Numbers that matter, expandable
/// Analyst view / Compare and learn, News signal, TIP and the CTAs.
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
		ZStack {
			VStack(spacing: 0) {
				HStack {
					AuthBackCircle(action: onBack)
					Spacer()
					Text("AAPL")
						.font(StakFont.sora(16, .semiBold))
						.foregroundStyle(Color.white)
					Spacer()
					ZStack {
						Circle().fill(card)
						Image("IcNewsShare")
							.resizable()
							.frame(width: 17, height: 17)
					}
					.frame(width: 40, height: 40)
				}
				.padding(.horizontal, 18)
				.padding(.vertical, 8)

				ScrollView(showsIndicators: false) {
					VStack(spacing: 0) {
						VStack(alignment: .leading, spacing: 4) {
							Text("AAPL · Apple Inc")
								.font(StakFont.geist(11))
								.foregroundStyle(muted)
							Text("$229.35")
								.font(StakFont.sora(26, .semiBold))
								.foregroundStyle(bright)
							Text("▲ 1.2% today")
								.font(StakFont.geist(12, .medium))
								.foregroundStyle(green)
						}
						.frame(maxWidth: .infinity, alignment: .leading)
						.padding(.horizontal, 20)
						.padding(.top, 10)
						.padding(.bottom, 6)

						Image("SdChartLine")
							.resizable()
							.scaledToFit()
							.frame(width: 345, height: 76)
						Spacer().frame(height: 40)
						HStack(spacing: 37) {
							ForEach(["1D", "1W", "1M", "3M", "YTD", "1Y"], id: \.self) { label in
								if label == "3M" {
									Text(label)
										.font(StakFont.geist(12, .medium))
										.foregroundStyle(teal)
										.frame(width: 39, height: 22.5)
										.background(Color(argb: 0x292C9DBC), in: RoundedRectangle(cornerRadius: 11.25))
										.overlay(
											RoundedRectangle(cornerRadius: 11.25)
												.strokeBorder(Color(argb: 0x662C9DBC), lineWidth: 0.75)
										)
								} else {
									Text(label)
										.font(StakFont.geist(12))
										.foregroundStyle(muted)
								}
							}
						}

						VStack(spacing: 14) {
							if fromMyStak {
								SinceYouSavedCard()
							}
							RiskFitCard()
							NumbersCard()
							AnalystCard()
							NewsSignalCard()
							CompareCard()
							HStack(alignment: .top, spacing: 8) {
								Text("TIP")
									.font(StakFont.geist(11, .medium))
									.foregroundStyle(Color(argb: 0xFF5BD7E4))
								Text("Steady giants move slower. Stable stocks often do.")
									.font(StakFont.geist(11))
									.foregroundStyle(muted)
									.frame(width: 260, alignment: .leading)
								Spacer(minLength: 0)
							}
							.padding(.horizontal, 12)
							.padding(.vertical, 10)
							.background(card, in: RoundedRectangle(cornerRadius: 12))
						}
						.padding(.horizontal, 20)
						.padding(.vertical, 12)

						VStack(spacing: 10) {
							if fromMyStak {
								DetailCta(text: "Practice buy") { showBuy = true }
								DetailSecondary(text: "Unsave", action: onBack)
							} else if saved {
								HStack(spacing: 6) {
									Image("IcSavedBookmark")
										.resizable()
										.frame(width: 12, height: 12)
									Text("Saved to My STAK")
										.font(StakFont.geist(14, .medium))
										.foregroundStyle(Color.white)
								}
								.frame(maxWidth: .infinity)
								.frame(height: 52)
								.overlay(RoundedRectangle(cornerRadius: 6).strokeBorder(Disc.ctaBorder, lineWidth: 0.36))
								DetailSecondary(text: "Practice buy") { showBuy = true }
							} else {
								DetailCta(text: "Save") { showSuccess = true }
								DetailSecondary(text: "Practice buy") { showBuy = true }
							}
						}
						.padding(.horizontal, 20)
						.padding(.top, 4)
						.padding(.bottom, 16)
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
		Button(action: action) {
			Text(text)
				.font(StakFont.geist(14, .medium))
				.foregroundStyle(Color.white)
				.frame(maxWidth: .infinity)
				.frame(height: 52)
				.background(discCtaGradient, in: RoundedRectangle(cornerRadius: 6))
				.overlay(RoundedRectangle(cornerRadius: 6).strokeBorder(Disc.ctaBorder, lineWidth: 0.36))
		}
		.buttonStyle(.plain)
	}
}

private struct DetailSecondary: View {
	let text: String
	let action: () -> Void

	var body: some View {
		Button(action: action) {
			Text(text)
				.font(StakFont.sora(13))
				.foregroundStyle(muted)
				.frame(maxWidth: .infinity)
				.frame(height: 52)
				.overlay(RoundedRectangle(cornerRadius: 6).strokeBorder(Color(argb: 0x54343B4F), lineWidth: 0.36))
		}
		.buttonStyle(.plain)
	}
}

/// Kicker label — Geist Medium 10, tracking 0.8, muted.
private struct Kicker: View {
	let text: String

	var body: some View {
		Text(text)
			.font(StakFont.geist(10, .medium))
			.tracking(0.8)
			.foregroundStyle(muted)
	}
}

private struct RiskFitCard: View {
	var body: some View {
		VStack(alignment: .leading, spacing: 12) {
			HStack {
				Text("Risk fit")
					.font(StakFont.sora(15, .semiBold))
					.foregroundStyle(bright)
				Spacer()
				Text("Matches you")
					.font(StakFont.geist(11, .medium))
					.foregroundStyle(Color(argb: 0xFFA6E4F7))
					.padding(.horizontal, 10)
					.padding(.vertical, 4)
					.background(Color(argb: 0x1F5DA8BF), in: Capsule())
			}
			ZStack(alignment: .topLeading) {
				RoundedRectangle(cornerRadius: 4)
					.fill(wellBg)
					.frame(height: 8)
				Image("IcSdMarker")
					.resizable()
					.frame(width: 14, height: 14)
					.offset(x: 88, y: -3)
			}
			HStack {
				Text("Low").font(StakFont.geist(10)).foregroundStyle(muted)
				Spacer()
				Text("High").font(StakFont.geist(10)).foregroundStyle(muted)
			}
			Text("Low volatility. Fits the steady side of your profile.")
				.font(StakFont.geist(11))
				.foregroundStyle(muted)
		}
		.padding(.horizontal, 16)
		.padding(.vertical, 14)
		.background(card, in: RoundedRectangle(cornerRadius: 16))
	}
}

private struct NumbersCard: View {
	var body: some View {
		VStack(alignment: .leading, spacing: 12) {
			Text("Numbers that matter")
				.font(StakFont.sora(15, .semiBold))
				.foregroundStyle(bright)
			HStack(spacing: 8) {
				StatCell(label: "P/E ratio", value: "31.2", verdict: "In line", verdictColor: muted)
				StatCell(label: "Revenue growth", value: "6.1%", verdict: "Slower", verdictColor: muted)
				StatCell(label: "Profit margin", value: "24.3%", verdict: "Excellent", verdictColor: green, border: true)
			}
			Text("Tap a stat for sector and peer benchmarks")
				.font(StakFont.geist(10))
				.foregroundStyle(muted)
		}
		.padding(.horizontal, 16)
		.padding(.vertical, 14)
		.background(card, in: RoundedRectangle(cornerRadius: 16))
	}
}

private struct StatCell: View {
	let label: String
	let value: String
	let verdict: String
	let verdictColor: Color
	var border: Bool = false

	var body: some View {
		VStack(alignment: .leading, spacing: 4) {
			Text(label).font(StakFont.geist(10)).foregroundStyle(muted)
			Text(value).font(StakFont.sora(17, .semiBold)).foregroundStyle(bright)
			Text(verdict).font(StakFont.geist(10, .medium)).foregroundStyle(verdictColor)
		}
		.padding(10)
		.frame(maxWidth: .infinity, alignment: .leading)
		.background(card, in: RoundedRectangle(cornerRadius: 12))
		.overlay(
			border
				? RoundedRectangle(cornerRadius: 12).strokeBorder(Color(argb: 0xFF212D4B), lineWidth: 1)
				: nil
		)
	}
}

/// Analyst view (collapsed 1:2454 / open 1:2651) — caret toggles.
private struct AnalystCard: View {
	@State private var open = false

	var body: some View {
		VStack(alignment: .leading, spacing: 12) {
			HStack {
				Text("Analyst view")
					.font(StakFont.sora(15, .semiBold))
					.foregroundStyle(bright)
				Spacer()
				if !open {
					Image("IcSdCaret")
						.resizable()
						.frame(width: 20, height: 20)
				}
			}
			if !open {
				Text("↑ 6.7% upside")
					.font(StakFont.geist(11, .medium))
					.foregroundStyle(green)
			} else {
				Kicker(text: "PRICE TARGET RANGE")
				ZStack(alignment: .topLeading) {
					RoundedRectangle(cornerRadius: 4).fill(wellBg).frame(height: 8)
					RoundedRectangle(cornerRadius: 4)
						.fill(Color(argb: 0x8C5DA8BF))
						.frame(width: 180, height: 8)
					Image("IcSdMarker")
						.resizable()
						.frame(width: 14, height: 14)
						.offset(x: 173, y: -3)
				}
				HStack {
					VStack(alignment: .leading, spacing: 1) {
						Text("Low").font(StakFont.geist(10)).foregroundStyle(muted)
						Text("$180").font(StakFont.geist(12, .medium)).foregroundStyle(bright)
					}
					Spacer()
					VStack(spacing: 1) {
						Text("Avg").font(StakFont.geist(10)).foregroundStyle(muted)
						Text("$248").font(StakFont.geist(12, .medium)).foregroundStyle(bright)
					}
					Spacer()
					VStack(alignment: .trailing, spacing: 1) {
						Text("High").font(StakFont.geist(10)).foregroundStyle(muted)
						Text("$300").font(StakFont.geist(12, .medium)).foregroundStyle(bright)
					}
				}
				Text("↑ 6.7% upside")
					.font(StakFont.geist(11, .medium))
					.foregroundStyle(green)
				Kicker(text: "WALL ST. CONSENSUS · 42 ANALYSTS")
				ZStack(alignment: .leading) {
					RoundedRectangle(cornerRadius: 4).fill(wellBg).frame(height: 8)
					RoundedRectangle(cornerRadius: 4).fill(green).frame(width: 212, height: 8)
				}
				HStack {
					Text("● Buy 28").font(StakFont.geist(11, .medium)).foregroundStyle(green)
					Spacer()
					Text("Hold 12").font(StakFont.geist(11, .medium)).foregroundStyle(muted)
					Spacer()
					Text("Sell 2").font(StakFont.geist(11, .medium)).foregroundStyle(muted)
				}
				Kicker(text: "RECENT ACTIONS")
				ForEach(
					[("Morgan Stanley", "Buy", "$260"), ("Wedbush", "Buy", "$285"),
					 ("Goldman Sachs", "Buy", "$256"), ("UBS", "Hold", "$236"),
					 ("Barclays", "Hold", "$230")],
					id: \.0
				) { name, action, target in
					HStack {
						Text(name).font(StakFont.geist(12, .medium)).foregroundStyle(bright)
						Spacer()
						Text(action)
							.font(StakFont.geist(11, .medium))
							.foregroundStyle(action == "Buy" ? green : muted)
						Spacer().frame(width: 10)
						Text(target).font(StakFont.geist(12, .medium)).foregroundStyle(bright)
					}
					.padding(.horizontal, 12)
					.frame(height: 38)
					.background(wellBg, in: RoundedRectangle(cornerRadius: 10))
				}
			}
		}
		.padding(.horizontal, 16)
		.padding(.vertical, 14)
		.background(card, in: RoundedRectangle(cornerRadius: 16))
		.contentShape(Rectangle())
		.onTapGesture { open.toggle() }
	}
}

private struct NewsSignalCard: View {
	var body: some View {
		VStack(alignment: .leading, spacing: 12) {
			Text("News signal")
				.font(StakFont.sora(15, .semiBold))
				.foregroundStyle(bright)
			Text("▲ +0.8% at yesterday’s close")
				.font(StakFont.geist(11, .medium))
				.foregroundStyle(green)
			Text("Foldable iPhone reports point to a premium fall lineup.")
				.font(StakFont.geist(11))
				.foregroundStyle(muted)
			Text("Q3 earnings land July 30.")
				.font(StakFont.geist(11))
				.foregroundStyle(muted)
			ScrollView(.horizontal, showsIndicators: false) {
				HStack(spacing: 12) {
					newsChip(source: "Yahoo · 13h ago")
					newsChip(source: "CNN · 1h ago")
				}
			}
		}
		.padding(.horizontal, 16)
		.padding(.vertical, 14)
		.background(card, in: RoundedRectangle(cornerRadius: 16))
	}

	private func newsChip(source: String) -> some View {
		VStack(alignment: .leading, spacing: 8) {
			HStack {
				Text(source).font(StakFont.geist(10)).foregroundStyle(muted)
				Spacer()
				Text("Neutral")
					.font(StakFont.geist(10, .medium))
					.foregroundStyle(muted)
					.padding(.horizontal, 8)
					.padding(.vertical, 3)
					.background(Color(argb: 0x14FFFFFF), in: Capsule())
			}
			Text("The rally leaves Apple about 4 percent shy of the market-cap crown")
				.font(StakFont.geist(12))
				.foregroundStyle(bright)
				.frame(width: 173, alignment: .leading)
		}
		.padding(12)
		.frame(width: 205, alignment: .leading)
		.background(card, in: RoundedRectangle(cornerRadius: 12))
	}
}

/// Compare and learn (collapsed 1:2526 / open 1:2719) — peer table.
private struct CompareCard: View {
	@State private var open = false

	var body: some View {
		VStack(alignment: .leading, spacing: open ? 21 : 12) {
			HStack {
				Text("Compare and learn")
					.font(StakFont.sora(15, .semiBold))
					.foregroundStyle(bright)
				Spacer()
				if !open {
					Image("IcSdCaret")
						.resizable()
						.frame(width: 20, height: 20)
				}
			}
			if !open {
				Text("vs MSFT · GOOGL")
					.font(StakFont.geist(11, .medium))
					.foregroundStyle(muted)
			} else {
				ZStack(alignment: .topLeading) {
					// AAPL column tint spans the table rows (frame 1:2721).
					RoundedRectangle(cornerRadius: 8)
						.fill(Color(argb: 0x125DA8BF))
						.frame(width: 81, height: 170)
						.offset(x: 78)
					VStack(spacing: 12) {
						compareRow("", "AAPL", "MSFT", "GOOGL")
						compareRow("P/E ratio", "31.2", "36x", "24x")
						compareRow("Rev growth", "+6.1%", "+15%", "+12%", valueColor: green)
						compareRow("Profit margin", "24.3%", "36%", "29%")
						compareRow("Market cap", "$3.5T", "$3.4T", "$2.3T")
					}
				}
				Text("Cultural context only, not financial advice.")
					.font(StakFont.geist(10, .medium))
					.foregroundStyle(muted)
			}
		}
		.padding(.horizontal, 16)
		.padding(.vertical, 14)
		.background(card, in: RoundedRectangle(cornerRadius: 16))
		.contentShape(Rectangle())
		.onTapGesture { open.toggle() }
	}

	private func compareRow(_ label: String, _ a: String, _ m: String, _ g: String, valueColor: Color? = nil) -> some View {
		HStack(spacing: 8) {
			Text(label)
				.font(StakFont.geist(11))
				.foregroundStyle(muted)
				.frame(maxWidth: .infinity, alignment: .leading)
			Text(a)
				.font(StakFont.geist(11, .medium))
				.foregroundStyle(valueColor ?? bright)
				.frame(maxWidth: .infinity)
			Text(m)
				.font(StakFont.geist(11))
				.foregroundStyle(valueColor ?? bright)
				.frame(maxWidth: .infinity)
			Text(g)
				.font(StakFont.geist(11))
				.foregroundStyle(valueColor ?? bright)
				.frame(maxWidth: .infinity)
		}
		.frame(height: 20)
	}
}

/// "SINCE YOU SAVED +4.6%" banner (16:1012) for the My STAK entry.
private struct SinceYouSavedCard: View {
	var body: some View {
		VStack(alignment: .leading, spacing: 12) {
			HStack(spacing: 8) {
				Image("IcSavedBookmark")
					.resizable()
					.frame(width: 12, height: 12)
				Text("SINCE YOU SAVED")
					.font(StakFont.geist(10, .medium))
					.tracking(0.8)
					.foregroundStyle(muted)
				Spacer().frame(width: 8)
				Text("+4.6%")
					.font(StakFont.geist(12, .medium))
					.foregroundStyle(green)
			}
			Text("Saved 5 weeks ago. AAPL is up 4.6% since, moving roughly with the market. Steady giants tend to.")
				.font(StakFont.geist(11))
				.lineSpacing(14 - 11)
				.foregroundStyle(muted)
		}
		.padding(16)
		.frame(maxWidth: .infinity, alignment: .leading)
		.background(card, in: RoundedRectangle(cornerRadius: 16))
	}
}

/// Saved-to-My-STAK sheet over the detail (92:969) — Apple row variant.
private struct DetailSavedSheet: View {
	let onDone: () -> Void

	var body: some View {
		SheetScaffold(onDismiss: onDone) {
			VStack(spacing: 14) {
				Image("IcSheetCheck")
					.resizable()
					.frame(width: 47, height: 47)
				Text("Saved to My STAK")
					.font(StakFont.sora(18, .semiBold))
					.foregroundStyle(Color.white)
				SheetStockRow(spec: aaplBuy)
				Text("Watching from today · no money committed")
					.font(StakFont.geist(12))
					.lineSpacing(18 - 12)
					.foregroundStyle(Color(argb: 0xFFC8D2E0))
					.frame(maxWidth: .infinity, alignment: .leading)
				VStack(spacing: 16) {
					SheetCta(text: "View in My STAK", action: onDone)
					SheetSecondary(text: "Keep exploring", action: onDone)
				}
			}
		}
	}
}
