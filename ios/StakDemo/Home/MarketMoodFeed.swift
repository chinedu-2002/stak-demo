import Foundation

/// Market Mood data source. Mirrors android ui/home/MarketMoodFeed.kt.
/// In this build phase the gauge always rests at the authored design
/// default (CHINEDU 1:958); flip `live` for production, where the needle
/// measures the worldwide CNN Fear & Greed index in current time —
/// green = market is good, middle = neutral, red = big problem.
enum MarketMoodFeed {
	/// Production switch — keep false while reviews compare build vs frame.
	static let live = false

	/// Authored rest angle of the needle (math degrees CCW from +x).
	static let demoAngleDeg: Double = 33.4

	/// 0 = extreme fear (red, right) … 100 = greed (green, left).
	static func angleFor(score: Double) -> Double { score / 100 * 180 }

	/// Fetches the live score and delivers the needle angle on the main
	/// queue. No-op while `live` is false. The CNN endpoint 4xx's without
	/// browser-looking headers — keep all three.
	static func refresh(_ apply: @escaping (Double) -> Void) {
		guard live else { return }
		guard let url = URL(string: "https://production.dataviz.cnn.io/index/fearandgreed/graphdata") else { return }
		var req = URLRequest(url: url)
		req.setValue(
			"Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.0 Mobile/15E148 Safari/604.1",
			forHTTPHeaderField: "User-Agent"
		)
		req.setValue("application/json", forHTTPHeaderField: "Accept")
		req.setValue("https://edition.cnn.com/markets/fear-and-greed", forHTTPHeaderField: "Referer")
		URLSession.shared.dataTask(with: req) { data, _, _ in
			guard
				let data,
				let obj = try? JSONSerialization.jsonObject(with: data) as? [String: Any],
				let fg = obj["fear_and_greed"] as? [String: Any],
				let score = fg["score"] as? Double
			else { return }
			DispatchQueue.main.async { apply(angleFor(score: score)) }
		}.resume()
	}
}
