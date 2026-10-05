# Android: Jetpack Compose

Real Liquid Glass (blur, vibrancy, lensing with chromatic edges, specular highlight, press flex) is
available through Kyant's open-source **backdrop** library, which renders with AGSL runtime shaders.
Templates in `assets/android-compose/` were built and verified on a phone (Android 16) with it.

## Dependencies

Check Maven Central for the latest versions before adding (`io.github.kyant0:backdrop`,
`io.github.kyant0:shapes`); the templates were written against backdrop 2.0.1 and shapes 1.2.1
(Compose 1.12+, Kotlin 2.4).

```toml
# gradle/libs.versions.toml
[versions]
backdrop = "2.0.1"
shapes = "1.2.1"

[libraries]
backdrop = { group = "io.github.kyant0", name = "backdrop", version.ref = "backdrop" }
shapes = { group = "io.github.kyant0", name = "shapes", version.ref = "shapes" }
```

```kotlin
// app/build.gradle.kts
implementation(libs.backdrop)
implementation(libs.shapes)
```

Capability by API level: lens/refraction and the interactive glow need **API 33+** (runtime shaders);
blur needs **API 31+** (RenderEffect). Below that the library draws only the surface tint, so on old
devices raise the glass surface alpha (≈0.85) to stay legible. minSdk of the library is 21.

## Files to copy

| Template | Put at | What it gives you |
|---|---|---|
| `Theme.kt` | `ui/Theme.kt` | `SystemColors` (light/dark, one tint), `SystemType` (iOS scale on Inter), `LiquidGlassTheme` |
| `Accessibility.kt` | `ui/Accessibility.kt` | Reduced transparency (Samsung), increased contrast (API 34), reduced motion |
| `components/LargeTitlePage.kt` | `ui/components/` | Large title, inline title on scroll, top/bottom scroll edge effects, tab-bar clearance |
| `components/InsetGroup.kt` | `ui/components/` | Inset group, ListRow (gray press highlight), separators, Value/Navigation/Action/Control/Notice rows |
| `components/SegmentedControl.kt` | `ui/components/` | Capsule segmented control with spring thumb |
| `glass/*.kt` | `ui/glass/` | GlassTabBar + GlassTab (draggable selection lens), GlassToggle (glass knob while touched), helpers |
| `MainActivityExample.kt` | reference | How the layers are wired; copy the structure into your activity |

Replace `com.example.app` with the app's package in every file. The `glass/` files are adapted from the
Backdrop Catalog (Apache-2.0); keep their header comments and ship the license (see the repo NOTICE).

## Fonts and icons

- Font: download Inter's variable font to `app/src/main/res/font/inter.ttf`:
  `https://raw.githubusercontent.com/google/fonts/main/ofl/inter/Inter%5Bopsz,wght%5D.ttf`
  (OFL; keep `OFL.txt` from the same folder in the repo's third-party notices).
- Icons: Material Symbols Rounded, filled (`fill1`), as vector drawables:
  `https://raw.githubusercontent.com/google/material-design-icons/master/symbols/android/<name>/materialsymbolsrounded/<name>_fill1_24px.xml`
  e.g. `bolt`, `settings`, `home`, `chevron_right` (outline: drop `_fill1`).
  These files contain `android:tint="?attr/colorControlNormal"`, an AppCompat attribute that fails to link
  in pure Compose apps. Remove the attribute; when it is the last one, keep the closing `>` of `<vector`:
  `sed -i 's/[[:space:]]*android:tint="?attr\/colorControlNormal"//' file.xml`.

## Wiring the layers

```kotlin
val backdrop = rememberLayerBackdrop()
Box(Modifier.fillMaxSize()) {
    Box(Modifier.fillMaxSize().layerBackdrop(backdrop)) {   // content layer, captured
        CurrentScreen()                                    // pages draw their own grouped background
    }
    GlassTabBar(backdrop = backdrop, /* … */ modifier = Modifier.align(Alignment.BottomCenter)
        .navigationBarsPadding().padding(bottom = 12.dp).width(216.dp)) { GlassTab(/* … */) }
}
```

- Everything the glass should refract (backgrounds, scroll content, scroll edge overlays) must be inside
  the `layerBackdrop` box; the glass itself must be outside it (a sibling drawn after).
- A control inside the content (GlassToggle) refracts only its own track via a local backdrop, so it
  never samples itself.
- Hoist each tab's `ScrollState` so positions survive tab switches. `BackHandler` returns to the first tab.
- `enableEdgeToEdge()`; set `android:windowBackground` to the grouped background color (`#F2F2F7` light /
  `#000000` dark in `values-night`) to avoid a white flash at launch.

## Custom glass elements

Use the same recipe as the tab bar (regular variant):

```kotlin
Modifier.drawBackdrop(
    backdrop = backdrop,
    shape = { Capsule() },                  // or RoundedRectangle(24.dp): continuous corners
    effects = {
        vibrancy()
        blur(8.dp.toPx())
        lens(24.dp.toPx(), 24.dp.toPx())    // refraction band depth and amount
    },
    onDrawSurface = { drawRect(colors.glassSurface) },
)
```

For press feedback reuse `InteractiveHighlight` (glow at the finger) and scale in `layerBlock`, as in
`GlassTabBar`. For a glass circle button, the catalog's `LiquidButton` pattern is: capsule shape,
`vibrancy(); blur(2.dp); lens(12.dp, 24.dp)`, 48 tall.

## Verify on a device

Build, install and look at it; the glass only reads correctly on real content.

```bash
./gradlew assembleDebug && adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n <package>/<activity>
adb exec-out screencap -p > screen.png        # binary-safe (don't redirect through PowerShell 5)
adb shell input swipe 540 1700 540 900 400    # scroll, then capture again for the inline title
```

Screenshots can contain private notifications in the status bar; crop to the app area or delete them
after review. If the screen is locked, ask the user to unlock; never try to bypass the lock.

## Known gotchas

- AGP 9 has built-in Kotlin: apply the Compose compiler plugin with the same version as the Kotlin
  Gradle plugin; if AGP's bundled KGP is older, declare `org.jetbrains.kotlin.android` with `apply false`
  in the root build file to lift it.
- Samsung One UI: the reduced-transparency switch is `Settings.System "accessibility_reduce_transparency"`.
- Glass refraction needs content behind it. Over a plain background the bar looks like a gray pill; that
  is expected, and it comes alive when lists scroll under it.
- Keep the number of glass surfaces small; every one samples and filters the backdrop each frame.
