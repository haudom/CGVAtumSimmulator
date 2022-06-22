package main.objekte;

import kapitel04.Vektor3D;
import main.Updatebar;
import main.utility.DayTimer;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;

public class Sun extends BasisObjekt implements Updatebar {

    Vektor3D position; //position.y entspricht der z-Achse auf der Ausgabe.
    int shaderPos;

    public Sun(int shaderProgram){
        shaderPos = glGetUniformLocation(shaderProgram,"u_Time");
    }

    @Override
    public void render() {

    }
    @Override
    public void update(double time) {
        float dayTime = DayTimer.calcDayTime();
        position.x = Math.sin(2 *Math.PI * dayTime);
        position.y = -Math.cos(2 *Math.PI * dayTime);
    }
}
