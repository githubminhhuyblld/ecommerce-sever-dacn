package nlu.edu.vn.ecommerce.repositories.cart;

import nlu.edu.vn.ecommerce.models.cart.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends MongoRepository<Cart,String> {
    List<Cart> getCartByUserId(String userId);
    Cart findByUserIdAndCartItemProductId(String userId, String productId);
    Cart findByIdAndUserId(String id,String userId);
    void deleteByUserIdAndCartItemProductId(String userId, String productId);


}
