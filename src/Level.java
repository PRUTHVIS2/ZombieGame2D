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
        if (levelComplete)
            return;

        // Update environment
        environment.update(dt);

        // Set environment reference for player boundary checking
        player.setEnvironment(environment);

        // Update player (boundary checking happens inside player.update())
        player.update(dt);

        // Update zombies
        for (int i = 0; i < zombies.size(); i++) {
            Zombie zombie = zombies.get(i);

            // Update AI only if alive
            if (zombie.isAlive()) {
                zombie.updateAI(player, environment, dt);
            }

            // Always update to let animations progress (even if dead)
            zombie.update(dt);

            // Remove ONLY if truly dead and animation is done
            if (!zombie.isAlive() && zombie.isDeathAnimationFinished()) {
                zombies.remove(i);
                i--;
            }
        }

        // Check collisions
        environment.checkCollisions(player, zombies);

        // Update and check projectiles
        updateProjectiles(dt);
        checkProjectileCollisions();

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
        for (Projectile p : projectiles) {
            p.render(g);
        }
        player.render(g);
        for (Zombie zombie : zombies) {
            // Render all zombies, including dead ones, until their death animation is
            // finished and they are removed.
            zombie.render(g);
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

    public void startNextWave() {
        levelComplete = false;
        // wave is already incremented in completeLevel
        // parameters are already reset in completeLevel
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

    // Projectile Management
    private List<Projectile> projectiles = new ArrayList<>();

    public void addProjectile(Projectile p) {
        projectiles.add(p);
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    private void updateProjectiles(float dt) {
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = projectiles.get(i);
            p.update(dt);

            // Wall collision
            if (!environment.isWalkable(p.getX(), p.getY())) {
                p.setActive(false);
            }

            if (!p.isActive()) {
                projectiles.remove(i);
                i--;
            }
        }
    }

    private void checkProjectileCollisions() {
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = projectiles.get(i);
            if (!p.isActive())
                continue;

            // Check collision with zombies
            for (Zombie z : zombies) {
                if (z.isAlive() && z.getBounds() instanceof Rectangle && p.getBounds() instanceof Rectangle) {
                    Rectangle pRect = (Rectangle) p.getBounds();
                    Rectangle zRect = (Rectangle) z.getBounds();

                    if (pRect.intersects(zRect)) {
                        z.takeDamage(p.getDamage());
                        p.setActive(false);
                        break; // One bullet hits one zombie (for now)
                    }
                }
            }
        }
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
