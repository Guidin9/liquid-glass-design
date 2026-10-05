# Review checklist

Go through this with real screenshots (device, emulator or browser), not by reading code. Capture:
light and dark, at rest and scrolled, a pressed state of the glass element, and reduced transparency if
the platform has the setting. Fix every "no" before calling the UI done.

## Layers
- [ ] Glass appears only on navigation-layer elements (tab bar, toolbar, sheet, menu, floating button)?
- [ ] No cards, list rows, backgrounds or charts are glass?
- [ ] No glass element overlaps another glass element?
- [ ] At most two or three glass surfaces on screen at once?
- [ ] At first appearance, nothing important sits under the glass (bottom clearance present)?

## Glass quality
- [ ] Content visibly scrolls beneath the glass, so it reads as glass, not a gray pill?
- [ ] The edge shows lensing (displacement near the rim) where the platform supports it, or at least a
      specular rim highlight and soft shadow where it doesn't?
- [ ] Labels and icons on the glass are legible over both the lightest and darkest content behind it?
- [ ] Pressing the glass gives feedback (flex/scale, glow); the selection lens moves with a spring?

## Color and type
- [ ] Only the selected tab / primary action carries the tint; everything else is neutral?
- [ ] Semantic colors with correct light and dark values (grouped background, cell, label hierarchy)?
- [ ] SF on Apple platforms, Inter elsewhere (no SF Pro / SF Symbols shipped outside Apple platforms)?
- [ ] Type follows the scale; live numbers use tabular figures; large title bold, left-aligned?
- [ ] Section headers in title case, footers in secondary color?

## Shape and layout
- [ ] Continuous corners; groups radius ~26; controls are capsules?
- [ ] Nested shapes concentric (inner radius = outer radius − inset)?
- [ ] Rows ≥ 52 tall, separators hairline and inset to the text?
- [ ] Inline title appears on scroll and stays legible over the content (strong top edge effect)?
- [ ] Bottom scroll edge effect dissolves content behind the floating bar?

## Behavior and accessibility
- [ ] Reduced transparency → frostier, more opaque glass, no lensing?
- [ ] Increased contrast → solid fills and visible borders?
- [ ] Reduced motion → no squash/stretch or morphing; short cross-fades instead?
- [ ] Every icon-only control has an accessibility label; tabs expose selected state; toggles expose
      on/off?
- [ ] Touch targets ≥ 44?
- [ ] Glass effects limited to small surfaces (no full-screen or per-row glass) and scrolling stays smooth?

## Copy
- [ ] Short, plain, sentence-case wording in the user's language; actions named by what they do?
- [ ] No Apple logos or product names presented as the app's own UI?
