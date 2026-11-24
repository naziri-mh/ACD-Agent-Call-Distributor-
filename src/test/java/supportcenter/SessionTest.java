package supportcenter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SessionTest {

    private QueueManager queueManager;
    private SimulationClock clock;
    private Session session;

    @BeforeEach
    void setUp() {
        queueManager = new QueueManager();
        clock = new SimulationClock();
        session = new Session(queueManager, new Reporter(), clock);
    }

    @Test
    void shouldGiveEachConversationAUniqueId() {
        Contact.Conversation first = session.handleIncomingMessage("C1", "Ayşe", "Hello");
        Contact.Conversation second = session.handleIncomingMessage("C2", "Mehmet", "Hi");

        assertEquals("CONV-001", first.getId());
        assertEquals("CONV-002", second.getId());
    }

    @Test
    void shouldAddMessageToActiveConversationInsteadOfOpeningNewOne() {
        Contact.Conversation first = session.handleIncomingMessage("C1", "Ayşe", "Hello");

        Contact.Conversation second = session.handleIncomingMessage("C1", "Ayşe", "Are you there?");

        assertSame(first, second);
        assertEquals(2, first.getMessages().size());
        assertEquals(1, queueManager.getTotalWaiting());
        assertEquals(1, session.getActiveCount());
    }

    @Test
    void shouldOpenNewConversationAfterPreviousOneIsFinished() {
        Contact.Conversation first = session.handleIncomingMessage("C1", "Ayşe", "Hello");
        session.finishConversation(first);

        Contact.Conversation second = session.handleIncomingMessage("C1", "Ayşe", "New question");

        assertNotEquals(first.getId(), second.getId());
        assertEquals(1, second.getMessages().size());
    }

    @Test
    void shouldStampCreatedAndCompletedTimes() {
        clock.advance(60);
        Contact.Conversation conversation = session.handleIncomingMessage("C1", "Ayşe", "Hello");
        clock.advance(120);

        session.finishConversation(conversation);

        assertEquals(60, conversation.getCreatedAt());
        assertEquals(180, conversation.getCompletedAt());
        assertNull(session.getActiveConversation("C1"));
    }

    @Test
    void shouldMoveActiveConversationToPriorityQueueOnTransfer() {
        Contact.Conversation conversation = session.handleIncomingMessage("C1", "Ayşe", "Hello");
        queueManager.pollNext();

        session.transferConversation(conversation);

        assertEquals(1, queueManager.getPriorityCount());
    }

    @Test
    void shouldIgnoreTransferOfFinishedConversation() {
        Contact.Conversation conversation = session.handleIncomingMessage("C1", "Ayşe", "Hello");
        queueManager.pollNext();
        session.finishConversation(conversation);

        session.transferConversation(conversation);

        assertEquals(0, queueManager.getPriorityCount());
    }
}
