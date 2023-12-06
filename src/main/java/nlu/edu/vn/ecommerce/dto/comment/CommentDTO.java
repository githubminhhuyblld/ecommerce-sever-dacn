package nlu.edu.vn.ecommerce.dto.comment;

import lombok.Data;

@Data
public class CommentDTO {
    private String productId;
    private String content;
    private String userId;
    private int rating;
    private String orderId;


}
