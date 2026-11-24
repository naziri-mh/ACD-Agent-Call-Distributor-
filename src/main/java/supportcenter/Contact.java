package supportcenter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Contact {

    private final Session session;

    public Contact(Session session){
        this.session = session;
    }

    public Conversation receiveMessage(String contactId, String customerInfo, String messageContent){
        Conversation conversation = new Conversation(contactId, contactId, customerInfo);
        session.startConversation(conversation, messageContent);
        return conversation;
    }

    public static class Conversation{
        private final String id;
        private final String contactId;
        private final String customerInfo;
        
        private static final long NOT_SET = -1;
        private final List<String> message = new ArrayList<>();
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

        public void addMesaage(String contact) {
            message.add(contact);
        }

        public List<String> getMessages() {
            return Collections.unmodifiableList(message);
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
