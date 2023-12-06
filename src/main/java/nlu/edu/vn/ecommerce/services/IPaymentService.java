package nlu.edu.vn.ecommerce.services;

public interface IPaymentService {
    String createOrder(int total, String orderInfor, String urlReturn);

    boolean processPayment(String amount, String bankCode, String orderInfo, String responseCode);

}
