package main.objekte;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import kapitel01.Model;
import kapitel01.POGL;
import kapitel04.Vektor3D;
import main.Wind;
import main.utility.NumberUtil;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class Blatt extends BasisObjekt {
  // Liste an Blatttexturen
  static private float[][] colors = {
      {0.875f, 0.324f, 0.051f},
      {0.965f, 0.699f, 0.1328f},
  };

  // Laubgebläse Instanz von der die Parameter für die Physik abgelesen werden
  public Laubgeblaese laubgeblaese;
  public Wind wind;

  public double mass = 0.001;
  Model model;

  public float[] color;

  public Blatt(Laubgeblaese laubgeblaese, Wind wind, Vektor3D position, Vektor3D velocity) {
    super(position);

    this.wind = wind;
    this.laubgeblaese = laubgeblaese;
    this.velocity = velocity;

    // Generiere eine zufällige Farbe, die zwischen den Rot- und Orange-Werten in Blatt.colors liegt
    float randomValue = ThreadLocalRandom.current().nextFloat();
    this.color = new float[]{
        NumberUtil.lerp(colors[0][0], colors[1][0], randomValue),
        NumberUtil.lerp(colors[0][1], colors[1][1], randomValue),
        NumberUtil.lerp(colors[0][2], colors[1][2], randomValue),
    };

    try {
      model = POGL.loadModel(new File("./objects/Blatt.obj"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void render() {
    glLoadIdentity();

      glTranslated(position.x, position.y, position.z -400);

      glColor4f(color[0], color[1], color[2], 1.0f);

      glScaled(10,10,10);
      glRotated(90,0.4,1,0);

    //POGL.renderViereck(10, 10);
    POGL.renderObject(model);
  }

  public double getBottom() {
    return Display.getDisplayMode().getHeight() * 0.9;
  }

  public double getReibung() {
    double BOTTOM = getBottom();

    // Wenn Blätter am Boden liegen, sollten sie schwerer zu bewegen sein
    return Math.max(0.0, position.y - (BOTTOM * 0.9)) / (BOTTOM * 0.1);
  }

  public void applyGravity(double time) {
    double BOTTOM = getBottom();
    double PPM = 200; // Pixel per Meter // TODO GL-Einheiten auf Meter anpassen?

    double g = 9.81;
    // xf = x0 + v0 * t + 0.5 * g * t * t;
    Vektor3D gravity = new Vektor3D(0.0, 0.5 * g * PPM * time * time, 0.0);

    this.velocity.add(gravity);

    if (position.y == BOTTOM) {
      this.velocity.mult(0);
    }
  }

  public void applyBlaeser(double time) {
    Vektor3D blaeser = new Vektor3D(laubgeblaese.getForceAt(position));
    blaeser.mult(time);
    velocity.add(blaeser);
  }

  public void applyWind(double time) {
    Vektor3D windAcceleration = new Vektor3D(wind.velocity);

    double t = 1.0 - Math.pow(position.y / getBottom(), 2);
    windAcceleration.mult(t);

    windAcceleration.mult(time);
    velocity.add(windAcceleration);
  }

  @Override
  public void update(double time) {
    int WIDTH = Display.getDisplayMode().getWidth();
    double BOTTOM = getBottom();

    applyGravity(time);

    applyBlaeser(time);

    applyWind(time);

    position.add(velocity);

    /* ******* ÜBERPRÜFE BILDRÄNDER ****** */

    int PADDING = 100;

    // Überprüfe, ob Blatt über die Bildränder hinausgehen würde
    if (position.x < -PADDING) position.setX(WIDTH + position.x + 2*PADDING);
    if (position.x > WIDTH + PADDING) position.setX(position.x - WIDTH - 2*PADDING);
    if (position.y > BOTTOM) position.setY(BOTTOM);
  }
}
