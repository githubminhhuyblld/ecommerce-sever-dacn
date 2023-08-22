package nlu.edu.vn.ecommerce.services;

public interface IPaymentService {
    public String createOrder(int total, String orderInfor, String urlReturn);
}
