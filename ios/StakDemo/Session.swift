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

	@Published private(set) var signedIn: Bool

	/// True when this launch started already signed in - the returning-user path.
	private(set) var resumedSignedIn: Bool

	private init() {
		let d = UserDefaults.standard
		signedIn = d.bool(forKey: Self.keySignedIn)
		resumedSignedIn = signedIn
		UserProfile.shared.displayName = d.string(forKey: Self.keyName) ?? ""
		if let risk = d.string(forKey: Self.keyRisk) { UserProfile.shared.riskStyle = risk }
	}

	/// Sign-in CTA or account creation (09 Proceed) - remembered across launches.
	func signIn() {
		signedIn = true
		persist()
	}

	/// Profile edits after sign-in stay with the session.
	func saveProfile() { persist() }

	/// Log out: forget the session and the profile; next launch asks to sign in.
	func signOut() {
		signedIn = false
		resumedSignedIn = false
		UserProfile.shared.displayName = ""
		UserProfile.shared.photoData = nil
		UserProfile.shared.riskStyle = "Growth-Oriented"
		let d = UserDefaults.standard
		d.removeObject(forKey: Self.keySignedIn)
		d.removeObject(forKey: Self.keyName)
		d.removeObject(forKey: Self.keyRisk)
	}

	private func persist() {
		let d = UserDefaults.standard
		d.set(signedIn, forKey: Self.keySignedIn)
		d.set(UserProfile.shared.displayName, forKey: Self.keyName)
		d.set(UserProfile.shared.riskStyle, forKey: Self.keyRisk)
	}
}
