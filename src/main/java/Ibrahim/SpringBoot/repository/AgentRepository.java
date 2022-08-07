package Ibrahim.SpringBoot.repository;


import Ibrahim.SpringBoot.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Integer> {
    Agent readByEmail(String email);

    @Query("select a from Agent a where a.store.id=?1 and a.roles.roleId<>3")
    List<Agent> getAgentsByStore(Long id);

    Optional<Agent> findAgentByEmail(String email);
}
