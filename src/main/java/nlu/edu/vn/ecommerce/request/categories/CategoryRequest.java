package nlu.edu.vn.ecommerce.request.categories;

import lombok.Data;
import lombok.NonNull;

@Data
public class CategoryRequest {
    @NonNull
    private String name;
    @NonNull
    private String iconUrl;
    private String description;
}
