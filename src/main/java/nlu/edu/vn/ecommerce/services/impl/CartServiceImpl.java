package nlu.edu.vn.ecommerce.services.impl;

import nlu.edu.vn.ecommerce.dto.shop.ShopDTO;
import nlu.edu.vn.ecommerce.models.cart.Cart;
import nlu.edu.vn.ecommerce.models.cart.CartItem;
import nlu.edu.vn.ecommerce.models.product.Product;
import nlu.edu.vn.ecommerce.models.shop.Shop;
import nlu.edu.vn.ecommerce.repositories.cart.CartRepository;
import nlu.edu.vn.ecommerce.repositories.product.ProductRepository;
import nlu.edu.vn.ecommerce.repositories.shop.ShopRepository;
import nlu.edu.vn.ecommerce.services.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        Cart existingCart = cartRepository.findByUserIdAndCartItemProductId(userId, cartItem.getProductId());

        if (existingCart != null) {
            existingCart.getCartItem().setAmount(existingCart.getCartItem().getAmount() + cartItem.getAmount());
            cartItem = existingCart.getCartItem();
        } else {

            existingCart = new Cart();
            existingCart.setUserId(userId);
        }

        Optional<Product> productOptional = productRepository.findById(cartItem.getProductId());
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            Shop shop = product.getShop();
            cartItem.setShop(new ShopDTO().fromShopDTO(shop));
        }

        BigDecimal totalPrice = cartItem.getNewPrice().multiply(BigDecimal.valueOf(cartItem.getAmount()));
        existingCart.setTotalPrice(totalPrice);

        existingCart.setCartItem(cartItem);

        cartRepository.save(existingCart);
    }


    @Override
    public boolean removeItemFromCart(String userId, String cartId) {
        Cart cart = cartRepository.findByIdAndUserId(cartId, userId);
        if (cart != null) {
            cartRepository.deleteById(cart.getId());
            return true;
        }
        return false;
    }


    @Override
    public CartItem updateCartItemQuantityByProductIdAndUserId(String productId, String userId, int amount) {
        Cart cart = cartRepository.findByUserIdAndCartItemProductId(userId, productId);
        if (cart != null) {
            CartItem item = cart.getCartItem();
            item.setAmount(amount);
            BigDecimal itemTotal = item.getNewPrice().multiply(BigDecimal.valueOf(item.getAmount()));
            cart.setTotalPrice(itemTotal);
            cartRepository.save(cart);
            return item;
        }
        return null;
    }


    @Override
    public List<Cart> getCartByUserId(String userId) {
        return cartRepository.getCartByUserId(userId);
    }
}
