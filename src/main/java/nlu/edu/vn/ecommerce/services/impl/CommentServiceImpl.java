package nlu.edu.vn.ecommerce.services.impl;

import nlu.edu.vn.ecommerce.dto.CommentDTO;
import nlu.edu.vn.ecommerce.models.comments.Comment;
import nlu.edu.vn.ecommerce.services.ICommentService;
import nlu.edu.vn.ecommerce.untils.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements ICommentService {
    private final String COLLECTION = "comments";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public CommentDTO addComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setUserId(commentDTO.getUserId());
        comment.setProductId(commentDTO.getProductId());
        comment.setRating(commentDTO.getRating());
        comment.setContent(commentDTO.getContent());
        comment.setCreateAt(new Timestamp().getTime());
        mongoTemplate.save(comment, COLLECTION);
        return Comment.toCommentDTO(comment);
    }

    @Override
    public List<CommentDTO> getCommentsByProductId(String productId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("productId").is(productId));

        List<Comment> comments = mongoTemplate.find(query, Comment.class, COLLECTION);
        if(!comments.isEmpty()) {
            return comments.stream().map(Comment::toCommentDTO).collect(Collectors.toList());
        }
        return null;
    }
}
