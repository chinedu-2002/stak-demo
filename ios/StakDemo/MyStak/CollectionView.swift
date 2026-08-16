import SwiftUI

// File-private palette — mirrors the Android CollectionScreen.kt literals verbatim.
private let cardBg = Color(argb: 0xFF181F30)
private let muted = Color(argb: 0xFF819ABB)
private let faint = Color(argb: 0xFF5C6B85)
private let green = Color(argb: 0xFF2FD08A)
private let redDown = Color(argb: 0xFFE5484D)
private let badgeInk = Color(argb: 0xFF9EADC7)

private struct CollStock: Identifiable {
	let badge: String
	let change: String
	let up: Bool
	let ticker: String
	let company: String
	let price: String
	var id: String { ticker }
}

private let stocks = [
	CollStock(badge: "N", change: "▲ 2.4%", up: true, ticker: "NVDA", company: "NVIDIA", price: "$122.10"),
	CollStock(badge: "A", change: "▲ 1.2%", up: true, ticker: "AAPL", company: "Apple", price: "$229.35"),
	CollStock(badge: "M", change: "▼ 0.4%", up: false, ticker: "MSFT", company: "Microsoft", price: "$438.20"),
	CollStock(badge: "G", change: "▲ 0.8%", up: true, ticker: "GOOGL", company: "Alphabet", price: "$178.90"),
	CollStock(badge: "A", change: "▲ 2.1%", up: true, ticker: "AMD", company: "Adv Micro", price: "$164.30")
]

/// The Android `chunked(2)` rows — pairs of tiles, the odd tail row hosts the Add-stock card.
private let stockRows: [[CollStock]] = stride(from: 0, to: stocks.count, by: 2).map {
	Array(stocks[$0..<min($0 + 2, stocks.count)])
}

/// 06 · My STAK — "Collection · Cards A · corrected" (CHINEDU 1:3333).
/// The AI & Tech collection: hero (glass art, title, meta, blurb) and
/// the stock-tile grid with the dashed Add-stock card. Tapping AAPL
/// opens the saved Stock Detail.
/// Ported from android/ ui/mystak/CollectionScreen.kt.
struct CollectionView: View {
	let onBack: () -> Void
	let onOpenStock: () -> Void

	var body: some View {
		VStack(spacing: 0) {
			HStack {
				AuthBackCircle(action: onBack)
				Spacer()
				Text("AI & Tech")
					.font(StakFont.sora(16, .semiBold))
					.foregroundStyle(StakColors.textPrimary)
				Spacer()
				ZStack {
					Circle().fill(cardBg)
					Image("IcMoreDots")
						.resizable()
						.frame(width: 24, height: 24)
				}
				.frame(width: 40, height: 40)
			}
			.padding(.leading, 16)
			.padding(.trailing, 18)
			.padding(.vertical, 8)

			ScrollView {
				VStack(spacing: 20) {
					hero
					grid
				}
				.padding(.horizontal, 20)
				.padding(.top, 16)
				.padding(.bottom, 26)
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}

	private var hero: some View {
		VStack(alignment: .leading, spacing: 10) {
			Image("MsCollAITech")
				.resizable()
				.scaledToFill()
				.frame(width: 60, height: 60)
				.clipped()
			Text("AI & Tech")
				.font(StakFont.sora(26, .semiBold))
				.foregroundStyle(StakColors.textPrimary)
			HStack(spacing: 7) {
				Text("5 stocks")
					.font(StakFont.geist(13))
					.foregroundStyle(muted)
				Text("·")
					.font(StakFont.geist(13))
					.foregroundStyle(faint)
				Text("+2.4% this week")
					.font(StakFont.geist(13, .medium))
					.foregroundStyle(green)
			}
			Text("Your highest-conviction growth and AI names.")
				.font(StakFont.geist(13))
				.foregroundStyle(Color(argb: 0xFFC8D2E0))
		}
		.frame(maxWidth: .infinity, alignment: .leading)
	}

	private var grid: some View {
		VStack(spacing: 10) {
			ForEach(Array(stockRows.enumerated()), id: \.offset) { _, row in
				// `.fixedSize(vertical: true)` = Compose IntrinsicSize.Min: tiles in a
				// row share the tallest tile's height, so the Add card stretches to match.
				HStack(spacing: 10) {
					ForEach(row) { stock in
						StockTile(stock: stock) {
							if stock.ticker == "AAPL" { onOpenStock() }
						}
					}
					if row.count == 1 {
						AddStockTile()
					}
				}
				.fixedSize(horizontal: false, vertical: true)
			}
		}
		.frame(maxWidth: .infinity)
	}
}

private struct StockTile: View {
	let stock: CollStock
	let action: () -> Void

	var body: some View {
		Button(action: action) {
			VStack(alignment: .leading, spacing: 10) {
				HStack {
					ZStack {
						Circle().fill(Color(argb: 0xFF242B3D))
						Text(stock.badge)
							.font(StakFont.sora(14, .semiBold))
							.foregroundStyle(badgeInk)
					}
					.frame(width: 36, height: 36)
					Spacer(minLength: 0)
					Text(stock.change)
						.font(StakFont.geist(12, .medium))
						.foregroundStyle(stock.up ? green : redDown)
				}
				VStack(alignment: .leading, spacing: 2) {
					Text(stock.ticker)
						.font(StakFont.sora(16, .semiBold))
						.foregroundStyle(StakColors.textPrimary)
					Text(stock.company)
						.font(StakFont.geist(11))
						.foregroundStyle(muted)
				}
				Text(stock.price)
					.font(StakFont.sora(15, .medium))
					.foregroundStyle(StakColors.textPrimary)
			}
			.padding(14)
			.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
			.background(cardBg, in: RoundedRectangle(cornerRadius: 16))
		}
		.buttonStyle(.plain)
	}
}

/// Dashed 1.5pt #2a3346 r16 add card.
private struct AddStockTile: View {
	var body: some View {
		VStack(spacing: 8) {
			Image("IcPlusCircle")
				.resizable()
				.frame(width: 24, height: 24)
			Text("Add stock")
				.font(StakFont.geist(13, .medium))
				.foregroundStyle(muted)
		}
		.padding(14)
		.frame(maxWidth: .infinity, maxHeight: .infinity)
		.overlay(
			RoundedRectangle(cornerRadius: 16)
				.stroke(Color(argb: 0xFF2A3346), style: StrokeStyle(lineWidth: 1.5, dash: [8, 8]))
		)
	}
}
