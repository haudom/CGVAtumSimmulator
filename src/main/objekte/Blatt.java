package main.objekte;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import kapitel01.Model;
import kapitel01.POGL;
import kapitel04.Vektor2D;
import kapitel04.Vektor3D;
import main.Wind;
import main.utility.NumberUtil;
import org.lwjgl.opengl.Display;

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

  public Vektor3D gravity = new Vektor3D();
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

  @Override
  public void update(double time) {
    int WIDTH = Display.getDisplayMode().getWidth();
    double BOTTOM = getBottom();

    double PPM = (double) BOTTOM / 7; // Pixel per Meter // TODO GL-Einheiten auf Meter anpassen?

    /* ******* SETUP ****** */

    Vektor3D velocitySum = new Vektor3D();

    velocitySum.add(wind.velocity);
    velocitySum.add(velocity);

    /* ******* REIBUNG ****** */

    double reibung = getReibung();

    // wenn Blatt auf dem Boden liegt, ist es sehr schwer horizontal bewegbar
    velocitySum.setX(velocitySum.x * (1.0 - reibung));
    // wenn Blatt auf dem Boden liegt, ist es ein wenig schwerer vertikal bewegbar, damit es
    // noch die Chance hat wieder vom Boden hochzukommen und sich mehr zu bewegen
    velocitySum.setY(velocitySum.y * (0.5 - reibung * 0.5)); // 50% der Reibung

    /* ******* GRAVITY ****** */

    // Wenn Blatt am Boden liegt, setze Erdanziehungskraft zurück
    if (reibung == 1.0) {
      gravity = new Vektor3D(0, 0, 0);
    } else {
      // Erdanziehungskraft extra auf Kräfte addieren F = m * 9.81 m/s²
      double F = mass * 9.81 * PPM;
      gravity.add(new Vektor3D(0, F, 0));
    }

    velocitySum.add(gravity);

    /* ******* LAUBGEBLÄSE ****** */

    velocitySum.add(laubgeblaese.getForceAt(position));

    /* ******* APPLY VELOCITY ****** */

    // Alle Velocity Werte sind x viele Pixel pro Sekunde, deswegen muss der Vektor noch auf
    // die Zeit angepasst werden, die wirklich zwischen dem aktuellen und letzten Frame vergangen
    // sind.
    velocitySum.mult(time);
    position.add(velocitySum);

    /* ******* ÜBERPRÜFE BILDRÄNDER ****** */

    // Überprüfe, ob Blatt über die Bildränder hinausgehen würde
    if (position.x < 0) position.setX(WIDTH + position.x);
    if (position.x > WIDTH) position.setX(position.x - WIDTH);
    if (position.y > BOTTOM) position.setY(BOTTOM);
  }
}
