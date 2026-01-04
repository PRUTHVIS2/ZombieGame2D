import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {
    private Game game;
    private BufferedImage buffer;
    private Graphics2D g2d;

    public GamePanel(Game game) {
        this.game = game;
        setFocusable(true);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Create buffer for double buffering
        if (buffer == null) {
            buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        }

        g2d = buffer.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Render game content
        renderGame(g2d);

        // Draw buffer to screen
        g.drawImage(buffer, 0, 0, null);
        g2d.dispose();
    }

    private void renderGame(Graphics2D g) {
        Game.GameState state = game.getGameState();

        switch (state) {
            case LOADING:
                drawLoadingScreen(g);
                break;
            case MENU:
                drawMainMenu(g);
                break;
            case PLAYING:
                drawGameplay(g);
                break;
            case PAUSED:
                drawPausedScreen(g);
                break;
            case GAME_OVER:
                drawGameOverScreen(g);
                break;
            case CREDITS:
                drawCreditsScreen(g);
                break;
        }
    }

    private void drawLoadingScreen(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        String text = "LOADING...";
        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = getHeight() / 2;
        g.drawString(text, x, y);

        // Draw progress bar
        g.setColor(Color.GREEN);
        int barWidth = 200;
        int barHeight = 30;
        int barX = (getWidth() - barWidth) / 2;
        int barY = y + 50;
        g.fillRect(barX, barY, barWidth, barHeight);
    }

    private void drawMainMenu(Graphics2D g) {
        // Draw title
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String title = "ZOMBIE DEFENSE";
        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(title)) / 2;
        int y = 80;
        g.drawString(title, x, y);

        // Draw menu buttons
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        String[] options = {"Start Game", "Settings", "Credits", "Exit"};
        int buttonY = 200;
        int buttonHeight = 50;
        int buttonSpacing = 70;

        for (String option : options) {
            g.drawRect(getWidth() / 2 - 100, buttonY, 200, buttonHeight);
            fm = g.getFontMetrics();
            int optionX = (getWidth() - fm.stringWidth(option)) / 2;
            g.drawString(option, optionX, buttonY + 35);
            buttonY += buttonSpacing;
        }
    }

    private void drawGameplay(Graphics2D g) {
        Level level = game.getCurrentLevel();
        if (level == null) return;

        // Draw environment background with gradient
        GradientPaint gradient = new GradientPaint(0, 0, new Color(30, 30, 30),
                                                    0, getHeight(), new Color(50, 50, 50));
        g.setPaint(gradient);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw grid pattern
        g.setColor(new Color(100, 100, 100));
        g.setStroke(new BasicStroke(1));
        for (int x = 0; x < getWidth(); x += 32) {
            g.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += 32) {
            g.drawLine(0, y, getWidth(), y);
        }

        // Draw tile map (if present)
        TileMap tileMap = null;
        if (level != null) tileMap = level.getTileMap();
        if (tileMap != null) {
            tileMap.render(g, 0, 0);
        }

        // Draw zombies first (background layer)
        for (Zombie zombie : level.getZombies()) {
            if (zombie.isAlive()) {
                drawZombie(g, zombie);
            }
        }

        // Draw player
        Player player = level.getPlayer();
        drawPlayer(g, player);

        // Draw HUD
        drawHUD(g, player, level);
    }

    private void drawPlayer(Graphics2D g, Player player) {
        int x = (int) player.getX();
        int y = (int) player.getY();
        int width = player.getWidth();
        int height = player.getHeight();

        // Draw player body
        g.setColor(new Color(50, 150, 255));
        g.fillRect(x, y, width, height);

        // Draw player head
        g.setColor(new Color(100, 200, 255));
        g.fillOval(x + 5, y - 10, 22, 20);

        // Draw outline
        g.setColor(Color.CYAN);
        g.setStroke(new BasicStroke(2));
        g.drawRect(x, y, width, height);

        // Draw weapon in hand
        if (player.getCurrentWeapon() != null) {
            g.setColor(new Color(200, 200, 200));
            g.drawLine(x + width, y + height / 2, x + width + 15, y + height / 2);
        }

        // Visual effect if invulnerable
        if (player.isInvulnerable()) {
            g.setColor(new Color(255, 255, 0, 100));
            g.setStroke(new BasicStroke(2));
            g.drawRect(x - 3, y - 3, width + 6, height + 6);
        }
    }

    private void drawZombie(Graphics2D g, Zombie zombie) {
        int x = (int) zombie.getX();
        int y = (int) zombie.getY();
        int width = zombie.getWidth();
        int height = zombie.getHeight();

        // Draw zombie body
        g.setColor(new Color(100, 200, 100));
        g.fillRect(x, y, width, height);

        // Draw zombie head
        g.setColor(new Color(150, 220, 150));
        g.fillOval(x + 5, y - 12, 22, 22);

        // Draw eyes
        g.setColor(Color.RED);
        g.fillOval(x + 8, y - 8, 4, 4);
        g.fillOval(x + 18, y - 8, 4, 4);

        // Draw outline
        g.setColor(Color.GREEN);
        g.setStroke(new BasicStroke(2));
        g.drawRect(x, y, width, height);

        // Draw health bar above zombie
        drawEntityHealthBar(g, zombie, x, y);
    }

    private void drawEntityHealthBar(Graphics2D g, Character character, int x, int y) {
        int barWidth = 30;
        int barHeight = 4;
        int barX = x + (character.getWidth() - barWidth) / 2;
        int barY = y - 15;

        // Draw background
        g.setColor(Color.BLACK);
        g.fillRect(barX, barY, barWidth, barHeight);

        // Draw health
        float healthPercent = (float) character.getHp() / character.getMaxHp();
        g.setColor(healthPercent > 0.5f ? Color.GREEN : (healthPercent > 0.25f ? Color.YELLOW : Color.RED));
        g.fillRect(barX, barY, (int) (barWidth * healthPercent), barHeight);

        // Draw border
        g.setColor(Color.WHITE);
        g.drawRect(barX, barY, barWidth, barHeight);
    }

    private void drawHUD(Graphics2D g, Player player, Level level) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));

        // Draw health
        String healthText = "Health: " + player.getHp() + "/" + player.getMaxHp();
        g.drawString(healthText, 10, 25);

        // Draw lives
        String livesText = "Lives: " + player.getLives();
        g.drawString(livesText, 10, 50);

        // Draw current weapon
        if (player.getCurrentWeapon() != null) {
            String weaponText = "Weapon: " + player.getCurrentWeapon().getName();
            g.drawString(weaponText, 10, 75);

            // Draw ammo
            if (player.getCurrentWeapon().getAmmoType() != null) {
                String ammoType = player.getCurrentWeapon().getAmmoType();
                int ammoCount = player.getAmmo().getOrDefault(ammoType, 0);
                String ammoText = "Ammo: " + ammoCount;
                g.drawString(ammoText, 10, 100);
            }
        }

        // Draw wave info
        String waveText = "Wave: " + level.getWave();
        g.drawString(waveText, getWidth() - 150, 25);

        // Draw zombie count
        String zombieText = "Zombies: " + level.getZombiesRemaining() + "/" + level.getZombiesRequired();
        g.drawString(zombieText, getWidth() - 150, 50);

        // Draw health bar
        drawHealthBar(g, player);
    }

    private void drawHealthBar(Graphics2D g, Player player) {
        int barWidth = 200;
        int barHeight = 20;
        int x = 10;
        int y = 120;

        // Draw background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, barWidth, barHeight);

        // Draw health
        float healthPercent = (float) player.getHp() / player.getMaxHp();
        g.setColor(healthPercent > 0.5f ? Color.GREEN : (healthPercent > 0.25f ? Color.YELLOW : Color.RED));
        g.fillRect(x, y, (int) (barWidth * healthPercent), barHeight);

        // Draw border
        g.setColor(Color.WHITE);
        g.drawRect(x, y, barWidth, barHeight);
    }

    private void drawPausedScreen(Graphics2D g) {
        // Draw semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw pause text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String text = "PAUSED";
        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = getHeight() / 2;
        g.drawString(text, x, y);

        // Draw instructions
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        String resume = "Press P to Resume";
        fm = g.getFontMetrics();
        x = (getWidth() - fm.stringWidth(resume)) / 2;
        g.drawString(resume, x, y + 50);
    }

    private void drawGameOverScreen(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String text = "GAME OVER";
        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = getHeight() / 2 - 50;
        g.drawString(text, x, y);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        String restart = "Press R to Restart or Q to Quit";
        fm = g.getFontMetrics();
        x = (getWidth() - fm.stringWidth(restart)) / 2;
        g.drawString(restart, x, y + 100);
    }

    private void drawCreditsScreen(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        String title = "CREDITS";
        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(title)) / 2;
        g.drawString(title, x, 60);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        String[] credits = {
            "Game Design & Programming",
            "Zombie Defense Team",
            "",
            "Music & Sound Effects",
            "Sound Designer",
            "",
            "Special Thanks",
            "All Players",
            "",
            "Press SPACE to return to menu"
        };

        int y = 150;
        for (String line : credits) {
            fm = g.getFontMetrics();
            x = (getWidth() - fm.stringWidth(line)) / 2;
            g.drawString(line, x, y);
            y += 35;
        }
    }

    public void render() {
        repaint();
    }
}
