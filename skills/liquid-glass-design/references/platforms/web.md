# Web: HTML/CSS, React, Vue, Svelte (and Electron, Tauri, WebView2)

The web gets the whole look with three files from `assets/web/`: `tokens.css` (system colors, type,
shape; light/dark; accessibility preferences), `liquid-glass.css` (components) and `liquid-glass.js`
(refraction, scroll effects, tab bar lens). `demo.html` is a complete screen to copy from; open it to
see every part working.

## How the glass works here

| Browser engine | Result |
|---|---|
| Chromium (Chrome, Edge, Opera, Brave, Electron, WebView2, Tauri on Windows) | Real lensing: an SVG `feDisplacementMap` generated from the element's rounded shape runs inside `backdrop-filter`, plus blur, saturation, specular rim and shadow |
| WebKit (Safari, Tauri on macOS/iOS), Gecko (Firefox) | Frosted glass: `backdrop-filter: blur() saturate()` + tint + specular rim + shadow (no displacement; these engines don't render SVG filters in `backdrop-filter`) |

`liquid-glass.js` decides this itself (it checks for a Chromium brand) and leaves the CSS fallback in
place elsewhere; it also skips refraction when the user prefers reduced transparency. A displacement map
is regenerated when the element is resized.

## Setup

```html
<link href="https://fonts.googleapis.com/css2?family=Inter:opsz,wght@14..32,400..700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="tokens.css">
<link rel="stylesheet" href="liquid-glass.css">
<script src="liquid-glass.js" defer></script>
<body class="lg-app">…</body>
```

- Set the app tint once: `:root { --lg-tint: #34c759 }` (and the dark value in the dark blocks).
- The font stack uses SF on Apple devices (`-apple-system`) and Inter everywhere else. Self-host Inter
  (OFL) for offline apps (Electron/Tauri).
- Icons: inline SVG from Material Symbols Rounded (filled), viewBox `0 0 960 960`, `fill: currentColor`.
  Never ship SF Symbols on the web.

## Markup patterns

```html
<!-- Inline title + top scroll edge effect (state set by liquid-glass.js) -->
<header class="lg-topbar"><p class="lg-topbar__title lg-headline">Settings</p></header>

<main class="lg-page">
  <h1 class="lg-large-title">Settings</h1>
  <section class="lg-section">
    <h2 class="lg-section__header lg-headline">General</h2>
    <div class="lg-group">
      <div class="lg-row"><span class="lg-row__title">Version</span><span class="lg-row__value">2.1</span></div>
      <a class="lg-row" href="/about"><span class="lg-row__title">About</span><span class="lg-chevron"></span></a>
      <label class="lg-row">
        <span class="lg-row__title">Sync</span>
        <span class="lg-toggle"><input type="checkbox" role="switch" checked aria-label="Sync">
          <span class="lg-toggle__track"></span><span class="lg-toggle__knob"></span></span>
      </label>
      <div class="lg-row lg-row--control">
        <span id="mode">Mode</span>
        <div class="lg-segmented" role="radiogroup" aria-labelledby="mode" style="--lg-segments:2">
          <span class="lg-segmented__thumb"></span>
          <input type="radio" name="m" id="m1" checked><label for="m1">Simple</label>
          <input type="radio" name="m" id="m2"><label for="m2">Advanced</label>
        </div>
      </div>
    </div>
    <p class="lg-section__footer lg-footnote">Explanations go in footers.</p>
  </section>
</main>

<div class="lg-bottom-edge"></div>

<!-- Floating glass tab bar: the only glass on the page -->
<nav class="lg-tabbar lg-glass" role="tablist" aria-label="Sections" data-lg-refract>
  <span class="lg-tabbar__lens"></span>
  <button class="lg-tab" role="tab" aria-selected="true"><svg…/>Home</button>
  <button class="lg-tab" role="tab" aria-selected="false"><svg…/>Settings</button>
</nav>
```

The tab bar dispatches `lg-tabchange` (`event.detail.index`); switch panels from it. Arrow keys move
between tabs; press-and-drag moves the lens like iOS.

Other glass surfaces (floating buttons, toolbars, sheets): add `class="lg-glass"` with a capsule or large
radius and `data-lg-refract` for lensing. Tune per element with `data-lg-bezel` (refraction band, px),
`data-lg-refraction` (max shift, px) and `data-lg-blur` (px).

## Frameworks

- **React/Next/Vue/Svelte:** import the two CSS files globally; call `window.LiquidGlass.init()` after
  mount (or `LiquidGlass.refract(element)` in a ref callback / `onMounted`) for elements rendered later.
  Keep the classes; render the same markup from components.
- **Tailwind:** keep the tokens as CSS variables and reference them (`bg-[var(--lg-cell)]`); put the
  glass, toggle and segmented styles in a component layer rather than re-deriving them in utilities.
- Scroll containers: if the page scrolls inside an element instead of the window, mark it
  `data-lg-scroll` so the inline title follows it.

## Accessibility

- `prefers-reduced-transparency` (Chromium; ignored where unsupported): frostier tint, no refraction.
- `prefers-contrast: more`: solid glass with a visible border; stronger secondary text.
- `prefers-reduced-motion`: no lens lift, no gel overshoot; instant thumb moves.
- Real semantics: `role="tablist"`/`tab` with `aria-selected`, `role="switch"` on the checkbox,
  radiogroup for segmented controls, visible focus rings.

## Performance

`backdrop-filter` with an SVG filter re-filters the area behind the element every frame it changes.
Keep glass to a few small fixed elements (tab bar, toolbar, floating button); never apply it to large
panels, long lists or every card. Very large elements produce large displacement maps; prefer the
frosted style for anything bigger than a sheet.

## Verify

Open `demo.html` (or the app) in Chrome and in Safari or Firefox: check the rim refraction on the tab
bar as content scrolls under it, the inline title appearing over the strong top edge, the toggle knob
turning to glass while held, both themes (`data-theme="dark"` on `<html>` forces dark), and the
fallback in the non-Chromium browser.
