package nlu.edu.vn.ecommerce.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PaymentDTO implements Serializable {
    private String status;
    private String message;
    private String URL;

}
