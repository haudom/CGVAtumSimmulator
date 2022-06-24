package main.objekte;

import kapitel01.POGL;

import static org.lwjgl.opengl.GL11.*;

public class Background extends BasisObjekt {
  private final int WIDTH, HEIGHT;

  public Background(int width, int height) {
    WIDTH = width;
    HEIGHT = height;
  }

  @Override
  public void render() {
    glLoadIdentity();
    glTranslated(0, 0, -10);
    POGL.renderGradient(
        WIDTH, (int) (HEIGHT * 0.8),
        84. / 255, 159. / 255, 239. / 255,
        202. / 255, 235. / 255, 255. / 255
    );
  }
}
