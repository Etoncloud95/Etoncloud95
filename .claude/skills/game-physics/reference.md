# Game Physics Reference

## Platformer Physics

### Gravity & Jumping
```js
const GRAVITY = 980;          // pixels/s²
const JUMP_FORCE = -350;      // negative = up
const MAX_FALL = 500;         // terminal velocity
const JUMP_CUT = 0.4;         // multiply vel when releasing jump

// In update:
vel.y += GRAVITY * dt;
vel.y = Math.min(vel.y, MAX_FALL);

// Variable jump height
if (input.released('Space') && vel.y < 0) {
  vel.y *= JUMP_CUT;
}
```

### Coyote Time (6 frames ≈ 0.1s)
Allows jumping briefly after walking off a ledge:
```js
let coyoteTimer = 0;
const COYOTE_TIME = 0.1;

// In update:
if (grounded) {
  coyoteTimer = COYOTE_TIME;
} else {
  coyoteTimer -= dt;
}

// Can jump if:
const canJump = grounded || coyoteTimer > 0;
if (input.pressed('Space') && canJump) {
  vel.y = JUMP_FORCE;
  coyoteTimer = 0;
}
```

### Jump Buffering (6 frames ≈ 0.1s)
Queues jump input if pressed slightly before landing:
```js
let jumpBuffer = 0;
const BUFFER_TIME = 0.1;

if (input.pressed('Space')) {
  jumpBuffer = BUFFER_TIME;
}
jumpBuffer -= dt;

// When landing:
if (grounded && jumpBuffer > 0) {
  vel.y = JUMP_FORCE;
  jumpBuffer = 0;
}
```

### Wall Sliding & Wall Jump
```js
const WALL_SLIDE_SPEED = 50;
const WALL_JUMP_FORCE = vec2(250, -300);

if (touchingWall && !grounded && vel.y > 0) {
  vel.y = Math.min(vel.y, WALL_SLIDE_SPEED);
  wallSliding = true;
}

if (input.pressed('Space') && wallSliding) {
  vel.x = wallDir * WALL_JUMP_FORCE.x; // away from wall
  vel.y = WALL_JUMP_FORCE.y;
}
```

### Moving Platforms
```js
// Platform moves, player rides on top
if (playerOnPlatform) {
  player.pos.x += platform.vel.x * dt;
  player.pos.y += platform.vel.y * dt;
}
```

### Horizontal Movement
```js
const ACCEL = 1500;     // acceleration
const FRICTION = 1200;  // deceleration
const MAX_SPEED = 200;

if (input.held('right')) {
  vel.x = approach(vel.x, MAX_SPEED, ACCEL * dt);
} else if (input.held('left')) {
  vel.x = approach(vel.x, -MAX_SPEED, ACCEL * dt);
} else {
  vel.x = approach(vel.x, 0, FRICTION * dt);
}
```

## Top-Down Movement

### 8-Directional with Diagonal Normalization
```js
let dx = 0, dy = 0;
if (input.held('left')) dx--;
if (input.held('right')) dx++;
if (input.held('up')) dy--;
if (input.held('down')) dy++;

// Normalize diagonal (prevent faster diagonal movement)
const dir = vec2(dx, dy);
if (dir.length() > 0) dir.normalize();

vel.x = approach(vel.x, dir.x * MAX_SPEED, ACCEL * dt);
vel.y = approach(vel.y, dir.y * MAX_SPEED, ACCEL * dt);
```

### Dash Mechanic
```js
const DASH_SPEED = 500;
const DASH_DURATION = 0.15;
const DASH_COOLDOWN = 0.5;

let dashTimer = 0, dashCooldown = 0;

if (input.pressed('Space') && dashCooldown <= 0) {
  dashTimer = DASH_DURATION;
  dashCooldown = DASH_COOLDOWN;
  dashDir = moveDir.clone().normalize();
}

if (dashTimer > 0) {
  vel.x = dashDir.x * DASH_SPEED;
  vel.y = dashDir.y * DASH_SPEED;
  dashTimer -= dt;
}
dashCooldown -= dt;
```

## Projectile Physics

### Linear (Bullets)
```js
const bullet = spawn({
  pos: vec2(x, y),
  vel: Vec2.fromAngle(angle, BULLET_SPEED),
  width: 4, height: 4
});
// Destroy when off-screen
if (bullet.pos.x < 0 || bullet.pos.x > width) bullet.destroy();
```

### Arc (Grenades)
```js
const grenade = spawn({
  pos: vec2(x, y),
  vel: vec2(throwForce * Math.cos(angle), throwForce * Math.sin(angle)),
  gravity: 500
});
// vel.y += gravity * dt each frame
```

### Homing (Missiles)
```js
function updateHoming(missile, target, dt) {
  const desired = target.pos.clone().sub(missile.pos).normalize().mul(MISSILE_SPEED);
  const steer = desired.sub(missile.vel);
  if (steer.length() > TURN_RATE) steer.normalize().mul(TURN_RATE);
  missile.vel.add(steer.mul(dt));
}
```

## Collision Detection

### AABB (Axis-Aligned Bounding Box)
Most common for rectangular entities:
```js
import { rectRect } from '../../lib/physics/collision.js';
const result = rectRect(
  { x: a.pos.x, y: a.pos.y, width: a.width, height: a.height },
  { x: b.pos.x, y: b.pos.y, width: b.width, height: b.height }
);
if (result.hit) {
  // result.overlap = penetration depth
  // result.normal = {x, y} separation direction
}
```

### Circle Collision
For round entities or distance-based:
```js
import { circleCircle } from '../../lib/physics/collision.js';
const result = circleCircle(
  { x: a.pos.x, y: a.pos.y, radius: a.radius },
  { x: b.pos.x, y: b.pos.y, radius: b.radius }
);
```

### Tilemap Collision
For platformer/grid-based:
```js
// Check tiles under entity
const tiles = tilemap.getCollisionTiles(entity.pos.x, entity.pos.y, entity.width, entity.height);
for (const tile of tiles) {
  if (tile.solid) {
    // Resolve: push entity out of tile
    resolveAABB(entity, tile);
  }
}
```

## Collision Response

### Bounce
```js
if (collision.hit) {
  vel.x *= -bounce * collision.normal.x || 1;
  vel.y *= -bounce * collision.normal.y || 1;
}
```

### Slide (Platformer)
```js
// Resolve horizontally, then vertically (separate axes)
pos.x += vel.x * dt;
resolveHorizontalCollisions(); // push out of walls

pos.y += vel.y * dt;
resolveVerticalCollisions(); // push out of floor/ceiling
if (hitFloor) { vel.y = 0; grounded = true; }
if (hitCeiling) { vel.y = 0; }
```

### Stop
```js
if (collision.hit) {
  pos.x -= collision.normal.x * collision.overlap;
  pos.y -= collision.normal.y * collision.overlap;
  vel.x = 0; vel.y = 0;
}
```

## Performance Tips
- Use SpatialHash for many entities (>50)
- Object pool bullets and particles
- Only check collisions between relevant groups (player vs enemies, bullets vs enemies)
- Skip collision for off-screen entities
