public abstract class Entity {
    protected float x;
    protected float y;
    protected int width;
    protected int height;
    protected float velocityX;
    protected float velocityY;
    protected boolean alive;
    protected Rectangle bounds;
    protected float speed;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocityX = 0;
        this.velocityY = 0;
        this.alive = true;
        this.bounds = new Rectangle(x, y, width, height);
        this.speed = 100; // Default speed in pixels per second
    }

    public abstract void update(float dt);

    public abstract void render(Object g);

    public Object getBounds() {
        bounds.x = x;
        bounds.y = y;
        return bounds;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean checkCollision(Entity other) {
        Rectangle otherBounds = (Rectangle) other.getBounds();
        return bounds.intersects(otherBounds);
    }
}
