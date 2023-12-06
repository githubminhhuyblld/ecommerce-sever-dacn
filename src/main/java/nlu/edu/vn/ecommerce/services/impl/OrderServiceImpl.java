package nlu.edu.vn.ecommerce.services.impl;

import nlu.edu.vn.ecommerce.dto.CartDTO;
import nlu.edu.vn.ecommerce.dto.OrderStatisticsDTO;
import nlu.edu.vn.ecommerce.exception.NotFoundException;
import nlu.edu.vn.ecommerce.models.*;
import nlu.edu.vn.ecommerce.models.enums.*;
import nlu.edu.vn.ecommerce.repositories.*;
import nlu.edu.vn.ecommerce.services.IOrderService;
import nlu.edu.vn.ecommerce.untils.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderManager orderManager;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProductRepository productRepository;

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
                LocalDateTime startDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(order.getCreateAt()), ZoneId.systemDefault());
                LocalDateTime endDate = startDate.plus(1, ChronoUnit.WEEKS);
                order.setEndDate(endDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
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
            LocalDateTime startDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(order.getCreateAt()), ZoneId.systemDefault());
            LocalDateTime endDate = startDate.plus(1, ChronoUnit.WEEKS);
            order.setEndDate(endDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
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

    private BigDecimal calculateTotalSales(List<Order> orders) {
        BigDecimal totalSales = BigDecimal.ZERO;
        for (Order order : orders) {
            totalSales = totalSales.add(order.getTotalPrice());
        }
        return totalSales;
    }

    @Override
    public List<OrderStatisticsDTO> getOrdersByWeek(String shopId) {
        Optional<Shop> shop = shopRepository.findById(shopId);
        if (shop.isEmpty()) {
            throw new NotFoundException("Shop doesn't exist");
        }
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endDate = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        List<OrderStatisticsDTO> orderStatisticsList = new ArrayList<>();
        while (!startDate.isAfter(endDate)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String fromTo = startDate.format(formatter);
            List<Order> canceledOrders = orderManager.findOrdersCanceledByStartDate(shopId, startDate);
            List<Order> deliveredOrders = orderManager.findOrdersDeliveredByStartDate(shopId, startDate);
            BigDecimal dailyTotalSale = calculateTotalSales(deliveredOrders);
            OrderStatisticsDTO orderStatisticsDTO = new OrderStatisticsDTO();
            orderStatisticsDTO.setFromTo(fromTo);
            orderStatisticsDTO.setCancelOrder(canceledOrders);
            orderStatisticsDTO.setSoldOrder(deliveredOrders);
            orderStatisticsDTO.setTotalSale(dailyTotalSale);
            orderStatisticsList.add(orderStatisticsDTO);
            startDate = startDate.plusDays(1);
        }
        return orderStatisticsList;
    }


    @Override
    public List<OrderStatisticsDTO> getOrdersByMonth(String shopId) {
        Optional<Shop> shop = shopRepository.findById(shopId);
        if (shop.isEmpty()) {
            throw new NotFoundException("Shop doesn't exist");
        }
        LocalDate today = LocalDate.now();
        YearMonth yearMonth = YearMonth.from(today);
        int numberOfWeeksInMonth = yearMonth.lengthOfMonth() / 7;
        List<OrderStatisticsDTO> monthlyStatistics = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (int week = 1; week <= numberOfWeeksInMonth; week++) {
            LocalDate startOfWeek = yearMonth.atDay(1).plusDays((week - 1) * 7);
            LocalDate endOfWeek = startOfWeek.plusDays(6);
            String fromTo = startOfWeek.format(formatter) + " - " + endOfWeek.format(formatter);
            List<Order> canceledOrders = orderManager.findOrdersCanceled(shopId, startOfWeek, endOfWeek);
            List<Order> deliveredOrders = orderManager.findOrdersDelivered(shopId, startOfWeek, endOfWeek);
            BigDecimal dailyTotalSale = calculateTotalSales(deliveredOrders);
            OrderStatisticsDTO orderStatisticsDTO = new OrderStatisticsDTO();
            orderStatisticsDTO.setFromTo(fromTo);
            orderStatisticsDTO.setCancelOrder(canceledOrders);
            orderStatisticsDTO.setSoldOrder(deliveredOrders);
            orderStatisticsDTO.setTotalSale(dailyTotalSale);
            monthlyStatistics.add(orderStatisticsDTO);
        }
        return monthlyStatistics;
    }


    @Override
    public List<OrderStatisticsDTO> getOrdersBySixMonth(String shopId) {
        LocalDate today = LocalDate.now();
        LocalDate sixMonthsAgo = today.minusMonths(6);

        List<OrderStatisticsDTO> monthlyStatistics = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        while (sixMonthsAgo.isBefore(today)) {
            LocalDate startOfMonth = sixMonthsAgo.with(TemporalAdjusters.firstDayOfMonth());
            LocalDate endOfMonth = sixMonthsAgo.with(TemporalAdjusters.lastDayOfMonth());

            String fromTo = startOfMonth.format(formatter) + " - " + endOfMonth.format(formatter);
            List<Order> canceledOrders = orderManager.findOrdersCanceled(shopId, startOfMonth, endOfMonth);
            List<Order> deliveredOrders = orderManager.findOrdersDelivered(shopId, startOfMonth, endOfMonth);
            BigDecimal dailyTotalSale = calculateTotalSales(deliveredOrders);
            OrderStatisticsDTO orderStatisticsDTO = new OrderStatisticsDTO();
            orderStatisticsDTO.setFromTo(fromTo);
            orderStatisticsDTO.setCancelOrder(canceledOrders);
            orderStatisticsDTO.setSoldOrder(deliveredOrders);
            orderStatisticsDTO.setTotalSale(dailyTotalSale);
            monthlyStatistics.add(orderStatisticsDTO);

            sixMonthsAgo = sixMonthsAgo.plusMonths(1);
        }

        return monthlyStatistics;
    }


    @Override
    public Page<Order> getOrdersForUser(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createAt").descending());

        LocalDate threeDaysAgoLocalDate = LocalDate.now().minusDays(3);

        List<Order> originalOrders = orderManager.findOrdersForUser(userId, threeDaysAgoLocalDate);
        List<Order> splitOrders = splitIntoSeparateOrders(originalOrders);

        int fromIndex = pageable.getPageNumber() * pageable.getPageSize();
        int toIndex = Math.min(fromIndex + pageable.getPageSize(), splitOrders.size());
        List<Order> ordersToReturn = splitOrders.subList(fromIndex, toIndex);

        return new PageImpl<>(ordersToReturn, pageable, splitOrders.size());
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

    //    @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(fixedRate = 30000) // Chạy tác vụ mỗi 30 giây
    public void autoDeliverOrders() {
        List<Order> readyOrders = orderRepository.findByOrderStatus(OrderStatus.READY);
        Date now = new Date();

        for (Order order : readyOrders) {
            long differenceInMillis = now.getTime() - order.getReadyAt().getTime();

            if (differenceInMillis >= 30000) {
                order.setOrderStatus(OrderStatus.DELIVERED);
                orderRepository.save(order);
                List<CartItem> cartItems = order.getCartItems();
                for (CartItem cartItem : cartItems) {
                    Optional<Product> productOptional = productRepository.findById(cartItem.getProductId());
                    if (productOptional.isPresent()) {
                        Product product = productOptional.get();
                        int currentQuantity = product.getQuantity();
                        int amountPurchased = cartItem.getAmount();
                        int newQuantity = currentQuantity - amountPurchased;
                        product.setQuantity(newQuantity);
                        productRepository.save(product);
                    } else {
                        throw new NotFoundException("Product not found");
                    }
                }
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
