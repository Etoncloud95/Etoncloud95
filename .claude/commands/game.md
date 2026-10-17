---
description: Create a new game or resume an existing project
---

# /game — Game Project Manager

You are the game project orchestrator for Claude Code Game Toolkit.

## Step 1: Check Existing Projects

Look in the `projects/` directory for existing game projects (each has a `project.json`).

If projects exist, list them with their status and ask the user which to resume, or if they want to create a new one.

## Step 2: New Game Creation

### 2a. Get Game Concept
Ask the user to either:
- **Describe their game idea** in natural language
- **Pick a template**: platformer, top-down-shooter, roguelike, puzzle, space-shooter

### 2b. Create Game Design Doc
Based on the concept, briefly outline:
- **Title**: Game name
- **Genre**: Game type
- **Core Mechanic**: The main gameplay loop
- **Features**: Prioritized feature list (mark as planned)
- **Controls**: Input scheme

### 2c. Scaffold Project
1. Create `projects/<game-name>/` directory
2. If template chosen: copy from `templates/<template>/`
3. If custom concept: create game.js and index.html using `lib/` modules
4. Create `project.json`:

```json
{
  "name": "<game-name>",
  "template": "<template-name or custom>",
  "phase": "build",
  "description": "<short description>",
  "features": {},
  "created": "<date>",
  "sessions": []
}
```

### 2d. Make It Playable
Ensure the game runs immediately:
- `index.html` loads `game.js` as ES module (`<script type="module">`)
- `game.js` imports from `../../lib/`
- Basic game loop running
- Tell user: **Open `projects/<game-name>/index.html` in your browser**

## Step 3: Resume Existing Project

1. Read `project.json` for current status
2. Show feature progress
3. Suggest next steps based on phase:
   - **build**: Implement next planned feature
   - **polish**: Suggest running `/polish`
   - **export**: Suggest running `/export`
4. Ask what the user wants to work on

## Important Rules
- Always use modules from `lib/` — never recreate engine functionality
- Every change should be immediately testable in browser
- Update `project.json` features after implementing each feature
- Keep game code clean and well-organized in a single `game.js` (or split if complex)
