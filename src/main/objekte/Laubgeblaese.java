package main.objekte;

import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex2f;

import kapitel01.POGL;
import kapitel04.LineareAlgebra;
import kapitel04.Vektor2D;
import kapitel04.Vektor3D;
import main.utility.VektorUtil;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Laubgeblaese extends BasisObjekt {
  public double windStaerke = 0;
  public double maxWindStaerke;
  public double windWinkel;
  public double windKante;

  /**
   * Erstellt ein neues Laubgebläse als Objekt, mit Laubgebläse-Logik.
   *
   * @param maxWindStaerke Setzt die maximale Stärke, die das Gebläse blasen kann.
   * @param windWinkel     Setzt den Abstrahlwinkel, in dem Wind empfangen wird.
   * @param windKante      Setzt die Weiche der Kante, ab der laut windWinkel kein Wind mehr
   *                       empfangen wird von 0.0 bis 1.0.
   */
  public Laubgeblaese(double maxWindStaerke, double windWinkel, double windKante) {
    this.maxWindStaerke = maxWindStaerke;
    this.windWinkel = windWinkel;
    this.windKante = windKante;
  }

  public Vektor2D mousePosition() {
    return new Vektor2D(Mouse.getX(), Display.getDisplayMode().getHeight() - Mouse.getY());
  }

  public boolean isMouseButtonDown() {
    return Mouse.isButtonDown(0); // Hole Button-Status für die linke Maustaste
  }

  public void renderKreis(float cx, float cy, float radius, int num_segments) {
    glBegin(GL_TRIANGLE_FAN);
    glVertex2f(cx, cy);

    for (int ii = 0; ii < num_segments; ii++)   {
      float theta = 2.0f * (float) Math.PI * (float) ii / (float) num_segments; //get the current angle
      float x = radius * (float) Math.cos(theta); //calculate the x component
      float y = radius * (float) Math.sin(theta); //calculate the y component
      glVertex2f(x + cx, y + cy);//output vertex
    }

    glVertex2f(cx + radius, cy);//output vertex

    glEnd();
  }

  @Override
  public void render() {
    glLoadIdentity();
    glTranslated(position.x, position.y, position.z);
    glRotatef((float) rotation.z, 0, 0, 1); // Rotiere um die z-Achse

    glColor3d(14. / 255, 19. / 255, 34. / 255);

    POGL.renderViereck(-30, -10, 0, 60, 20);

    glColor3d(86. / 255, 185. / 255, 238. / 255);

    renderKreis(35, -13, 25, 16);
  }

  @Override
  public void update(double diff) {
    int HEIGHT = Display.getDisplayMode().getHeight();
    int WIDTH = Display.getDisplayMode().getWidth();

    Vektor2D mouse = mousePosition();
    position = new Vektor3D(mouse.x, mouse.y, 0);
    // drehe den Laubbläser so, dass er immer ins Bildzentrum zeigt
    rotation.setZ(
        VektorUtil.getWinkel(
            new Vektor2D(position.x, position.y),
            new Vektor2D((float) WIDTH / 2, (float) HEIGHT / 2)
        )
    );

    // Wenn der Mausbutton gedrückt wird, schalte Gebläse an, wenn nicht schalte aus
    if (isMouseButtonDown()) {
      windStaerke = maxWindStaerke;
    } else {
      windStaerke = 0;
    }
  }

  /**
   * Berechne die Stärke des Laubgebläses an einer bestimmten Position.
   */
  public Vektor3D getSpeedAt(Vektor3D blatt) {
    int WIDTH = Display.getDisplayMode().getWidth();

    // Berechne den Winkel von Bläser zu Blatt
    double winkelBlaeserBlatt = VektorUtil.getWinkel(
        new Vektor2D(blatt.x, blatt.y),
        new Vektor2D(position.x, position.y)
    );

    // Ziehe den Winkel des Bläsers ab um die Orientierung von Blatt zu Auspusterohr des Bläsers
    // zu berechnen. Wenn das Blatt direkt in der Schusslinie ist, ist der Winkel 180 Grad.
    // Deswegen ziehen wir nochmal 180 Grad ab
    double schussbahnWinkel = Math.floorMod((int) (winkelBlaeserBlatt - rotation.z), 360) - 180.0;

    double halbWindWinkel = windWinkel / 2;

    // Wenn das Blatt in Schussbahn liegt, fahre fort
    if (Math.abs(schussbahnWinkel) <= halbWindWinkel) {
      Vektor2D richtungsVektor2D = LineareAlgebra.rotate(new Vektor2D(-windStaerke, 0), rotation.z);
      Vektor3D velocity = new Vektor3D(richtungsVektor2D.x, richtungsVektor2D.y, 0);

      Vektor3D distanzVektor = new Vektor3D(blatt);
      distanzVektor.sub(position);

      // Je näher der Bläser am Blatt, desto (quadratisch) höher die Kraft
      double distanzStaerke = 1.0 - Math.sqrt(distanzVektor.length() / WIDTH);

      // Reduziere die Kraft am Rand
      double maxKante = halbWindWinkel;
      double minKante = halbWindWinkel * windKante;
      double randStaerke = Math.min(1.0, 1.0 - (Math.abs(schussbahnWinkel) - minKante) / (maxKante - minKante));

      velocity.mult(distanzStaerke * randStaerke);

      return velocity;
    } else {
      return new Vektor3D();
    }
  }
}
