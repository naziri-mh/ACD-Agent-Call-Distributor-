package supportcenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Session {

    private final QueueManager queueManager;
    private final Reporter reporter;
    private final Map<String, Contact.Conversation> activeConversations;
    private final Map<String, List<String>> conversationHistories = new HashMap<>();

    public Session(QueueManager queueManager, Reporter reporter){
        this.queueManager = queueManager;
        this.reporter = reporter;
        this.activeConversations = new HashMap<>();
    }

    public void startConversation(Contact.Conversation conversation, String messageContent){
        activeConversations.put(conversation.getId(), conversation);
        conversationHistories.put(conversation.getId(), new ArrayList<>());
        conversationHistories.get(conversation.getId()).add(messageContent);
        queueManager.enqueue(conversation);
        reporter.reportNewConversation(conversation); 
    }

    public void finishConversation(String conversationId){
        activeConversations.remove(conversationId);
        conversationHistories.remove(conversationId);
    }

    public void transferConversation(String conversationId){
        Contact.Conversation conversation = activeConversations.get(conversationId);
        if(conversation != null){
            queueManager.enqueuePriority(conversation);
        }
    }

    public Contact.Conversation getActiveConversation(String conversation){
        return activeConversations.get(conversation);
    }

    public List<String> getConversationHistory(String conversationId){
    return conversationHistories.get(conversationId);
    }

    public int getWaitingCount(){
        return queueManager.getTotalWaiting();
    }
}
