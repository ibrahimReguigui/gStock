package Ibrahim.SpringBoot.repository;

import Ibrahim.SpringBoot.model.Bill;
import Ibrahim.SpringBoot.model.Product;
import Ibrahim.SpringBoot.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    @Query("select p from Product p where p.bill.id=?1 " )
    public List<Product> getBillProducts(Integer id) ;
    @Query("select b from Bill b where b.agent.id=?1")
    public List<Bill> getbillsByAgent(Integer id);
    @Query("select b from Bill b where b.store.id=?1 " )
    List<Bill>getBillsByStore(Long id);
    @Query("select count(b) from Bill b where b.store.id=?1 " )
    Integer getbillsNumberByStore(Long id);
    @Query("select sum (b.total) from Bill b where b.store.id=?1 " )
    float getBillsTotal(Long id);
    @Query("select avg (b.total) from Bill b where b.store.id=?1 " )
    float getAvgBills(Long id);
    @Query("select max (b.total) from Bill b where b.store.id=?1 " )
    float getMaxTotalBill(Long id);
}
