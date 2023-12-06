package nlu.edu.vn.ecommerce.services.impl;

import nlu.edu.vn.ecommerce.dto.district.DistrictDTO;
import nlu.edu.vn.ecommerce.exception.DuplicateRecordException;
import nlu.edu.vn.ecommerce.exception.NotFoundException;
import nlu.edu.vn.ecommerce.models.address.District;
import nlu.edu.vn.ecommerce.models.address.Province;
import nlu.edu.vn.ecommerce.repositories.address.DistrictRepository;
import nlu.edu.vn.ecommerce.repositories.address.ProvinceRepository;
import nlu.edu.vn.ecommerce.request.address.DistrictRequest;
import nlu.edu.vn.ecommerce.services.IDistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DistrictServiceImpl implements IDistrictService {
    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private ProvinceRepository provinceRepository;


    @Override
    public District addDistrict(String provinceId, DistrictRequest request) {
        Optional<Province> optionalProvince = provinceRepository.findById(provinceId);
        if (!optionalProvince.isPresent()) {
            throw new NotFoundException("Không tồn tại id tỉnh này");
        }
        District existingDistrict = districtRepository.findByName(request.getName());
        if (existingDistrict != null) {
            throw new DuplicateRecordException("Tên Quận,huyện,thành phố đã tồn tại");
        }
        District district = new District();

        Province province = optionalProvince.get();
        List<District> districts = province.getDistricts();
        if (districts == null) {
            districts = new ArrayList<>();

        }
        district.setName(request.getName());
        districts.add(district);
        province.setDistricts(districts);
        districtRepository.save(district);
        provinceRepository.save(province);

        return district;
    }

    @Override
    public List<DistrictDTO> getDistrictByProvinceId(String provinceId) {
        Optional<Province> optionalProvince = provinceRepository.findById(provinceId);
        if (!optionalProvince.isPresent()) {
            throw new NotFoundException("Không tồn tại id tỉnh này");
        }

        List<District> districts = optionalProvince.get().getDistricts();
        List<DistrictDTO> districtDTOs = new ArrayList<>();

        for (District district : districts) {
            DistrictDTO districtDTO = DistrictDTO.fromDistrict(district);
            districtDTOs.add(districtDTO);
        }

        return districtDTOs;
    }


}
