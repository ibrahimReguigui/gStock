package Ibrahim.SpringBoot.service;


import Ibrahim.SpringBoot.model.Agent;
import Ibrahim.SpringBoot.model.Store;

import java.util.List;
import java.util.Optional;

public interface StoreService {
    public List<Store> getStore();

    public Store getStoreById(long id);

    public void saveStore(Store store);

    public void deleteStore(long id);

    public Store getStoreByStoreNumber(String mobileNumber);

    Optional<Store> findStoreByStoreNumber(String storeNumber);

}
