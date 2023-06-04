package nlu.edu.vn.ecommerce.services;

import nlu.edu.vn.ecommerce.dto.CartDTO;
import nlu.edu.vn.ecommerce.models.Cart;
import nlu.edu.vn.ecommerce.models.Order;
import nlu.edu.vn.ecommerce.models.OrderStatus;

import java.util.List;

public interface IOrderService {
    boolean order(CartDTO cartDTO,String userId);

    List<Order> getOrdersByUserId(String userId);

    List<Order> getOrdersByShopId(String shopId);

    List<Order> getAllOrders();

    boolean updateOrderStatusDelivered(String orderId);
    boolean updateOrderStatusCanceled(String orderId);

}
