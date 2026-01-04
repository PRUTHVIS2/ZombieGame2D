import java.util.ArrayList;
import java.util.List;

public class Level {
    private int id;
    private Environment environment;
    private TileMap tileMap;
    private Player player;
    private List<Zombie> zombies;
    private Pathfinder pathfinder;
    private float zombieSpawnTimer;
    private float zombieSpawnInterval;
    private int zombiesSpawned;
    private int zombiesRequired;
    private boolean levelComplete;
    private int wave;

    public Level(int id, Environment environment, Player player) {
        this.id = id;
        this.environment = environment;
        this.player = player;
        this.zombies = new ArrayList<>();
        this.pathfinder = new Pathfinder();
        this.zombieSpawnTimer = 0;
        this.zombieSpawnInterval = 2.0f; // Spawn zombie every 2 seconds
        this.zombiesSpawned = 0;
        this.zombiesRequired = 10; // Zombies to defeat per wave
        this.levelComplete = false;
        this.wave = 1;
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public void setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
    }

    public void update(float dt) {
        if (levelComplete) return;

        // Update environment
        environment.update(dt);

        // Update player
        player.update(dt);

        // Update zombies
        for (int i = 0; i < zombies.size(); i++) {
            Zombie zombie = zombies.get(i);
            if (zombie.isAlive()) {
                zombie.updateAI(player, environment);
                zombie.update(dt);
            } else {
                zombies.remove(i);
                i--;
            }
        }

        // Check collisions
        environment.checkCollisions(player, zombies);

        // Spawn zombies
        zombieSpawnTimer -= dt;
        if (zombieSpawnTimer <= 0 && zombiesSpawned < zombiesRequired) {
            spawnZombie();
            zombieSpawnTimer = zombieSpawnInterval;
        }

        // Check level completion
        if (zombiesSpawned >= zombiesRequired && zombies.isEmpty()) {
            completeLevel();
        }

        // Check if player is dead
        if (player.isDead()) {
            if (player.getLives() <= 0) {
                gameOver();
            }
        }
    }

    public void render(Object g) {
        environment.render(g);
        player.render(g);
        for (Zombie zombie : zombies) {
            if (zombie.isAlive()) {
                zombie.render(g);
            }
        }
    }

    public void spawnZombie() {
        // Spawn zombie at random edge location
        float spawnX = (float) (Math.random() * environment.getWidth());
        float spawnY = (float) (Math.random() * environment.getHeight());

        // Ensure spawn is not inside walls
        while (!environment.isWalkable(spawnX, spawnY)) {
            spawnX = (float) (Math.random() * environment.getWidth());
            spawnY = (float) (Math.random() * environment.getHeight());
        }

        // Create zombie with increasing difficulty per wave
        int hp = 20 + (wave - 1) * 5;
        float speed = 50 + (wave - 1) * 10;
        int damage = 10 + (wave - 1) * 2;

        Zombie zombie = new Zombie(spawnX, spawnY, 32, 32, hp, speed, damage);
        zombies.add(zombie);
        zombiesSpawned++;
    }

    public void completeLevel() {
        levelComplete = true;
        wave++;
        // Reset for next wave
        zombiesSpawned = 0;
        zombieSpawnTimer = 0;
        zombiesRequired = 10 + (wave - 1) * 5; // More zombies each wave
    }

    public void gameOver() {
        // Game over - player lost all lives
        levelComplete = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Zombie> getZombies() {
        return zombies;
    }

    public void addZombie(Zombie zombie) {
        zombies.add(zombie);
    }

    public void removeZombie(Zombie zombie) {
        zombies.remove(zombie);
    }

    public boolean isLevelComplete() {
        return levelComplete;
    }

    public int getWave() {
        return wave;
    }

    public int getZombiesSpawned() {
        return zombiesSpawned;
    }

    public int getZombiesRequired() {
        return zombiesRequired;
    }

    public int getZombiesRemaining() {
        return zombies.size();
    }
}
