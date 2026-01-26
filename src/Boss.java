
public class Boss extends Zombie {

    public Boss(float x, float y, int hp, float speed, int attackDamage) {
        // Boss is 4x size (32 * 4 = 128)
        super(x, y, 128, 128, hp, speed, attackDamage);
    }

    @Override
    protected void loadAnimations() {
        // Use 0 as frameCount to auto-detect based on square frames (width/height)

        // Idle Animation
        idleAnim = loadAnimationFromStrip("assets/skins/boss/Idle.png", 0, 200);

        // Move Animation
        moveAnim = loadAnimationFromStrip("assets/skins/boss/Walk.png", 0, 150);

        // Attack Animation
        attackAnim = loadAnimationFromStrip("assets/skins/boss/Attack.png", 0, 150);

        // Hurt Animation
        hurtAnim = loadAnimationFromStrip("assets/skins/boss/Hurt.png", 0, 100);
        if (hurtAnim != null) {
            hurtAnim.setLooping(false);
        }

        // Death Animation
        deadAnim = loadAnimationFromStrip("assets/skins/boss/Dead.png", 0, 100);
        if (deadAnim != null) {
            deadAnim.setLooping(false);
        }

        // Set initial animation
        if (idleAnim != null) {
            currentAnimation = idleAnim;
        } else if (moveAnim != null) {
        }
    }

    public void updateAI(Player player, Environment environment, float dt) {
        if (!alive)
            return;

        this.targetPlayer = player;

        // Use center-to-center distance for more accurate large-unit interaction
        float centerX = x + width / 2;
        float centerY = y + height / 2;
        float playerCenterX = player.getX() + player.getWidth() / 2;
        float playerCenterY = player.getY() + player.getHeight() / 2;

        float distanceToPlayer = (float) Math.sqrt(
                Math.pow(playerCenterX - centerX, 2) + Math.pow(playerCenterY - centerY, 2));

        // Update pathfinding timer
        // We use a simpler approach for Boss:
        // If close, move directly. If far, use pathfinding (or just move directly if
        // map is open).
        // Given the "back and forth" issue, direct movement when close is smoother.

        // Attack range: Boss is large (radius ~64). Player radius ~16.
        // Collision distance ~80. Attack range should be slightly larger, e.g. 100-150.
        float attackRangeThreshold = 150.0f;

        if (distanceToPlayer < attackRangeThreshold) {
            state = "attacking";
            velocityX = 0;
            velocityY = 0;

            // Face player
            if (playerCenterX > centerX)
                facingRight = true;
            else
                facingRight = false;

            if (attackTimer <= 0) {
                attack(player);
                attackTimer = attackCooldown;
            }
        } else {
            // Move towards player
            // If very close (but not attack range), move directly to avoid pathfinding
            // jitter
            if (distanceToPlayer < 400) {
                moveDirectly(playerCenterX, playerCenterY, dt, environment);
            } else {
                // Determine direction via pathfinding or direct fallback
                // For now, simpler direct movement is often more robust for large bosses
                // unless there are complex mazes. "Zombie" base pathfinding assumes size 32.
                // We'll use direct movement for proper responsiveness as requested.
                moveDirectly(playerCenterX, playerCenterY, dt, environment);
            }
        }

        attackTimer -= dt;
    }

    private void moveDirectly(float targetX, float targetY, float dt, Environment environment) {
        float centerX = x + width / 2;
        float centerY = y + height / 2;
        float dirX = targetX - centerX;
        float dirY = targetY - centerY;

        float length = (float) Math.sqrt(dirX * dirX + dirY * dirY);
        if (length > 0) {
            float vx = (dirX / length) * speed;
            float vy = (dirY / length) * speed;

            // Check walkability of the *next step* for the Boss's large body?
            // "Environment.isWalkable" checks a point.
            // We'll just move. The physics loop (Environment.checkCollisions) handles wall
            // collisions.

            // Allow moving smoothly
            velocityX = vx;
            velocityY = vy;
            state = "moving";

            if (vx > 0)
                facingRight = true;
            if (vx < 0)
                facingRight = false;
        }
    }
}
