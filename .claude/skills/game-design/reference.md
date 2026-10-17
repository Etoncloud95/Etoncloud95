# Game Design Reference

## Juice — Making Games Feel Good

"Juice" = excessive positive feedback for every player action. The game should feel alive and responsive.

### Screenshake
Apply on: enemy kills, player damage, explosions, landing hard, boss attacks.

```js
import { ScreenShake } from '../../lib/effects/screenshake.js';
const shake = new ScreenShake();

// Scale intensity to event importance
shake.shake(2, 0.1);   // small hit
shake.shake(5, 0.2);   // enemy kill
shake.shake(10, 0.4);  // explosion
shake.shake(15, 0.6);  // boss death
```

**Rule**: If something impacts something else, shake the screen. Start subtle (intensity 2-3), increase for bigger events.

### Particles
Every event should spawn particles:

```js
import { ParticleSystem, ParticlePresets } from '../../lib/graphics/particles.js';

// Enemy death → explosion
particles.emit(enemy.x, enemy.y, ParticlePresets.explosion);

// Player landing → dust
particles.emit(player.x, player.y + player.height, ParticlePresets.dust);

// Bullet trail → trail particles every frame
particles.emit(bullet.x, bullet.y, ParticlePresets.trail);

// Collecting item → sparkle
particles.emit(item.x, item.y, { count: 10, speed: 100, life: 0.3, size: 3, color: '#ffff00', fadeOut: true });
```

### Hitstop (Freeze Frames)
Brief pause on significant impacts. Makes hits feel powerful.

```js
import { SlowMotion } from '../../lib/effects/slowmo.js';
const slowmo = new SlowMotion();

// Freeze for 2-3 frames (0.03-0.05 seconds)
slowmo.hitstop(0.04); // on hit
slowmo.hitstop(0.08); // on kill
slowmo.hitstop(0.15); // on boss hit
```

### Screen Flash
Brief color overlay for dramatic moments:

```js
import { ScreenFlash } from '../../lib/effects/flash.js';

flash.flash('#ff0000', 0.1, 0.3); // damage: red
flash.flash('#00ff00', 0.1, 0.2); // heal: green
flash.flash('#ffffff', 0.05, 0.5); // power: white
flash.flash('#ffff00', 0.1, 0.3);  // collect: yellow
```

### Sound Design
**Rule: No silent actions.** Every interaction needs audio feedback.

| Event | Sound Type | Synth Preset |
|-------|-----------|-------------|
| Jump | Rising pitch | `jump` |
| Land | Short thud | `hit` (low pitch) |
| Shoot | Sharp attack | `shoot` or `laser` |
| Collect | Ascending notes | `coin` |
| Damage | Noise burst | `hit` |
| Kill | Explosion | `explosion` |
| Power-up | Rising sweep | `powerup` |
| Menu select | Clean blip | `menuSelect` |
| Death | Descending | `death` |

```js
synth.playPreset('jump');
synth.playPreset('coin');
synth.playPreset('explosion');
```

## Player Feedback Loops

### The 30-Second Loop
Every game has a core loop that repeats every ~30 seconds:
1. **See** challenge → 2. **Plan** approach → 3. **Act** on plan → 4. **Get feedback** → repeat

Design this loop first, make it feel good, then add content.

### Feedback Hierarchy
1. **Immediate** (0-100ms): Visual flash, SFX, screenshake
2. **Short-term** (1-5s): Score popup, particle effects, combo counter
3. **Medium-term** (30s-2m): Level progress, power-ups, new enemies
4. **Long-term** (5m+): High scores, unlocks, new areas

## Difficulty Curves

### Progressive Difficulty
```
Easy → Medium → Hard → Breather → Harder → Boss
```

- Introduce one mechanic at a time
- Give player safe space to learn
- Ramp gradually, not suddenly
- Add breather moments after intense sections

### Difficulty Scaling Techniques
- Enemy count increases
- Enemy speed increases
- Enemy health increases
- New enemy types introduced
- Fewer power-ups
- Shorter time limits
- Combined hazards

## Controls Feel

### Response Time
- Input → visual response: < 1 frame (16ms)
- Never add input delay
- Process input before update logic

### Movement Feel
```js
// GOOD: Acceleration + deceleration (snappy)
if (input.held('right')) {
  vel.x = approach(vel.x, maxSpeed, accel * dt);
} else {
  vel.x = approach(vel.x, 0, friction * dt);
}

// BAD: Instant velocity (robotic)
if (input.held('right')) vel.x = maxSpeed;
else vel.x = 0;

// BAD: Too much acceleration (floaty)
if (input.held('right')) vel.x += 2; // slow to respond
```

### Platformer-Specific
- Coyote time: 6 frames grace period after leaving edge
- Jump buffering: Queue jump if pressed 6 frames before landing
- Variable jump: Higher jump when holding button longer
- Fast fall: Higher gravity when falling than rising

## Anti-Patterns

### Floaty Controls
**Problem**: Player feels disconnected from character.
**Fix**: Higher acceleration, add ground friction, fast response.

### Silent Actions
**Problem**: Game feels dead and unresponsive.
**Fix**: Add SFX to literally everything. Even menu cursor movement.

### No Visual Feedback
**Problem**: Player doesn't know what happened.
**Fix**: Flash sprites on hit, spawn particles, animate numbers.

### Camera Problems
**Problem**: Static or jerky camera.
**Fix**: Smooth lerp follow (0.1-0.2 factor), look-ahead in movement direction.

### Unclear Goals
**Problem**: Player doesn't know what to do.
**Fix**: Clear HUD, objective indicators, tutorial text, visual affordances.

## Reward Systems

### Positive Reinforcement
- Score popups with increasing combo text
- Satisfying collection effects (particles + sound + score)
- Screen clear rewards (big explosion, slow-mo, confetti)
- High score persistence (localStorage)

### Combo Systems
```js
let combo = 0;
let comboTimer = 0;

function onKill() {
  combo++;
  comboTimer = 2.0; // seconds to keep combo
  score += 100 * combo;

  if (combo >= 5) particles.emit(x, y, ParticlePresets.confetti);
  if (combo >= 10) flash.flash('#ffff00', 0.1, 0.3);
}

function update(dt) {
  comboTimer -= dt;
  if (comboTimer <= 0) combo = 0;
}
```
