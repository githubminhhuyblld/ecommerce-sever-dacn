package nlu.edu.vn.ecommerce.services;

import nlu.edu.vn.ecommerce.dto.CommentDTO;

import java.util.List;

public interface ICommentService {
    public CommentDTO addComment(CommentDTO commentDTO);
    public List<CommentDTO> getCommentsByProductId(String productId);
}
