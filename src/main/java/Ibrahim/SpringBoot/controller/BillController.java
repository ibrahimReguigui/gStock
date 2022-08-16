package Ibrahim.SpringBoot.controller;

import Ibrahim.SpringBoot.model.Agent;
import Ibrahim.SpringBoot.model.Bill;
import Ibrahim.SpringBoot.model.Product;
import Ibrahim.SpringBoot.model.Store;
import Ibrahim.SpringBoot.repository.BillRepository;
import Ibrahim.SpringBoot.repository.ProductRepository;
import Ibrahim.SpringBoot.service.BillServiceImp;
import Ibrahim.SpringBoot.service.ProductServiceImp;
import Ibrahim.SpringBoot.service.StoreServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.security.core.Authentication;
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
import java.util.List;

@Slf4j
@Controller
public class BillController {

    @Autowired
    private BillServiceImp billServiceImp;
    @Autowired
    private ProductServiceImp productServiceImp;
    @Autowired
    private BillRepository bRep;
    @Autowired
    private ProductRepository pRepo;
    @Autowired
    private StoreServiceImp storeServiceImp;

    @GetMapping("/billForm")
    public ModelAndView newBill(HttpSession session) {
        Agent agent = (Agent) session.getAttribute("LoggedInAgent");
        ModelAndView mav = new ModelAndView("newBill");
        mav.addObject("bill", new Bill());
        mav.addObject("username", agent.getName());
        return mav;
    }

    @PostMapping("/createBill")
    public String createBill(@Valid @ModelAttribute Bill bill, Errors errors, HttpSession session) {
        if (errors.hasErrors()) {
            log.error("form error :" + errors.toString());
            return "newBill.html";
        }
        Agent agent = (Agent) session.getAttribute("LoggedInAgent");
        bill.setAgent(agent);
        bill.setStatus("Instance");
        bill.setStore(agent.getStore());
        billServiceImp.saveBill(bill);
        session.setAttribute("bill", bill);
        return "redirect:/addProductToBillForm";
    }

    @GetMapping("/addProductToBillForm")
    public String addProductToBillForm(HttpSession session, Model model) {
        Agent agent = (Agent) session.getAttribute("LoggedInAgent");
        Bill sbill = (Bill) session.getAttribute("bill");
        Bill bill = billServiceImp.getBillById(sbill.getId());
        model.addAttribute("bill", bill);
        model.addAttribute("products", pRepo.getAllStoreProduct(agent.getStore().getId()));
        model.addAttribute("username", agent.getName());
        model.addAttribute("billProducts", bRep.getBillProducts(bill.getId()));
        return "addProductToBillForm";
    }

    @GetMapping("/addProductToBill")
    public String addProductToBill(@RequestParam Integer productId, HttpSession session) {
        Product product = productServiceImp.getProductById(productId);
        Bill sbill = (Bill) session.getAttribute("bill");
        Bill bill = billServiceImp.getBillById(sbill.getId());
        product.setBill(bill);
        productServiceImp.saveProduct(product);
        bill.setTotal(bill.getTotal() + product.getQuantity() * product.getPrice());
        billServiceImp.saveBill(bill);
        return "redirect:/addProductToBillForm";
    }

    @GetMapping("/cancelBill")
    public String cancelBill(@RequestParam Integer billId) {
        List<Product> listProduct = bRep.getBillProducts(billId);
        for (Product p : listProduct) {
            Product pRef = pRepo.findByReference(p.getReference());
            if (pRef != null) {
                pRef.setQuantity(pRef.getQuantity() + p.getQuantity());
                productServiceImp.deleteProduct(p.getId());
                productServiceImp.saveProduct(pRef);
            } else {
                p.setBill(null);
                productServiceImp.saveProduct(p);
            }
        }
        billServiceImp.deleteBill(billId);
        return "redirect:/showBills";
    }

    @GetMapping("/removeProduct")
    public String removeProduct(@RequestParam Integer productId) {
        Product product = productServiceImp.getProductById(productId);
        Bill bill = product.getBill();
        bill.setTotal(bill.getTotal() - product.getQuantity() * product.getPrice());
        billServiceImp.saveBill(bill);
        Product pRef = pRepo.findByReference(product.getReference());
        if (pRef != null) {
            pRef.setQuantity(pRef.getQuantity() + product.getQuantity());
            productServiceImp.saveProduct(pRef);
            productServiceImp.deleteProduct(productId);
        } else {
            product.setBill(null);
            productServiceImp.saveProduct(product);
        }
        return "redirect:/addProductToBillForm";
    }

    @GetMapping("/showBills")
    public ModelAndView showBills(HttpSession session) {
        Agent agent = (Agent) session.getAttribute("LoggedInAgent");
        ModelAndView mav = new ModelAndView("showBills");
        mav.addObject("bumberOfBills",billServiceImp.getbillsNumberByStore(agent.getStore().getId()));
        mav.addObject("billsTotal",billServiceImp.getBillsTotal(agent.getStore().getId()));
        mav.addObject("avgBills",billServiceImp.getAvgBills(agent.getStore().getId()));
        mav.addObject("maxTotal",billServiceImp.getMaxTotalBill(agent.getStore().getId()));
        Store store = storeServiceImp.getStoreById(agent.getStore().getId());
        if (agent.getRole().getRoleId() == 2) {
            mav.addObject("bills", bRep.getbillsByAgent(agent.getId()));
        } else {
            mav.addObject("bills", bRep.getBillsByStore(agent.getStore().getId()));
        }
        return mav;
    }

    @GetMapping("/deleteBill")
    public String deleteBill(@RequestParam Integer billId) {
        List<Product> listProduct = bRep.getBillProducts(billId);
        for (Product p : listProduct) {
            p.setBill(null);
            productServiceImp.saveProduct(p);
        }
        billServiceImp.deleteBill(billId);
        return "redirect:/showBills";
    }

    @GetMapping("/billDetails")
    public String billDetails(@RequestParam Integer billId, HttpSession session, Model model) {
        Agent agent = (Agent) session.getAttribute("LoggedInAgent");
        Bill bill = billServiceImp.getBillById(billId);
        model.addAttribute("bill", bill);
        model.addAttribute("username", agent.getName());
        model.addAttribute("billProducts", bRep.getBillProducts(bill.getId()));
        return "billDetails";
    }

    @GetMapping("/confirmBill")
    public String confirmBill(@RequestParam Integer billId) {
        Bill bill = billServiceImp.getBillById(billId);
        bill.setStatus("Confirmed");
        billServiceImp.saveBill(bill);
        return "redirect:/showBills";
    }

    @GetMapping("/removeProductBillDetails")
    public String removeProductBillDetails(@RequestParam Integer productId) {
        Product product = productServiceImp.getProductById(productId);
        Bill bill = product.getBill();
        bill.setTotal(bill.getTotal() - product.getQuantity() * product.getPrice());
        billServiceImp.saveBill(bill);
        Product pRef = pRepo.findByReference(product.getReference());
        if (pRef != null) {
            pRef.setQuantity(pRef.getQuantity() + product.getQuantity());
            productServiceImp.saveProduct(pRef);
            productServiceImp.deleteProduct(productId);
        } else {
            product.setBill(null);
            productServiceImp.saveProduct(product);
        }
        return "redirect:/billDetails?billId=" + bill.getId();
    }


}
