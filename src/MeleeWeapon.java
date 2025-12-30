public class MeleeWeapon extends Weapon {
    private float range;

    public MeleeWeapon(String name, int damage, float cooldown, float range) {
        super(name, damage, cooldown, "melee");
        this.range = range;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }
}
