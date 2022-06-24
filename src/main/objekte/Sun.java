package main.objekte;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import kapitel04.Vektor2D;
import main.Updatebar;
import main.utility.DayTimer;

import static org.lwjgl.opengl.GL20.*;

public class Sun extends BasisObjekt implements Updatebar {
  private Vektor2D position = new Vektor2D(); // position.y entspricht der z-Achse auf der Ausgabe.
  private final float width;
  private final float height;
  private int shaderProgram = -1;
  private int shaderPos = -1; // "Zeiger" auf Speicherstelle in der Grafikkarte mit Sonnenpositionswert
  private int shaderTime = -1; // "Zeiger" auf Speicherstelle in der Grafikkarte mit Zeitwert

  public Sun(float width, float height) {
    this.width = width;
    this.height = height;
    prepareShader();
  }

  private void prepareShader(){
    File vertexShader = new File("shader/shader.vert");
    File fragmentShader = new File("shader/shader.frag");

    try {
      Scanner fragShaderReader = new Scanner(fragmentShader);
      Scanner vertShaderReader = new Scanner(vertexShader);

      vertShaderReader.useDelimiter("\\Z");
      fragShaderReader.useDelimiter("\\Z");

      if (!(vertShaderReader.hasNext() && fragShaderReader.hasNext())){
        System.out.println("[ERROR]: Shader konnte nicht geladen werden");
        return;
      }

      shaderProgram = glCreateProgram();

      int shaderObjectV = glCreateShader(GL_VERTEX_SHADER);
      int shaderObjectF = glCreateShader(GL_FRAGMENT_SHADER);

      glShaderSource(shaderObjectV,vertShaderReader.next());
      glCompileShader(shaderObjectV);
      System.out.println("[INFO] [VERT SHADER]:\n " + glGetShaderInfoLog(shaderObjectV, 1024));
      glAttachShader(shaderProgram, shaderObjectV);

      glShaderSource(shaderObjectF,fragShaderReader.next());
      glCompileShader(shaderObjectF);
      System.out.println("[INFO] [FRAG SHADER]:\n" + glGetShaderInfoLog(shaderObjectF, 1024));
      glAttachShader(shaderProgram, shaderObjectF);

      glLinkProgram(shaderProgram);
      glUseProgram(shaderProgram);

      shaderTime = glGetUniformLocation(shaderProgram,"u_Time");
      shaderPos = glGetUniformLocation(shaderProgram, "u_SunPos");

      int shaderScreenSize = glGetUniformLocation(shaderProgram, "u_ScreenSize");

      glUniform2f(shaderScreenSize, width, height);
    } catch (FileNotFoundException e) {
      System.out.println("[ERROR] next line:\n");
      e.printStackTrace();
    }
  }

  @Override
  public void render() {
    // Übergebe Tageszeit an Shader
    if (shaderTime != -1){
      glUniform1f(shaderTime, DayTimer.calcDayTime());
    }

    // Übergebe Sonnenposition an Shader
    if (shaderPos != -1){
      glUniform2f(shaderPos, (float) position.x, (float) position.y);
    }
  }

  @Override
  public void update(double time) {
    float dayTime = DayTimer.calcDayTime();
    position.x = height * 2 * -Math.sin(2 * Math.PI * dayTime);
    position.y = height * 2 * Math.cos(2 * Math.PI * dayTime);
  }
}
