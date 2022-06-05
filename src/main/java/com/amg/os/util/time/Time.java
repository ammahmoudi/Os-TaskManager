package util.time;

import java.time.LocalTime;

public class Time {

    public static int getNowMillis() {
        return LocalTime.now().getNano() / 1000000;
    }
}