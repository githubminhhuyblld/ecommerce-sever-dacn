package nlu.edu.vn.ecommerce.services;

import nlu.edu.vn.ecommerce.dto.cart.CartDTO;
import nlu.edu.vn.ecommerce.dto.statistics.OrderStatisticsDTO;
import nlu.edu.vn.ecommerce.models.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    String order(CartDTO cartDTO,String userId);
    Page<Order> getOrdersForUser(String userId, int page, int size);
    Page<Order> findByShopId(String shopId, Pageable pageable);
    List<Order> getAllOrders();
    boolean updateOrderStatusDelivered(String orderId);
    boolean updateOrderStatusCanceled(String orderId);
    boolean updateOrderStatusReady(String orderId);
    boolean updateOrderStatusShipping(String orderId);
    boolean updateOrderStatusReturned(String orderId);
    Page<Order> getOrdersByShopIdAndStatus(String shopId, String orderStatus, Pageable pageable);
    Page<Order> findByShopIdOrderByCreatedAtDesc(String shopId, int page, int size);
    boolean deleteOrderById(String id);
    Optional<Order> findById(String id);
    List<OrderStatisticsDTO> getOrdersByWeek(String shopId);
    List<OrderStatisticsDTO> getOrdersByMonth(String shopId);
    List<OrderStatisticsDTO> getOrdersBySixMonth(String shopId);
    List<Order> findOrdersByShopAndEmail(String shopId,String email);

}
