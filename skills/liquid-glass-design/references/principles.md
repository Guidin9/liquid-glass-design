# Liquid Glass principles

A condensed digest of Apple's public guidance, paraphrased, with the reasoning behind each rule.
Read the section you need; the SKILL.md summary covers the everyday cases.

Sources:
- Apple, *Adopting Liquid Glass*: https://developer.apple.com/documentation/technologyoverviews/adopting-liquid-glass
- Apple Human Interface Guidelines, *Materials*: https://developer.apple.com/design/human-interface-guidelines/materials
- WWDC25 session 219, *Meet Liquid Glass*: https://developer.apple.com/videos/play/wwdc2025/219/
- Apple, *Applying Liquid Glass to custom views* (SwiftUI): https://developer.apple.com/documentation/swiftui/applying-liquid-glass-to-custom-views

## Contents
1. What Liquid Glass is
2. Layers: content vs navigation
3. Variants: regular and clear
4. Tinting and color
5. Shape: capsules, concentricity, continuous corners
6. Scroll edge effects and legibility
7. Navigation: tab bars, toolbars, sidebars
8. Controls
9. Lists, forms and section headers
10. Sheets, popovers, menus, alerts
11. Motion and behavior
12. Adaptivity
13. Accessibility
14. Typography
15. App icons

## 1. What Liquid Glass is

A dynamic material that combines the optical properties of glass (it bends, concentrates and reflects
light) with fluidity (it flexes, morphs and responds). It forms a distinct functional layer for
controls and navigation and adapts continuously to what is behind it, so the content stays the focus.

Its visual signature is **lensing**: light is bent at the edges of the shape, so the content behind is
displaced and magnified near the rim rather than simply blurred. Specular highlights trace the
geometry, and a soft shadow separates it from the content. A plain blur plus transparency
("glassmorphism") lacks the lensing and the highlights; where a platform can't refract, add a specular
rim and accept the approximation.

## 2. Layers: content vs navigation

- Liquid Glass is reserved for the **navigation layer**: tab bars, toolbars, sidebars, floating action
  buttons, sheets, popovers and menus that float above the content.
- **Do not use it in the content layer.** Cards, list cells, backgrounds and charts use opaque fills or
  the standard (non-Liquid) materials. Mixing glass into content creates a confusing hierarchy.
- Exception: controls that live in content and have a transient interactive part (slider thumb, toggle
  knob) take on the glass look *while being touched* to emphasize the interaction.
- **Avoid glass on glass.** Stacking glass elements makes the interface cluttered and confusing. If two
  glass elements must coexist, keep them side by side, not overlapping, and let them merge when they
  come close (SwiftUI's container does this).
- Establish a clear navigation structure that is visibly separate from the content.
- **Steady state:** when a screen first appears, avoid content intersecting the glass.

## 3. Variants: regular and clear

- **Regular** (default): blurs and adjusts the luminosity of what is behind it so foreground text and
  symbols stay legible in any context. Use it for almost everything, and always for components with a
  lot of text (alerts, sidebars, popovers).
- **Clear**: highly translucent, to keep visually rich backgrounds prominent. Use it only when all three
  hold: the element floats over media-rich content; a dimming layer won't hurt that content; and the
  symbols/text on top are bold and bright. If the media is bright, put a dark dimming layer of about 35%
  opacity under the controls.
- Under glass, structure the content with the standard materials and vibrancy. Choose a material by its
  meaning, not the color it happens to produce, since system settings can change its appearance.
  Thicker = more opaque and better for fine text; thinner = more context visible.

## 4. Tinting and color

- Tint only to emphasize **primary** elements and actions (the selected tab, the main call to action).
  When every element is tinted, nothing stands out.
- Be judicious with color in controls and navigation so they stay legible. Prefer system colors; custom
  colors need light, dark and increased-contrast variants.
- Symbols and text on glass flip between light and dark with the glass to keep contrast.

## 5. Shape: capsules, concentricity, continuous corners

- The hardware's shape informs the curvature of controls: many controls became rounder (capsules) so they
  nestle into the rounded corners of windows and displays.
- Nested shapes are **concentric**: an inner element's corner radius = outer radius − the inset between
  them. This keeps the curves parallel.
- Use continuous-curvature corners (squircles), not circular arcs; that subtle difference is part of the
  Apple look.
- Lists' sections got larger corner radii to match the rounder controls.

## 6. Scroll edge effects and legibility

- Where content scrolls under a bar, a scroll edge effect gently dissolves the content (blur plus reduced
  opacity) so the bar and its labels stay legible and the glass reads as floating above moving content.
- System bars apply it automatically; custom bars with controls or text over scrolling content need it
  too.
- Don't add custom opaque backgrounds to bars; that fights the glass and the edge effect.

## 7. Navigation: tab bars, toolbars, sidebars

- Tab bars and sidebars float in the glass layer. On iPhone the tab bar is a floating capsule inset from
  the screen edges; it can **minimize on scroll** and expand when scrolling back.
- The **selection indicator** is itself a lens: on touch it lifts, magnifies and can be dragged between
  tabs, morphing as it moves. The selected tab is the tinted one.
- Search gets its own tab or field at the bottom/trailing end on iPhone.
- **Toolbars group** related items on one shared glass background (e.g. Undo+Redo together, Markup+More
  together); separate groups with fixed spacing. Don't mix text and icons inside one group. Prefer
  standard icons for common actions, and give every icon an accessibility label.
- Hide a toolbar item by hiding the item itself, never by leaving an empty glass slot.
- Sidebars: content can appear to extend beneath them (background extension: mirrored, blurred edge).

## 8. Controls

- Controls come alive on interaction: slider and toggle knobs turn into glass while touched; buttons
  morph into the menus/popovers they open; segmented controls slide a capsule thumb.
- Use the standard glass button styles (glass, prominent glass) rather than building custom glass
  buttons where the platform provides them.
- Keep standard spacing; avoid overcrowding and overlapping glass controls.
- Controls also have an extra-large size for more room for labels.

## 9. Lists, forms and section headers

- Lists, tables and forms have larger row heights and padding so content can breathe.
- Section corners are rounder (inset grouped style).
- Section headers use **title-style capitalization**, not all caps.
- Use footers in secondary color for explanations; values trail in secondary color.

## 10. Sheets, popovers, menus, alerts

- Sheets have a larger corner radius; half sheets are inset from the screen edges so content peeks
  through, and become more opaque when expanded to full height to keep focus on the task.
- Check content near the rounder sheet corners.
- Action sheets originate from the control that triggered them, not from the bottom edge.
- Menus adopt glass and show icons for common actions; their top actions match the item's swipe actions.
- Remove custom background views from popovers and sheets so they match the system.

## 11. Motion and behavior

- **Materialize:** glass appears and disappears by gradually modulating its light bending and lensing,
  not by popping in.
- **Fluidity:** glass morphs between shapes and contexts (a button becoming a menu, the tab selection
  moving between tabs); related glass elements merge when they get close.
- **Flex and energize:** on touch, glass flexes with a gel-like give and glows from within at the point of
  contact.
- Motion should answer interaction. No decorative looping animation.

## 12. Adaptivity

- Small glass elements (tab bars, nav bars) flip between light and dark appearances based on what is
  behind them; large ones (menus, sidebars) adapt but don't flip.
- Tint and dynamic range shift automatically to keep symbols legible while letting as much content show
  as possible.
- Shadows grow more opaque over text to keep separation.
- People can choose a preferred glass look (clearer or more tinted) in system settings; test custom
  elements under each.

## 13. Accessibility

- **Reduce Transparency:** glass becomes frostier and hides more of the content behind it.
- **Increase Contrast:** elements become predominantly black or white with a contrasting border.
- **Reduce Motion:** effects are toned down and elastic behavior is disabled.
- Test custom elements, colors and animations under each of these settings.
- Every icon-only control needs an accessibility label; keep real semantics (a decorated button is still
  a button).

## 14. Typography

- Apple platforms use SF Pro (SF Pro Display at large sizes, SF Pro Text at small), SF Pro Rounded for
  some numerals and widgets. Large titles are bold and left-aligned.
- iOS default text sizes (pt): Large Title 34, Title 1 28, Title 2 22, Title 3 20, Headline 17 semibold,
  Body 17, Callout 16, Subheadline 15, Footnote 13, Caption 1 12, Caption 2 11.
- SF Pro and SF Symbols are licensed for Apple platforms only. Elsewhere use Inter (closest open
  equivalent, with an optical-size axis for text and display cuts) and an open icon set (Material
  Symbols Rounded, filled, or Lucide).

## 15. App icons

- Icons are layered: foreground, middle and background layers of solid, filled, partly translucent
  shapes; the system adds the glass lighting, refraction and shadow.
- Keep the design simple and centered; don't bake in blur, gloss or shadows.
- Provide default (light), dark, clear and tinted variants where the platform supports them; on Android
  supply an adaptive icon with a monochrome layer.
