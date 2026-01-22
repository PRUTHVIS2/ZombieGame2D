public interface Renderable {
    void render(Object g);

    java.awt.image.BufferedImage getCurrentFrame();
}
