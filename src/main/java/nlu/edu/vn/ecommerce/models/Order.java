package nlu.edu.vn.ecommerce.models;

import lombok.*;
import nlu.edu.vn.ecommerce.base.BaseEntity;
import nlu.edu.vn.ecommerce.dto.ShopDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Document("order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseEntity {
    @Id
    private String id;

    private String userId;

    private String name;

    private String email;

    private String numberPhone;

    private List<CartItem> cartItems;

    private BigDecimal totalPrice;

    private OrderStatus orderStatus;

    private OrderType orderType;

    private PaymentType paymentType;

    private String address;

    private String  shopId;


}
