package nlu.edu.vn.ecommerce.repositories.address;

import nlu.edu.vn.ecommerce.models.address.District;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends MongoRepository<District,String> {
    District findByName(String name);

}
