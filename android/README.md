# STAK Android (Kotlin / Jetpack Compose)

Native Kotlin/Compose sibling of the `ios/` SwiftUI app — same Figma
frames, same flow, equal design fidelity. Built from the CHINEDU Figma
file (`vB8TOvR1WyyXEKVZ8frstU`, section "01 · Onboarding & Auth", node
1:178). 1 Figma px = 1 dp/sp; screens are stateless composables +
callbacks wired in `StakNavHost`.

## Building

Standard Gradle: open `android/` in Android Studio, or

```sh
cd android
./gradlew assembleDebug
```

Gradle wrapper is pinned to 8.11.1 (AGP 8.7.3 is incompatible with
Gradle 9.x — don't regenerate the wrapper to a newer major).

## Project layout

Mirrors the `ios/StakDemo/` group layout one-to-one so engineers can hop
between the two codebases:

| android/ …/com/stak/demo/ | ios/StakDemo/ | Contents |
|---|---|---|
| `StakApp.kt` / `MainActivity.kt` | `StakDemoApp.swift` | App entry |
| `navigation/StakNavHost.kt` + `StakRoutes.kt` | `RootFlowView.swift` | Flow routing |
| `MainTabsShell` (in `StakNavHost.kt`) | `MainTabsView.swift` | Tab shell placeholder |
| `ui/theme/` | `Theme/` | Color + font tokens |
| `ui/components/` | `Components/` | Shared chrome (tab bar) |
| `ui/onboarding/` | `Onboarding/` | The 12 flow screens + `AuthKit` |
| `res/drawable*` / `res/font` | `Assets.xcassets` / `Resources/Fonts` | Exports + fonts |
| `core/firebase/` | — | Firebase DI (wiring is a later phase) |

Asset naming convention: an Android res name is the snake_case of its
iOS asset (`brand_apple` ↔ `BrandApple`, `ic_google_g` ↔ `IcGoogleG`,
`splash_glass_ball` ↔ `SplashGlassBall`), so any export lives under the
same words on both platforms. Android res names are lowercase by
platform rule; iOS uses PascalCase.

## Screen inventory (CHINEDU node IDs)

| Screen | Composable | Node |
|---|---|---|
| 00 Splash | `SplashScreen` | 1:926 |
| 01 Welcome | `IntroScreen` | 1:179 |
| 02 Brand picks | `BrandPicksScreen` | 1:232 |
| 03 Swipe tutorial | `SwipeTutorialScreen` | 1:344 |
| 04 Goal matrix | `GoalScreen` (`MatrixQuizScreen`) | 1:498 |
| 05 Risk matrix | `RiskScreen` (`MatrixQuizScreen`) | 1:569 |
| 06 Preparing deck | `PreparingDeckScreen` | 1:634 |
| 07 Taste reveal | `TasteRevealScreen` | 1:687 |
| 08 Permissions | `PermissionsScreen` | 1:749 |
| 09 Profile setup | `ProfileSetupScreen` | 1:793 |
| Auth · Sign up | `CreateAccountScreen` | 1:830 |
| Auth · Sign in | `SignInScreen` | 1:879 |

Flow: splash → sign up (⇄ sign in) → 01 → … → 07 → 08 → 09 → tab shell
(sign-in goes straight to the shell). The splash auto-advances to sign
up after 1200ms with a 350ms ease-out dissolve, per the file's
prototype wiring. `MainTabsShell` is the phase-3 placeholder with the
real Figma tab bar (`StakTabBar`).

## Fonts & assets

`res/font/` carries the Sora and Geist variable TTFs (weights
instantiated per style in `ui/theme/Type.kt`) plus Squarish Sans CT.
`res/drawable-nodpi/` holds flattened Figma renders (glass ball, hero
box, tutorial cards, 12 brand circles, Google/Apple marks);
`res/drawable/` holds icon vector drawables converted from the Figma
SVGs. Tab-bar icons, chevrons and the spinner are drawn with Canvas
from the Figma vector geometry.
