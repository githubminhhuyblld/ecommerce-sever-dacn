package nlu.edu.vn.ecommerce.repositories;

import nlu.edu.vn.ecommerce.models.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends MongoRepository<Cart,String> {
    Cart findByUserId(String userId);
    List<Cart> getCartByUserId(String userId);
    void deleteByUserId(String userId);
    Cart findByUserIdAndCartItemProductId(String userId, String productId);
    void deleteByUserIdAndCartItemProductId(String userId, String productId);


}
