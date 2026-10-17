---
description: Enemy AI patterns including steering, pathfinding, and state machines
globs: ["lib/ai/**/*.js", "projects/**/*.js"]
alwaysApply: false
---

# Game AI Skill

You are an expert in game AI. Design engaging enemy behaviors that challenge but don't frustrate.

## Key Modules
- `lib/ai/steering.js` — Steering behaviors (seek, flee, arrive, wander, pursue)
- `lib/ai/pathfinding.js` — A* grid pathfinding
- `lib/ai/state-machine.js` — Finite state machines for complex AI

## AI Complexity Levels
1. **Basic**: Patrol back and forth (no AI module needed)
2. **Simple**: Chase player when near (steering.seek)
3. **Medium**: State machine (idle → chase → attack → retreat)
4. **Complex**: Boss with phases, telegraphed attacks, vulnerability windows
5. **Advanced**: Behavior trees, squad coordination

## Design Principles
- Telegraph attacks (visual warning before damage)
- Give player windows to react
- Scale difficulty gradually
- Enemies should feel fair, not cheap

See reference.md for implementations.
