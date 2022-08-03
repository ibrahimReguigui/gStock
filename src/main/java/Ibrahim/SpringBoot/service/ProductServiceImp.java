package Ibrahim.SpringBoot.service;

import Ibrahim.SpringBoot.model.Product;
import Ibrahim.SpringBoot.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository pRepo;

    public List<Product> getAllProduct() {
        return pRepo.findAll();
    }

    public void saveProduct(Product newP) {
        pRepo.save(newP);
    }

    public void deleteProduct(Integer productId) {
        pRepo.deleteById(productId);
    }

    public Product getProductById(Integer productId) {
        return pRepo.findById(productId).get();
    }


}
