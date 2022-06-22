package main.objekte;

import kapitel01.POGL;

import static org.lwjgl.opengl.GL11.*;

public class Background extends BasisObjekt{
    private final int WIDTH, HEIGHT;
    public Background(int width, int height){

        WIDTH = width;
        HEIGHT = height;
    }
    @Override
    public void render() {

        glColor3d(0.5,0.2,0);
        glLoadIdentity();
        glTranslated(WIDTH/2d,HEIGHT/2d,0);
        POGL.renderViereck(WIDTH,HEIGHT, -10);


    }
}
