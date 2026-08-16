import SwiftUI

/// Palette of the CHINEDU "02 · Home" frames.
private enum Home {
	static let cardBg = Color(argb: 0xFF171D2C)
	static let tabBg = Color(argb: 0xFF060C1D)
	static let teal = Color(argb: 0xFF69B3CA)
	static let cardInk = Color(argb: 0xFF0E162B)
	static let paperWhite = Color(argb: 0xFFF9F9F9)
	static let navCircle = Color(argb: 0xFF192238)
}

/// One 236.86x278.45 news card of the deck in its frame pose — offsets are
/// from the parent card's center (350x397), rotation about its own center.
private struct DeckCard {
	let bg: Color
	let title: String
	let bodyText: String
	let bodyWeight: StakFont.Weight
	let bodySize: CGFloat
	let titleBodyGap: CGFloat
	let offsetX: CGFloat
	let offsetY: CGFloat
	let rotation: Double
}

/// Front card straight, the two behind rotated; later cards draw on top.
private let deckCards: [DeckCard] = [
	DeckCard(
		bg: Home.paperWhite,
		title: "Wall Street's fear gauge reads 32",
		bodyText: "The Fear & Greed Index is firmly in Fear territory. Money is rotating out of the ....",
		bodyWeight: .light,
		bodySize: 12,
		titleBodyGap: 12,
		offsetX: 6.2,
		offsetY: 59.73,
		rotation: 0
	),
	DeckCard(
		bg: Home.teal,
		title: "Fed meeting notes drop Wednesday",
		bodyText: "Minutes from the last Fed meeting land July 8. A market this tense moves on every word....",
		bodyWeight: .regular,
		bodySize: 11.89,
		titleBodyGap: 17,
		offsetX: 8.33,
		offsetY: 189.44,
		rotation: -3.72
	),
	DeckCard(
		bg: Home.paperWhite,
		title: "The OpenAI IPO is reportedly delayed",
		bodyText: "The year's most anticipated listing just slipped. Markets riding a wave of IPO excitement...",
		bodyWeight: .light,
		bodySize: 12,
		titleBodyGap: 12,
		offsetX: -0.02,
		offsetY: 264.57,
		rotation: -7.68
	)
]

/// 02 · Home — CHINEDU "Home first run" (1:958) and "Home Main" (1:1097).
/// Ported from android/ ui/home/HomeScreen.kt.
///
/// Both frames share the whole content stack: fixed top nav (STAK logo,
/// bell + profile circles, "Good Morning, Hamza"), the Market Mood card
/// with its clipped news-deck stack, the "Why this matters" row and the
/// teal deck banner. First run replaces the tab bar with a bottom scrim
/// and the frosted "See Todays Pick" pill; tapping it reveals Home Main
/// (prototype: Swap overlay · Instant). The tab bar itself lives in
/// MainTabsView so the other tabs share it.
struct HomeView: View {
	let firstRun: Bool
	let onSeeTodaysPick: () -> Void
	var onProfile: () -> Void = {}

	var body: some View {
		GeometryReader { geo in
			ZStack(alignment: .bottom) {
				VStack(spacing: 0) {
					TopNav(onProfile: onProfile)
					ScrollView {
						VStack(spacing: 0) {
							Spacer().frame(height: 21)
							MarketMoodCard()
							Spacer().frame(height: 10)
							WhyThisMattersCard()
							Spacer().frame(height: 20)
							DeckBanner()
							Spacer().frame(height: firstRun ? 140 : 20)
						}
						.padding(.horizontal, 20)
					}
				}
				if firstRun {
					// The frame pins the scrim 41px above the deck banner (Tab bar
					// y629 vs banner y670) — anchor to the same content geometry:
					// banner top = status inset + 626, so the scrim starts 585 below
					// the inset and runs to the physical bottom of the screen.
					FirstRunOverlay(onSeeTodaysPick: onSeeTodaysPick)
						.frame(height: geo.size.height + geo.safeAreaInsets.bottom - 585)
						.offset(y: geo.safeAreaInsets.bottom)
				}
			}
			.frame(maxWidth: .infinity, maxHeight: .infinity)
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}

/// Fixed top nav — logo row with bell/profile circles + greeting (Figma 131px block).
private struct TopNav: View {
	let onProfile: () -> Void

	var body: some View {
		VStack(alignment: .leading, spacing: 0) {
			HStack(spacing: 0) {
				Image("StakLogoMark")
					.resizable()
					.frame(width: 26.48, height: 26.48)
				Spacer().frame(width: 4.49)
				Image("IcStakWordmark")
					.resizable()
					.frame(width: 78.16, height: 14.98)
					.accessibilityLabel("STAK")
				Spacer()
				Image("IcNavBell")
					.resizable()
					.frame(width: 35, height: 35)
					.accessibilityLabel("Notifications")
				Spacer().frame(width: 4)
				Button(action: onProfile) {
					ZStack {
						Circle().fill(Home.navCircle)
						Image("IcNavPerson")
							.resizable()
							.frame(width: 12.99, height: 13.64)
					}
					.frame(width: 35, height: 35)
				}
				.buttonStyle(.plain)
				.accessibilityLabel("Profile")
			}
			.frame(height: 35)
			Spacer().frame(height: 10)
			Text("Good Morning, Hamza")
				.font(StakFont.sora(16, .semiBold))
				.foregroundStyle(Color.white)
		}
		.padding(.horizontal, 17)
		.padding(.top, 22)
		.frame(maxWidth: .infinity, alignment: .leading)
		.background(StakColors.bg)
	}
}

/// Market Mood — 350x397 #171d2c card with the clipped news-deck stack.
private struct MarketMoodCard: View {
	var body: some View {
		ZStack {
			NewsDeck()
			// The frame's bottom strip (1:1175, 30px) backdrop-blurs the stack —
			// redraw the same deck blurred, clipped to the card's last 30pt.
			// An oversized child gets centered in the 30pt band; shift it up
			// by (397-30)/2 so the stack's bottom edge lines up with the band.
			NewsDeck()
				.frame(maxWidth: .infinity)
				.frame(height: 397)
				.blur(radius: 4)
				.offset(y: -183.5)
				.frame(height: 30)
				.frame(maxWidth: .infinity)
				.clipped()
				.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .bottom)
			HStack(spacing: 0) {
				VStack(alignment: .leading, spacing: 4) {
					Text("Market Mood")
						.font(StakFont.sora(20, .medium))
						.foregroundStyle(Color.white)
					(
						Text("High volatility").foregroundColor(Home.teal)
							+ Text(", you should consider being cautious.").foregroundColor(Color.white)
					)
					.font(StakFont.geist(12))
				}
				.frame(width: 180, alignment: .leading)
				Spacer().frame(width: 46)
				Image("HomeMoodGauge")
					.resizable()
					.frame(width: 56.9, height: 28.84)
			}
			.padding(.top, 25)
			.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
		}
		.frame(maxWidth: .infinity)
		.frame(height: 397)
		.background(Home.cardBg)
		.clipShape(RoundedRectangle(cornerRadius: 8))
	}
}

/// The stacked news cards in their frame poses — front card straight, the
/// two behind rotated; offsets are from the parent card's center (350x397).
private struct NewsDeck: View {
	var body: some View {
		ZStack {
			ForEach(deckCards, id: \.title) { card in
				NewsDeckCard(card: card)
			}
		}
	}
}

/// One 236.86x278.45 news card of the deck, placed by its rotated-bounds center.
private struct NewsDeckCard: View {
	let card: DeckCard

	var body: some View {
		VStack(alignment: .leading, spacing: card.titleBodyGap) {
			Text(card.title)
				.font(StakFont.sora(16, .medium))
				.foregroundStyle(Home.cardInk)
				.frame(width: 202.9, alignment: .leading)
			Text(card.bodyText)
				.font(StakFont.geist(card.bodySize, card.bodyWeight))
				.foregroundStyle(Home.cardInk)
				.frame(width: 189.31, alignment: .leading)
		}
		.padding(.leading, 14.43)
		.padding(.top, 23.77)
		.frame(width: 236.86, height: 278.45, alignment: .topLeading)
		.background(card.bg, in: RoundedRectangle(cornerRadius: 6.79))
		.rotationEffect(.degrees(card.rotation))
		.offset(x: card.offsetX, y: card.offsetY)
	}
}

/// "Why this matters to you" — 350x91 card with the glass caution ball art.
private struct WhyThisMattersCard: View {
	var body: some View {
		ZStack {
			Image("HomeCautionBall")
				.resizable()
				.frame(width: 105, height: 105)
				.offset(x: 3, y: -7)
				.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
			VStack(alignment: .leading, spacing: 6) {
				Text("Why this matters to you")
					.font(StakFont.sora(14))
					.lineSpacing(15 - 14)
					.foregroundStyle(Color.white)
				Text("Your STAK collections houses 80% of stocks from effected industries.")
					.font(StakFont.geist(12, .light))
					.lineSpacing(15 - 12)
					.foregroundStyle(Color.white)
					.frame(width: 198, alignment: .leading)
			}
			.padding(.leading, 127)
			.frame(maxWidth: .infinity, alignment: .leading)
		}
		.frame(maxWidth: .infinity)
		.frame(height: 91)
		.background(Home.cardBg)
		.clipShape(RoundedRectangle(cornerRadius: 8))
	}
}

/// Teal deck banner — 350x116 with the box-and-coins art and Go to Deck chip.
private struct DeckBanner: View {
	var body: some View {
		ZStack {
			// The illustration zone of the frame (box + coins + shadow), cropped
			// from the banner render so its pose is exact; the teal it carries is
			// the same banner fill it sits on.
			Image("HomeBannerIllustration")
				.resizable()
				.scaledToFit()
				.frame(width: 172, height: 116)
				.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
			VStack(alignment: .leading, spacing: 10) {
				Text("Take your first deck to build your taste")
					.font(StakFont.geist(12, .light))
					.lineSpacing(15 - 12)
					.foregroundStyle(Color.black)
				Button {
					// Deck screen lands in a later phase.
				} label: {
					HStack(spacing: 3) {
						Text("Go to Deck")
							.font(StakFont.geist(11.49, .medium))
							.foregroundStyle(Color.white)
						Image("IcArrowRightSmall")
							.resizable()
							.frame(width: 16, height: 16)
					}
					.frame(width: 123, height: 32)
					.background(StakColors.bg, in: RoundedRectangle(cornerRadius: 15))
				}
				.buttonStyle(.plain)
			}
			.frame(width: 156, alignment: .leading)
			.offset(y: 0.5)
			.padding(.leading, 184)
			.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
		}
		.frame(maxWidth: .infinity)
		.frame(height: 116)
		.background(Home.teal)
		.clipShape(RoundedRectangle(cornerRadius: 8))
	}
}

/// First-run bottom treatment — scrim fading to bg over its first 98.7px
/// (43.68% of the frame's 226px overlay) + the frosted "See Todays Pick"
/// pill, whose bottom sits 70px above the screen edge as in the frame.
private struct FirstRunOverlay: View {
	let onSeeTodaysPick: () -> Void

	var body: some View {
		ZStack(alignment: .bottom) {
			VStack(spacing: 0) {
				LinearGradient(
					stops: [
						.init(color: Color(argb: 0x000A1020), location: 0),
						.init(color: StakColors.bg, location: 1)
					],
					startPoint: .top,
					endPoint: .bottom
				)
				.frame(height: 98.7)
				StakColors.bg
			}
			Button(action: onSeeTodaysPick) {
				Text("See Todays Pick")
					.font(StakFont.geist(12, .medium))
					.foregroundStyle(Color.white)
					.frame(width: 136, height: 51)
					.background(Color(argb: 0x0FFFFFFF), in: Capsule())
					.overlay(Capsule().strokeBorder(Color(argb: 0x66FFFFFF), lineWidth: 0.94))
			}
			.buttonStyle(.plain)
			.padding(.bottom, 70)
		}
		.frame(maxWidth: .infinity)
	}
}
