package nlu.edu.vn.ecommerce.models.product;

import lombok.*;
import nlu.edu.vn.ecommerce.base.BaseEntity;
import nlu.edu.vn.ecommerce.models.attribute.Color;
import nlu.edu.vn.ecommerce.models.attribute.Image;
import nlu.edu.vn.ecommerce.models.shop.Shop;
import nlu.edu.vn.ecommerce.models.attribute.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents a product entity.
 */
@Document("products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Product extends BaseEntity {
    /**
     * The unique identifier for the product.
     */
    @Id
    private String id;

    /**
     * The name of the product.
     */
    @NonNull
    private String name;

    /**
     * The URL of the main image of the product.
     */
    @NonNull
    private String mainImage;

    /**
     * The list of images associated with the product.
     */
    private List<Image> images;

    /**
     * The new price of the product.
     */
    @NonNull
    private BigDecimal newPrice;

    /**
     * The old price of the product.
     */
    private BigDecimal oldPrice;

    /**
     * The description of the product.
     */
    @NonNull
    private String description;

    /**
     * The list of available colors for the product.
     */
    private List<Color> colors;

    /**
     * The list of available sizes for the product.
     */
    private List<Size> sizes;

    /**
     * The quantity of the product available in stock.
     */
    @NonNull
    private int quantity;

    /**
     * The sale percentage for the product.
     */
    private int sale;

    /**
     * The category ID associated with the product.
     */
    @NonNull
    private String categoryId;

    /**
     * The rating of the product.
     */
    private String rating;

    /**
     * The shop where the product is available.
     */
    @NonNull
    private Shop shop;
}
