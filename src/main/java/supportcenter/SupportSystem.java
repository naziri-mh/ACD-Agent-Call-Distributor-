package supportcenter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SupportSystem {
    
    private final List<Agent> agents;
    private final QueueManager queueManager;
    private final Reporter reporter;
    private final AssignmentStrategy assignmentStrategy;
    private final Session session;
    private final Contact contact;
    private long availablitySequence;              // sürekli artacak sayç

    public SupportSystem(List<Agent> agents, QueueManager queueManager, Reporter reporter, AssignmentStrategy assignmentStrategy){
        this.agents = agents;
        this.queueManager = queueManager;
        this.reporter = reporter;
        this.assignmentStrategy = assignmentStrategy;
        this.availablitySequence = 0;

        Session session = new Session(queueManager, reporter);
        this.contact = new Contact(session);
        this.session = session;

        for(Agent agent : agents){
            if(agent.isAvailable()){
                markAvailable(agent);
            }
        }
    }

    private void markAvailable(Agent agent){
        agent.markAvailableNow(availablitySequence++);
    }

    // kuyruktaki sohbeti müsait temsilci ile birleştirir.
    public DispatchResult dispatchNext(){
        Optional<Agent> availableAgent = findFirstAvailableAgent();

        if(!availableAgent.isPresent()){
            reporter.reportNotAvailableAgent();
            return DispatchResult.noAvailableAgent();
        }

        Optional <Contact.Conversation> nextConversation = queueManager.pollNext();

        if(!nextConversation.isPresent()){
            reporter.reportQueueEmpty();
            return DispatchResult.queueEmpty();
        }

        Agent agent = availableAgent.get();
        Contact.Conversation conversation = nextConversation.get();

        agent.assignConversation(conversation);

        reporter.reportDispatchAssign(conversation, agent);
        return DispatchResult.assigned(agent, conversation);
    }

    public void submitMessage(String contactId, String customerInfo, String content){
        contact.receiveMessage(contactId, customerInfo, content);
        dispatchNext();
    }

    public void completeJob(Agent agent){
        Contact.Conversation finished = agent.releaseConversation();
        if(finished!=null){
            session.finishConversation(finished.getId());
            reporter.reportJobCompleted(agent, finished.getId());
        }
        markAvailable(agent);
        dispatchNext();
    }

    public void endShift(Agent agent){
        Contact.Conversation inProgress = agent.goOffline();
        if(inProgress != null){
            session.transferConversation(inProgress.getId());
            reporter.reportShiftEnded(agent, inProgress.getId());
        }
        else {
            reporter.reportShiftEndedNoConversation(agent);
        }
        dispatchNext();
    }

    public void bringAgentOnline(Agent agent){
        agent.goOnline();
        markAvailable(agent);
        reporter.reportAgentOnline(agent);
        dispatchNext();
    }

    // Temsilci offline olur, elinde sohbet varsa geri döner, yoksa null döner.
    private Optional<Agent> findFirstAvailableAgent(){
        List<Agent> availableAgents = new ArrayList<>();
        for(Agent agent : agents){
            if(agent.isAvailable()){
                availableAgents.add(agent);
            }
        }
        if(availableAgents.isEmpty()){
            return Optional.empty();
        }

        availableAgents.sort(Comparator.comparingLong(Agent::getAvailableSince));
        return assignmentStrategy.selectAgent(availableAgents);
    }

    public int getAvailableCount(){
        int count = 0;
        for(Agent agent : agents){
            if(agent.isAvailable()){
                count++;
            }
        }
        return count;
    }

    public int getOfflineCount(){
        int count = 0;
        for(Agent agent : agents){
            if(agent.isOffline()){
                count++;
            }
        }
        return count;
    }

    public int getBusyCount(){
        return agents.size() - getAvailableCount() - getOfflineCount();
    }

    public int getNormalQueueCount(){
        return queueManager.getNormalCount();
    }

    public int getPriorityQueueCount(){
        return queueManager.getPriorityCount();
    }

    public static class DispatchResult{

        public enum Status {
            ASSIGNED,
            NO_AVAILABLE_AGENT,
            QUEUE_EMPTY
        }

        private final Status status;
        private final Agent agent;
        private Contact.Conversation conversation;

        private DispatchResult(Status status, Agent agent, Contact.Conversation conversation){
            this.status = status;
            this.agent = agent;
            this.conversation = conversation;
        }

        public static DispatchResult assigned(Agent agent, Contact.Conversation conversation){
            return new DispatchResult(Status.ASSIGNED, agent, conversation);
        }

        public static DispatchResult noAvailableAgent(){
            return new DispatchResult(Status.NO_AVAILABLE_AGENT, null, null);
        }

        public static DispatchResult queueEmpty(){
            return new DispatchResult(Status.QUEUE_EMPTY, null, null);
        }

        public Status getStatus(){
            return status;
        }

        public Agent getAgent(){
            return agent;
        }

        public Contact.Conversation getConversation(){
            return conversation;
        }
    }
}


