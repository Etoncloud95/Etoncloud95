---
description: Generate a showcase page for your game
---

# /showcase — Generate Game Showcase

## Step 1: Gather Info
Read `project.json` and game code to understand: name, description, genre, features, controls.

## Step 2: Generate Showcase

### Showcase HTML Page
Create `projects/<game-name>/showcase.html` with:
- Hero section (game title + description)
- Embedded playable game (iframe)
- Controls section
- Features list
- Credits

### README Section
Generate markdown for including in a README:
```markdown
## <Game Name>
<Description>

**Controls**: [list controls]
**Features**: [list features]

[Play Now](link) | [Source Code](link)
```

## Step 3: Screenshot Guide
Tell the user:
1. Open game in browser
2. Play to an interesting moment
3. Screenshot via DevTools console:
```js
document.querySelector('canvas').toBlob(b => {
  const a = document.createElement('a');
  a.href = URL.createObjectURL(b);
  a.download = 'screenshot.png';
  a.click();
});
```

## Step 4: Social Media
Generate descriptions for:
- **Twitter/X**: Short + hashtags (#gamedev #indiedev)
- **Reddit**: Title + description for r/gamedev
- **Dev.to**: Blog post outline
