package supportcenter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SupportSystemTest {

    private Agent a1;
    private Agent a2;
    private SimulationClock clock;
    private SupportSystem system;

    @BeforeEach
    void setUp() {
        a1 = new Agent("A1");
        a2 = new Agent("A2");
        clock = new SimulationClock();
        system = new SupportSystem(List.of(a1, a2), new QueueManager(), new Reporter(),
                new FirstAvailableStrategy(), clock);
    }

    @Test
    void shouldAssignToLongestAvailableAgentFirst() {
        system.submitMessage("C1", "Ayşe", "Hello");

        assertTrue(a1.isBusy());
        assertTrue(a2.isAvailable());
    }

    @Test
    void shouldNotAssignSecondMessageOfSameCustomerToAnotherAgent() {
        system.submitMessage("C1", "Ayşe", "Hello");
        system.submitMessage("C1", "Ayşe", "Still waiting");

        assertTrue(a2.isAvailable());
        assertEquals(1, a1.getAssignedCount());
        assertEquals(2, a1.getCurrentConversation().getMessages().size());
    }

    @Test
    void shouldQueueWhenNoAgentIsAvailableAndAssignOnCompletion() {
        system.submitMessage("C1", "Ayşe", "Hello");
        system.submitMessage("C2", "Mehmet", "Hi");
        system.submitMessage("C3", "Elif", "Help");
        assertEquals(1, system.getNormalQueueCount());

        system.completeJob(a1);

        assertEquals(0, system.getNormalQueueCount());
        assertEquals("C3", a1.getCurrentConversation().getContactId());
        assertEquals(1, a1.getCompletedCount());
    }

    @Test
    void shouldTransferConversationToPriorityQueueAtShiftEnd() {
        system.submitMessage("C1", "Ayşe", "Hello");
        system.submitMessage("C2", "Mehmet", "Hi");
        Contact.Conversation inProgress = a1.getCurrentConversation();

        system.endShift(a1);
        system.completeJob(a2);

        assertTrue(a1.isOffline());
        assertSame(inProgress, a2.getCurrentConversation());
        assertEquals(1, a1.getAssignedCount());
        assertEquals(0, a1.getCompletedCount());
    }

    @Test
    void shouldStampAssignedTimeFromClock() {
        clock.advance(30);
        system.submitMessage("C1", "Ayşe", "Hello");

        assertEquals(30, a1.getCurrentConversation().getAssignedAt());
    }

    @Test
    void shouldCountAgentsByStatus() {
        system.submitMessage("C1", "Ayşe", "Hello");
        system.endShift(a2);

        assertEquals(0, system.getAvailableCount());
        assertEquals(1, system.getBusyCount());
        assertEquals(1, system.getOfflineCount());
    }
}