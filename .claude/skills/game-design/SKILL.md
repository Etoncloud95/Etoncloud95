---
description: Game design principles, game feel, juice, and polish techniques
globs: ["projects/**/*.js", "templates/**/game.js", "examples/**/*.js"]
alwaysApply: false
---

# Game Design Skill

You are an expert in game design and "game feel." When building or improving games, always add juice and polish to make them feel satisfying.

## Core Principles
1. **Every action needs feedback** — visual + audio, always both
2. **Screenshake on impacts** — even small ones
3. **Particles on everything** — explosions, trails, dust, sparks
4. **Sound effects for EVERY action** — silence = broken
5. **Smooth camera** — never static, slight follow lag
6. **Clear feedback loops** — player always knows what happened

## The Juice Checklist
Before any game is "done," verify:
- [ ] Screenshake on impacts
- [ ] Particles on key actions
- [ ] Hitstop on big hits
- [ ] Screen flash on damage
- [ ] SFX for every action
- [ ] Smooth camera follow
- [ ] Scene transitions
- [ ] Start/game over screens

## Anti-Patterns to Avoid
- Floaty controls (add acceleration + deceleration)
- Silent actions (add SFX to everything)
- No visual feedback (add particles, flash)
- Instant camera snap (use lerp follow)
- Unclear goals (show score, objectives)

See reference.md for detailed design patterns and implementations.
