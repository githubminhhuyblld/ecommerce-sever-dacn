package nlu.edu.vn.ecommerce.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    private String  id;

    private String provinceId;

    private String districtId;

    private String wardId;

    private String fullAddress;

    private String address;

    private String fullName;

    private String numberPhone;

    private AddressType type;
}
