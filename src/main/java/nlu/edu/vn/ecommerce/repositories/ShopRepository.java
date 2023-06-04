package nlu.edu.vn.ecommerce.repositories;

import nlu.edu.vn.ecommerce.models.Shop;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends MongoRepository<Shop,String> {

}
