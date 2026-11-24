package supportcenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Contact {

    private final Session session;

    public Contact(Session session){
        this.session = session;
    }

    /**
     * Routes an incoming message. If the customer already has an active conversation,
     * the message is added to it; otherwise a new conversation is started.
     */
    public Conversation receiveMessage(String contactId, String customerInfo, String messageContent){
        return session.handleIncomingMessage(contactId, customerInfo, messageContent);
    }

    public static class Conversation {
        private static final long NOT_SET = -1;

        private final String id;
        private final String contactId;
        private final String customerInfo;
        private final List<String> messages = new ArrayList<>();
        private long createdAt = NOT_SET;
        private long assignedAt = NOT_SET;
        private long completedAt = NOT_SET;

        public Conversation(String id, String contactId, String customerInfo){
            this.id = id;
            this.contactId = contactId;
            this.customerInfo = customerInfo;
        }

        public String getId(){
            return id;
        }

        public String getContactId(){
            return contactId;
        }

        public String getCustomerInfo(){
            return customerInfo;
        }

        public void addMessage(String content) {
            messages.add(content);
        }

        public List<String> getMessages() {
            return Collections.unmodifiableList(messages);
        }

        public long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }

        public long getAssignedAt() {
            return assignedAt;
        }

        public void setAssignedAt(long assignedAt) {
            this.assignedAt = assignedAt;
        }

        public long getCompletedAt() {
            return completedAt;
        }

        public void setCompletedAt(long completedAt) {
            this.completedAt = completedAt;
        }

        @Override
        public String toString(){
            return "Conversation{" + id + " contact = " + contactId + "}";
        }
    }
}
