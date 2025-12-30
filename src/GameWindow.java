import javax.swing.*;

public class GameWindow extends JFrame {
    private GamePanel gamePanel;
    private Game game;

    public GameWindow(Game game) {
        this.game = game;

        setTitle("Zombie Defense Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(game.getWindowWidth(), game.getWindowHeight());
        setLocationRelativeTo(null);

        // Create game panel
        gamePanel = new GamePanel(game);
        add(gamePanel);

        // Add input handler
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                game.getInputHandler().keyPressed(e.getKeyCode());
                handleSpecialKeys(e.getKeyCode());
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                game.getInputHandler().keyReleased(e.getKeyCode());
            }
        });

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                game.getInputHandler().mousePressed(e);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                game.getInputHandler().mouseReleased(e);
            }
        });

        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                game.getInputHandler().mouseMoved(e);
            }
        });

        setFocusable(true);
        setVisible(true);
    }

    private void handleSpecialKeys(int keyCode) {
        switch (keyCode) {
            case java.awt.event.KeyEvent.VK_P:
                // Pause/Resume
                if (game.getGameState() == Game.GameState.PLAYING) {
                    game.pause();
                } else if (game.getGameState() == Game.GameState.PAUSED) {
                    game.resume();
                }
                break;
            case java.awt.event.KeyEvent.VK_R:
                // Restart
                if (game.getGameState() == Game.GameState.GAME_OVER) {
                    game.startNewGame();
                }
                break;
            case java.awt.event.KeyEvent.VK_Q:
                // Quit
                if (game.getGameState() == Game.GameState.GAME_OVER) {
                    game.quit();
                }
                break;
            case java.awt.event.KeyEvent.VK_SPACE:
                // Menu transition
                if (game.getGameState() == Game.GameState.CREDITS ||
                    game.getGameState() == Game.GameState.MENU) {
                    game.setGameState(Game.GameState.MENU);
                }
                break;
            case java.awt.event.KeyEvent.VK_ESCAPE:
                game.quit();
                break;
        }
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void update() {
        gamePanel.render();
    }
}
