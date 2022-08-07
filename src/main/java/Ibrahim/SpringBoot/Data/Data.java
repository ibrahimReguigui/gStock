package Ibrahim.SpringBoot.Data;

import Ibrahim.SpringBoot.model.Roles;
import Ibrahim.SpringBoot.service.RolesServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Data {
    @Autowired
    private RolesServiceImp rolesServiceImp;

    @EventListener
    public void appReady(ApplicationReadyEvent event){
        rolesServiceImp.saveRole(new Roles(1,"MANAGER"));
        rolesServiceImp.saveRole(new Roles(2,"VENDOR"));
        rolesServiceImp.saveRole(new Roles(3,"OWNER"));
    }
}