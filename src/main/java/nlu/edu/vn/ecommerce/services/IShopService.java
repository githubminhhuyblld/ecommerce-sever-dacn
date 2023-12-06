package nlu.edu.vn.ecommerce.services;

import nlu.edu.vn.ecommerce.models.shop.Shop;
import nlu.edu.vn.ecommerce.request.shop.ShopRequest;

public interface IShopService {
    Shop getShopById(String id);

    Shop registerShop(String userId, ShopRequest shopRequest);

    boolean checkExitsName(String name);

    Shop updateShopById(String id,ShopRequest shopRequest,String userId);


}
