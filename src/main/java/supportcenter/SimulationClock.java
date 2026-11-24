package supportcenter;

/**
 * Simulated time in seconds. The simulation never reads the real system clock,
 * so every run is repeatable and timestamps can be asserted in tests.
 */
public class SimulationClock {

    private long nowSeconds;

    public long now() {
        return nowSeconds;
    }

    public void advance(long seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("Time cannot go backwards: " + seconds);
        }
        nowSeconds += seconds;
    }

    /** Formats seconds as mm:ss, e.g. 90 -> "01:30". */
    public static String format(long seconds) {
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }
}