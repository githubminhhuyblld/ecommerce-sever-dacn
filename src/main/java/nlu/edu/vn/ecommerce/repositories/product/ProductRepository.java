package nlu.edu.vn.ecommerce.repositories.product;

import nlu.edu.vn.ecommerce.models.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product,String> {
    List<Product> findProductByName(String name);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategoryIdIn(List<String> categoryIds);
    Page<Product> findByCategoryIdIn(List<String> categoryIds, Pageable pageable);
    List<Product> findAllByOrderByIdDesc();
    List<Product> findByShopId(String shopId);
    boolean existsByCategoryId(String categoryId);
    Page<Product> findAllByOrderByNewPriceDesc(Pageable pageable);
    Page<Product> findAllByOrderByNewPriceAsc(Pageable pageable);


}
