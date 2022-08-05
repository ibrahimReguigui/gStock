package Ibrahim.SpringBoot.service;

import Ibrahim.SpringBoot.model.Agent;

import java.util.List;

public interface AgentService {

    public List<Agent> getAllAgents();

    public Agent getAgentById(Integer id);

    public void saveAgent(Agent agent);

    public void deleteAgent(Integer id);

     public List<Agent> getAgentsByStore(Long id);
    Agent readByEmail(String email);
}
