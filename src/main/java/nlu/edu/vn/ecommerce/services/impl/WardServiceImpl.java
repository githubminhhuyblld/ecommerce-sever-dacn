package nlu.edu.vn.ecommerce.services.impl;

import nlu.edu.vn.ecommerce.exception.DuplicateRecordException;
import nlu.edu.vn.ecommerce.exception.NotFoundException;
import nlu.edu.vn.ecommerce.models.address.District;
import nlu.edu.vn.ecommerce.models.address.Ward;
import nlu.edu.vn.ecommerce.repositories.address.DistrictRepository;
import nlu.edu.vn.ecommerce.repositories.address.WardRepository;
import nlu.edu.vn.ecommerce.request.address.WardRequest;
import nlu.edu.vn.ecommerce.services.IWardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WardServiceImpl implements IWardService {
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private WardRepository wardRepository;

    @Override
    public Ward addWards(String districtId,WardRequest request) {
        Optional<District> optionalDistrict = districtRepository.findById(districtId);
        if (!optionalDistrict.isPresent()) {
            throw new NotFoundException("Không tồn tại id quận,huyện,tp này");
        }
        Ward existingWard = wardRepository.findByName(request.getName());
        if (existingWard != null) {
            throw new DuplicateRecordException("Tên phường,xã đã tồn tại");
        }
        Ward ward = new Ward();
        District district = optionalDistrict.get();
        List<Ward> wards = district.getWards();
        if (wards == null) {
            wards = new ArrayList<>();
        }
        ward.setName(request.getName());
        wards.add(ward);
        district.setWards(wards);
        wardRepository.save(ward);
        districtRepository.save(district);
        return ward;

    }

    @Override
    public List<Ward> getWardsByDistrictId(String districtId) {
        Optional<District> optionalDistrict = districtRepository.findById(districtId);
        if (!optionalDistrict.isPresent()) {
            throw new NotFoundException("Không tồn tại id tp,quận,huyện này");
        }
        return optionalDistrict.get().getWards();

    }
}
