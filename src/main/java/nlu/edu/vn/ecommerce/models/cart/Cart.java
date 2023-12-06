package nlu.edu.vn.ecommerce.models.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * Represents a cart entity.
 */
@Document("cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    /**
     * The unique identifier for the cart.
     */
    @Id
    private String id;

    /**
     * The user ID associated with the cart.
     */
    private String userId;

    /**
     * The total price of items in the cart.
     */
    private BigDecimal totalPrice;

    /**
     * The cart item associated with the cart.
     */
    private CartItem cartItem;

}
