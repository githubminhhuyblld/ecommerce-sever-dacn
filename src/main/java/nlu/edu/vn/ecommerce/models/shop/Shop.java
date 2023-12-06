package nlu.edu.vn.ecommerce.models.shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nlu.edu.vn.ecommerce.base.BaseEntity;
import nlu.edu.vn.ecommerce.models.enums.ActiveStatus;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a shop entity.
 */
@EqualsAndHashCode(callSuper = true)
@Document("shop")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shop extends BaseEntity {
    /**
     * The unique identifier for the shop.
     */
    private String id;

    /**
     * The name of the shop.
     */
    private String name;

    /**
     * The image URL of the shop.
     */
    private String image;

    /**
     * The address of the shop.
     */
    private String address;

    /**
     * The description of the shop.
     */
    private String description;

    /**
     * The active status of the shop.
     */
    private ActiveStatus activeStatus;
}
