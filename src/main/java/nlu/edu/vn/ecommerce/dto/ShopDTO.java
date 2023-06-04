package nlu.edu.vn.ecommerce.dto;

import lombok.Data;
import nlu.edu.vn.ecommerce.models.Shop;

@Data
public class ShopDTO {
    private String id;
    private String name;
    private String description;
    private String address;

    public ShopDTO fromShopDTO(Shop shop) {
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setId(shop.getId());
        shopDTO.setName(shop.getName());
        shopDTO.setAddress(shop.getAddress());
        shopDTO.setDescription(shop.getDescription());
        return shopDTO;
    }
}
