package Ibrahim.SpringBoot.service;

import Ibrahim.SpringBoot.model.Agent;
import Ibrahim.SpringBoot.model.Store;
import Ibrahim.SpringBoot.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreServiceImp implements StoreService {

    @Autowired
    private StoreRepository sRep;

    @Override
    public List<Store> getStore() {
        return sRep.findAll();
    }

    @Override
    public Store getStoreById(long id) {
        return sRep.findById(id).get();
    }

    @Override
    public void saveStore(Store store) {
        sRep.save(store);
    }

    @Override
    public void deleteStore(long id) {
        sRep.deleteById(id);
    }

    @Override
    public Store getStoreByStoreNumber(String mobileNumber){
        return sRep.getStoreByStoreNumber(mobileNumber);
    };


}
