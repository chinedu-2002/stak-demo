import SwiftUI
import UIKit

/// The app is portrait-locked (Info.plist), EXCEPT while the article
/// player's native fullscreen presentation is up: then the scene turns to
/// landscape and may be held either way up, so the clip plays like a
/// Netflix player (user, 2026-09-05; mirrors Android's fullscreen
/// SENSOR_LANDSCAPE). Leaving fullscreen turns the scene back to portrait
/// before the dismissal lands. The delegate's answer overrides Info.plist.
final class OrientationLock {
	static let shared = OrientationLock()
	private(set) var landscapeAllowed = false

	func allowLandscape(_ allow: Bool) {
		guard landscapeAllowed != allow else { return }
		landscapeAllowed = allow
		let mask: UIInterfaceOrientationMask = allow ? .landscape : .portrait
		for scene in UIApplication.shared.connectedScenes.compactMap({ $0 as? UIWindowScene }) {
			if #available(iOS 16.0, *) {
				scene.keyWindow?.rootViewController?.setNeedsUpdateOfSupportedInterfaceOrientations()
				scene.requestGeometryUpdate(.iOS(interfaceOrientations: mask))
			} else {
				UIViewController.attemptRotationToDeviceOrientation()
			}
		}
	}
}

final class AppDelegate: NSObject, UIApplicationDelegate {
	func application(_ application: UIApplication, supportedInterfaceOrientationsFor window: UIWindow?) -> UIInterfaceOrientationMask {
		OrientationLock.shared.landscapeAllowed ? .allButUpsideDown : .portrait
	}
}
