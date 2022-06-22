package main.utility;

public class DayTimer {
    public static float calcDayTime(){
        return (float)(System.currentTimeMillis()%30000)/30000f;
    }
}
