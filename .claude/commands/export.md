---
description: Export your game as a single HTML file or deploy to the web
---

# /export — Export Game

## Step 1: Find the Game
Locate the current project in `projects/`.

## Step 2: Choose Export Format

### Option A: Single HTML File (Recommended)
Bundle everything into one self-contained .html file:
1. Read the game's `index.html` and `game.js`
2. Trace all `import` statements to find used lib/ modules
3. Concatenate all JS into a single `<script>` block (replace imports with concatenated code)
4. Write to `projects/<game-name>/build/<game-name>.html`

Or use: `node tools/export.js projects/<game-name>`

### Option B: GitHub Pages
1. Copy game files to a deployment directory
2. Ensure all paths are relative
3. Provide instructions for GitHub Pages setup

### Option C: itch.io Package
1. Create zip containing: index.html, game.js, lib/ (only used modules)
2. Provide itch.io upload instructions
3. Set viewport size to match game dimensions

## Step 3: Test
- Open exported file in browser
- Verify all features work
- Check no console errors
- Test on mobile if applicable

## Step 4: Share
Provide the user with file location and deployment instructions.
