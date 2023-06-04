package nlu.edu.vn.ecommerce.services;

import nlu.edu.vn.ecommerce.models.Ward;
import nlu.edu.vn.ecommerce.request.WardRequest;

import java.util.List;

public interface IWardService {
    Ward addWards(String districtId,WardRequest Province);
    List<Ward> getWardsByDistrictId(String id);

}
