package supportcenter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class QueueManager {
    
    private final Deque<Contact.Conversation> priorityQueue = new ArrayDeque<>();
    private final Deque<Contact.Conversation> normalQueue = new ArrayDeque<>();

    // yeni gelen sohbeti kuyruğun sonuna ekler.
    public void enqueue(Contact.Conversation conversation){
        normalQueue.addLast(conversation);
    }

    public void enqueuePriority(Contact.Conversation conversation){
        priorityQueue.addLast(conversation);
    }

    // Kuyruk başından sohbeti çıkarır.
    public Optional<Contact.Conversation> pollNext(){
        if(!priorityQueue.isEmpty()){
            return Optional.of(priorityQueue.pollFirst());
        }

        if(!normalQueue.isEmpty()){
            return Optional.of(normalQueue.pollFirst());
        }
        return Optional.empty();
    }

    public int getTotalWaiting(){
        return priorityQueue.size() + normalQueue.size();
    }

    public int getPriorityCount(){
        return priorityQueue.size();
    }

    public int getNormalCount(){
        return normalQueue.size();
    }
}
