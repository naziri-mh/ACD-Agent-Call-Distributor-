package supportcenter;

import java.util.ArrayList;
import java.util.List;

public class Reporter {

    private void puse(){
        try {
            Thread.sleep(500);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void reportDispatchAssign(Contact.Conversation conversation, Agent agent){
        System.out.println("Dispatch: Sohbet " + conversation.getId() + "-> temsilci " + agent.getId() + "'a atadnı.");
        puse();
    }

    public void reportNotAvailableAgent(){
        System.out.println("Dispatch: müsait temsilci yok");
        puse();
    }

    public void reportQueueEmpty(){
        System.out.println("Dispatch: Kuyruk boş");
        puse();
    }

    public void reportJobCompleted(Agent agent, String conversationId){
        System.out.println("Temsilci " + agent.getId() + " işini bitiridi: " + conversationId);
        puse();
    }

    public void reportShiftEnded(Agent agent, String conversationId){
        System.out.println("Temsilci " + agent.getId() + "mesaisini bitirdi -> sohbet " + conversationId + "öncelikli kuyruğa transfer edildi.");
        puse();
    }

    public void reportShiftEndedNoConversation(Agent agent){
        System.out.println("Temsilci " + agent.getId() + "mesaisini bitirdi, yarım kalan sohbet yok.");
        puse();
    }

    public void reportAgentOnline(Agent agent){
        System.out.println("Temsilci " + agent.getId() + "tekrar müsait duruma geçti.");
        puse();
    }

    public void reportNewConversation(Contact.Conversation conversation){
        System.out.println("Session yeni sohbet alındı -> " + conversation);
        puse();
    }

    public void reportFinalSummary(List<Agent> agents){
        System.out.println();
        System.out.println("--- Simülasyon Özeti ---");
        for (Agent agent: agents){
            System.out.println("Temsilci " + agent.getId() + ": " + agent.getTotalJobHandled() + " iş aldı.");
        }
    }

    public void reportStepHeader(int stepNumber){
        System.out.println();
        System.out.println("--- " + stepNumber + ". Adım ---");
    }

    public void reportInitialStatus(List<Agent> agents){
    List<String> offlineIds = new ArrayList<>();
    List<String> availableIds = new ArrayList<>();
    List<String> busyIds = new ArrayList<>();

    for (Agent agent : agents) {
        if (agent.isOffline()) {
            offlineIds.add(agent.getId());
        } else if (agent.isAvailable()) {
            availableIds.add(agent.getId());
        } else {
            busyIds.add(agent.getId());
        }
    }

    List<String> parts = new ArrayList<>();
    if (!offlineIds.isEmpty()) {
        parts.add("Temsilci " + formatList(offlineIds) + " masa başında değil");
    }
    if (!availableIds.isEmpty()) {
        parts.add("temsilci " + formatList(availableIds) + " müsait");
    }
    if (!busyIds.isEmpty()) {
        parts.add("temsilci " + formatList(busyIds) + " meşgul");
    }

    System.out.println("\n---Başlangıç Durumunda---");
    System.out.println(String.join(", ", parts));
    }

    private String formatList(List<String> ids){
        if (ids.size() == 1) {
            return ids.get(0);
        }
        String allButLast = String.join(", ", ids.subList(0, ids.size() - 1));
        String last = ids.get(ids.size() - 1);
        return allButLast + " ve " + last;
    }

    public void reportSystemStatus(int availableCount, int busyCount, int offlineCount, int normalQueueCount, int priorityQueueCount){
        System.out.println("Durum -> Müsait: " + availableCount
            + " | Meşgül: " + busyCount
            + " | Offline: " + offlineCount
            + " | Normal kuyruk bekleyen: " + normalQueueCount
            + " | Öncelikli kuyruk bekleyen: " + priorityQueueCount);
    }

    
}
