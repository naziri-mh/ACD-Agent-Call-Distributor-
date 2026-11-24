package supportcenter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class ReporterTest {

    @Test
    void shouldFormatSingleId() {
        assertEquals("A1", Reporter.formatList(List.of("A1")));
    }

    @Test
    void shouldJoinIdsWithCommaAndVe() {
        assertEquals("A1, A2 ve A3", Reporter.formatList(List.of("A1", "A2", "A3")));
    }
}
