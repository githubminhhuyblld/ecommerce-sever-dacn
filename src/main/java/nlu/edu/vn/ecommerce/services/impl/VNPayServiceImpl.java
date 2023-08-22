package nlu.edu.vn.ecommerce.services.impl;

import nlu.edu.vn.ecommerce.config.VNPayConfig;
import nlu.edu.vn.ecommerce.dto.TransactionStatus;
import nlu.edu.vn.ecommerce.exception.NotFoundException;
import nlu.edu.vn.ecommerce.models.Order;
import nlu.edu.vn.ecommerce.models.enums.PaymentStatus;
import nlu.edu.vn.ecommerce.repositories.OrderRepository;
import nlu.edu.vn.ecommerce.services.IOrderService;
import nlu.edu.vn.ecommerce.services.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VNPayServiceImpl implements IPaymentService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private MongoTemplate  mongoTemplate;
    @Override
    public String createOrder(int total, String orderInfor, String urlReturn) {
        Optional<Order> order = orderRepository.findById(orderInfor);
        if(order.isEmpty()){
            throw new NotFoundException("Không tìm thấy order" +orderInfor);
        }

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        String orderType = "order-type";

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(total*100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfor);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "vn";
        vnp_Params.put("vnp_Locale", locate);

        urlReturn += VNPayConfig.vnp_ReturnUrl;
        vnp_Params.put("vnp_ReturnUrl", urlReturn);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
        return paymentUrl;
    }

    @Override
    public TransactionStatus processPayment(String amount, String bankCode, String orderInfo, String responseCode) {
        TransactionStatus transactionStatus = new TransactionStatus();
        Optional<Order> order = iOrderService.findById(orderInfo);

        if (responseCode.equals("00")) {
            order.ifPresentOrElse(
                    ord -> {
                        ord.setPaymentStatus(PaymentStatus.PAID);
                        mongoTemplate.save(ord);
                        transactionStatus.setStatus("oke");
                        transactionStatus.setMessage("successfully");
                    },
                    () -> {
                        throw new NotFoundException("Không tìm thấy order" + orderInfo);
                    }
            );
        } else {
            order.ifPresent(ord -> {
                ord.setPaymentStatus(PaymentStatus.FAILED);
                mongoTemplate.save(ord);
                transactionStatus.setStatus("failed");
                transactionStatus.setMessage("failed");
            });
        }

        return transactionStatus;
    }
}
