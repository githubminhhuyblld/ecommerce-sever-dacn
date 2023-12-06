package nlu.edu.vn.ecommerce.controllers;

import nlu.edu.vn.ecommerce.exception.NotFoundException;
import nlu.edu.vn.ecommerce.exception.ResponseObject;
import nlu.edu.vn.ecommerce.models.categories.Category;
import nlu.edu.vn.ecommerce.request.categories.CategoryRequest;
import nlu.edu.vn.ecommerce.services.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @Autowired
    private ICategoryService iCategoryService;

    @GetMapping("")
    public ResponseEntity<?> getAllCategories() {
        List<Category> categoryList = iCategoryService.getAllCategories();
        if (categoryList == null) {
            throw new NotFoundException("Categories not found");
        } else {
            return ResponseEntity.ok().body(new ResponseObject("oke", "Thành công", categoryList));
        }

    }

    @PostMapping("")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest request) {
        return ResponseEntity.ok().body(iCategoryService.insertCategory(request));

    }

}
