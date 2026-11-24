package supportcenter;

import java.util.List;

public class SimulationRunner {

    /** Simulated seconds between two scenario steps. */
    public static final long DEFAULT_SECONDS_PER_STEP = 30;

    private final SupportSystem supportSystem;
    private final List<ScenarioStep> scenario;
    private final Reporter reporter;
    private final SimulationClock clock;
    private final long secondsPerStep;

    public SimulationRunner(SupportSystem supportSystem, List<ScenarioStep> scenario, Reporter reporter,
                            SimulationClock clock, long secondsPerStep) {
        this.supportSystem = supportSystem;
        this.scenario = scenario;
        this.reporter = reporter;
        this.clock = clock;
        this.secondsPerStep = secondsPerStep;
    }

    public void run() {
        int stepNumber = 1;
        for (ScenarioStep step : scenario) {
            reporter.reportStepHeader(stepNumber, clock.now());

            switch (step.getType()) {
                case MESSAGE_ARRIVAL ->
                        supportSystem.submitMessage(step.getContactId(), step.getCustomerInfo(), step.getContent());
                case JOB_COMPLETION -> supportSystem.completeJob(step.getAgent());
                case AGENT_SHIFT_END -> supportSystem.endShift(step.getAgent());
                case AGENT_GOES_ONLINE -> supportSystem.bringAgentOnline(step.getAgent());
            }

            reporter.reportSystemStatus(
                    supportSystem.getAvailableCount(),
                    supportSystem.getBusyCount(),
                    supportSystem.getOfflineCount(),
                    supportSystem.getNormalQueueCount(),
                    supportSystem.getPriorityQueueCount());

            clock.advance(secondsPerStep);
            stepNumber++;
        }
    }
}
