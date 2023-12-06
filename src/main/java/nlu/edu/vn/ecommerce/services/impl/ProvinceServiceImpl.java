package nlu.edu.vn.ecommerce.services.impl;

import nlu.edu.vn.ecommerce.dto.province.ProvinceDTO;
import nlu.edu.vn.ecommerce.exception.DuplicateRecordException;
import nlu.edu.vn.ecommerce.models.address.Province;
import nlu.edu.vn.ecommerce.repositories.address.ProvinceRepository;
import nlu.edu.vn.ecommerce.request.address.ProvinceRequest;
import nlu.edu.vn.ecommerce.services.IProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProvinceServiceImpl implements IProvinceService {

    @Autowired
    private ProvinceRepository provinceRepository;

    @Override
    public List<ProvinceDTO> getAllProvinces() {
        List<Province> provinces = provinceRepository.findAll();
        List<ProvinceDTO> provinceDTOs = new ArrayList<>();
        for (Province province : provinces) {
            ProvinceDTO provinceDTO = ProvinceDTO.fromProvince(province);
            provinceDTOs.add(provinceDTO);
        }
        return provinceDTOs;
    }

    @Override
    public Province addProvinces(ProvinceRequest request) {
        Province existingProvince = provinceRepository.findByName(request.getName());
        if (existingProvince != null) {
            throw new DuplicateRecordException("Tên tỉnh đã tồn tại");
        }
        Province province = new Province();
        province.setName(request.getName());
        return provinceRepository.save(province);
    }


}
