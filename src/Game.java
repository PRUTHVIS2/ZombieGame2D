public class Game {
    public enum GameState {
        LOADING, MENU, PLAYING, PAUSED, GAME_OVER, CREDITS
    }

    private int windowWidth;
    private int windowHeight;
    private Level currentLevel;
    private UI ui;
    private GameState gameState;
    private InputHandler inputHandler;
    private GameWindow gameWindow;
    private boolean running;
    private float deltaTime;
    private long lastFrameTime;

    public Game(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.gameState = GameState.LOADING;
        this.inputHandler = new InputHandler();
        this.running = false;
        this.deltaTime = 0.016f; // 60 FPS
        this.lastFrameTime = System.currentTimeMillis();
    }

    public void init() {
        // Initialize game window
        gameWindow = new GameWindow(this);
        running = true;
        ui = new UI();

        // Skip menu and start game immediately
        startNewGame();
    }

    public void startNewGame() {
        // Create environment
        Environment environment = new Environment(windowWidth, windowHeight, 32);
        // Try to load a tile map from assets/maps/map1.txt. Falls back to simple border
        // collisions.
        TileMap tileMap = new TileMap(environment.getTileSize());
        try {
            tileMap.loadMap("assets/maps/map1.txt");

            // Ensure collision grid matches environment dimensions (pad/crop as needed)
            int[][] envGrid = environment.getCollisionTiles();
            int envRows = envGrid.length;
            int envCols = envGrid[0].length;
            int[][] mapGrid = tileMap.getCollisionGrid();
            int[][] finalGrid = new int[envRows][envCols];
            for (int r = 0; r < envRows; r++) {
                for (int c = 0; c < envCols; c++) {
                    if (r < mapGrid.length && c < mapGrid[0].length)
                        finalGrid[r][c] = mapGrid[r][c];
                    else
                        finalGrid[r][c] = 0;
                }
            }
            environment.setCollisionTiles(finalGrid);
            // Always enforce map borders so player cannot leave the window
            environment.ensureBorders();
        } catch (Exception e) {
            // Fallback - initialize simple border collisions
            initializeEnvironmentCollisions(environment);
        }

        // Create player
        Player player = new Player(windowWidth / 2, windowHeight / 2, 32, 32, 100, 3);

        // Add weapons to player
        MeleeWeapon sword = new MeleeWeapon("Sword", 15, 0.5f, 50);
        RangedWeapon flamethrower = new RangedWeapon("Flamethrower", 20, 0.8f, "fuel", 1, 200);
        player.addWeapon(sword);
        player.addWeapon(flamethrower);
        player.setCurrentWeapon(sword);
        player.setAmmo("fuel", 100);

        // Create level
        currentLevel = new Level(1, environment, player);
        currentLevel.setTileMap(tileMap);

        // Setup UI
        ui.setPlayer(player);
        ui.setLevel(currentLevel);
        ui.showInGame();

        // Start playing
        gameState = GameState.PLAYING;
    }

    private void initializeEnvironmentCollisions(Environment environment) {
        // Initialize collision map - simple border walls
        int[][] collisionTiles = environment.getCollisionTiles();
        int mapWidth = environment.getWidth() / environment.getTileSize();
        int mapHeight = environment.getHeight() / environment.getTileSize();

        // Create border walls
        for (int x = 0; x < mapWidth; x++) {
            collisionTiles[0][x] = 1; // Top wall
            collisionTiles[mapHeight - 1][x] = 1; // Bottom wall
        }
        for (int y = 0; y < mapHeight; y++) {
            collisionTiles[y][0] = 1; // Left wall
            collisionTiles[y][mapWidth - 1] = 1; // Right wall
        }
        // Also ensure borders are set (defensive)
        environment.ensureBorders();
    }

    public void gameLoop() {
        while (running) {
            updateDeltaTime();
            update(deltaTime);
            gameWindow.update();

            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.exit(0);
    }

    private void updateDeltaTime() {
        long currentTime = System.currentTimeMillis();
        deltaTime = (currentTime - lastFrameTime) / 1000.0f;
        lastFrameTime = currentTime;

        // Cap deltaTime to prevent large jumps
        if (deltaTime > 0.033f) {
            deltaTime = 0.033f;
        }
    }

    public void update(float dt) {
        switch (gameState) {
            case LOADING:
                // Loading state
                break;
            case MENU:
                // Menu state - handle menu input
                break;
            case PLAYING:
                updatePlaying(dt);
                break;
            case PAUSED:
                // Paused state
                break;
            case GAME_OVER:
                // Game over state
                break;
            case CREDITS:
                // Credits state
                break;
        }

        if (ui != null) {
            ui.update(dt);
        }

        inputHandler.reset();
    }

    private void updatePlaying(float dt) {
        if (currentLevel != null) {
            Player player = currentLevel.getPlayer();

            // Handle input
            player.handleInput(inputHandler);

            // Update level
            currentLevel.update(dt);

            // Check game over
            if (player.getLives() <= 0) {
                gameState = GameState.GAME_OVER;
                ui.showDeathScreen();
            }

            // Check level complete
            if (currentLevel.isLevelComplete() && currentLevel.getZombies().isEmpty()) {
                if (player.getLives() > 0) {
                    // Next wave or level complete
                    // For now, restart current level
                    startNewGame();
                }
            }
        }
    }

    public void render() {
        Object g = null; // Graphics handled by GamePanel

        switch (gameState) {
            case PLAYING:
                if (currentLevel != null) {
                    currentLevel.render(g);
                }
                break;
            case MENU:
            case PAUSED:
            case GAME_OVER:
            case CREDITS:
                // Render UI for these states
                break;
        }

        if (ui != null) {
            ui.draw(g);
        }
    }

    public void pause() {
        if (gameState == GameState.PLAYING) {
            gameState = GameState.PAUSED;
            ui.showPauseMenu();
        }
    }

    public void resume() {
        if (gameState == GameState.PAUSED) {
            gameState = GameState.PLAYING;
            ui.showInGame();
        }
    }

    public void quit() {
        running = false;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Level level) {
        this.currentLevel = level;
    }

    public UI getUI() {
        return ui;
    }

    public void setUI(UI ui) {
        this.ui = ui;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState state) {
        this.gameState = state;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }
}
