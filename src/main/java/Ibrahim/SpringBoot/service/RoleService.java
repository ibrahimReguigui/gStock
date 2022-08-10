package Ibrahim.SpringBoot.service;

import Ibrahim.SpringBoot.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    public List<Role> getRoles();

    public Optional<Role> getRoleById(Integer id);

    public void saveRole(Role role);

    public void deleteRole(Integer id);
}
