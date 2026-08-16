import SwiftUI

// File-private palette — mirrors the Android MyStakScreen.kt literals verbatim.
private let cardBg = Color(argb: 0xFF181F30)
private let muted = Color(argb: 0xFF819ABB)
private let faint = Color(argb: 0xFF5C6B85)
private let bodyColor = Color(argb: 0xFFC8D2E0)
private let green = Color(argb: 0xFF2FD08A)
private let red = Color(argb: 0xFFFF5A6A)
private let teal = Color(argb: 0xFF69B3CA)
private let ink = Color(argb: 0xFF0E162B)
private let track = Color(argb: 0xFF2A3346)
private let headerGray = Color(argb: 0xFFD3D3DD)

private let ctaGradient = LinearGradient(
	stops: [
		.init(color: Color(argb: 0xFFA6E4F7), location: 0.0889),
		.init(color: Color(argb: 0xFF5DA8BF), location: 0.3919),
		.init(color: Color(argb: 0xFF3C98B4), location: 0.7255),
		.init(color: Color(argb: 0xFF3C98B4), location: 1)
	],
	startPoint: .top,
	endPoint: .bottom
)

/// 06 · My STAK — "My STAK Overview · corrected" (CHINEDU 1:3155).
/// Collections grid with the Add more CTA, the "Your read" insight,
/// the performance summary (chart, range pills, best/worst), the
/// Breakdown allocation donut with sector bars, and the teal
/// "More like your STAK" Discover banner. Tab bar via MainTabsView.
/// Ported from android/ ui/mystak/MyStakScreen.kt.
struct MyStakView: View {
	let onOpenCollection: () -> Void
	let onStartSwiping: () -> Void

	var body: some View {
		VStack(spacing: 0) {
			VStack(alignment: .leading, spacing: 4) {
				Text("My STAK")
					.font(StakFont.sora(26, .semiBold))
					.foregroundStyle(StakColors.textPrimary)
				Text("Your saved stocks, live.")
					.font(StakFont.geist(13))
					.foregroundStyle(muted)
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			.padding(.horizontal, 20)
			.padding(.top, 20)

			ScrollView {
				VStack(spacing: 20) {
					SectionHeader(title: "Collections")
					collectionsGrid
					addMoreCta
					yourReadCard
					PortfolioSummary()
					SectionHeader(title: "Breakdown")
					AllocationCard()
					discoverBanner
					// Mirrors the Android trailing 0dp spacer — buys one extra 20pt gap.
					Color.clear.frame(height: 0)
				}
				.padding(.horizontal, 20)
				.padding(.top, 20)
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}

	private var collectionsGrid: some View {
		VStack(spacing: 10) {
			HStack(spacing: 10) {
				CollectionChip(name: "AI & Tech", count: "5 stocks", image: "MsCollAITech", action: onOpenCollection)
				CollectionChip(name: "Finance", count: "3 stocks", image: "MsCollFinance")
			}
			HStack(spacing: 10) {
				CollectionChip(name: "Green Energy", count: "3 stocks", icon: "IcCatGreen")
				CollectionChip(name: "Real Estate", count: "2 stocks", icon: "IcCatRealEstate")
			}
			HStack(spacing: 10) {
				CollectionChip(name: "Healthcare", count: "4 stocks", icon: "IcCatHealth")
				CollectionChip(name: "Consumer", count: "2 stocks", icon: "IcCatConsumer")
			}
		}
	}

	private var addMoreCta: some View {
		Button(action: onStartSwiping) {
			HStack(spacing: 8) {
				Text("Add more")
					.font(StakFont.geist(14, .medium))
					.foregroundStyle(StakColors.textPrimary)
				Image("IcPlusSmall")
					.resizable()
					.frame(width: 14, height: 14)
			}
			.frame(width: 150, height: 52)
			.background(ctaGradient, in: RoundedRectangle(cornerRadius: 6))
			.overlay(
				RoundedRectangle(cornerRadius: 6)
					.strokeBorder(StakColors.ctaBorder, lineWidth: 0.36)
			)
		}
		.buttonStyle(.plain)
	}

	/// Your read insight card.
	private var yourReadCard: some View {
		VStack(alignment: .leading, spacing: 7) {
			HStack(spacing: 8) {
				Image("IcGistSparkle")
					.resizable()
					.frame(width: 24, height: 24)
				Text("Your read")
					.font(StakFont.sora(14, .semiBold))
					.foregroundStyle(StakColors.textPrimary)
			}
			Text("You lean into growth and tech.")
				.font(StakFont.sora(15, .semiBold))
				.foregroundStyle(StakColors.textPrimary)
			Text("Six of your fourteen picks are tech or AI names. Your STAK skews high-growth, with a small hedge in real estate.")
				.font(StakFont.geist(13))
				.lineSpacing(19 - 13)
				.foregroundStyle(bodyColor)
		}
		.frame(maxWidth: .infinity, alignment: .leading)
		.padding(16)
		.background(cardBg, in: RoundedRectangle(cornerRadius: 14))
	}

	/// Discover banner.
	private var discoverBanner: some View {
		Button(action: onStartSwiping) {
			VStack(alignment: .leading, spacing: 9) {
				Text("DISCOVER")
					.font(StakFont.geist(10, .medium))
					.tracking(0.6)
					.foregroundStyle(ink)
				Text("More like your STAK")
					.font(StakFont.sora(18, .semiBold))
					.foregroundStyle(ink)
				Text("Based on your taste, 8 fresh picks are waiting in the deck.")
					.font(StakFont.geist(12))
					.lineSpacing(17 - 12)
					.foregroundStyle(ink)
				HStack(spacing: 4) {
					Text("Start swiping")
						.font(StakFont.geist(13, .medium))
						.foregroundStyle(Color(argb: 0xB80A1020))
					Text("›")
						.font(StakFont.geist(14, .medium))
						.foregroundStyle(ink)
				}
				.padding(.top, 4)
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			.padding(18)
			.background(teal, in: RoundedRectangle(cornerRadius: 16))
		}
		.buttonStyle(.plain)
	}
}

private struct SectionHeader: View {
	let title: String

	var body: some View {
		Text(title)
			.font(StakFont.sora(16, .semiBold))
			.foregroundStyle(headerGray)
			.frame(maxWidth: .infinity, alignment: .leading)
	}
}

/// One collection chip — art/icon, name + count, chevron (#181f30 r12).
private struct CollectionChip: View {
	let name: String
	let count: String
	var image: String? = nil
	var icon: String? = nil
	var action: () -> Void = {}

	var body: some View {
		Button(action: action) {
			HStack(spacing: 10) {
				if let image {
					Image(image)
						.resizable()
						.scaledToFill()
						.frame(width: 34, height: 34)
						.clipped()
				} else if let icon {
					Image(icon)
						.resizable()
						.frame(width: 36, height: 36)
				}
				VStack(alignment: .leading, spacing: 2) {
					Text(name)
						.font(StakFont.sora(13, .semiBold))
						.foregroundStyle(StakColors.textPrimary)
						.lineLimit(1)
					Text(count)
						.font(StakFont.geist(11))
						.foregroundStyle(muted)
				}
				.frame(maxWidth: .infinity, alignment: .leading)
				Text("›")
					.font(StakFont.geist(16))
					.foregroundStyle(faint)
			}
			.padding(12)
			.background(cardBg, in: RoundedRectangle(cornerRadius: 12))
		}
		.buttonStyle(.plain)
	}
}

/// Performance this week — +4.9%, chart, range pills, best/worst.
private struct PortfolioSummary: View {
	var body: some View {
		VStack(spacing: 14) {
			VStack(alignment: .leading, spacing: 8) {
				Text("Performance this week")
					.font(StakFont.sora(12))
					.foregroundStyle(muted)
				Text("+4.9%")
					.font(StakFont.sora(44, .semiBold))
					.tracking(-0.44)
					.foregroundStyle(StakColors.textPrimary)
				HStack(spacing: 10) {
					Text("Across 14 stocks")
						.font(StakFont.geist(14, .medium))
						.foregroundStyle(green)
					Text(".")
						.font(StakFont.sora(14))
						.foregroundStyle(muted)
					Text("3M")
						.font(StakFont.geist(14))
						.foregroundStyle(muted)
				}
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			.padding(.leading, 20)

			Image("MsChartLine")
				.resizable()
				.scaledToFit()
				.frame(width: 345, height: 76)

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
			.padding(.top, 26)

			Rectangle()
				.fill(track)
				.frame(maxWidth: .infinity)
				.frame(height: 1)

			HStack(spacing: 151) {
				VStack(alignment: .leading, spacing: 3) {
					Text("Best this week")
						.font(StakFont.geist(11))
						.foregroundStyle(faint)
					HStack(spacing: 6) {
						Text("TSLA")
							.font(StakFont.sora(13, .semiBold))
							.foregroundStyle(StakColors.textPrimary)
						Text("+3.4%")
							.font(StakFont.geist(12, .medium))
							.foregroundStyle(green)
					}
				}
				VStack(alignment: .leading, spacing: 3) {
					Text("Worst")
						.font(StakFont.geist(11))
						.foregroundStyle(faint)
					HStack(spacing: 6) {
						Text("SNOW")
							.font(StakFont.sora(13, .semiBold))
							.foregroundStyle(StakColors.textPrimary)
						Text("-0.5%")
							.font(StakFont.geist(12, .medium))
							.foregroundStyle(red)
					}
				}
			}
			.frame(maxWidth: .infinity)
		}
		.frame(maxWidth: .infinity)
		.padding(.vertical, 18)
		.background(cardBg, in: RoundedRectangle(cornerRadius: 8))
	}
}

/// Allocation — the 150pt donut render + sector bars.
private struct AllocationCard: View {
	var body: some View {
		VStack(spacing: 16) {
			Text("Allocation")
				.font(StakFont.sora(15, .semiBold))
				.foregroundStyle(StakColors.textPrimary)
			Image("MsDonut")
				.resizable()
				.frame(width: 150, height: 150)
			VStack(spacing: 12) {
				SectorBar(name: "Tech & AI", share: "42% · 6 stocks", color: teal, fill: 132)
				SectorBar(name: "Finance", share: "21% · 3 stocks", color: Color(argb: 0xFF7AB3F0), fill: 66)
				SectorBar(name: "Green Energy", share: "20% · 3 stocks", color: green, fill: 63)
				SectorBar(name: "Real Estate", share: "12% · 2 stocks", color: Color(argb: 0xFF9E8CE5), fill: 38)
				SectorBar(name: "Other", share: "5% · 1 stock", color: faint, fill: 16)
			}
			.frame(maxWidth: .infinity)
		}
		.frame(maxWidth: .infinity)
		.padding(18)
		.background(cardBg, in: RoundedRectangle(cornerRadius: 16))
	}
}

private struct SectorBar: View {
	let name: String
	let share: String
	let color: Color
	/// Exact Figma fill width in pt.
	let fill: CGFloat

	var body: some View {
		VStack(spacing: 6) {
			HStack(spacing: 0) {
				Circle()
					.fill(color)
					.frame(width: 9, height: 9)
				Spacer().frame(width: 8)
				Text(name)
					.font(StakFont.geist(13))
					.foregroundStyle(StakColors.textPrimary)
				Spacer(minLength: 0)
				Text(share)
					.font(StakFont.geist(12, .medium))
					.foregroundStyle(muted)
			}
			ZStack(alignment: .leading) {
				RoundedRectangle(cornerRadius: 4)
					.fill(track)
				RoundedRectangle(cornerRadius: 4)
					.fill(color)
					.frame(width: fill)
			}
			.frame(maxWidth: .infinity)
			.frame(height: 7)
		}
	}
}
