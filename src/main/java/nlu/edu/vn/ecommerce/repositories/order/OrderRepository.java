package nlu.edu.vn.ecommerce.repositories.order;

import nlu.edu.vn.ecommerce.models.order.Order;
import nlu.edu.vn.ecommerce.models.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order,String> {

    Page<Order> findByShopId(String shopId, Pageable pageable);
    Optional<Order> findById(String orderId);
    Page<Order> findByShopIdAndOrderStatus(String shopId, OrderStatus orderStatus, Pageable pageable);
    Page<Order>findByShopIdOrderByCreateAtDesc(String shopId, Pageable pageable);
    List<Order> findByOrderStatus(OrderStatus orderStatus);



}
