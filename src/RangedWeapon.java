public class RangedWeapon extends Weapon {
    private int ammoPerShot;
    private float projectileSpeed;

    public RangedWeapon(String name, int damage, float cooldown, String ammoType, int ammoPerShot,
            float projectileSpeed) {
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

    @Override
    protected void rangedAttack(Entity origin, float targetX, float targetY, Level level) {
        if (level == null)
            return;

        float startX = origin.getX() + origin.getWidth() / 2.0f;
        float startY = origin.getY() + origin.getHeight() / 2.0f;

        float dirX = targetX - startX;
        float dirY = targetY - startY;

        // Normalize
        float length = (float) Math.sqrt(dirX * dirX + dirY * dirY);
        if (length != 0) {
            dirX /= length;
            dirY /= length;
        }

        // Create projectile
        // Infinite range so we use a large max distance
        Projectile p = new Projectile(startX, startY, dirX, dirY, projectileSpeed, getDamage(), 2000f);
        level.addProjectile(p);
    }
}
