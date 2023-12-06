package nlu.edu.vn.ecommerce.models.comments;

import lombok.*;
import nlu.edu.vn.ecommerce.base.BaseEntity;
import nlu.edu.vn.ecommerce.dto.comment.CommentDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a comment entity.
 */
@Document(collection = "comments")
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {
    /**
     * The unique identifier for the comment.
     */
    @Id
    private String id;

    /**
     * The identifier of the product associated with the comment.
     */
    private String productId;

    /**
     * The identifier of the user who posted the comment.
     */
    private String userId;

    /**
     * The content of the comment.
     */
    private String content;

    /**
     * The rating given to the product in the comment.
     */
    private int rating;

    /**
     * The identifier of the order associated with the comment.
     */
    private String orderId;

    /**
     * Convert a Comment object to a CommentDTO object.
     *
     * @param comment The Comment object to be converted.
     *
     * @return A CommentDTO object representing the Comment.
     */
    public static CommentDTO toCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setProductId(comment.getProductId());
        commentDTO.setUserId(comment.getUserId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setRating(comment.getRating());

        return commentDTO;
    }

    /**
     * Convert a CommentDTO object to a Comment object.
     *
     * @param commentDTO The CommentDTO object to be converted.
     *
     * @return A Comment object representing the CommentDTO.
     */
    public static Comment toComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setProductId(commentDTO.getProductId());
        comment.setUserId(commentDTO.getUserId());
        comment.setContent(commentDTO.getContent());
        comment.setRating(commentDTO.getRating());
        return comment;
    }
}
