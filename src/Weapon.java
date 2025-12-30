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
        } else if (type.equals("ranged")) {
            rangedAttack(origin, target);
        }
    }

    private void meleAttack(Entity origin, Entity target) {
        // Melee attack in a radius
        if (target instanceof Zombie) {
            Zombie zombie = (Zombie) target;
            if (zombie.checkCollision((Character) origin)) {
                zombie.takeDamage(damage);
            }
        }
    }

    private void rangedAttack(Entity origin, Entity target) {
        // Ranged attack - create projectile
        // Projectiles would be managed by Level/Game
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
