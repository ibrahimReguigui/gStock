package Ibrahim.SpringBoot.service;

import Ibrahim.SpringBoot.model.Bill;
import Ibrahim.SpringBoot.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BillServiceImp implements BillService {

    @Autowired
    private BillRepository bRepo;

    @Override
    public List<Bill> getAllBill() {
        return bRepo.findAll();
    }

    @Override
    public Bill getBillById(Integer billId) {
        return bRepo.findById(billId).get();
    }

    @Override
    public void deleteBill(Integer billId) {
        bRepo.deleteById(billId);
    }

    @Override
    public void saveBill(Bill newB) {
        bRepo.save(newB);
    }
}
