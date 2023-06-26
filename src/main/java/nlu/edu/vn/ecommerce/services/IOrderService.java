package nlu.edu.vn.ecommerce.services;

import nlu.edu.vn.ecommerce.dto.CartDTO;
import nlu.edu.vn.ecommerce.models.Cart;
import nlu.edu.vn.ecommerce.models.Order;
import nlu.edu.vn.ecommerce.models.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
    boolean order(CartDTO cartDTO, String userId);

    List<Order> getOrdersByUserId(String userId);

    List<Order> getOrderByStatus(String userId, String status);

    Page<Order> findByShopId(String shopId, Pageable pageable);

    List<Order> getAllOrders();

    boolean updateOrderStatusDelivered(String orderId);

    boolean updateOrderStatusCanceled(String orderId);

    boolean updateOrderStatusReady(String orderId);


}
