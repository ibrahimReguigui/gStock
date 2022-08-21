package Ibrahim.SpringBoot.service;

import Ibrahim.SpringBoot.model.Agent;
import Ibrahim.SpringBoot.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class AgentServiceImp implements AgentService {

    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    public Agent getAgentById(Integer id) {
        return agentRepository.findById(id).get();
    }

    public void saveAgent(Agent agent) {
        agent.setPwd(passwordEncoder.encode(agent.getPwd()));
        agentRepository.save(agent);
    }
    public void updateAgentWithImage(Agent agent, MultipartFile file) {

        try {
            agent.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        agentRepository.save(agent);
    }

    public void updateAgent(Agent agent) {
        agentRepository.save(agent);
    }

    public void deleteAgent(Integer id) {
        agentRepository.deleteById(id);
    }

    public List<Agent> getAgentsByStore(Long id) {
        return agentRepository.getAgentsByStore(id);
    }

    ;

    public Agent readByEmail(String email) {
        return agentRepository.readByEmail(email);
    }

    ;

    public Optional<Agent> findAgentByEmail(String email) {
        return agentRepository.findAgentByEmail(email);
    }

    ;

    public boolean agentExist(String email) {
        return findAgentByEmail(email).isPresent();
    }

    public Integer getNumberOfAgentByStore(Long id) {
        return agentRepository.getNumberOfAgentByStore(id);
    }

    ;

    public Integer getAwaitingConfirmationAgents(Long id) {
        return agentRepository.getAwaitingConfirmationAgents(id);
    }

    ;
    public List<Agent> confirmedAgentsSearch(Long id,String searchTxt){
        return agentRepository.confirmedAgentsSearch(id,searchTxt);
    };
    public List<Agent> getAgentsConfirmedByStore(Long id){
        return agentRepository.getAgentsConfirmedByStore(id);
    };
    public List<Agent> getAgentsAwaitingConfirmationByStore(Long id){
        return agentRepository.getAgentsAwaitingConfirmationByStore(id);
    };

}
