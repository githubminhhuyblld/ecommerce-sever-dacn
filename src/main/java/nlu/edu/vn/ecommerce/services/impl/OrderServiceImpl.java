package nlu.edu.vn.ecommerce.services.impl;

import nlu.edu.vn.ecommerce.dto.CartDTO;
import nlu.edu.vn.ecommerce.dto.ShopDTO;
import nlu.edu.vn.ecommerce.models.*;
import nlu.edu.vn.ecommerce.repositories.CartRepository;
import nlu.edu.vn.ecommerce.repositories.OrderRepository;
import nlu.edu.vn.ecommerce.services.IOrderService;
import nlu.edu.vn.ecommerce.untils.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;

    @Override
    public boolean order(CartDTO cartDTO, String userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart != null) {
            List<Order> orders = new ArrayList<>();
            Map<String, List<CartItem>> shopCartItemsMap = new HashMap<>();


            for (CartItem cartItem : cartDTO.getCartItems()) {
                String shopId = cartItem.getShop().getId();
                List<CartItem> shopCartItems = shopCartItemsMap.getOrDefault(shopId, new ArrayList<>());
                shopCartItems.add(cartItem);
                shopCartItemsMap.put(shopId, shopCartItems);
            }


            for (Map.Entry<String, List<CartItem>> entry : shopCartItemsMap.entrySet()) {
                String shopId = entry.getKey();
                List<CartItem> shopCartItems = entry.getValue();

                Order order = new Order();
                order.setUserId(userId);
                order.setCartItems(shopCartItems);

                BigDecimal totalPrice = calculateTotalPrice(shopCartItems);
                order.setTotalPrice(totalPrice);
                order.setAddress(cartDTO.getAddress());
                order.setEmail(cartDTO.getEmail());
                order.setNumberPhone(cartDTO.getNumberPhone());
                order.setName(cartDTO.getName());
                order.setCreateAt(new Timestamp().getTime());
                order.setShopId(shopId);
                order.setOrderType(OrderType.SELL);
                order.setOrderStatus(OrderStatus.PROCESSING);
                order.setPaymentType(PaymentType.PAYMENT_ON_DELIVERY);

                orders.add(order);
            }

            orderRepository.saveAll(orders);
//            cartRepository.deleteByUserId(cartDTO.getUserId());
            return true;
        }
        return false;
    }

    private BigDecimal calculateTotalPrice(List<CartItem> cartItems) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            BigDecimal itemPrice = cartItem.getNewPrice().multiply(BigDecimal.valueOf(cartItem.getAmount()));
            totalPrice = totalPrice.add(itemPrice);
        }
        return totalPrice;
    }

    @Override
    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> getOrdersByShopId(String shopId) {
        List<Order> orders = orderRepository.findByShopId(shopId);
        if (orders.size() > 0) {
            return orders;
        }
        return null;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }


    @Override
    public boolean updateOrderStatusDelivered(String orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setOrderStatus(OrderStatus.DELIVERED);
            order.setUpdateAt(new Timestamp().getTime());
            orderRepository.save(order);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean updateOrderStatusCanceled(String orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setOrderStatus(OrderStatus.CANCELED);
            order.setUpdateAt(new Timestamp().getTime());
            orderRepository.save(order);
            return true;
        } else {
            return false;
        }
    }
}
