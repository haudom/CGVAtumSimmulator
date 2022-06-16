package main.objekte;

import kapitel01.POGL;
import kapitel04.Vektor2D;

public class Blatt extends BasisObjekt {
  // Liste an Blatttexturen
  static private String[] texturen = new String[]{
      "",
  };

  // Laubgebläse Instanz von der die Parameter für die Physik abgelesen werden
  public Laubgeblaese laubgeblaese;

  public Vektor2D velocity;
  public double mass = 0;

  public Blatt(Laubgeblaese laubgeblaese, Vektor2D position) {
    this(laubgeblaese, position, new Vektor2D(0, 0));
  }

  public Blatt(Laubgeblaese laubgeblaese, Vektor2D position, Vektor2D velocity) {
    super(position);

    this.laubgeblaese = laubgeblaese;
    this.velocity = velocity;
  }

  @Override
  public void render() {
    POGL.renderViereckMitTexturbindung();
  }

  @Override
  public void update(double time) {
    super.update(time);
  }
}
