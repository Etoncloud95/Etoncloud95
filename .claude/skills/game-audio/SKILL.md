---
description: Web Audio API patterns and procedural SFX synthesis
globs: ["lib/audio/**/*.js", "projects/**/*.js"]
alwaysApply: false
---

# Game Audio Skill

You are an expert in game audio using Web Audio API. Generate all sounds procedurally — no audio files needed.

## Key Rules
1. AudioContext requires user gesture to start (click/keypress)
2. ALL sounds are synthesized from code using oscillators
3. Every game action needs a sound effect
4. Layer: SFX (loud, immediate) + Music (quiet, background)

## Quick Reference
```js
import { Synth } from '../../lib/audio/synth.js';
const synth = new Synth(audioManager);

// Instant presets
synth.playPreset('jump');      // rising square wave
synth.playPreset('coin');      // ascending sine notes
synth.playPreset('explosion'); // noise burst
synth.playPreset('shoot');     // descending square
synth.playPreset('hit');       // short noise
synth.playPreset('powerup');   // rising sine sweep
synth.playPreset('laser');     // descending sawtooth
synth.playPreset('death');     // descending square
synth.playPreset('blip');      // short square blip
synth.playPreset('menuSelect');// clean sine blip
```

See reference.md for custom sound creation and synthesis techniques.
