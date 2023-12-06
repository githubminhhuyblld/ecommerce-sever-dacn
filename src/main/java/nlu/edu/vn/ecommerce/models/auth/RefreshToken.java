package nlu.edu.vn.ecommerce.models.auth;

import lombok.Data;
import nlu.edu.vn.ecommerce.models.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

/**
 * Represents a refresh token entity.
 */
@Document
@Data
public class RefreshToken {
    /**
     * The unique identifier for the refresh token.
     */
    @Id
    String id;

    /**
     * The user associated with the refresh token.
     */
    @DBRef(lazy = true)
    private User owner;
}

