import java.util.List;
import java.util.ArrayList;

public class Environment {
    private String background; // Background image path
    private int[][] collisionTiles; // Collision map (0 = walkable, 1 = obstacle)
    private List<Entity> objects;
    private int tileSize;
    private int width; // Map width in pixels
    private int height; // Map height in pixels

    public Environment(int width, int height, int tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        this.objects = new ArrayList<>();
        this.collisionTiles = new int[height / tileSize][width / tileSize];
    }

    public void update(float dt) {
        // Update environment and all objects
        for (Entity obj : objects) {
            if (obj instanceof Character) {
                Character character = (Character) obj;
                if (character.isAlive()) {
                    character.update(dt);
                }
            }
        }
    }

    public void render(Object g) {
        // Render background
        if (background != null) {
            // Render background image
        }
        // Render all objects
        for (Entity obj : objects) {
            if (obj instanceof Character) {
                Character character = (Character) obj;
                if (character.isAlive()) {
                    character.render(g);
                }
            }
        }
    }

    public boolean isWalkable(float x, float y) {
        int tileX = (int) (x / tileSize);
        int tileY = (int) (y / tileSize);

        if (tileX < 0 || tileX >= collisionTiles[0].length ||
            tileY < 0 || tileY >= collisionTiles.length) {
            return false;
        }

        return collisionTiles[tileY][tileX] == 0;
    }

    public void checkCollisions(Player player, List<Zombie> zombies) {
        // Check player collision with environment
        // Clamp player to the tile grid bounds (first/last tile columns/rows)
        if (collisionTiles != null && collisionTiles.length > 0) {
            int cols = collisionTiles[0].length;
            int rows = collisionTiles.length;
            float minX = 0f;
            float minY = 0f;
            float maxX = cols * tileSize - player.getWidth();
            float maxY = rows * tileSize - player.getHeight();

            if (player.getX() < minX) player.setX(minX);
            if (player.getY() < minY) player.setY(minY);
            if (player.getX() > maxX) player.setX(maxX);
            if (player.getY() > maxY) player.setY(maxY);
        } else {
            if (player.getX() < 0) player.setX(0);
            if (player.getY() < 0) player.setY(0);
            if (player.getX() + player.getWidth() > width) player.setX(width - player.getWidth());
            if (player.getY() + player.getHeight() > height) player.setY(height - player.getHeight());
        }

        // If player is inside a non-walkable tile, try to nudge them back inside walkable area
        if (!isWalkable(player.getX(), player.getY())) {
            int tileX = (int) (player.getX() / tileSize);
            int tileY = (int) (player.getY() / tileSize);
            // If player is outside the valid tile range, snap them to the nearest valid tile edge
            if (collisionTiles != null && collisionTiles.length > 0) {
                int cols = collisionTiles[0].length;
                int rows = collisionTiles.length;
                if (tileX < 0) player.setX(0);
                if (tileY < 0) player.setY(0);
                if (tileX >= cols) player.setX((cols - 1) * tileSize - player.getWidth());
                if (tileY >= rows) player.setY((rows - 1) * tileSize - player.getHeight());
            }
        }

        // Check zombie collisions with player
        for (Zombie zombie : zombies) {
            if (zombie.isAlive() && player.isAlive()) {
                if (player.checkCollision(zombie)) {
                    // Collision between player and zombie
                    // Zombie will attack through AI
                }
            }
        }

        // Check zombie collisions with environment
        for (Zombie zombie : zombies) {
            // Clamp zombies to the tile grid bounds as well
            if (collisionTiles != null && collisionTiles.length > 0) {
                int cols = collisionTiles[0].length;
                int rows = collisionTiles.length;
                float maxXz = cols * tileSize - zombie.getWidth();
                float maxYz = rows * tileSize - zombie.getHeight();
                if (zombie.getX() < 0) zombie.setX(0);
                if (zombie.getY() < 0) zombie.setY(0);
                if (zombie.getX() > maxXz) zombie.setX(maxXz);
                if (zombie.getY() > maxYz) zombie.setY(maxYz);
            } else {
                if (zombie.getX() < 0) zombie.setX(0);
                if (zombie.getY() < 0) zombie.setY(0);
                if (zombie.getX() + zombie.getWidth() > width) zombie.setX(width - zombie.getWidth());
                if (zombie.getY() + zombie.getHeight() > height) zombie.setY(height - zombie.getHeight());
            }

            if (!isWalkable(zombie.getX(), zombie.getY())) {
                // Simple fallback: move zombie one tile toward center
                float centerX = width / 2f;
                float centerY = height / 2f;
                if (zombie.getX() < centerX) zombie.setX(zombie.getX() - tileSize);
                else zombie.setX(zombie.getX() + tileSize);
                if (zombie.getY() < centerY) zombie.setY(zombie.getY() - tileSize);
                else zombie.setY(zombie.getY() + tileSize);
            }
        }
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int[][] getCollisionTiles() {
        return collisionTiles;
    }

    public void setCollisionTiles(int[][] tiles) {
        this.collisionTiles = tiles;
    }

    public void setCollisionTile(int x, int y, int value) {
        if (y >= 0 && y < collisionTiles.length &&
            x >= 0 && x < collisionTiles[0].length) {
            collisionTiles[y][x] = value;
        }
    }

    public List<Entity> getObjects() {
        return objects;
    }

    public void addObject(Entity obj) {
        objects.add(obj);
    }

    public void removeObject(Entity obj) {
        objects.remove(obj);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTileSize() {
        return tileSize;
    }

    // Ensure the outermost tiles are marked as non-walkable (border)
    public void ensureBorders() {
        if (collisionTiles == null) return;
        int rows = collisionTiles.length;
        if (rows == 0) return;
        int cols = collisionTiles[0].length;

        for (int x = 0; x < cols; x++) {
            collisionTiles[0][x] = 1;
            collisionTiles[rows - 1][x] = 1;
        }
        for (int y = 0; y < rows; y++) {
            collisionTiles[y][0] = 1;
            collisionTiles[y][cols - 1] = 1;
        }
    }
}
