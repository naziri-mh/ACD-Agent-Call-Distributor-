package supportcenter;

public class ScenarioStep {

    public enum Type{
        MESSAGE_ARRIVAL,
        JOB_COMPLETION,
        AGENT_SHIFT_END,
        AGENT_GOES_ONLINE
    }

    private final Type type;
    private final Agent agent;
    private final String contactId;
    private final String customerInfo;
    private final String content;

    private ScenarioStep(Type type, Agent agent, String contactId, String customerInfo, String content){
        this.type = type;
        this.agent = agent;
        this.contactId = contactId;
        this.customerInfo = customerInfo;
        this.content = content;
    }

    public static ScenarioStep messageArrival(String contactId, String customerInfo, String content){
        return new ScenarioStep(Type.MESSAGE_ARRIVAL, null, contactId, customerInfo, content);
    }

    public static ScenarioStep jobCompletion(Agent agent) {
        return new ScenarioStep(Type.JOB_COMPLETION, agent, null, null, null);
    }

    public static ScenarioStep agentShiftEnd(Agent agent){
        return new ScenarioStep(Type.AGENT_SHIFT_END, agent, null, null, null);
    }

    public static ScenarioStep agentGoesOnline(Agent agent){
        return new ScenarioStep(Type.AGENT_GOES_ONLINE, agent, null, null, null);
    }
    
    public Type getType() {
        return type;
    }

    public Agent getAgent(){
        return agent;
    }

    public String getContactId(){
        return contactId;
    }

    public String getCustomerInfo(){
        return customerInfo;
    }

    public String getContent(){
        return content;
    }
}
