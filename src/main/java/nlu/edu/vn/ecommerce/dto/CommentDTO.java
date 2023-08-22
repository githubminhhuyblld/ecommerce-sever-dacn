package nlu.edu.vn.ecommerce.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private String productId;
    private String content;
    private String userId;
    private int rating;

}
