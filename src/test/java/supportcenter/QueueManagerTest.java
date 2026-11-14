package supportcenter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QueueManagerTest {

    private QueueManager queue;

    @BeforeEach
    void setUp() {
        queue = new QueueManager();
    }

    private Contact.Conversation conversation(String id) {
        return new Contact.Conversation(id, id, "Customer " + id);
    }

    @Test
    void shouldReturnEmptyWhenNoConversationIsWaiting() {
        assertTrue(queue.pollNext().isEmpty());
    }

    @Test
    void shouldReturnConversationsInArrivalOrder() {
        queue.enqueue(conversation("C1"));
        queue.enqueue(conversation("C2"));

        String first = queue.pollNext().orElseThrow().getId();
        String second = queue.pollNext().orElseThrow().getId();

        assertEquals("C1", first);
        assertEquals("C2", second);
    }

    @Test
    void shouldServePriorityQueueBeforeNormalQueue() {
        queue.enqueue(conversation("C1"));
        queue.enqueuePriority(conversation("P1"));

        String first = queue.pollNext().orElseThrow().getId();
        String second = queue.pollNext().orElseThrow().getId();

        assertEquals("P1", first);
        assertEquals("C1", second);
    }

    @Test
    void shouldCountWaitingConversationsPerQueue() {
        queue.enqueue(conversation("C1"));
        queue.enqueuePriority(conversation("P1"));

        assertEquals(1, queue.getNormalCount());
        assertEquals(1, queue.getPriorityCount());
        assertEquals(2, queue.getTotalWaiting());
    }
}
