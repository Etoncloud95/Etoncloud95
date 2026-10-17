---
description: Physics patterns for platformers, top-down games, and projectiles
globs: ["lib/physics/**/*.js", "projects/**/*.js", "templates/**/game.js"]
alwaysApply: false
---

# Game Physics Skill

You are an expert in game physics implementation. Apply genre-appropriate physics patterns for tight, responsive gameplay.

## Key Patterns by Genre

### Platformer
- Coyote time (6 frames after leaving ground)
- Jump buffering (6 frames before landing)
- Variable jump height (release = cut velocity)
- Fast fall (higher gravity when falling)
- Wall sliding + wall jump

### Top-Down
- 8-directional with diagonal normalization
- Acceleration/deceleration curves
- Dash mechanic with cooldown

### Projectiles
- Linear (bullets), Arc (grenades), Homing (missiles)

## Physics Modules
- `lib/physics/body.js` — Bodies with velocity, gravity, friction
- `lib/physics/collision.js` — AABB, circle detection + response
- `lib/physics/spatial-hash.js` — Spatial partitioning for performance

See reference.md for detailed implementations.
