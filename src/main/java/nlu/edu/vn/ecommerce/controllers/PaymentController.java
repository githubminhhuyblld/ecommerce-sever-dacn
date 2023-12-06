package nlu.edu.vn.ecommerce.controllers;

import lombok.extern.slf4j.Slf4j;
import nlu.edu.vn.ecommerce.exception.ResponseObject;
import nlu.edu.vn.ecommerce.services.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/payment")

public class PaymentController {
    @Autowired
    private IPaymentService vnPayService;
    @Autowired
    private IPaymentService iPaymentService;

    @PostMapping("/create-payment")
    public ResponseEntity<?> submitOrder(@RequestParam("amount") int orderTotal,
                                         @RequestParam("orderInfo") String orderInfo) {
        String baseUrl = "https://illustrious-twilight-80c533.netlify.app";
//        String baseUrl = "http://localhost:3024";
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);

        return ResponseEntity.ok().body(new ResponseObject("oke", "Thành công", vnpayUrl));
    }

    @GetMapping("/payment-info")
    public ResponseEntity<?> transaction(@RequestParam(value = "vnp_Amount") String amount,
                                         @RequestParam(value = "vnp_BankCode") String bankCode,
                                         @RequestParam(value = "vnp_OrderInfo") String orderInfo,
                                         @RequestParam(value = "vnp_ResponseCode") String responseCode) {
        boolean payment = iPaymentService.processPayment(amount, bankCode, orderInfo, responseCode);
        if (payment) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("oke", "Thanh toán thành công !", null));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("failed", "Thanh toán thất bại!", null));

        }
    }
}
