package main.utility;

import kapitel04.Vektor2D;

public class VektorUtil {
  static public double getWinkel(Vektor2D a, Vektor2D b) {
    return Math.toDegrees(Math.atan2(
        a.y - b.y,
        a.x - b.x
    ));
  }
}
