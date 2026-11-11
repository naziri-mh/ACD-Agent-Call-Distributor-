package supportcenter;


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

        @Override
        public String toString(){
            return "Conversation{" + id + " contact = " + contactId + "}";
        }
    }
    
}
