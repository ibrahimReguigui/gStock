package Ibrahim.SpringBoot.controller;


import Ibrahim.SpringBoot.model.Store;
import Ibrahim.SpringBoot.service.StoreServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StoreContoller {

    @Autowired
    private StoreServiceImp sServ;

    @GetMapping("/storeRegistrationForm")
    public ModelAndView storeRegistrationForm() {
        ModelAndView mav = new ModelAndView("store-Registration-Form");
        mav.addObject("store", new Store());
        return mav;
    }
    @PostMapping("/saveStore")
    public String saveProduct(@ModelAttribute Store newS) {
        sServ.saveStore(newS);
        return "redirect:/connection";
    }

}
