package nlu.edu.vn.ecommerce.services.impl;

import nlu.edu.vn.ecommerce.dto.comment.CommentDTO;
import nlu.edu.vn.ecommerce.dto.comment.CommentResponseDTO;
import nlu.edu.vn.ecommerce.models.cart.CartItem;
import nlu.edu.vn.ecommerce.models.order.Order;
import nlu.edu.vn.ecommerce.models.product.Product;
import nlu.edu.vn.ecommerce.models.user.User;
import nlu.edu.vn.ecommerce.models.comments.Comment;
import nlu.edu.vn.ecommerce.models.enums.CommentStatus;
import nlu.edu.vn.ecommerce.repositories.order.OrderRepository;
import nlu.edu.vn.ecommerce.repositories.product.ProductRepository;
import nlu.edu.vn.ecommerce.repositories.user.UserRepository;
import nlu.edu.vn.ecommerce.services.ICommentService;
import nlu.edu.vn.ecommerce.untils.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements ICommentService {
    private final String COLLECTION = "comments";

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public CommentDTO addComment(CommentDTO commentDTO) {
        if (commentDTO == null) {
            return null;
        }

        Comment comment = new Comment();
        comment.setUserId(commentDTO.getUserId());
        comment.setProductId(commentDTO.getProductId());
        comment.setOrderId(comment.getOrderId());
        comment.setRating(commentDTO.getRating());
        comment.setContent(commentDTO.getContent());
        comment.setCreateAt(new Timestamp().getTime());

        Optional<Order> optionalOrder = orderRepository.findById(commentDTO.getOrderId());
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();


            for (CartItem cartItem : order.getCartItems()) {
                if (cartItem.getProductId().equals(commentDTO.getProductId())) {
                    cartItem.setComment(CommentStatus.COMMENTED);
                    break;
                }
            }

            orderRepository.save(order);
        }

        mongoTemplate.save(comment, COLLECTION);
        return Comment.toCommentDTO(comment);
    }


    @Override
    public List<CommentResponseDTO> getCommentsByProductId(String productId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("productId").is(productId));
        List<Comment> comments = mongoTemplate.find(query, Comment.class, "comments"); // Assuming the collection name is "comments"

        List<CommentResponseDTO> commentResponseDTOs = new ArrayList<>();
        for (Comment comment : comments) {
            User user = userRepository.findById(comment.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + comment.getUserId()));
            Optional<Product> product = productRepository.findById(productId);


            CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
            commentResponseDTO.setId(comment.getId());
            commentResponseDTO.setUsername(user.getUsername());
            commentResponseDTO.setAvatar(user.getImage());
            commentResponseDTO.setComment(comment.getContent());
            commentResponseDTO.setRating(comment.getRating());
            commentResponseDTO.setProductImage(product.get().getMainImage());
            commentResponseDTO.setCreateAt(comment.getCreateAt());
//            commentResponseDTO.setSize(product.get().getS);

            commentResponseDTOs.add(commentResponseDTO);
        }

        return commentResponseDTOs;
    }

}
