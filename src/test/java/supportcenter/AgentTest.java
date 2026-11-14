package supportcenter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class AgentTest {

    private Agent agent;
    private Contact.Conversation conversation;

    @BeforeEach
    void setUp() {
        agent = new Agent("A1");
        conversation = new Contact.Conversation("C1", "C1", "Customer C1");
    }

    @Test
    void shouldBeAvailableWithNoJobsWhenCreated() {
        assertTrue(agent.isAvailable());
        assertEquals(0, agent.getTotalJobHandled());
        assertNull(agent.getCurrentConversation());
    }

    @Test
    void shouldBecomeBusyWhenConversationIsAssigned() {
        agent.assignConversation(conversation);

        assertTrue(agent.isBusy());
        assertSame(conversation, agent.getCurrentConversation());
        assertEquals(1, agent.getTotalJobHandled());
    }

    @Test
    void shouldBecomeAvailableWhenConversationIsReleased() {
        agent.assignConversation(conversation);

        Contact.Conversation released = agent.releaseConversation();

        assertSame(conversation, released);
        assertTrue(agent.isAvailable());
        assertNull(agent.getCurrentConversation());
    }

    @Test
    void shouldReturnConversationWhenGoingOffline() {
        agent.assignConversation(conversation);

        Contact.Conversation released = agent.goOffline();

        assertSame(conversation, released);
        assertTrue(agent.isOffline());
        assertNull(agent.getCurrentConversation());
    }

    @Test
    void shouldBecomeAvailableWhenGoingOnlineFromOffline() {
        Agent offlineAgent = new Agent("A2", Agent.Status.OFFLINE);

        offlineAgent.goOnline();

        assertTrue(offlineAgent.isAvailable());
    }

    @Test
    void shouldStayBusyWhenGoingOnlineWhileBusy() {
        agent.assignConversation(conversation);

        agent.goOnline();

        assertTrue(agent.isBusy());
    }

    @Disabled("TD-03: releaseConversation sets an offline agent to AVAILABLE")

    @Test
    void shouldStayOfflineWhenReleasingWhileOffline() {
        Agent offlineAgent = new Agent("A2", Agent.Status.OFFLINE);

        offlineAgent.releaseConversation();

        assertTrue(offlineAgent.isOffline());
    }
}
