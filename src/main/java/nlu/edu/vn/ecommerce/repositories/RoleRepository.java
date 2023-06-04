package nlu.edu.vn.ecommerce.repositories;

import nlu.edu.vn.ecommerce.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<Role,String> {
    Role findByName(String name);
    boolean existsByName(String name);
}
