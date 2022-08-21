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
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class ProductContoller {

    @Autowired
    private ProductServiceImp productServiceImp;

    @Autowired
    private AgentRepository aRepo;
    @Autowired
    private ProductRepository pRepo;
    @Autowired
    private BillServiceImp billServiceImp;
    @Autowired
    private StoreServiceImp sServ;

    @GetMapping("/showProducts")
    public ModelAndView showProducts(Authentication authentication, HttpSession session) {
        ModelAndView mav = new ModelAndView("list-products");
        Agent agent = (Agent) session.getAttribute("LoggedInAgent");
        mav.addObject("products", pRepo.getAllStoreProduct(agent.getStore().getId()));
        mav.addObject("numberProduct",productServiceImp.getNumberOfProductinStore(agent.getStore().getId()));
        mav.addObject("quantityProduct",productServiceImp.getTotalQuantityByStore(agent.getStore().getId()));
        mav.addObject("numberCategories",productServiceImp.getNumberOfCategorieByStore(agent.getStore().getId()));
        mav.addObject("totalPrice",productServiceImp.getTotalPriceInStore(agent.getStore().getId()));
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
        productServiceImp.saveProduct(newP);
        return "redirect:/showProducts";
    }

    @GetMapping("/updateForm")
    public ModelAndView updateForm(@RequestParam Integer productId) {
        ModelAndView mav = new ModelAndView("add-product-form");
        mav.addObject("product", productServiceImp.getProductById(productId));
        return mav;
    }

    @GetMapping("/deleteProduct")
    public String deleteProduct(@RequestParam Integer productId) {
        productServiceImp.deleteProduct(productId);
        return "redirect:/showProducts";
    }

    @GetMapping("/splitProductForm")
    public ModelAndView splitProductForm(@RequestParam Integer productId) {
        ModelAndView mav = new ModelAndView("splitProductForm");
        mav.addObject("product", productServiceImp.getProductById(productId));
        return mav;
    }

    @GetMapping( "/splitProduct" )
    public String splitProduct(@RequestParam("wantedQuantity") Integer wantedQuantity, @RequestParam("productId") Integer productId, HttpSession session, RedirectAttributes redirAttrs) {
        Product wantedProduct=productServiceImp.getProductById(productId);
        if(wantedQuantity>wantedProduct.getQuantity()){
            redirAttrs.addFlashAttribute("wantedSup", "Sorry insufficient quality in stock !");
            return "redirect:/addProductToBillForm";
        }
        if(wantedQuantity<1){
            redirAttrs.addFlashAttribute("wantedInf", "Quantity can't be below 1 !");
            return "redirect:/addProductToBillForm";
        }
        if(wantedQuantity==0){
            return "redirect:/addProductToBillForm";
        }

        Bill sbill = (Bill) session.getAttribute("bill");
        Bill bill=billServiceImp.getBillById(sbill.getId());

        Product billProduct=new Product();
        billProduct.setBill(bill);
        billProduct.setQuantity(wantedQuantity);
        billProduct.setReference(wantedProduct.getReference());
        billProduct.setName(wantedProduct.getName());
        billProduct.setPrice(wantedProduct.getPrice());
        billProduct.setCategories(wantedProduct.getCategories());
        billProduct.setStore(wantedProduct.getStore());

        wantedProduct.setQuantity(wantedProduct.getQuantity()-wantedQuantity);

        if (wantedProduct.getQuantity()==0){
            productServiceImp.deleteProduct(wantedProduct.getId());
        }else
            productServiceImp.saveProduct(wantedProduct);

        productServiceImp.saveProduct(billProduct);

        bill.setTotal(sbill.getTotal()+billProduct.getQuantity()* billProduct.getPrice());
        billServiceImp.saveBill(bill);

        return "redirect:/addProductToBillForm";


//        Agent agent = (Agent) session.getAttribute("LoggedInAgent");
//        Product ancientP = productServiceImp.getProductById(product.getId());
//        if (ancientP.getQuantity() < product.getQuantity()) {
//            bindingResult.addError(new FieldError("product", "quantity", "Sorry insufficient quality in stock "));
//            return "splitProductForm";
//        }
//        if (product.getQuantity() == 0) {
//            return "redirect:/addProductToBillForm";
//        }
//
//        Product newP = new Product();
//        newP.setStore(sServ.getStoreById(agent.getStore().getId()));
//        newP.setCategories(ancientP.getCategories());
//        newP.setName(ancientP.getName());
//        newP.setPrice(ancientP.getPrice());
//        newP.setReference(ancientP.getReference());
//        newP.setQuantity(product.getQuantity());
//        newP.setCreatedBy(ancientP.getCreatedBy());
//        newP.setCreatedAt(ancientP.getCreatedAt());
//        Bill bill = (Bill) session.getAttribute("bill");
//        Bill bill2=billServiceImp.getBillById(bill.getId());
//        newP.setBill(bill2);
//        productServiceImp.saveProduct(newP);
//
//
//        ancientP.setQuantity(ancientP.getQuantity() - newP.getQuantity());
//        bill2.setTotal(bill2.getTotal() + newP.getQuantity() * newP.getPrice());
//        billServiceImp.saveBill(bill2);
//        if (ancientP.getQuantity() == 0) {
//            productServiceImp.deleteProduct(ancientP.getId());
//        } else {
//            productServiceImp.saveProduct(ancientP);
//        }


    }

    @GetMapping( "/splitProductBillDetails" )
    public String splitProductBillDetails(@RequestParam("wantedQuantity") Integer wantedQuantity, @RequestParam("productId") Integer productId,@RequestParam("billId") Integer billId, HttpSession session, RedirectAttributes redirAttrs) {
        Product wantedProduct = productServiceImp.getProductById(productId);
        if (wantedQuantity > wantedProduct.getQuantity()) {
            redirAttrs.addFlashAttribute("wantedSup", "Sorry insufficient quality in stock !");
            return "redirect:/billDetails?billId=" + billId;
        }
        if (wantedQuantity < 1) {
            redirAttrs.addFlashAttribute("wantedInf", "Quantity can't be below 1 !");
            return "redirect:/billDetails?billId=" + billId;
        }
        if (wantedQuantity == 0) {
            return "redirect:/billDetails?billId=" + billId;
        }


        Bill bill = billServiceImp.getBillById(billId);

        Product billProduct = new Product();
        billProduct.setBill(bill);
        billProduct.setQuantity(wantedQuantity);
        billProduct.setReference(wantedProduct.getReference());
        billProduct.setName(wantedProduct.getName());
        billProduct.setPrice(wantedProduct.getPrice());
        billProduct.setCategories(wantedProduct.getCategories());
        billProduct.setStore(wantedProduct.getStore());

        wantedProduct.setQuantity(wantedProduct.getQuantity() - wantedQuantity);

        if (wantedProduct.getQuantity() == 0) {
            productServiceImp.deleteProduct(wantedProduct.getId());
        } else
            productServiceImp.saveProduct(wantedProduct);

        productServiceImp.saveProduct(billProduct);

        bill.setTotal(bill.getTotal() + billProduct.getQuantity() * billProduct.getPrice());
        billServiceImp.saveBill(bill);

        return "redirect:/billDetails?billId=" + billId;

    }
    }
