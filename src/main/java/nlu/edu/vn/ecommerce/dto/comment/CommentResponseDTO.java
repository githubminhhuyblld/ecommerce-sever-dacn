package nlu.edu.vn.ecommerce.dto.comment;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import nlu.edu.vn.ecommerce.base.BaseEntity;

@Getter
@Setter
public class CommentResponseDTO extends BaseEntity {
    private String id;
    private String username;
    private String avatar;
    private String productImage;
    private String comment;
    private int rating;
    private String color;
    private String size;



}
