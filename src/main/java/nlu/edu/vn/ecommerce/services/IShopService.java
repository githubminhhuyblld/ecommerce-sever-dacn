package nlu.edu.vn.ecommerce.services;

import nlu.edu.vn.ecommerce.exception.ErrorException;
import nlu.edu.vn.ecommerce.models.Shop;
import nlu.edu.vn.ecommerce.request.ShopRequest;

public interface IShopService {
    Shop getShopById(String id);
    Shop registerShop(String userId, ShopRequest shopRequest) ;
}
