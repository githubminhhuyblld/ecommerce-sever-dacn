package nlu.edu.vn.ecommerce.services;

import nlu.edu.vn.ecommerce.dto.customer.CustomerDTO;

import java.util.List;

public interface ICustomerService {
     List<CustomerDTO> getAllCustomersByShopId(String shopId);
     CustomerDTO createCustomer(CustomerDTO customerDTO,String userId);
}
