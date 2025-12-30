public class Character extends Entity {
    protected int hp;
    protected int maxHp;

    public Character(float x, float y, int width, int height, int maxHp) {
        super(x, y, width, height);
        this.maxHp = maxHp;
        this.hp = maxHp;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) {
            hp = 0;
        }
        if (hp <= 0) {
            alive = false;
        }
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
        if (this.hp >= maxHp) {
            this.hp = maxHp;
        }
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    @Override
    public void update(float dt) {
        // Base character update logic
    }

    @Override
    public void render(Object g) {
        // Base character render logic
    }

    @Override
    public Object getBounds() {
        // Return bounds rectangle
        return null;
    }
}
