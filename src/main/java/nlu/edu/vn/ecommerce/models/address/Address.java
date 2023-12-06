package nlu.edu.vn.ecommerce.models.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nlu.edu.vn.ecommerce.models.enums.AddressType;
import org.springframework.data.annotation.Id;

/**
 * @author Minh Huy
 *
 * Represents an address entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    /**
     * The unique identifier for the address.
     */
    @Id
    private String id;

    /**
     * The identifier of the province where the address is located.
     */
    private String provinceId;

    /**
     * The identifier of the district where the address is located.
     */
    private String districtId;

    /**
     * The identifier of the ward where the address is located.
     */
    private String wardId;

    /**
     * The full address including province, district, and ward details.
     */
    private String fullAddress;

    /**
     * The specific address details (if any) apart from province, district, and ward.
     */
    private String address;

    /**
     * The full name associated with the address.
     */
    private String fullName;

    /**
     * The contact number associated with the address.
     */
    private String numberPhone;

    /**
     * The type of address (e.g., Home, Office, etc.).
     */
    private AddressType type;
}
