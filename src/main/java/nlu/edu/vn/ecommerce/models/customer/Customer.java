package nlu.edu.vn.ecommerce.models.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nlu.edu.vn.ecommerce.base.BaseEntity;
import nlu.edu.vn.ecommerce.dto.customer.CustomerDTO;
import nlu.edu.vn.ecommerce.models.enums.ActiveStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends BaseEntity {
    @Id
    private String id;

    private String name;

    private String email;

    private String numberPhone;

    private String address;

    private String shopId;

    private String userId;

    private ActiveStatus status;

    public static CustomerDTO fromToDto(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setName(customer.getName());
        customerDTO.setNumberPhone(customer.getNumberPhone());
        customerDTO.setAddress(customer.getAddress());
        customerDTO.setShopId(customer.getShopId());
        customerDTO.setUserId(customer.getUserId());
        return customerDTO;
    }

}
