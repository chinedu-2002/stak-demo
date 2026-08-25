import Foundation

/// News article data source - the article page is a TEMPLATE (user,
/// 2026-08-25: tapping the Dow brief must open the DOW story, not the
/// Apple one). The STAK BACKEND serves each story's own article -
/// headline, subtitle, body, media, related stock - and the app renders
/// it into the authored page (1:1495). Mirrors android
/// ui/news/NewsArticleFeed.kt.
///
/// The Apple article is the section's PLACEHOLDER (user, 2026-08-25):
/// ONE template renders every story - gist, pull quote, explainer,
/// source, tags and read-next come from the served article, and the
/// stock card / key stats appear whenever the story has a ticker. The
/// authored Apple copy ships as data, so that page stays frame-exact.
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
		let gist: [String]
		let pullQuote: String?
		let explainer: String?
		let tags: [String]
	}

	private static func demo(_ id: String, _ category: String, _ headline: String, _ subtitle: String, _ paragraphs: [String], _ gist: [String], _ pullQuote: String, _ explainer: String, _ tags: [String]) -> Article {
		Article(
			id: id,
			category: category,
			headline: headline,
			subtitle: subtitle,
			ticker: nil,
			paragraphs: paragraphs,
			// No served media yet - the hero renders its empty slot.
			media: .image(posterAsset: nil, url: nil),
			shareText: "\(headline) - read it on STAK: https://stak.app/news/\(id)",
			gist: gist,
			pullQuote: pullQuote,
			explainer: explainer,
			tags: tags
		)
	}

	private static let articles: [Article] = [
		Article(
			id: apple,
			category: "Tech & Ai",
			headline: "Apple climbs 5% on foldable iPhone push",
			subtitle: "A bigger foldable order and the widest iPhone lineup in years sent Apple toward a record, and to within touching distance of Nvidia’s crown.",
			ticker: "AAPL",
			// The AUTHORED body/gist/quote/explainer as data - the single
			// rendering path reproduces the authored page bit-exact.
			paragraphs: [
				"Apple had one of its best days in months on Thursday, climbing almost 5 percent after word got out that the company is planning its widest iPhone lineup in years. Nikkei Asia reported that Apple has asked suppliers to prepare at least five new models, and to lift output of its first foldable to around 10 million units, well above the seven to eight million it had penciled in.",
				"That last number is the tell. Companies do not quietly double down on a product they expect to flop, and the foldable, which the rumor mill has taken to calling the iPhone Ultra, is now expected to land between late 2026 and the first half of 2027. Traders read the order size as confidence and bought accordingly. Apple gained about 182 billion dollars in market value on the day, nearly enough on its own to paper over a sell-off tearing through chip stocks.",
				"The rally leaves Apple roughly 4 percent shy of retaking the title of most valuable company in the world from Nvidia, a crown the two have passed back and forth all year. It also lets the stock shake off a rough June, when a rare mid-cycle price increase on Macs and iPads, blamed on climbing memory costs, sent shares lower and rattled investors who had grown used to Apple holding the line.",
				"The real verdict comes on July 30, when Apple reports fiscal third quarter results. Wall Street is penciling in revenue of around 108 billion dollars, but the number everyone will hunt for is any early read on how the new lineup, and its price tags, are actually selling.",
			],
			media: NewsMedia.demo(),
			shareText: "Apple climbs 5% on foldable iPhone push - read it on STAK: https://stak.app/news/apple-foldable-iphone-push",
			gist: [
				"Apple rose about 5% on plans for its widest iPhone lineup yet.",
				"It raised foldable orders to 10 million units, a show of confidence.",
				"The stock sits about 4% from passing Nvidia as the most valuable company.",
			],
			pullQuote: "Companies do not quietly double down on a product they expect to flop.",
			explainer: "A foldable phone opens out into a small tablet. For Apple it means a pricier device to sell, and a way to win back buyers who drifted to Samsung, which has offered foldables for years.",
			tags: ["Apple", "Tech"]
		),
		demo(
			"dow-record-chips-slide", "Markets",
			"Dow closes at a record as chips slide",
			"Wall Street split into the long weekend: a record Dow, a bruised Nasdaq, and a jobs report soft enough to keep rate-cut hopes alive.",
			[
				"The Dow notched an all-time closing high on Thursday even as a rout in memory chip names dragged the Nasdaq lower, an unusual split that left traders debating which index was telling the truth about the economy.",
				"A softer-than-expected jobs report did the heavy lifting. Slower hiring eased pressure on the Federal Reserve, and rate-sensitive corners of the market - banks, industrials, housing - rallied while high-multiple chip stocks gave back part of a torrid run.",
				"The question for next week is whether the rotation holds. Earnings from two mega-cap tech names and a fresh inflation print will decide whether the record was a changing of the guard or just a long-weekend head fake.",
			],
			[
				"The Dow closed at a record while the Nasdaq fell.",
				"A soft jobs report eased pressure on the Fed.",
				"Rotation out of chips lifted banks and industrials.",
			],
			"Two indexes, two very different stories about the same economy.",
			"The Dow tracks 30 large, established companies, so it can rise even when tech-heavy indexes fall. A split like this usually means money is rotating, not leaving.",
			["Markets", "Stocks"]
		),
		demo(
			"fed-holds-rates", "Markets",
			"Fed holds rates, signals patience on cuts",
			"Policymakers left the benchmark rate unchanged and made clear they want more data before easing.",
			[
				"The Federal Reserve held its benchmark rate steady and, in language traders read as deliberately unhurried, said it wants further evidence that inflation is cooling before it begins cutting.",
				"Futures markets trimmed bets on a September move within minutes of the statement. The dot plot still pencils in easing this year, but the bar for the first cut has clearly moved higher.",
				"For savers the pause means more of the same; for borrowers it delays relief. Markets will now comb every speech and inflation print between here and the next meeting for the word the Fed would not say: when.",
			],
			[
				"Rates were left unchanged again.",
				"The Fed wants more inflation data before cutting.",
				"Markets trimmed bets on a September cut.",
			],
			"The bar for the first cut has clearly moved higher.",
			"When the Fed holds rates, borrowing costs stay where they are. Markets care less about the decision itself than about hints of when cuts begin.",
			["Markets", "Fed"]
		),
		demo(
			"oil-opec-supply", "Markets",
			"Oil slips after positive Iran talks",
			"Crude fell for a third session as diplomacy and a possible OPEC+ supply boost weighed on prices.",
			[
				"Oil extended its slide after reports that OPEC+ may lift output next quarter landed alongside signs of progress in talks with Iran, a combination that points the same direction: more barrels.",
				"Energy shares lagged the broader market while the losers of expensive fuel - airlines, shippers, delivery names - caught a bid. The move also shaves a little off headline inflation math if it sticks.",
				"Analysts caution that both stories can still reverse: supply decisions are not final and talks have collapsed before. But for now the path of least resistance for crude is lower.",
			],
			[
				"Crude fell for a third straight session.",
				"OPEC+ may lift output next quarter.",
				"Airlines and shippers rallied on cheaper fuel.",
			],
			"Both stories point the same direction: more barrels.",
			"Oil prices move on expected supply and demand. More supply - real or promised - usually means lower prices at the pump and lower costs for fuel-hungry industries.",
			["Energy", "Markets"]
		),
		demo(
			"tech-earnings-week", "Tech & Ai",
			"Tech earnings week: what to watch",
			"Five of the largest names report in four days - AI spending guidance is the number that moves everything.",
			[
				"Five of the market’s largest companies report over four days, and one line item towers over the rest: how much they plan to spend on AI infrastructure, and what they say it is earning them.",
				"Cloud growth rates, buyback pace against record cash piles, and any hint of softness in advertising round out the checklist. Options markets are pricing bigger-than-usual single-day moves across the group.",
				"The stakes reach past tech. These names carry enough index weight that their guidance will set the tone for the whole tape into the next Fed meeting.",
			],
			[
				"Five mega-caps report within four days.",
				"AI spending guidance is the headline number.",
				"Options price bigger-than-usual single-day moves.",
			],
			"Their guidance will set the tone for the whole tape.",
			"Earnings season is when companies report results and give guidance. For the largest names, that guidance moves entire indexes, not just their own stock.",
			["Tech", "Earnings"]
		),
	]

	/// The served article for a story - the backend resolves this in production.
	static func article(_ id: String) -> Article {
		articles.first { $0.id == id } ?? articles[0]
	}

	/// Brief page index -> its article id (demo mapping).
	static let briefArticles = ["dow-record-chips-slide", "fed-holds-rates", "oil-opec-supply", "tech-earnings-week"]
}
