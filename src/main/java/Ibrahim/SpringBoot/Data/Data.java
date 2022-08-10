package Ibrahim.SpringBoot.Data;

import Ibrahim.SpringBoot.model.Role;
import Ibrahim.SpringBoot.service.RoleServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Data {
    @Autowired
    private RoleServiceImp roleServiceImp;

    @EventListener
    public void appReady(ApplicationReadyEvent event){
        roleServiceImp.saveRole(new Role(1,"MANAGER"));
        roleServiceImp.saveRole(new Role(2,"VENDOR"));
        roleServiceImp.saveRole(new Role(3,"OWNER"));
    }
}