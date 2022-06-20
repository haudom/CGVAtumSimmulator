package main;

import static java.lang.System.exit;
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

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import kapitel04.Vektor3D;
import main.objekte.BasisObjekt;
import main.objekte.Baum;
import main.objekte.Blatt;
import main.objekte.Laubgeblaese;
import org.lwjgl.opengl.Display;

import javax.swing.*;
import javax.swing.text.html.Option;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glTranslated;

public class AutumnSimulator extends LWJGLBasisFenster {
  private Wind wind;
  private Laubgeblaese laubgeblaese;
  private List<BasisObjekt> objekte = new ArrayList<BasisObjekt>();
  private long lastTime = System.nanoTime();

  public AutumnSimulator(String title, int width, int height) {
    super(title, width, height);
    int userOBJorGL;
    JFrame f = new JFrame();
    f.setTitle(title);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Canvas c = new Canvas();
    f.add(c);
    f.setBounds(0, 0, width, height);
    f.setLocationRelativeTo(null);
    Object[] options = {"OBJ Datei",
            "OpenGL"};

    userOBJorGL = JOptionPane.showOptionDialog(f,"Blatt Lademethode","Autumn Simulator",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null,options,options[0]);
    System.out.println(userOBJorGL);
    if (userOBJorGL < 0){
      exit(0);
    }

    f.setVisible(true);

    initDisplay(c);

    wind = new Wind(
        new Vektor3D(5, -50, 0),
        new Vektor3D(5000, 40, 0)
    );
    laubgeblaese = new Laubgeblaese(
        500,
        100,
        0.5
    );

    // Render erst die Blätter
    erzeugeBlaetter(100);
    objekte.add(new Baum());
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
      glOrtho(0, WIDTH, HEIGHT, 0, -0, 1000);
      glMatrixMode(GL_MODELVIEW);







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
