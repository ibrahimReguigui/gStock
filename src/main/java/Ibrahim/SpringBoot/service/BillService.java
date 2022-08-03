package Ibrahim.SpringBoot.service;

import Ibrahim.SpringBoot.model.Bill;

import java.util.List;

public interface BillService {
    public List<Bill> getAllBill();

    public Bill getBillById(Integer billId);

    public void deleteBill(Integer billId);

    public void saveBill(Bill newB);

}
