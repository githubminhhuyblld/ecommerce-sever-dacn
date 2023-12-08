package nlu.edu.vn.ecommerce.services.impl;

import nlu.edu.vn.ecommerce.dto.customer.CustomerDTO;
import nlu.edu.vn.ecommerce.models.customer.Customer;
import nlu.edu.vn.ecommerce.repositories.customer.CustomerManager;
import nlu.edu.vn.ecommerce.services.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements ICustomerService {
    @Autowired
    private CustomerManager customerManager;
    @Override
    public List<CustomerDTO> findByShopId(String shopId) {
        List<Customer> customers = customerManager.findByShopId(shopId);
        return customers.stream()
                .map(Customer::fromToDto)
                .collect(Collectors.toList());
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
