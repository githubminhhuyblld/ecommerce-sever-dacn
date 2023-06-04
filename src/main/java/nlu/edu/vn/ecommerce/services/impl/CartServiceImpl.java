package nlu.edu.vn.ecommerce.services.impl;

import nlu.edu.vn.ecommerce.dto.ShopDTO;
import nlu.edu.vn.ecommerce.models.Cart;
import nlu.edu.vn.ecommerce.models.CartItem;
import nlu.edu.vn.ecommerce.models.Product;
import nlu.edu.vn.ecommerce.models.Shop;
import nlu.edu.vn.ecommerce.repositories.CartRepository;
import nlu.edu.vn.ecommerce.repositories.ProductRepository;
import nlu.edu.vn.ecommerce.repositories.ShopRepository;
import nlu.edu.vn.ecommerce.services.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProductRepository productRepository;
    @Override
    public void addToCart(String userId, CartItem cartItem) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setCartItems(new ArrayList<>());
        }
        List<CartItem> cartItems = cart.getCartItems();
        boolean found = false;
        for (CartItem item : cartItems) {
            if (item.getProductId().equals(cartItem.getProductId())) {
                item.setAmount(item.getAmount() + cartItem.getAmount());
                String productId = item.getProductId();
                Optional<Product> productOptional = productRepository.findById(productId);
                Product product = productOptional.get();
                Shop shop = product.getShop();
                item.setShop(new ShopDTO().fromShopDTO(shop));
                found = true;
                break;
            }
        }
        if (!found) {
            cartItems.add(cartItem);
        }
        BigDecimal totalPrice = cartItems.stream()
                .map(item -> item.getNewPrice().multiply(BigDecimal.valueOf(item.getAmount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(totalPrice);

        cart.setCartItems(cartItems);
        cartRepository.save(cart);
    }

    @Override
    public boolean removeItemFromCart(String userId, String productId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart != null) {
            List<CartItem> items = cart.getCartItems();
            Iterator<CartItem> iterator = items.iterator();
            while (iterator.hasNext()) {
                CartItem item = iterator.next();
                if (item.getProductId().equals(productId)) {
                    iterator.remove();
                    cart.setCartItems(items);

                    BigDecimal total = BigDecimal.ZERO;
                    for (CartItem i : items) {
                        BigDecimal itemTotal = i.getNewPrice().multiply(BigDecimal.valueOf(i.getAmount()));
                        total = total.add(itemTotal);
                    }
                    cart.setTotalPrice(total);
                    cart.setCartItems(items);

                    cartRepository.save(cart);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public CartItem updateCartItemQuantityByProductIdAndUserId(String productId, String userId, int amount) {
        Cart cart = cartRepository.findByUserId(userId);
        List<CartItem> cartItems = cart.getCartItems();
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            if (item.getProductId().equals(productId)) {
                item.setAmount(amount);
            }
            BigDecimal itemTotal = item.getNewPrice().multiply(BigDecimal.valueOf(item.getAmount()));
            total = total.add(itemTotal);
        }
        cart.setTotalPrice(total);
        cartRepository.save(cart);
        for (CartItem item : cartItems) {
            if (item.getProductId().equals(productId)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public List<Cart> getCartByUserId(String userId) {
        return cartRepository.getCartByUserId(userId);
    }
}
