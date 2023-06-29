package nlu.edu.vn.ecommerce.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nlu.edu.vn.ecommerce.models.GenderType;

@Getter
@Setter
@AllArgsConstructor
public class UpdateUserRequest {
    private String image;

    private String firstName;

    private String lastName;

    private String numberPhone;

    private long dateOfBirth;

    private GenderType gender;


}
