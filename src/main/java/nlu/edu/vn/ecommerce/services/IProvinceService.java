package nlu.edu.vn.ecommerce.services;

import nlu.edu.vn.ecommerce.dto.province.ProvinceDTO;
import nlu.edu.vn.ecommerce.models.address.Province;
import nlu.edu.vn.ecommerce.request.address.ProvinceRequest;

import java.util.List;

public interface IProvinceService {
    List<ProvinceDTO> getAllProvinces();

    Province addProvinces(ProvinceRequest Province);



}
