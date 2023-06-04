package nlu.edu.vn.ecommerce.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("role")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Role {
    @Id
    private String id;
    @NonNull
    private String name;

}
