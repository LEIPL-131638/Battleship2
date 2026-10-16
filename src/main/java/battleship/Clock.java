package battleship;

import net.time4j.PlainTime;
import net.time4j.SystemClock;
import net.time4j.tz.olson.EUROPE;

import java.util.LinkedList;

public class Clock {
    private PlainTime start;
    private LinkedList<PlainTime> times;

    public Clock() {
        this.times = new LinkedList<>();
    }

    public void startClock() {
        this.start = SystemClock.inZonalView(EUROPE.LISBON).now().getWallTime();
    }

    public void endClock() {
        PlainTime end = SystemClock.inZonalView(EUROPE.LISBON).now().getWallTime();

        int hours = end.getHour() - start.getHour();
        int minutes = end.getMinute() - start.getMinute();
        int seconds = end.getSecond() - start.getSecond();

        PlainTime duration = PlainTime.of(hours, minutes, seconds);
        times.add(duration);
    }

    public void showLastPlay() {
        PlainTime time = times.getLast();
        System.out.println("A jogada demorou " + time.getHour() + "h" + time.getMinute() + "m" + time.getSecond() + "s");
    }
}
