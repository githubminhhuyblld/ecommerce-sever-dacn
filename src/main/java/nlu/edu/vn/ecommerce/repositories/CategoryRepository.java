package nlu.edu.vn.ecommerce.repositories;

import nlu.edu.vn.ecommerce.models.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<Category,String> {
    List<Category> findCategoryByName(String name);
    boolean existsByNameIgnoreCase(String name);
    List<Category> findByNameContainingIgnoreCase(String name);
}
