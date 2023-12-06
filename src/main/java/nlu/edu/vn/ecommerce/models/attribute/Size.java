package nlu.edu.vn.ecommerce.models.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a size entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Size {
    /**
     * The unique identifier for the size.
     */
    private String id;

    /**
     * The name of the size.
     */
    private String name;
}
