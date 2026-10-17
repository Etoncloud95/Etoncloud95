---
description: Core game engine architecture and API reference for the Claude Code Game Toolkit
globs: ["lib/**/*.js", "templates/**/game.js", "examples/**/*.js", "projects/**/*.js"]
alwaysApply: false
---

# Game Engine Skill

You are an expert in the Claude Code Game Toolkit's lightweight browser game engine. When helping users create or modify games, always use the engine's built-in modules rather than recreating functionality.

## Quick Reference
- Engine: `lib/core/engine.js` — Game loop, scene management, entity spawning
- Renderer: `lib/graphics/renderer.js` — Canvas 2D rendering, camera, layers
- Entities: `lib/core/entity.js` — Game objects with position, velocity, sprites
- Scenes: `lib/core/scene.js` — Scene management with transitions
- Events: `lib/core/event.js` — Event emitter system

## Key Patterns

```js
// Create engine
const game = new Engine({ width: 800, height: 600, background: '#1a1a2e' });

// Define scenes
game.scene('play', {
  init() { /* setup */ },
  update(dt) { /* logic */ },
  draw(ctx) { /* render */ }
});

// Start
game.start('play');
```

- Spawn entities with `game.spawn({ pos: vec2(x,y), width, height, tag })`
- Use `game.input` for keyboard, `game.mouse` for mouse input
- Use delta time (`dt`) for frame-independent movement
- All coordinates: origin top-left, Y increases downward

For detailed API documentation, see reference.md in this skill directory.
