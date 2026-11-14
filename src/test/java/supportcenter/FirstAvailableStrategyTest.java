package supportcenter;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class FirstAvailableStrategyTest {

    private final FirstAvailableStrategy strategy = new FirstAvailableStrategy();

    @Test
    void shouldReturnEmptyWhenNoAgentIsAvailable() {
        assertTrue(strategy.selectAgent(List.of()).isEmpty());
    }

    @Test
    void shouldReturnFirstAgentInList() {
        Agent first = new Agent("A1");
        Agent second = new Agent("A2");

        Agent selected = strategy.selectAgent(List.of(first, second)).orElseThrow();

        assertSame(first, selected);
    }
}
