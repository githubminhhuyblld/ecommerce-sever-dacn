package nlu.edu.vn.ecommerce.services.impl;

import nlu.edu.vn.ecommerce.exception.DuplicateRecordException;
import nlu.edu.vn.ecommerce.exception.ResponseObject;
import nlu.edu.vn.ecommerce.models.categories.Category;
import nlu.edu.vn.ecommerce.repositories.category.CategoryRepository;
import nlu.edu.vn.ecommerce.request.categories.CategoryRequest;
import nlu.edu.vn.ecommerce.services.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> findCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name);
    }

    @Override
    public ResponseObject insertCategory(CategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateRecordException("Tên thể loại đã tồn tại");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setIconUrl(request.getIconUrl());
        return new ResponseObject("oke", "Thêm thành công", categoryRepository.save(category));

    }
}
