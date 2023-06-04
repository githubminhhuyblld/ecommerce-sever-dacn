package nlu.edu.vn.ecommerce.services;

import nlu.edu.vn.ecommerce.dto.DistrictDTO;
import nlu.edu.vn.ecommerce.models.District;
import nlu.edu.vn.ecommerce.request.DistrictRequest;

import java.util.List;

public interface IDistrictService {
    District addDistrict(String provinceId,DistrictRequest request);
    List<DistrictDTO> getDistrictByProvinceId(String provinceId);

}
