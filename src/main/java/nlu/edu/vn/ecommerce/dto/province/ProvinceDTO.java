package nlu.edu.vn.ecommerce.dto.province;

import lombok.Data;
import nlu.edu.vn.ecommerce.models.address.Province;

@Data
public class ProvinceDTO {
    private String id;
    private String name;

    public static ProvinceDTO fromProvince(Province province) {
        ProvinceDTO provinceDTO = new ProvinceDTO();
        provinceDTO.setId(province.getId());
        provinceDTO.setName(province.getName());
        return provinceDTO;
    }


}
