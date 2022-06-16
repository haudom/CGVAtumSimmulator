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
   public int WIDTH, HEIGHT;
   public String TITLE;

   public LWJGLBasisFenster() {
      this("LWJGLBasisFenster", 800, 450);
   }

   public LWJGLBasisFenster(int width, int height) {
      this("LWJGLBasisFenster", width, height);
   }

   public LWJGLBasisFenster(String title, int width, int height) {
      WIDTH = width;
      HEIGHT = height;
      TITLE = title;
   }

   public void initDisplay(Canvas c) {
      try {
         Display.setParent(c);
         Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
         Display.setTitle(TITLE);
         Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
         Display.setLocation((d.width-WIDTH)/2, (d.height-HEIGHT)/2);
         Display.create();
      } catch (LWJGLException e) {
         e.printStackTrace();
      }
   }

   public void initDisplay() {
      try {
         Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
         Display.setTitle(TITLE);
         Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
         Display.setLocation((d.width-WIDTH)/2, (d.height-HEIGHT)/2);
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
