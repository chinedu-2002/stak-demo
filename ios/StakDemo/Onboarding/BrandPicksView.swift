import SwiftUI

private struct Brand: Identifiable {
	let name: String
	let asset: String
	var id: String { name }
}

/// The 12 brand tiles of Figma "Onboarding · 02 Brand picks" (1:232), in grid order.
private let brands: [Brand] = [
	Brand(name: "Apple", asset: "BrandApple"),
	Brand(name: "Tesla", asset: "BrandTesla"),
	Brand(name: "Nike", asset: "BrandNike"),
	Brand(name: "Spotify", asset: "BrandSpotify"),
	Brand(name: "Netflix", asset: "BrandNetflix"),
	Brand(name: "Amazon", asset: "BrandAmazon"),
	Brand(name: "Disney", asset: "BrandDisney"),
	Brand(name: "Microsoft", asset: "BrandMicrosoft"),
	Brand(name: "NVIDIA", asset: "BrandNVIDIA"),
	Brand(name: "PlayStation", asset: "BrandPlayStation"),
	Brand(name: "Coinbase", asset: "BrandCoinbase"),
	Brand(name: "Uber", asset: "BrandUber")
]

/// Onboarding · 02 Brand picks — Figma node 1:232 (CHINEDU file, "STEP 2 OF 6").
///
/// A 3-wide grid of #181f30 tiles, each a white 34pt circle with the real
/// brand mark (flattened Figma exports) and a Geist 11 label. Selected
/// tiles carry a 1.5pt rgba(105,179,202,0.5) border and a white label.
/// The gradient CTA counts the picks ("Continue · N picked"), with the
/// hairline "Back" button and "You can change this later" underneath.
struct BrandPicksView: View {
	let onBack: () -> Void
	let onContinue: () -> Void
	@State private var picked: Set<String> = []

	var body: some View {
		VStack(spacing: 0) {
			HStack {
				AuthBackCircle(action: onBack)
				Spacer()
				StepLabel(text: "STEP 2 OF 6")
			}
			.padding(.horizontal, 20)
			.padding(.top, 10)
			.padding(.bottom, 4)

			ScrollView {
				VStack(alignment: .leading, spacing: 18) {
					VStack(alignment: .leading, spacing: 12) {
						Text("Which brands do you know or use?")
							.font(StakFont.sora(24, .semiBold))
							.lineSpacing(31 - 24)
							.foregroundStyle(StakColors.textPrimary)
						Text("Pick a few. STAK uses this to learn what feels familiar to you.")
							.font(StakFont.geist(12))
							.foregroundStyle(Auth.subtitleGray)
					}

					// 3-wide tile grid, 10pt gaps.
					VStack(spacing: 10) {
						ForEach(0..<4) { row in
							HStack(spacing: 10) {
								ForEach(brands[row * 3..<(row * 3 + 3)]) { brand in
									BrandTile(brand: brand, selected: picked.contains(brand.name)) {
										if picked.contains(brand.name) {
											picked.remove(brand.name)
										} else {
											picked.insert(brand.name)
										}
									}
								}
							}
						}
					}
					.padding(.top, 6)
				}
				.padding(.horizontal, 24)
				.padding(.top, 14)
			}

			VStack(spacing: 10) {
				AuthCta(text: picked.isEmpty ? "Continue" : "Continue · \(picked.count) picked") {
					if !picked.isEmpty { onContinue() }
				}
				AuthSecondaryButton(text: "Back", action: onBack)
				Text("You can change this later")
					.font(StakFont.geist(11))
					.foregroundStyle(Auth.faintText)
			}
			.padding(.top, 8)
			.padding(.bottom, 26)
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}

/// One brand tile — white circle render + label; teal border when selected.
private struct BrandTile: View {
	let brand: Brand
	let selected: Bool
	let action: () -> Void

	var body: some View {
		Button(action: action) {
			VStack(spacing: 7) {
				Image(brand.asset)
					.resizable()
					.frame(width: 34, height: 34)
				Text(brand.name)
					.font(StakFont.geist(11))
					.foregroundStyle(selected ? StakColors.textPrimary : StakColors.muted)
			}
			.frame(maxWidth: .infinity)
			.padding(.top, 13)
			.padding(.bottom, 11)
			.padding(.horizontal, 4)
			.background(Auth.inputBg, in: RoundedRectangle(cornerRadius: 14))
			.overlay {
				if selected {
					RoundedRectangle(cornerRadius: 14)
						.strokeBorder(Color(argb: 0x8069B3CA), lineWidth: 1.5)
				}
			}
		}
		.buttonStyle(.plain)
	}
}
