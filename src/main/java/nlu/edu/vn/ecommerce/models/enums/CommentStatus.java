package nlu.edu.vn.ecommerce.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum CommentStatus {
    COMMENTED("Đã được đánh giá"),
    UNCOMMENTED("Chưa được đánh giá");

    @Getter
    @Setter
    private String description;


}
