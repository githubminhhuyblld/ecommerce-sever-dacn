package nlu.edu.vn.ecommerce.services.impl;

import nlu.edu.vn.ecommerce.dto.shop.ShopDTO;
import nlu.edu.vn.ecommerce.exception.*;
import nlu.edu.vn.ecommerce.models.cart.CartItem;
import nlu.edu.vn.ecommerce.models.enums.ActiveStatus;
import nlu.edu.vn.ecommerce.models.order.Order;
import nlu.edu.vn.ecommerce.models.product.Product;
import nlu.edu.vn.ecommerce.models.role.Role;
import nlu.edu.vn.ecommerce.models.shop.Shop;
import nlu.edu.vn.ecommerce.models.user.User;
import nlu.edu.vn.ecommerce.repositories.order.OrderRepository;
import nlu.edu.vn.ecommerce.repositories.product.ProductRepository;
import nlu.edu.vn.ecommerce.repositories.role.RoleRepository;
import nlu.edu.vn.ecommerce.repositories.shop.ShopRepository;
import nlu.edu.vn.ecommerce.repositories.user.UserRepository;
import nlu.edu.vn.ecommerce.request.shop.ShopRequest;
import nlu.edu.vn.ecommerce.services.IShopService;
import nlu.edu.vn.ecommerce.untils.Timestamp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

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
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Shop getShopById(String id) {
        Optional<Shop> optionalShop = shopRepository.findById(id);
        return optionalShop.orElse(null);
    }

    @Override
    public Shop registerShop(String userId, ShopRequest shopRequest) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new NotFoundException("Không tìm thấy user");
        }
        User user = optionalUser.get();
        if (user.getShopId() != null) {
            throw new DuplicateRecordException("User already has a shop");
        }
        if (shopRepository.existsByName(shopRequest.getName())) {
            throw new DuplicateRecordException("Tên shop đã tồn tại");
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

    @Override
    public boolean checkExitsName(String name) {
        return shopRepository.existsByName(name);
    }

    @Override
    public Shop updateShopById(String id, ShopRequest shopRequest, String userId) {
        Shop existingShop = shopRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Shop not found with id: " + id));

        BeanUtils.copyProperties(shopRequest, existingShop, "id");
        List<Product> products = productRepository.findByShopId(id);
        for (Product product : products) {
            product.setShop(existingShop);
            mongoTemplate.save(product);
        }
        List<Order> ordersUsers = orderRepository.findAll();
        for (Order order : ordersUsers) {
            for (CartItem cartItem : order.getCartItems()) {
                if (cartItem.getShop().getId().equals(existingShop.getId())) {
                    ShopDTO updatedShop = new ShopDTO();
                    updatedShop.setId(existingShop.getId());
                    updatedShop.setName(existingShop.getName());
                    updatedShop.setImage(existingShop.getImage());
                    updatedShop.setAddress(existingShop.getAddress());
                    updatedShop.setDescription(existingShop.getDescription());
                    cartItem.setShop(updatedShop);
                }
            }
            mongoTemplate.save(order);
        }
        return shopRepository.save(existingShop);
    }


}
