package com.stak.demo.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * The worldwide market mood behind the Market Mood gauge — the CNN
 * Fear & Greed Index (the same index the design cites: "Wall Street's
 * fear gauge reads 32"). Score 0 = extreme fear .. 100 = extreme
 * greed; the gauge maps it across the dial so fear points into the
 * red and greed into the green. Fetched once per app start; on any
 * failure the gauge rests at the authored demo pose.
 */
object MarketMoodFeed {
	/**
	 * The user's call (2026-08-21): the gauge shows the DESIGN DEFAULT in
	 * this build phase so it always matches the Figma. Flip this on for
	 * the production build - then the needle measures the worldwide
	 * market in current time on the user's phone.
	 */
	const val LIVE = false

	var score by mutableStateOf<Float?>(null)
		private set
	private var attempted = false

	/** Authored demo needle angle (1:1158) used until data arrives. */
	const val DEMO_ANGLE_DEG = 33.4f

	fun angleFor(score: Float): Float = (score.coerceIn(0f, 100f) / 100f) * 180f

	suspend fun refresh() {
		if (!LIVE) return
		if (attempted && score != null) return
		attempted = true
		score = withContext(Dispatchers.IO) {
			runCatching {
				val conn = URL("https://production.dataviz.cnn.io/index/fearandgreed/graphdata")
					.openConnection() as HttpURLConnection
				conn.connectTimeout = 5000
				conn.readTimeout = 5000
				conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0 Safari/537.36")
				conn.setRequestProperty("Accept", "application/json")
				conn.setRequestProperty("Referer", "https://edition.cnn.com/markets/fear-and-greed")
				conn.inputStream.bufferedReader().use { reader ->
					JSONObject(reader.readText())
						.getJSONObject("fear_and_greed")
						.getDouble("score")
						.toFloat()
				}
			}.getOrNull()
		}
	}
}
