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
    private StoreRepository storeRepository;

    @Override
    public List<Store> getStore() {
        return storeRepository.findAll();
    }

    @Override
    public Store getStoreById(long id) {
        return storeRepository.findById(id).get();
    }

    @Override
    public void saveStore(Store store) {
        storeRepository.save(store);
    }

    @Override
    public void deleteStore(long id) {
        storeRepository.deleteById(id);
    }

    @Override
    public Store getStoreByStoreNumber(String mobileNumber) {
        return storeRepository.getStoreByStoreNumber(mobileNumber);
    }

    @Override
    public Optional<Store> findStoreByStoreNumber(String storeNumber) {
        return storeRepository.findStoreByStoreNumber(storeNumber);
    }
    public boolean numberExist(String storeNumber) {
        return findStoreByStoreNumber(storeNumber).isPresent();
    }

}
