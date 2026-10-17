# Game Engine API Reference

## Core

### Engine (`lib/core/engine.js`)
```js
import { Engine } from '../../lib/core/engine.js';

const game = new Engine({
  width: 800,        // canvas width
  height: 600,       // canvas height
  canvas: element,   // optional existing canvas
  background: '#000', // clear color
  responsive: true   // scale to viewport
});
```

**Properties:** `width`, `height`, `canvas`, `ctx`, `fps`, `time`, `dt`, `input` (Keyboard), `mouse` (Mouse), `audio` (AudioManager), `timer` (TimerManager), `entities` (array)

**Methods:**
| Method | Description |
|--------|-------------|
| `scene(name, { init, update, draw, destroy })` | Register a scene |
| `start(sceneName)` | Start game loop with scene |
| `stop()` | Stop game loop |
| `pause()` / `resume()` | Pause/resume |
| `spawn(config)` | Create entity, returns Entity |
| `destroy(entity)` | Mark entity for removal |
| `on(event, fn)` | Listen for events |
| `emit(event, ...args)` | Emit event |

### SceneManager (`lib/core/scene.js`)
```js
// Usually accessed via game.scene() and game.start()
sceneManager.add('menu', { init, update, draw, destroy });
sceneManager.switch('play', 'fade'); // with transition
sceneManager.current; // current scene name
```

### Entity (`lib/core/entity.js`)
```js
const e = game.spawn({
  pos: vec2(100, 200),
  vel: vec2(0, 0),
  width: 16, height: 16,
  tag: 'enemy',
  health: 3,
  // any custom props get copied
});

e.pos        // Vec2
e.vel        // Vec2
e.active     // boolean
e.tag        // string
e.overlaps(other) // AABB check
e.destroy()  // mark for removal
```

### EventEmitter (`lib/core/event.js`)
```js
const cleanup = emitter.on('hit', (damage) => { ... });
emitter.once('ready', () => { ... });
emitter.emit('hit', 10);
emitter.off('hit', fn);
cleanup(); // also removes listener
```

## Graphics

### Renderer + Camera (`lib/graphics/renderer.js`)
```js
import { Renderer, Camera } from '../../lib/graphics/renderer.js';

const camera = new Camera();
camera.follow(player, 0.1); // smooth follow
camera.zoom = 2;
camera.shake(5, 0.3); // intensity, duration

// In draw:
renderer.clear('#000');
renderer.beginFrame(camera);
renderer.drawRect(x, y, w, h, '#ff0000');
renderer.drawCircle(x, y, r, '#00ff00');
renderer.drawLine(x1, y1, x2, y2, '#fff', 2);
renderer.endFrame();
```

### SpriteSheet + Animator (`lib/graphics/sprite.js`)
```js
import { SpriteSheet, Animator } from '../../lib/graphics/sprite.js';

// Create sprite sheet from code
const sheet = SpriteSheet.fromCanvas((ctx, i) => {
  // draw frame i
  ctx.fillStyle = '#ff0';
  ctx.fillRect(2, 2, 12, 12);
}, 16, 16, 4); // frameW, frameH, frameCount

const anim = new Animator(sheet, {
  idle: { frames: [0, 1], speed: 0.2 },
  run: { frames: [2, 3], speed: 0.1 }
});
anim.play('run');
anim.update(dt);
anim.draw(ctx, x, y, flipX);
```

### ParticleSystem (`lib/graphics/particles.js`)
```js
import { ParticleSystem, ParticlePresets } from '../../lib/graphics/particles.js';

const particles = new ParticleSystem();

// Use presets
particles.emit(x, y, ParticlePresets.explosion);
particles.emit(x, y, ParticlePresets.dust);
particles.emit(x, y, ParticlePresets.spark);
particles.emit(x, y, ParticlePresets.trail);
particles.emit(x, y, ParticlePresets.confetti);

// Custom config
particles.emit(x, y, {
  count: 20, speed: 200, speedVar: 50,
  angle: -90, angleVar: 30,
  life: 0.5, size: 4, sizeEnd: 0,
  color: '#ff6600', colorEnd: '#ff0000',
  gravity: 200, fadeOut: true
});

// In update/draw:
particles.update(dt);
particles.draw(ctx);
```

### TextRenderer (`lib/graphics/text.js`)
```js
import { TextRenderer, BitmapFont } from '../../lib/graphics/text.js';

const text = new TextRenderer(ctx);
text.draw('Score: 100', 10, 10, { size: 24, color: '#fff' });
text.drawCentered('GAME OVER', 400, 300, { size: 48, color: '#f00', outline: true });

const font = BitmapFont.default();
font.draw(ctx, 'PIXEL TEXT', 10, 10, 2); // scale 2x
```

### Tilemap (`lib/graphics/tilemap.js`)
```js
import { Tilemap } from '../../lib/graphics/tilemap.js';

const map = new Tilemap(20, 15, 32); // 20x15 tiles, 32px each
map.setFromArray([
  [1,1,1,1,1],
  [1,0,0,0,1],
  [1,0,0,0,1],
  [1,1,1,1,1]
]);

const tileset = {
  1: { color: '#666' }, // wall
  0: { color: '#222' }  // floor
};
map.draw(ctx, tileset, camera);

// Collision helpers
const { tx, ty } = map.worldToTile(worldX, worldY);
const tiles = map.getCollisionTiles(x, y, w, h);
```

## Physics

### Body (`lib/physics/body.js`)
```js
import { Body } from '../../lib/physics/body.js';

const body = new Body({
  x: 100, y: 100,
  width: 16, height: 16,
  gravity: true,     // affected by world gravity
  friction: 0.8,
  bounce: 0.5,
  mass: 1,
  maxSpeed: 300
});

body.applyForce(100, 0);
body.applyImpulse(0, -200); // jump
body.update(dt, 980); // dt + world gravity
body.grounded; // boolean
body.getBounds(); // { x, y, width, height }
```

### Collision (`lib/physics/collision.js`)
```js
import { rectRect, circleCircle, rectCircle, resolveCollision } from '../../lib/physics/collision.js';

const result = rectRect(a, b); // { hit, overlap, normal }
if (result.hit) {
  resolveCollision(bodyA, bodyB, result);
}
```

### SpatialHash (`lib/physics/spatial-hash.js`)
```js
import { SpatialHash } from '../../lib/physics/spatial-hash.js';

const grid = new SpatialHash(64); // 64px cells
entities.forEach(e => grid.insert(e));
const nearby = grid.query(x, y, w, h);
const pairs = grid.getPairs(); // collision pairs
```

## Input

### Keyboard (`lib/input/keyboard.js`)
```js
// Accessed via game.input
game.input.pressed('Space')   // just pressed
game.input.held('ArrowLeft')  // held down
game.input.released('KeyZ')   // just released
// Aliases: 'left','right','up','down','space','z','x','a','w','s','d'
```

### Mouse (`lib/input/mouse.js`)
```js
game.mouse.pos          // Vec2 (canvas coords)
game.mouse.pressed()    // left click this frame
game.mouse.held()       // left held
game.mouse.toWorld(cam) // world coordinates
```

### Touch (`lib/input/touch.js`)
```js
import { TouchInput } from '../../lib/input/touch.js';
const touch = new TouchInput(canvas);
touch.enableJoystick({ side: 'left' });
touch.joystick; // { active, x, y, angle, distance }
touch.tapped;   // boolean
touch.draw(ctx);
```

### Gamepad (`lib/input/gamepad.js`)
```js
import { GamepadInput } from '../../lib/input/gamepad.js';
const pad = new GamepadInput();
pad.pressed(0);    // A button
pad.leftStick;     // { x, y }
```

## Audio

### AudioManager (`lib/audio/audio.js`)
```js
game.audio.play('bgm', { loop: true, volume: 0.5 });
game.audio.stop('bgm');
game.audio.setVolume(0.8);
game.audio.mute() / game.audio.unmute();
```

### Synth (`lib/audio/synth.js`)
```js
import { Synth } from '../../lib/audio/synth.js';
const synth = new Synth(game.audio);

// Play presets instantly
synth.playPreset('jump');
synth.playPreset('coin');
synth.playPreset('explosion');
synth.playPreset('shoot');
synth.playPreset('hit');
synth.playPreset('powerup');
synth.playPreset('laser');
synth.playPreset('death');

// Create custom sound
synth.create('mySound', {
  type: 'square',
  freq: [800, 200],
  duration: 0.2,
  decay: 'exponential'
});
synth.play('mySound');
```

## Effects

### ScreenShake (`lib/effects/screenshake.js`)
```js
import { ScreenShake } from '../../lib/effects/screenshake.js';
const shake = new ScreenShake();

shake.shake(5, 0.2);            // intensity, duration
ScreenShake.light();             // preset
ScreenShake.medium();
ScreenShake.heavy();
ScreenShake.earthquake(2.0);

shake.update(dt);
// Apply: ctx.translate(shake.offsetX, shake.offsetY);
```

### ScreenFlash (`lib/effects/flash.js`)
```js
import { ScreenFlash } from '../../lib/effects/flash.js';
const flash = new ScreenFlash();

flash.flash('#ff0000', 0.15, 0.3); // color, duration, opacity
ScreenFlash.damage();
ScreenFlash.heal();
ScreenFlash.power();

flash.update(dt);
flash.draw(ctx, width, height);
```

### SlowMotion (`lib/effects/slowmo.js`)
```js
import { SlowMotion } from '../../lib/effects/slowmo.js';
const slowmo = new SlowMotion();

slowmo.hitstop(0.05);      // freeze 50ms
slowmo.slowmo(0.3, 1.0);   // 30% speed for 1 second

// In update: dt = slowmo.update(dt);
```

### Transitions (`lib/effects/transition.js`)
```js
import { TransitionManager } from '../../lib/effects/transition.js';
const trans = new TransitionManager();

trans.fade('#000', 0.5, () => { switchScene(); });
trans.wipe('left', 0.5, () => { switchScene(); });
trans.circle(x, y, 0.5, () => { switchScene(); });

trans.update(dt);
trans.draw(ctx, width, height);
```

## AI

### Steering (`lib/ai/steering.js`)
```js
import { SteeringBehavior, applySteer } from '../../lib/ai/steering.js';

const force = SteeringBehavior.seek(enemy, player.pos, 100);
const flee = SteeringBehavior.flee(enemy, player.pos, 100);
const arrive = SteeringBehavior.arrive(enemy, player.pos, 100, 50);
const wander = SteeringBehavior.wander(enemy, 50, 100, 20);

applySteer(enemy, force, 200, 100); // maxForce, maxSpeed
```

### Pathfinder (`lib/ai/pathfinding.js`)
```js
import { Pathfinder } from '../../lib/ai/pathfinding.js';

const grid = [[0,0,1],[0,0,1],[0,0,0]]; // 0=walkable, 1=wall
const pf = new Pathfinder(grid, { diagonal: true });
const path = pf.findPath(0, 0, 2, 2); // [{x,y}...]
```

### StateMachine (`lib/ai/state-machine.js`)
```js
import { StateMachine } from '../../lib/ai/state-machine.js';

const fsm = new StateMachine(enemy);
fsm.add('idle', {
  update(owner, dt) { if (seePlayer) fsm.switch('chase'); }
});
fsm.add('chase', {
  enter(owner) { owner.speed = 120; },
  update(owner, dt) { /* move toward player */ },
  exit(owner) { owner.speed = 60; }
});
fsm.update(dt);
```

## UI

### Menu (`lib/ui/menu.js`)
```js
import { Menu } from '../../lib/ui/menu.js';

const menu = Menu.title('MY GAME', [
  { label: 'Start', onSelect: () => startGame() },
  { label: 'Quit', onSelect: () => {} }
]);
Menu.pause(onResume, onRestart, onQuit);
Menu.gameOver(score, onRestart, onQuit);

menu.update(input);
menu.draw(ctx, x, y, w, h);
```

### HUD (`lib/ui/hud.js`)
```js
import { HUD } from '../../lib/ui/hud.js';

HUD.healthBar(10, 10, hp, maxHp, { width: 100, height: 12 });
HUD.score(780, 10, score, { align: 'right', color: '#fff' });
HUD.timer(400, 10, seconds);
HUD.fpsCounter(10, 580);
```

### DialogBox (`lib/ui/dialog.js`)
```js
import { DialogBox } from '../../lib/ui/dialog.js';

const dialog = new DialogBox({ position: 'bottom', typeSpeed: 30 });
dialog.show('Hello adventurer!', 'Elder');
dialog.showChoice('Which path?', ['Left', 'Right'], (i) => { ... });
dialog.update(dt, input);
dialog.draw(ctx, width, height);
```

## Utils

### Vec2 + Math (`lib/utils/math.js`)
```js
import { Vec2, vec2, lerp, clamp, map, approach, ease, randomRange, randomInt } from '../../lib/utils/math.js';

const v = vec2(10, 20);
v.add(other).normalize().mul(speed);
v.length(), v.distance(other), v.angle();

lerp(0, 100, 0.5);      // 50
clamp(val, 0, 1);
ease.outBounce(t);
```

### SeededRandom (`lib/utils/random.js`)
```js
import { SeededRandom } from '../../lib/utils/random.js';
const rng = new SeededRandom(42);
rng.next(); rng.range(0, 10); rng.pick(array); rng.shuffle(arr); rng.chance(0.5);
```

### Pool (`lib/utils/pool.js`)
```js
import { Pool } from '../../lib/utils/pool.js';
const bullets = new Pool(() => ({ x:0, y:0, active:false }), (b) => { b.active=false; });
const b = bullets.get(); bullets.release(b);
```

### TimerManager (`lib/utils/timer.js`)
```js
game.timer.after(1.0, () => { /* after 1s */ });
game.timer.every(0.5, () => { /* every 0.5s */ }, 10);
game.timer.tween(obj, { x: 100, alpha: 0 }, 0.5, ease.outQuad).then(() => { ... });
```

### Storage (`lib/utils/storage.js`)
```js
import { storage } from '../../lib/utils/storage.js';
storage.save('highscore', 9999);
const hs = storage.load('highscore', 0);
```
