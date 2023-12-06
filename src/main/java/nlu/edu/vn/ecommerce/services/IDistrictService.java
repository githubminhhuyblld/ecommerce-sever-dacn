package nlu.edu.vn.ecommerce.services;

import nlu.edu.vn.ecommerce.dto.district.DistrictDTO;
import nlu.edu.vn.ecommerce.models.address.District;
import nlu.edu.vn.ecommerce.request.address.DistrictRequest;

import java.util.List;

public interface IDistrictService {
    District addDistrict(String provinceId,DistrictRequest request);
    List<DistrictDTO> getDistrictByProvinceId(String provinceId);

}
