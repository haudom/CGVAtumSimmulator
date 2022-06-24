package main;

import static java.lang.System.exit;
import static org.lwjgl.opengl.GL11.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import kapitel04.Vektor3D;
import main.objekte.*;
import org.lwjgl.opengl.Display;

import javax.swing.*;

public class AutumnSimulator extends LWJGLBasisFenster {
  private Wind wind;
  private Laubgeblaese laubgeblaese;
  private Boden bottom;
  private ArrayList<BasisObjekt> objekte = new ArrayList<>();
  private long lastTime;
  private boolean useOBJ = false;

  private JFrame frame;

  public AutumnSimulator(String title, int width, int height) {
    super(title, width, height);

    frame = new JFrame();
    frame.setTitle(title);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Canvas c = new Canvas();
    frame.add(c);
    frame.setSize(new Dimension(width, height));
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);

    Object[] options = {"OBJ Datei", "OpenGL"};
    int useOBJorGL = JOptionPane.showOptionDialog(
        frame,"Blatt Lademethode","Autumn Simulator",
        JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null,
        options,options[0]
    );
    if (useOBJorGL < 0){
      exit(0);
    }

    useOBJ = useOBJorGL == 0;

    frame.setVisible(true);

    initDisplay(c);

    wind = new Wind(
        new Vektor3D(-5., -5., 0.),
        new Vektor3D(8., 5., 0.)
    );
    laubgeblaese = new Laubgeblaese(
        70.,
        100.,
        0.5
    );
    bottom = new Boden(width, height);

    // Render erst die Blätter
    objekte.add(new Sun(width, height));
    objekte.add(new Background(width, height));
    objekte.add(bottom);
    erzeugeBlaetter(300);
    objekte.add(new Baum());
    // Render Laubgebläse zum Schluss
    objekte.add(laubgeblaese);

    // Setze lastTime nach allen anspruchsvollen Operationen, damit Zeitdifferenz nicht riesig ist
    // beim ersten Frame
    lastTime = System.nanoTime();
  }

  private void erzeugeBlaetter(int anz) {
    Random rand = ThreadLocalRandom.current();

    for (int i = 0; i < anz; i++) {
      objekte.add(new Blatt(
          laubgeblaese,
          wind,
          bottom,
          // Zufällige Position
          new Vektor3D(rand.nextInt(width), rand.nextInt(height), 0),
          // Zufällige Startgeschwindigkeit
          new Vektor3D(rand.nextFloat() * 1 - 0.5, rand.nextFloat() * 0.5 - 0.25, 0),
          useOBJ
      ));
    }
  }

  @Override
  public void renderLoop() {
    // Haupt-loop. Solange die UserIn das Fenster nicht schließen möchte, fahre mit der Game-loop
    // fort.
    while (!Display.isCloseRequested()) {
      Display.sync(60);

      long now = System.nanoTime();
      double diff = (now - lastTime) / 1e9;
      lastTime = now;

      System.out.println("fps: " + Math.round(1.0 / diff));

      // Zeichne eine Hintergrundfarbe

      glClearColor(0.95f, 0.95f, 0.95f, 1.0f);
      glClear(GL_COLOR_BUFFER_BIT);

      glMatrixMode(GL_PROJECTION);
      glLoadIdentity();
      glOrtho(0, width, height, 0, 0, 100);
      glMatrixMode(GL_MODELVIEW);

      wind.update(diff);

      for (BasisObjekt objekt : objekte) {
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
