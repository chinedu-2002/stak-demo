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
/// All values are artboard units, multiplied by `figmaUnit` at use.
private struct DeckCard {
	let bg: Color
	let bodyWeight: StakFont.Weight
	let bodySize: CGFloat
	let titleBodyGap: CGFloat
	let offsetX: CGFloat
	let offsetY: CGFloat
	let rotation: Double
	let maxUpU: CGFloat
}

/// Front card straight, the two behind rotated; later cards draw on top.
/// Story text comes from NewsDeckFeed; card bottoms sit at
/// 397.4/527.2/602.7 in the 397 card, so the up-drag clamps at bottom-397.
private let deckCards: [DeckCard] = [
	DeckCard(
		bg: Home.paperWhite,
		bodyWeight: .light,
		bodySize: 12,
		titleBodyGap: 12,
		offsetX: 6.2,
		offsetY: 59.73,
		rotation: 0,
		maxUpU: 0.4
	),
	DeckCard(
		bg: Home.teal,
		bodyWeight: .regular,
		bodySize: 11.89,
		titleBodyGap: 17,
		offsetX: 8.33,
		offsetY: 189.44,
		rotation: -3.72,
		maxUpU: 130.2
	),
	DeckCard(
		bg: Home.paperWhite,
		bodyWeight: .light,
		bodySize: 12,
		titleBodyGap: 12,
		offsetX: -0.02,
		offsetY: 264.57,
		rotation: -7.68,
		maxUpU: 205.7
	)
]

/// Shared per-card drag offsets (pt): the crisp deck takes the gesture,
/// the blurred band copy mirrors the same motion. Each card can be
/// dragged up to reveal its full info and eases back to the authored
/// rest pose when the thumb leaves (user, 2026-08-22).
private final class DeckDragState: ObservableObject {
	@Published var offsets: [CGFloat] = [0, 0, 0]
	var active: Int = -1
}

/// 02 · Home — CHINEDU "Home first run" (1:958) and "Home Main" (1:1097),
/// dev-ready geometry from "Home Main" (118:1633).
/// Ported from android/ ui/home/HomeScreen.kt.
///
/// Both frames share the whole content stack: the top nav (STAK logo,
/// bell + profile circles, "Good Morning, Hamza") — which SCROLLS with the
/// content per 118:1633 — the Market Mood card with its clipped news-deck
/// stack, the "Why this matters" row and the teal deck banner. First run
/// replaces the tab bar with a bottom scrim and the frosted "See Todays
/// Pick" pill; tapping it reveals Home Main (prototype: Swap overlay ·
/// Instant). The tab bar itself lives in MainTabsView so the other tabs
/// share it.
struct HomeView: View {
	let firstRun: Bool
	let onSeeTodaysPick: () -> Void
	var onProfile: () -> Void = {}
	var onOpenNews: () -> Void = {}
	var onOpenMyStak: () -> Void = {}
	var onOpenDeck: () -> Void = {}

	var body: some View {
		let u = figmaUnit
		GeometryReader { geo in
			ZStack(alignment: .bottom) {
				// Dev-ready Home Main (118:1633): the top nav SCROLLS with the
				// content — the greeting block lives inside scroll content.
				ScrollView {
					VStack(spacing: 0) {
						TopNav(onProfile: onProfile)
							.padding(.horizontal, 17 * u)
						Spacer().frame(height: 21 * u)
						VStack(spacing: 0) {
							MarketMoodCard(onOpenNews: onOpenNews)
							Spacer().frame(height: 10 * u)
							WhyThisMattersCard(onOpenMyStak: onOpenMyStak)
							Spacer().frame(height: 20 * u)
							DeckBanner(onOpenDeck: onOpenDeck)
							// Authored scroll content (118:1634) ends exactly at the
							// banner's bottom edge — no trailing gap. First run keeps
							// room for the scrim pill.
							if firstRun {
								Spacer().frame(height: 140 * u)
							}
						}
						.padding(.horizontal, 20 * u)
					}
					.frame(maxWidth: .infinity)
				}
				if firstRun {
					// The frame pins the scrim 41px above the deck banner (Tab bar
					// y629 vs banner y670) — anchor to the same content geometry:
					// banner top = status inset + 626, so the scrim starts 585 below
					// the inset and runs to the physical bottom of the screen.
					FirstRunOverlay(onSeeTodaysPick: onSeeTodaysPick)
						.frame(height: geo.size.height + geo.safeAreaInsets.bottom - 585 * u)
						.offset(y: geo.safeAreaInsets.bottom)
				}
			}
			.frame(maxWidth: .infinity, maxHeight: .infinity)
		}
		.background(StakColors.bg.ignoresSafeArea())
	}
}

/// Top nav — logo row with bell/profile circles + greeting (Figma 131px
/// block). Scrolls with the content (118:1633); authored side inset 17.
private struct TopNav: View {
	let onProfile: () -> Void
	@ObservedObject var profile = UserProfile.shared
	@ObservedObject var notifications = StakNotifications.shared
	/// Time-of-day in the user's own timezone (device clock); re-read every
	/// 30s so an open app rolls over at noon / 5pm.
	@State private var greeting = Greeting.now()
	private let clock = Timer.publish(every: 30, on: .main, in: .common).autoconnect()

	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: 0) {
			HStack(spacing: 0) {
				Image("StakLogoMark")
					.resizable()
					.frame(width: 26.48 * u, height: 26.48 * u)
				Spacer().frame(width: 4.49 * u)
				Image("IcStakWordmark")
					.resizable()
					.frame(width: 78.16 * u, height: 14.98 * u)
					.accessibilityLabel("STAK")
				Spacer()
				// Bell + stateful unread dot (151:1207): the authored badge
				// (cx26.25 cy11.667 r2.917 #FF8030) shows while untouched
				// notifications exist and clears once they're opened and read.
				ZStack(alignment: .topLeading) {
					Image("IcNavBell")
						.resizable()
						.frame(width: 35 * u, height: 35 * u)
					if notifications.hasUnread {
						Circle()
							.fill(Color(argb: 0xFFFF8030))
							.frame(width: 5.833 * u, height: 5.833 * u)
							.offset(x: 23.333 * u, y: 8.75 * u)
					}
				}
				.contentShape(Rectangle())
				.onTapGesture { StakNotifications.shared.markAllRead() }
				.accessibilityLabel("Notifications")
				Spacer().frame(width: 4 * u)
				Button(action: onProfile) {
					ZStack {
						Circle().fill(Home.navCircle)
						Image("IcNavPerson")
							.resizable()
							.frame(width: 12.99 * u, height: 13.64 * u)
					}
					.frame(width: 35 * u, height: 35 * u)
				}
				.buttonStyle(.plain)
				.accessibilityLabel("Profile")
			}
			.frame(height: 35 * u)
			Spacer().frame(height: 10 * u)
			Text("\(greeting), \(profile.greetingName)")
				.onReceive(clock) { _ in greeting = Greeting.now() }
				.font(StakFont.sora(16 * u, .semiBold))
				.lineSpacing((20 - 16) * u)
				.foregroundStyle(Color.white)
		}
		.padding(.top, 22 * u)
		.frame(maxWidth: .infinity, alignment: .leading)
		.background(StakColors.bg)
	}
}

/// Market Mood — 350x397 #171d2c card with the clipped news-deck stack.
private struct MarketMoodCard: View {
	let onOpenNews: () -> Void

	@StateObject private var deckDrags = DeckDragState()

	var body: some View {
		let u = figmaUnit
		ZStack {
			NewsDeck(drags: deckDrags, interactive: true)
			// The frame's bottom strip (1:1175, 30px) backdrop-blurs the stack —
			// redraw the same deck blurred, clipped to the card's last 30 units.
			// An oversized child gets centered in the 30-unit band; shift it up
			// by (397-30)/2 so the stack's bottom edge lines up with the band.
			// Opaque ground: backdrop blur replaces everything behind the strip;
			// without it the blurred cards' soft alpha edges let the crisp deck
			// below show through. Radius render-calibrated on Android against the
			// frame's blurred title ink (band diff 9.55 -> 8.23); verify the 2.6u
			// visual on a simulator once this compiles on the Mac.
			ZStack {
				Home.cardBg
				NewsDeck(drags: deckDrags, interactive: false)
			}
				.frame(maxWidth: .infinity)
				.frame(height: 397 * u)
				.blur(radius: 2.6 * u)
				.offset(y: -183.5 * u)
				.frame(height: 30 * u)
				.frame(maxWidth: .infinity)
				.clipped()
				.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .bottom)
			HStack(spacing: 0) {
				VStack(alignment: .leading, spacing: 4 * u) {
					Text("Market Mood")
						.font(StakFont.sora(20 * u, .medium))
						.lineSpacing((25 - 20) * u)
						.foregroundStyle(Color.white)
					// Backend-served with the mood score in production (the words
					// change with the market); authored demo copy this phase.
					(
						Text(MarketMoodFeed.demoStatusLead).foregroundColor(Home.teal)
							+ Text(MarketMoodFeed.demoStatusRest).foregroundColor(Color.white)
					)
					.font(StakFont.geist(12 * u))
					.lineSpacing((16 - 12) * u)
				}
				.frame(width: 180 * u, alignment: .leading)
				Spacer().frame(width: 46 * u)
				MarketMoodGauge()
			}
			.padding(.top, 25 * u)
			.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
		}
		.frame(maxWidth: .infinity)
		.frame(height: 397 * u)
		.background(Home.cardBg)
		.clipShape(RoundedRectangle(cornerRadius: 8 * u))
		.contentShape(Rectangle())
		.onTapGesture(perform: onOpenNews)
	}
}

/// The stacked news cards in their frame poses — front card straight, the
/// two behind rotated; offsets are from the parent card's center (350x397).
/// Story text comes from NewsDeckFeed (backend-proxied breaking news in
/// production). A drag lifts the touched card above its siblings, reveals
/// its full info, and eases back to the authored rest pose on release;
/// the card is picked with a rotation-aware point test, topmost first.
private struct NewsDeck: View {
	@ObservedObject var drags: DeckDragState
	let interactive: Bool

	var body: some View {
		let u = figmaUnit
		let stories = NewsDeckFeed.stories()
		ZStack {
			ForEach(deckCards.indices, id: \.self) { i in
				NewsDeckCard(card: deckCards[i], story: stories[i])
					.offset(y: drags.offsets[i])
					.zIndex(drags.offsets[i] != 0 ? 1 : 0)
			}
			if interactive {
				Color.clear
					.contentShape(Rectangle())
					.gesture(
						DragGesture(minimumDistance: 8)
							.onChanged { value in
								if drags.active < 0 {
									drags.active = pickCard(at: value.startLocation, u: u)
								}
								let i = drags.active
								guard i >= 0 else { return }
								let maxUp = deckCards[i].maxUpU * u
								drags.offsets[i] = min(0, max(-maxUp, value.translation.height))
							}
							.onEnded { _ in
								let i = drags.active
								drags.active = -1
								guard i >= 0 else { return }
								withAnimation(.easeOut(duration: 0.3)) { drags.offsets[i] = 0 }
							}
					)
			}
		}
	}

	/// Topmost card whose rotated 236.86x278.45 rect contains the point;
	/// the deck area is the 350x397 mood card.
	private func pickCard(at p: CGPoint, u: CGFloat) -> Int {
		for i in deckCards.indices.reversed() {
			let card = deckCards[i]
			let cx = 175 * u + card.offsetX * u
			let cy = 198.5 * u + card.offsetY * u + drags.offsets[i]
			let rad = -card.rotation * .pi / 180
			let dx = p.x - cx
			let dy = p.y - cy
			let lx = dx * CGFloat(cos(rad)) + dy * CGFloat(sin(rad))
			let ly = -dx * CGFloat(sin(rad)) + dy * CGFloat(cos(rad))
			if abs(lx) <= 236.86 * u / 2, abs(ly) <= 278.45 * u / 2 {
				return i
			}
		}
		return -1
	}
}

/// One 236.86x278.45 news card of the deck, placed by its rotated-bounds center.
private struct NewsDeckCard: View {
	let card: DeckCard
	let story: NewsDeckFeed.Story

	var body: some View {
		let u = figmaUnit
		VStack(alignment: .leading, spacing: card.titleBodyGap * u) {
			Text(story.title)
				.font(StakFont.sora(16 * u, .medium))
				.foregroundStyle(Home.cardInk)
				.frame(width: 202.9 * u, alignment: .leading)
			Text(story.body)
				.font(StakFont.geist(card.bodySize * u, card.bodyWeight))
				.foregroundStyle(Home.cardInk)
				.frame(width: 189.31 * u, alignment: .leading)
		}
		.padding(.leading, 14.43 * u)
		.padding(.top, 23.77 * u)
		.frame(width: 236.86 * u, height: 278.45 * u, alignment: .topLeading)
		.background(card.bg, in: RoundedRectangle(cornerRadius: 6.79 * u))
		.rotationEffect(.degrees(card.rotation))
		.offset(x: card.offsetX * u, y: card.offsetY * u)
	}
}

/// "Why this matters to you" — 350x91 card (1:1037) with the glass caution
/// ball art. Shaped background, no clip — the visible ball never reaches
/// the card edges, only transparent padding overhangs.
private struct WhyThisMattersCard: View {
	let onOpenMyStak: () -> Void

	var body: some View {
		let u = figmaUnit
		ZStack {
			// Authored (1:1043/1:1044): the 105x105 image box sits at (3, -7)
			// with the source mapped 1:1 (no crop) — the ball itself stays
			// inside the card; only the box's transparent padding overhangs.
			Image("HomeCautionGlass")
				.resizable()
				.frame(width: 105 * u, height: 105 * u)
				.offset(x: 3 * u, y: -7 * u)
				.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
			VStack(alignment: .leading, spacing: 6 * u) {
				Text("Why this matters to you")
					// Authored (1:1040): Sora Regular 14 / lh15.
					.font(StakFont.sora(14 * u))
					.lineSpacing((15 - 14) * u)
					.foregroundStyle(Color.white)
				// Backend-served summary of why today's news matters to THIS
				// user (holdings + risk profile); authored demo copy this phase.
				Text(WhyThisMattersFeed.body())
					.font(StakFont.geist(12 * u, .light))
					.lineSpacing((15 - 12) * u)
					.foregroundStyle(Color.white)
					.frame(width: 198 * u, alignment: .leading)
			}
			.padding(.leading, 127 * u)
			.frame(maxWidth: .infinity, alignment: .leading)
		}
		.frame(maxWidth: .infinity)
		.frame(height: 91 * u)
		.background(Home.cardBg, in: RoundedRectangle(cornerRadius: 8 * u))
		.contentShape(Rectangle())
		.onTapGesture(perform: onOpenMyStak)
	}
}

/// Teal deck banner — 350x116 (118:1720) with the box-and-coins art and
/// Go to Deck chip; the whole banner opens the deck.
private struct DeckBanner: View {
	let onOpenDeck: () -> Void

	var body: some View {
		let u = figmaUnit
		ZStack {
			// The illustration zone of the frame (box + coins + shadow), cropped
			// from the banner render so its pose is exact; the teal it carries is
			// the same banner fill it sits on. The 1:1191 node's in-banner slice
			// (121.5x116 at x13), baked from the 2x frame render.
			Image("HomeBannerIllustration")
				.resizable()
				.scaledToFit()
				.frame(width: 121.5 * u, height: 116 * u)
				.offset(x: 13 * u)
				.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
			VStack(alignment: .leading, spacing: 10 * u) {
				Text("Take your first deck to build your taste")
					.font(StakFont.geist(12 * u, .light))
					.lineSpacing((15 - 12) * u)
					.foregroundStyle(Color.black)
				// No button of its own: the authored connection is on the whole
				// banner (1:1184), whose tap target includes this chip.
				HStack(spacing: 3 * u) {
					Text("Go to Deck")
						.font(StakFont.geist(11.49 * u, .medium))
						.lineSpacing((15 - 11.49) * u)
						.foregroundStyle(Color.white)
					Image("IcArrowRightSmall")
						.resizable()
						.frame(width: 16 * u, height: 16 * u)
				}
				.frame(width: 123 * u, height: 32 * u)
				.background(StakColors.bg, in: RoundedRectangle(cornerRadius: 15 * u))
			}
			.frame(width: 156 * u, alignment: .leading)
			.offset(y: 0.5 * u)
			.padding(.leading, 184 * u)
			.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
		}
		.frame(maxWidth: .infinity)
		.frame(height: 116 * u)
		.background(Home.teal)
		.clipShape(RoundedRectangle(cornerRadius: 8 * u))
		.contentShape(Rectangle())
		.onTapGesture(perform: onOpenDeck)
	}
}

/// First-run bottom treatment — scrim fading to bg over its first 98.7px
/// (43.68% of the frame's 226px overlay) + the frosted "See Todays Pick"
/// pill, whose bottom sits 70px above the screen edge as in the frame.
private struct FirstRunOverlay: View {
	let onSeeTodaysPick: () -> Void

	var body: some View {
		let u = figmaUnit
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
				.frame(height: 98.7 * u)
				StakColors.bg
			}
			Button(action: onSeeTodaysPick) {
				Text("See Todays Pick")
					.font(StakFont.geist(12 * u, .medium))
					.foregroundStyle(Color.white)
					.frame(width: 136 * u, height: 51 * u)
					.background(Color(argb: 0x0FFFFFFF), in: Capsule())
					.overlay(Capsule().strokeBorder(Color(argb: 0x66FFFFFF), lineWidth: 0.94 * u))
			}
			.buttonStyle(.plain)
			.padding(.bottom, 70 * u)
		}
		.frame(maxWidth: .infinity)
	}
}

/// The Market Mood gauge, drawn from the AUTHORED SVG primitives of
/// 1:1159 (Group 314): ring center (28.4509, 28.4509), centerline
/// r24.8946, stroke 7.1127 in the 56.9018x28.8371 canvas; three
/// 60-degree segments (green 180..120, neutral 120..60, red 60..0);
/// the needle is the exact authored path - tip (48.8472, 16.37), base
/// (28.0396, 28.2577)/(26.7526, 25.6491) - with the pivot blob at
/// (27.8686, 26.7203) r1.5806. Rest = the authored pose verbatim (axis
/// 26.27 deg from the blob); live values rotate about the blob center.
private struct MarketMoodGauge: View {
	/// Needle pose in math degrees CCW from +x — rests at the authored
	/// default; no entry sweep (the frame's pose is the rest state).
	@State private var sweepDeg: Double = MarketMoodFeed.demoAngleDeg
	/// Breathe offset in math degrees, -0.8...0.8 autoreversing over 2.4s.
	@State private var wobbleDeg: Double = -0.8

	var body: some View {
		let u = figmaUnit
		let k = u   // canvas pt per authored unit (canvas width = 56.9018u)
		ZStack {
			GaugeArc(startDeg: 180)
				.stroke(Color(argb: 0xFF61A57F), style: StrokeStyle(lineWidth: 7.1127 * k, lineCap: .butt))
			GaugeArc(startDeg: 240)
				.stroke(Color(argb: 0xFFD8CFCF), style: StrokeStyle(lineWidth: 7.1127 * k, lineCap: .butt))
			GaugeArc(startDeg: 300)
				.stroke(Color(argb: 0xFFDE4E71), style: StrokeStyle(lineWidth: 7.1127 * k, lineCap: .butt))
			GaugeNeedle()
				.fill(Color.white)
				// Rotate the authored needle group about the authored blob
				// center; math degrees run CCW, rotationEffect CW - flip sign.
				.rotationEffect(
					.degrees(authoredAxisDeg - (sweepDeg + wobbleDeg)),
					anchor: UnitPoint(x: 27.8686 / 56.9018, y: 26.7203 / 28.8371)
				)
		}
		.frame(width: 56.9018 * u, height: 28.8371 * u)
		.onAppear {
			// Idle breathe, tween 2400 EaseInOutSine reversing (cubic-bezier
			// 0.37, 0, 0.63, 1 is the sine ease-in-out curve).
			withAnimation(.timingCurve(0.37, 0, 0.63, 1, duration: 2.4).repeatForever(autoreverses: true)) {
				wobbleDeg = 0.8
			}
			// Moves to the live worldwide reading once it arrives (user's
			// call, 2026-08-21); no-op while MarketMoodFeed.live is false.
			MarketMoodFeed.refresh { angle in
				withAnimation(.easeOut(duration: 0.9)) { sweepDeg = angle }
			}
		}
	}
}

/// One 60-degree gauge segment on the centerline radius 25.6, pivot at the
/// gauge's bottom-center. Screen angles: 180 = left (green end), sweeping
/// clockwise over the top to 360 = right (red end).
private struct GaugeArc: Shape {
	let startDeg: Double

	func path(in rect: CGRect) -> Path {
		let k = rect.width / 56.9018
		var path = Path()
		path.addArc(
			center: CGPoint(x: 28.4509 * k, y: 28.4509 * k),
			radius: 24.8946 * k,
			startAngle: .degrees(startDeg),
			endAngle: .degrees(startDeg + 60),
			// In SwiftUI's y-down space `clockwise: false` sweeps with
			// increasing screen angle — visually clockwise, like Compose's
			// positive sweepAngle.
			clockwise: false
		)
		return path
	}
}

/// The white needle (1:1159) — the exact authored path: tapered blade to
/// the tip plus the pivot blob. Rotation happens on the view, about the
/// authored blob center.
private struct GaugeNeedle: Shape {
	func path(in rect: CGRect) -> Path {
		let k = rect.width / 56.9018
		var path = Path()
		path.move(to: CGPoint(x: 48.8472 * k, y: 16.37 * k))
		path.addLine(to: CGPoint(x: 28.0396 * k, y: 28.2577 * k))
		path.addLine(to: CGPoint(x: 26.7526 * k, y: 25.6491 * k))
		path.closeSubpath()
		let r = 1.5806 * k
		path.addEllipse(in: CGRect(x: (27.8686 - 1.5806) * k, y: (26.7203 - 1.5806) * k, width: r * 2, height: r * 2))
		return path
	}
}

/// Authored needle axis (blob center -> tip) in math degrees.
private let authoredAxisDeg = 26.27
