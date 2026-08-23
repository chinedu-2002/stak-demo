package com.stak.demo.ui.news

import com.stak.demo.R

/**
 * The article hero is a MEDIA SLOT, not a fixed picture (user,
 * 2026-08-23): the backend sends a link with each story - a video
 * (YouTube or any other source) or an image - so viewers get a view of
 * what the stock is about. The app renders whatever is served inside
 * the authored 360x208 r10 hero box; a video shows its poster with the
 * play glyph and plays IN PLACE on tap.
 *
 * Demo = the authored hero art as the poster with a sample video link,
 * so the rest state matches the frame. Served posters arrive as URLs
 * (posterUrl) - the data phase's image loader renders those.
 */
sealed class NewsMedia {
	/** A still image. */
	data class Image(val posterRes: Int? = null, val url: String? = null) : NewsMedia()

	/** A video behind a poster: YouTube links embed, other links play directly. */
	data class Video(val url: String, val posterRes: Int? = null, val posterUrl: String? = null) : NewsMedia() {
		/** YouTube watch/short links -> the embeddable player URL; null for direct media. */
		val youTubeEmbedUrl: String?
			get() {
				val id = Regex("""(?:youtube\.com/(?:watch\?v=|embed/|shorts/)|youtu\.be/)([A-Za-z0-9_-]{6,})""")
					.find(url)?.groupValues?.get(1) ?: return null
				return "https://www.youtube.com/embed/$id?autoplay=1&playsinline=1&rel=0"
			}
	}

	companion object {
		/** Demo story media: authored poster + Apple's official iPhone film on YouTube (oEmbed-verified). */
		fun demo(): NewsMedia = Video(
			url = "https://www.youtube.com/watch?v=_-AS5DtDeqs",
			posterRes = R.drawable.news_hero_phone,
		)
	}
}
