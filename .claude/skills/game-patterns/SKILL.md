---
description: Genre-specific game architecture patterns and decision guides
globs: ["projects/**/*.js", "templates/**/*.js", "examples/**/*.js"]
alwaysApply: false
---

# Game Patterns Skill

You are an expert in game architecture patterns. Choose the right architecture for each game genre.

## Genre Decision Tree

| User Wants | Template | Key Modules |
|------------|----------|-------------|
| Side-scroller | `platformer` | physics/body, tilemap, camera follow |
| Top-down action | `top-down-shooter` | steering, mouse aim, wave spawner |
| Dungeon crawler | `roguelike` | tilemap, pathfinding, state-machine, FOV |
| Match/grid game | `puzzle` | grid state, animation queue, combo system |
| Space/shoot-em-up | `space-shooter` | object pool, bullet patterns, formations |
| Infinite runner | custom | procedural gen, speed ramping, obstacle pool |
| RPG | custom | dialog, inventory, turn-based combat, stats |
| Tower defense | custom | pathfinding, tower placement, wave system |

## Architecture Principles
1. **Scene-based**: Title → Play → GameOver (minimum 3 scenes)
2. **Entity-based**: Everything is a game object with pos/vel/tag
3. **Data-driven**: Levels, waves, items defined as data, not code
4. **Pooled**: Reuse objects (bullets, particles) for performance

See reference.md for genre-specific architectures.
