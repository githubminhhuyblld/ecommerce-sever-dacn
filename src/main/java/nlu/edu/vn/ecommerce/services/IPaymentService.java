package nlu.edu.vn.ecommerce.services;

import nlu.edu.vn.ecommerce.dto.TransactionStatus;

public interface IPaymentService {
    public String createOrder(int total, String orderInfor, String urlReturn);

    boolean processPayment(String amount, String bankCode, String orderInfo, String responseCode);

}
