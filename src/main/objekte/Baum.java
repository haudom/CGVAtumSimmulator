package main.objekte;

import kapitel01.POGL;

import static org.lwjgl.opengl.GL11.*;

public class Baum extends BasisObjekt {
  @Override
  public void render() {
    glColor3d(0.4,0.17,0.06);
    glLoadIdentity();
    glTranslated(200,500,0);
    POGL.renderViereck(100,500);

    glColor3d(244. / 255,159. / 255, 0. / 255);
    glLoadIdentity();
    glTranslated(200,200,0);
    POGL.renderKreis(0,0,10,200);
  }
}
