package nlu.edu.vn.ecommerce.models.order;

import lombok.*;
import nlu.edu.vn.ecommerce.base.BaseEntity;
import nlu.edu.vn.ecommerce.models.cart.CartItem;
import nlu.edu.vn.ecommerce.models.enums.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Represents an order entity.
 */
@EqualsAndHashCode(callSuper = true)
@Document("order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseEntity {
    /**
     * The unique identifier for the order.
     */
    @Id
    private String id;

    /**
     * The user ID associated with the order.
     */
    private String userId;

    /**
     * The name of the customer who placed the order.
     */
    private String name;

    /**
     * The email address of the customer who placed the order.
     */
    private String email;

    /**
     * The phone number of the customer who placed the order.
     */
    private String numberPhone;

    /**
     * The list of cart items in the order.
     */
    private List<CartItem> cartItems;

    /**
     * The total price of the order.
     */
    private BigDecimal totalPrice;

    /**
     * The status of the order.
     */
    private OrderStatus orderStatus;

    /**
     * The type of the order.
     */
    private OrderType orderType;

    /**
     * The payment type for the order.
     */
    private PaymentType paymentType;

    /**
     * The shipping address for the order.
     */
    private String address;

    /**
     * The shop ID associated with the order.
     */
    private String shopId;

    /**
     * The payment status of the order.
     */
    private PaymentStatus paymentStatus;

    /**
     * The date when the order was canceled.
     */
    private Date canceledAt;

    /**
     * The date when the order was delivered.
     */
    private Date deliveredAt;

    /**
     * The date when the order is ready for pickup.
     */
    private Date readyAt;
}
