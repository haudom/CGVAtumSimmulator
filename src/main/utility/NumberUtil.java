package main.utility;

public class NumberUtil {
  static public double lerp(double a, double b, double x) {
    return x * (b - a) + a;
  }
  static public float lerp(float a, float b, float x) {
    return x * (b - a) + a;
  }
}
