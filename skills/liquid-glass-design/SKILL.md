---
name: liquid-glass-design
description: Design and build app interfaces in the style of Apple's Liquid Glass (iOS 26, WWDC25) so they look and behave like an Apple system app, on any stack — Android (Jetpack Compose), web (HTML/CSS, React, Vue, Svelte), Windows (WinUI 3, WPF, Electron, Tauri, WebView2), Apple platforms (SwiftUI, UIKit) and Flutter. Use this skill whenever the user wants an "Apple-like", "iOS-style", "Liquid Glass", glassmorphism, frosted or translucent glass look, a premium/polished system-app feel, or asks to redesign a UI that feels plain or "like a test app" — even if they never say "Liquid Glass" — and whenever the user has adopted this style as their default for new apps. Provides the design rules (what may be glass and what must stay opaque), tokens (system colors, type scale, shapes, motion), component patterns (floating glass tab bar, large titles with scroll edge effects, inset grouped lists, iOS toggles and segmented controls), per-platform recipes with ready templates, and a review checklist.
---

# Liquid Glass design

Build interfaces that read like one of Apple's own apps from the iOS 26 generation: calm, opaque content
with a thin layer of living glass floating above it for navigation. This works on every platform, but
each platform reaches the effect differently, so the workflow below routes you to the right recipe.

The style is defined by restraint more than by the glass itself. Most screens that "look like a
glassmorphism demo" break Apple's rules: glass on cards, glass on glass, everything tinted. Follow the
rules and the result looks native; ignore them and it looks like a template.

## Workflow

1. **Identify the stack** and open its guide (read only the one you need):

   | Stack | Guide | Real lensing (refraction)? |
   |---|---|---|
   | Android, Jetpack Compose | `references/platforms/android-compose.md` | Yes, API 33+ (AGSL), via the `backdrop` library |
   | Web: HTML/CSS, React, Vue, Svelte, Next | `references/platforms/web.md` | Yes in Chromium (SVG displacement); frosted fallback elsewhere |
   | Windows: Electron, Tauri, WebView2, MAUI Blazor | `references/platforms/web.md` + `references/platforms/windows.md` | Yes (Chromium/WebView2) |
   | Windows: WinUI 3, WPF | `references/platforms/windows.md` | No; acrylic + specular rim approximation |
   | Apple: SwiftUI, UIKit | `references/platforms/apple.md` | Native: use the system APIs, don't imitate |
   | Flutter | `references/platforms/flutter.md` | Yes on Impeller (iOS/Android/macOS); fallback elsewhere |

2. **Read `references/principles.md`** when you need the reasoning behind a rule or hit a case the
   summary below doesn't settle. It is a condensed, sourced digest of Apple's guidance.

3. **Set up the tokens** from `references/tokens.md`: system colors for light and dark, the type scale,
   corner radii, spacing and motion. Platform templates in `assets/` already contain them.

4. **Build the content layer first** (opaque, grouped, large title), then add the **navigation layer**
   (glass tab bar or toolbar) on top. Pick components from `references/components.md`.

5. **Review** with `references/review-checklist.md` before calling it done. Look at real screenshots in
   light and dark mode, scrolled and at rest, and with reduced transparency. Fix what the checklist finds.

## The rules that make it look native

These carry most of the result. Each has a reason; keep the reason in mind for cases not listed.

1. **Two layers.** Content (lists, cards, charts, media) sits in an opaque content layer. Liquid Glass
   is only for the navigation layer that floats above it: tab bars, toolbars, sidebars, floating buttons,
   sheets, menus. Glass on content blurs the line between "what I look at" and "what I operate".
2. **Controls in content are the one exception, and only while touched.** A toggle knob or slider thumb
   may turn into glass during interaction and go back to solid when released.
3. **Use glass sparingly; never stack glass on glass.** One or two glass surfaces per screen. Glass over
   glass muddies both and reads as clutter.
4. **Regular variant by default.** Regular glass blurs and adjusts the luminosity behind it so text stays
   legible over anything. The clear variant is only for controls floating over rich media (photos,
   video, maps); add a ~35% dark dimming layer under it when the media is bright.
5. **Tint almost nothing.** Color goes to the one primary or selected element (the active tab, a primary
   action). When every element is tinted, nothing stands out.
6. **Concentric, continuous shapes.** Controls are capsules; containers use large continuous-curvature
   ("squircle") corners; a nested shape's radius equals its container's radius minus the inset between
   them. Mixed corner styles are the fastest giveaway of a non-Apple UI.
7. **Scroll edge effects.** Where content scrolls under a bar, dissolve it (fade, plus blur where the
   platform allows) so the bar's labels stay legible. Strong enough under an inline title that the text
   behind never competes with it.
8. **Apple layout grammar.** Large left-aligned title that collapses into a small centered title on
   scroll; inset grouped lists with generous rows (~52pt) and ~26pt section corners; title-case section
   headers (no all caps); footers in secondary color; trailing values in secondary color.
9. **System colors and type.** iOS semantic colors (grouped background, label hierarchy, fills,
   separators, systemGreen/Blue/Orange/Red) in light and dark. SF Pro on Apple platforms; Inter
   elsewhere, because SF Pro and SF Symbols are licensed only for Apple platforms.
10. **Glass behaves like a material.** It materializes in (fades and bends in, rather than popping), flexes
    and brightens under a finger, and the selection lens morphs between positions. Motion answers
    touch; nothing loops for decoration.
11. **Respect the accessibility switches.** Reduced transparency: frostier, more opaque glass and no
    lensing. Increased contrast: solid fills and a visible border. Reduced motion: no elastic or
    morphing motion. Every icon-only control gets an accessibility label.
12. **Steady state stays clean.** When a screen first appears, nothing important should sit under glass;
    leave bottom padding equal to the floating bar's height plus margin.

## Layer model

```
 ┌─────────────────────────────────────┐
 │ status bar                          │  scroll edge effect fades content here
 │ Inline Title (appears on scroll)    │  (no bar background; legibility from the effect)
 ├─────────────────────────────────────┤
 │ Large Title                         │
 │ ╭─────────────────────────────────╮ │  CONTENT LAYER — opaque
 │ │ hero / summary card             │ │  grouped background + cells
 │ ╰─────────────────────────────────╯ │  continuous 26pt corners
 │ Section header                      │
 │ ╭─────────────────────────────────╮ │
 │ │ Row title             value  >  │ │
 │ │─────────────────────────────────│ │  hairline separators inset to text
 │ │ Row title              [toggle] │ │
 │ ╰─────────────────────────────────╯ │
 │       ╭─────────────────────╮       │  NAVIGATION LAYER — Liquid Glass
 │       │ (◉ Tab)    ○ Tab    │       │  floating capsule, selection lens,
 │       ╰─────────────────────╯       │  only the selected tab tinted
 └─────────────────────────────────────┘
```

## Assets

- `assets/android-compose/`: Compose theme (system colors, Inter type scale), large-title page, inset
  grouped list rows, segmented control, glass tab bar, glass toggle, interaction helpers, wiring example.
- `assets/web/`: `tokens.css`, `liquid-glass.css`, `liquid-glass.js` (Chromium refraction with automatic
  fallback), and `demo.html`, a complete sample screen to copy from.
- `assets/windows/`: `LiquidGlass.xaml` WinUI 3 resource dictionary (colors, acrylic glass brushes,
  capsule bar and inset group styles) and an example page.

Copy templates into the project and adapt names; don't link the skill folder from app code.

## Pitfalls seen in practice

- A glass bar over an empty or uniform background looks like a flat gray pill. Glass needs something
  behind it; real content scrolling under it is what sells the effect.
- Inline titles over a weak scroll edge effect collide with the content behind them. Make the effect
  nearly opaque right under the title, dissolving further down.
- Material Symbols XML downloaded for Android references `?attr/colorControlNormal`, which fails to
  link without AppCompat. Delete that tint attribute. If it ends the tag, keep the `>`.
- Don't ship SF Pro or SF Symbols outside Apple platforms, and don't use Apple's logos or product names
  in the app UI. The goal is the design language, not impersonation.
- Glass effects cost GPU time. Limit glass to a few small surfaces; never put it on large scrolling
  areas or every list row.
