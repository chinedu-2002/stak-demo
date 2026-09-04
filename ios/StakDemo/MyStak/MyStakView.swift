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
/// Every metric is scaled by the 390pt artboard unit (`figmaUnit`),
/// exactly like the Android build's `u` scaling.
/// Ported from android/ ui/mystak/MyStakScreen.kt.
struct MyStakView: View {
	/// Receives the tapped chip's collection id (MyStak/Collections.swift).
	let onOpenCollection: (String) -> Void
	let onStartSwiping: () -> Void
	/// Codex audit (2026-09-04): every count on this page - chips, "Across
	/// N stocks", Allocation - derives from the holdings store.
	@ObservedObject private var holdings = MyStakHoldings.shared

	var body: some View {
		let u = figmaUnit
		VStack(spacing: 0) {
			VStack(alignment: .leading, spacing: 4 * u) {
				Text("My STAK")
					.font(StakFont.sora(26 * u, .semiBold))
					.foregroundStyle(StakColors.textPrimary)
				Text("Your saved stocks, live.")
					.font(StakFont.geist(13 * u))
					.foregroundStyle(muted)
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			.padding(.horizontal, 20 * u)
			.padding(.top, 20 * u)

			ScrollView {
				VStack(spacing: 20 * u) {
					SectionHeader(title: "Collections")
					collectionsGrid
					addMoreCta
					yourReadCard
					PortfolioSummary(stockCount: holdings.count)
					SectionHeader(title: "Breakdown")
					AllocationCard(buckets: allocationBuckets(holdings.tickers))
					discoverBanner
					// Mirrors the Android trailing 0dp spacer — buys one extra 20u gap.
					Color.clear.frame(height: 0)
				}
				.padding(.horizontal, 20 * u)
				.padding(.top, 20 * u)
			}
		}
		.background(StakColors.bg.ignoresSafeArea())
	}

	private var collectionsGrid: some View {
		let u = figmaUnit
		// Authored (1:3180 template): EVERY collection card opens the
		// Collection screen, Instant. Codex parity audit (2026-09-04): each
		// chip carries its own collection id so the page serves the tapped
		// one; the six catalogue chips fill the authored three rows of two.
		let all = StakCollections.all
		let rows = stride(from: 0, to: all.count, by: 2).map {
			Array(all[$0..<min($0 + 2, all.count)])
		}
		return VStack(spacing: 10 * u) {
			ForEach(Array(rows.enumerated()), id: \.offset) { _, row in
				HStack(spacing: 10 * u) {
					ForEach(row) { collection in
						CollectionChip(
							collection: collection,
							action: { onOpenCollection(collection.id) },
							countLabel: heldCountLabel(collection.held(in: holdings.tickers).count)
						)
					}
				}
			}
		}
	}

	private var addMoreCta: some View {
		let u = figmaUnit
		return Button(action: onStartSwiping) {
			HStack(spacing: 8 * u) {
				// Figma 1:3155 sets 14 (Codex parity audit, 2026-09-04).
				Text("Add more")
					.font(StakFont.geist(14 * u, .medium))
					.foregroundStyle(StakColors.textPrimary)
				Image("IcPlusSmall")
					.resizable()
					.frame(width: 14 * u, height: 14 * u)
			}
			.frame(width: 150 * u, height: 52 * u)
			.background(ctaGradient, in: RoundedRectangle(cornerRadius: 6 * u))
			.overlay(
				RoundedRectangle(cornerRadius: 6 * u)
					.strokeBorder(StakColors.ctaBorder, lineWidth: 0.36 * u)
			)
		}
		.buttonStyle(.plain)
	}

	/// Codex audit (2026-09-04) - the authored "six of fourteen" was a third
	/// count that agreed with nothing; the sentence now reads the store:
	/// "Five of your twenty-one picks" at rest.
	private var yourReadBody: String {
		let tech = numberWord(StakCollections.collection("aitech").held(in: holdings.tickers).count)
		return "\(tech.prefix(1).uppercased() + String(tech.dropFirst())) of your \(numberWord(holdings.count)) picks are tech or AI names. Your STAK skews high-growth, with a small hedge in real estate."
	}

	/// Your read insight card.
	private var yourReadCard: some View {
		let u = figmaUnit
		return VStack(alignment: .leading, spacing: 7 * u) {
			HStack(spacing: 8 * u) {
				Image("IcGistSparkle")
					.resizable()
					.frame(width: 24 * u, height: 24 * u)
				Text("Your read")
					.font(StakFont.sora(14 * u, .semiBold))
					.foregroundStyle(StakColors.textPrimary)
			}
			Text("You lean into growth and tech.")
				.font(StakFont.sora(15 * u, .semiBold))
				.foregroundStyle(StakColors.textPrimary)
			// Figma 1:3155 sets the body at 13 (Codex parity audit, 2026-09-04).
			Text(yourReadBody)
				.font(StakFont.geist(13 * u))
				.lineSpacing((19 - 13) * u)
				.foregroundStyle(bodyColor)
		}
		.frame(maxWidth: .infinity, alignment: .leading)
		.padding(16 * u)
		.background(cardBg, in: RoundedRectangle(cornerRadius: 14 * u))
	}

	/// Discover banner.
	private var discoverBanner: some View {
		let u = figmaUnit
		return Button(action: onStartSwiping) {
			VStack(alignment: .leading, spacing: 9 * u) {
				Text("DISCOVER")
					.font(StakFont.geist(10 * u, .medium))
					.tracking(0.6 * u)
					.foregroundStyle(ink)
				Text("More like your STAK")
					.font(StakFont.sora(18 * u, .semiBold))
					.foregroundStyle(ink)
				Text("Based on your taste, 8 fresh picks are waiting in the deck.")
					.font(StakFont.geist(13 * u))
					.lineSpacing((17 - 13) * u)
					.foregroundStyle(ink)
				// The Android row's 22sp line height makes it a 22u-tall strip.
				HStack(spacing: 4 * u) {
					Text("Start swiping")
						.font(StakFont.geist(13 * u, .medium))
						.foregroundStyle(Color(argb: 0xB80A1020))
					Text("›")
						.font(StakFont.geist(14 * u, .medium))
						.foregroundStyle(ink)
				}
				.frame(height: 22 * u)
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			.padding(18 * u)
			.background(teal, in: RoundedRectangle(cornerRadius: 16 * u))
		}
		.buttonStyle(.plain)
	}
}

private struct SectionHeader: View {
	let title: String

	var body: some View {
		Text(title)
			.font(StakFont.sora(16 * figmaUnit, .semiBold))
			.foregroundStyle(headerGray)
			.frame(maxWidth: .infinity, alignment: .leading)
	}
}

/// One collection chip — art/icon, name + count, chevron (#181f30 r12).
private struct CollectionChip: View {
	let collection: StakCollection
	let action: () -> Void
	/// The held count from the store - never the authored `collection.count`.
	var countLabel: String = ""

	var body: some View {
		let u = figmaUnit
		Button(action: action) {
			HStack(spacing: 10 * u) {
				if let image = collection.image {
					Image(image)
						.resizable()
						.scaledToFill()
						.frame(width: 34 * u, height: 34 * u)
						.clipped()
				} else if let icon = collection.icon {
					Image(icon)
						.resizable()
						.frame(width: 36 * u, height: 36 * u)
				}
				VStack(alignment: .leading, spacing: 2 * u) {
					// Authored: "Green Energy" (box 90) overflows its 84 column —
					// the frame draws it past the column, so never wrap or clip it
					// (Kotlin softWrap = false + TextOverflow.Visible).
					Text(collection.name)
						.font(StakFont.sora(13 * u, .semiBold))
						.foregroundStyle(StakColors.textPrimary)
						.lineLimit(1)
						.fixedSize(horizontal: true, vertical: false)
					Text(countLabel)
						.font(StakFont.geist(11 * u))
						.foregroundStyle(muted)
				}
				.frame(maxWidth: .infinity, alignment: .leading)
				Text("›")
					.font(StakFont.geist(16 * u))
					.foregroundStyle(faint)
			}
			.padding(12 * u)
			.background(cardBg, in: RoundedRectangle(cornerRadius: 12 * u))
		}
		.buttonStyle(.plain)
	}
}

/// Performance this week — +4.9%, chart, range pills, best/worst.
private struct PortfolioSummary: View {
	/// The holdings count - the authored "Across 14 stocks" now follows the store.
	var stockCount: Int = 0

	var body: some View {
		let u = figmaUnit
		VStack(spacing: 14 * u) {
			VStack(alignment: .leading, spacing: 8 * u) {
				Text("Performance this week")
					.font(StakFont.sora(12 * u))
					.foregroundStyle(muted)
				Text("+4.9%")
					.font(StakFont.sora(44 * u, .semiBold))
					.tracking(-0.44 * u)
					.foregroundStyle(StakColors.textPrimary)
				HStack(spacing: 10 * u) {
					Text("Across \(stockCount) stocks")
						.font(StakFont.geist(14 * u, .medium))
						.foregroundStyle(green)
					Text(".")
						.font(StakFont.sora(14 * u))
						.foregroundStyle(muted)
					Text("3M")
						.font(StakFont.geist(14 * u))
						.foregroundStyle(muted)
				}
			}
			.frame(maxWidth: .infinity, alignment: .leading)
			.padding(.leading, 20 * u)

			Image("MsChartLine")
				.resizable()
				.scaledToFit()
				.frame(width: 343 * u, height: 73.56 * u)

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
			.padding(.top, 26 * u)

			Rectangle()
				.fill(track)
				.frame(maxWidth: .infinity)
				.frame(height: 1 * u)

			HStack(spacing: 151 * u) {
				VStack(alignment: .leading, spacing: 3 * u) {
					Text("Best this week")
						.font(StakFont.geist(11 * u))
						.foregroundStyle(faint)
					HStack(spacing: 6 * u) {
						Text("TSLA")
							.font(StakFont.sora(13 * u, .semiBold))
							.foregroundStyle(StakColors.textPrimary)
						Text("+3.4%")
							.font(StakFont.geist(12 * u, .medium))
							.foregroundStyle(green)
					}
				}
				VStack(alignment: .leading, spacing: 3 * u) {
					Text("Worst")
						.font(StakFont.geist(11 * u))
						.foregroundStyle(faint)
					HStack(spacing: 6 * u) {
						Text("SNOW")
							.font(StakFont.sora(13 * u, .semiBold))
							.foregroundStyle(StakColors.textPrimary)
						Text("-0.5%")
							.font(StakFont.geist(12 * u, .medium))
							.foregroundStyle(red)
					}
				}
			}
			.frame(maxWidth: .infinity)
		}
		.frame(maxWidth: .infinity)
		.padding(.vertical, 13.5 * u)
		.background(cardBg, in: RoundedRectangle(cornerRadius: 8 * u))
	}
}

/// One Allocation bucket - the ring segment and the sector bar share it.
private struct AllocBucket: Identifiable {
	let name: String
	let color: Color
	let count: Int
	var pct: Int
	var id: String { name }
}

/// Codex audit (2026-09-04): the five authored sectors (1:3155) derived
/// from the holdings - Tech & AI / Finance / Green Energy / Real Estate
/// are their collection's held count, Other = Healthcare + Consumer + any
/// held ticker no collection lists (TSLA, SNOW). Zero buckets are skipped;
/// the last non-zero bucket absorbs the rounding remainder so the percents
/// sum to 100. Mirrors android ui/mystak/MyStakScreen.kt allocationBuckets.
private func allocationBuckets(_ held: Set<String>) -> [AllocBucket] {
	func heldIn(_ id: String) -> Int { StakCollections.collection(id).held(in: held).count }
	let listed = Set(StakCollections.all.flatMap { $0.stocks.map(\.ticker) })
	let unlisted = held.subtracting(listed).count
	let raw: [(name: String, color: Color, count: Int)] = [
		("Tech & AI", teal, heldIn("aitech")),
		("Finance", Color(argb: 0xFF7AB3F0), heldIn("finance")),
		("Green Energy", green, heldIn("green")),
		("Real Estate", Color(argb: 0xFF9E8CE5), heldIn("realestate")),
		("Other", faint, heldIn("health") + heldIn("consumer") + unlisted)
	]
	let nonZero = raw.filter { $0.count > 0 }
	let total = nonZero.reduce(0) { $0 + $1.count }
	guard total > 0 else { return [] }
	var buckets = nonZero.map {
		AllocBucket(name: $0.name, color: $0.color, count: $0.count, pct: Int((Double($0.count) / Double(total) * 100).rounded()))
	}
	let rounded = buckets.dropLast().reduce(0) { $0 + $1.pct }
	buckets[buckets.count - 1].pct = 100 - rounded
	return buckets
}

/// Allocation — the drawn ring + sector bars, both from the buckets.
private struct AllocationCard: View {
	var buckets: [AllocBucket] = []

	var body: some View {
		let u = figmaUnit
		VStack(spacing: 16 * u) {
			Text("Allocation")
				.font(StakFont.sora(15 * u, .semiBold))
				.foregroundStyle(StakColors.textPrimary)
			AllocationRing(buckets: buckets)
			VStack(spacing: 12 * u) {
				// The authored 132/314 fill IS 42% - each bar is its percent
				// of the 314u track (Codex audit 2026-09-04).
				ForEach(buckets) { b in
					SectorBar(
						name: b.name,
						share: "\(b.pct)% · \(heldCountLabel(b.count))",
						color: b.color,
						fill: 314 * CGFloat(b.pct) / 100
					)
				}
			}
			.frame(maxWidth: .infinity)
		}
		.frame(maxWidth: .infinity)
		.padding(18 * u)
		.background(cardBg, in: RoundedRectangle(cornerRadius: 16 * u))
	}
}

/// Codex audit (2026-09-04): the baked MsDonut render is gone - it could
/// not follow the store. A drawn ring in the same 150u box: 22u stroke,
/// butt caps, segments in bucket order from -90 degrees clockwise with
/// 2-degree gaps between them; a single bucket draws one full ring.
/// Mirrors android ui/mystak/MyStakScreen.kt AllocationRing.
private struct AllocationRing: View {
	let buckets: [AllocBucket]

	private struct Segment: Identifiable {
		let id: String
		let color: Color
		let start: Double
		let end: Double
	}

	/// Each bucket spans pct * 3.6 degrees; the 2-degree gap is trimmed
	/// off its tail so the first segment starts exactly at -90.
	private var segments: [Segment] {
		var cursor = -90.0
		var out: [Segment] = []
		for b in buckets {
			let sweep = Double(b.pct) * 3.6
			out.append(Segment(id: b.id, color: b.color, start: cursor, end: cursor + sweep - 2))
			cursor += sweep
		}
		return out
	}

	var body: some View {
		let u = figmaUnit
		// Stroke centred 11u inside the box so the 22u ring's outer edge sits on the 150u frame.
		let inset = 11 * u
		ZStack {
			if buckets.count == 1 {
				Circle()
					.inset(by: inset)
					.stroke(buckets[0].color, style: StrokeStyle(lineWidth: 22 * u, lineCap: .butt))
			} else {
				ForEach(segments) { seg in
					RingArc(start: seg.start, end: seg.end, inset: inset)
						.stroke(seg.color, style: StrokeStyle(lineWidth: 22 * u, lineCap: .butt))
				}
			}
		}
		.frame(width: 150 * u, height: 150 * u)
	}
}

/// One ring segment. Degrees in SwiftUI's angle space: 0 = 3 o'clock,
/// -90 = 12 o'clock, increasing = clockwise on screen (y is flipped, so
/// `clockwise: false` is the on-screen clockwise sweep).
private struct RingArc: Shape {
	let start: Double
	let end: Double
	let inset: CGFloat

	func path(in rect: CGRect) -> Path {
		var p = Path()
		p.addArc(
			center: CGPoint(x: rect.midX, y: rect.midY),
			radius: min(rect.width, rect.height) / 2 - inset,
			startAngle: .degrees(start),
			endAngle: .degrees(end),
			clockwise: false
		)
		return p
	}
}

private struct SectorBar: View {
	let name: String
	let share: String
	let color: Color
	/// Exact Figma fill width in artboard units (scaled by `figmaUnit`).
	let fill: CGFloat

	var body: some View {
		let u = figmaUnit
		VStack(spacing: 6 * u) {
			HStack(spacing: 0) {
				Circle()
					.fill(color)
					.frame(width: 9 * u, height: 9 * u)
				Spacer().frame(width: 8 * u)
				Text(name)
					.font(StakFont.geist(13 * u))
					.foregroundStyle(StakColors.textPrimary)
				Spacer(minLength: 0)
				Text(share)
					.font(StakFont.geist(12 * u, .medium))
					.foregroundStyle(muted)
			}
			ZStack(alignment: .leading) {
				RoundedRectangle(cornerRadius: 4 * u)
					.fill(track)
				RoundedRectangle(cornerRadius: 4 * u)
					.fill(color)
					.frame(width: fill * u)
			}
			.frame(maxWidth: .infinity)
			.frame(height: 7 * u)
		}
	}
}

// Review (2026-09-04): private to this file - the Discover deck keeps its own
// private 0...12 helper; an internal one here would redeclare it module-wide.
/// 0...30 as a word for the Overview's "Your read" sentence ("Five of
/// your twenty-one picks"); digits beyond. Mirrors android
/// ui/mystak/Collections.kt numberWord.
private func numberWord(_ n: Int) -> String {
	let small = [
		"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
		"eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen", "twenty"
	]
	switch n {
	case 0...20: return small[n]
	case 21...29: return "twenty-" + small[n - 20]
	case 30: return "thirty"
	default: return String(n)
	}
}
