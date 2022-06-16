package main.objekte;

import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glTranslated;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import kapitel01.POGL;
import kapitel04.Vektor3D;
import main.Wind;

public class Blatt extends BasisObjekt {
  // Liste an Blatttexturen
  static private float[][] colors = {
      {0.875f, 0.324f, 0.051f},
      {0.965f, 0.699f, 0.1328f},
  };

  // Laubgebläse Instanz von der die Parameter für die Physik abgelesen werden
  public Laubgeblaese laubgeblaese;
  public Wind wind;

  public Vektor3D velocity;
  public double mass = 0;
  public float[] color;

  public Blatt(Laubgeblaese laubgeblaese, Wind wind, Vektor3D position, Vektor3D velocity) {
    super(position);

    this.wind = wind;
    this.laubgeblaese = laubgeblaese;
    this.velocity = velocity;

    // Generiere eine zufällige Farbe, die zwischen den Rot- und Orange-Werten in Blatt.colors liegt
    float randomValue = ThreadLocalRandom.current().nextFloat();
    this.color = new float[]{
        randomValue * (colors[0][0] - colors[1][0]) + colors[1][0],
        randomValue * (colors[0][1] - colors[1][1]) + colors[1][1],
        randomValue * (colors[0][2] - colors[1][2]) + colors[1][2],
    };
  }

  @Override
  public void render() {
    glLoadIdentity();
    glTranslated(position.x, position.y, position.z);

    glColor4f(this.color[0], this.color[1], this.color[2], 1.0f);

    POGL.renderViereck(10, 10);
  }

  @Override
  public void update(double time) {
    super.update(time);
  }
}
