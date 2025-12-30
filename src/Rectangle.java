public class Rectangle {
    public float x;
    public float y;
    public int width;
    public int height;

    public Rectangle(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean intersects(Rectangle other) {
        return this.x < other.x + other.width &&
               this.x + this.width > other.x &&
               this.y < other.y + other.height &&
               this.y + this.height > other.y;
    }

    public boolean contains(float px, float py) {
        return px >= x && px < x + width &&
               py >= y && py < y + height;
    }
}
