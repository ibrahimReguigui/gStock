package Ibrahim.SpringBoot.service;

import Ibrahim.SpringBoot.model.Agent;

import java.util.List;
import java.util.Optional;

public interface AgentService {

    public List<Agent> getAllAgents();

    public Agent getAgentById(Integer id);

    public void saveAgent(Agent agent);

    public void deleteAgent(Integer id);

    public List<Agent> getAgentsByStore(Long id);

    public Agent readByEmail(String email);

    public boolean agentExist(String email);

    public void updateAgent(Agent agent);

    public Optional<Agent> findAgentByEmail(String email);

    Integer getNumberOfAgentByStore(Long id);
    Integer getAwaitingConfirmationAgents(Long id);
    List<Agent> confirmedAgentsSearch(Long id,String searchTxt);
    List<Agent> getAgentsConfirmedByStore(Long id);
    List<Agent> getAgentsAwaitingConfirmationByStore(Long id);
}
