package main;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import main.objekte.BasisObjekt;
import main.objekte.Blatt;
import main.objekte.Laubgeblaese;
import org.lwjgl.opengl.Display;

import kapitel04.Vektor2D;

public class AutumnSimulator extends LWJGLBasisFenster {
  private Wind wind;
  private Laubgeblaese laubgeblaese;
  private ObjektManager blaetter;
  private long last = System.nanoTime();

  public AutumnSimulator(String title, int width, int height) {
    super(title, width, height);

    initDisplay();

    wind = new Wind(10);
    laubgeblaese = new Laubgeblaese();
    blaetter = ObjektManager.getExemplar();

    erzeugeBlaetter(100);
  }

  private void erzeugeBlaetter(int anz) {
    Random rand = ThreadLocalRandom.current();

    for (int i = 0; i < anz; i++) {
      blaetter.add(new Blatt(
          laubgeblaese,
          new Vektor2D(rand.nextInt(WIDTH), rand.nextInt(HEIGHT)),
          new Vektor2D(rand.nextFloat()*20, 0)
      ));
    }
  }

  public void syncFPS() {
    long now = System.nanoTime();
    try {
      Thread.sleep(Math.max(
          1,
          // rechne die aktuelle Frame-Differenz in ms aus und zieh sie vom 1000/60 ms Zeitbudget ab
          (long) (1000 / 60) - (long) ((now - last) / 1e6)
      ));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void renderLoop() {
    glEnable(GL_DEPTH_TEST);

    // Haupt-loop. Solange die UserIn das Fenster nicht schließen möchte, fahre mit der Game-loop
    // fort.
    while (!Display.isCloseRequested()) {
      syncFPS();

      long now = System.nanoTime();
      double diff = (now - last) / 1e9;
      last = now;

      // Zeichne eine Hintergrundfarbe
      glClearColor(0.95f, 0.95f, 0.95f, 1.0f);
      glClear(GL_COLOR_BUFFER_BIT);

      glMatrixMode(GL_PROJECTION);
      glLoadIdentity();
      glOrtho(0, WIDTH, HEIGHT, 0, 0, 1);
      glMatrixMode(GL_MODELVIEW);
      glDisable(GL_DEPTH_TEST);

      // Render erst die Blätter
      for (int i = 0; i < blaetter.size(); i++) {
        BasisObjekt blatt = blaetter.get(i);

        blatt.update(diff);
        blatt.render();
      }

      Display.update();
    }
  }

  public static void main(String[] args) {
    new AutumnSimulator("Autumn Simulator", 800, 600).start();
  }
}
