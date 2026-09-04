import SwiftUI

// File-private palette — mirrors the Android CollectionScreen.kt literals verbatim.
private let cardBg = Color(argb: 0xFF181F30)
private let muted = Color(argb: 0xFF819ABB)
private let faint = Color(argb: 0xFF5C6B85)
private let green = Color(argb: 0xFF2FD08A)
private let redDown = Color(argb: 0xFFE5484D)
private let badgeInk = Color(argb: 0xFF9EADC7)

/// 06 · My STAK — "Collection · Cards A · corrected" (CHINEDU 1:3333).
/// The Collection page: hero (art, title, meta, blurb) and the stock-tile
/// grid with the dashed Add-stock card. Codex parity audit (2026-09-04):
/// the authored AI & Tech frame is the template - it serves whichever
/// collection the Overview chip carried (MyStak/Collections.swift), and
/// tapping a tile opens THAT stock's saved Stock Detail.
/// Every metric is scaled by the 390pt artboard unit (`figmaUnit`),
/// exactly like the Android build's `u` scaling.
/// Ported from android/ ui/mystak/CollectionScreen.kt.
struct CollectionView: View {
	let collection: StakCollection
	let onBack: () -> Void
	let onOpenStock: (String) -> Void

	/// The Android `chunked(2)` rows — pairs of tiles, the odd tail row hosts the Add-stock card.
	private var stockRows: [[CollStock]] {
		let stocks = collection.stocks
		return stride(from: 0, to: stocks.count, by: 2).map {
			Array(stocks[$0..<min($0 + 2, stocks.count)])
		}
	}

	var body: some View {
		let u = figmaUnit
		VStack(spacing: 0) {
			HStack {
				AuthBackCircle(action: onBack)
				Spacer()
				Text(collection.name)
					.font(StakFont.sora(16 * u, .semiBold))
					.foregroundStyle(StakColors.textPrimary)
				Spacer()
				ZStack {
					Circle().fill(cardBg)
					Image("IcMoreDots")
						.resizable()
						.frame(width: 24 * u, height: 24 * u)
				}
				.frame(width: 40 * u, height: 40 * u)
			}
			.padding(.leading, 16 * u)
			.padding(.trailing, 18 * u)
			.padding(.vertical, 8 * u)

			ScrollView {
				VStack(spacing: 20 * u) {
					hero
					grid
				}
				.padding(.horizontal, 20 * u)
				.padding(.top, 16 * u)
				.padding(.bottom, 26 * u)
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}

	private var hero: some View {
		let u = figmaUnit
		return VStack(alignment: .leading, spacing: 10 * u) {
			// The authored 60u art frame. A collection with a category icon
			// instead of glass art centres the chip's own 36u icon in that
			// same frame, so title / meta / blurb keep their authored
			// positions (Codex parity audit, 2026-09-04).
			ZStack {
				if let image = collection.image {
					Image(image)
						.resizable()
						.scaledToFill()
						.frame(width: 60 * u, height: 60 * u)
						.clipped()
				} else if let icon = collection.icon {
					Image(icon)
						.resizable()
						.frame(width: 36 * u, height: 36 * u)
				}
			}
			.frame(width: 60 * u, height: 60 * u)
			Text(collection.name)
				.font(StakFont.sora(26 * u, .semiBold))
				.foregroundStyle(StakColors.textPrimary)
			HStack(spacing: 7 * u) {
				Text(collection.count)
					.font(StakFont.geist(13 * u))
					.foregroundStyle(muted)
				Text("·")
					.font(StakFont.geist(13 * u))
					.foregroundStyle(faint)
				// The week move is authored copy (1:3333) the shared demo
				// data does not define per collection - it stays as drawn.
				Text("+2.4% this week")
					.font(StakFont.geist(13 * u, .medium))
					.foregroundStyle(green)
			}
			Text(collection.blurb)
				.font(StakFont.geist(13 * u))
				.foregroundStyle(Color(argb: 0xFFC8D2E0))
		}
		.frame(maxWidth: .infinity, alignment: .leading)
	}

	private var grid: some View {
		let u = figmaUnit
		return VStack(spacing: 10 * u) {
			ForEach(Array(stockRows.enumerated()), id: \.offset) { _, row in
				// `.fixedSize(vertical: true)` = Compose IntrinsicSize.Min: tiles in a
				// row share the tallest tile's height, so the Add card stretches to match.
				HStack(spacing: 10 * u) {
					ForEach(row) { stock in
						// Authored (1:3375 template): EVERY card opens the saved
						// Stock Detail, Instant - serving the tapped ticker
						// (Codex parity audit, 2026-09-04).
						StockTile(stock: stock) {
							onOpenStock(stock.ticker)
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
		let u = figmaUnit
		Button(action: action) {
			VStack(alignment: .leading, spacing: 10 * u) {
				HStack {
					ZStack {
						Circle().fill(Color(argb: 0xFF242B3D))
						Text(stock.badge)
							.font(StakFont.sora(14 * u, .semiBold))
							.foregroundStyle(badgeInk)
					}
					.frame(width: 36 * u, height: 36 * u)
					Spacer(minLength: 0)
					Text(stock.change)
						.font(StakFont.geist(12 * u, .medium))
						.foregroundStyle(stock.up ? green : redDown)
				}
				VStack(alignment: .leading, spacing: 2 * u) {
					Text(stock.ticker)
						.font(StakFont.sora(16 * u, .semiBold))
						.foregroundStyle(StakColors.textPrimary)
					Text(stock.company)
						.font(StakFont.geist(11 * u))
						.foregroundStyle(muted)
				}
				Text(stock.price)
					.font(StakFont.sora(15 * u, .medium))
					.foregroundStyle(StakColors.textPrimary)
			}
			.padding(14 * u)
			.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
			.background(cardBg, in: RoundedRectangle(cornerRadius: 16 * u))
		}
		.buttonStyle(.plain)
	}
}

/// Dashed 1.5u #2a3346 r16 add card — 6 on / 5 off dash pattern.
private struct AddStockTile: View {
	var body: some View {
		let u = figmaUnit
		VStack(spacing: 8 * u) {
			Image("IcPlusCircle")
				.resizable()
				.frame(width: 24 * u, height: 24 * u)
			Text("Add stock")
				.font(StakFont.geist(13 * u, .medium))
				.foregroundStyle(muted)
		}
		.padding(14 * u)
		.frame(maxWidth: .infinity, maxHeight: .infinity)
		.overlay(
			RoundedRectangle(cornerRadius: 16 * u)
				.stroke(Color(argb: 0xFF2A3346), style: StrokeStyle(lineWidth: 1.5 * u, dash: [6 * u, 5 * u]))
		)
	}
}
