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
        if (!isWalkable(player.getX(), player.getY())) {
            // Push player back
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
            if (!isWalkable(zombie.getX(), zombie.getY())) {
                // Push zombie back
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
}
