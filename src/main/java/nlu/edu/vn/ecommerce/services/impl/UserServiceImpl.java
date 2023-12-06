package nlu.edu.vn.ecommerce.services.impl;

import nlu.edu.vn.ecommerce.exception.NotFoundException;
import nlu.edu.vn.ecommerce.models.address.Address;
import nlu.edu.vn.ecommerce.models.enums.AddressType;
import nlu.edu.vn.ecommerce.models.user.User;
import nlu.edu.vn.ecommerce.repositories.user.UserRepository;
import nlu.edu.vn.ecommerce.request.address.AddressRequest;
import nlu.edu.vn.ecommerce.request.user.UpdateUserRequest;
import nlu.edu.vn.ecommerce.services.IUserService;
import nlu.edu.vn.ecommerce.untils.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;


    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public boolean checkUsernameExits(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean checkEmailExits(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void addAddressByUserId(String userId, AddressRequest addressRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Address address = addressRequest.toAddress(addressRequest);
        address.setId(UUID.randomUUID().toString());
        address.setType(AddressType.NO_DEFAULT);

        List<Address> userAddresses = user.getAddress();
        if (userAddresses == null) {
            userAddresses = new ArrayList<>();
        }
        userAddresses.add(address);
        user.setAddress(userAddresses);
        userRepository.save(user);
    }

    @Override
    public void updateAddressById(String userId, String addressId, AddressRequest addressRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Address updatedAddress = user.getAddress()
                .stream()
                .filter(address -> address.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Address not found with id: " + addressId));

        updatedAddress.setProvinceId(addressRequest.getProvinceId());
        updatedAddress.setDistrictId(addressRequest.getDistrictId());
        updatedAddress.setWardId(addressRequest.getWardId());
        updatedAddress.setFullName(addressRequest.getFullName());
        updatedAddress.setNumberPhone(addressRequest.getNumberPhone());
        updatedAddress.setFullAddress(addressRequest.getFullAddress());
        updatedAddress.setAddress(addressRequest.getAddress());

        userRepository.save(user);
    }

    @Override
    public Address getAddressById(String addressId) {
        Optional<User> userOptional = userRepository.findByAddressId(addressId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Address> addresses = user.getAddress();
            if (addresses != null) {
                for (Address address : addresses) {
                    if (address.getId().equals(addressId)) {
                        return address;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public User updateUser(String userId, UpdateUserRequest updateUserRequest) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        existingUser.setImage(updateUserRequest.getImage());
        existingUser.setFirstName(updateUserRequest.getFirstName());
        existingUser.setLastName(updateUserRequest.getLastName());
        existingUser.setNumberPhone(updateUserRequest.getNumberPhone());
        existingUser.setDateOfBirth(updateUserRequest.getDateOfBirth());
        existingUser.setGender(updateUserRequest.getGender());
        existingUser.setUpdateAt(new Timestamp().getTime());

        return userRepository.save(existingUser);
    }

    @Override
    public boolean updatePassword(String userId, String oldPassword, String newPassword) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Không tìm thấy user");
        }
        if (!passwordEncoder.matches(oldPassword, user.get().getPassword())) {
            return false;
        }
        user.get().setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user.get());
        return true;
    }

}
