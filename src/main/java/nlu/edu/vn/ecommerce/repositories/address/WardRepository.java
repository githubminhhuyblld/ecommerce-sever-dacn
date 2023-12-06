package nlu.edu.vn.ecommerce.repositories.address;

import nlu.edu.vn.ecommerce.models.address.Ward;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WardRepository extends MongoRepository<Ward,String> {
    Ward findByName(String name);
}
