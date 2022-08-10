package Ibrahim.SpringBoot.service;

import Ibrahim.SpringBoot.model.Role;
import Ibrahim.SpringBoot.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class RoleServiceImp implements RoleService {
    @Autowired
    private RoleRepository rRepo;

    @Override
    public List<Role> getRoles() {
        return rRepo.findAll();
    }

    @Override
    public Optional<Role> getRoleById(Integer id) {
        return rRepo.findById(id);
    }

    @Override
    public void saveRole(Role role) {
        rRepo.save(role);
    }

    @Override
    public void deleteRole(Integer id) {
        rRepo.deleteById(id);
    }
}
