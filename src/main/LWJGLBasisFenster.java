package main;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.awt.*;

/*
 * Beschreibung:
 * Als Erweiterung kommt hinzu, dass wir ein Canvas-Objekt zum Zeichnen
 * übergeben können. Dazu müssen wir den init-Teil selbst aktiv im Konstruktor
 * ausführen und start() auf den renderLoop() und das Abmelden reduzieren.
 */
public abstract class LWJGLBasisFenster {

  public int width, height;
  public String title;

  public LWJGLBasisFenster(String title, int width, int height) {
    this.width = width;
    this.height = height;
    this.title = title;
  }

  public void initDisplay() {
    try {
      Display.setDisplayMode(new DisplayMode(width, height));
      Display.setTitle(title);
      Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
      Display.setLocation((d.width - width) / 2, (d.height - height) / 2);
      Display.create();
    } catch (LWJGLException e) {
      e.printStackTrace();
    }
  }

  public void initDisplay(Canvas c) {
    try {
      Display.setParent(c);
      Display.setDisplayMode(new DisplayMode(width, height));
      Display.setTitle(title);
      Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
      Display.setLocation((d.width - width) / 2, (d.height - height) / 2);
      Display.create();
    } catch (LWJGLException e) {
      e.printStackTrace();
    }
  }

  public abstract void renderLoop();

  public void start() {
    renderLoop();
    Display.destroy();
    System.exit(0);
  }
}
