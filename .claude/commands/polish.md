---
description: Add juice and polish — particles, screenshake, SFX, and more
---

# /polish — Add Game Juice

Systematically add "juice" to make the game feel amazing.

## Step 1: Find the Game
Locate the current project in `projects/`.

## Step 2: Audit
Read the game code and identify what polish already exists.

## Step 3: Polish Checklist

For each unchecked item, implement it:

### Screen Effects
- [ ] **Screenshake** on impacts/explosions
  ```js
  import { ScreenShake } from '../../lib/effects/screenshake.js';
  ```
- [ ] **Screen flash** on damage/powerup
  ```js
  import { ScreenFlash } from '../../lib/effects/flash.js';
  ```
- [ ] **Hitstop** on big hits
  ```js
  import { SlowMotion } from '../../lib/effects/slowmo.js';
  ```

### Particles
- [ ] Explosion on enemy death (`ParticlePresets.explosion`)
- [ ] Dust on landing (`ParticlePresets.dust`)
- [ ] Trail on projectiles (`ParticlePresets.trail`)
- [ ] Spark on wall hits (`ParticlePresets.spark`)
- [ ] Confetti on level complete (`ParticlePresets.confetti`)

### Sound Effects (EVERY action needs sound)
- [ ] Player jump/move
- [ ] Player shoot/attack
- [ ] Player collect item
- [ ] Player take damage
- [ ] Player death
- [ ] Enemy death
- [ ] Menu navigation
- [ ] Game start / Game over

### Camera
- [ ] Smooth follow with lerp
- [ ] Camera bounds (don't show outside level)

### UI Screens
- [ ] Start screen with title + "Press Space"
- [ ] Game over screen with score + restart
- [ ] Pause menu (Escape key)
- [ ] Score/health HUD

### Animations
- [ ] Squash & stretch on actions
- [ ] Score popup animation
- [ ] Scene transitions (fade)

## Step 4: Verify
After adding polish, ask user to playtest. Game should feel noticeably better.
