package main.objekte;

import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glTranslated;

import kapitel01.POGL;
import kapitel04.Vektor3D;
import main.Wind;

public class Blatt extends BasisObjekt {
  // Liste an Blatttexturen
  static private String[] texturen = new String[]{
      "",
  };

  // Laubgebläse Instanz von der die Parameter für die Physik abgelesen werden
  public Laubgeblaese laubgeblaese;
  public Wind wind;

  public Vektor3D velocity;
  public double mass = 0;

  public Blatt(Laubgeblaese laubgeblaese, Wind wind, Vektor3D position) {
    this(laubgeblaese, wind, position, new Vektor3D(0, 0, 0));
  }

  public Blatt(Laubgeblaese laubgeblaese, Wind wind, Vektor3D position, Vektor3D velocity) {
    super(position);

    this.wind = wind;
    this.laubgeblaese = laubgeblaese;
    this.velocity = velocity;
  }

  @Override
  public void render() {
    glLoadIdentity();
    glTranslated(position.x, position.y, position.z);

    glColor4f(0.05f, 0.39f, 0.51f, 1.0f);

    POGL.renderViereck();
  }

  @Override
  public void update(double time) {
    super.update(time);
  }
}
