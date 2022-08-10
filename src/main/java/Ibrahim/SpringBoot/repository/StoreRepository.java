package Ibrahim.SpringBoot.repository;


import Ibrahim.SpringBoot.model.Agent;
import Ibrahim.SpringBoot.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Store getStoreByStoreNumber(String mobileNumber);

    Optional<Store> findStoreByStoreNumber(String storeNumber);
}
