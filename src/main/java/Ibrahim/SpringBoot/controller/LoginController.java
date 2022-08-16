package Ibrahim.SpringBoot.controller;

import Ibrahim.SpringBoot.model.Agent;
import Ibrahim.SpringBoot.service.AgentServiceImp;
import Ibrahim.SpringBoot.service.ProductServiceImp;
import Ibrahim.SpringBoot.service.RoleServiceImp;
import Ibrahim.SpringBoot.service.StoreServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Slf4j
@Controller
public class LoginController {

    @Autowired
    private RoleServiceImp roleServiceImp;
    @Autowired
    private AgentServiceImp agentServiceImp;
    @Autowired
    private StoreServiceImp storeServiceImp;
    @Autowired
    private ProductServiceImp productServiceImp;

    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String displayLoginPage(@RequestParam(value = "error", required = false) String error,
                                   @RequestParam(value = "logout", required = false) String logout,
                                   @RequestParam(value = "register", required = false) String register,
                                   Model model, RedirectAttributes redirAttrs) {
        String errorMessge = null;
        if (error != null) {
            errorMessge = error;
        }
        if (logout != null) {
            redirAttrs.addFlashAttribute("success", "You have been successfully logged out !!");
            return "redirect:/login";
        }
        if (register != null) {
            redirAttrs.addFlashAttribute("success", "Registration successful !!");
            return "redirect:/login";
        }
        model.addAttribute("errorMessge", errorMessge);
        return "login.html";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/home";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("agent", new Agent());
        model.addAttribute("roles", roleServiceImp.getRoles());
        return "register-agent.html";
    }

    @GetMapping("/home")
    public ModelAndView homePage(Authentication authentication, HttpSession session, Principal principal) {
        ModelAndView mav = new ModelAndView("homePage");
        Integer agentSize=agentServiceImp.getAllAgents().size();
        Integer productSize=productServiceImp.getAllProduct().size();
        Integer storeSize=storeServiceImp.getStore().size();
        mav.addObject("agentSize",agentSize);
        mav.addObject("productSize",productSize);
        mav.addObject("storeSize",storeSize);
        if(principal==null){
            return mav;
        }
        Agent agent = agentServiceImp.readByEmail(authentication.getName());
        session.setAttribute("LoggedInAgent", agent);
        return mav;
    }


}