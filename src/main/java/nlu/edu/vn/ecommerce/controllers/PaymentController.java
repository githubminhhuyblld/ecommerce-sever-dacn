package nlu.edu.vn.ecommerce.controllers;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import nlu.edu.vn.ecommerce.config.VNPayConfig;
import nlu.edu.vn.ecommerce.dto.PaymentDTO;
import nlu.edu.vn.ecommerce.dto.TransactionStatus;
import nlu.edu.vn.ecommerce.exception.NotFoundException;
import nlu.edu.vn.ecommerce.exception.ResponseObject;
import nlu.edu.vn.ecommerce.models.Order;
import nlu.edu.vn.ecommerce.models.enums.PaymentStatus;
import nlu.edu.vn.ecommerce.services.IOrderService;
import nlu.edu.vn.ecommerce.services.IPaymentService;
import nlu.edu.vn.ecommerce.services.impl.VNPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/payment")

public class PaymentController {
    @Autowired
    private IPaymentService vnPayService;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private MongoTemplate mongoTemplate;


    @PostMapping("/submitOrder")
    public ResponseEntity<?> submidOrder(@RequestParam("amount") int orderTotal,
                                         @RequestParam("orderInfo") String orderInfo) {
        String baseUrl = "http://localhost:3024";
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);

        return ResponseEntity.ok().body(new ResponseObject("oke", "Thành công", vnpayUrl));
    }

//    @GetMapping("/vnpay-payment")
//    public String GetMapping(HttpServletRequest request, Model model) {
//        int paymentStatus = vnPayService.orderReturn(request);
//
//        String orderInfo = request.getParameter("vnp_OrderInfo");
//        String paymentTime = request.getParameter("vnp_PayDate");
//        String transactionId = request.getParameter("vnp_TransactionNo");
//        String totalPrice = request.getParameter("vnp_Amount");
//
//        model.addAttribute("orderId", orderInfo);
//        model.addAttribute("totalPrice", totalPrice);
//        model.addAttribute("paymentTime", paymentTime);
//        model.addAttribute("transactionId", transactionId);
//
//        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
//    }

    @GetMapping("payment-info")
    public ResponseEntity<?> transaction(@RequestParam(value = "vnp_Amount") String amount,
                                         @RequestParam(value = "vnp_BankCode") String bankCode,
                                         @RequestParam(value = "vnp_OrderInfo") String orderInfo,
                                         @RequestParam(value = "vnp_ResponseCode") String responseCode) {
        TransactionStatus transactionStatus = new TransactionStatus();
        Optional<Order> order = iOrderService.findById(orderInfo);
        if (responseCode.equals("00")) {

            if(order.isEmpty()){
                throw new NotFoundException("Không tìm thấy order" +orderInfo);
            }
            order.get().setPaymentStatus(PaymentStatus.PAID);
            mongoTemplate.save(order);


            transactionStatus.setStatus("oke");
            transactionStatus.setMessage("successfully");
            transactionStatus.setData(null);
        } else {
            transactionStatus.setStatus("failed");
            transactionStatus.setMessage("failed");
            transactionStatus.setData(null);
            order.get().setPaymentStatus(PaymentStatus.FAILED);
            mongoTemplate.save(order);
        }
        return ResponseEntity.status(HttpStatus.OK).body(transactionStatus);

    }


}
