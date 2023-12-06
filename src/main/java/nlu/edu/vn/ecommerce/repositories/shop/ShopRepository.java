package nlu.edu.vn.ecommerce.repositories.shop;

import nlu.edu.vn.ecommerce.models.shop.Shop;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends MongoRepository<Shop, String> {
    boolean existsByName(String name);
}
