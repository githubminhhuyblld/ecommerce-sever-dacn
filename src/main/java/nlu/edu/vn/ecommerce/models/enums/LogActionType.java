package nlu.edu.vn.ecommerce.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogActionType {
    CREATE("Tạo mới",1),
    UPDATE("Cập nhật",10),
    DELETE("xóa",-1);

    private final String description;
    private final int code;

}
