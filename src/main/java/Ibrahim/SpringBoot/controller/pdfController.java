package Ibrahim.SpringBoot.controller;

import Ibrahim.SpringBoot.model.Bill;
import Ibrahim.SpringBoot.service.BillServiceImp;
import Ibrahim.SpringBoot.service.CreatePdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class pdfController {
    private CreatePdfService createPdfService;

    @Autowired
    public pdfController(CreatePdfService createPdfService){
        this.createPdfService=createPdfService;
    }

    @GetMapping("/createPdf")
    public String pdfFile(@RequestParam Integer billId){
        createPdfService.createPdf(billId);
        return "redirect:/billDetails?billId=" + billId;
    }
}
