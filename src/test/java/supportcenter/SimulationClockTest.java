package supportcenter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SimulationClockTest {

    @Test
    void shouldStartAtZero() {
        assertEquals(0, new SimulationClock().now());
    }

    @Test
    void shouldAdvanceBySeconds() {
        SimulationClock clock = new SimulationClock();

        clock.advance(30);
        clock.advance(45);

        assertEquals(75, clock.now());
    }

    @Test
    void shouldRejectNegativeAdvance() {
        SimulationClock clock = new SimulationClock();

        assertThrows(IllegalArgumentException.class, () -> clock.advance(-1));
    }

    @Test
    void shouldFormatAsMinutesAndSeconds() {
        assertEquals("01:30", SimulationClock.format(90));
        assertEquals("00:05", SimulationClock.format(5));
    }
}