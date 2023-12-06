package nlu.edu.vn.ecommerce.dto.shop;

import lombok.Data;
import nlu.edu.vn.ecommerce.models.shop.Shop;

@Data
public class ShopDTO {
    private String id;

    private String name;

    private String description;

    private String address;

    private String image;

    public ShopDTO fromShopDTO(Shop shop) {
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setId(shop.getId());
        shopDTO.setName(shop.getName());
        shopDTO.setAddress(shop.getAddress());
        shopDTO.setDescription(shop.getDescription());
        shopDTO.setImage(shop.getImage());
        return shopDTO;
    }
    public ShopDTO toShopDTO() {
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setId(this.id);
        shopDTO.setName(this.name);
        shopDTO.setImage(this.image);
        shopDTO.setAddress(this.address);
        shopDTO.setDescription(this.description);
        return shopDTO;
    }
}
