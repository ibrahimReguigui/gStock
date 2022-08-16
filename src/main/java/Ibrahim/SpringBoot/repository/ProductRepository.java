package Ibrahim.SpringBoot.repository;

import Ibrahim.SpringBoot.model.Product;
import Ibrahim.SpringBoot.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("select p from Product p where p.store.id=?1 and p.bill is null " )
    public List<Product> getAllStoreProduct(Long storeId) ;
    @Query("select p from Product p where p.reference=?1 and p.bill is null " )
    Product findByReference(String ref );

    @Query("select sum(p.quantity) from Product p where p.store.id=?1 and p.bill is null " )
    Integer getTotalQuantityByStore(Long id );

    @Query("select distinct p.categories from Product p where p.store.id=?1 and p.bill is null" )
    List<String> getNumberOfCategorieByStore(Long id );

    @Query("select sum(p.quantity*p.price) from Product p where p.store.id=?1 and p.bill is null" )
    float getTotalPriceInStore(Long id );

    @Query("select count (p) from Product p where p.store.id=?1 and p.bill is null" )
    Integer getNumberOfProductinStore(Long id );

}
