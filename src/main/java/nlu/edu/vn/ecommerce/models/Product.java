package nlu.edu.vn.ecommerce.models;

import lombok.*;
import nlu.edu.vn.ecommerce.base.BaseEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document("products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Product extends BaseEntity {
    @Id
    private String id;

    @NonNull
    private String name;

    @NonNull
    private String mainImage;

    private List<Image> images;
    @NonNull
    private BigDecimal newPrice;

    private BigDecimal oldPrice;

    @NonNull
    private String description;

    private List<Color> colors;

    private List<Size> sizes;

    @NonNull
    private int quantity;

    private int sale;

    @NonNull
    private String categoryId;

    @NonNull
    private Shop shop;


}
