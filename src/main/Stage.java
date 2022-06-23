package main;

import java.util.ArrayList;

import main.objekte.BasisObjekt;

public class Stage extends BasisObjekt {
  private ArrayList<BasisObjekt> objekte = new ArrayList<>();

  public double width;
  public double height;
  public double realWidth;
  public double realHeight;

  public Stage(double width, double height, double realWidth, double realHeight) {
    super();

    this.width = width;
    this.height = height;
    this.realWidth = realWidth;
    this.realHeight = realHeight;
  }

  public void render() {

  }

  public void update(double time) {

  }
}
