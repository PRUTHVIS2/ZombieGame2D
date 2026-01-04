import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class TileMap {
    private int tileSize;
    private int rows;
    private int cols;
    private String[][] tiles;
    private Map<String, BufferedImage> tileImages;

    public TileMap(int tileSize) {
        this.tileSize = tileSize;
        this.tileImages = new HashMap<>();
    }

    public void loadMap(String mapPath) throws IOException {
        List<String> lines = Files.readAllLines(new File(mapPath).toPath());
        rows = lines.size();
        cols = 0;
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length > cols) cols = parts.length;
        }

        tiles = new String[rows][cols];

        for (int r = 0; r < rows; r++) {
            String line = lines.get(r).trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(",");
            for (int c = 0; c < parts.length; c++) {
                String name = parts[c].trim();
                if (name.isEmpty()) name = "grass";
                tiles[r][c] = name;
                loadTileImage(name);
            }
        }
    }

    private void loadTileImage(String name) {
        if (tileImages.containsKey(name)) return;

        String path = "assets/tiles/" + name + ".png";
        try {
            BufferedImage img = ImageIO.read(new File(path));
            tileImages.put(name, img);
        } catch (IOException e) {
            // If asset not found, create a placeholder image
            BufferedImage placeholder = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = placeholder.createGraphics();
            switch (name.toLowerCase()) {
                case "dirt":
                    g.setColor(new Color(139, 69, 19)); break;
                case "water":
                    g.setColor(new Color(30, 144, 255)); break;
                case "lava":
                    g.setColor(new Color(255, 100, 0)); break;
                default:
                    g.setColor(new Color(60, 180, 60)); break; // grass
            }
            g.fillRect(0, 0, tileSize, tileSize);
            g.dispose();
            tileImages.put(name, placeholder);
        }
    }

    public void render(Graphics2D g, int xOffset, int yOffset) {
        if (tiles == null) return;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                String name = tiles[r][c];
                if (name == null) continue;
                BufferedImage img = tileImages.get(name);
                if (img != null) {
                    g.drawImage(img, c * tileSize + xOffset, r * tileSize + yOffset, tileSize, tileSize, null);
                }
            }
        }
    }

    public int[][] getCollisionGrid() {
        if (tiles == null) return new int[0][0];
        int[][] grid = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                String name = tiles[r][c];
                if (name == null) { grid[r][c] = 0; continue; }
                String lower = name.toLowerCase();
                if (lower.equals("water") || lower.equals("lava")) grid[r][c] = 1;
                else grid[r][c] = 0;
            }
        }
        return grid;
    }

    public int getTileSize() { return tileSize; }
    public int getRows() { return rows; }
    public int getCols() { return cols; }
}
