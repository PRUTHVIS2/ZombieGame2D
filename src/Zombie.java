import java.util.List;
import java.util.ArrayList;

public class Zombie extends Character {
    private float speed;
    private int attackDamage;
    private List<Integer> path; // Path for pathfinding (node indices)
    private String state; // idle, moving, attacking
    private float attackCooldown;
    private float attackTimer;
    private float detectionRange;
    private Player targetPlayer;

    public Zombie(float x, float y, int width, int height, int maxHp, float speed, int attackDamage) {
        super(x, y, width, height, maxHp);
        this.speed = speed;
        this.attackDamage = attackDamage;
        this.state = "idle";
        this.path = new ArrayList<>();
        this.attackCooldown = 1.0f;
        this.attackTimer = 0;
        this.detectionRange = 300; // pixels
    }

    public void updateAI(Player player, Environment environment) {
        if (!alive) return;

        this.targetPlayer = player;
        float distanceToPlayer = (float) Math.sqrt(
            Math.pow(player.getX() - x, 2) + Math.pow(player.getY() - y, 2)
        );

        // Check if player is in range
        if (distanceToPlayer < detectionRange) {
            // Move towards player
            float dirX = player.getX() - x;
            float dirY = player.getY() - y;
            float distance = (float) Math.sqrt(dirX * dirX + dirY * dirY);

            if (distance > 0) {
                velocityX = (dirX / distance) * speed;
                velocityY = (dirY / distance) * speed;
                state = "moving";

                // Check if in attack range
                if (distanceToPlayer < 50) {
                    state = "attacking";
                    if (attackTimer <= 0) {
                        attack(player);
                        attackTimer = attackCooldown;
                    }
                }
            }
        } else {
            state = "idle";
            velocityX = 0;
            velocityY = 0;
        }
    }

    public String findPathTo(float targetX, float targetY) {
        // Placeholder for A* pathfinding
        // In full implementation, would use A* algorithm
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
        return path;
    }

    public void setPath(List<Integer> path) {
        this.path = path;
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
        if (!alive) return;

        // Update position based on velocity
        x += velocityX * dt;
        y += velocityY * dt;

        // Update attack timer
        attackTimer -= dt;
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
