package nlu.edu.vn.ecommerce.models.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public enum PaymentType {
    TRANSFER("Banking", "Thanh toán bằng tài khoản"),
    PAYMENT_ON_DELIVERY("Thanh toán khi nhận hàng", "Thanh toán khi nhận hàng"),
    QR_CODE("QR code", "Thanh toán bằng QR code");
    private final String label;
    private final String description;

    PaymentType(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }



}
