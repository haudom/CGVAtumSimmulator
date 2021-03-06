package main.objekte;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import kapitel01.Model;
import kapitel01.POGL;
import kapitel04.LineareAlgebra;
import kapitel04.Vektor3D;
import main.Wind;
import main.utility.NumberUtil;
import main.utility.VektorUtil;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;

public class Blatt extends BasisObjekt {
  // Liste der Blattfarben
  static private final float[][] colors = {
      {0.875f, 0.324f, 0.051f},
      {0.965f, 0.699f, 0.1328f},
  };
  static private Model model = null;

  private final double PPM = 200; // Pixel per Meter

  // Laubgebläse Instanz von der die Parameter für die Physik abgelesen werden
  public Laubgeblaese laubgeblaese;
  public Wind wind;
  public Boden bottom;
  public boolean useOBJ;

  public double mass = 0.001; // Gewicht in kg
  public double size = 0.0004; // Größe in m²

  public float[] color;

  public Blatt(Laubgeblaese laubgeblaese, Wind wind, Boden bottom, Vektor3D position, Vektor3D velocity, boolean useOBJ) {
    super(position);

    this.wind = wind;
    this.laubgeblaese = laubgeblaese;
    this.bottom = bottom;
    this.speed = velocity;
    this.useOBJ = useOBJ;

    // Generiere eine zufällige Farbe, die zwischen den Rot- und Orange-Werten in Blatt.colors liegt
    float randomValue = ThreadLocalRandom.current().nextFloat();
    this.color = new float[]{
        NumberUtil.lerp(colors[0][0], colors[1][0], randomValue),
        NumberUtil.lerp(colors[0][1], colors[1][1], randomValue),
        NumberUtil.lerp(colors[0][2], colors[1][2], randomValue),
    };

    if (this.useOBJ && model == null) {
      try {
        model = POGL.loadModel(new File("./resources/objects/Blatt.obj"));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void render() {
    glLoadIdentity();

    glTranslated(position.x, position.y, position.z);
    glScaled(10, 10, 10);
    glRotated(rotation.x, 1, 0, 0);
    glRotated(rotation.y, 0, 1, 0);
    glRotated(rotation.z, 0, 0, 1);
    glRotated(90, 0.4, 1, 0); // rotations-fix für model

    glColor4f(color[0], color[1], color[2], 1.0f);

    if (useOBJ && model != null) {
      POGL.renderObject(model);
    } else {
      // Nutze GL, um Blatt zu modellieren
      glBegin(GL_TRIANGLES);

      // BLATTFLÄCHE
      POGL.mirrorXFace(
          0, -0.640846f, -1.58905f,
          0, -0.738234f, -1.11924f,
          0.324593f, -0.777373f, -1.13709f
      );
      POGL.mirrorXFace(
          0, -0.738234f, -1.11924f,
          0.324593f, -0.777373f, -1.13709f,
          0, -0.845147f, -0.417682f
      );
      POGL.mirrorXFace(
          0.324593f, -0.777373f, -1.13709f,
          0, -0.845147f, -0.417682f,
          1.04153f, -0.985358f, -0.466856f
      );
      POGL.mirrorXFace(
          0, -0.845147f, -0.417682f,
          1.04153f, -0.985358f, -0.466856f,
          0, -0.850934f, 0.29147f
      );
      POGL.mirrorXFace(
          1.04153f, -0.985358f, -0.466856f,
          0, -0.850934f, 0.29147f,
          0.851437f, -0.962186f, 0.274806f
      );
      POGL.mirrorXFace(
          0, -0.850934f, 0.29147f,
          0.851437f, -0.962186f, 0.274806f,
          0, -0.832336f, 0.526535f
      );
      POGL.mirrorXFace(
          0.851437f, -0.962186f, 0.274806f,
          0, -0.832336f, 0.526535f,
          0.065162f, -0.836061f, 0.761987f
      );

      // STIL
      POGL.mirrorXFace(
          0, -0.832336f, 0.526535f,
          0.065162f, -0.836061f, 0.761987f,
          0, -0.797448f, 0.76154f
      );
      POGL.mirrorXFace(
          0.065162f, -0.836061f, 0.761987f,
          0, -0.797448f, 0.76154f,
          0.050795f, -0.653416f, 1.55312f
      );
      POGL.mirrorXFace(
          0, -0.797448f, 0.76154f,
          0.050795f, -0.653416f, 1.55312f,
          0, -0.623526f, 1.55224f
      );
      POGL.mirrorXFace(
          0.050795f, -0.653416f, 1.55312f,
          0, -0.623526f, 1.55224f,
          0, -0.683123f, 1.5523f
      );

      glEnd();
    }
  }

  public double getBottom(double x) {
    return Display.getDisplayMode().getHeight() - bottom.getBottomHeight(x);
  }

  public Vektor3D getGravityForce(double time) {
    // TODO GL-Einheiten auf Meter anpassen? Ne zu aufwändig

    double g = 9.81;
    // xf = x0 + v0 * t + 0.5 * g * t * t;
    Vektor3D gravity = new Vektor3D(0.0, 0.5 * g * time, 0.0);

    return gravity;
  }

  public Vektor3D getBlaeserWindSpeed() {
    Vektor3D blaeserSpeed = laubgeblaese.getSpeedAt(position);

    return blaeserSpeed;
  }

  private Vektor3D getGlobalWindSpeed() {
    Vektor3D windSpeed = new Vektor3D(wind.velocity);

    return windSpeed;
  }

  public double calcWinkelToLuft(Vektor3D wind) {
    wind = new Vektor3D(wind);

    // Ein Vektor, der von der Fläche des Blattes ausgeht
    Vektor3D blattFlaecheVektor = new Vektor3D(0, 1, 0);
    blattFlaecheVektor = VektorUtil.rotateVektor(blattFlaecheVektor, new Vektor3D(1, 0, 0), rotation.x);
    blattFlaecheVektor = VektorUtil.rotateVektor(blattFlaecheVektor, new Vektor3D(0, 1, 0), rotation.y);
    blattFlaecheVektor = VektorUtil.rotateVektor(blattFlaecheVektor, new Vektor3D(0, 0, 1), rotation.z);
    blattFlaecheVektor.normalize();

    wind.normalize();

    double dotProdukt = LineareAlgebra.dotProduct(
        wind,
        blattFlaecheVektor
    );
    double lengthA = wind.length();
    double lengthB = blattFlaecheVektor.length();

    return Math.acos(dotProdukt / (lengthA * lengthB));
  }

  public Vektor3D getWindlast() {
    Vektor3D v_RelativeWindgeschwindigkeit = new Vektor3D();

    // Addiere alle Windgeschwindigkeiten
    v_RelativeWindgeschwindigkeit.add(getGlobalWindSpeed());
    v_RelativeWindgeschwindigkeit.add(getBlaeserWindSpeed());

    // Berechne Windgeschwindigkeit relativ zur Objektgeschwindigkeit
    v_RelativeWindgeschwindigkeit.sub(speed);

    // p_Luftdichte - Dichte des Mediums (Luft)
    final double p_Luftdichte = 1.2041; // Bei 20 °C auf Meereshöhe in m³
    // c_p - Druckbeiwert
    final double c_p = 1.11;

    // winddruck (in N/m²) = 1/2 * p_Luftdichte * c_p * v_Windgeschwindigkeit ²
    // windlast (in N)     = A_Stirnfläche * winddruck;
    Vektor3D windlast = new Vektor3D(0.5, 0.5, 0.5);

    Vektor3D v_RelativeWindgeschwindigkeitQuadrat = new Vektor3D(v_RelativeWindgeschwindigkeit);
    v_RelativeWindgeschwindigkeitQuadrat.mult(v_RelativeWindgeschwindigkeitQuadrat.length());

    windlast.x *= v_RelativeWindgeschwindigkeitQuadrat.x;
    windlast.y *= v_RelativeWindgeschwindigkeitQuadrat.y;
    windlast.z *= v_RelativeWindgeschwindigkeitQuadrat.z;

    if (windlast.length() > 0.0001) {
      windlast.mult(p_Luftdichte * c_p);

      double winkelLuftZuBlatt = calcWinkelToLuft(v_RelativeWindgeschwindigkeit);
      double A_Stirnfläche = size * Math.abs(Math.cos(winkelLuftZuBlatt));

      windlast.mult(A_Stirnfläche);
    }

    return windlast;
  }

  @Override
  public void update(double time) {
    int WIDTH = Display.getDisplayMode().getWidth();
    double BOTTOM = getBottom(position.x);

    // Trage verschiedene Beschleunigungen zusammen
    Vektor3D acceleration = new Vektor3D();

    acceleration.add(getWindlast());
    acceleration.add(getGravityForce(time));

    // Wenn Blatt am Boden liegt, soll sich die Geschwindigkeit auf 0 verringern und keine
    // horizontale Beschleunigung mehr einwirken
    if (position.y >= BOTTOM - 1) {
      speed.mult(0);
      acceleration.setX(0);
      acceleration.setZ(0);
    }

    speed.add(acceleration);

    // Berechne die Distanz, die sich das Blatt bewegt hat, bei der Geschwindigkeit (speed) in m/s
    Vektor3D distance = new Vektor3D(speed);
    distance.mult(time * PPM);

    position.add(distance);

    /* ******* ÜBERPRÜFE BILDRÄNDER ****** */

    int PADDING = 20;

    // Überprüfe, ob Blatt über die Bildränder hinausgehen würde
    if (position.x < -PADDING) position.setX(WIDTH + position.x + 2 * PADDING);
    if (position.x > WIDTH + PADDING) position.setX(position.x - WIDTH - 2 * PADDING);
    if (position.y > BOTTOM) position.setY(BOTTOM);
  }
}
