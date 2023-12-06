package nlu.edu.vn.ecommerce.services;

import nlu.edu.vn.ecommerce.dto.comment.CommentDTO;
import nlu.edu.vn.ecommerce.dto.comment.CommentResponseDTO;

import java.util.List;

public interface ICommentService {
     CommentDTO addComment(CommentDTO commentDTO);
     List<CommentResponseDTO> getCommentsByProductId(String productId);



}
