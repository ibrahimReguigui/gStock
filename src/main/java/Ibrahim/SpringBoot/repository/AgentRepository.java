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

    @Query("select a from Agent a where a.store.id=?1 and a.role.roleId<>3")
    List<Agent> getAgentsByStore(Long id);
    @Query("select a from Agent a where a.store.id=?1 and a.role.roleId<>3 and a.status=1")
    List<Agent> getAgentsConfirmedByStore(Long id);
    @Query("select a from Agent a where a.store.id=?1 and a.role.roleId<>3 and a.status=0")
    List<Agent> getAgentsAwaitingConfirmationByStore(Long id);
    @Query( "select a from Agent a where a.store.id=?1 and a.role.roleId<>3 and a.status=1 and ( a.email like %?2% " +
            "or a.mobileNumber like %?2% or a.name like %?2% or a.email like %?2% or a.role.roleName like %?2% )  "
            )
    List<Agent> confirmedAgentsSearch(Long id,String searchTxt);
    @Query( "select a from Agent a where a.store.id=?1 and a.role.roleId<>3 and a.status=0 and ( a.email like %?2% " +
            "or a.mobileNumber like %?2% or a.name like %?2% or a.email like %?2% or a.role.roleName like %?2% )  "
    )
    List<Agent> awaitingConfirmationAgentsSearch(Long id,String searchTxt);

    Optional<Agent> findAgentByEmail(String email);

    @Query("select count(a) from Agent a where a.store.id=?1 and a.role.roleId<>3 and a.status=1")
    Integer getNumberOfAgentByStore(Long id);
    @Query("select count(a) from Agent a where a.store.id=?1 and a.role.roleId<>3 and a.status=0")
    Integer getAwaitingConfirmationAgents(Long id);
}
