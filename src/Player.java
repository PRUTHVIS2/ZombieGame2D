import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player extends Character {
    private int lives;
    private List<Weapon> weapons;
    private Weapon currentWeapon;
    private Map<String, Integer> ammo;
    private UI hud;
    private float attackCooldown;
    private float attackTimer;
    private float invulnerabilityTimer;
    private static final float INVULNERABILITY_TIME = 1.0f;

    public Player(float x, float y, int width, int height, int maxHp, int lives) {
        super(x, y, width, height, maxHp);
        this.lives = lives;
        this.weapons = new ArrayList<>();
        this.ammo = new java.util.HashMap<>();
        this.attackCooldown = 0.5f;
        this.attackTimer = 0;
        this.invulnerabilityTimer = 0;
        this.speed = 200; // Pixels per second
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

        // Handle movement (WASD)
        if (input.isMovingUp()) {
            velocityY = -speed;
        }
        if (input.isMovingDown()) {
            velocityY = speed;
        }
        if (input.isMovingLeft()) {
            velocityX = -speed;
        }
        if (input.isMovingRight()) {
            velocityX = speed;
        }

        // Handle attacks
        if (input.isAttacking() && attackTimer <= 0) {
            attack(input.getMouseX(), input.getMouseY());
            attackTimer = attackCooldown;
        }
    }

    public void attack(float targetX, float targetY) {
        if (currentWeapon != null) {
            currentWeapon.attack(this, null);
        }
    }

    public void meleeAttack() {
        // Deal melee damage in a small radius
        if (currentWeapon != null && currentWeapon.getName().contains("Sword") || currentWeapon.getName().contains("Mace")) {
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

    @Override
    public void update(float dt) {
        // Update position
        x += velocityX * dt;
        y += velocityY * dt;

        // Update cooldowns
        attackTimer -= dt;
        invulnerabilityTimer -= dt;
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
