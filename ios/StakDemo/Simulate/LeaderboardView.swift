import SwiftUI

/// 07 · Simulate — "Final · Leaderboard · % return" (CHINEDU 1:4124).
/// This-week/All-time chips, RANKED BY RETURN, the highlighted You row
/// (#47, ▲ 12 spots) above the top five and the neighbors around you.
/// Ported from android/ ui/simulate/LeaderboardScreen.kt. Every metric
/// is scaled by the 390pt artboard unit (`figmaUnit`), exactly like
/// the Android build's `u` scaling.
private struct Rank {
	let rank: String
	let initial: String
	let name: String
	let picks: String
	let pct: String
}

private let top: [Rank] = [
	Rank(rank: "1", initial: "M", name: "Maya A.", picks: "9 picks", pct: "+9.4%"),
	Rank(rank: "2", initial: "J", name: "Jide O.", picks: "14 picks", pct: "+8.8%"),
	Rank(rank: "3", initial: "T", name: "Tunde K.", picks: "7 picks", pct: "+8.1%"),
	Rank(rank: "4", initial: "Z", name: "Zara M.", picks: "22 picks", pct: "+7.6%"),
	Rank(rank: "5", initial: "K", name: "Kofi B.", picks: "11 picks", pct: "+7.0%")
]
private let near: [Rank] = [
	Rank(rank: "46", initial: "L", name: "Lena S.", picks: "8 picks", pct: "+4.3%"),
	Rank(rank: "48", initial: "D", name: "Dami F.", picks: "15 picks", pct: "+4.1%")
]

struct LeaderboardView: View {
	let onBack: () -> Void

	var body: some View {
		let u = figmaUnit
		VStack(spacing: 0) {
			HStack {
				AuthBackCircle(action: onBack)
				Spacer()
				Text("Leaderboard")
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
					HStack(spacing: 8 * u) {
						PeriodChip(label: "This week", selected: true)
						PeriodChip(label: "All time", selected: false)
					}
					HStack {
						Text("RANKED BY RETURN")
							.font(StakFont.geist(10 * u, .medium))
							.tracking(0.9 * u)
							.foregroundStyle(Sim.faint)
							.padding(.leading, 2 * u)
						Spacer()
						Text("Trailing 7 days")
							.font(StakFont.geist(10 * u))
							.foregroundStyle(Sim.faint)
					}

					// You — highlighted with the ▲ spots delta. Teal-tint bg
					// only, no border (1:4124).
					HStack(spacing: 11 * u) {
						Text("47")
							.font(StakFont.sora(16 * u, .semiBold))
							.foregroundStyle(Sim.teal)
							.frame(width: 28 * u, alignment: .leading)
						ZStack {
							Circle().fill(Sim.chipBg)
							Text("E")
								.font(StakFont.sora(13 * u, .semiBold))
								.foregroundStyle(Sim.badgeInk)
						}
						.frame(width: 36 * u, height: 36 * u)
						VStack(alignment: .leading, spacing: 2 * u) {
							Text("You")
								.font(StakFont.geist(12 * u, .semiBold))
								.foregroundStyle(Color.white)
							Text("12 picks this week")
								.font(StakFont.geist(10 * u))
								.foregroundStyle(Sim.muted)
						}
						.frame(maxWidth: .infinity, alignment: .leading)
						VStack(alignment: .trailing, spacing: 2 * u) {
							Text("+4.2%")
								.font(StakFont.sora(13 * u, .semiBold))
								.foregroundStyle(Sim.teal)
							Text("▲ 12 spots")
								.font(StakFont.geist(10 * u))
								.foregroundStyle(Sim.green)
						}
					}
					.padding(.horizontal, 16 * u)
					.padding(.vertical, 14 * u)
					.background(Sim.tealTint, in: RoundedRectangle(cornerRadius: 12 * u))

					ForEach(top, id: \.rank) { LeaderRow(r: $0) }
					Text("· · ·")
						.font(StakFont.geist(12 * u))
						.foregroundStyle(Sim.faint)
						.frame(maxWidth: .infinity)
					ForEach(near, id: \.rank) { LeaderRow(r: $0) }
					Text("Percentage return, not dollar size, so everyone competes on the same scale. This week ranks the trailing 7 days.")
						.font(StakFont.geist(11 * u))
						.lineSpacing((16 - 11) * u)
						.foregroundStyle(Sim.faint)
						// 1:4124 centres the explainer across the full column width.
						.multilineTextAlignment(.center)
						.frame(maxWidth: .infinity)
				}
				.padding(.horizontal, 20 * u)
				.padding(.top, 6 * u)
				.padding(.bottom, 24 * u)
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}

private struct PeriodChip: View {
	let label: String
	let selected: Bool

	var body: some View {
		let u = figmaUnit
		Text(label)
			.font(StakFont.geist(12 * u, .medium))
			.foregroundStyle(selected ? Sim.teal : Sim.muted)
			.padding(.horizontal, 13 * u)
			.padding(.vertical, 7 * u)
			.background(selected ? Sim.tealTint : Sim.cardBg, in: RoundedRectangle(cornerRadius: 14 * u))
			.overlay(
				selected
					? RoundedRectangle(cornerRadius: 14 * u).strokeBorder(Color(argb: 0x662C9DBC), lineWidth: 0.75 * u)
					: nil
			)
	}
}

private struct LeaderRow: View {
	let r: Rank

	var body: some View {
		let u = figmaUnit
		HStack(spacing: 11 * u) {
			Text(r.rank)
				.font(StakFont.sora(12 * u, .semiBold))
				.foregroundStyle(Sim.faint)
				.frame(width: 24 * u, alignment: .leading)
			ZStack {
				Circle().fill(Sim.chipBg)
				Text(r.initial)
					.font(StakFont.sora(13 * u, .semiBold))
					.foregroundStyle(Sim.badgeInk)
			}
			.frame(width: 32 * u, height: 32 * u)
			VStack(alignment: .leading, spacing: 2 * u) {
				Text(r.name)
					.font(StakFont.geist(12 * u, .medium))
					.foregroundStyle(Color.white)
				Text(r.picks)
					.font(StakFont.geist(10 * u))
					.foregroundStyle(Sim.muted)
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			Text(r.pct)
				.font(StakFont.sora(12 * u, .semiBold))
				.foregroundStyle(Sim.headerGray)
		}
		.padding(.horizontal, 14 * u)
		.padding(.vertical, 11 * u)
		.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 12 * u))
	}
}
