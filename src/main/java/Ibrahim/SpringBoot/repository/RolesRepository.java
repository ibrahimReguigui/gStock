package Ibrahim.SpringBoot.repository;

import Ibrahim.SpringBoot.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Integer> {
    Roles getByRoleName(String roleName);
}
