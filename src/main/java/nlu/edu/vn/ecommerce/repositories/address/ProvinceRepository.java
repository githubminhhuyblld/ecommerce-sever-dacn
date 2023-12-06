package nlu.edu.vn.ecommerce.repositories.address;

import nlu.edu.vn.ecommerce.models.address.Province;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProvinceRepository extends MongoRepository<Province,String> {
    Province findByName(String name);
    Optional<Province> findById(String id);
}
