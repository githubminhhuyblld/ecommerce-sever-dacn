package nlu.edu.vn.ecommerce.repositories;

import nlu.edu.vn.ecommerce.models.Ward;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WardRepository extends MongoRepository<Ward,String> {
    Ward findByName(String name);
}
