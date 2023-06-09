package nlu.edu.vn.ecommerce.services.impl;

import nlu.edu.vn.ecommerce.repositories.UserRepository;
import nlu.edu.vn.ecommerce.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public boolean checkUsernameExits(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean checkEmailExits(String email) {
        return userRepository.existsByEmail(email);
    }
}
