# Game AI Reference

## Steering Behaviors

### Seek (Move toward target)
```js
import { SteeringBehavior, applySteer } from '../../lib/ai/steering.js';

function updateEnemy(enemy, dt) {
  const force = SteeringBehavior.seek(enemy, player.pos, 100);
  applySteer(enemy, force, 200, 100); // maxForce, maxSpeed
}
```

### Flee (Run away)
```js
const force = SteeringBehavior.flee(enemy, player.pos, 100);
// Flee when health is low
if (enemy.health < 2) {
  applySteer(enemy, force, 200, 120);
}
```

### Arrive (Approach and slow down)
```js
const force = SteeringBehavior.arrive(enemy, target, 100, 80);
// slowRadius = 80px: starts slowing down when within 80px
```

### Pursue (Predict target position)
```js
const force = SteeringBehavior.pursue(enemy, player, 100);
// Smarter than seek — aims where player WILL be
```

### Wander (Random movement)
```js
const force = SteeringBehavior.wander(enemy, 50, 100, 20);
// wanderRadius=50, wanderDistance=100, wanderJitter=20
// Creates natural-looking random movement
```

### Combining Behaviors
```js
const seekForce = SteeringBehavior.seek(enemy, player.pos, 100);
const sepForce = SteeringBehavior.separate(enemy, otherEnemies, 30);

const combined = SteeringBehavior.combine([
  { force: seekForce, weight: 1.0 },
  { force: sepForce, weight: 1.5 } // higher weight = higher priority
]);

applySteer(enemy, combined, 200, 100);
```

## State Machines

### Basic Enemy FSM
```js
import { StateMachine } from '../../lib/ai/state-machine.js';

const fsm = new StateMachine(enemy);

fsm.add('idle', {
  enter(e) { e.vel.set(0, 0); },
  update(e, dt) {
    const dist = e.pos.distance(player.pos);
    if (dist < 200) fsm.switch('chase');
  }
});

fsm.add('chase', {
  enter(e) { /* play alert animation */ },
  update(e, dt) {
    const dist = e.pos.distance(player.pos);
    if (dist > 300) fsm.switch('idle');
    if (dist < 40) fsm.switch('attack');

    const force = SteeringBehavior.seek(e, player.pos, 120);
    applySteer(e, force, 200, 120);
  }
});

fsm.add('attack', {
  enter(e) {
    e.attackTimer = 0.5; // wind-up time
    e.attacking = false;
  },
  update(e, dt) {
    e.attackTimer -= dt;
    if (e.attackTimer <= 0 && !e.attacking) {
      e.attacking = true;
      dealDamage(player);
      fsm.switch('cooldown');
    }
  }
});

fsm.add('cooldown', {
  enter(e) { e.cooldown = 1.0; },
  update(e, dt) {
    e.cooldown -= dt;
    if (e.cooldown <= 0) fsm.switch('chase');
  }
});
```

### Boss AI (Multi-Phase)
```js
const bossFSM = new StateMachine(boss);

bossFSM.add('phase1', {
  enter(b) { b.pattern = 'aimed_shot'; },
  update(b, dt) {
    if (b.health < b.maxHealth * 0.6) {
      bossFSM.switch('transition');
      return;
    }
    // Shoot aimed bullets at player
    b.shootTimer -= dt;
    if (b.shootTimer <= 0) {
      shootAtPlayer(b);
      b.shootTimer = 1.5;
    }
  }
});

bossFSM.add('transition', {
  enter(b) {
    b.invulnerable = true;
    // Dramatic effect
    screenshake.shake(10, 1.0);
    flash.flash('#ffffff', 0.5, 0.5);
  },
  update(b, dt) {
    if (fsm.time > 2.0) {
      b.invulnerable = false;
      bossFSM.switch('phase2');
    }
  }
});

bossFSM.add('phase2', {
  enter(b) { b.pattern = 'spread'; b.speed *= 1.5; },
  update(b, dt) {
    if (b.health < b.maxHealth * 0.3) {
      bossFSM.switch('phase3');
      return;
    }
    // 5-way spread shot
    b.shootTimer -= dt;
    if (b.shootTimer <= 0) {
      for (let i = -2; i <= 2; i++) {
        shootAtAngle(b, angleToPlayer + i * 15);
      }
      b.shootTimer = 1.0;
    }
  }
});

bossFSM.add('phase3', {
  enter(b) { b.pattern = 'circle'; b.speed *= 1.5; },
  update(b, dt) {
    // Circle bullet pattern
    b.shootTimer -= dt;
    if (b.shootTimer <= 0) {
      for (let i = 0; i < 12; i++) {
        shootAtAngle(b, (360 / 12) * i);
      }
      b.shootTimer = 0.8;
    }
  }
});
```

## A* Pathfinding

### Grid-Based Pathfinding
```js
import { Pathfinder } from '../../lib/ai/pathfinding.js';

// Create grid (0=walkable, 1=wall)
const grid = [
  [0, 0, 0, 1, 0],
  [0, 1, 0, 1, 0],
  [0, 1, 0, 0, 0],
  [0, 0, 0, 1, 0],
];

const pf = new Pathfinder(grid, { diagonal: true });
const path = pf.findPath(0, 0, 4, 3); // start → end
// Returns: [{x:0,y:0}, {x:0,y:1}, {x:0,y:2}, {x:0,y:3}, {x:1,y:3}, ...]

// Follow path
if (path && path.length > 0) {
  const target = path[0];
  const worldTarget = tilemap.tileToWorld(target.x, target.y);
  const force = SteeringBehavior.seek(enemy, worldTarget, speed);
  applySteer(enemy, force, maxForce, speed);

  // Move to next waypoint when close
  if (enemy.pos.distance(worldTarget) < 4) path.shift();
}
```

### Dynamic Grid Updates
```js
// When terrain changes
pf.setWalkable(3, 2, false); // block tile
pf.setWalkable(3, 2, true);  // unblock tile

// Recalculate path periodically
enemy.pathTimer -= dt;
if (enemy.pathTimer <= 0) {
  const { tx: ex, ty: ey } = tilemap.worldToTile(enemy.pos.x, enemy.pos.y);
  const { tx: px, ty: py } = tilemap.worldToTile(player.pos.x, player.pos.y);
  enemy.path = pf.findPath(ex, ey, px, py);
  enemy.pathTimer = 0.5; // recalculate every 0.5s
}
```

## Enemy Patterns

### Patrol
```js
function patrol(enemy, pointA, pointB, speed) {
  const target = enemy.movingToB ? pointB : pointA;
  const dist = enemy.pos.distance(target);

  if (dist < 5) enemy.movingToB = !enemy.movingToB;

  const dir = target.clone().sub(enemy.pos).normalize();
  enemy.vel = dir.mul(speed);
}
```

### Circle Player
```js
function circle(enemy, target, radius, speed) {
  const angle = Math.atan2(enemy.pos.y - target.y, enemy.pos.x - target.x);
  const targetPos = vec2(
    target.x + Math.cos(angle + speed * 0.01) * radius,
    target.y + Math.sin(angle + speed * 0.01) * radius
  );
  const force = SteeringBehavior.seek(enemy, targetPos, speed);
  applySteer(enemy, force, 200, speed);
}
```

### Formation
```js
function formation(enemies, leader, offsets) {
  enemies.forEach((e, i) => {
    const targetPos = leader.pos.clone().add(offsets[i]);
    const force = SteeringBehavior.arrive(e, targetPos, 100, 30);
    applySteer(e, force, 200, 100);
  });
}
```

## Difficulty Scaling

### Parameters to Scale
| Parameter | Easy | Normal | Hard |
|-----------|------|--------|------|
| Enemy speed | 60 | 100 | 150 |
| Reaction time | 1.0s | 0.5s | 0.2s |
| Accuracy | 40% | 70% | 90% |
| Aggression | Low | Medium | High |
| Health | 1 | 2 | 4 |
| Spawn rate | 3s | 2s | 1s |

### Wave Spawning
```js
function getWaveConfig(waveNumber) {
  return {
    enemyCount: 3 + Math.floor(waveNumber * 1.5),
    enemySpeed: 60 + waveNumber * 5,
    enemyHealth: 1 + Math.floor(waveNumber / 3),
    spawnDelay: Math.max(0.5, 2.0 - waveNumber * 0.1),
    types: waveNumber >= 3 ? ['basic', 'shooter'] :
           waveNumber >= 6 ? ['basic', 'shooter', 'tank'] : ['basic']
  };
}
```

## Design Tips
1. **Telegraph attacks** — Flash, wind-up animation, sound before dealing damage
2. **Vulnerability windows** — Boss is only hittable after attacking
3. **Fair difficulty** — Player should always feel they could have dodged/avoided
4. **Readable patterns** — Player should be able to learn enemy behavior
5. **Variety** — Mix melee, ranged, and area enemies in encounters
