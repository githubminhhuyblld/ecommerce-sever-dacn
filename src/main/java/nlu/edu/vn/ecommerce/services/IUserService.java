package nlu.edu.vn.ecommerce.services;


import nlu.edu.vn.ecommerce.models.Address;
import nlu.edu.vn.ecommerce.models.User;
import nlu.edu.vn.ecommerce.request.AddressRequest;
import nlu.edu.vn.ecommerce.request.UpdateUserRequest;

public interface IUserService {
    boolean checkUsernameExits(String username);

    boolean checkEmailExits(String email);

    void addAddressByUserId(String userId, AddressRequest address);

    void updateAddressById(String userId, String addressId, AddressRequest addressRequest);
    Address getAddressById(String addressId);

    User updateUser(String userId, UpdateUserRequest updateUserRequest);
}
