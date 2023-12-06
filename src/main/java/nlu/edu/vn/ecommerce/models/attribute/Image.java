package nlu.edu.vn.ecommerce.models.attribute;

import lombok.*;

/**
 * Represents an image entity.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    /**
     * The unique identifier for the image.
     */
    private String id;

    /**
     * The URL of the image.
     */
    private String imgUrl;
}
