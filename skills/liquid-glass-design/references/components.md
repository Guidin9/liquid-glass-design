# Components

Platform-neutral anatomy and behavior. Each entry says whether it is glass (navigation layer) or solid
(content layer). Platform guides show how to build them; templates implement the starred ones.

## Contents
- Navigation layer: floating tab bar*, toolbar buttons and groups, sheets, menus and popovers, search
- Content layer: large-title page*, inset grouped list and rows*, hero/summary card, charts, notices*,
  empty and error states
- Controls: toggle*, segmented control*, slider, buttons

---

## Navigation layer (glass)

### Floating tab bar *
- **Anatomy:** a regular-glass capsule (height 64) floating above the bottom inset, inset from the screen
  edges; 2–5 tabs, each an icon (24) over a label (10 semibold); a selection capsule (height 56,
  concentric) behind the active tab.
- **Color:** only the active tab's icon and label take the app tint; others use `label`.
- **Behavior:** tap switches tabs; the selection lens slides with a snappy spring. Pressing lifts the
  lens (scales ~1.2–1.4), turns it into refracting glass with chromatic edges, and lets the finger drag
  it between tabs; release snaps to the nearest tab. The whole bar flexes slightly under the finger and
  glows at the touch point.
- **Width:** fill the width for 4–5 tabs; for 2–3 tabs a compact centered capsule (~104 per tab) looks
  better than a long, mostly empty pill.
- **Optional:** minimize on scroll down (shrink to the selected tab only), expand on scroll up.
- **Don't:** put a solid background behind it; tint every tab; stack a second glass element over it.

### Toolbar buttons and groups
- Glass circles (44) for single icon actions, glass capsules for groups. Group actions that belong
  together on one shared glass background; separate groups with a fixed gap (~12).
- Icons only for common actions (with accessibility labels); never mix text and icons within one group.
- A primary action may use the prominent (tinted) glass style; at most one per screen.

### Sheets
- Glass sheet with large concentric corners; a half sheet is inset from the screen edges (~8) so content
  peeks around it, and becomes more opaque at full height.
- Grabber 36 × 5 capsule, `tertiaryLabel`. Content inside uses solid grouped styling.

### Menus and popovers
- Glass, rounded (radius ~ 20–26), grow out of the control that opened them (morph, not a slide from the
  bottom). Rows with leading icons for common actions, destructive items in red at the bottom.

### Search
- On phones: a glass capsule field at the bottom (or a search tab at the trailing end of the tab bar);
  it slides up with the keyboard.

---

## Content layer (solid)

### Large-title page *
- Large title (34 bold) left-aligned under the status bar; content scrolls in an inset column (side
  margin 16).
- On scroll the large title moves away and a small **inline title** (17 semibold) fades in, centered in the
  top bar area. There is no bar background: legibility comes from the **top scroll edge effect**, a
  gradient of the background color that is nearly opaque under the title and dissolves below it (plus
  blur where available). It appears only once content has scrolled underneath.
- A **bottom scroll edge effect** behind the floating tab bar dissolves content sliding under it.
- Bottom padding ≈ bar height + inset + 24 so the last row can scroll clear of the glass.

### Inset grouped list and rows *
- Group: `cell` fill, radius 26 continuous, inset from the screen margins; optional header above
  (title-case, Headline weight, `label` color in iOS 26 style) and footer below (Footnote,
  `secondaryLabel`).
- Rows (min height 52, padding 16/10), separated by hairlines inset to the text:
  - **Value row:** title (Body, `label`) … value (Body, `secondaryLabel`, tabular figures); optional
    secondary detail before the value in `tertiaryLabel`.
  - **Navigation row:** title … value … chevron (`tertiaryLabel`); the whole row highlights with `fill`
    when pressed (no ripple).
  - **Toggle row:** title (+ footnote subtitle) … toggle.
  - **Control row:** title above a full-width segmented control.
  - **Action row:** a single Body text in the tint color (or red for destructive), left aligned. After
    the action, the row can briefly read its confirmation ("Reset", "Done") in `secondaryLabel`.
  - **Icon row:** a 30-pt colored icon tile (white glyph) leading the title; separator inset 58.
- Destructive actions go in their own group at the bottom; confirm with an action sheet / dialog when
  irreversible.

### Notice (setup problem) *
- An icon row at the top of the screen: tile color by severity (green = needs starting, orange =
  restricted, red = blocked), title states the problem, subtitle the consequence, trailing tint-colored
  verb ("Turn On", "Allow", "Fix") that performs the fix. No banners or colored cards.

### Hero / summary card
- One opaque card that shows the screen's key state, e.g. a ring like Apple's Batteries widget (stroke
  ~22, round cap, track = same color at 0.18 alpha), a big tabular number (44–52 semibold) with a small unit,
  a one-line headline status and a secondary detail line under it.
- Animate fills once on first show (0.9 s ease-out), then follow the data.

### Charts (Apple style)
- Thin line (2) with round joins, a soft vertical gradient area fill (0.32 → 0.04 alpha), dashed
  horizontal gridlines (`separator`, 4/4) and a solid zero line. Axis labels in Caption 2 `tertiaryLabel`
  on the **trailing** side, rounded to clean values. Time axis as "2 min ago … Now".
- Positive and negative series in two system colors split at zero (e.g. green above, orange below); a
  dot marks the latest value.
- Empty state: one footnote sentence that says when data will appear.

### Empty and error states
- Centered SF-style symbol (48, `tertiaryLabel`), Title 3 headline, Body explanation in `secondaryLabel`,
  and at most one tinted action. Errors say what happened and how to fix it; they don't apologize.

---

## Controls

### Toggle *
- Track 64 × 28 capsule (iOS 26 proportions): `fill` when off, tint (often green) when on; white knob
  40 × 24 with a soft shadow, 2 inset.
- **While touched** the knob scales up (~1.5), turns into refracting glass showing the track color through
  it, and can be dragged; release snaps to the nearest end and toggles. Then it returns to solid white.
- A scroll gesture that steals the touch must not flip it.
- Semantics: switch role, on/off state, click action.

### Segmented control *
- Capsule track (`fill`, height ~32–36, 2 inset) with a capsule thumb (`segmentThumb`, soft shadow in
  light mode) sliding under the selected label with a spring. Labels Subheadline, semibold when
  selected. Up to ~5 short segments.
- Semantics: radio buttons with selected state.

### Slider
- Thin track (4) with the tint fill; round thumb (28) solid white with shadow; the thumb becomes glass
  and enlarges while dragged.

### Buttons
- **Glass** button (capsule, regular glass) for secondary actions on the navigation layer.
- **Prominent** button: tint-filled capsule, white label, for the single primary action.
- In content: tint-colored text buttons or tinted capsules; no glass.
