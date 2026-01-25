package graphics;

import java.awt.image.BufferedImage;

public class Animation {
    private int speed;
    private int index;
    private long lastTime;
    private long timer;
    private BufferedImage[] frames;

    private boolean looping = true;
    private boolean finished = false;

    public Animation(int speed, BufferedImage[] frames) {
        this.speed = speed;
        this.frames = frames;
        this.index = 0;
        this.timer = 0;
        this.lastTime = System.currentTimeMillis();
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    public boolean isFinished() {
        return finished;
    }

    public void update() {
        timer += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();

        if (timer > speed) {
            index++;
            timer = 0;
            if (index >= frames.length) {
                if (looping) {
                    index = 0;
                } else {
                    index = frames.length - 1;
                    finished = true;
                }
            }
        }
    }

    public BufferedImage getCurrentFrame() {
        return frames[index];
    }

    public void reset() {
        index = 0;
        timer = 0;
        lastTime = System.currentTimeMillis();
        finished = false;
    }
}
