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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import kapitel04.Vektor3D;
import main.objekte.BasisObjekt;
import main.objekte.Blatt;
import main.objekte.Laubgeblaese;
import org.lwjgl.opengl.Display;

public class AutumnSimulator extends LWJGLBasisFenster {
  private Wind wind;
  private Laubgeblaese laubgeblaese;
  private List<BasisObjekt> objekte = new ArrayList<BasisObjekt>();
  private long lastTime = System.nanoTime();

  public AutumnSimulator(String title, int width, int height) {
    super(title, width, height);

    wind = new Wind(
        30,
        new Vektor3D(-900, -50, 0),
        new Vektor3D(1000, 10, 0)
    );
    laubgeblaese = new Laubgeblaese(2000, 60, 0.5);

    // Render erst die Blätter
    erzeugeBlaetter(100);

    // Render Laubgebläse zum Schluss
    objekte.add(laubgeblaese);
  }

  private void erzeugeBlaetter(int anz) {
    Random rand = ThreadLocalRandom.current();

    for (int i = 0; i < anz; i++) {
      objekte.add(new Blatt(
          laubgeblaese,
          wind,
          new Vektor3D(rand.nextInt(WIDTH), rand.nextInt(HEIGHT), 0),
          new Vektor3D(rand.nextFloat() * 5 - 2.5, rand.nextFloat() - 0.5, 0)
      ));
    }
  }

  public void syncFPS() {
    long now = System.nanoTime();
    try {
      Thread.sleep(Math.max(
          1,
          // rechne die aktuelle Frame-Differenz in ms aus und zieh sie vom 1000/60 ms Zeitbudget ab
          (long) (1000 / 60) - (long) ((now - lastTime) / 1e6)
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
      double diff = (now - lastTime) / 1e9;
      lastTime = now;

      // Zeichne eine Hintergrundfarbe
      glClearColor(0.95f, 0.95f, 0.95f, 1.0f);
      glClear(GL_COLOR_BUFFER_BIT);

      glMatrixMode(GL_PROJECTION);
      glLoadIdentity();
      glOrtho(0, WIDTH, HEIGHT, 0, 0, 1);
      glMatrixMode(GL_MODELVIEW);
      glDisable(GL_DEPTH_TEST);

      for (BasisObjekt objekt : objekte) {
        wind.update(diff);

        objekt.update(diff);
        objekt.render();
      }

      Display.update();
    }
  }

  public static void main(String[] args) {
    new AutumnSimulator("Autumn Simulator", 1280, 800).start();
  }
}
