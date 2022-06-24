package main.objekte;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.util.concurrent.ThreadLocalRandom;
import kapitel01.POGL;
import main.utility.NumberUtil;

public class Boden extends BasisObjekt {
  // Liste der Blattfarben
  static private final float[][] colors = {
      {0.875f, 0.324f, 0.051f},
      {0.965f, 0.699f, 0.1328f},
  };
  static {
    for (float[] color : colors) {
      for (int i = 0; i < color.length; i++) color[i] *= 1.1;
    }
  }

  private final int width, height;

  public Boden(int width, int height) {
    this.width = width;
    this.height = height;
  }

  private float getHeightLevel4(double x) {
    return (float) (
        Math.sin(x * 0.08 + 750) * 2
            + Math.sin(x * 0.023 + 230) * 10
            + Math.sin(x * 0.01 - 800) * 15
            + Math.sin(x * 0.0025 - 700) * 40
            + 310
    );
  }

  private float getHeightLevel3(double x) {
    return (float) (
        Math.sin(x * 0.0756 + 800) * 2
            + Math.sin(x * 0.02 + 300) * 10
            + Math.sin(x * 0.008 - 500) * 15
            + Math.sin(x * 0.0035 - 400) * 40
            + 320
    ) * (float) Math.sqrt(1.0 - x / width * 0.7);
  }

  private float getHeightLevel2(double x) {
    return (float) (
        Math.sin(x * 0.04 + 800) * 2
            + Math.sin(x * 0.015 + 300) * 10
            + Math.sin(x * 0.006 - 500) * 15
            + Math.sin((x - 200) / width * Math.PI * 0.7) * 150
            + 95
    );
  }

  private float getHeightLevel1(double x) {
    return (float) (
        Math.sin(x * 0.0356 + 600) * 5
            + Math.sin(x * 0.01 + 100) * 20
            + Math.sin(x * 0.004 - 400) * 30
            + 140
        );
  }

  public float getBottomHeight(double x) {
    return (float) (
        Math.sin(x * 0.0356 + 600) * 5
            + Math.sin(x * 0.01 + 100) * 10
            + Math.sin(x * 0.004 - 400) * 15
            + 75
    );
  }

  @Override
  public void render() {
    glLoadIdentity();

    // Hinterster Hügel
    glColor3d(0.7,0.7,0.1);
    glTranslated(0, height,-3);
    glBegin(GL_QUADS);
    for (int x = 0; x < width; x += 10) {

      glVertex3f(x, -getHeightLevel4(x), 0);
      glVertex3f(x + 10, -getHeightLevel4(x + 10), 0);
      glVertex3f(x + 10, 0, 0);
      glVertex3f(x, 0, 0);

    }
    glEnd();

    glLoadIdentity();

    // Hinterster Hügel
    glColor3d(235. / 255, 208. / 255,65. / 255);
    glTranslated(0, height,-3);
    glBegin(GL_QUADS);
    for (int x = 0; x < width; x += 10) {

      glVertex3f(x, -getHeightLevel3(x), 0);
      glVertex3f(x + 10, -getHeightLevel3(x + 10), 0);
      glVertex3f(x + 10, 0, 0);
      glVertex3f(x, 0, 0);

    }
    glEnd();

    glLoadIdentity();

    // Mittlerer Hügel
    glColor3d(244. / 255,159. / 255, 0. / 255);
    glTranslated(0, height,-3);
    glBegin(GL_QUADS);
    for (int x = 0; x < width; x += 10) {

      glVertex3f(x, -getHeightLevel2(x), 0);
      glVertex3f(x + 10, -getHeightLevel2(x + 10), 0);
      glVertex3f(x + 10, 0, 0);
      glVertex3f(x, 0, 0);

    }
    glEnd();

    glLoadIdentity();

    // Vorderer Hügel
    glColor3d(198. / 255,178. / 255,28. / 255);
    glTranslated(0, height,-1);
    glBegin(GL_QUADS);
    for (int x = 0; x < width; x += 10) {

      glVertex3f(x, -getHeightLevel1(x), 0);
      glVertex3f(x + 10, -getHeightLevel1(x + 10), 0);
      glVertex3f(x + 10, 0, 0);
      glVertex3f(x, 0, 0);

    }
    glEnd();
  }
}
