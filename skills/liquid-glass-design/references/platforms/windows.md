# Windows: WinUI 3, WPF, Electron/Tauri/WebView2, .NET MAUI

Windows has its own materials (Mica, Acrylic) but no built-in refraction. Choose the stack by how much of
the Liquid Glass look the app needs:

| Stack | Glass you get | Recommendation |
|---|---|---|
| Electron, Tauri (Windows uses WebView2), WebView2 in WinUI/WPF, MAUI Blazor Hybrid | Full: Chromium renders the SVG lensing | Use `references/platforms/web.md` unchanged |
| WinUI 3 (Windows App SDK) | In-app acrylic blur + rim + shadow, no lensing | `assets/windows/LiquidGlass.xaml` + this guide |
| WPF | Window-level Mica/Acrylic only; no in-app backdrop blur | Emulate (below) or host the UI in WebView2 |
| .NET MAUI | Per platform: native glass on iOS 26, backdrop on Android, acrylic on Windows | Use platform handlers; follow each platform guide |

## WinUI 3

1. Merge `assets/windows/LiquidGlass.xaml` into `App.xaml`. It defines Light/Dark/HighContrast brushes
   with iOS system colors, an in-app `AcrylicBrush` for glass (`LgGlassBrush`), a specular rim gradient
   (`LgGlassRimBrush`), a bottom scroll edge gradient, text styles and the inset-group/row/bar styles.
2. Package Inter at `Assets/Fonts/Inter.ttf` (OFL). Keep Segoe Fluent Icons for glyphs, or ship
   Material Symbols Rounded as a font or SVG paths. Don't ship SF Pro or SF Symbols.
3. Content layer: page background `LgGroupedBackgroundBrush` (opaque, not Mica; Apple's content layer is
   calm and solid); inset groups are `Border` with `LgInsetGroupStyle` (radius 26), rows `MinHeight=52`,
   1px separators inset 16.
4. Navigation layer: a `Border` with `LgGlassBarStyle` floating at the bottom center (64 tall, radius 32,
   4 padding, `Translation="0,0,32"` + `<ThemeShadow/>`), with a selection lens `Border` (radius 28,
   `LgGlassSelectionBrush`) under the tab buttons. Animate the lens with `TranslationTransition`
   (`Vector3Transition`, 350 ms) and tint only the selected tab's foreground. See
   `assets/windows/ExamplePage.xaml`.
5. Acrylic blurs the app's own content behind it, so put the bar over the `ScrollViewer` (same Grid cell,
   later child) and give the content bottom padding ≈ 104 so the last row can scroll clear.
6. Large title: a `TextBlock` with `LgLargeTitleStyle` at the top of the scrolled content; on
   `ScrollViewer.ViewChanged` fade in a small centered title over a top gradient once the offset passes ~40.
7. Toggle: the system `ToggleSwitch` is Windows-shaped (40×20). For the iOS look retemplate it: track
   64×28 capsule (`LgFillBrush` off, tint on), knob 40×24 white with shadow, 2 inset; on pointer press
   scale the knob ~1.35 and switch its fill to a translucent white with a rim to suggest glass.
8. Corner radii in XAML are circular, not continuous; keep them large (26, capsules) so the difference is
   minor.

Accessibility: when the user turns off **Transparency effects** (`UISettings.AdvancedEffectsEnabled ==
false`) or uses battery saver, `AcrylicBrush` falls back to its `FallbackColor` automatically, which
gives the frosted, opaque variant. HighContrast resources switch everything to system colors. Set
`AutomationProperties.Name` on icon-only buttons.

Lensing on WinUI would need rendering the content to a texture and running a custom Direct2D/Win2D
displacement shader each frame: expensive and complex. Only consider it for a single hero element; the
acrylic + rim + shadow approximation is the default.

## WPF

WPF can't blur what is behind an element inside the window. Options, in order of fidelity:
1. **Host the UI in WebView2** and use the web assets: full lensing.
2. **WPF UI** (lepoco/wpfui, MIT) or `DwmSetWindowAttribute(DWMWA_SYSTEMBACKDROP_TYPE)` for window-level
   Mica/Acrylic on Windows 11, with opaque inset groups on top, and a floating bar drawn as a
   semi-opaque tint (`#CCFAFAFA` light / `#CC1C1C1E` dark) + rim gradient border + `DropShadowEffect`.
3. Snapshot-based blur (`VisualBrush` of the content + `BlurEffect`) only for static backgrounds; it does
   not update cheaply while scrolling.

## Electron, Tauri, WebView2

Chromium everywhere on Windows, so the web assets give real refraction. Keep the window opaque (content
layer), let the HTML draw the glass, and set the window background color to the grouped background to
avoid a white flash. Tauri on macOS uses WKWebView, which gets the frosted fallback.

## Verify

Run the app in light, dark and High Contrast, with Transparency effects on and off (Settings >
Personalization > Colors). Screenshot (Win + Shift + S) with content scrolled under the bar and check
the review checklist.
