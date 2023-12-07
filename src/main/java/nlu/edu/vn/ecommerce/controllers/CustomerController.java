package nlu.edu.vn.ecommerce.controllers;

import nlu.edu.vn.ecommerce.dto.customer.CustomerDTO;
import nlu.edu.vn.ecommerce.services.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    @Autowired
    private ICustomerService iCustomerService;

    @PostMapping("")
    public ResponseEntity<?> createCustomer(@RequestBody CustomerDTO customerDTO, @RequestParam("userId") String userId) {
        try {
            CustomerDTO createdCustomer = iCustomerService.createCustomer(customerDTO, userId);
            if (createdCustomer != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Create customer failed!.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Processing failed " + e.getMessage());
        }
    }

}
