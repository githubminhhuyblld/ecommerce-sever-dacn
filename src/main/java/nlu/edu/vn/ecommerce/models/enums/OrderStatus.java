package nlu.edu.vn.ecommerce.models.enums;

import lombok.Getter;
import lombok.Setter;

public enum OrderStatus {
    READY("Đơn hàng đã xác nhận"),
    PROCESSING("Đơn hàng đang chờ xác nhận"),
    DELIVERED("Đơn hàng đã giao"),
    CANCELED("Đơn hàng đã hủy"),
    UNPAID("Đơn hàng chưa thanh toán"),
    SHIPPING("Đơn hàng đang giao"),
    RETURNED("Đơn hàng trả lại");




    @Getter
    @Setter
    private String description;


    OrderStatus(String description) {
        this.description = description;
    }
}
