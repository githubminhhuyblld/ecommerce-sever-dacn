package nlu.edu.vn.ecommerce.services;


import nlu.edu.vn.ecommerce.models.address.Address;
import nlu.edu.vn.ecommerce.models.user.User;
import nlu.edu.vn.ecommerce.request.address.AddressRequest;
import nlu.edu.vn.ecommerce.request.user.UpdateUserRequest;

public interface IUserService {
    boolean checkUsernameExits(String username);

    boolean checkEmailExits(String email);

    void addAddressByUserId(String userId, AddressRequest address);

    void updateAddressById(String userId, String addressId, AddressRequest addressRequest);
    Address getAddressById(String addressId);
    User updateUser(String userId, UpdateUserRequest updateUserRequest);

    boolean updatePassword(String userId,String oldPassword,String newPassword);
}
