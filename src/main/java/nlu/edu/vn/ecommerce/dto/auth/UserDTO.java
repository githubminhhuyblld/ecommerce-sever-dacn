package nlu.edu.vn.ecommerce.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nlu.edu.vn.ecommerce.exception.ResponseObject;
import nlu.edu.vn.ecommerce.models.address.Address;
import nlu.edu.vn.ecommerce.models.enums.GenderType;
import nlu.edu.vn.ecommerce.models.enums.ServiceType;
import nlu.edu.vn.ecommerce.models.role.Role;
import nlu.edu.vn.ecommerce.models.user.User;

import java.util.List;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String id;

    private String username;

    private String email;

    private String image;

    private String firstName;

    private String lastName;

    private String numberPhone;

    private long dateOfBirth;

    private GenderType gender;

    private ServiceType serviceType;

    private List<Role> roles;

    private String shopId ;

    private List<Address> address;


    public static ResponseObject from(User user) {
        return new ResponseObject("ok", "thành công", builder()
                .id(user.getId())
                .username(user.getUsername())
                .image(user.getImage())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .numberPhone(user.getNumberPhone())
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .serviceType(user.getServiceType())
                .roles(user.getRoles())
                .address(user.getAddress())
                .shopId(user.getShopId())
                .build());
    }

    public static UserDTO fromToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setImage(user.getImage());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setNumberPhone(user.getNumberPhone());
        userDTO.setGender(user.getGender());
        userDTO.setDateOfBirth(user.getDateOfBirth());
        userDTO.setRoles(user.getRoles());
        return userDTO;
    }

    public User toUser() {
        User user = new User();
        user.setId(this.getId());
        user.setUsername(this.getUsername());
        user.setEmail(this.getEmail());
        user.setImage(this.getImage());
        user.setFirstName(this.getFirstName());
        user.setLastName(this.getLastName());
        user.setNumberPhone(this.getNumberPhone());
        user.setDateOfBirth(this.getDateOfBirth());
        user.setGender(this.getGender());
        user.setRoles(this.getRoles());
        return user;
    }
}
