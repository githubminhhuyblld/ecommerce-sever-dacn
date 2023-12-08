package nlu.edu.vn.ecommerce.dto.customer;

import lombok.Data;
import nlu.edu.vn.ecommerce.models.enums.ActiveStatus;

@Data
public class CustomerDTO {
    private String name;

    private String email;

    private String numberPhone;

    private String address;

    private String shopId;

    private String userId;

}
