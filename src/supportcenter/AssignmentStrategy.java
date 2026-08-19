package supportcenter;

import java.util.List;
import java.util.Optional;

public interface AssignmentStrategy {
    
    Optional<Agent> selectAgent(List<Agent> availableAgent);
}
