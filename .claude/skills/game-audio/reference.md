# Game Audio Reference

## Web Audio API Basics

### AudioContext Lifecycle
Browsers require a user gesture (click/keypress) before audio can play.

```js
// The engine handles this — AudioContext is created lazily on first play
// Just call synth.playPreset() and it handles the rest
```

### Sound Architecture
```
AudioContext
  └── GainNode (master volume)
      ├── GainNode (SFX channel)
      │   ├── OscillatorNode → sound 1
      │   └── OscillatorNode → sound 2
      └── GainNode (Music channel)
          └── OscillatorNode → background music
```

## Procedural SFX Synthesis

### How Synth Works
Each sound is built from:
1. **Oscillator type**: sine, square, sawtooth, triangle, noise
2. **Frequency envelope**: start freq → end freq over duration
3. **Volume envelope**: attack → decay (linear or exponential)
4. **Optional**: vibrato, filter

### Oscillator Types & Their Character
| Type | Sound Character | Best For |
|------|----------------|----------|
| `sine` | Pure, clean | Coins, blips, gentle sounds |
| `square` | Harsh, retro, 8-bit | Jump, shoot, chip-tune |
| `sawtooth` | Buzzy, aggressive | Lasers, alarms |
| `triangle` | Soft, mellow | Background, subtle effects |
| `noise` | White noise, chaotic | Explosions, hits, static |

### Creating Custom Sounds

```js
const synth = new Synth(audioManager);

// Simple: frequency sweep
synth.create('myJump', {
  type: 'square',
  freq: [200, 600],        // sweep from 200Hz to 600Hz
  duration: 0.15,
  decay: 'exponential'
});

// Multi-point frequency
synth.create('myCoin', {
  type: 'sine',
  freq: [500, 800, 1000],  // three frequency points
  duration: 0.12,
  decay: 'linear'
});

// With vibrato
synth.create('alarm', {
  type: 'square',
  freq: [800, 800],        // steady pitch
  duration: 0.5,
  vibrato: { freq: 10, depth: 50 }  // wobble
});

// With filter
synth.create('muffled', {
  type: 'noise',
  freq: [200, 50],
  duration: 0.3,
  filter: { type: 'lowpass', freq: 1000, Q: 5 }
});
```

### Preset Breakdowns

**Jump** — Rising square wave (retro feel):
```js
{ type: 'square', freq: [200, 600], duration: 0.15, decay: 'exponential' }
```
Why: Square wave = retro. Rising pitch = upward motion. Short = snappy.

**Coin** — Ascending sine notes (pleasant):
```js
{ type: 'sine', freq: [500, 800, 1000], duration: 0.12, decay: 'linear' }
```
Why: Sine = clean. Ascending = reward feeling. Multi-point = musical.

**Explosion** — Noise burst (chaotic):
```js
{ type: 'noise', freq: [400, 50], duration: 0.4, decay: 'exponential' }
```
Why: Noise = chaos. Descending = impact dissipating. Long = dramatic.

**Shoot** — Descending square (attack):
```js
{ type: 'square', freq: [800, 200], duration: 0.1, decay: 'exponential' }
```
Why: Sharp attack. Descending = projectile launching. Very short = punchy.

**Hit** — Short noise (impact):
```js
{ type: 'noise', freq: [300, 100], duration: 0.15, decay: 'exponential' }
```

**Powerup** — Rising sine sweep (excitement):
```js
{ type: 'sine', freq: [300, 600, 900, 1200], duration: 0.3, decay: 'linear' }
```

**Laser** — Descending sawtooth (sci-fi):
```js
{ type: 'sawtooth', freq: [1000, 200], duration: 0.15, decay: 'exponential' }
```

**Death** — Long descending square (sad):
```js
{ type: 'square', freq: [400, 100, 50], duration: 0.5, decay: 'exponential' }
```

## Sound Design Guidelines

### Volume Mixing
```
SFX:    0.3 - 0.5  (prominent but not painful)
Music:  0.1 - 0.2  (background, never dominates)
UI:     0.2 - 0.3  (subtle but present)
```

### Frequency Ranges
```
Bass/Impact:     50 - 200 Hz
Mid/Body:        200 - 800 Hz
High/Detail:     800 - 2000 Hz
Sparkle/Air:     2000 - 5000 Hz
```

### Timing
```
Blip/Click:      0.03 - 0.05s
Shoot/Jump:      0.1 - 0.2s
Coin/Collect:    0.1 - 0.15s
Hit/Impact:      0.15 - 0.25s
Explosion:       0.3 - 0.5s
Death:           0.4 - 0.6s
Power-up:        0.3 - 0.4s
```

## Simple Music Generation

### Bass Line Loop
```js
function playBassLoop(synth) {
  const notes = [130, 130, 165, 130]; // C2, C2, E2, C2
  let i = 0;
  setInterval(() => {
    synth.create('bass_' + i, {
      type: 'triangle',
      freq: [notes[i % notes.length], notes[i % notes.length] * 0.8],
      duration: 0.3,
      decay: 'linear'
    });
    synth.play('bass_' + i, { volume: 0.15 });
    i++;
  }, 500); // 120 BPM
}
```

### Drum Pattern
```js
function playDrums(synth) {
  let beat = 0;
  setInterval(() => {
    if (beat % 4 === 0) synth.playPreset('hit'); // kick on 1
    if (beat % 4 === 2) { // snare on 3
      synth.create('snare', { type: 'noise', freq: [400, 100], duration: 0.1 });
      synth.play('snare', { volume: 0.2 });
    }
    beat++;
  }, 250); // 120 BPM, 16th notes
}
```

## Common Mistakes
1. **Too loud** — Start at 0.3 volume, adjust up
2. **Too long** — Game SFX should be < 0.5s (except special events)
3. **Too many simultaneous** — Limit concurrent sounds to ~8
4. **No variety** — Slightly randomize pitch (±5%) per play for realism
5. **Forgetting mobile** — Some mobile browsers need explicit resume()
