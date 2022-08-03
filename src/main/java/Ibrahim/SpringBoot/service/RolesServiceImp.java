package Ibrahim.SpringBoot.service;

import Ibrahim.SpringBoot.model.Roles;
import Ibrahim.SpringBoot.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class RolesServiceImp implements RolesService {
    @Autowired
    private RolesRepository rRepo;

    @Override
    public List<Roles> getRoles() {
        return rRepo.findAll();
    }

    @Override
    public Optional<Roles> getRoleById(Integer id) {
        return rRepo.findById(id);
    }

    @Override
    public void saveRole(Roles role) {
        rRepo.save(role);
    }

    @Override
    public void deleteRole(Integer id) {
        rRepo.deleteById(id);
    }
}
