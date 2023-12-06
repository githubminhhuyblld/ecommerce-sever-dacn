package nlu.edu.vn.ecommerce.models.address;

import lombok.*;
import nlu.edu.vn.ecommerce.models.address.Ward;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Represents a district entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "districts")
public class District {
    /**
     * The unique identifier for the district.
     */
    @Id
    private String id;

    /**
     * The name of the district.
     */
    private String name;

    /**
     * The list of wards associated with the district.
     */
    @DBRef
    private List<Ward> wards;


}
