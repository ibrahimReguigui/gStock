package Ibrahim.SpringBoot.repository;

import Ibrahim.SpringBoot.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role getByRoleName(String roleName);
}
