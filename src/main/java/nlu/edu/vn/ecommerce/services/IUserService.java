package nlu.edu.vn.ecommerce.services;

public interface IUserService {
    boolean checkUsernameExits(String username);
    boolean checkEmailExits(String email);
}
