import java.util.List;

public class Weapon {
    private String name;
    private int damage;
    private float cooldown;
    private String type; // "melee" or "ranged"
    private String ammoType; // Type of ammo (null for melee)
    private float range; // Attack range in pixels

    public Weapon(String name, int damage, float cooldown, String type) {
        this.name = name;
        this.damage = damage;
        this.cooldown = cooldown;
        this.type = type;
        this.range = type.equals("melee") ? 50 : 300;
        this.ammoType = type.equals("ranged") ? "bullet" : null;
    }

    public Weapon(String name, int damage, float cooldown, String type, String ammoType) {
        this(name, damage, cooldown, type);
        this.ammoType = ammoType;
    }

    public void attack(Entity origin, Entity target) {
        // Attack logic will be implemented based on weapon type
        if (type.equals("melee")) {
            meleAttack(origin, target);
        }
    }

    public void attack(Entity origin, float targetX, float targetY, Level level) {
        if (type.equals("melee")) {
            // Melee still needs a target entity for now, or we do a radius check
            // For simplicity, let's just ignore coordinates for melee or use them for
            // direction
            // But existing melee logic relies on passing a specific target (which is null
            // in Player.java currently)

            // In Player.java attack(), it calls currentWeapon.attack(this, null).
            // MeleeWeapon needs to find targets if target is null.
            if (level != null) {
                // Simple melee area check
                float reach = 50;
                // Ideally we check all zombies.
                for (Zombie z : level.getZombies()) {
                    float dx = origin.getX() - z.getX();
                    float dy = origin.getY() - z.getY();
                    float dist = (float) Math.sqrt(dx * dx + dy * dy);

                    if (z.isAlive() && dist < reach) {
                        // Check angle if we want to be precise, or just distance
                        z.takeDamage(damage);
                    }
                }
            }
        } else if (type.equals("ranged")) {
            rangedAttack(origin, targetX, targetY, level);
        }
    }

    private void meleAttack(Entity origin, Entity target) {
        // Melee attack in a radius
        if (target != null && target instanceof Zombie) {
            Zombie zombie = (Zombie) target;
            if (zombie.checkCollision((Character) origin)) {
                zombie.takeDamage(damage);
            }
        }
    }

    protected void rangedAttack(Entity origin, float targetX, float targetY, Level level) {
        // To be overridden or implemented
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public float getCooldown() {
        return cooldown;
    }

    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmmoType() {
        return ammoType;
    }

    public void setAmmoType(String ammoType) {
        this.ammoType = ammoType;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }
}
