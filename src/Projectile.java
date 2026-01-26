
import java.awt.Color;
import java.awt.Graphics2D;

public class Projectile extends Entity {
    private float dx;
    private float dy;
    private float speed;
    private int damage;
    private boolean active;
    private float distanceTraveled;
    private float maxDistance;

    public Projectile(float x, float y, float dx, float dy, float speed, int damage, float maxDistance) {
        super(x, y, 8, 8); // Larger size for better visibility
        this.dx = dx;
        this.dy = dy;
        this.speed = speed;
        this.damage = damage;
        this.maxDistance = maxDistance; // Can be float.MAX_VALUE for infinite
        this.active = true;
        this.distanceTraveled = 0;

        // Normalize direction
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        if (length != 0) {
            this.dx /= length;
            this.dy /= length;
        }
    }

    @Override
    public void update(float dt) {
        float moveX = dx * speed * dt;
        float moveY = dy * speed * dt;

        x += moveX;
        y += moveY;

        distanceTraveled += Math.sqrt(moveX * moveX + moveY * moveY);

        if (distanceTraveled > maxDistance) {
            active = false;
        }
    }

    @Override
    public void render(Object g) {
        // Assuming g is Graphics2D (or wrapping it) - adapting to existing pattern
        if (g instanceof Graphics2D) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.RED);
            g2d.fillOval((int) x, (int) y, width, height);
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public Object getBounds() {
        bounds.x = x;
        bounds.y = y;
        bounds.width = width;
        bounds.height = height;
        return bounds;
    }
}
