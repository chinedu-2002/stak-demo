import Foundation

/// The article hero is a MEDIA SLOT, not a fixed picture (user,
/// 2026-08-23): the backend sends a link with each story - a video
/// (YouTube or any other source) or an image - so viewers get a view of
/// what the stock is about. The app renders whatever is served inside
/// the authored 360x208 r10 hero box; a video shows its poster with the
/// play glyph and plays IN PLACE on tap. Mirrors android ui/news/NewsMedia.kt.
///
/// Demo = the authored hero art as the poster with a sample video link,
/// so the rest state matches the frame. Served posters arrive as URLs
/// (posterUrl) - the data phase's image loader renders those.
enum NewsMedia {
	/// A still image.
	case image(posterAsset: String?, url: String?)
	/// A video behind a poster: YouTube links embed, other links play directly.
	case video(url: String, posterAsset: String?, posterUrl: String?)

	/// YouTube watch/short links -> the embeddable player URL; nil for direct media.
	static func youTubeEmbedURL(for url: String) -> URL? {
		let pattern = #"(?:youtube\.com/(?:watch\?v=|embed/|shorts/)|youtu\.be/)([A-Za-z0-9_-]{6,})"#
		guard let re = try? NSRegularExpression(pattern: pattern),
			  let m = re.firstMatch(in: url, range: NSRange(url.startIndex..., in: url)),
			  let r = Range(m.range(at: 1), in: url) else { return nil }
		return URL(string: "https://www.youtube.com/embed/\(url[r])?autoplay=1&playsinline=1&rel=0")
	}

	/// Demo story media: the authored poster art over a verified
	/// direct-stream phone b-roll (Mixkit). YouTube embeds stay the
	/// production contract, but this environment's in-app WebViews are
	/// refused YouTube's video streams (user, 2026-08-25 black screen),
	/// so the demo serves direct MP4s the platform players decode.
	static func demo() -> NewsMedia {
		.video(url: "https://assets.mixkit.co/videos/4915/4915-720.mp4", posterAsset: "NewsHeroPhone", posterUrl: nil)
	}
}
