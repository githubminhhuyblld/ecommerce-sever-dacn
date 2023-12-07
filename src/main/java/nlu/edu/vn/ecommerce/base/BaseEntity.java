package nlu.edu.vn.ecommerce.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a base entity with common fields.
 *
 * @author Minh Huy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseEntity {
    /**
     * The timestamp when this entity was created.
     */
    private Long createAt;

    /**
     * The timestamp when this entity ends or expires.
     */
    private Long endDate;

    /**
     * The timestamp when this entity was last updated.
     */
    private Long updateAt;

    /**
     * The username or identifier of the user who created this entity.
     */
    private String createBy;

    /**
     * The username or identifier of the user who last updated this entity.
     */
    private String updateBy;
}
