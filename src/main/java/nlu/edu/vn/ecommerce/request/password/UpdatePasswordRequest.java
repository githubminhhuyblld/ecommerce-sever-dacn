package nlu.edu.vn.ecommerce.request.password;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
