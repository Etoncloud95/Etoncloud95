---
description: Design and implement enemy types and behaviors
---

# /enemy — Enemy Designer

## Step 1: Enemy Concept
Ask what type: Basic, Ranged, Tank, Fast, or Boss.

## Step 2: Design

### Visual (Procedural Sprite)
Create using Canvas 2D drawing. Keep it simple — shapes + color = recognizable.

### Stats
```js
{ health: 3, speed: 80, damage: 1, scoreValue: 100, size: 16 }
```

### Behavior (AI)
Use `lib/ai/state-machine.js` for complex enemies:
- **Idle** → detect player → **Chase** → in range → **Attack** → cooldown → **Chase**
- Low health → **Retreat**

Use `lib/ai/steering.js` for movement:
- Patrol: back and forth between points
- Chase: `SteeringBehavior.seek(enemy, player.pos, speed)`
- Flee: `SteeringBehavior.flee(enemy, player.pos, speed)`
- Wander: `SteeringBehavior.wander(enemy, ...params)`

## Step 3: Implement
1. Create enemy factory function
2. Add spawn system
3. Add collision with player/bullets
4. Add death effects (particles + SFX + score popup)
5. Add hit feedback (flash, screenshake)

## Step 4: Balance
- Telegraph attacks (visual warning before damage)
- Fair difficulty (player can always react)
- Test against player abilities
