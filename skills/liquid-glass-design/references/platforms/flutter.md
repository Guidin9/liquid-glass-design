# Flutter

Two layers of help: the **Cupertino widgets** give the Apple system-app structure on every platform,
and the **liquid_glass_renderer** package draws real refracting glass where Flutter runs on Impeller.

## Structure with Cupertino widgets

```dart
CupertinoPageScaffold(
  backgroundColor: CupertinoColors.systemGroupedBackground,
  child: CustomScrollView(
    slivers: [
      const CupertinoSliverNavigationBar(
        largeTitle: Text('Settings'),       // collapses into the inline title on scroll
        border: null,
      ),
      SliverToBoxAdapter(
        child: CupertinoListSection.insetGrouped(
          header: const Text('General'),     // title case
          footer: const Text('Explanations go in footers.'),
          children: [
            CupertinoListTile(title: const Text('Version'), additionalInfo: const Text('2.1')),
            CupertinoListTile(
              title: const Text('Sync'),
              trailing: CupertinoSwitch(value: sync, onChanged: (v) => setState(() => sync = v)),
            ),
          ],
        ),
      ),
      const SliverPadding(padding: EdgeInsets.only(bottom: 104)),   // clearance for the floating bar
    ],
  ),
)
```

- `CupertinoSlidingSegmentedControl` for segmented controls; `CupertinoColors.*` are the iOS semantic
  colors (resolve with `CupertinoDynamicColor.resolve` for light/dark).
- Fonts: the Cupertino text theme uses SF on iOS/macOS. On Android, Windows, Linux and web set Inter
  (`google_fonts` or a bundled asset); never bundle SF Pro.
- Sections: iOS 26 uses larger corners (~26) than older Cupertino defaults; wrap or theme if the
  installed Flutter still draws the smaller radius.

## Glass with liquid_glass_renderer

Check pub.dev for the current version (0.2.0-dev at the time of writing; experimental). It requires the
**Impeller** renderer (iOS, Android, macOS); web, Windows and Linux are not supported.

```dart
Stack(
  children: [
    const ContentLayer(),                   // the scrolling page above
    Positioned(
      left: 0, right: 0, bottom: 12 + MediaQuery.paddingOf(context).bottom,
      child: Center(
        child: LiquidGlassLayer(
          settings: const LiquidGlassSettings(thickness: 20, blur: 8, glassColor: Color(0x33FFFFFF)),
          child: LiquidGlass(
            shape: LiquidRoundedSuperellipse(borderRadius: 32),
            child: SizedBox(width: 216, height: 64, child: TabBarContent()),
          ),
        ),
      ),
    ),
  ],
)
```

- Settings worth tuning: `thickness` (refraction), `blur`, `glassColor` (tint + alpha), `lightIntensity`,
  `refractiveIndex` (~1.5), `saturation`. Glass shapes in one `LiquidGlassLayer` blend together (max 16).
- Keep glass to the tab bar / toolbar; heavy shapes and animations are GPU-expensive, and animating
  shapes can spike memory.

### Fallback (web, Windows, Linux, Skia)

```dart
ClipRSuperellipse(
  borderRadius: BorderRadius.circular(32),
  child: BackdropFilter(
    filter: ImageFilter.blur(sigmaX: 16, sigmaY: 16),
    child: DecoratedBox(
      decoration: ShapeDecoration(
        color: glassTint,                                   // e.g. 0x66FAFAFA light / 0x66121212 dark
        shape: RoundedSuperellipseBorder(
          borderRadius: BorderRadius.circular(32),
          side: BorderSide(color: Colors.white.withValues(alpha: 0.35), width: 0.5),   // specular rim
        ),
      ),
      child: tabBarContent,
    ),
  ),
)
```

(`ClipRSuperellipse` / `RoundedSuperellipseBorder` need a recent Flutter; use `ClipRRect` /
`RoundedRectangleBorder` on older versions.) For Flutter web on Chromium you can also host the glass
in HTML using the web assets, but the fallback above is usually enough.

## Accessibility

`MediaQuery.highContrastOf(context)` → solid fills and borders; `MediaQuery.disableAnimationsOf(context)`
→ no gel motion. Flutter doesn't expose iOS Reduce Transparency directly; add an app setting or a small
platform channel if it matters.
