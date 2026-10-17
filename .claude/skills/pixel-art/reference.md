# Pixel Art Reference

## Creating Sprites from Code

### Helper Function
```js
function createSprite(width, height, drawFn) {
  const canvas = document.createElement('canvas');
  canvas.width = width;
  canvas.height = height;
  const ctx = canvas.getContext('2d');
  ctx.imageSmoothingEnabled = false; // crisp pixels
  drawFn(ctx, width, height);
  return canvas;
}
```

### Player Character (16x16)
```js
const playerSprite = createSprite(16, 16, (ctx) => {
  // Body
  ctx.fillStyle = '#4488ff';
  ctx.fillRect(4, 4, 8, 8); // torso

  // Head
  ctx.fillStyle = '#ffcc88';
  ctx.fillRect(5, 1, 6, 5); // head

  // Eyes
  ctx.fillStyle = '#000';
  ctx.fillRect(6, 3, 2, 2);
  ctx.fillRect(10, 3, 2, 2);

  // Legs
  ctx.fillStyle = '#2244aa';
  ctx.fillRect(4, 12, 3, 4);
  ctx.fillRect(9, 12, 3, 4);

  // Arms
  ctx.fillStyle = '#ffcc88';
  ctx.fillRect(2, 5, 2, 5);
  ctx.fillRect(12, 5, 2, 5);
});
```

### Enemy Types

**Slime (8x8)**
```js
const slime = createSprite(8, 8, (ctx) => {
  ctx.fillStyle = '#44cc44';
  // Round body
  ctx.fillRect(1, 3, 6, 5);
  ctx.fillRect(2, 2, 4, 1);
  ctx.fillRect(0, 4, 8, 3);
  // Eyes
  ctx.fillStyle = '#fff';
  ctx.fillRect(2, 4, 2, 2);
  ctx.fillRect(5, 4, 2, 2);
  ctx.fillStyle = '#000';
  ctx.fillRect(3, 5, 1, 1);
  ctx.fillRect(6, 5, 1, 1);
});
```

**Skeleton (16x16)**
```js
const skeleton = createSprite(16, 16, (ctx) => {
  ctx.fillStyle = '#ddd';
  // Skull
  ctx.fillRect(5, 0, 6, 5);
  ctx.fillStyle = '#000';
  ctx.fillRect(6, 2, 2, 2); // eyes
  ctx.fillRect(10, 2, 2, 2);
  ctx.fillRect(7, 4, 3, 1); // mouth
  // Ribcage
  ctx.fillStyle = '#ddd';
  ctx.fillRect(6, 6, 5, 1);
  ctx.fillRect(5, 7, 7, 1);
  ctx.fillRect(6, 8, 5, 1);
  ctx.fillRect(5, 9, 7, 1);
  // Limbs
  ctx.fillRect(3, 6, 2, 6);
  ctx.fillRect(12, 6, 2, 6);
  ctx.fillRect(6, 11, 2, 5);
  ctx.fillRect(10, 11, 2, 5);
});
```

### Items

**Coin (8x8)**
```js
const coin = createSprite(8, 8, (ctx) => {
  ctx.fillStyle = '#ffcc00';
  ctx.fillRect(2, 0, 4, 1);
  ctx.fillRect(1, 1, 6, 6);
  ctx.fillRect(2, 7, 4, 1);
  // Shine
  ctx.fillStyle = '#ffee66';
  ctx.fillRect(2, 2, 2, 2);
  // Detail
  ctx.fillStyle = '#cc9900';
  ctx.fillRect(4, 2, 1, 4);
});
```

**Heart (8x8)**
```js
const heart = createSprite(8, 8, (ctx) => {
  ctx.fillStyle = '#ff2244';
  ctx.fillRect(1, 1, 2, 2);
  ctx.fillRect(5, 1, 2, 2);
  ctx.fillRect(0, 2, 8, 3);
  ctx.fillRect(1, 5, 6, 1);
  ctx.fillRect(2, 6, 4, 1);
  ctx.fillRect(3, 7, 2, 1);
});
```

**Key (8x8)**
```js
const key = createSprite(8, 8, (ctx) => {
  ctx.fillStyle = '#ffcc00';
  ctx.fillRect(1, 0, 3, 3);
  ctx.fillRect(2, 1, 1, 1); // hole
  ctx.fillStyle = '#ffcc00';
  ctx.fillRect(3, 3, 1, 5);
  ctx.fillRect(4, 5, 2, 1);
  ctx.fillRect(4, 7, 2, 1);
});
```

## Color Palettes

### NES-Style (25 colors)
```js
const NES = {
  black: '#000000', darkGray: '#6d6d6d', lightGray: '#b6b6b6', white: '#ffffff',
  red: '#cc0000', orange: '#ee7700', yellow: '#eeee00', green: '#00cc00',
  cyan: '#00cccc', blue: '#0000cc', purple: '#cc00cc', pink: '#ff7777',
  darkRed: '#880000', darkGreen: '#008800', darkBlue: '#000088',
  skin: '#ffcc88', brown: '#884400'
};
```

### Neon Palette
```js
const NEON = {
  bg: '#0a0a2e', darkBg: '#050520',
  cyan: '#00ffff', magenta: '#ff00ff', yellow: '#ffff00',
  green: '#00ff66', pink: '#ff3388', blue: '#3388ff',
  orange: '#ff8800', purple: '#8833ff', white: '#ffffff'
};
```

### Earthy/Dungeon Palette
```js
const DUNGEON = {
  bg: '#1a1a2e', floor: '#2a2a3e', wall: '#444466',
  stone: '#666688', gold: '#ffcc00', blood: '#cc2222',
  skin: '#ffcc88', bone: '#ddddcc', wood: '#885522',
  grass: '#448844', water: '#4466cc', fire: '#ff6622'
};
```

### Pastel Palette
```js
const PASTEL = {
  pink: '#ffb3ba', orange: '#ffdfba', yellow: '#ffffba',
  green: '#baffc9', blue: '#bae1ff', purple: '#e8baff',
  white: '#ffffff', gray: '#cccccc'
};
```

## Animation Principles

### Frame-by-Frame Sprite Animation
```js
import { SpriteSheet, Animator } from '../../lib/graphics/sprite.js';

const sheet = SpriteSheet.fromCanvas((ctx, frameIndex) => {
  // Draw different frames based on frameIndex
  switch (frameIndex) {
    case 0: drawPlayerStand(ctx); break;
    case 1: drawPlayerRun1(ctx); break;
    case 2: drawPlayerRun2(ctx); break;
    case 3: drawPlayerJump(ctx); break;
  }
}, 16, 16, 4);

const anim = new Animator(sheet, {
  idle: { frames: [0], speed: 1 },
  run: { frames: [1, 2], speed: 0.1 },
  jump: { frames: [3], speed: 1 }
});
```

### Squash & Stretch
```js
// On landing
scaleX = 1.3; scaleY = 0.7; // squash
// Tween back to normal
tween({scaleX: 1.3, scaleY: 0.7}, {scaleX: 1, scaleY: 1}, 0.15);

// On jumping
scaleX = 0.7; scaleY = 1.3; // stretch
```

### Simple Bob Animation
```js
// Floating items (coins, powerups)
const bobOffset = Math.sin(time * 3) * 4; // 4px amplitude
ctx.drawImage(sprite, x, y + bobOffset);
```

### Flashing (Invincibility/Damage)
```js
// Flash on/off every 100ms
if (invincible) {
  if (Math.floor(time * 10) % 2 === 0) return; // skip draw
}
```

## Procedural Backgrounds

### Starfield
```js
const stars = Array.from({length: 100}, () => ({
  x: Math.random() * width,
  y: Math.random() * height,
  size: Math.random() * 2 + 0.5,
  speed: Math.random() * 50 + 20,
  brightness: Math.random()
}));

function drawStars(ctx, dt) {
  stars.forEach(s => {
    s.y += s.speed * dt;
    if (s.y > height) { s.y = 0; s.x = Math.random() * width; }
    ctx.fillStyle = `rgba(255,255,255,${s.brightness})`;
    ctx.fillRect(s.x, s.y, s.size, s.size);
  });
}
```

### Parallax Mountains
```js
function drawMountains(ctx, cameraX) {
  // Far layer (slow)
  ctx.fillStyle = '#1a1a3e';
  for (let i = 0; i < 10; i++) {
    const x = i * 200 - (cameraX * 0.2) % 200;
    drawTriangle(ctx, x, 400, 200, 150);
  }

  // Near layer (faster)
  ctx.fillStyle = '#2a2a4e';
  for (let i = 0; i < 10; i++) {
    const x = i * 150 - (cameraX * 0.5) % 150;
    drawTriangle(ctx, x, 450, 150, 100);
  }
}
```

### Grid Background
```js
function drawGrid(ctx, cellSize, color, alpha) {
  ctx.strokeStyle = color;
  ctx.globalAlpha = alpha;
  ctx.lineWidth = 1;
  for (let x = 0; x < width; x += cellSize) {
    ctx.beginPath(); ctx.moveTo(x, 0); ctx.lineTo(x, height); ctx.stroke();
  }
  for (let y = 0; y < height; y += cellSize) {
    ctx.beginPath(); ctx.moveTo(0, y); ctx.lineTo(width, y); ctx.stroke();
  }
  ctx.globalAlpha = 1;
}
```

## Tips
1. **Start simple** — Rectangle with eyes = recognizable character
2. **Limit palette** — 4-6 colors per sprite for cohesion
3. **Add outlines** — 1px dark outline makes sprites pop against backgrounds
4. **Symmetry** — Mirror sprites horizontally for left/right facing
5. **Silhouette** — If the silhouette is readable, the sprite works
6. **Scale with ctx.drawImage** — Draw small, render large with `image-rendering: pixelated`
