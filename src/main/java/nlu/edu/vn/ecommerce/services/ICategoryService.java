package nlu.edu.vn.ecommerce.services;

import nlu.edu.vn.ecommerce.exception.ResponseObject;
import nlu.edu.vn.ecommerce.models.Category;
import nlu.edu.vn.ecommerce.request.CategoryRequest;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategories();
    List<Category> findCategoryByName(String name);
    ResponseObject insertCategory(CategoryRequest request);

}
