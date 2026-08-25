import Foundation

/// News article data source - the article page is a TEMPLATE (user,
/// 2026-08-25: tapping the Dow brief must open the DOW story, not the
/// Apple one). The STAK BACKEND serves each story's own article -
/// headline, subtitle, body, media, related stock - and the app renders
/// it into the authored page (1:1495). Mirrors android
/// ui/news/NewsArticleFeed.kt.
///
/// The Apple article is the AUTHORED one and stays frame-exact; the
/// other demo articles stand in for served content. Apple-specific
/// authored extras (stock card, gist, glossary, related, tags) render
/// only on the Apple article until the backend serves their data.
enum NewsArticleFeed {
	static let apple = "apple-foldable"

	struct Article {
		let id: String
		let category: String
		let headline: String
		let subtitle: String
		let ticker: String?
		let paragraphs: [String]
		let media: NewsMedia
		let shareText: String
	}

	private static func demo(_ id: String, _ category: String, _ headline: String, _ subtitle: String, _ paragraphs: [String]) -> Article {
		Article(
			id: id,
			category: category,
			headline: headline,
			subtitle: subtitle,
			ticker: nil,
			paragraphs: paragraphs,
			// No served media yet - the hero renders its empty slot.
			media: .image(posterAsset: nil, url: nil),
			shareText: "\(headline) - read it on STAK: https://stak.app/news/\(id)"
		)
	}

	private static let articles: [Article] = [
		Article(
			id: apple,
			category: "Tech & Ai",
			headline: "Apple climbs 5% on foldable iPhone push",
			subtitle: "A bigger foldable order and the widest iPhone lineup in years sent Apple toward a record, and to within touching distance of Nvidia’s crown.",
			ticker: "AAPL",
			paragraphs: [], // the authored body renders inline (frame-exact)
			media: NewsMedia.demo(),
			shareText: "Apple climbs 5% on foldable iPhone push - read it on STAK: https://stak.app/news/apple-foldable-iphone-push"
		),
		demo(
			"dow-record-chips-slide", "Markets",
			"Dow closes at a record as chips slide",
			"Wall Street split into the long weekend: a record Dow, a bruised Nasdaq, and a jobs report soft enough to keep rate-cut hopes alive.",
			[
				"The Dow notched an all-time closing high on Thursday even as a rout in memory chip names dragged the Nasdaq lower, an unusual split that left traders debating which index was telling the truth about the economy.",
				"A softer-than-expected jobs report did the heavy lifting. Slower hiring eased pressure on the Federal Reserve, and rate-sensitive corners of the market - banks, industrials, housing - rallied while high-multiple chip stocks gave back part of a torrid run.",
				"The question for next week is whether the rotation holds. Earnings from two mega-cap tech names and a fresh inflation print will decide whether the record was a changing of the guard or just a long-weekend head fake.",
			]
		),
		demo(
			"fed-holds-rates", "Markets",
			"Fed holds rates, signals patience on cuts",
			"Policymakers left the benchmark rate unchanged and made clear they want more data before easing.",
			[
				"The Federal Reserve held its benchmark rate steady and, in language traders read as deliberately unhurried, said it wants further evidence that inflation is cooling before it begins cutting.",
				"Futures markets trimmed bets on a September move within minutes of the statement. The dot plot still pencils in easing this year, but the bar for the first cut has clearly moved higher.",
				"For savers the pause means more of the same; for borrowers it delays relief. Markets will now comb every speech and inflation print between here and the next meeting for the word the Fed would not say: when.",
			]
		),
		demo(
			"oil-opec-supply", "Markets",
			"Oil slips after positive Iran talks",
			"Crude fell for a third session as diplomacy and a possible OPEC+ supply boost weighed on prices.",
			[
				"Oil extended its slide after reports that OPEC+ may lift output next quarter landed alongside signs of progress in talks with Iran, a combination that points the same direction: more barrels.",
				"Energy shares lagged the broader market while the losers of expensive fuel - airlines, shippers, delivery names - caught a bid. The move also shaves a little off headline inflation math if it sticks.",
				"Analysts caution that both stories can still reverse: supply decisions are not final and talks have collapsed before. But for now the path of least resistance for crude is lower.",
			]
		),
		demo(
			"tech-earnings-week", "Tech & Ai",
			"Tech earnings week: what to watch",
			"Five of the largest names report in four days - AI spending guidance is the number that moves everything.",
			[
				"Five of the market’s largest companies report over four days, and one line item towers over the rest: how much they plan to spend on AI infrastructure, and what they say it is earning them.",
				"Cloud growth rates, buyback pace against record cash piles, and any hint of softness in advertising round out the checklist. Options markets are pricing bigger-than-usual single-day moves across the group.",
				"The stakes reach past tech. These names carry enough index weight that their guidance will set the tone for the whole tape into the next Fed meeting.",
			]
		),
	]

	/// The served article for a story - the backend resolves this in production.
	static func article(_ id: String) -> Article {
		articles.first { $0.id == id } ?? articles[0]
	}

	/// Brief page index -> its article id (demo mapping).
	static let briefArticles = ["dow-record-chips-slide", "fed-holds-rates", "oil-opec-supply", "tech-earnings-week"]
}
