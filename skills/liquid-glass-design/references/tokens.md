# Design tokens

Platform-neutral values. Templates in `assets/` already encode them; use this file when building from
scratch or porting to a new stack. Units: pt on Apple, dp/sp on Android, CSS px on the web, epx on
Windows; these line up closely in physical size.

## Color

iOS semantic system colors. Use the semantic role, not the hex, in code (one token per role, with a light
and a dark value). Alpha is written as the 0–1 opacity of the base color.

| Role | Light | Dark | Use |
|---|---|---|---|
| groupedBackground | `#F2F2F7` | `#000000` | Screen background behind inset groups |
| cell (secondaryGroupedBackground) | `#FFFFFF` | `#1C1C1E` | Group / card fill |
| tertiaryGroupedBackground | `#F2F2F7` | `#2C2C2E` | Nested fills inside cells |
| label | `#000000` | `#FFFFFF` | Primary text |
| secondaryLabel | `#3C3C43` @ 0.60 | `#EBEBF5` @ 0.60 | Values, subtitles, footers |
| tertiaryLabel | `#3C3C43` @ 0.30 | `#EBEBF5` @ 0.30 | Placeholders, axis labels |
| separator | `#3C3C43` @ 0.29 | `#545458` @ 0.60 | Hairlines between rows |
| fill (tertiarySystemFill) | `#767680` @ 0.12 | `#767680` @ 0.24 | Segmented track, pressed rows, toggle off |
| segmentThumb | `#FFFFFF` | `#636366` | Selected segment |
| glassSurface (regular) | `#FAFAFA` @ 0.40 | `#121212` @ 0.40 | Tint drawn on top of blurred backdrop |
| glassSelection | `#000000` @ 0.10 | `#FFFFFF` @ 0.10 | Resting selection pill inside a glass bar |
| green | `#34C759` | `#30D158` | Success, charging, positive, app tint example |
| blue | `#007AFF` | `#0A84FF` | Default tint, links, actions |
| orange | `#FF9500` | `#FF9F0A` | Warnings, secondary series |
| red | `#FF3B30` | `#FF453A` | Destructive, critical |
| yellow | `#FFCC00` | `#FFD60A` | Low-power style states |
| teal / indigo / pink | `#30B0C7` / `#5856D6` / `#FF2D55` | `#40C8E0` / `#5E5CE6` / `#FF375F` | Extra series, icon tiles |

Rules of thumb:
- Pick **one** app tint (usually blue, or a color that matches the subject: green for energy/health,
  orange for activity). Use it for the selected tab, primary buttons, toggles' on state and links.
- Settings-style icon tiles: white glyph on a solid system color tile (30pt, continuous 8pt corners).
- Increased contrast: drop translucency (solid `cell` fills), darken secondary label to ~0.75, add a
  1px border (`label` @ 0.35) around glass and controls.

## Type

Apple platforms: system font (SF Pro). Everywhere else: **Inter** (variable font with `opsz` and `wght`
axes, OFL license, https://fonts.google.com/specimen/Inter). Use the text optical size (`opsz` 16)
below 20pt and the display optical size (`opsz` 32) at 20pt and above, mirroring SF Text / SF Display.
Use tabular figures (`tnum`) for any number that updates live.

| Style | Size / line height | Weight | Tracking with Inter |
|---|---|---|---|
| Large Title | 34 / 41 | Bold 700 | −0.6 |
| Title 1 | 28 / 34 | Bold 700 | −0.5 |
| Title 2 | 22 / 28 | Bold 700 | −0.4 |
| Title 3 | 20 / 25 | Semibold 600 | −0.3 |
| Headline | 17 / 22 | Semibold 600 | −0.3 |
| Body | 17 / 22 | Regular 400 | −0.3 |
| Callout | 16 / 21 | Regular 400 | −0.25 |
| Subheadline | 15 / 20 | Regular 400 | −0.2 |
| Footnote | 13 / 18 | Regular 400 | −0.1 |
| Caption 1 | 12 / 16 | Regular 400 | 0 |
| Caption 2 | 11 / 13 | Medium 500 | 0 |
| Tab label | 10 / 12 | Semibold 600 | 0 |
| Hero number | 44–52 / +4 | Semibold 600, `tnum` | −1.0 |

Web font stack: `-apple-system, BlinkMacSystemFont, "Inter", "Segoe UI", Roboto, sans-serif`, so Apple
devices get SF and every other device gets Inter.

## Shape

- **Continuous corners everywhere** (superellipse/squircle). Circular-arc rounded rects are a fallback.
- Inset group / card: radius **26**. Hero card can match.
- Controls: **capsule** (radius = height / 2) for buttons, segmented controls, toggles, search fields,
  floating tab bars and toolbars.
- Icon tile: 30 × 30, radius 8. Small chips: capsule.
- Sheets: radius ~ 34–38 (concentric with the device corners), half sheets inset 8–10 from the edges.
- **Concentricity:** inner radius = outer radius − inset. A 64-tall capsule bar (radius 32) with 4 inset
  holds a 56-tall selection capsule (radius 28).

## Spacing and layout

| Token | Value |
|---|---|
| Screen side margin | 16 (20 on large phones / tablets) |
| Large title | 4 extra leading inset, 44 below the status bar, 4 below the title |
| Section spacing | 28 above a header / group |
| Section header → group | 8 |
| Group → footer | 8 |
| Row height | min 52, 16 horizontal / 10 vertical padding |
| Separator | 1 physical pixel, inset 16 from the leading edge (58 when rows have icon tiles) |
| Floating tab bar | height 64, inset 12 above the home indicator, ~216 wide for 2 tabs (~104 per tab) |
| Content bottom clearance | bar height + bottom inset + 24 (≈ 104) so nothing rests under glass |
| Tap targets | ≥ 44 × 44 |

## Glass parameters

Library-neutral starting points for a regular-variant floating bar. Tune by eye on a real device over
real content.

| Parameter | Value |
|---|---|
| Backdrop blur | 8 (frosted-only platforms: 16–24) |
| Saturation / vibrancy | ×1.6–1.8 |
| Lens (refraction) band | 24 deep, 24 amount at the rim; selection lens 10/14 while pressed |
| Surface tint | glassSurface (see Color) |
| Specular rim | 0.5–1 px, white, brighter top-left (~0.5) to dimmer bottom-right (~0.15) |
| Shadow | radius 24, y offset 4, black @ 0.10 (rises over text) |
| Press response | scale up by ~16 px across the width; inner glow at the touch point |
| Reduced transparency | blur 24+, tint alpha 0.92, no lens |

Clear variant: blur 0–2, tint alpha 0.05–0.10, bold white symbols, plus a 35% black dimming layer
behind when the media is bright.

## Motion

| Interaction | Spec |
|---|---|
| Selection lens moving between tabs | spring, damping 1.0, stiffness ~1000 (snappy, no overshoot) |
| Press flex (scale, squash/stretch) | spring, damping 0.6–0.7, stiffness ~250 (gel-like give) |
| Segmented thumb | spring, damping ~0.82, stiffness ~520 |
| Value fills (rings, bars) on first show | 0.9 s ease-out, once |
| Materialize glass | fade + lens strength 0 → 1 over ~250 ms |
| Reduced motion | no squash/stretch or morph; cross-fade ≤ 150 ms |
