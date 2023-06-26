package nlu.edu.vn.ecommerce.repositories;

import nlu.edu.vn.ecommerce.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order,String> {
    List<Order> findByUserId(String userId);
    Page<Order> findByShopId(String shopId, Pageable pageable);
    Optional<Order> findById(String orderId);
}
