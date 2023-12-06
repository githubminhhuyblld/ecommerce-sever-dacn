package nlu.edu.vn.ecommerce.services;

import nlu.edu.vn.ecommerce.exception.ResponseObject;
import nlu.edu.vn.ecommerce.request.role.RoleRequest;

public interface IRoleService {
    ResponseObject addRole(RoleRequest request);
}
