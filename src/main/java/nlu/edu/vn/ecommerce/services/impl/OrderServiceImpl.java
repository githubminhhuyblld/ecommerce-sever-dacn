package nlu.edu.vn.ecommerce.services.impl;

import nlu.edu.vn.ecommerce.dto.CartDTO;
import nlu.edu.vn.ecommerce.models.*;
import nlu.edu.vn.ecommerce.models.enums.OrderStatus;
import nlu.edu.vn.ecommerce.models.enums.OrderType;
import nlu.edu.vn.ecommerce.models.enums.PaymentStatus;
import nlu.edu.vn.ecommerce.repositories.CartRepository;
import nlu.edu.vn.ecommerce.repositories.OrderRepository;
import nlu.edu.vn.ecommerce.services.IOrderService;
import nlu.edu.vn.ecommerce.untils.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public String order(CartDTO cartDTO, String userId) {
        List<Cart> cartList = cartRepository.getCartByUserId(userId);
        if (cartList != null && !cartList.isEmpty()) {
            Order order = new Order();
            order.setUserId(userId);
            BigDecimal totalOrderPrice = BigDecimal.ZERO;

            for (CartItem cartItem : cartDTO.getCartItems()) {
                BigDecimal itemTotalPrice = cartItem.getNewPrice().multiply(new BigDecimal(cartItem.getAmount()));
                totalOrderPrice = totalOrderPrice.add(itemTotalPrice);
            }

            order.setTotalPrice(totalOrderPrice);
            order.setCartItems(cartDTO.getCartItems());
            order.setAddress(cartDTO.getAddress());
            order.setEmail(cartDTO.getEmail());
            order.setNumberPhone(cartDTO.getNumberPhone());
            order.setName(cartDTO.getName());
            order.setCreateAt(new Timestamp().getTime());
            order.setShopId(cartDTO.getCartItems().get(0).getShop().getId());
            order.setOrderType(OrderType.SELL);
            order.setOrderStatus(OrderStatus.PROCESSING);
            order.setPaymentType(cartDTO.getPaymentType());
            order.setPaymentStatus(PaymentStatus.PENDING);

            Order savedOrder = orderRepository.save(order);

            // Delete only the cart items that were ordered
            for (CartItem orderedItem : cartDTO.getCartItems()) {
                cartRepository.deleteByUserIdAndCartItemProductId(userId, orderedItem.getProductId());
            }

            return savedOrder.getId();
        }
        return null;
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
        return orderRepository.findByUserIdOrderByCreateAtDesc(userId);
    }

    @Override
    public Page<Order> findByShopId(String shopId, Pageable pageable) {

        return orderRepository.findByShopId(shopId, pageable);
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

    @Override
    public boolean updateOrderStatusReady(String orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setOrderStatus(OrderStatus.READY);
            order.setUpdateAt(new Timestamp().getTime());
            orderRepository.save(order);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Page<Order> getOrdersByShopIdAndStatus(String shopId, String orderStatus, Pageable pageable) {
        OrderStatus status = OrderStatus.valueOf(orderStatus);
        return orderRepository.findByShopIdAndOrderStatus(shopId, status, pageable);
    }

    @Override
    public Page<Order> findByShopIdOrderByCreatedAtDesc(String shopId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createAt"));
        return orderRepository.findByShopIdOrderByCreateAtDesc(shopId, pageable);
    }

    public boolean deleteOrderById(String id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Order> findById(String id) {
        return orderRepository.findById(id);
    }

}
