package supportcenter;

import java.util.List;

public class SimulationRunner {

    private final SupportSystem supportSystem;
    private final List<ScenarioStep> scenario;
    private final Reporter reporter;

    public SimulationRunner(SupportSystem supportSystem, List<ScenarioStep> scenario, Reporter reporter){
        this.supportSystem = supportSystem;
        this.scenario = scenario;
        this.reporter = reporter;
    }

    public void run(){
        int stepNumber = 1;

        for(ScenarioStep step : scenario){
            reporter.reportStepHeader(stepNumber);

            switch(step.getType()){
                case MESSAGE_ARRIVAL:
                    supportSystem.submitMessage(step.getContactId(), step.getCustomerInfo(), step.getContent());
                    break;
                
                case JOB_COMPLETION:
                    supportSystem.completeJob(step.getAgent());
                    break;

                case AGENT_SHIFT_END:
                    supportSystem.endShift(step.getAgent());
                    break;

                case AGENT_GOES_ONLINE:
                    supportSystem.bringAgentOnline(step.getAgent());
                    break;
            }

            reporter.reportSystemStatus(
                supportSystem.getAvailableCount(),
                supportSystem.getBusyCount(),
                supportSystem.getOfflineCount(),
                supportSystem.getNormalQueueCount(),
                supportSystem.getPriorityQueueCount()
            );

            stepNumber++;
        }
    }
    
}
