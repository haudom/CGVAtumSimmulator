package main.utility;

import kapitel04.LineareAlgebra;
import kapitel04.Vektor2D;
import kapitel04.Vektor3D;

public class VektorUtil {
  static public double getWinkel(Vektor2D a, Vektor2D b) {
    return Math.toDegrees(Math.atan2(
        a.y - b.y,
        a.x - b.x
    ));
  }

  static public Vektor3D rotateVektor(Vektor3D vec, Vektor3D unit, double theta) {
    double tmp = (unit.x * vec.x + unit.y * vec.y + unit.z * vec.z) * (1d - Math.cos(theta));

    double xPrime = unit.x * tmp
        + vec.x * Math.cos(theta)
        + (-unit.z * vec.y + unit.y * vec.z) * Math.sin(theta);
    double yPrime = unit.y * tmp
        + vec.y * Math.cos(theta)
        + (unit.z * vec.x - unit.x * vec.z) * Math.sin(theta);
    double zPrime = unit.z * tmp
        + vec.z * Math.cos(theta)
        + (-unit.y * vec.x + unit.x * vec.y) * Math.sin(theta);

    return new Vektor3D(
        xPrime,
        yPrime,
        zPrime
    );
  }
}
