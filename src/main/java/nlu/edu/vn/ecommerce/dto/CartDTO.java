package nlu.edu.vn.ecommerce.dto;

import lombok.Data;
import nlu.edu.vn.ecommerce.models.CartItem;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartDTO {

    private String userId;

    private String name;

    private String email;

    private String numberPhone;

    private List<CartItem> cartItems;

    private BigDecimal totalPrice;

    private String address;

}

