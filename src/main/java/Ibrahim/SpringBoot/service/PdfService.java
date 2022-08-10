package Ibrahim.SpringBoot.service;

import Ibrahim.SpringBoot.model.Bill;
import Ibrahim.SpringBoot.model.Product;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfService {
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

            Paragraph title = new Paragraph("Bill N° : "+bill.getId()).setFontSize(30).setTextAlignment(TextAlignment.CENTER.CENTER);
            Paragraph store = new Paragraph("Store : "+bill.getStore().getStoreName()+"                                            " +
                    "                        Tel : "+bill.getStore().getStoreNumber()).setTextAlignment(TextAlignment.CENTER.CENTER);
            Paragraph name = new Paragraph("Client Name : "+bill.getClient()).setItalic();
            Paragraph date = new Paragraph("Date : "+bill.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).setItalic();
            Paragraph time = new Paragraph("Time : "+bill.getCreatedAt().format(DateTimeFormatter.ofPattern("HH-mm-ss"))).setItalic();
            Paragraph retourALaLigne = new Paragraph(" ");
            Paragraph ligne = new Paragraph("---------------------------------------------------------" +
                    "-------------------------------------------------------------------------")
                    .setTextAlignment(TextAlignment.CENTER.CENTER);
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


            try {
                // Create an ImageData object
                String imageFile = "C:\\Users\\Asus\\Desktop\\images.png";
                ImageData data = ImageDataFactory.create(imageFile);
                // Creating an Image object
                Image image = new Image(data);
                // Set the position of the image.
                image.setFixedPosition(1,0, 0);
                // Adding image to the document
                document.add(image);

            } catch (Exception e) {
                System.out.println(
                        "unable to set image position due to " + e);
            }
            document.add(store);
            document.add(ligne);
            document.add(title);
            document.add(name);
            document.add(date);
            document.add(time);
            document.add(retourALaLigne);
            document.add(retourALaLigne);
            document.add(retourALaLigne);
            document.add(table);
            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
