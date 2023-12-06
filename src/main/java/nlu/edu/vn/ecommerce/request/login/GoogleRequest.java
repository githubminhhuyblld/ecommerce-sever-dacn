package nlu.edu.vn.ecommerce.request.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GoogleRequest {
    @JsonProperty(required = false)
    private String email;

    @JsonProperty(required = false)
    private String familyName;

    @JsonProperty(required = false)
    private String givenName;

    @JsonProperty(required = false)
    private String image;




}
