import SwiftUI

/// 07 · Simulate — "Final · Portfolio · paper" (CHINEDU 1:4496) with
/// the Sell NVDA? confirm (1:4698) and Position closed (73:855)
/// sheets. Ported from android/ ui/simulate/SimPortfolioScreen.kt.
/// Every metric is scaled by the 390pt artboard unit (`figmaUnit`),
/// exactly like the Android build's `u` scaling.
/// Codex audit (2026-09-04): the rows (SimPick) and the SOLD · REALIZED
/// list come from PaperPortfolio - a buy prepends a row, a sell moves it
/// down here.
struct SimPortfolioView: View {
	let onBack: () -> Void
	/// Codex parity audit (2026-09-04): every row / Sell pill opens ITS
	/// pick - the tapped ticker rides to PickDetailView(symbol:).
	let onOpenPick: (String) -> Void

	@State private var showSell = false
	@State private var showClosed = false
	@ObservedObject private var portfolio = PaperPortfolio.shared

	var body: some View {
		let u = figmaUnit
		ZStack {
			VStack(spacing: 0) {
				HStack {
					AuthBackCircle(action: onBack)
					Spacer()
					Text("Your portfolio")
						.font(StakFont.sora(16 * u, .semiBold))
						.foregroundStyle(Color.white)
					Spacer()
					ZStack {
						Circle().fill(Sim.cardBg)
						Image("IcNewsShare")
							.resizable()
							.frame(width: 17 * u, height: 17 * u)
					}
					.frame(width: 40 * u, height: 40 * u)
				}
				.padding(.horizontal, 18 * u)
				.padding(.vertical, 8 * u)

				ScrollView(showsIndicators: false) {
					VStack(alignment: .leading, spacing: 16 * u) {
						// 1:4496 hides the Portfolio-value/$10,240/cash layers —
						// the visible summary is this one centered 158x32 pill,
						// filled with the card colour (no hairline).
						// Authored copy (user, 2026-09-04 (CHINEDU 07 · Simulate 423:1007): the authored look wins).
						Text("12 picks · +$240.00 all time")
							.font(StakFont.geist(10 * u))
							.foregroundStyle(Sim.muted)
							.frame(width: 158 * u, height: 32 * u)
							.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 16 * u))
							.frame(maxWidth: .infinity)
						HStack(spacing: 8 * u) {
							FilterChip(label: "Top gainers", selected: true)
							FilterChip(label: "Newest", selected: false)
							FilterChip(label: "Worst", selected: false)
						}
						ForEach(portfolio.positions) { position in
							let p = position.row
							PortfolioRow(
								badge: p.badge, ticker: p.ticker, sub: p.sub,
								amount: p.amount, pct: p.pct, up: p.up,
								action: { onOpenPick(p.ticker) },
								// Authored (1:4548 template): every Sell pill opens
								// the Pick detail of ITS ticker, Instant - the authored
								// sell flow lives there; this page's sheets stay unwired.
								trailing: { SellPill(action: { onOpenPick(p.ticker) }) }
							)
						}
						Text("SOLD · REALIZED")
							.font(StakFont.geist(10 * u, .medium))
							.tracking(0.9 * u)
							.foregroundStyle(Sim.faint)
							.frame(height: 17 * u)
							.frame(maxWidth: .infinity, alignment: .leading)
							.padding(.leading, 2 * u)
						ForEach(portfolio.realized) { r in
							RealizedRow(badge: r.badge, ticker: r.ticker, sub: r.sub, amount: r.amount, up: r.up)
						}
						Text("Sell a pick and the cash returns to your balance, gain or loss.")
							.font(StakFont.geist(11 * u))
							.lineSpacing((14 - 11) * u)
							.foregroundStyle(Sim.faint)
							.frame(maxWidth: .infinity)
					}
					.padding(.horizontal, 20 * u)
					.padding(.top, 6 * u)
					.padding(.bottom, 20 * u)
				}
			}
			if showSell {
				SimSheet(onDismiss: { showSell = false }) {
					SellConfirmSheet(
						// Unwired on this page - the authored NVDA sample.
						pick: PickSpecs.pick("NVDA"),
						onConfirm: { showSell = false; showClosed = true },
						onDismiss: { showSell = false }
					)
				}
			}
			if showClosed {
				SimSheet(onDismiss: { showClosed = false }) {
					PositionClosedSheet(
						pick: PickSpecs.pick("NVDA"),
						onBackToSimulate: { showClosed = false; onBack() },
						onViewPortfolio: { showClosed = false }
					)
				}
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}

private struct FilterChip: View {
	let label: String
	let selected: Bool

	var body: some View {
		let u = figmaUnit
		Text(label)
			.font(StakFont.geist(12 * u, .medium))
			.foregroundStyle(selected ? Sim.teal : Sim.muted)
			.padding(.horizontal, 12 * u)
			.padding(.vertical, 6 * u)
			// Selected chip is fill-only (1:4519) — no teal hairline.
			.background(selected ? Sim.tealTint : Sim.cardBg, in: RoundedRectangle(cornerRadius: 14 * u))
	}
}

/// 60x30 outlined Sell pill — transparent bg with the app's secondary hairline.
private struct SellPill: View {
	let action: () -> Void

	var body: some View {
		let u = figmaUnit
		Button(action: action) {
			Text("Sell")
				.font(StakFont.sora(12 * u, .medium))
				.foregroundStyle(Color(argb: 0xFFDCE7F7))
				.frame(width: 60 * u, height: 30 * u)
				.overlay(
					RoundedRectangle(cornerRadius: 6 * u)
						.strokeBorder(Color(argb: 0x54343B4F), lineWidth: 0.36 * u)
				)
		}
		.buttonStyle(.plain)
	}
}

private struct RealizedRow: View {
	let badge: String
	let ticker: String
	let sub: String
	let amount: String
	let up: Bool

	var body: some View {
		let u = figmaUnit
		HStack(spacing: 12 * u) {
			ZStack {
				Circle().fill(Sim.chipBg)
				Text(badge)
					.font(StakFont.sora(14 * u, .semiBold))
					.foregroundStyle(Sim.badgeInk)
			}
			.frame(width: 36 * u, height: 36 * u)
			VStack(alignment: .leading, spacing: 2 * u) {
				Text(ticker)
					.font(StakFont.sora(12 * u, .medium))
					.foregroundStyle(Color.white)
				Text(sub)
					.font(StakFont.geist(10 * u))
					.foregroundStyle(Sim.muted)
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			Text(amount)
				.font(StakFont.sora(13 * u, .semiBold))
				.foregroundStyle(up ? Sim.green : Sim.red)
		}
		.padding(.horizontal, 14 * u)
		.padding(.vertical, 12 * u)
		.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 12 * u))
	}
}

/// Shared sheet scaffold for the sell flow — scrim + r24 #181f30 sheet.
private struct SimSheet<Content: View>: View {
	let onDismiss: () -> Void
	@ViewBuilder let content: Content

	var body: some View {
		let u = figmaUnit
		ZStack(alignment: .bottom) {
			// Authored scrim: near-black at ~62%.
			Color(argb: 0x9E02050E)
				.ignoresSafeArea()
				.onTapGesture(perform: onDismiss)
			VStack(spacing: 0) {
				// Handle at y10–14, content at y32 → 18 below the rect.
				RoundedRectangle(cornerRadius: 2 * u)
					.fill(Sim.track)
					.frame(width: 40 * u, height: 4 * u)
					.padding(.bottom, 18 * u)
				content
			}
			.padding(.horizontal, 20 * u)
			.padding(.top, 10 * u)
			// Authored 30 to the screen edge — no extra home-indicator inset.
			.padding(.bottom, 30 * u)
			.frame(maxWidth: .infinity)
			.background(Sim.cardBg, in: UnevenRoundedRectangle(topLeadingRadius: 24 * u, topTrailingRadius: 24 * u))
			.ignoresSafeArea(edges: .bottom)
		}
	}
}

/// The pick's row on the sell sheets — teal-tinted, badge + price + change.
/// Authored as the NVDA row (1:4698); Codex parity audit (2026-09-04):
/// serves the tapped pick (PickSpec) into the same row.
private struct PickSellRow: View {
	let pick: PickSpec

	var body: some View {
		let u = figmaUnit
		HStack(spacing: 11 * u) {
			ZStack {
				Circle().fill(Sim.chipBg)
				Text(pick.badge)
					.font(StakFont.sora(15 * u, .semiBold))
					.foregroundStyle(Sim.badgeInk)
			}
			.frame(width: 38 * u, height: 38 * u)
			VStack(alignment: .leading, spacing: 2 * u) {
				Text(pick.company)
					.font(StakFont.geist(13 * u, .medium))
					.foregroundStyle(Color.white)
				Text("\(pick.priceNow) today")
					.font(StakFont.geist(10 * u))
					.foregroundStyle(Sim.muted)
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			Text(pick.dayChange)
				.font(StakFont.geist(12 * u, .medium))
				.foregroundStyle(pick.dayUp ? Sim.green : Sim.red)
		}
		.padding(.horizontal, 14 * u)
		.padding(.vertical, 12 * u)
		.background(Sim.tealTint, in: RoundedRectangle(cornerRadius: 6 * u))
	}
}

/// "Sell NVDA?" confirm ticket (1:4698) — content only; its host (the
/// SellFlowHost morph or a bare SimSheet) provides scrim + sheet.
struct SellConfirmSheet: View {
	/// The pick being sold - NVDA renders the frame's literals verbatim.
	let pick: PickSpec
	let onConfirm: () -> Void
	let onDismiss: () -> Void

	@State private var mode = 0

	var body: some View {
		let u = figmaUnit
		Group {
			VStack(alignment: .leading, spacing: 14 * u) {
				Text("Sell \(pick.symbol)?")
					.font(StakFont.sora(18 * u, .semiBold))
					.foregroundStyle(Color.white)
				PickSellRow(pick: pick)
				// Review (2026-09-04): the pick's own cost basis ("$100" authored).
				Text("You hold \(pick.shares) shares from your \(pick.stakeBasis) stake.")
					.font(StakFont.geist(12 * u))
					.lineSpacing((18 - 12) * u)
					.foregroundStyle(Sim.body)
				HStack(spacing: 6 * u) {
					Text("Position value")
						.font(StakFont.geist(12 * u))
						.foregroundStyle(Sim.muted)
					Text(pick.stakeValue)
						.font(StakFont.geist(12 * u, .medium))
						.foregroundStyle(Sim.bright)
				}
				HStack(spacing: 8 * u) {
					ForEach(Array(["All", "Half", "Custom"].enumerated()), id: \.offset) { i, label in
						let sel = i == mode
						Button { mode = i } label: {
							Text(label)
								.font(StakFont.geist(12 * u, .medium))
								.foregroundStyle(sel ? Color(argb: 0xFFA6E4F7) : Color(argb: 0xFFDCE7F7))
								.frame(maxWidth: .infinity)
								.padding(.vertical, 8 * u)
								.background(
									sel ? Color(argb: 0xFF0F2A38) : Color(argb: 0xFF0B1430),
									in: RoundedRectangle(cornerRadius: 10 * u)
								)
								.overlay(
									RoundedRectangle(cornerRadius: 10 * u)
										.strokeBorder(
											sel ? Color(argb: 0xFF5DA8BF) : Color(argb: 0x1FFFFFFF),
											lineWidth: (sel ? 0.5 : 1) * u
										)
								)
						}
						.buttonStyle(.plain)
					}
				}
				HStack(alignment: .top, spacing: 6 * u) {
					Spacer()
					Text("Returning")
						.font(StakFont.geist(12 * u))
						.foregroundStyle(Sim.muted)
					Text(pick.stakeValue)
						.font(StakFont.sora(15 * u, .semiBold))
						.foregroundStyle(Sim.bright)
					Text("to your cash")
						.font(StakFont.geist(12 * u))
						.foregroundStyle(Sim.muted)
					Spacer()
				}
				VStack(spacing: 16 * u) {
					// Dark navy confirm — #12203e per the frame.
					Button(action: onConfirm) {
						Text("Confirm sell")
							.font(StakFont.geist(14 * u, .medium))
							.foregroundStyle(Color.white)
							.frame(maxWidth: .infinity)
							.frame(height: 52 * u)
							.background(Sim.darkCta, in: RoundedRectangle(cornerRadius: 6 * u))
					}
					.buttonStyle(.plain)
					SimSheetSecondary(text: "Back", action: onDismiss)
				}
			}
		}
	}
}

/// h52 secondary button used inside the sell sheets — faint 4% white
/// fill under the app's 0.36 hairline.
private struct SimSheetSecondary: View {
	let text: String
	let action: () -> Void

	var body: some View {
		let u = figmaUnit
		Button(action: action) {
			Text(text)
				.font(StakFont.sora(14 * u))
				.foregroundStyle(Sim.muted)
				.frame(maxWidth: .infinity)
				.frame(height: 52 * u)
				.background(Color(argb: 0x0AFFFFFF), in: RoundedRectangle(cornerRadius: 6 * u))
				.overlay(
					RoundedRectangle(cornerRadius: 6 * u)
						.strokeBorder(Color(argb: 0x54343B4F), lineWidth: 0.36 * u)
				)
		}
		.buttonStyle(.plain)
	}
}

/// "Position closed" receipt (73:855) — content only; its host (the
/// SellFlowHost morph or a bare SimSheet) provides scrim + sheet.
struct PositionClosedSheet: View {
	/// The pick just sold - NVDA renders the frame's literals verbatim.
	let pick: PickSpec
	let onBackToSimulate: () -> Void
	let onViewPortfolio: () -> Void

	var body: some View {
		let u = figmaUnit
		Group {
			VStack(spacing: 14 * u) {
				Image("IcSheetCheck")
					.resizable()
					.frame(width: 47 * u, height: 47 * u)
				Text("Position closed")
					.font(StakFont.sora(18 * u, .semiBold))
					.foregroundStyle(Color.white)
				PickSellRow(pick: pick)
				Text("Sold \(pick.shares) shares from your \(pick.stakeBasis) stake.")
					.font(StakFont.geist(12 * u))
					.lineSpacing((18 - 12) * u)
					.foregroundStyle(Sim.body)
					.frame(maxWidth: .infinity, alignment: .leading)
				HStack(spacing: 6 * u) {
					Text("Proceeds")
						.font(StakFont.geist(12 * u))
						.foregroundStyle(Sim.muted)
					Text(pick.stakeValue)
						.font(StakFont.geist(12 * u, .medium))
						.foregroundStyle(Sim.bright)
					Spacer()
				}
				// 73:855 centres the Returned line (Proceeds above stays left-aligned).
				HStack(alignment: .top, spacing: 6 * u) {
					Spacer()
					Text("Returned")
						.font(StakFont.geist(12 * u))
						.foregroundStyle(Sim.muted)
					Text(pick.stakeValue)
						.font(StakFont.sora(15 * u, .semiBold))
						.foregroundStyle(Sim.bright)
					Text("to your cash (\(pick.gainSigned))")
						.font(StakFont.geist(12 * u))
						.foregroundStyle(Sim.muted)
					Spacer()
				}
				VStack(spacing: 16 * u) {
					Button(action: onBackToSimulate) {
						Text("Back to Simulate")
							.font(StakFont.geist(14 * u, .medium))
							.foregroundStyle(Color.white)
							.frame(maxWidth: .infinity)
							.frame(height: 52 * u)
							// Authored teal drop shadow — same glow as the Discover
							// deck CTA: #52AAC7 at 9% (23/255), blur 12.28, dy 12.28.
							.background {
								RoundedRectangle(cornerRadius: 6 * u)
									.fill(Color(argb: 0xFF52AAC7))
									.opacity(0.09)
									.blur(radius: 12.28 * u)
									.offset(y: 12.28 * u)
							}
							.background(discCtaGradient, in: RoundedRectangle(cornerRadius: 6 * u))
							.overlay(
								RoundedRectangle(cornerRadius: 6 * u)
									.strokeBorder(Sim.ctaBorder, lineWidth: 0.36 * u)
							)
					}
					.buttonStyle(.plain)
					SimSheetSecondary(text: "View portfolio", action: onViewPortfolio)
				}
			}
		}
	}
}

/// Sell → Position-closed flow, reused by the pick detail. Authored
/// SMART_ANIMATE 350 (1:4698 -> 73:855): ONE sheet stays put while the
/// confirm cross-fades into the receipt and the height eases along.
struct SellFlowHost: View {
	/// The pick this flow sells (PickDetailView's tapped symbol).
	let pick: PickSpec
	let onClose: () -> Void
	/// Authored exits for the receipt CTAs (73:855); nil falls back to onClose.
	var onBackToSimulate: (() -> Void)? = nil
	var onViewPortfolio: (() -> Void)? = nil

	@State private var closed = false

	var body: some View {
		SimSheet(onDismiss: {
			if closed { (onViewPortfolio ?? onClose)() } else { onClose() }
		}) {
			ZStack(alignment: .top) {
				if !closed {
					// Codex audit (2026-09-04): the confirm closes the position in
					// PaperPortfolio exactly once - here, where the receipt appears.
					// Review (2026-09-04): the receipt only follows a real sell.
					SellConfirmSheet(pick: pick, onConfirm: { guard !closed else { return }; if PaperPortfolio.shared.sell(pick.symbol) { closed = true } }, onDismiss: onClose)
						.transition(.opacity)
				} else {
					PositionClosedSheet(
						pick: pick,
						onBackToSimulate: onBackToSimulate ?? onClose,
						onViewPortfolio: onViewPortfolio ?? onClose
					)
					.transition(.opacity)
				}
			}
			.animation(.easeOut(duration: 0.35), value: closed)
		}
	}
}
