package nlu.edu.vn.ecommerce.services.impl;

import nlu.edu.vn.ecommerce.dto.CartDTO;
import nlu.edu.vn.ecommerce.models.*;
import nlu.edu.vn.ecommerce.models.enums.*;
import nlu.edu.vn.ecommerce.repositories.CartRepository;
import nlu.edu.vn.ecommerce.repositories.OrderRepository;
import nlu.edu.vn.ecommerce.services.IOrderService;
import nlu.edu.vn.ecommerce.untils.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
                cartItem.setComment(CommentStatus.UNCOMMENTED);
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
            if (cartDTO.getPaymentType().equals(PaymentType.TRANSFER)) {
                order.setOrderStatus(OrderStatus.UNPAID);
            } else {
                order.setOrderStatus(OrderStatus.PROCESSING);
            }
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
            order.setCanceledAt(new Date());
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
            order.setReadyAt(new Date());
            order.setUpdateAt(new Timestamp().getTime());
            orderRepository.save(order);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateOrderStatusShipping(String orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setOrderStatus(OrderStatus.SHIPPING);
            order.setUpdateAt(new Timestamp().getTime());
            orderRepository.save(order);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateOrderStatusReturned(String orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setOrderStatus(OrderStatus.RETURNED);
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

    @Override
    public List<Order> getOrdersForUser(String userId) {
        Date threeDaysAgo = Date.from(Instant.now().minus(Duration.ofDays(3)));
        List<Order> originalOrders = orderRepository.findByUserIdAndOrderStatusNotOrOrderStatusAndCanceledAtAfter(
                userId,
                OrderStatus.CANCELED,
                OrderStatus.CANCELED,
                threeDaysAgo);

        return splitIntoSeparateOrders(originalOrders);
    }

    //Lấy ra sau đó tách nhiều sản phẩm thành nhiều order tương ứng
    private List<Order> splitIntoSeparateOrders(List<Order> originalOrders) {
        List<Order> splitOrders = new ArrayList<>();

        for (Order order : originalOrders) {
            for (CartItem cartItem : order.getCartItems()) {
                Order newOrder = new Order();
                newOrder.setId(order.getId());
                newOrder.setUserId(order.getUserId());
                newOrder.setName(order.getName());
                newOrder.setEmail(order.getEmail());
                newOrder.setNumberPhone(order.getNumberPhone());
                newOrder.setOrderStatus(order.getOrderStatus());
                newOrder.setOrderType(order.getOrderType());
                newOrder.setPaymentType(order.getPaymentType());
                newOrder.setAddress(order.getAddress());
                newOrder.setShopId(order.getShopId());
                newOrder.setPaymentStatus(order.getPaymentStatus());
                newOrder.setReadyAt(order.getReadyAt());
                newOrder.setCreateAt(order.getCreateAt());
                newOrder.setUpdateAt(order.getUpdateAt());

                BigDecimal totalPrice = cartItem.getNewPrice().multiply(new BigDecimal(cartItem.getAmount()));
                newOrder.setTotalPrice(totalPrice);

                newOrder.setCartItems(List.of(cartItem));

                splitOrders.add(newOrder);
            }
        }

        return splitOrders;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void autoDeliverOrders() {
        List<Order> readyOrders = orderRepository.findByOrderStatus(OrderStatus.READY);
        Date now = new Date();
        for (Order order : readyOrders) {
            long differenceInMillis = now.getTime() - order.getReadyAt().getTime();
            long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMillis);

            if (differenceInDays >= 3) {
                order.setOrderStatus(OrderStatus.DELIVERED);
                orderRepository.save(order);
            }
        }
    }
//    @Scheduled(fixedRate = 30000) // Chạy tác vụ mỗi 30 giây
//    public void autoDeliverOrders() {
//        List<Order> readyOrders = orderRepository.findByOrderStatus(OrderStatus.READY);
//        Date now = new Date();
//
//        for (Order order : readyOrders) {
//            long differenceInMillis = now.getTime() - order.getReadyAt().getTime();
//
//            if (differenceInMillis >= 30000) {
//                order.setOrderStatus(OrderStatus.DELIVERED);
//                orderRepository.save(order);
//            }
//        }
//    }

//    @Scheduled(fixedRate = 30000) // 30 giây
//    public void testGetOrdersForUser() {
//        String testUserId = "6475590403cc4931f154ad70";
//        List<Order> orders = getOrdersForUser(testUserId);
//
//        orders.forEach(order -> {
//            System.out.println(order);
//        });
//    }


}
