package nlu.edu.vn.ecommerce.models;


import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("category")
@Getter
@Setter
public class Category {
    @Id
    private String id;
    @NonNull
    private String name;
    @NonNull
    private String iconUrl;
    private String description;

}
