import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player extends Character {
    private graphics.Animation idleAnim;
    private graphics.Animation walkAnim;

    private int lives;
    private java.util.List<Weapon> weapons;
    private Weapon currentWeapon;
    private java.util.Map<String, Integer> ammo;
    private UI hud;
    private float attackCooldown;
    private float attackTimer;
    private float invulnerabilityTimer;
    private static final float INVULNERABILITY_TIME = 1.0f;
    private Environment environment;

    // Stamina & Speed
    private float walkSpeed;
    private float runSpeed;
    private float stamina;
    private float maxStamina;
    private float staminaRegen;
    private float staminaDrain;
    private static final float STAMINA_COOLDOWN = 1.0f;
    private float staminaRegenTimer;

    public Player(float x, float y, int width, int height, int maxHp, int lives) {
        super(x, y, width, height, maxHp);
        this.lives = lives;
        this.weapons = new ArrayList<>();
        this.ammo = new java.util.HashMap<>();
        this.attackCooldown = 0.5f;
        this.attackTimer = 0;
        this.invulnerabilityTimer = 0;

        // Speed & Stamina Configuration
        this.walkSpeed = 60; // Matches Zombie base speed
        this.runSpeed = 200; // Fast sprint
        this.speed = walkSpeed;

        this.maxStamina = 100;
        this.stamina = maxStamina;
        this.staminaDrain = 30; // Drain per second
        this.staminaRegen = 20; // Regen per second
        this.staminaRegenTimer = 0;

        loadAnimations();
    }

    private void loadAnimations() {
        // Load separate animations using inherited helper
        // Use 0 as frameCount to auto-detect based on square frames

        // Idle Animation
        idleAnim = loadAnimationFromStrip("assets/skins/player/Idle.png", 0, 200);

        // Walk/Run Animation
        // Prefer Run.png since base speed is 200
        walkAnim = loadAnimationFromStrip("assets/skins/player/Run.png", 0, 100);
        if (walkAnim == null) {
            walkAnim = loadAnimationFromStrip("assets/skins/player/Walk.png", 0, 100);
        }

        // Fallback for walk if not present
        if (walkAnim == null && idleAnim != null) {
            walkAnim = idleAnim;
        }

        currentAnimation = idleAnim;
    }

    public void spawn(float x, float y) {
        this.x = x;
        this.y = y;
        this.alive = true;
        this.hp = maxHp;
        this.invulnerabilityTimer = INVULNERABILITY_TIME;
    }

    public void handleInput(InputHandler input) {
        // Reset velocity
        velocityX = 0;
        velocityY = 0;

        // Handle Running
        boolean isRunning = input.isRunning()
                && (input.isMovingUp() || input.isMovingDown() || input.isMovingLeft() || input.isMovingRight());

        if (isRunning && stamina > 0) {
            speed = runSpeed;
            stamina -= staminaDrain * (1.0f / 60.0f); // Approx dt
            staminaRegenTimer = STAMINA_COOLDOWN;
            if (stamina < 0)
                stamina = 0;
        } else {
            speed = walkSpeed;
            if (staminaRegenTimer > 0) {
                staminaRegenTimer -= (1.0f / 60.0f);
            } else if (stamina < maxStamina) {
                stamina += staminaRegen * (1.0f / 60.0f);
                if (stamina > maxStamina)
                    stamina = maxStamina;
            }
        }

        // Handle movement (WASD)
        if (input.isMovingUp()) {
            velocityY = -speed;
        }
        if (input.isMovingDown()) {
            velocityY = speed;
        }
        if (input.isMovingLeft()) {
            velocityX = -speed;
            facingRight = false;
        }
        if (input.isMovingRight()) {
            velocityX = speed;
            facingRight = true;
        }

        // Update Animation State
        if (idleAnim != null && velocityX == 0 && velocityY == 0) {
            currentAnimation = idleAnim;
        } else if (speed == runSpeed && walkAnim != null) {
            currentAnimation = walkAnim;
        } else if (walkAnim != null) {
            currentAnimation = walkAnim;
        }

        // Handle attacks
        if (input.isAttacking() && attackTimer <= 0) {
            attack(input.getMouseX(), input.getMouseY());
            attackTimer = attackCooldown;
        }
    }

    public float getStamina() {
        return stamina;
    }

    public float getMaxStamina() {
        return maxStamina;
    }

    public void attack(float targetX, float targetY) {
        if (currentWeapon != null) {
            currentWeapon.attack(this, null);
        }
    }

    public void meleeAttack() {
        // Deal melee damage in a small radius
        if (currentWeapon != null && currentWeapon.getName().contains("Sword")
                || currentWeapon.getName().contains("Mace")) {
            // Damage will be applied through collision detection
        }
    }

    public void useBomb() {
        // Use bomb ammo if available
        if (ammo.containsKey("bomb") && ammo.get("bomb") > 0) {
            ammo.put("bomb", ammo.get("bomb") - 1);
            // Create bomb explosion effect
        }
    }

    public void die() {
        lives--;
        hp = 0;
        alive = false;
        if (lives > 0) {
            spawn(x, y);
        }
    }

    public void respawn() {
        this.alive = true;
        this.hp = maxHp;
        this.invulnerabilityTimer = INVULNERABILITY_TIME;
    }

    @Override
    public void takeDamage(int damage) {
        if (invulnerabilityTimer <= 0) {
            hp -= damage;
            if (hp < 0) {
                hp = 0;
            }
            invulnerabilityTimer = INVULNERABILITY_TIME;
            if (hp <= 0) {
                die();
            }
        }
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public void addWeapon(Weapon weapon) {
        weapons.add(weapon);
        if (currentWeapon == null) {
            currentWeapon = weapon;
        }
    }

    public void removeWeapon(Weapon weapon) {
        weapons.remove(weapon);
        if (currentWeapon == weapon && !weapons.isEmpty()) {
            currentWeapon = weapons.get(0);
        }
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public void setCurrentWeapon(Weapon weapon) {
        if (weapons.contains(weapon)) {
            this.currentWeapon = weapon;
        }
    }

    public Map<String, Integer> getAmmo() {
        return ammo;
    }

    public void setAmmo(String ammoType, int amount) {
        ammo.put(ammoType, amount);
    }

    public void addAmmo(String ammoType, int amount) {
        ammo.put(ammoType, ammo.getOrDefault(ammoType, 0) + amount);
    }

    public UI getHud() {
        return hud;
    }

    public void setHud(UI hud) {
        this.hud = hud;
    }

    public float getInvulnerabilityTimer() {
        return invulnerabilityTimer;
    }

    public boolean isInvulnerable() {
        return invulnerabilityTimer > 0;
    }

    public void setEnvironment(Environment env) {
        this.environment = env;
    }

    public void update(float dt) {
        // Calculate potential new positions
        float nextX = x + velocityX * dt;
        float nextY = y + velocityY * dt;

        if (environment != null) {
            // Check X axis collision
            if (canMove(nextX, y)) {
                x = nextX;
            }

            // Check Y axis collision
            if (canMove(x, nextY)) {
                y = nextY;
            }

            // Keep within strict window bounds as a failsafe
            int windowWidth = environment.getWidth();
            int windowHeight = environment.getHeight();

            if (x < 0)
                x = 0;
            if (y < 0)
                y = 0;
            if (x + width > windowWidth)
                x = windowWidth - width;
            if (y + height > windowHeight)
                y = windowHeight - height;
        } else {
            // Fallback if no environment
            x = nextX;
            y = nextY;
        }

        // Update cooldowns
        attackTimer -= dt;
        invulnerabilityTimer -= dt;

        // Update Animation
        if (velocityX != 0 || velocityY != 0) {
            if (walkAnim != null)
                currentAnimation = walkAnim;
        } else {
            if (idleAnim != null)
                currentAnimation = idleAnim;
        }

        super.update(dt);
    }

    private boolean canMove(float newX, float newY) {
        // Check all 4 corners of the player's bounding box
        return environment.isWalkable(newX, newY) &&
                environment.isWalkable(newX + width - 1, newY) &&
                environment.isWalkable(newX, newY + height - 1) &&
                environment.isWalkable(newX + width - 1, newY + height - 1);
    }

    @Override
    public void render(Object g) {
        // Render player sprite
        // Can add visual feedback for invulnerability (blinking)
    }

    @Override
    public Object getBounds() {
        bounds.x = x;
        bounds.y = y;
        return bounds;
    }
}
