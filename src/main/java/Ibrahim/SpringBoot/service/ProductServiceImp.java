package Ibrahim.SpringBoot.service;

import Ibrahim.SpringBoot.model.Product;
import Ibrahim.SpringBoot.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    public void saveProduct(Product newP) {
        productRepository.save(newP);
    }

    public void deleteProduct(Integer productId) {
        productRepository.deleteById(productId);
    }

    public Product getProductById(Integer productId) {
        return productRepository.findById(productId).get();
    }

    public  Integer getTotalQuantityByStore(Long id ){
        return productRepository.getTotalQuantityByStore(id);
    };

    public Integer getNumberOfCategorieByStore(Long id ){
        return productRepository.getNumberOfCategorieByStore(id).size();
    };

    public float getTotalPriceInStore(Long id ){
        return productRepository.getTotalPriceInStore(id);
    };
    public Integer getNumberOfProductinStore(Long id ){
        return productRepository.getNumberOfProductinStore(id);
    };


}
