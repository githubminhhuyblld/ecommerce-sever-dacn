package nlu.edu.vn.ecommerce.models.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a color entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Color {
    /**
     * The unique identifier for the color.
     */
    private String id;

    /**
     * The name of the color.
     */
    private String colorName;
}
