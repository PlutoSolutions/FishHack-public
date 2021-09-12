package cat.cattyn.fishhack.api.util;

public final class TimerUtil {

    private long time;

    public TimerUtil() {
        time = -1;
    }

    public boolean passed(double ms) {
        return System.currentTimeMillis() - this.time >= ms;
    }

    public boolean passedTicks(double ticks) {
        return System.currentTimeMillis() - this.time >= ticks / 20 * 1000;
    }

    public void reset() {
        this.time = System.currentTimeMillis();
    }

}