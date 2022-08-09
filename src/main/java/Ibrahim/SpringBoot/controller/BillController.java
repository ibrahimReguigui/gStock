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
    private BillServiceImp bServ;
    @Autowired
    private ProductServiceImp pServ;
    @Autowired
    private BillRepository bRep;
    @Autowired
    private ProductRepository pRepo;
    @Autowired
    private StoreServiceImp sServ;

    @GetMapping("/billForm")
    public ModelAndView newBill(HttpSession session) {
        Agent agent = (Agent) session.getAttribute("LoggedInAgent");
        ModelAndView mav = new ModelAndView("newBill");
        mav.addObject("bill", new Bill());
        mav.addObject("username", agent.getName());
        return mav;
    }

    @PostMapping("/createBill")
    public String createBill(@Valid @ModelAttribute Bill newB, Errors errors, HttpSession session) {
        if (errors.hasErrors()) {
            /*ModelAndView mav = new ModelAndView("newBill");*/
            log.error("form error :" + errors.toString());
            return "redirect:/billForm";
        }
        Agent agent = (Agent) session.getAttribute("LoggedInAgent");
        newB.setAgent(agent);
        newB.setStatus("Instance");
        newB.setStore(agent.getStore());
        bServ.saveBill(newB);
        session.setAttribute("bill", newB);


       /* ModelAndView mav = new ModelAndView("addProductToBillForm");
        mav.addObject("bill",newB);*/
        return "redirect:/addProductToBillForm";
    }

    @GetMapping("/addProductToBillForm")
    public String addProductToBillForm(HttpSession session, Model model) {
        Agent agent = (Agent) session.getAttribute("LoggedInAgent");
        Bill sbill = (Bill) session.getAttribute("bill");
        Bill bill = bServ.getBillById(sbill.getId());
        model.addAttribute("bill", bill);
        model.addAttribute("products", pRepo.getAllStoreProduct(agent.getStore().getId()));
        model.addAttribute("username", agent.getName());
        model.addAttribute("billProducts", bRep.getBillProducts(bill.getId()));
        return "addProductToBillForm";
    }

    @GetMapping("/addProductToBill")
    public String addProductToBill(@RequestParam Integer productId, HttpSession session) {
        Product product = pServ.getProductById(productId);
        Bill sbill = (Bill) session.getAttribute("bill");
        Bill bill = bServ.getBillById(sbill.getId());
        product.setBill(bill);
        pServ.saveProduct(product);
        bill.setTotal(bill.getTotal() + product.getQuantity() * product.getPrice());
        bServ.saveBill(bill);
        return "redirect:/addProductToBillForm";
    }

    @GetMapping("/cancelBill")
    public String cancelBill(@RequestParam Integer billId) {
        List<Product> listProduct = bRep.getBillProducts(billId);
        for (Product p : listProduct) {
            Product pRef = pRepo.findByReference(p.getReference());
            if (pRef != null) {
                pRef.setQuantity(pRef.getQuantity() + p.getQuantity());
                pServ.deleteProduct(p.getId());
                pServ.saveProduct(pRef);
            } else {
                p.setBill(null);
                pServ.saveProduct(p);
            }
        }
        bServ.deleteBill(billId);
        return "redirect:/showBills";
    }

    @GetMapping("/removeProduct")
    public String removeProduct(@RequestParam Integer productId) {
        Product product = pServ.getProductById(productId);
        Bill bill = product.getBill();
        bill.setTotal(bill.getTotal() - product.getQuantity() * product.getPrice());
        bServ.saveBill(bill);
        Product pRef = pRepo.findByReference(product.getReference());
        if (pRef != null) {
            pRef.setQuantity(pRef.getQuantity() + product.getQuantity());
            pServ.saveProduct(pRef);
            pServ.deleteProduct(productId);
        } else {
            product.setBill(null);
            pServ.saveProduct(product);
        }
        return "redirect:/addProductToBillForm";
    }

    @GetMapping("/showBills")
    public ModelAndView showBills(HttpSession session) {
        Agent agent = (Agent) session.getAttribute("LoggedInAgent");
        ModelAndView mav = new ModelAndView("showBills");

        Store store = sServ.getStoreById(agent.getStore().getId());
        if (agent.getRoles().getRoleId() == 2) {
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
            pServ.saveProduct(p);
        }
        bServ.deleteBill(billId);
        return "redirect:/showBills";
    }

    @GetMapping("/billDetails")
    public String billDetails(@RequestParam Integer billId, HttpSession session, Model model) {
        Agent agent = (Agent) session.getAttribute("LoggedInAgent");
        Bill bill = bServ.getBillById(billId);
        model.addAttribute("bill", bill);
        model.addAttribute("username", agent.getName());
        model.addAttribute("billProducts", bRep.getBillProducts(bill.getId()));
        return "billDetails";
    }

    @GetMapping("/confirmBill")
    public String confirmBill(@RequestParam Integer billId) {
        Bill bill = bServ.getBillById(billId);
        bill.setStatus("Confirmed");
        bServ.saveBill(bill);
        return "redirect:/showBills";
    }

    @GetMapping("/removeProductBillDetails")
    public String removeProductBillDetails(@RequestParam Integer productId) {
        Product product = pServ.getProductById(productId);
        Bill bill = product.getBill();
        bill.setTotal(bill.getTotal() - product.getQuantity() * product.getPrice());
        bServ.saveBill(bill);
        Product pRef = pRepo.findByReference(product.getReference());
        if (pRef != null) {
            pRef.setQuantity(pRef.getQuantity() + product.getQuantity());
            pServ.saveProduct(pRef);
            pServ.deleteProduct(productId);
        } else {
            product.setBill(null);
            pServ.saveProduct(product);
        }
        return "redirect:/billDetails?billId=" + bill.getId();
    }


}
