package Ibrahim.SpringBoot.service;

import Ibrahim.SpringBoot.model.Product;

import java.util.List;

public interface ProductService {
    public List<Product> getAllProduct();

    public Product getProductById(Integer productId);

    public void deleteProduct(Integer productId);

    public void saveProduct(Product newP);

    public Integer getTotalQuantityByStore(Long id );
    public Integer getNumberOfCategorieByStore(Long id );

    public float getTotalPriceInStore(Long id);
    public Integer getNumberOfProductinStore(Long id );


}
