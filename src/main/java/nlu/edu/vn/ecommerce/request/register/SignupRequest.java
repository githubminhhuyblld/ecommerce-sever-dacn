package nlu.edu.vn.ecommerce.request.register;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignupRequest {
    @NotBlank
    @Size(message = "Tài khoản phải từ 3 kí tự trở lên và tối đa là 30 kí tự",min = 3, max = 30)
    private String username;
    @NotBlank
    @Size(max = 60)
    @Email(message = "Email đã tồn tại")
    private String email;
    @NotBlank
    @Size(message = "Mật khẩu phải từ 6 kí tự trở lên và tối đa là 20 kí tự",min = 6, max = 20)
    private String password;
    private String firstName;
    private String lastName;
    private String image;
    private String numberPhone;




}
