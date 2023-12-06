package nlu.edu.vn.ecommerce.models.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "wards")
public class Ward {
    /**
     * The unique identifier for the ward.
     */
    @Id
    private String id;

    /**
     * The name of the ward.
     */
    private String name;
}
