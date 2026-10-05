# Apple platforms: SwiftUI, UIKit, AppKit

Liquid Glass is native here. The job is to **use the system components and not fight them**, not to
imitate the material. Build with the iOS 26 / macOS 26 SDKs; standard bars, tab views, sheets, popovers,
menus and controls adopt Liquid Glass automatically. Gate new APIs with `if #available(iOS 26, *)` and
fall back to `.ultraThinMaterial` (or plain backgrounds) on earlier versions.

Reference: https://developer.apple.com/documentation/technologyoverviews/adopting-liquid-glass and
https://developer.apple.com/documentation/swiftui/applying-liquid-glass-to-custom-views

## Structure (SwiftUI)

```swift
TabView {
    Tab("Summary", systemImage: "bolt.fill") {
        NavigationStack {
            List {
                Section("Details") {
                    LabeledContent("Power", value: "7.6 W")
                    LabeledContent("Temperature", value: "36.8 °C")
                }
                Section {
                    Toggle("Show live meter", isOn: $isOn)
                } footer: {
                    Text("The meter pauses while the screen is off.")
                }
            }
            .listStyle(.insetGrouped)
            .navigationTitle("Battery")          // large title by default at the root
        }
    }
    Tab("Settings", systemImage: "gearshape.fill") { SettingsView() }
    Tab(role: .search) { SearchView() }          // the system places search at the trailing end
}
.tint(.green)                                     // one app tint: selected tab, toggles, prominent buttons
.tabBarMinimizeBehavior(.onScrollDown)            // optional: tab bar recedes while reading
```

- Remove custom bar backgrounds (`toolbarBackground`, `UINavigationBarAppearance` fills, custom tab bar
  views): they cover the glass and break the scroll edge effect.
- Section headers: pass normal title-case strings; iOS 26 no longer uppercases them.
- Use SF Symbols and the system font; that is allowed and expected here.

## Custom glass

```swift
// One element: regular glass in a capsule; tint only primary elements; interactive responds to touch.
Label("Start", systemImage: "play.fill")
    .padding()
    .glassEffect(.regular.tint(.green).interactive(), in: .capsule)

// Several glass elements: put them in one container so they render together and morph/merge.
@Namespace private var ns
GlassEffectContainer(spacing: 20) {
    HStack(spacing: 20) {
        Button("Undo", systemImage: "arrow.uturn.backward") { }
            .glassEffect().glassEffectID("undo", in: ns)
        if canRedo {
            Button("Redo", systemImage: "arrow.uturn.forward") { }
                .glassEffect().glassEffectID("redo", in: ns)
        }
    }
}

// Buttons: prefer the built-in styles to hand-made glass.
Button("Done") { }.buttonStyle(.glassProminent)
Button("Options") { }.buttonStyle(.glass)
```

- Apply `glassEffect` **after** other appearance modifiers (it captures the view's look).
- Use `glassEffectUnion(id:namespace:)` to merge nearby elements into one shape, and
  `glassEffectTransition(.materialize)` for elements far apart (`.matchedGeometry` morphs close ones).
- Variants: `.regular` almost always; `.clear` only over photos/video/maps with bold symbols, with a
  dimming layer when the media is bright.
- Too many separate glass effects or containers on screen cost performance; group them.

## Layout helpers

- Custom bars with content scrolling under them: `.safeAreaBar(edge: .bottom) { … }` so they get the
  scroll edge effect; tune with `.scrollEdgeEffectStyle(.soft, for: .top)`.
- Concentric shapes: `ConcentricRectangle()` / `.rect(corners:isUniform:)` instead of fixed radii.
- Toolbars: group related items; separate groups with `ToolbarSpacer(.fixed)`.
- Hero media next to a sidebar or inspector: `.backgroundExtensionEffect()`.
- Sheets: standard `.sheet` with detents; don't add backgrounds to them.

## UIKit / AppKit equivalents

- `UIGlassEffect` in a `UIVisualEffectView`; `UIButton.Configuration.glass()`, `.prominentGlass()`,
  `.clearGlass()`, `.prominentClearGlass()`.
- `UITabBarController.tabBarMinimizeBehavior = .onScrollDown`; `UISearchTab` for the search tab.
- `UIScrollEdgeElementContainerInteraction` for custom bars over scroll views;
  `UIView.cornerConfiguration` for concentric corners; `UIBackgroundExtensionView`.
- AppKit: `NSGlassEffectView`, `NSButton.BezelStyle.glass`, `NSBackgroundExtensionView`.

## Accessibility and testing

System components adapt automatically. For custom glass, read
`@Environment(\.accessibilityReduceTransparency)`, `\.colorSchemeContrast` and
`\.accessibilityReduceMotion`, and test with Reduce Transparency, Increase Contrast, Reduce Motion and
both glass appearance options (Settings > Display & Brightness). App icons: build them in Icon Composer
from flat layers; the system adds the glass.
