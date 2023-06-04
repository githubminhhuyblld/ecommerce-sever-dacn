package nlu.edu.vn.ecommerce.repositories;

import nlu.edu.vn.ecommerce.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findByUsername(String username);
    User findByEmail(String email);
    Optional<User> findById(String id);
    boolean existsByUsername(String s);
    boolean existsByEmail(String s);
}
