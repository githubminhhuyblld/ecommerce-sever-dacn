package nlu.edu.vn.ecommerce.services;

import nlu.edu.vn.ecommerce.dto.ProvinceDTO;
import nlu.edu.vn.ecommerce.models.Province;
import nlu.edu.vn.ecommerce.request.ProvinceRequest;

import java.util.List;

public interface IProvinceService {
    List<ProvinceDTO> getAllProvinces();

    Province addProvinces(ProvinceRequest Province);



}
