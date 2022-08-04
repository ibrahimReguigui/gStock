package Ibrahim.SpringBoot.controller;

import Ibrahim.SpringBoot.model.Agent;
import Ibrahim.SpringBoot.model.Product;
import Ibrahim.SpringBoot.repository.AgentRepository;
import Ibrahim.SpringBoot.service.AgentServiceImp;
import Ibrahim.SpringBoot.service.RolesServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
public class AgentContoller {
    @Autowired
    private AgentServiceImp aServ;
    @Autowired
    private AgentRepository aRepo;

  /*  @Autowired
    private RolesServiceImp rServ;
*/
    /*@GetMapping("/addAgentForm")
    public ModelAndView addProductForm() {
        log.error("aaaaaaaaaaaaaaaaaaa"+rServ.getRoles().toString());
        ModelAndView mav = new ModelAndView("register-agent");
        mav.addObject("agent", new Agent());
        mav.addObject("roles",rServ.getRoles());
        return mav;
    }*/


    @PostMapping("/saveAgent")
    public String saveProduct(@Valid @ModelAttribute Agent newA, Errors errors) {

        if (errors.hasErrors()) {
            log.error("form error :" + errors.toString());
            return "register-agent.html";
        }
        if(newA.getRoles().getRoleId()==3){
            newA.setStatus("Accepted");
        }
        aServ.saveAgent(newA);
        return "redirect:/login?register=true";
    }

    @GetMapping("/agentsList")
    public String agentsList(HttpSession session, Model model) {
        Agent agent = (Agent) session.getAttribute("LoggedInAgent");
        model.addAttribute("agents", aRepo.getAgentsByStore(agent.getStore().getId()));
        return "agentsList.html";
    }


    @GetMapping("/deleteAgent")
    public String deleteAgent(@RequestParam Integer agentId) {
        aServ.deleteAgent(agentId);
        return "redirect:/agentsList";
    }

    @GetMapping("/updateAgentForm")
    public ModelAndView updateAgent(@RequestParam Integer agentId) {
        ModelAndView mav = new ModelAndView("updateAgent");
        mav.addObject("agent", aServ.getAgentById(agentId));
        return mav;
    }
    @PostMapping("updateAgent")
    public String updateAgent(@ModelAttribute Agent newA){
        Agent agent=aServ.getAgentById(newA.getId());
        agent.setStatus(newA.getStatus());
        aServ.saveAgent(agent);
        return "redirect:/agentsList";
    }
}
