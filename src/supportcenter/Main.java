package supportcenter;

import java.util.ArrayList;
import java.util.List;

public class Main{
    
    public static void main(String[] args){

        // temsilciler
        Agent a1 = new Agent("A1", Agent.Status.OFFLINE);
        Agent a2 = new Agent("A2", Agent.Status.OFFLINE);
        Agent a3 = new Agent("A3");
        Agent a4 = new Agent("A4");
        Agent a5 = new Agent("A5");
        Agent a6 = new Agent("A6");

        List<Agent> agents = new ArrayList<>();
        
        agents.add(a1);
        agents.add(a2);
        agents.add(a3);
        agents.add(a4);
        agents.add(a5);
        agents.add(a6);

        // sistem
        Reporter reporter = new Reporter();
        QueueManager queueManager = new QueueManager();
        AssignmentStrategy strategy = new FirstAvailableStrategy();
        SupportSystem supportSystem = new SupportSystem(agents, queueManager, reporter, strategy);
        reporter.reportInitialStatus(agents);

        // senaryolar
        List<ScenarioStep> scenario = new ArrayList<>();

        scenario.add(ScenarioStep.messageArrival("C1", "Ayşe Yılmaz", "Şifremi unuttum"));
        scenario.add(ScenarioStep.agentGoesOnline(a1));
        scenario.add(ScenarioStep.messageArrival("C2", "Mehmet Kaya", "Faturamı göremiyorum"));
        scenario.add(ScenarioStep.messageArrival("C3", "Elif Demir", "Hesabım kilitlendi"));
        scenario.add(ScenarioStep.agentGoesOnline(a2));
        scenario.add(ScenarioStep.messageArrival("C4", "Can Öztürk", "Kargo takibi çalışmıyor"));
        scenario.add(ScenarioStep.messageArrival("C5", "Zeynep Arslan", "İptal talebi"));
        scenario.add(ScenarioStep.messageArrival("C6", "Burak Şahin", "Ürün iade süreci"));
        scenario.add(ScenarioStep.messageArrival("C7", "Selin Aydın", "Ödeme hatası"));
        scenario.add(ScenarioStep.agentShiftEnd(a5));
        scenario.add(ScenarioStep.jobCompletion(a3));
        scenario.add(ScenarioStep.jobCompletion(a4));
        scenario.add(ScenarioStep.jobCompletion(a6));
        scenario.add(ScenarioStep.jobCompletion(a1));
        scenario.add(ScenarioStep.jobCompletion(a2));
        scenario.add(ScenarioStep.jobCompletion(a3));
        scenario.add(ScenarioStep.jobCompletion(a4));

        SimulationRunner runner = new SimulationRunner(supportSystem, scenario, reporter);
        runner.run();

        reporter.reportFinalSummary(agents);
    }
}