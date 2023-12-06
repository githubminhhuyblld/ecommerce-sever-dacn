package nlu.edu.vn.ecommerce.services;

import nlu.edu.vn.ecommerce.models.product.Product;
import nlu.edu.vn.ecommerce.request.product.ProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

public interface IProductService {

    List<Product> findProductByName(String name);
    Product insertProduct(ProductRequest productDTO,String userId);
    List<Product> getAllProducts(int maxResult);

    Page<Product> getProducts(int page, int size);
    Page<Product> getProductsByCategoryId(String categoryId, Pageable pageable);

    boolean existsByCategoryId(String categoryId);
    boolean deleteProductById(String productId);
    Optional<Product> getProductById(String id);
    boolean updateProductById(String productId, ProductRequest productRequest,String userId);
    Page<Product> findProductBySearch(String search, Pageable pageable);
    List<Product> getAllProductByShopId(String shopId);
    Page<Product> getAllProductsSortedByPriceDescending(Pageable pageable);
    Page<Product> getAllProductsSortedByPriceAscending(Pageable pageable);

    void updateProduct(Product product);


}
