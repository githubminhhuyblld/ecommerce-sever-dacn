package nlu.edu.vn.ecommerce.base;

import lombok.Data;

@Data
public abstract class BaseEntity {
    private long createAt;
    private long endDate;
    private long updateAt;
    private String createBy;
    private String updateBy;
}
