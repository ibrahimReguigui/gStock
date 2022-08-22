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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
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
    @Autowired
    private PasswordEncoder passwordEncoder;


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

    @GetMapping("/confirmedAgentsList")
    public String agentsList(HttpSession session, Model model) {
        Agent agent = (Agent) session.getAttribute("LoggedInAgent");
        List<Agent> list= agentRepository.getAgentsConfirmedByStore(agent.getStore().getId());
        if (list.size()==0 ) {
            model.addAttribute("agents","empty");
        } else
            model.addAttribute("agents", list);
        model.addAttribute("numberOfAgents", agentRepository.getNumberOfAgentByStore(agent.getStore().getId()));
        return "agentsList.html";
    }
    @GetMapping("/awaitingConfirmationAgentsList")
    public String awaitingConfirmationAgentsList(HttpSession session, Model model) {
        Agent agent = (Agent) session.getAttribute("LoggedInAgent");
        List<Agent> list=agentRepository.getAgentsAwaitingConfirmationByStore(agent.getStore().getId());
        if (list.size()==0 ) {
            model.addAttribute("agents","empty");
        } else
            model.addAttribute("agents", list);
        model.addAttribute("awaitingAgents", agentRepository.getAwaitingConfirmationAgents(agent.getStore().getId()));
        return "agentsListAwaitingConfirmation.html";
    }

    @GetMapping("/deleteAgent")
    public String deleteAgent(@RequestParam Integer agentId) {
        agentServiceImp.deleteAgent(agentId);
        return "redirect:/confirmedAgentsList";
    }
    @GetMapping("/deleteAgentawaitingConfirmationAgentsList")
    public String deleteAgentawaitingConfirmationAgentsList(@RequestParam Integer agentId) {
        agentServiceImp.deleteAgent(agentId);
        return "redirect:/awaitingConfirmationAgentsList";
    }
    @GetMapping("/deleteAgentconfirmedAgentsList")
    public String deleteAgentconfirmedAgentsList(@RequestParam Integer agentId) {
        agentServiceImp.deleteAgent(agentId);
        return "redirect:/confirmedAgentsList";
    }

    @GetMapping("/updateAgentForm")
    public ModelAndView updateAgent(@RequestParam Integer agentId, HttpSession session) {
        ModelAndView mav = new ModelAndView("updateAgent");
        Agent LoggedInAgent = (Agent) session.getAttribute("LoggedInAgent");
        System.out.println(LoggedInAgent.getRole());
        mav.addObject("LoggedInAgent", LoggedInAgent);
        mav.addObject("agent", agentServiceImp.getAgentById(agentId));
        List<Role> roles = roleServiceImp.getRoles();
        roles.remove(2);
        mav.addObject("roles", roles);
        mav.addObject("allStatus", AgentStatus.values());
        return mav;
    }

    @PostMapping("updateAgent")
    public String updateAgent(@ModelAttribute Agent newA) {
        Agent agent = agentServiceImp.getAgentById(newA.getId());
        agent.setName(newA.getName());
        agent.setRole(newA.getRole());
        agent.setStatus(newA.getStatus());
        agentServiceImp.updateAgent(agent);
        return "redirect:/confirmedAgentsList";
    }

    @GetMapping("/profile")
    public ModelAndView agentProfile(HttpSession session) {
        ModelAndView mav = new ModelAndView("profile");
        Agent LoggedInAgent = (Agent) session.getAttribute("LoggedInAgent");
        mav.addObject("LoggedInAgent", agentServiceImp.getAgentById(LoggedInAgent.getId()));

        return mav;
    }

    @GetMapping("/updateProfile")
    public ModelAndView updateProfile(HttpSession session) {
        ModelAndView mav = new ModelAndView("updateProfile");
        Agent LoggedInAgent = (Agent) session.getAttribute("LoggedInAgent");
        mav.addObject("LoggedInAgent", LoggedInAgent);
        mav.addObject("agent", LoggedInAgent);
        return mav;
    }

//    @PostMapping("updateProfile")
//    public String updateProfile( RedirectAttributes redirAttrs,@ModelAttribute Agent newA, HttpSession session, Model model, BindingResult bindingResult) {
//        Agent agent = agentServiceImp.getAgentById(newA.getId());
//
//        if(newA.getMobileNumber().isEmpty()){
//            redirAttrs.addFlashAttribute("mobileNumberEmpty",  "Mobile number must not be blank");
//        }else if (newA.getMobileNumber().matches("(^$|[0-9]{8})")) {
//            agent.setMobileNumber(newA.getMobileNumber());
//            agentServiceImp.updateAgent(agent);
//        } else {
//            redirAttrs.addFlashAttribute("mobileNumberInvalid",  "Mobile number must be 8 digits");
//        }
//
//
//        session.setAttribute("LoggedInAgent", agent);
//        Agent LoggedInAgent = (Agent) session.getAttribute("LoggedInAgent");
//        model.addAttribute("LoggedInAgent",LoggedInAgent);
//
//        return "redirect:/profile";
//    }

    @GetMapping("/changePasswordForm")
    public ModelAndView changePasswordForm(HttpSession session) {
        ModelAndView mav = new ModelAndView("changePasswordForm");
        Agent LoggedInAgent = (Agent) session.getAttribute("LoggedInAgent");
        mav.addObject("LoggedInAgent", LoggedInAgent);
        mav.addObject("agent", new Agent());
        return mav;
    }

    @PostMapping("/changePassword")
    public String changePassword(RedirectAttributes redirAttrs, @ModelAttribute Agent agent, HttpSession session, Model model) {

        Agent LoggedInAgent = (Agent) session.getAttribute("LoggedInAgent");
        if (agent.getName().isEmpty()) {
            redirAttrs.addFlashAttribute("ancientPasswordEmpty", "Ancient Password must not be blank");
        } else if (!passwordEncoder.matches(agent.getName(), LoggedInAgent.getPwd())) {
            redirAttrs.addFlashAttribute("wrongPassword", "Wrong Password!");
        }
        if (agent.getPwd().isEmpty()) {
            redirAttrs.addFlashAttribute("passwordEmpty", "Password must not be blank");
        } else if (agent.getPwd().length() < 5) {
            redirAttrs.addFlashAttribute("shortPassword", "Password must be at least 5 characters long");
        }
        if (agent.getConfirmPwd().isEmpty()) {
            redirAttrs.addFlashAttribute("confirmPasswordEmpty", "Confirm Password must not be blank");
        } else if (!agent.getPwd().equals(agent.getConfirmPwd())) {
            redirAttrs.addFlashAttribute("PwdConfirmPwdMismatch", "Passwords do not match!");
        }
        if (redirAttrs.getFlashAttributes().isEmpty()) {
            Agent modifiedAgent = agentServiceImp.getAgentById(LoggedInAgent.getId());
            modifiedAgent.setPwd(agent.getPwd());
            agentServiceImp.saveAgent(modifiedAgent);
            Agent finalAgent = agentServiceImp.getAgentById(LoggedInAgent.getId());
            redirAttrs.addFlashAttribute("success", "Passwords Changed!");
            session.setAttribute("LoggedInAgent", finalAgent);
        }

        model.addAttribute("LoggedInAgent", LoggedInAgent);

        return "redirect:/changePasswordForm";
    }

    @PostMapping("/updateMobileNumber")
    public String updateMobileNumber(RedirectAttributes redirAttrs, @ModelAttribute Agent agent, HttpSession session, Model model) {
        Agent LoggedInAgent = (Agent) session.getAttribute("LoggedInAgent");
        if (agent.getMobileNumber().isEmpty()) {
            redirAttrs.addFlashAttribute("mobileNumberEmpty", "Mobile number must not be blank");
        } else if (agent.getMobileNumber().matches("(^$|[0-9]{8})")) {
            LoggedInAgent.setMobileNumber(agent.getMobileNumber());
            agentServiceImp.updateAgent(LoggedInAgent);
            session.setAttribute("LoggedInAgent", LoggedInAgent);
            redirAttrs.addFlashAttribute("success", "Mobile number changed !");
            return "redirect:/profile";
        } else {
            redirAttrs.addFlashAttribute("mobileNumberInvalid", "Mobile number must be 8 digits");
        }
        return "redirect:/updateProfile";
    }

    @PostMapping("/updateImage")
    public String updateImage(RedirectAttributes redirAttrs, @RequestParam MultipartFile file, HttpSession session, Model model) {
        Agent LoggedInAgent = (Agent) session.getAttribute("LoggedInAgent");
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println(fileName);
        if (fileName.contains(".png")|| fileName.contains(".jpeg") || fileName.contains(".jpg")){
            agentServiceImp.updateAgentWithImage(LoggedInAgent, file);
            Agent updatedLoggedInAgent = agentServiceImp.getAgentById(LoggedInAgent.getId());
            session.setAttribute("LoggedInAgent", updatedLoggedInAgent);
            model.addAttribute("agent", updatedLoggedInAgent);
            redirAttrs.addFlashAttribute("success", "Photo updated !");
            return "redirect:/profile";

        }else
            redirAttrs.addFlashAttribute("errorImg", "Not a valid Image !");
            return "redirect:/updateProfile";

//        String filename = LoggedInAgent.getId() + file.getOriginalFilename().substring(file.getOriginalFilename().length() - 4);
//        Path fileNameAndPath = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/Images", filename);
//        try {
//            Files.write(fileNameAndPath, file.getBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        LoggedInAgent.setImage(filename);
//        agentServiceImp.updateAgent(LoggedInAgent);

    }

    @GetMapping("/confirmedAgentsSearch")
    public String confirmedAgentsSearch(@RequestParam String searchText, HttpSession session, Model model) {
        Agent agent = (Agent) session.getAttribute("LoggedInAgent");
        List<Agent> list=agentRepository.confirmedAgentsSearch(agent.getStore().getId(), searchText);
        if (list.size()==0 ) {
            model.addAttribute("agents",null);
        } else
            model.addAttribute("agents", list);
        model.addAttribute("numberOfAgents", agentRepository.getNumberOfAgentByStore(agent.getStore().getId()));
        return "agentsList.html";
    }
    @GetMapping("/awaitingConfirmationAgentsSearch")
    public String awaitingConfirmationAgentsSearch(@RequestParam String searchText, HttpSession session, Model model) {
        Agent agent = (Agent) session.getAttribute("LoggedInAgent");
        List<Agent> list=agentRepository.awaitingConfirmationAgentsSearch(agent.getStore().getId(), searchText);
        if (list.size()==0 ) {
            model.addAttribute("agents",null);
        } else
            model.addAttribute("agents", list);
        model.addAttribute("numberOfAgents", agentRepository.getNumberOfAgentByStore(agent.getStore().getId()));
        return "agentsListAwaitingConfirmation.html";
    }
}
