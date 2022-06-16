

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

import org.lwjgl.opengl.Display;

import kapitel04.Vektor2D;

public class AutumnSimulator extends LWJGLBasisFenster {
  private ObjektManager elemente;
  private long last = System.nanoTime();

  public AutumnSimulator(String title, int width, int height) {
    super(title, width, height);
    initDisplay();
    elemente = ObjektManager.getExemplar();
    erzeugeAgenten(10);
  }

  private void erzeugeAgenten(int anz) {
    Random rand = ThreadLocalRandom.current();

    for (int i = 0; i < anz; i++) {
      Agent agent = new Agent(
          new Vektor2D(rand.nextInt(WIDTH), rand.nextInt(HEIGHT)),
          new Vektor2D(rand.nextFloat()*20, 0), 10, 1f, 1f, 1f);
      agent.setVerhalten(new VerhaltenAgent(agent));
      agent.setObjektManager(elemente);
      elemente.registrierePartikel(agent);
    }
  }

  @Override
  public void renderLoop() {
    glEnable(GL_DEPTH_TEST);

    while (!Display.isCloseRequested()) {
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      long now = System.nanoTime();
      double diff = (now - last) / 1e9;
      last = now;

      glClearColor(0.95f, 0.95f, 0.95f, 1.0f);
      glClear(GL_COLOR_BUFFER_BIT);

      glMatrixMode(GL_PROJECTION);
      glLoadIdentity();
      glOrtho(0, WIDTH, HEIGHT, 0, 0, 1);
      glMatrixMode(GL_MODELVIEW);
      glDisable(GL_DEPTH_TEST);

      for (int i = 1; i <= elemente.getAgentSize(); i++) {
        BasisObjekt aktAgent = elemente.getAgent(i);

        aktAgent.render();
        aktAgent.update(diff);
      }

      Display.update();
    }
  }

  public static void main(String[] args) {
    new AutumnSimulator("Autumn Simulator", 800, 450).start();
  }
}
