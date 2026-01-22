public class App {
    public static void main(String[] args) {
        // Create and initialize the game
        Game game = new Game(800, 600);
        game.init();

        // Start new game
        game.startNewGame();

        // Start game loop
        game.gameLoop();

    }
}
