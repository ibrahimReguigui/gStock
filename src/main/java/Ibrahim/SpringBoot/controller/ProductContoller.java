package Ibrahim.SpringBoot.controller;

import Ibrahim.SpringBoot.model.Agent;
import Ibrahim.SpringBoot.model.Bill;
import Ibrahim.SpringBoot.model.Product;
import Ibrahim.SpringBoot.repository.AgentRepository;
import Ibrahim.SpringBoot.repository.BillRepository;
import Ibrahim.SpringBoot.repository.ProductRepository;
import Ibrahim.SpringBoot.service.AgentServiceImp;
import Ibrahim.SpringBoot.service.BillServiceImp;
import Ibrahim.SpringBoot.service.ProductServiceImp;
import Ibrahim.SpringBoot.service.StoreServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class ProductContoller {

    @Autowired
    private ProductServiceImp pServ;

    @Autowired
    private AgentRepository aRepo;
    @Autowired
    private ProductRepository pRepo;
    @Autowired
    private BillServiceImp bServ;
@Autowired
private StoreServiceImp sServ;
    @GetMapping("/showProducts")
    public ModelAndView showProducts(Authentication authentication, HttpSession session) {
        ModelAndView mav = new ModelAndView("list-products");
        Agent agent = aRepo.readByEmail(authentication.getName());
        session.setAttribute("LoggedInAgent", agent);
        mav.addObject("products", pRepo.getAllStoreProduct(agent.getStore().getId()));
        mav.addObject("username", agent.getName());
        mav.addObject("roles", authentication.getAuthorities().toString());
        mav.addObject("newQuantity", 0);
        return mav;
    }

    @GetMapping("/addProductForm")
    public String addProductForm(Model model, Authentication authentication, HttpSession session) {
        model.addAttribute("product", new Product());
        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities().toString());
        Agent agent = (Agent) session.getAttribute("LoggedInAgent");

        return "add-product-form.html";
    }


    @PostMapping("/saveProduct")
    public String saveProduct(@Valid @ModelAttribute Product newP, Errors errors, HttpSession session) {
        if (errors.hasErrors()) {
            log.error("form error :" + errors.toString());
            return "add-product-form.html";
        }
        Agent agent = (Agent) session.getAttribute("LoggedInAgent");
        newP.setStore(agent.getStore());
        pServ.saveProduct(newP);
        return "redirect:/showProducts";
    }

    @GetMapping("/updateForm")
    public ModelAndView updateForm(@RequestParam Integer productId) {
        ModelAndView mav = new ModelAndView("add-product-form");
        mav.addObject("product", pServ.getProductById(productId));
        return mav;
    }

    @GetMapping("/deleteProduct")
    public String deleteProduct(@RequestParam Integer productId) {
        pServ.deleteProduct(productId);
        return "redirect:/showProducts";
    }

    @GetMapping("/splitProductForm")
    public ModelAndView splitProductForm(@RequestParam Integer productId) {
        ModelAndView mav = new ModelAndView("splitProductForm");
        mav.addObject("product", pServ.getProductById(productId));
        return mav;
    }
    @PostMapping("/splitProduct")
    public String splitProduct(@ModelAttribute Product product, HttpSession session) {
        Agent agent = (Agent) session.getAttribute("LoggedInAgent");
        Product ancientP=pServ.getProductById(product.getId());
        if(ancientP.getQuantity()<product.getQuantity()){
            return "redirect:/addProductToBillForm";
        }

        Product newP =new Product();
        newP.setStore(sServ.getStoreById(agent.getStore().getId()));
        newP.setCategories(ancientP.getCategories());
        newP.setName(ancientP.getName());
        newP.setPrice(ancientP.getPrice());
        newP.setReference(ancientP.getReference());
        newP.setQuantity(product.getQuantity());
        newP.setCreatedBy(ancientP.getCreatedBy());
        newP.setCreatedAt(ancientP.getCreatedAt());
        Bill bill=(Bill)session.getAttribute("bill");
        newP.setBill(bill);
        pServ.saveProduct(newP);


        ancientP.setQuantity(ancientP.getQuantity()-newP.getQuantity());
        bill.setTotal(bill.getTotal()+newP.getQuantity()*newP.getPrice());
        bServ.saveBill(bill);
        if (ancientP.getQuantity()==0){
            pServ.deleteProduct(ancientP.getId());
        }else {
            pServ.saveProduct(ancientP);
        }
        return "redirect:/addProductToBillForm";
    }
}
