package main.objekte;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import kapitel01.Model;
import kapitel01.POGL;
import kapitel04.Vektor3D;
import main.Wind;
import main.utility.NumberUtil;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;

public class Blatt extends BasisObjekt {
  private final double PPM = 200; // Pixel per Meter
  private Vektor3D leafWindSpeed = new Vektor3D();


  // Liste an Blatttexturen
  static private float[][] colors = {
      {0.875f, 0.324f, 0.051f},
      {0.965f, 0.699f, 0.1328f},
  };

  // Laubgebläse Instanz von der die Parameter für die Physik abgelesen werden
  public Laubgeblaese laubgeblaese;
  public Wind wind;

  public double mass = 0.001; // gewicht in kg
  public double size = 0.0004; // Größe in m²
  private double rotationSensitivity = 1; //Stärke, mit der das Blatt auf eine Kraft mit einer Rotatin reagiert
  Model model;

  public float[] color;

  public Blatt(Laubgeblaese laubgeblaese, Wind wind, Vektor3D position, Vektor3D velocity) {
    super(position);

    this.wind = wind;
    this.laubgeblaese = laubgeblaese;
    this.speed = velocity;
    rotation.x = 0;
    rotation.y = 1;
    rotation.z = 0;

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

      glTranslated(position.x, position.y, position.z );

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
     // TODO GL-Einheiten auf Meter anpassen? Ne zu aufwändig

    double g = 9.81;
    // xf = x0 + v0 * t + 0.5 * g * t * t;
    Vektor3D gravity = new Vektor3D(0.0, g * time , 0.0);

    this.speed.add(gravity);

    if (position.y == BOTTOM) {
      this.speed.mult(0);
    }
  }

  public void addBlaserWindSpeed() {
    Vektor3D blaeser = new Vektor3D(laubgeblaese.getForceAt(position));
//    System.out.println("bläser: " + blaeser);
    leafWindSpeed.add(blaeser);
  }

  private void addGlobalWindSpeed() {
    Vektor3D windAcceleration = new Vektor3D(wind.velocity);

    //double t = 1.0 - Math.pow(position.y / getBottom(), 2);
    //windAcceleration.mult(t);

    //TODO

    leafWindSpeed.add(windAcceleration);
  }
  private void calcRelativeWindSpeed(){
    //addGlobalWindSpeed();
    addBlaserWindSpeed();
    leafWindSpeed.sub(speed);
  }

  private void applyAirResistance(double time){
    final double Luftdruck = 1.204;
    final double cw = 1;
    Vektor3D luftwiederstandbeschläunigung;
    double winkelLuftBlatt =
            Math.acos(
                    (leafWindSpeed.x * rotation.x) + (leafWindSpeed.y * rotation.y) + (leafWindSpeed.z * rotation.z)
                    / (leafWindSpeed.length() * rotation.length())
            );
    double strinFläche = size * Math.sin(winkelLuftBlatt);

    luftwiederstandbeschläunigung = new Vektor3D(leafWindSpeed);
    luftwiederstandbeschläunigung.x *= luftwiederstandbeschläunigung.x;
    luftwiederstandbeschläunigung.y *= luftwiederstandbeschläunigung.y;
    luftwiederstandbeschläunigung.z *= luftwiederstandbeschläunigung.z;

    luftwiederstandbeschläunigung.mult(Luftdruck*strinFläche*time*1/mass);

    luftwiederstandbeschläunigung.x *= Math.signum(leafWindSpeed.x);
    luftwiederstandbeschläunigung.y *= Math.signum(leafWindSpeed.y);
    luftwiederstandbeschläunigung.z *= Math.signum(leafWindSpeed.z);

    speed.add(luftwiederstandbeschläunigung);


  }



  @Override
  public void update(double time) {
    int WIDTH = Display.getDisplayMode().getWidth();
    double BOTTOM = getBottom();

    leafWindSpeed.mult(0);



    calcRelativeWindSpeed();
    applyAirResistance(time);
    applyGravity(time);

    Vektor3D distance = new Vektor3D(speed);
    distance.mult(time);

    position.add(distance);
//    System.out.println("Speed:" + speed);
//    System.out.println("pos: " + position);
//    System.out.println("windSpeed: " + leafWindSpeed );

    /* ******* ÜBERPRÜFE BILDRÄNDER ****** */

    int PADDING = 100;

    // Überprüfe, ob Blatt über die Bildränder hinausgehen würde
    if (position.x < -PADDING) position.setX(WIDTH + position.x + 2*PADDING);
    if (position.x > WIDTH + PADDING) position.setX(position.x - WIDTH - 2*PADDING);
    if (position.y > BOTTOM) position.setY(BOTTOM);
  }
}
