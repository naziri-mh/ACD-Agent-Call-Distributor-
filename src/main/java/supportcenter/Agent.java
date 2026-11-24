package supportcenter;

public class Agent {
    public enum Status {
        OFFLINE,
        AVAILABLE,
        BUSY
    }

    private final String id;
    private Status status;
    private int assignedCount;
    private int completedCount;
    private Contact.Conversation currentConversation;
    private long availableSince;                                         // fifio sırası için

    public Agent(String id, Status baslangicDurumu){
        this.id = id;
        this.status = baslangicDurumu;
        this.currentConversation = null;
        this.availableSince = -1;
    }

    public Agent(String id){
        this(id, Status.AVAILABLE);
    }

    public String getId(){
        return id;
    }

    public Status getStatus(){
        return status;
    }

    public boolean isAvailable(){
        return status == Status.AVAILABLE;
    }

    public boolean isOffline(){
        return status == Status.OFFLINE;
    }

    public boolean isBusy(){
        return status == Status.BUSY;
    }

    public void setStatus(Status newStatus){
        this.status = newStatus;
    }

    public long getAvailableSince(){                // fifo müsaitlik durumu için
        return availableSince;
    }

    public void markAvailableNow(long sequence){    // fifo müsaitlik durumu için
        this.availableSince = sequence;
    }

    public Contact.Conversation getCurrentConversation(){
        return currentConversation;
    }

    public void assignConversation(Contact.Conversation conversation){
        this.currentConversation = conversation;
        this.status = Status.BUSY;
        this.assignedCount++;
    }

    public int getAssignedCount(){
        return assignedCount;
    }

    public int getCompletedCount(){
        return completedCount;
    }

    public int getTotalJobHandled(){
        return assignedCount;
    }

    public Contact.Conversation releaseConversation(){
        Contact.Conversation released = currentConversation;
        if (released != null) {
            completedCount++;
        }
        this.currentConversation = null;
        if (status != Status.OFFLINE) {
            this.status = Status.AVAILABLE;
        }
        return released;
    }

    public Contact.Conversation goOffline(){
        Contact.Conversation released = currentConversation;
        this.currentConversation = null;
        this.status = Status.OFFLINE;
        return released;
    }

    // Offline olan bir temsilci tekrar online olup müsait duruma geçer.
    public void goOnline(){
        if(this.status == Status.OFFLINE){
            this.status = Status.AVAILABLE;
        }
    }

    @Override
    public String toString(){
        return "Agent{" + id + ", " + status + "}";
    }

}
