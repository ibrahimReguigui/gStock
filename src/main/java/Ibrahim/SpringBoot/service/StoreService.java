package Ibrahim.SpringBoot.service;


import Ibrahim.SpringBoot.model.Store;

import java.util.List;

public interface StoreService {
    public List<Store> getStore();

    public Store getStoreById(long id);

    public void saveStore(Store store);

    public void deleteStore(long id);
}
