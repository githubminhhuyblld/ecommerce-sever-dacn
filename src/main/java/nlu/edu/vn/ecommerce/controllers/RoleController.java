package nlu.edu.vn.ecommerce.controllers;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import nlu.edu.vn.ecommerce.request.role.RoleRequest;
import nlu.edu.vn.ecommerce.services.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    @Autowired
    private IRoleService roleService;
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<?> addRole(@RequestBody RoleRequest role){
        return ResponseEntity.ok().body(roleService.addRole(role));
    }

}
