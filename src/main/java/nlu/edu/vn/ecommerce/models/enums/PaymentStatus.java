package nlu.edu.vn.ecommerce.models.enums;


public enum PaymentStatus {
    PENDING("Đang chờ thanh toán"),
    PAID("Đã thanh toán"),
    FAILED("Thanh toán thất bại"),
    REFUNDED("Đã hoàn tiền ");

    private String description;


    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
