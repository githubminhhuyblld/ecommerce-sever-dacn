package nlu.edu.vn.ecommerce.models.address;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Represents a province entity.
 */
@Document(collection = "provinces")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Province {
    /**
     * The unique identifier for the province.
     */
    @Id
    private String id;

    /**
     * The name of the province.
     */
    private String name;

    /**
     * The list of districts associated with the province.
     */
    @DBRef
    private List<District> districts;


}
