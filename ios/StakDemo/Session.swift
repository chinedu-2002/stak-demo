import SwiftUI

/// Persisted sign-in state (user, 2026-08-23): a user who already signed
/// in is not asked to sign in again - the splash goes straight to Home;
/// a first-time user is taken to create an account. Backed by
/// UserDefaults until the real auth backend lands (then this is the
/// seam that holds the token + the profile it returns). Mirrors android
/// ui/Session.kt.
final class Session: ObservableObject {
	static let shared = Session()

	private static let keySignedIn = "stak.signedIn"
	private static let keyName = "stak.displayName"
	private static let keyRisk = "stak.riskStyle"
	/// Legacy: earlier builds kept the photo bytes here; read once and moved
	/// to the photo file (see loadPhoto).
	private static let keyPhoto = "stak.photoData"
	private static let keyDemo = "stak.demoAccount"
	private static let keyPicks = "stak.brandPicks"
	private static let keyGoal = "stak.goalAnswer"
	private static let keyRiskAnswer = "stak.riskAnswer"

	@Published private(set) var signedIn: Bool

	/// True when this launch started already signed in - the returning-user path.
	private(set) var resumedSignedIn: Bool
	/// Which account this is (product audit, 2026-09-05; mirrors Android): Sign in = the
	/// DEMO account with the authored history, Create account = a NEW account that starts
	/// empty and earns its numbers. Persisted with the sign-in.
	@Published private(set) var demoAccount: Bool

	private init() {
		let d = UserDefaults.standard
		// A @Published property can be assigned but not READ through self
		// until every stored property is initialized - both flags come
		// from the local instead.
		let wasSignedIn = d.bool(forKey: Self.keySignedIn)
		signedIn = wasSignedIn
		resumedSignedIn = wasSignedIn
		demoAccount = d.object(forKey: Self.keyDemo) as? Bool ?? true
		UserProfile.shared.displayName = d.string(forKey: Self.keyName) ?? ""
		if let risk = d.string(forKey: Self.keyRisk) { UserProfile.shared.riskStyle = risk }
		UserProfile.shared.brandPicks = Set(d.stringArray(forKey: Self.keyPicks) ?? [])
		UserProfile.shared.goal = d.object(forKey: Self.keyGoal) as? Int ?? -1
		UserProfile.shared.risk = d.object(forKey: Self.keyRiskAnswer) as? Int ?? -1
		UserProfile.shared.photoData = Self.loadPhoto()
		applyAccount()
	}

	/// Seeds (demo) or clears (new account) every user-data singleton for the current account.
	func applyAccount() {
		MyStakHoldings.shared.reset(demo: demoAccount)
		PaperPortfolio.shared.reset(demo: demoAccount)
	}

	/// Sign-in CTA or account creation (09 Proceed) - remembered across launches.
	/// `demo` = the authored demo account (Sign in); false = a fresh account (Create account).
	func signIn(demo: Bool) {
		signedIn = true
		demoAccount = demo
		persist()
		applyAccount()
	}

	/// Profile edits after sign-in stay with the session.
	func saveProfile() { persist() }

	/// Log out: forget the session and the profile; next launch asks to sign in.
	func signOut() {
		signedIn = false
		resumedSignedIn = false
		demoAccount = true
		UserProfile.shared.displayName = ""
		UserProfile.shared.photoData = nil
		UserProfile.shared.riskStyle = "Growth-Oriented"
		UserProfile.shared.brandPicks = []
		UserProfile.shared.goal = -1
		UserProfile.shared.risk = -1
		let d = UserDefaults.standard
		d.removeObject(forKey: Self.keySignedIn)
		d.removeObject(forKey: Self.keyName)
		d.removeObject(forKey: Self.keyRisk)
		d.removeObject(forKey: Self.keyPhoto)
		d.removeObject(forKey: Self.keyDemo)
		d.removeObject(forKey: Self.keyPicks)
		d.removeObject(forKey: Self.keyGoal)
		d.removeObject(forKey: Self.keyRiskAnswer)
		Self.savePhoto(nil)
		applyAccount()
	}

	private func persist() {
		let d = UserDefaults.standard
		d.set(signedIn, forKey: Self.keySignedIn)
		d.set(demoAccount, forKey: Self.keyDemo)
		d.set(UserProfile.shared.displayName, forKey: Self.keyName)
		d.set(UserProfile.shared.riskStyle, forKey: Self.keyRisk)
		d.set(Array(UserProfile.shared.brandPicks).sorted(), forKey: Self.keyPicks)
		d.set(UserProfile.shared.goal, forKey: Self.keyGoal)
		d.set(UserProfile.shared.risk, forKey: Self.keyRiskAnswer)
		Self.savePhoto(UserProfile.shared.photoData)
	}

	// MARK: - Profile photo file

	/// The (downsampled, tens-of-KB) profile photo lives as a FILE in
	/// Application Support, not as a UserDefaults blob (audit 2026-09-04):
	/// defaults are for small values - a multi-MB blob there is flagged at
	/// 4 MB and re-read on every launch. Mirrors android's URI-backed
	/// UserProfile.photoUri.
	private static let photoFileName = "stak_profile_photo.jpg"

	private static var photoFileURL: URL? {
		guard let dir = try? FileManager.default.url(
			for: .applicationSupportDirectory, in: .userDomainMask, appropriateFor: nil, create: true
		) else { return nil }
		return dir.appendingPathComponent(photoFileName)
	}

	private static func loadPhoto() -> Data? {
		if let url = photoFileURL, let data = try? Data(contentsOf: url) { return data }
		// A blob an earlier build kept in defaults moves to the file once -
		// shrunk the way the picker now shrinks a pick, since that blob may
		// be a full-size original.
		let d = UserDefaults.standard
		guard let legacy = d.data(forKey: keyPhoto) else { return nil }
		d.removeObject(forKey: keyPhoto)
		let data = UIImage(data: legacy)?
			.preparingThumbnail(of: CGSize(width: 512, height: 512))?
			.jpegData(compressionQuality: 0.85) ?? legacy
		savePhoto(data)
		return data
	}

	private static func savePhoto(_ data: Data?) {
		guard let url = photoFileURL else { return }
		if let data {
			try? data.write(to: url, options: .atomic)
		} else {
			try? FileManager.default.removeItem(at: url)
		}
	}
}
