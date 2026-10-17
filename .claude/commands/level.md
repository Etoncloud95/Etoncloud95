---
description: Design and implement game levels
---

# /level — Level Designer

## Step 1: Identify Game Type
Read the project to determine genre and level format.

## Step 2: Design Levels

### Platformer (Tile-Based)
Design as string/array data:
```js
const level = [
  '                    ',
  '       C   C        ',
  '      =====    E    ',
  '  P              ===',
  '===================='
];
```
- Level 1: Simple jumps, few enemies, many coins (tutorial)
- Level 2: Moving platforms, more enemies
- Level 3+: Complex layouts, hazards, secrets

### Roguelike (Procedural)
Configure generation: room count, size ranges, enemy density, item rates, special rooms.

### Shooter (Wave-Based)
Design wave progression:
```js
const waves = [
  { enemies: [{type: 'basic', count: 5}] },
  { enemies: [{type: 'basic', count: 8}] },
  { enemies: [{type: 'shooter', count: 3}, {type: 'basic', count: 5}] },
];
```

### Puzzle
Define board configurations and puzzle solutions.

## Step 3: Difficulty Curve
- First levels: Tutorial, teach mechanics
- Middle: Challenge ramp
- Late: Test mastery
- Include breather levels between hard sections

## Step 4: Implement
Write level data, loading code, and test each level.
