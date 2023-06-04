package nlu.edu.vn.ecommerce.services.impl;

import nlu.edu.vn.ecommerce.exception.NotFoundException;
import nlu.edu.vn.ecommerce.models.*;
import nlu.edu.vn.ecommerce.repositories.CategoryRepository;
import nlu.edu.vn.ecommerce.repositories.ProductRepository;
import nlu.edu.vn.ecommerce.repositories.ShopRepository;
import nlu.edu.vn.ecommerce.repositories.UserRepository;
import nlu.edu.vn.ecommerce.request.ProductRequest;
import nlu.edu.vn.ecommerce.services.IProductService;
import nlu.edu.vn.ecommerce.untils.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShopRepository shopRepository;


    @Override
    public List<Product> findProductByName(String name) {
        return productRepository.findProductByName(name);
    }

    @Override
    public Product insertProduct(ProductRequest request, String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Không tìm thấy user!");
        }
        User user = userOptional.get();
        Optional<Shop> optionalShop = shopRepository.findById(user.getShopId());
        if(optionalShop.isEmpty()){
            throw new NotFoundException("Không tìm thấy Shop!");
        }
        Shop shop = optionalShop.get();


        Product product = new Product();
        product.setName(request.getName());
        product.setMainImage(request.getMainImage());
        product.setNewPrice(request.getNewPrice());
        product.setOldPrice(request.getOldPrice());
        product.setDescription(request.getDescription());
        product.setQuantity(request.getQuantity());
        product.setSale(request.getSale());
        product.setCategoryId(request.getCategoryId());
        product.setCreateAt(new Timestamp().getTime());
        product.setCreateBy(userId);
        product.setShop(shop);

        List<Image> images = new ArrayList<>();
        if (request.getImages() != null) {
            for (Image image : request.getImages()) {
                Image newImage = new Image();
                newImage.setId(UUID.randomUUID().toString());
                newImage.setImgUrl(image.getImgUrl());
                images.add(newImage);
            }
            product.setImages(images);
        }

        List<Color> colors = new ArrayList<>();
        if (request.getColors() != null) {
            for (Color color : request.getColors()) {
                Color newColor = new Color();
                newColor.setId(UUID.randomUUID().toString());
                newColor.setColorName(color.getColorName());
                colors.add(newColor);
            }
            product.setColors(colors);
        }

        List<Size> sizes = new ArrayList<>();
        if (request.getSizes() != null) {
            for (Size size : request.getSizes()) {
                Size newSize = new Size();
                newSize.setId(UUID.randomUUID().toString());
                newSize.setName(size.getName());
                sizes.add(newSize);
            }
            product.setSizes(sizes);
        }


        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts(int maxResult) {
        List<Product> productList = productRepository.findAllByOrderByIdDesc();
        if (maxResult > 0) {
            return productList.stream().limit(maxResult).collect(Collectors.toList());
        }
        return productList;
    }

    @Override
    public Page<Product> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }

    @Override
    public List<Product> getProductsByCategoryId(String categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public boolean existsByCategoryId(String categoryId) {
        return productRepository.existsByCategoryId(categoryId);
    }

    @Override
    public boolean deleteProductById(String productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    @Override
    public boolean updateProductById(String productId, ProductRequest productRequest,String userId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();
            existingProduct.setName(productRequest.getName());

            List<Image> mappedImages = mapList(productRequest.getImages(), image -> {
                Image mappedImage = new Image();
                mappedImage.setId(UUID.randomUUID().toString());
                mappedImage.setImgUrl(image.getImgUrl());
                return mappedImage;
            });
            existingProduct.setImages(mappedImages);

            List<Color> mappedColors = mapList(productRequest.getColors(), color -> {
                Color mappedColor = new Color();
                mappedColor.setId(UUID.randomUUID().toString());
                mappedColor.setColorName(color.getColorName());
                return mappedColor;
            });
            existingProduct.setColors(mappedColors);

            List<Size> mappedSizes = mapList(productRequest.getSizes(), size -> {
                Size mappedSize = new Size();
                mappedSize.setId(UUID.randomUUID().toString());
                mappedSize.setName(size.getName());
                return mappedSize;
            });
            existingProduct.setSizes(mappedSizes);

            existingProduct.setNewPrice(productRequest.getNewPrice());
            existingProduct.setOldPrice(productRequest.getOldPrice());
            existingProduct.setDescription(productRequest.getDescription());
            existingProduct.setQuantity(productRequest.getQuantity());
            existingProduct.setSale(productRequest.getSale());
            existingProduct.setCategoryId(productRequest.getCategoryId());
            existingProduct.setUpdateAt(new Timestamp().getTime());
            existingProduct.setUpdateBy(userId);

            productRepository.save(existingProduct);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Product> findProductBySearch(String search, int maxResult) {
        List<Product> productList = productRepository.findByNameContainingIgnoreCase(search);
        if (!productList.isEmpty()) {
            return maxResult > 0 ? productList.stream().limit(maxResult).collect(Collectors.toList()) : productList;
        }

        List<Category> categoryList = categoryRepository.findByNameContainingIgnoreCase(search);
        if (!categoryList.isEmpty()) {
            List<String> categoryIds = categoryList.stream().map(Category::getId).collect(Collectors.toList());
            if (maxResult <= 0) {
                return productRepository.findByCategoryIdIn(categoryIds);
            } else {
                Page<Product> products = productRepository.findByCategoryIdIn(categoryIds, PageRequest.of(0, maxResult));
                return products.getContent();
            }
        }

        return Collections.emptyList();
    }

    @Override
    public List<Product> getAllProductByShopId(String shopId) {
        return productRepository.findByShopId(shopId);
    }

    private static <T> List<T> mapList(List<T> list, Function<T, T> mapper) {
        if (list == null) {
            return null;
        }

        List<T> mappedList = new ArrayList<>();
        for (T item : list) {
            T mappedItem = mapper.apply(item);
            mappedList.add(mappedItem);
        }
        return mappedList;
    }

}


