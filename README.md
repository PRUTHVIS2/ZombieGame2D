# Zombie Defense Game

A Java-based tower defense style game where players fight waves of zombies using various weapons.

## Features

### Core Gameplay
- **Wave-Based Combat**: Defeat progressively harder waves of zombies
- **Multiple Weapons**: Sword (melee) and Flamethrower (ranged) with ammo management
- **Health & Lives System**: 3 lives, 100 HP, invulnerability frames after damage
- **Zombie AI**: Intelligent pathfinding and detection system with 300-pixel detection range
- **Collision Detection**: Grid-based collision system with 32x32 tiles

### Player Mechanics
- **Movement**: WASD keys for 8-directional movement at 200 pixels/second
- **Combat**: Left-click to attack with current weapon
- **Weapon Switching**: Switch between melee and ranged weapons
- **Pause/Resume**: Press P to pause and resume gameplay
- **Invulnerability**: 1-second invulnerability after taking damage

### Visual Features
- Animated player and zombie sprites with colored outlines
- Health bars for player and all enemies
- HUD displaying health, lives, current weapon, ammo, and wave info
- Multiple UI screens: Loading, Menu, Gameplay, Pause, Game Over, Credits
- Grid-based game world with gradient background

### Game Progression
- 3 lives per game
- Zombies spawn every 2 seconds
- Each wave requires defeating more zombies
- Increasing difficulty: +5 HP, +10 speed, +2 damage per wave
- Respawn system for next wave

## Getting Started

### Prerequisites
- Java 8 or higher (tested with Java 25)
- No external dependencies (uses built-in Swing library)

### Building the Game

```bash
cd /home/pruthvi-s/Documents/new\ javaporject/Zombies
javac -d bin src/*.java
```

### Running the Game

```bash
java -cp bin App
```

The game window (800x600) will open automatically.

## Controls

| Key | Action |
|-----|--------|
| W | Move Up |
| A | Move Left |
| S | Move Down |
| D | Move Right |
| Left Click | Attack |
| P | Pause/Resume |
| R | Restart (Game Over screen) |
| Q | Quit (Game Over screen) |
| ESC | Quit Game |

## Game Structure

### Core Classes (17 Classes)

**Game Logic:**
- `App.java` - Main entry point
- `Game.java` - Main game controller with state management
- `Level.java` - Level management with spawning and progression
- `InputHandler.java` - Keyboard and mouse input handling

**Entities:**
- `Entity.java` - Base class for all game objects
- `Character.java` - Base class for living entities (Player & Zombie)
- `Player.java` - Player character with weapons and health
- `Zombie.java` - Enemy zombie with AI tracking

**Weapons:**
- `Weapon.java` - Base weapon class
- `MeleeWeapon.java` - Melee attacks (Sword, Mace)
- `RangedWeapon.java` - Ranged attacks (Flamethrower)

**Environment:**
- `Environment.java` - Game world with collision detection
- `Rectangle.java` - Collision bounds utility

**Graphics & UI:**
- `GamePanel.java` - Swing JPanel for rendering
- `GameWindow.java` - Swing JFrame main window
- `UI.java` - UI state and screen management
- `Pathfinder.java` - AI pathfinding system

## Game Architecture

### Game Loop
- **Update Phase**: Input handling, entity updates, collision detection
- **Render Phase**: Draw all entities and UI to screen
- **Frame Rate**: 60 FPS with delta time calculation

### Game States
1. **LOADING** - Initial loading screen
2. **MENU** - Main menu screen
3. **PLAYING** - Active gameplay
4. **PAUSED** - Game paused
5. **GAME_OVER** - Game over/death screen
6. **CREDITS** - Credits screen

### Collision System
- Grid-based 32x32 tile collision map
- AABB (Axis-Aligned Bounding Box) collision detection
- Border walls prevent moving off-screen

### AI System
- Zombie detection range: 300 pixels
- Movement toward player with velocity-based pathfinding
- Attack cooldown: 1 second between attacks
- States: idle, moving, attacking

## Game Settings

**Default Values:**
- Window Size: 800x600 pixels
- Tile Size: 32x32 pixels
- Player HP: 100
- Player Lives: 3
- Player Speed: 200 pixels/second
- Zombie Spawn Interval: 2 seconds
- Initial Zombies per Wave: 10
- Wave Difficulty Increase: +5 HP, +10 speed, +2 damage

**Weapons:**
1. **Sword** - Melee weapon
   - Damage: 15
   - Cooldown: 0.5 seconds
   - Range: 50 pixels

2. **Flamethrower** - Ranged weapon
   - Damage: 20
   - Cooldown: 0.8 seconds
   - Range: 300 pixels
   - Ammo Type: Fuel
   - Starting Ammo: 100

## Future Enhancements

- [ ] Additional weapons (dual weapons, grenades)
- [ ] Enemy variety (fast, tough, special zombies)
- [ ] Sound effects and music
- [ ] Power-ups and pickups
- [ ] Level variety and map design
- [ ] Score system and leaderboards
- [ ] Particle effects
- [ ] Different difficulty levels
- [ ] Mobile support
- [ ] Multiplayer support

## Technical Details

### Dependencies
- **Java AWT/Swing**: Built-in graphics and UI
- **Java Threads**: For game loop management

### Performance
- 60 FPS target framerate
- Delta time-based updates
- Double buffering for smooth rendering
- Efficient collision detection

### Code Organization
- Model-View-Controller pattern
- Inheritance hierarchy for entities
- Separation of concerns (Input, Logic, Rendering)
- Object pooling concepts for entity management

## Troubleshooting

### Game window doesn't open
- Ensure Java is properly installed
- Check that your system has display capabilities
- Try running with: `java -cp bin App`

### Low frame rate
- Close other applications to free up resources
- Check Java heap size: `java -Xmx512m -cp bin App`

### Controls not responding
- Ensure game window has focus (click on it)
- Check that CAPS LOCK is not on

## License

This project is provided as-is for educational purposes.

## Credits

Developed as a comprehensive Java game using core Java libraries.

For more information or to report issues, please check the source code.

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
