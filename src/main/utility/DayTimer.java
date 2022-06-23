package main.utility;

public class DayTimer {
  public static float calcDayTime() {
    return (float) (System.currentTimeMillis() % 60000) / 60000f;
  }
}
