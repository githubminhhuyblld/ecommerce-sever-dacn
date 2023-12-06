package nlu.edu.vn.ecommerce.services;

import nlu.edu.vn.ecommerce.models.cart.Cart;
import nlu.edu.vn.ecommerce.models.cart.CartItem;

import java.util.List;

public interface ICartService {
    void addToCart(String userId, CartItem cartItem);
    boolean removeItemFromCart(String userId, String productId);
    CartItem updateCartItemQuantityByProductIdAndUserId(String productId, String userId, int quantity);
    List<Cart> getCartByUserId(String userId);
}
