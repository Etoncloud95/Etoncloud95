# Game Patterns Reference

## Platformer Architecture

### Structure
```
Scenes: title → play → gameOver
Entities: player, enemies[], platforms[], coins[], projectiles[]
Systems: physics, collision (tilemap), camera, particles, audio
Data: level tiles (2D array), enemy spawn points, coin positions
```

### Level Data
```js
const LEVELS = [
  {
    tiles: [
      '                    ',
      '       C   C        ',
      '      =====    E    ',
      '  P              ===',
      '===================='
    ],
    legend: { '=': 'ground', 'P': 'playerSpawn', 'C': 'coin', 'E': 'enemy' }
  }
];
```

### Core Loop
```js
update(dt) {
  handleInput();
  applyPhysics(player, dt);
  moveEnemies(dt);
  checkTilemapCollision(player);
  checkEntityCollisions(); // player vs coins, enemies
  camera.follow(player);
  particles.update(dt);
}
```

### Key Features
- Coyote time + jump buffering (see game-physics skill)
- Tile-based collision with separate X/Y resolution
- Camera follows player with bounds clamping
- Moving platforms add their velocity to player

## Roguelike Architecture

### Structure
```
Scenes: title → play → gameOver
Systems: turn-based (process on player input), FOV, procgen
Data: dungeon grid, entity positions, inventory, floor number
```

### Turn-Based Loop
```js
// Only update when player acts
if (playerMoved) {
  movePlayer(dx, dy);
  enemies.forEach(e => e.takeTurn()); // enemies act after player
  updateFOV();
  turn++;
}
```

### Procedural Dungeon Generation (BSP)
```js
function generateDungeon(width, height) {
  const grid = Array(height).fill(null).map(() => Array(width).fill(1)); // all walls

  // Place random rooms
  const rooms = [];
  for (let i = 0; i < 8; i++) {
    const room = randomRoom(width, height);
    if (!overlapsAny(room, rooms)) {
      carveRoom(grid, room);
      rooms.push(room);
    }
  }

  // Connect rooms with corridors
  for (let i = 1; i < rooms.length; i++) {
    carveCorridor(grid, rooms[i-1].center, rooms[i].center);
  }

  // Place entities
  placePlayer(grid, rooms[0]);
  placeStairs(grid, rooms[rooms.length - 1]);
  placeEnemies(grid, rooms.slice(1));
  placeItems(grid, rooms);

  return grid;
}
```

### Field of View
```js
function calculateFOV(grid, px, py, radius) {
  const visible = new Set();
  const rays = 360;

  for (let i = 0; i < rays; i++) {
    const angle = (i / rays) * Math.PI * 2;
    let x = px, y = py;

    for (let d = 0; d < radius; d++) {
      x += Math.cos(angle);
      y += Math.sin(angle);
      const tx = Math.round(x), ty = Math.round(y);

      if (tx < 0 || ty < 0 || tx >= width || ty >= height) break;
      visible.add(`${tx},${ty}`);
      explored.add(`${tx},${ty}`);
      if (grid[ty][tx] === 1) break; // wall blocks sight
    }
  }
  return visible;
}
```

## Puzzle Architecture (Match-3)

### Structure
```
Scenes: title → play → gameOver
State Machine: idle → selected → swapping → checking → clearing → falling → checking...
Data: grid[row][col] = gemType, score, combo, timer
```

### State Machine
```js
const states = {
  idle: {
    // Wait for player input
    onClick(col, row) {
      if (selected) {
        if (isAdjacent(selected, {col, row})) {
          swap(selected, {col, row});
          state = 'swapping';
        } else {
          selected = {col, row};
        }
      } else {
        selected = {col, row};
      }
    }
  },
  swapping: {
    // Animate swap, then check matches
    onComplete() { state = 'checking'; }
  },
  checking: {
    // Find all matches (3+ in row/col)
    update() {
      const matches = findMatches(grid);
      if (matches.length > 0) {
        clearMatches(matches);
        combo++;
        score += matches.length * 10 * combo;
        state = 'clearing';
      } else {
        if (wasSwap && noMatches) {
          swapBack(); state = 'swapping';
        } else {
          combo = 0; state = 'idle';
        }
      }
    }
  },
  clearing: {
    // Animate clear, then apply gravity
    onComplete() { state = 'falling'; }
  },
  falling: {
    // Drop gems, fill from top
    onComplete() { state = 'checking'; } // chain check
  }
};
```

## Shooter Architecture

### Top-Down Shooter
```
Scenes: title → play → gameOver
Entities: player, bullets[] (pooled), enemies[], powerups[]
Systems: wave spawner, collision, camera, particles
```

### Wave Spawner
```js
class WaveSpawner {
  constructor() {
    this.wave = 0;
    this.spawnTimer = 0;
    this.enemiesRemaining = 0;
  }

  startWave() {
    this.wave++;
    this.enemiesRemaining = 3 + this.wave * 2;
    this.spawnTimer = 0;
  }

  update(dt) {
    if (this.enemiesRemaining <= 0 && enemies.length === 0) {
      // Wave complete
      this.startWave();
      return;
    }

    this.spawnTimer -= dt;
    if (this.spawnTimer <= 0 && this.enemiesRemaining > 0) {
      spawnEnemy(this.getEnemyType());
      this.enemiesRemaining--;
      this.spawnTimer = Math.max(0.5, 2.0 - this.wave * 0.1);
    }
  }

  getEnemyType() {
    if (this.wave >= 5 && Math.random() < 0.3) return 'tank';
    if (this.wave >= 3 && Math.random() < 0.4) return 'shooter';
    return 'basic';
  }
}
```

### Bullet Pool
```js
import { Pool } from '../../lib/utils/pool.js';

const bulletPool = new Pool(
  () => ({ pos: vec2(0,0), vel: vec2(0,0), active: false, damage: 1 }),
  (b) => { b.active = false; b.damage = 1; }
);

function shoot(x, y, angle, speed) {
  const b = bulletPool.get();
  b.pos.set(x, y);
  b.vel = Vec2.fromAngle(angle, speed);
  b.active = true;
}

// In update:
bulletPool.active.forEach(b => {
  b.pos.add(b.vel.clone().mul(dt));
  if (offScreen(b)) bulletPool.release(b);
});
```

### Bullet Patterns
```js
// Aimed shot
function aimedShot(enemy, target, speed) {
  const angle = Math.atan2(target.y - enemy.y, target.x - enemy.x);
  shoot(enemy.x, enemy.y, angle, speed);
}

// Spread shot
function spreadShot(x, y, centerAngle, count, spreadAngle, speed) {
  for (let i = 0; i < count; i++) {
    const angle = centerAngle + (i - (count-1)/2) * spreadAngle;
    shoot(x, y, angle, speed);
  }
}

// Circle pattern
function circleShot(x, y, count, speed) {
  for (let i = 0; i < count; i++) {
    shoot(x, y, (Math.PI * 2 / count) * i, speed);
  }
}

// Spiral pattern
let spiralAngle = 0;
function spiralShot(x, y, speed) {
  shoot(x, y, spiralAngle, speed);
  spiralAngle += 0.3;
}
```

## Space Shooter (Shmup)

### Scrolling Background
```js
const layers = [
  { stars: generateStars(50), speed: 20 },  // far
  { stars: generateStars(30), speed: 50 },  // mid
  { stars: generateStars(20), speed: 100 }, // near
];

function drawBackground(ctx, dt) {
  ctx.fillStyle = '#000';
  ctx.fillRect(0, 0, width, height);
  layers.forEach(layer => {
    layer.stars.forEach(s => {
      s.y += layer.speed * dt;
      if (s.y > height) { s.y = 0; s.x = Math.random() * width; }
      ctx.fillStyle = `rgba(255,255,255,${s.a})`;
      ctx.fillRect(s.x, s.y, s.size, s.size);
    });
  });
}
```

### Enemy Formations
```js
const formations = {
  line: (count, startX, y, spacing) =>
    Array.from({length: count}, (_, i) => ({x: startX + i * spacing, y})),

  v: (count, centerX, y, spacing) =>
    Array.from({length: count}, (_, i) => ({
      x: centerX + (i - (count-1)/2) * spacing,
      y: y + Math.abs(i - (count-1)/2) * spacing * 0.5
    })),

  circle: (count, centerX, centerY, radius) =>
    Array.from({length: count}, (_, i) => ({
      x: centerX + Math.cos((Math.PI*2/count)*i) * radius,
      y: centerY + Math.sin((Math.PI*2/count)*i) * radius
    }))
};
```

## Infinite Runner

### Procedural Generation
```js
class ChunkGenerator {
  constructor() {
    this.chunks = [];
    this.nextX = 0;
  }

  update(cameraX) {
    // Generate ahead of camera
    while (this.nextX < cameraX + width * 2) {
      this.generateChunk();
    }
    // Remove behind camera
    this.chunks = this.chunks.filter(c => c.x + c.width > cameraX - 100);
  }

  generateChunk() {
    const type = rng.pick(['flat', 'gap', 'steps', 'slide']);
    const chunk = CHUNK_TEMPLATES[type](this.nextX);
    this.chunks.push(chunk);
    this.nextX += chunk.width;
  }
}
```

### Speed Ramping
```js
const BASE_SPEED = 200;
const MAX_SPEED = 600;
const RAMP_TIME = 120; // seconds to reach max

gameSpeed = lerp(BASE_SPEED, MAX_SPEED, Math.min(1, time / RAMP_TIME));
```

## Common Architecture Patterns

### Scene Template
```js
game.scene('play', {
  init() {
    this.entities = [];
    this.score = 0;
    this.particles = new ParticleSystem();
    // Setup...
  },
  update(dt) {
    // Input → Physics → Collision → Cleanup
    this.entities.forEach(e => e.update(dt));
    this.checkCollisions();
    this.entities = this.entities.filter(e => e.active);
    this.particles.update(dt);
  },
  draw(ctx) {
    // Background → Entities → Particles → UI
    this.drawBackground(ctx);
    this.entities.forEach(e => e.draw(ctx));
    this.particles.draw(ctx);
    this.drawHUD(ctx);
  }
});
```

### Entity Factory
```js
function createEnemy(type, x, y) {
  const configs = {
    basic: { health: 1, speed: 60, color: '#ff4444', score: 100 },
    fast: { health: 1, speed: 120, color: '#ffff44', score: 150 },
    tank: { health: 5, speed: 40, color: '#8844ff', score: 300 },
  };
  const cfg = configs[type];
  return game.spawn({ pos: vec2(x, y), tag: 'enemy', ...cfg });
}
```
