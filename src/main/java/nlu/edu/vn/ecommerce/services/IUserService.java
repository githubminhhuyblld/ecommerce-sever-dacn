package nlu.edu.vn.ecommerce.services;



import nlu.edu.vn.ecommerce.request.AddressRequest;

public interface IUserService {
    boolean checkUsernameExits(String username);
    boolean checkEmailExits(String email);

    void addAddressByUserId(String userId, AddressRequest address);
}
