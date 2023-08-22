package nlu.edu.vn.ecommerce.controllers;

import lombok.extern.slf4j.Slf4j;
import nlu.edu.vn.ecommerce.dto.TransactionStatus;
import nlu.edu.vn.ecommerce.exception.NotFoundException;
import nlu.edu.vn.ecommerce.exception.ResponseObject;
import nlu.edu.vn.ecommerce.models.Order;
import nlu.edu.vn.ecommerce.models.enums.PaymentStatus;
import nlu.edu.vn.ecommerce.services.IOrderService;
import nlu.edu.vn.ecommerce.services.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/payment")

public class PaymentController {
    @Autowired
    private IPaymentService vnPayService;


    @Autowired
    private IPaymentService iPaymentService;


    @PostMapping("/create-payment")
    public ResponseEntity<?> submidOrder(@RequestParam("amount") int orderTotal,
                                         @RequestParam("orderInfo") String orderInfo) {
        String baseUrl = "http://localhost:3024";
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);

        return ResponseEntity.ok().body(new ResponseObject("oke", "Thành công", vnpayUrl));
    }

    @GetMapping("payment-info")
    public ResponseEntity<?> transaction(@RequestParam(value = "vnp_Amount") String amount,
                                         @RequestParam(value = "vnp_BankCode") String bankCode,
                                         @RequestParam(value = "vnp_OrderInfo") String orderInfo,
                                         @RequestParam(value = "vnp_ResponseCode") String responseCode) {

        TransactionStatus transactionStatus = iPaymentService.processPayment(amount, bankCode, orderInfo, responseCode);
        return ResponseEntity.status(HttpStatus.OK).body(transactionStatus);
    }


}