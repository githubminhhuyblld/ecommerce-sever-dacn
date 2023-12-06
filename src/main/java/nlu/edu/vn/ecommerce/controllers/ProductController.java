package nlu.edu.vn.ecommerce.controllers;


import io.swagger.annotations.ApiOperation;
import nlu.edu.vn.ecommerce.exception.NotFoundException;
import nlu.edu.vn.ecommerce.exception.ResponseArray;
import nlu.edu.vn.ecommerce.exception.ResponseObject;
import nlu.edu.vn.ecommerce.models.product.Product;
import nlu.edu.vn.ecommerce.models.user.User;
import nlu.edu.vn.ecommerce.request.product.ProductRequest;
import nlu.edu.vn.ecommerce.services.IProductService;
import nlu.edu.vn.ecommerce.validate.ValidateParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    private IProductService iProductService;

    @GetMapping("")
    @ApiOperation(value = "Get all products", notes = "Get all products with optional max result", response = ResponseArray.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation", response = ResponseArray.class),
            @ApiResponse(code = 400, message = "Products not found", response = ResponseArray.class)
    })
    public ResponseEntity<?> getAllProducts(@RequestParam(name = "maxResult", defaultValue = "0") String maxResult,
                                            HttpServletRequest request) {
        if (!ValidateParameter.validateRequestParams(request, "maxResult")) {
            return ResponseEntity.badRequest().body("Invalid parameters provided or param value null");

        }
        int maxResultInt;
        try {
            maxResultInt = Integer.parseInt(maxResult);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid maxResult value");
        }
        int total = iProductService.getAllProducts(0).toArray().length;
        return ResponseEntity.ok().body(
                new ResponseArray(total, "oke", "thành công", iProductService.getAllProducts(maxResultInt))
        );

    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<?> insertProduct(@RequestBody ProductRequest request, @RequestParam("userId") String userId) {
        List<Product> products = iProductService.findProductByName(request.getName());
        Product product = iProductService.insertProduct(request, userId);
        return ResponseEntity.ok().body(new ResponseObject("oke", "Thành công", product));
    }

    @GetMapping("/pagination")
    @ApiOperation(value = "Get products pagination", notes = "Get products pagination", response = ResponseObject.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation", response = ResponseArray.class),
            @ApiResponse(code = 400, message = "Products not found", response = ResponseObject.class)
    })
    public ResponseEntity<?> getProducts(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok().body(new ResponseObject("oke", "Thành công", iProductService.getProducts(page, size)));
    }

    @GetMapping("/{categoryId}/category")
    public ResponseEntity<?> getProductsByCategory(
            @PathVariable String categoryId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = iProductService.getProductsByCategoryId(categoryId, pageable);

        if (products.hasContent()) {
            return ResponseEntity.ok(products);
        } else {
            return ResponseEntity.badRequest().body(new ResponseObject("Không tìm thấy sản phẩm", "", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findProductById(@PathVariable String id) {
        Optional<Product> product = iProductService.getProductById(id);
        if (product.isPresent()) {
            return ResponseEntity.ok().body(new ResponseObject("oke", "Thành công", product));
        } else {
            throw new NotFoundException("Không tìm thấy sản phẩm");
        }
    }

    @GetMapping("/{shopId}/shops")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    @PreAuthorize("#user.id == #userId")
    public ResponseEntity<?> GetAllProductByShopId(@ApiIgnore @AuthenticationPrincipal User user, @PathVariable("shopId") String shopId, @RequestParam String userId) {
        List<Product> products = iProductService.getAllProductByShopId(shopId);
        if (products.size() > 0) {
            return ResponseEntity.ok().body(new ResponseArray(products.size(), "oke", "thành công", products));
        } else {
            throw new NotFoundException("Không tìm thấy sản phẩm");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<?> updateProduct(@PathVariable("id") String productId, @RequestBody ProductRequest productRequest, @RequestParam String userId) {
        boolean isUpdated = iProductService.updateProductById(productId, productRequest, userId);
        if (isUpdated) {
            return ResponseEntity.ok().body(new ResponseObject("oke", "Thành công", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseObject("failed", "Thất bại", null));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("#user.id == #userId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<?> deleteProduct(@ApiIgnore @AuthenticationPrincipal User user, @PathVariable("id") String productId, @RequestParam String userId) {
        boolean isDeleted = iProductService.deleteProductById(productId);
        if (isDeleted) {
            return ResponseEntity.ok().body(new ResponseObject("oke", "Thành công", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseObject("failed", "Thất bại", null));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam(name = "search") String search,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = iProductService.findProductBySearch(search, pageable);

        if (products.hasContent()) {
            return ResponseEntity.ok(products);
        } else {
            throw new NotFoundException("Không tìm thấy sản phẩm");
        }
    }

    @GetMapping("/sorted-by-price-descending")
    public ResponseEntity<Page<Product>> getAllProductsSortedByPriceDescending(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "newPrice");
        Page<Product> products = iProductService.getAllProductsSortedByPriceDescending(pageable);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/sorted-by-price-ascending")
    public ResponseEntity<Page<Product>> getAllProductsSortedByPriceAscending(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "newPrice");
        Page<Product> products = iProductService.getAllProductsSortedByPriceAscending(pageable);
        return ResponseEntity.ok().body(products);
    }


}
