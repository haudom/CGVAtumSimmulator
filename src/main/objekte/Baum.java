package main.objekte;

import kapitel01.POGL;

import static org.lwjgl.opengl.GL11.*;

public class Baum extends BasisObjekt {
  // Liste an Blatttexturen
  static private String texture = "";


  @Override
  public void render() {

    glColor3d(0.5,0.2,0);
    glLoadIdentity();
    glTranslated(200,500,0);
    POGL.renderViereck(100,500);


    glColor3d(0.8,0.5,0);
    glLoadIdentity();
    glTranslated(200,200,0);
    POGL.renderKreis(0,0,10,200);


  }
}
