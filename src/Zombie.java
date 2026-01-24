import java.util.List;
import java.util.ArrayList;
import java.awt.Point;

public class Zombie extends Character {
    private float speed;
    private int attackDamage;
    private List<Point> path; // Path for pathfinding (Points)
    private int pathIndex; // Current index in the path
    private String state; // idle, moving, attacking
    private float attackCooldown;
    private float attackTimer;
    private float detectionRange;
    private Player targetPlayer;
    private Pathfinder pathfinder;
    private float pathfindingTimer; // Timer for periodic pathfinding
    private static final float PATHFINDING_UPDATE_INTERVAL = 0.5f; // Update path every 0.5 seconds
    private static final int TILE_SIZE = 32; // Tile size in pixels

    private graphics.Animation idleAnim;
    private graphics.Animation moveAnim;
    private graphics.Animation attackAnim;

    public Zombie(float x, float y, int width, int height, int maxHp, float speed, int attackDamage) {
        super(x, y, width, height, maxHp);
        this.speed = speed;
        this.attackDamage = attackDamage;
        this.state = "idle";
        this.path = new ArrayList<>();
        this.pathIndex = 0;
        this.pathfinder = new Pathfinder();
        this.attackCooldown = 1.0f;
        this.attackTimer = 0;
        this.detectionRange = 500; // pixels
        this.pathfindingTimer = 0;

        loadAnimations();
    }

    private void loadAnimations() {
        // Use 0 as frameCount to auto-detect based on square frames (width/height)

        // Idle Animation (User: 768x128 => 6 frames)
        idleAnim = loadAnimationFromStrip("assets/skins/zombie/Idle.png", 0, 200);

        // Move Animation (Auto-detect frames)
        moveAnim = loadAnimationFromStrip("assets/skins/zombie/Walk.png", 0, 150);

        // Attack Animation (Auto-detect frames)
        attackAnim = loadAnimationFromStrip("assets/skins/zombie/Attack.png", 0, 150);

        // Initial animation
        if (idleAnim != null) {
            currentAnimation = idleAnim;
        } else if (moveAnim != null) {
            currentAnimation = moveAnim;
        }
    }

    public void updateAI(Player player, Environment environment, float dt) {
        if (!alive)
            return;

        this.targetPlayer = player;
        float distanceToPlayer = (float) Math.sqrt(
                Math.pow(player.getX() - x, 2) + Math.pow(player.getY() - y, 2));

        // Update pathfinding timer
        pathfindingTimer -= dt;

        // Check if player is in range
        if (distanceToPlayer < detectionRange) {
            // If path is empty or needs updating, compute a new path
            if (path.isEmpty() || pathfindingTimer <= 0) {
                computePath(player, environment);
                pathfindingTimer = PATHFINDING_UPDATE_INTERVAL;
                pathIndex = 0;
            }

            // Follow the path
            if (!path.isEmpty() && pathIndex < path.size()) {
                Point nextNode = path.get(pathIndex);
                float nextX = nextNode.x * TILE_SIZE + TILE_SIZE / 2;
                float nextY = nextNode.y * TILE_SIZE + TILE_SIZE / 2;

                float dirX = nextX - x;
                float dirY = nextY - y;
                float distance = (float) Math.sqrt(dirX * dirX + dirY * dirY);

                // If reached the node, move to the next one
                if (distance < 10) {
                    pathIndex++;
                    if (pathIndex >= path.size()) {
                        pathIndex = 0;
                        state = "idle";
                    }
                } else if (distance > 0) {
                    float vx = (dirX / distance) * speed;
                    float vy = (dirY / distance) * speed;

                    // Predict next position and check walkability
                    float nextPosX = x + vx * dt;
                    float nextPosY = y + vy * dt;
                    boolean canMoveDiag = environment.isWalkable(nextPosX, nextPosY);

                    if (canMoveDiag) {
                        velocityX = vx;
                        velocityY = vy;
                        state = "moving";
                        if (vx > 0)
                            facingRight = true;
                        if (vx < 0)
                            facingRight = false;
                    } else {
                        // Try moving only in X or only in Y as fallback
                        boolean canMoveX = environment.isWalkable(x + vx * dt, y);
                        boolean canMoveY = environment.isWalkable(x, y + vy * dt);
                        if (canMoveX) {
                            velocityX = vx;
                            velocityY = 0;
                            state = "moving";
                            if (vx > 0)
                                facingRight = true;
                            if (vx < 0)
                                facingRight = false;
                        } else if (canMoveY) {
                            velocityX = 0;
                            velocityY = vy;
                            state = "moving";
                        } else {
                            // Stuck, clear path and try again
                            path.clear();
                            pathIndex = 0;
                            velocityX = 0;
                            velocityY = 0;
                            state = "idle";
                        }
                    }
                }

                // Check if in attack range
                if (distanceToPlayer < 50) {
                    state = "attacking";
                    velocityX = 0;
                    velocityY = 0;
                    // Face player when attacking
                    if (player.getX() > x)
                        facingRight = true;
                    if (player.getX() < x)
                        facingRight = false;

                    if (attackTimer <= 0) {
                        attack(player);
                        attackTimer = attackCooldown;
                    }
                }
            } else {
                state = "idle";
                velocityX = 0;
                velocityY = 0;
            }
        } else {
            state = "idle";
            velocityX = 0;
            velocityY = 0;
            path.clear();
            pathIndex = 0;
        }
    }

    private void computePath(Player player, Environment environment) {
        int[][] collisionMap = environment.getCollisionTiles();
        if (collisionMap == null)
            return;

        Point start = new Point((int) (x / TILE_SIZE), (int) (y / TILE_SIZE));
        Point goal = new Point((int) (player.getX() / TILE_SIZE), (int) (player.getY() / TILE_SIZE));

        path = pathfinder.findPath(start, goal, collisionMap);
    }

    public String findPathTo(float targetX, float targetY) {
        // Helper method to compute path to arbitrary target
        Point start = new Point((int) (x / TILE_SIZE), (int) (y / TILE_SIZE));
        Point goal = new Point((int) (targetX / TILE_SIZE), (int) (targetY / TILE_SIZE));
        // Note: This would require access to environment's collision map
        // For now, just return empty string as the main pathfinding uses computePath
        return "";
    }

    public void attack(Player player) {
        if (player != null && !player.isDead()) {
            player.takeDamage(attackDamage);
        }
    }

    public void die() {
        alive = false;
        state = "dead";
        velocityX = 0;
        velocityY = 0;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public List<Integer> getPath() {
        // Return empty list for compatibility
        return new ArrayList<>();
    }

    public void setPath(List<Integer> path) {
        // For compatibility; actual path is stored as List<Point>
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public float getDetectionRange() {
        return detectionRange;
    }

    public void setDetectionRange(float range) {
        this.detectionRange = range;
    }

    @Override
    public void update(float dt) {
        if (!alive)
            return;

        // Update position based on velocity
        x += velocityX * dt;
        y += velocityY * dt;

        // Update attack timer
        attackTimer -= dt;

        // Update Animation State
        if (state.equals("attacking")) {
            if (attackAnim != null)
                currentAnimation = attackAnim;
        } else if (state.equals("moving") || velocityX != 0 || velocityY != 0) {
            if (moveAnim != null)
                currentAnimation = moveAnim;
        } else {
            if (idleAnim != null)
                currentAnimation = idleAnim;
        }

        super.update(dt);
    }

    @Override
    public void render(Object g) {
        // Render zombie sprite
    }

    @Override
    public Object getBounds() {
        bounds.x = x;
        bounds.y = y;
        return bounds;
    }
}
