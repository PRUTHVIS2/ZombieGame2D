public class UI {
    public enum UIState {
        LOADING, MAIN_MENU, IN_GAME, PAUSE_MENU, DEATH_SCREEN, CREDITS
    }

    private UIState currentState;
    private Player player;
    private Level level;

    public UI() {
        this.currentState = UIState.LOADING;
    }

    public void draw(Object g) {
        switch (currentState) {
            case LOADING:
                drawLoadingScreen(g);
                break;
            case MAIN_MENU:
                drawMainMenu(g);
                break;
            case IN_GAME:
                drawHUD(g);
                break;
            case PAUSE_MENU:
                drawPauseMenu(g);
                break;
            case DEATH_SCREEN:
                drawDeathScreen(g);
                break;
            case CREDITS:
                drawCredits(g);
                break;
        }
    }

    public void update(float dt) {
        // Update UI animations and transitions
    }

    private void drawLoadingScreen(Object g) {
        // Draw loading screen with progress bar
    }

    private void drawMainMenu(Object g) {
        // Draw main menu with buttons
        // - Start Game
        // - Settings
        // - Credits
        // - Exit
    }

    private void drawHUD(Object g) {
        // Draw in-game HUD
        if (player != null) {
            // Draw health bar
            // Draw current weapon and ammo
            // Draw lives remaining
            // Draw score/waves
            // Draw mini-map if applicable
        }
    }

    private void drawPauseMenu(Object g) {
        // Draw pause menu with options
        // - Resume
        // - Settings
        // - Main Menu
    }

    private void drawDeathScreen(Object g) {
        // Draw death/game over screen
        // - Game Over message
        // - Score/Statistics
        // - Restart button
        // - Main Menu button
    }

    private void drawCredits(Object g) {
        // Draw game credits
    }

    public UIState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(UIState state) {
        this.currentState = state;
    }

    public void showLoadingScreen() {
        currentState = UIState.LOADING;
    }

    public void showMainMenu() {
        currentState = UIState.MAIN_MENU;
    }

    public void showInGame() {
        currentState = UIState.IN_GAME;
    }

    public void showPauseMenu() {
        currentState = UIState.PAUSE_MENU;
    }

    public void showDeathScreen() {
        currentState = UIState.DEATH_SCREEN;
    }

    public void showCredits() {
        currentState = UIState.CREDITS;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
