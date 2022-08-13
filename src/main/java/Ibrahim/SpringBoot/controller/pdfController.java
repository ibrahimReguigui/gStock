package Ibrahim.SpringBoot.controller;

import Ibrahim.SpringBoot.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class pdfController {
    private PdfService pdfService;

    @Autowired
    public pdfController(PdfService pdfService){
        this.pdfService = pdfService;
    }

    @GetMapping("/createPdf")
    public String pdfFile(@RequestParam Integer billId, RedirectAttributes redirAttrs){
        pdfService.createPdf(billId);
        redirAttrs.addFlashAttribute("success", "File Downloaded .");
        return "redirect:/billDetails?billId=" + billId;
    }
}
