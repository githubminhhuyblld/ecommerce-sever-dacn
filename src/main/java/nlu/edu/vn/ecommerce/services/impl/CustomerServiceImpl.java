package nlu.edu.vn.ecommerce.services.impl;

import nlu.edu.vn.ecommerce.dto.customer.CustomerDTO;
import nlu.edu.vn.ecommerce.models.customer.Customer;
import nlu.edu.vn.ecommerce.repositories.customer.CustomerManager;
import nlu.edu.vn.ecommerce.services.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements ICustomerService {
    @Autowired
    private CustomerManager customerManager;


    @Override
    public List<CustomerDTO> getAllCustomersByShopId(String shopId) {
        return null;
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO, String userId) {
        Customer customer = customerManager.createCustomer(customerDTO, userId);
        if (customer != null) {
            return Customer.fromToDto(customer);
        }
        return null;
    }
}
