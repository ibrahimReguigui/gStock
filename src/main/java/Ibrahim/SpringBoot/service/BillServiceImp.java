package Ibrahim.SpringBoot.service;

import Ibrahim.SpringBoot.model.Bill;
import Ibrahim.SpringBoot.model.Product;
import Ibrahim.SpringBoot.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BillServiceImp implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Override
    public List<Bill> getAllBill() {
        return billRepository.findAll();
    }

    @Override
    public Bill getBillById(Integer billId) {
        return billRepository.findById(billId).get();
    }

    @Override
    public void deleteBill(Integer billId) {
        billRepository.deleteById(billId);
    }

    @Override
    public void saveBill(Bill newB) {
        billRepository.save(newB);
    }
    @Override
    public List<Product> getBillProducts(Integer id){
        return billRepository.getBillProducts(id);
    }

    public Integer getbillsNumberByStore(Long id){
        return billRepository.getbillsNumberByStore(id);
    };

    public float getBillsTotal(Long id){
        return billRepository.getBillsTotal(id);
    };
    public float getAvgBills(Long id){
        return billRepository.getAvgBills(id);
    };
    public float getMaxTotalBill(Long id){
        return billRepository.getMaxTotalBill(id);
    };
}
