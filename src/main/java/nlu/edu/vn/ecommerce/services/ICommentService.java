package nlu.edu.vn.ecommerce.services;

import nlu.edu.vn.ecommerce.dto.CommentDTO;
import nlu.edu.vn.ecommerce.dto.CommentResponseDTO;

import java.util.List;

public interface ICommentService {
     CommentDTO addComment(CommentDTO commentDTO);
     List<CommentResponseDTO> getCommentsByProductId(String productId);



}
