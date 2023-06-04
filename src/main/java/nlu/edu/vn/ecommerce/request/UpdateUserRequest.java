package nlu.edu.vn.ecommerce.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateUserRequest {
    private String image;
    private String firstName;
    private String lastName;
    private String numberPhone;

}
