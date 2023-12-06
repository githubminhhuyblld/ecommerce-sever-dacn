package nlu.edu.vn.ecommerce.models.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nlu.edu.vn.ecommerce.dto.shop.ShopDTO;
import nlu.edu.vn.ecommerce.models.enums.CommentStatus;

import java.math.BigDecimal;

/**
 * Represents a cart item entity.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItem {
    /**
     * The unique identifier for the product associated with the cart item.
     */
    private String productId;

    /**
     * The name of the product.
     */
    private String name;

    /**
     * The main image URL of the product.
     */
    private String mainImage;

    /**
     * The color of the product.
     */
    private String color;

    /**
     * The size of the product.
     */
    private String size;

    /**
     * The new price of the product.
     */
    private BigDecimal newPrice;

    /**
     * The old price of the product.
     */
    private BigDecimal oldPrice;

    /**
     * The description of the product.
     */
    private String description;

    /**
     * The quantity of the product in the cart.
     */
    private int amount;

    /**
     * The sale percentage for the product.
     */
    private int sale;

    /**
     * The category ID of the product.
     */
    private String categoryId;

    /**
     * The shop information associated with the product.
     */
    private ShopDTO shop;

    /**
     * The comment status for the product.
     */
    private CommentStatus comment;
}
