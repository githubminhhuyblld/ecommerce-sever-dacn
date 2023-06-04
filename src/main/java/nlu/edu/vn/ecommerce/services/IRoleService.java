package nlu.edu.vn.ecommerce.services;

import nlu.edu.vn.ecommerce.exception.ResponseObject;
import nlu.edu.vn.ecommerce.models.Role;
import nlu.edu.vn.ecommerce.request.RoleRequest;

public interface IRoleService {
    ResponseObject addRole(RoleRequest request);
}
