package nlu.edu.vn.ecommerce.controllers;

import nlu.edu.vn.ecommerce.dto.comment.CommentDTO;
import nlu.edu.vn.ecommerce.dto.comment.CommentResponseDTO;
import nlu.edu.vn.ecommerce.exception.ResponseObject;
import nlu.edu.vn.ecommerce.services.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
    @Autowired
    private ICommentService iCommentService;

    @PostMapping("")
    public ResponseEntity<?> postComment(@RequestBody CommentDTO commentDTO) {
        CommentDTO savedCommentDTO = iCommentService.addComment(commentDTO);
        if (savedCommentDTO == null) {
            ResponseEntity.badRequest().body(new ResponseObject("failed", "Thêm thất bại!", null));
        }
        return ResponseEntity.ok().body(new ResponseObject("oke", "Thêm thành công!", savedCommentDTO));
    }



    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getCommentsByProductId(@PathVariable String productId) {
        List<CommentResponseDTO> comments = iCommentService.getCommentsByProductId(productId);
        if (comments == null || comments.isEmpty()) {
            return ResponseEntity.ok().body(new ResponseObject("empty", "Không có bình luận nào cho sản phẩm này!", null));
        }
        return ResponseEntity.ok().body(new ResponseObject("success", "Lấy bình luận thành công!", comments));
    }
}
