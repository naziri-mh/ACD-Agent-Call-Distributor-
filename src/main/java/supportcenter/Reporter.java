package supportcenter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Writes simulation events through java.util.logging.
 * The pause after each event is configurable (0 = no pause, used in tests and CI).
 */
public class Reporter {

    private static final Logger LOG = Logger.getLogger(Reporter.class.getName());
    private static final String AGENT = "Temsilci ";

    private final long eventDelayMillis;

    public Reporter(long eventDelayMillis) {
        this.eventDelayMillis = Math.max(0, eventDelayMillis);
    }

    public Reporter() {
        this(0);
    }

    private void event(Supplier<String> message) {
        LOG.info(message);
        pause();
    }

    private void pause() {
        if (eventDelayMillis == 0) {
            return;
        }
        try {
            Thread.sleep(eventDelayMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void reportDispatchAssign(Contact.Conversation conversation, Agent agent) {
        event(() -> "Dispatch: sohbet " + conversation.getId() + " -> temsilci " + agent.getId() + " atandı.");
    }

    public void reportNotAvailableAgent() {
        event(() -> "Dispatch: müsait temsilci yok.");
    }

    public void reportQueueEmpty() {
        event(() -> "Dispatch: kuyruk boş.");
    }

    public void reportJobCompleted(Agent agent, Contact.Conversation conversation) {
        event(() -> AGENT + agent.getId() + " işini bitirdi: " + conversation.getId()
                + " (" + conversation.getMessages().size() + " mesaj)");
    }

    public void reportShiftEnded(Agent agent, String conversationId) {
        event(() -> AGENT + agent.getId() + " mesaisini bitirdi -> sohbet " + conversationId
                + " öncelikli kuyruğa aktarıldı.");
    }

    public void reportShiftEndedNoConversation(Agent agent) {
        event(() -> AGENT + agent.getId() + " mesaisini bitirdi, yarım kalan sohbet yok.");
    }

    public void reportAgentOnline(Agent agent) {
        event(() -> AGENT + agent.getId() + " tekrar müsait duruma geçti.");
    }

    public void reportNewConversation(Contact.Conversation conversation) {
        event(() -> "Session: yeni sohbet açıldı -> " + conversation);
    }

    public void reportMessageAppended(Contact.Conversation conversation) {
        event(() -> "Session: " + conversation.getContactId() + " için açık sohbet var, mesaj "
                + conversation.getId() + " sohbetine eklendi (" + conversation.getMessages().size() + " mesaj).");
    }

    public void reportFinalSummary(List<Agent> agents) {
        LOG.info("");
        LOG.info("--- Simülasyon Özeti ---");
        for (Agent agent : agents) {
            LOG.info(() -> AGENT + agent.getId() + ": " + agent.getAssignedCount() + " atandı, "
                    + agent.getCompletedCount() + " tamamladı.");
        }
    }

    public void reportStepHeader(int stepNumber, long timeSeconds) {
        LOG.info("");
        LOG.info(() -> "--- " + stepNumber + ". Adım (t=" + SimulationClock.format(timeSeconds) + ") ---");
    }

    public void reportInitialStatus(List<Agent> agents) {
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
            parts.add(AGENT + formatList(offlineIds) + " masa başında değil");
        }
        if (!availableIds.isEmpty()) {
            parts.add("temsilci " + formatList(availableIds) + " müsait");
        }
        if (!busyIds.isEmpty()) {
            parts.add("temsilci " + formatList(busyIds) + " meşgul");
        }

        LOG.info("");
        LOG.info("--- Başlangıç Durumu ---");
        LOG.info(() -> String.join(", ", parts));
    }

    static String formatList(List<String> ids) {
        if (ids.size() == 1) {
            return ids.get(0);
        }
        String allButLast = String.join(", ", ids.subList(0, ids.size() - 1));
        String last = ids.get(ids.size() - 1);
        return allButLast + " ve " + last;
    }

    public void reportSystemStatus(int availableCount, int busyCount, int offlineCount,
                                   int normalQueueCount, int priorityQueueCount) {
        LOG.info(() -> "Durum -> Müsait: " + availableCount
                + " | Meşgul: " + busyCount
                + " | Offline: " + offlineCount
                + " | Normal kuyruk: " + normalQueueCount
                + " | Öncelikli kuyruk: " + priorityQueueCount);
    }
}