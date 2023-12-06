package nlu.edu.vn.ecommerce.services.impl;

import nlu.edu.vn.ecommerce.exception.ResponseObject;
import nlu.edu.vn.ecommerce.models.role.Role;
import nlu.edu.vn.ecommerce.repositories.role.RoleRepository;
import nlu.edu.vn.ecommerce.request.role.RoleRequest;
import nlu.edu.vn.ecommerce.services.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private RoleRepository roleRepository;


    @Override
    public ResponseObject addRole(RoleRequest request) {
        if(roleRepository.existsByName(request.getName())){
            return new ResponseObject("FOUND", "Role đã tồn tại", null);
        }
        else{
            Role role = new Role();
            role.setName(request.getName());
            return new ResponseObject("ok", "thêm thành công", roleRepository.save(role));

        }

    }
}
