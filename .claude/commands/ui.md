---
description: Design game UI — menus, HUD, dialogs
---

# /ui — UI Designer

## Step 1: Identify Needs
Ask what UI the game needs:
- Title/start screen
- Pause menu
- Game over screen
- HUD (health, score)
- Settings menu
- Dialog/tutorial boxes

## Step 2: Implement

### Title Screen
```js
import { Menu } from '../../lib/ui/menu.js';
const titleMenu = Menu.title('GAME NAME', [
  { label: 'Start Game', onSelect: () => startGame() },
  { label: 'How to Play', onSelect: () => showHelp() },
]);
```

### HUD
```js
import { HUD } from '../../lib/ui/hud.js';
HUD.healthBar(10, 10, hp, maxHp);
HUD.score(width - 10, 10, score, { align: 'right' });
```

### Pause Menu
```js
const pauseMenu = Menu.pause(onResume, onRestart, onQuit);
// Toggle: if (input.pressed('Escape')) paused = !paused;
```

### Game Over
```js
const gameOverMenu = Menu.gameOver(score, onRestart, onQuit);
```

### Dialog
```js
import { DialogBox } from '../../lib/ui/dialog.js';
const dialog = new DialogBox({ position: 'bottom' });
dialog.show('Welcome!', 'Elder');
```

## Step 3: Consistency
- Match colors to game palette
- Consistent font sizes
- Add menu navigation SFX
- Ensure readability over gameplay

## Step 4: Polish
- Transitions between screens (fade)
- Button hover/select effects
