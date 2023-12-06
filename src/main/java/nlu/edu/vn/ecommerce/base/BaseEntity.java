package nlu.edu.vn.ecommerce.base;

import lombok.Data;

/**
 * Represents a base entity with common fields.
 *
 * @author Minh Huy
 */
@Data
public abstract class BaseEntity {
    /**
     * The timestamp when this entity was created.
     */
    private long createAt;

    /**
     * The timestamp when this entity ends or expires.
     */
    private long endDate;

    /**
     * The timestamp when this entity was last updated.
     */
    private long updateAt;

    /**
     * The username or identifier of the user who created this entity.
     */
    private String createBy;

    /**
     * The username or identifier of the user who last updated this entity.
     */
    private String updateBy;
}
