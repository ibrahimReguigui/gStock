package Ibrahim.SpringBoot.controller;

import Ibrahim.SpringBoot.model.Agent;
import Ibrahim.SpringBoot.model.AgentStatus;
import Ibrahim.SpringBoot.model.Role;
import Ibrahim.SpringBoot.model.Store;
import Ibrahim.SpringBoot.repository.AgentRepository;
import Ibrahim.SpringBoot.service.AgentServiceImp;
import Ibrahim.SpringBoot.service.RoleServiceImp;
import Ibrahim.SpringBoot.service.StoreServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class AgentContoller {
    @Autowired
    private AgentServiceImp agentServiceImp;
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private StoreServiceImp storeServiceImp;
    @Autowired
    private RoleServiceImp roleServiceImp;


    @PostMapping("/saveAgent")
    public String saveAgent(@ModelAttribute Agent newA) {
        agentServiceImp.saveAgent(newA);
        return "redirect:/login?register=true";
    }


    @PostMapping("/finalizeRegistration")
    public String finalizeRegistration(@Valid @ModelAttribute Agent newA, Errors errors, Model model, BindingResult bindingResult) {
        model.addAttribute("roles", roleServiceImp.getRoles());
        if (agentServiceImp.agentExist(newA.getEmail())) {
            bindingResult.addError(new FieldError("newA", "email", "Email already exist"));
        }
        if (bindingResult.hasErrors()) {
            return "register-agent.html";
        }
        if (newA.getRole().getRoleId() == 3) {
            model.addAttribute("agent", newA);
            model.addAttribute("store", new Store());
            return "store-Registration-Form.html";
        }
        model.addAttribute("agent", newA);
        model.addAttribute("stores", storeServiceImp.getStore());
        return "finalizeRegistration.html";
    }

    @GetMapping("/agentsList")
    public String agentsList(HttpSession session, Model model) {
        Agent agent = (Agent) session.getAttribute("LoggedInAgent");
        model.addAttribute("agents", agentRepository.getAgentsByStore(agent.getStore().getId()));
        return "agentsList.html";
    }


    @GetMapping("/deleteAgent")
    public String deleteAgent(@RequestParam Integer agentId) {
        agentServiceImp.deleteAgent(agentId);
        return "redirect:/agentsList";
    }

    @GetMapping("/updateAgentForm")
    public ModelAndView updateAgent(@RequestParam Integer agentId) {
        ModelAndView mav = new ModelAndView("updateAgent");
        mav.addObject("agent", agentServiceImp.getAgentById(agentId));
        List<Role> roles=roleServiceImp.getRoles();
        roles.remove(2);
        mav.addObject("roles", roles);
        mav.addObject("allStatus", AgentStatus.values());
        return mav;
    }

    @PostMapping("updateAgent")
    public String updateAgent(@ModelAttribute Agent newA) {
        Agent agent = agentServiceImp.getAgentById(newA.getId());
        agent.setRole(newA.getRole());
        agent.setStatus(newA.getStatus());
        agentServiceImp.updateAgent(agent);
        return "redirect:/agentsList";
    }
}
