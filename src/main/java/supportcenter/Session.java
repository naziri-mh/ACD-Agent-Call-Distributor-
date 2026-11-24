package supportcenter;

import java.util.HashMap;
import java.util.Map;

/**
 * Keeps track of active conversations. A customer (contactId) can have at most one
 * active conversation at a time; each conversation gets a unique id (CONV-001, CONV-002 ...).
 */
public class Session {

    private final QueueManager queueManager;
    private final Reporter reporter;
    private final SimulationClock clock;
    private final Map<String, Contact.Conversation> activeByContact = new HashMap<>();
    private int conversationSequence;

    public Session(QueueManager queueManager, Reporter reporter, SimulationClock clock) {
        this.queueManager = queueManager;
        this.reporter = reporter;
        this.clock = clock;
    }

    public Session(QueueManager queueManager, Reporter reporter) {
        this(queueManager, reporter, new SimulationClock());
    }

    public Contact.Conversation handleIncomingMessage(String contactId, String customerInfo, String content) {
        Contact.Conversation active = activeByContact.get(contactId);
        if (active != null) {
            active.addMessage(content);
            reporter.reportMessageAppended(active);
            return active;
        }
        Contact.Conversation conversation = new Contact.Conversation(nextConversationId(), contactId, customerInfo);
        conversation.addMessage(content);
        conversation.setCreatedAt(clock.now());
        activeByContact.put(contactId, conversation);
        queueManager.enqueue(conversation);
        reporter.reportNewConversation(conversation);
        return conversation;
    }

    public void finishConversation(Contact.Conversation conversation) {
        conversation.setCompletedAt(clock.now());
        activeByContact.remove(conversation.getContactId());
    }

    public void transferConversation(Contact.Conversation conversation) {
        if (activeByContact.containsKey(conversation.getContactId())) {
            queueManager.enqueuePriority(conversation);
        }
    }

    public Contact.Conversation getActiveConversation(String contactId) {
        return activeByContact.get(contactId);
    }

    public int getActiveCount() {
        return activeByContact.size();
    }

    private String nextConversationId() {
        conversationSequence++;
        return String.format("CONV-%03d", conversationSequence);
    }
}
