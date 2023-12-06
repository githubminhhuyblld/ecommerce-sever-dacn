package nlu.edu.vn.ecommerce.models.categories;


import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a category entity.
 */
@Document("category")
@Getter
@Setter
public class Category {
    /**
     * The unique identifier for the category.
     */
    @Id
    private String id;

    /**
     * The name of the category.
     */
    @NonNull
    private String name;

    /**
     * The URL of the category icon.
     */
    @NonNull
    private String iconUrl;

    /**
     * The description of the category.
     */
    private String description;
}
