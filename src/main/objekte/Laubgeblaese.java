package main.objekte;

import kapitel01.POGL;
import kapitel04.Vektor2D;
import kapitel04.Vektor3D;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Laubgeblaese extends BasisObjekt {

  public Vektor2D mousePosition() {
    return new Vektor2D(Mouse.getX(), Display.getDisplayMode().getHeight() - Mouse.getY());
  }

  @Override
  public void render() {
    POGL.renderViereckMitTexturbindung();
  }

  @Override
  public void update(double time) {
    Vektor2D mouse = mousePosition();
    this.position = new Vektor3D(mouse.x, mouse.y, 0);
  }
}
