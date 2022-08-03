package Ibrahim.SpringBoot.service;

import Ibrahim.SpringBoot.model.Roles;

import java.util.List;
import java.util.Optional;

public interface RolesService {
    public List<Roles> getRoles();

    public Optional<Roles> getRoleById(Integer id);

    public void saveRole(Roles role);

    public void deleteRole(Integer id);
}
