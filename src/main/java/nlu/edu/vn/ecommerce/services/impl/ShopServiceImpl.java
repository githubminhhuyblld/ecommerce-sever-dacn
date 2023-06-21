package nlu.edu.vn.ecommerce.services.impl;

import nlu.edu.vn.ecommerce.exception.*;
import nlu.edu.vn.ecommerce.models.ActiveStatus;
import nlu.edu.vn.ecommerce.models.Role;
import nlu.edu.vn.ecommerce.models.Shop;
import nlu.edu.vn.ecommerce.models.User;
import nlu.edu.vn.ecommerce.repositories.RoleRepository;
import nlu.edu.vn.ecommerce.repositories.ShopRepository;
import nlu.edu.vn.ecommerce.repositories.UserRepository;
import nlu.edu.vn.ecommerce.request.ShopRequest;
import nlu.edu.vn.ecommerce.services.IShopService;
import nlu.edu.vn.ecommerce.untils.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShopServiceImpl implements IShopService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Shop getShopById(String id) {
        Optional<Shop> optionalShop = shopRepository.findById(id);
        return optionalShop.orElse(null);
    }

    @Override
    public Shop registerShop(String userId, ShopRequest shopRequest)  {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new NotFoundException("Không tìm thấy user");
        }
        User user = optionalUser.get();
        if (user.getShopId() != null) {
            throw new DuplicateRecordException("User already has a shop");
        }
        Shop shop = new Shop();
        shop.setName(shopRequest.getName());
        shop.setDescription(shopRequest.getDescription());
        shop.setImage(shopRequest.getImage());
        shop.setAddress(shopRequest.getAddress());
        shop.setActiveStatus(ActiveStatus.ACTIVE);
        shop.setCreateAt(new Timestamp().getTime());
        shop.setCreateBy(userId);
        shopRepository.save(shop);

        user.setShopId(shop.getId());
        List<Role> roles = user.getRoles();
        boolean isAdmin = roles.stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
            roles.add(roleAdmin);
            user.setRoles(roles);
        }
        userRepository.save(user);
        return shop;

    }
}
