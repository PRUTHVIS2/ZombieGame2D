import graphics.Animation;
import java.awt.image.BufferedImage;

public class Character extends Entity {
    protected int hp;
    protected int maxHp;
    protected float speed;
    protected boolean facingRight = true;
    protected BufferedImage currentFrame;
    protected Animation currentAnimation;

    public boolean isFacingRight() {
        return facingRight;
    }

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

    public BufferedImage getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(BufferedImage frame) {
        this.currentFrame = frame;
    }

    @Override
    public void update(float dt) {
        if (currentAnimation != null) {
            currentAnimation.update();
            currentFrame = currentAnimation.getCurrentFrame();
        }
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

    protected graphics.Animation loadAnimationFromStrip(String path, int frameCount, int speed) {
        try {
            java.awt.image.BufferedImage strip = graphics.ResourceManager.getTexture(path);
            if (strip != null) {
                // Auto-detect frame count if 0 or negative
                if (frameCount <= 0) {
                    if (strip.getHeight() > 0) {
                        frameCount = strip.getWidth() / strip.getHeight();
                    } else {
                        frameCount = 1; // Safety fallback
                    }
                }

                // Prevent division by zero
                if (frameCount < 1)
                    frameCount = 1;

                int frameWidth = strip.getWidth() / frameCount;
                int frameHeight = strip.getHeight();

                java.awt.image.BufferedImage[] frames = new java.awt.image.BufferedImage[frameCount];
                for (int i = 0; i < frameCount; i++) {
                    frames[i] = strip.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
                }
                return new graphics.Animation(speed, frames);
            }
        } catch (Exception e) {
            System.err.println("Error loading animation from " + path + ": " + e.getMessage());
        }
        return null;
    }
}
