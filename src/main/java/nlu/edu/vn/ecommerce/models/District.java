package nlu.edu.vn.ecommerce.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "districts")
public class District {
    @Id
    private String id;

    private String name;

    @DBRef
    private List<Ward> wards;


}
