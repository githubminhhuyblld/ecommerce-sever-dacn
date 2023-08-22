package nlu.edu.vn.ecommerce.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nlu.edu.vn.ecommerce.base.BaseEntity;
import nlu.edu.vn.ecommerce.models.enums.ActiveStatus;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Document("shop")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shop extends BaseEntity {
    private String id;
    private String name;
    private String image;
    private String address;
    private String description;
    private ActiveStatus activeStatus;

}
