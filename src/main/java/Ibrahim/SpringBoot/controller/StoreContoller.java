package Ibrahim.SpringBoot.controller;


import Ibrahim.SpringBoot.model.Agent;
import Ibrahim.SpringBoot.model.Store;
import Ibrahim.SpringBoot.service.AgentServiceImp;
import Ibrahim.SpringBoot.service.StoreServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Slf4j
@Controller
public class StoreContoller {

    @Autowired
    private StoreServiceImp storeServiceImp;
    @Autowired
    private AgentServiceImp agentServiceImp;


    @PostMapping("/saveStore")
    public String saveProduct(@Valid @ModelAttribute Store newS, Errors errors, @ModelAttribute Agent newA) {
        if (errors.hasErrors()) {
            log.error("form error :" + errors.toString());
            return "store-Registration-Form.html";
        }
        storeServiceImp.saveStore(newS);
        Store store = storeServiceImp.getStoreByStoreNumber(newS.getStoreNumber());
        newA.setStore(store);
        newA.setStatus("Accepted");
        agentServiceImp.saveAgent(newA);
        return "redirect:/login?register=true";
    }

}
