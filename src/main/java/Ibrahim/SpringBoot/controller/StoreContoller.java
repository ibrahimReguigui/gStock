package Ibrahim.SpringBoot.controller;


import Ibrahim.SpringBoot.model.Agent;
import Ibrahim.SpringBoot.model.AgentStatus;
import Ibrahim.SpringBoot.model.Store;
import Ibrahim.SpringBoot.service.AgentServiceImp;
import Ibrahim.SpringBoot.service.StoreServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
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
    public String saveProduct(@Valid @ModelAttribute Store newS, Errors errors, @ModelAttribute Agent newA, BindingResult bindingResult) {
        if (storeServiceImp.numberExist(newS.getStoreNumber())) {
            FieldError err=new FieldError("store", "storeNumber", "Number already in use !!");
            bindingResult.addError(err);
            System.out.println("Number already in use !!");
        }
        if (errors.hasErrors()||bindingResult.hasErrors()) {
            log.error("form error :" + errors.toString());
            return "store-Registration-Form";
        }
        storeServiceImp.saveStore(newS);
        Store store = storeServiceImp.getStoreByStoreNumber(newS.getStoreNumber());
        newA.setStore(store);
        newA.setStatus(AgentStatus.CONFIRMED);
        agentServiceImp.saveAgent(newA);
        return "redirect:/login?register=true";
    }

}
