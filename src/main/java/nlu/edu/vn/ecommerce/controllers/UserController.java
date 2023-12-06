package nlu.edu.vn.ecommerce.controllers;


import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import nlu.edu.vn.ecommerce.dto.auth.UserDTO;
import nlu.edu.vn.ecommerce.exception.ResponseObject;
import nlu.edu.vn.ecommerce.models.address.Address;
import nlu.edu.vn.ecommerce.models.user.User;
import nlu.edu.vn.ecommerce.repositories.user.UserRepository;

import nlu.edu.vn.ecommerce.request.address.AddressRequest;
import nlu.edu.vn.ecommerce.request.password.UpdatePasswordRequest;
import nlu.edu.vn.ecommerce.request.user.UpdateUserRequest;
import nlu.edu.vn.ecommerce.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IUserService iUserService;


    @GetMapping("/me")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<?> me(@ApiIgnore @AuthenticationPrincipal User user) {
        if (user != null) {
            return ResponseEntity.ok().body(UserDTO.from(user));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<?> getUserById(@PathVariable("id") String userId) {
        User user = userRepository.findById(userId).orElseGet(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(UserDTO.from(user));
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsernameExists(@RequestParam("username") String username) {
        boolean exists = iUserService.checkUsernameExits(username);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam("email") String email) {
        boolean exists = iUserService.checkEmailExits(email);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/{userId}/addresses")
    public ResponseEntity<?> addAddress(@PathVariable("userId") String userId, @RequestBody AddressRequest address) {
        iUserService.addAddressByUserId(userId, address);
        return ResponseEntity.ok().body(new ResponseObject("oke", "Thành công", null));
    }

    @PutMapping("/{userId}/addresses")
    public ResponseEntity<?> updateAddressById(@RequestParam String id, @PathVariable("userId") String userId, @RequestBody AddressRequest address) {
        iUserService.updateAddressById(userId, id, address);
        return ResponseEntity.ok().body(new ResponseObject("oke", "Update Thành công", null));
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<?> getAddressById(@PathVariable("addressId") String addressId) {
        Address address = iUserService.getAddressById(addressId);
        if (address != null) {
            return ResponseEntity.ok().body(address);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{userId}")
    @PreAuthorize("#user.id == #userId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<?> updateUser(@ApiIgnore @AuthenticationPrincipal User user, @PathVariable("userId") String userId, @RequestBody UpdateUserRequest updateUserRequest) {
        User updatedUser = iUserService.updateUser(userId, updateUserRequest);
        return ResponseEntity.ok().body(new ResponseObject("oke","update thành công",updatedUser));
    }
    @PostMapping("/update/password/{userId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    @PreAuthorize("#user.id == #userId")
    public ResponseEntity<?> updatePassword(@ApiIgnore @AuthenticationPrincipal User user,
                                            @PathVariable("userId") String userId,
                                            @RequestBody UpdatePasswordRequest request) {
        boolean updated = iUserService.updatePassword(userId, request.getOldPassword(), request.getNewPassword());
        if (updated) {
            return ResponseEntity.ok().body(new ResponseObject("oke", "Thành công", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseObject("failed", "Đổi mật khẩu không thành công", null));
        }
    }

}
