package main.objekte;

import kapitel04.Vektor2D;
import main.Updatebar;
import main.utility.DayTimer;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL40.glUniform2d;

public class Sun extends BasisObjekt implements Updatebar {
  Vektor2D position = new Vektor2D(); // position.y entspricht der z-Achse auf der Ausgabe.
  int shaderPos; // "Zeiger" auf Speicherstelle in der Grafikkarte mit Sonnenpositionswert
  final float WIDTH;
  final float HEIGHT;

  public Sun(int shaderProgram, float width, float height) {
    WIDTH = width;
    HEIGHT = height;
    shaderPos = glGetUniformLocation(shaderProgram, "u_SunPos");
  }

  @Override
  public void render() {
    glUniform2f(shaderPos, (float) position.x, (float) position.y);
  }

  @Override
  public void update(double time) {
    float dayTime = DayTimer.calcDayTime();
    position.x = HEIGHT * 2 * -Math.sin(2 * Math.PI * dayTime);
    position.y = HEIGHT * 2 * Math.cos(2 * Math.PI * dayTime);
  }
}
