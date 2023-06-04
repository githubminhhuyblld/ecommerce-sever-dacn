package nlu.edu.vn.ecommerce.dto;

import lombok.Data;
import nlu.edu.vn.ecommerce.models.District;

@Data
public class DistrictDTO {
    private String id;
    private String name;

    public static DistrictDTO fromDistrict(District district) {
        DistrictDTO districtDTO = new DistrictDTO();
        districtDTO.setId(district.getId());
        districtDTO.setName(district.getName());
        return districtDTO;
    }
}
