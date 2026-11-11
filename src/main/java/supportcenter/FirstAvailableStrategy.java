package supportcenter;

import java.util.List;
import java.util.Optional;

public class FirstAvailableStrategy implements AssignmentStrategy{
    
    @Override
    public Optional<Agent> selectAgent(List<Agent> availableAgents){
        if(availableAgents.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(availableAgents.get(0));
    }
}
