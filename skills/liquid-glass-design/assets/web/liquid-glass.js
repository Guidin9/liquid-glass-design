/* Liquid Glass for the web.
   - Real refraction on Chromium (Chrome, Edge, Electron, WebView2): an SVG displacement map, generated from the
     element's rounded shape, is applied inside `backdrop-filter`. Other browsers keep the CSS frosted fallback.
   - Inline title and top scroll edge effect driven by scroll position.
   - Floating tab bar: keyboard + click selection and a press-and-drag selection lens.
   Mark glass elements with `data-lg-refract`; optional data-lg-bezel / data-lg-refraction / data-lg-blur (px). */
(() => {
  const SVG_NS = "http://www.w3.org/2000/svg";
  const reduceTransparency = matchMedia("(prefers-reduced-transparency: reduce)");
  const reduceMotion = matchMedia("(prefers-reduced-motion: reduce)");
  // Only Chromium renders SVG filters inside backdrop-filter; elsewhere the url() would be ignored or break the blur.
  const canRefract = !!navigator.userAgentData?.brands?.some((b) => b.brand === "Chromium");

  let defs;
  let filterCount = 0;
  const observed = new Map();

  function ensureDefs() {
    if (defs) return defs;
    const svg = document.createElementNS(SVG_NS, "svg");
    svg.setAttribute("aria-hidden", "true");
    svg.style.cssText = "position:absolute;width:0;height:0;overflow:hidden";
    defs = document.createElementNS(SVG_NS, "defs");
    svg.appendChild(defs);
    document.body.appendChild(svg);
    return defs;
  }

  /* Displacement map for a rounded rectangle: inside a band along the rim, each pixel samples from further
     inward, so content near the edge is pulled and magnified like light bending through a lens.
     R/G encode the x/y displacement around a neutral 128. */
  function displacementMap(w, h, radius, bezel) {
    const canvas = document.createElement("canvas");
    canvas.width = w;
    canvas.height = h;
    const ctx = canvas.getContext("2d");
    const img = ctx.createImageData(w, h);
    const hw = w / 2;
    const hh = h / 2;
    const r = Math.min(radius, hw, hh);
    for (let y = 0; y < h; y++) {
      for (let x = 0; x < w; x++) {
        const px = x + 0.5 - hw;
        const py = y + 0.5 - hh;
        const qx = Math.abs(px) - (hw - r);
        const qy = Math.abs(py) - (hh - r);
        const sdf = Math.hypot(Math.max(qx, 0), Math.max(qy, 0)) + Math.min(Math.max(qx, qy), 0) - r;
        const depth = -sdf; // distance from the edge, positive inside
        let nx = 0;
        let ny = 0;
        let m = 0;
        if (depth > 0 && depth < bezel) {
          if (qx > 0 && qy > 0) {
            const l = Math.hypot(qx, qy) || 1;
            nx = (qx / l) * Math.sign(px);
            ny = (qy / l) * Math.sign(py);
          } else if (qx > qy) {
            nx = Math.sign(px);
          } else {
            ny = Math.sign(py);
          }
          const t = 1 - depth / bezel;
          m = t * t; // strongest at the rim, fading inward
        }
        const i = (y * w + x) * 4;
        img.data[i] = 128 - nx * m * 127;
        img.data[i + 1] = 128 - ny * m * 127;
        img.data[i + 2] = 128;
        img.data[i + 3] = 255;
      }
    }
    ctx.putImageData(img, 0, 0);
    return canvas.toDataURL();
  }

  function buildFilter(el, id) {
    const w = Math.round(el.offsetWidth);
    const h = Math.round(el.offsetHeight);
    if (!w || !h) return;
    const radius = parseFloat(getComputedStyle(el).borderTopLeftRadius) || 0;
    const bezel = Math.min(Number(el.dataset.lgBezel) || 24, h / 2, w / 2);
    const amount = Number(el.dataset.lgRefraction) || 20;
    const blur = Number(el.dataset.lgBlur) || 3;

    let filter = ensureDefs().querySelector(`#${id}`);
    if (!filter) {
      filter = document.createElementNS(SVG_NS, "filter");
      filter.id = id;
      defs.appendChild(filter);
    }
    for (const [k, v] of Object.entries({
      x: 0, y: 0, width: w, height: h,
      filterUnits: "userSpaceOnUse",
      primitiveUnits: "userSpaceOnUse",
      "color-interpolation-filters": "sRGB",
    })) filter.setAttribute(k, v);
    filter.innerHTML =
      `<feGaussianBlur in="SourceGraphic" stdDeviation="${blur}" result="blurred"/>` +
      `<feImage href="${displacementMap(w, h, radius, bezel)}" x="0" y="0" width="${w}" height="${h}" preserveAspectRatio="none" result="map"/>` +
      `<feDisplacementMap in="blurred" in2="map" scale="${amount * 2}" xChannelSelector="R" yChannelSelector="G"/>`;
    el.style.backdropFilter = `url(#${id}) saturate(var(--lg-glass-saturate))`;
  }

  function refract(el) {
    if (!canRefract || reduceTransparency.matches || observed.has(el)) return;
    const id = `lg-refract-${++filterCount}`;
    let last = "";
    const ro = new ResizeObserver(() => {
      const key = `${el.offsetWidth}x${el.offsetHeight}`;
      if (key !== last) {
        last = key;
        buildFilter(el, id);
      }
    });
    ro.observe(el);
    observed.set(el, { ro, id });
  }

  function unrefractAll() {
    for (const [el, { ro, id }] of observed) {
      ro.disconnect();
      el.style.backdropFilter = "";
      defs?.querySelector(`#${id}`)?.remove();
    }
    observed.clear();
  }

  /* Inline title + top scroll edge effect. Uses window scroll, or the element marked data-lg-scroll. */
  function bindTopbar() {
    const bar = document.querySelector(".lg-topbar");
    if (!bar) return;
    const scroller = document.querySelector("[data-lg-scroll]");
    const target = scroller || window;
    let ticking = false;
    const apply = () => {
      ticking = false;
      const y = scroller ? scroller.scrollTop : window.scrollY;
      bar.style.setProperty("--lg-edge", Math.min(y / 24, 1).toFixed(3));
      bar.style.setProperty("--lg-inline", Math.min(Math.max((y - 40) / 16, 0), 1).toFixed(3));
    };
    target.addEventListener("scroll", () => {
      if (!ticking) {
        ticking = true;
        requestAnimationFrame(apply);
      }
    }, { passive: true });
    apply();
  }

  /* Tab bar: role=tablist with role=tab buttons. Fires `lg-tabchange` with detail.index. */
  function bindTabbar(bar) {
    const tabs = [...bar.querySelectorAll('[role="tab"]')];
    if (!tabs.length) return;
    bar.style.setProperty("--lg-tabs", tabs.length);
    let current = Math.max(0, tabs.findIndex((t) => t.getAttribute("aria-selected") === "true"));

    const select = (i, focus = false) => {
      current = i;
      tabs.forEach((t, k) => {
        t.setAttribute("aria-selected", String(k === i));
        t.tabIndex = k === i ? 0 : -1;
      });
      bar.style.setProperty("--lg-index", i);
      if (focus) tabs[i].focus();
      bar.dispatchEvent(new CustomEvent("lg-tabchange", { detail: { index: i }, bubbles: true }));
    };
    select(current);

    tabs.forEach((t, i) => t.addEventListener("click", () => select(i)));
    bar.addEventListener("keydown", (e) => {
      const step = { ArrowRight: 1, ArrowLeft: -1 }[e.key];
      if (!step) return;
      e.preventDefault();
      select((current + step + tabs.length) % tabs.length, true);
    });

    // Press lifts the lens into glass; dragging moves it between tabs; release snaps to the nearest one.
    let pressed = false;
    let moved = false;
    let startX = 0;
    let dragIndex = current;
    bar.addEventListener("pointerdown", (e) => {
      if (reduceMotion.matches || e.button !== 0) return;
      pressed = true;
      moved = false;
      startX = e.clientX;
      bar.classList.add("is-pressed");
    });
    window.addEventListener("pointermove", (e) => {
      if (!pressed) return;
      if (!moved && Math.abs(e.clientX - startX) < 6) return;
      moved = true;
      bar.classList.add("is-dragging");
      const rect = bar.getBoundingClientRect();
      const tabWidth = (rect.width - 8) / tabs.length;
      dragIndex = Math.min(Math.max((e.clientX - rect.left - 4 - tabWidth / 2) / tabWidth, 0), tabs.length - 1);
      bar.style.setProperty("--lg-index", dragIndex.toFixed(3));
    });
    const release = () => {
      if (!pressed) return;
      pressed = false;
      bar.classList.remove("is-pressed", "is-dragging");
      if (moved) select(Math.round(dragIndex));
    };
    window.addEventListener("pointerup", release);
    window.addEventListener("pointercancel", release);
  }

  function init(root = document) {
    root.querySelectorAll("[data-lg-refract]").forEach(refract);
    root.querySelectorAll(".lg-tabbar").forEach(bindTabbar);
    bindTopbar();
  }

  reduceTransparency.addEventListener?.("change", () => {
    if (reduceTransparency.matches) unrefractAll();
    else document.querySelectorAll("[data-lg-refract]").forEach(refract);
  });

  window.LiquidGlass = { init, refract, canRefract };
  if (document.readyState === "loading") document.addEventListener("DOMContentLoaded", () => init());
  else init();
})();
