import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class InputHandler {
    private boolean[] keys = new boolean[256];
    private float mouseX;
    private float mouseY;
    private boolean mousePressed;

    public void keyPressed(int keyCode) {
        if (keyCode < keys.length) {
            keys[keyCode] = true;
        }
    }

    public void keyReleased(int keyCode) {
        if (keyCode < keys.length) {
            keys[keyCode] = false;
        }
    }

    public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        mousePressed = true;
    }

    public void mouseReleased(MouseEvent e) {
        mousePressed = false;
    }

    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public boolean isKeyPressed(int keyCode) {
        return keyCode < keys.length && keys[keyCode];
    }

    public boolean isMovingUp() {
        return isKeyPressed(KeyEvent.VK_W);
    }

    public boolean isMovingDown() {
        return isKeyPressed(KeyEvent.VK_S);
    }

    public boolean isMovingLeft() {
        return isKeyPressed(KeyEvent.VK_A);
    }

    public boolean isMovingRight() {
        return isKeyPressed(KeyEvent.VK_D);
    }

    public boolean isAttacking() {
        return mousePressed;
    }

    public float getMouseX() {
        return mouseX;
    }

    public float getMouseY() {
        return mouseY;
    }

    public void reset() {
        mousePressed = false;
    }
}
