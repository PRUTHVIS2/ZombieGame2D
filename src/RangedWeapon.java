public class RangedWeapon extends Weapon {
    private int ammoPerShot;
    private float projectileSpeed;

    public RangedWeapon(String name, int damage, float cooldown, String ammoType, int ammoPerShot, float projectileSpeed) {
        super(name, damage, cooldown, "ranged", ammoType);
        this.ammoPerShot = ammoPerShot;
        this.projectileSpeed = projectileSpeed;
    }

    public int getAmmoPerShot() {
        return ammoPerShot;
    }

    public void setAmmoPerShot(int ammoPerShot) {
        this.ammoPerShot = ammoPerShot;
    }

    public float getProjectileSpeed() {
        return projectileSpeed;
    }

    public void setProjectileSpeed(float projectileSpeed) {
        this.projectileSpeed = projectileSpeed;
    }
}
