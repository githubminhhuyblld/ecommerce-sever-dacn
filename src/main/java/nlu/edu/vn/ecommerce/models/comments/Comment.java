package nlu.edu.vn.ecommerce.models.comments;

import lombok.*;
import nlu.edu.vn.ecommerce.base.BaseEntity;
import nlu.edu.vn.ecommerce.dto.CommentDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "comments")
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    private String id;

    private String productId;

    private String userId;

    private String content;

    private int rating;

    private String orderId;

    public static CommentDTO toCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setProductId(comment.getProductId());
        commentDTO.setUserId(comment.getUserId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setRating(comment.getRating());

        return commentDTO;
    }

    public static Comment toComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setProductId(commentDTO.getProductId());
        comment.setUserId(commentDTO.getUserId());
        comment.setContent(commentDTO.getContent());
        comment.setRating(commentDTO.getRating());
        return comment;
    }
}
