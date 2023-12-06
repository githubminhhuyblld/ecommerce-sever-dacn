package nlu.edu.vn.ecommerce.models.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a role entity.
 */
@Document("role")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Role {
    /**
     * The unique identifier for the role.
     */
    @Id
    private String id;

    /**
     * The name of the role.
     */
    @NonNull
    private String name;
}
