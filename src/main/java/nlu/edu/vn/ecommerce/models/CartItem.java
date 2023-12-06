package nlu.edu.vn.ecommerce.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nlu.edu.vn.ecommerce.dto.ShopDTO;
import nlu.edu.vn.ecommerce.models.enums.CommentStatus;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItem {
    private String productId;

    private String name;

    private String mainImage;

    private String color;

    private String size;

    private BigDecimal newPrice;

    private BigDecimal oldPrice;

    private String description;

    private int amount;

    private int sale;

    private String categoryId;

    private ShopDTO shop;

    private CommentStatus comment;
}
