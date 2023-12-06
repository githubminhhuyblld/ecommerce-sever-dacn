package nlu.edu.vn.ecommerce.request.address;

import lombok.Data;
import nlu.edu.vn.ecommerce.models.address.Address;

@Data
public class AddressRequest {
    private String provinceId;

    private String districtId;

    private String wardId;

    private String fullAddress;

    private String address;

    private String fullName;

    private String numberPhone;



    public Address toAddress(AddressRequest addressRequest) {
        Address address = new Address();
        address.setProvinceId(addressRequest.getProvinceId());
        address.setDistrictId(addressRequest.getDistrictId());
        address.setWardId(addressRequest.getWardId());
        address.setFullName(addressRequest.getFullName());
        address.setNumberPhone(addressRequest.getNumberPhone());
        address.setFullAddress(addressRequest.getFullAddress());
        address.setAddress(addressRequest.getAddress());
        return address;
    }
}
