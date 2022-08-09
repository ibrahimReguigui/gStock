package Ibrahim.SpringBoot.service;

import Ibrahim.SpringBoot.model.Bill;
import Ibrahim.SpringBoot.model.Product;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CreatePdfService {
    @Autowired
    private BillServiceImp billServiceImp;

    public void createPdf(Integer billId) {
        Bill bill = billServiceImp.getBillById(billId);
        List<Product> billProducts = billServiceImp.getBillProducts(bill.getId());
        String filePdf = "C:\\Users\\Asus\\Desktop\\SpringBoot\\src\\main\\resources\\pdfFiles\\" + bill.getClient() + "-" + bill.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss")) + ".pdf";
        try {
            PdfWriter writer = new PdfWriter(filePdf);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            Paragraph title = new Paragraph(bill.getClient()).setTextAlignment(TextAlignment.CENTER.CENTER);
            Paragraph date = new Paragraph(bill.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss"))).setTextAlignment(TextAlignment.CENTER.CENTER);
            Paragraph retourALaLigne = new Paragraph(" ");
            Table table = new Table(5);
            table.addCell(new Cell().add("Reference").setBackgroundColor(Color.BLUE));
            table.addCell(new Cell().add("Name").setBackgroundColor(Color.BLUE));
            table.addCell(new Cell().add("Categorie").setBackgroundColor(Color.BLUE));
            table.addCell(new Cell().add("Quantity").setBackgroundColor(Color.BLUE));
            table.addCell(new Cell().add("Price").setBackgroundColor(Color.BLUE));
            for (Product product : billProducts) {
                table.addCell(new Cell().add(product.getReference()));
                table.addCell(new Cell().add(product.getName()));
                table.addCell(new Cell().add(product.getCategories()));
                table.addCell(new Cell().add(String.valueOf(product.getQuantity())));
                table.addCell(new Cell().add(String.valueOf(product.getPrice())));
            }
            table.addCell(new Cell().add("Total"));
            table.addCell(new Cell().add(String.valueOf(bill.getTotal())));

            document.add(title);
            document.add(date);
            document.add(retourALaLigne);
            document.add(table);
            try {
                // Create an ImageData object
                String imageFile = "C:\\Users\\Asus\\Desktop\\images.png";
                ImageData data = ImageDataFactory.create(imageFile);
                // Creating an Image object
                Image image = new Image(data);

                // Set the position of the image.
                image.setFixedPosition(200, 300);
                // Adding image to the document
                document.add(image);
            } catch (Exception e) {
                System.out.println(
                        "unable to set image position due to " + e);
            }
            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
