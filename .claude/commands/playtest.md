---
description: Playtest your game and get improvement suggestions
---

# /playtest — Game Quality Checklist

Help the user systematically test their game.

## Step 1: Find the Game
Look for the current project in `projects/`. If multiple, ask which one.

## Step 2: Launch
Tell the user: **Open `projects/<game-name>/index.html` in your browser**

## Step 3: Quality Checklist

Walk through each category. Ask the user to test and report:

### Controls
- [ ] Movement feels responsive (no input lag)
- [ ] Jump/action feels satisfying
- [ ] Controls explained on start screen
- [ ] Keyboard works correctly

### Visual Feedback
- [ ] Visual feedback on player actions (particles, flash)
- [ ] Screen shake on impacts
- [ ] Animations are smooth
- [ ] Clear visual hierarchy (player vs enemies vs background)

### Audio
- [ ] SFX on player actions (jump, shoot, collect)
- [ ] SFX on game events (hit, die, level up)
- [ ] Volume appropriate, no clipping

### Gameplay
- [ ] Game immediately understandable
- [ ] Difficulty appropriate
- [ ] Clear goal/objective visible
- [ ] Score/progress feedback shown
- [ ] Game over condition clear

### Technical
- [ ] 60fps performance (no lag)
- [ ] No console errors
- [ ] Canvas scales on resize
- [ ] No visual glitches

## Step 4: Improvement Plan
Based on feedback, create prioritized list:
- **Critical**: Bugs, crashes, broken controls
- **Important**: Missing feedback, unclear gameplay
- **Nice to have**: Extra polish, content

Ask which to implement first, then do it.
