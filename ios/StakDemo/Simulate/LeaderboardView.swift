import SwiftUI

/// 07 · Simulate — "Final · Leaderboard · % return" (CHINEDU 1:4124).
/// This-week/All-time chips, RANKED BY RETURN, the highlighted You row
/// (#47, ▲ 12 spots) above the top five and the neighbors around you.
/// Ported from android/ ui/simulate/LeaderboardScreen.kt.
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
		VStack(spacing: 0) {
			HStack {
				AuthBackCircle(action: onBack)
				Spacer()
				Text("Leaderboard")
					.font(StakFont.sora(16, .semiBold))
					.foregroundStyle(Color.white)
				Spacer()
				ZStack {
					Circle().fill(Sim.cardBg)
					Image("IcNewsShare")
						.resizable()
						.frame(width: 17, height: 17)
				}
				.frame(width: 40, height: 40)
			}
			.padding(.horizontal, 18)
			.padding(.vertical, 8)

			ScrollView(showsIndicators: false) {
				VStack(spacing: 10) {
					HStack(spacing: 8) {
						PeriodChip(label: "This week", selected: true)
						PeriodChip(label: "All time", selected: false)
						Spacer()
					}
					HStack {
						Text("RANKED BY RETURN")
							.font(StakFont.geist(10, .medium))
							.tracking(0.9)
							.foregroundStyle(Sim.faint)
						Spacer()
						Text("Trailing 7 days")
							.font(StakFont.geist(10))
							.foregroundStyle(Sim.faint)
					}
					.padding(.top, 4)

					// You — highlighted with the ▲ spots delta.
					HStack(spacing: 10) {
						Text("47")
							.font(StakFont.sora(12, .semiBold))
							.foregroundStyle(Sim.teal)
						ZStack {
							Circle().fill(Sim.chipBg)
							Text("E")
								.font(StakFont.sora(13, .semiBold))
								.foregroundStyle(Sim.badgeInk)
						}
						.frame(width: 32, height: 32)
						VStack(alignment: .leading, spacing: 2) {
							Text("You")
								.font(StakFont.geist(13, .semiBold))
								.foregroundStyle(Color.white)
							Text("12 picks this week")
								.font(StakFont.geist(10))
								.foregroundStyle(Sim.muted)
						}
						.frame(maxWidth: .infinity, alignment: .leading)
						VStack(alignment: .trailing, spacing: 2) {
							Text("+4.2%")
								.font(StakFont.sora(13, .semiBold))
								.foregroundStyle(Sim.teal)
							Text("▲ 12 spots")
								.font(StakFont.geist(10))
								.foregroundStyle(Sim.green)
						}
					}
					.padding(.horizontal, 12)
					.padding(.vertical, 10)
					.background(Sim.tealTint, in: RoundedRectangle(cornerRadius: 12))
					.overlay(RoundedRectangle(cornerRadius: 12).strokeBorder(Color(argb: 0x662C9DBC), lineWidth: 0.75))

					ForEach(top, id: \.rank) { LeaderRow(r: $0) }
					Text("· · ·")
						.font(StakFont.geist(12))
						.foregroundStyle(Sim.faint)
					ForEach(near, id: \.rank) { LeaderRow(r: $0) }
					Text("Percentage return, not dollar size, so everyone competes on the same scale. This week ranks are trailing 7 days.")
						.font(StakFont.geist(10))
						.lineSpacing(15 - 10)
						.foregroundStyle(Sim.faint)
						.frame(maxWidth: .infinity, alignment: .leading)
						.padding(.top, 8)
				}
				.padding(.horizontal, 20)
				.padding(.top, 10)
				.padding(.bottom, 24)
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}

private struct PeriodChip: View {
	let label: String
	let selected: Bool

	var body: some View {
		Text(label)
			.font(StakFont.geist(12, .medium))
			.foregroundStyle(selected ? Sim.teal : Sim.muted)
			.padding(.horizontal, 12)
			.padding(.vertical, 6)
			.background(selected ? Sim.tealTint : Sim.cardBg, in: RoundedRectangle(cornerRadius: 14))
			.overlay(
				selected
					? RoundedRectangle(cornerRadius: 14).strokeBorder(Color(argb: 0x662C9DBC), lineWidth: 0.75)
					: nil
			)
	}
}

private struct LeaderRow: View {
	let r: Rank

	var body: some View {
		HStack(spacing: 10) {
			Text(r.rank)
				.font(StakFont.sora(12, .semiBold))
				.foregroundStyle(Sim.faint)
			ZStack {
				Circle().fill(Sim.chipBg)
				Text(r.initial)
					.font(StakFont.sora(13, .semiBold))
					.foregroundStyle(Sim.badgeInk)
			}
			.frame(width: 32, height: 32)
			VStack(alignment: .leading, spacing: 2) {
				Text(r.name)
					.font(StakFont.geist(13, .medium))
					.foregroundStyle(Color.white)
				Text(r.picks)
					.font(StakFont.geist(10))
					.foregroundStyle(Sim.muted)
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			Text(r.pct)
				.font(StakFont.sora(12, .semiBold))
				.foregroundStyle(Sim.headerGray)
		}
		.padding(.horizontal, 12)
		.padding(.vertical, 10)
		.background(Sim.cardBg, in: RoundedRectangle(cornerRadius: 12))
	}
}
