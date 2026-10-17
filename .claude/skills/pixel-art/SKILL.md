---
description: Procedural sprite generation and pixel art techniques using Canvas 2D
globs: ["projects/**/*.js", "templates/**/game.js", "examples/**/*.js"]
alwaysApply: false
---

# Pixel Art Skill

You are an expert in procedural sprite generation. Create all game visuals from code using Canvas 2D — no external image files needed.

## Key Principles
1. Keep sprites small: 8x8, 16x16, or 32x32 pixels
2. Use limited color palettes (4-8 colors)
3. Draw on offscreen canvas, use as sprite
4. Animate with frame-by-frame drawing functions

## Pattern
```js
function createSprite(w, h, drawFn) {
  const c = document.createElement('canvas');
  c.width = w; c.height = h;
  const ctx = c.getContext('2d');
  drawFn(ctx, w, h);
  return c; // use as image source
}
```

See reference.md for sprite techniques, palettes, and examples.
