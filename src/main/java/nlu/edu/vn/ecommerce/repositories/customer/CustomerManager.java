package nlu.edu.vn.ecommerce.repositories.customer;

import nlu.edu.vn.ecommerce.base.BaseEntityManager;
import nlu.edu.vn.ecommerce.dto.customer.CustomerDTO;
import nlu.edu.vn.ecommerce.models.customer.Customer;
import nlu.edu.vn.ecommerce.models.enums.ActiveStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerManager extends BaseEntityManager<Customer> {
    private static final String ORDER_COLLECTION = "customers";
    @Autowired
    private MongoTemplate mongoTemplate;
    public CustomerManager() {
        super(ORDER_COLLECTION, Customer.class);
    }

    public Customer createCustomer(CustomerDTO customerDTO,String createBy){
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setNumberPhone(customerDTO.getNumberPhone());
        customer.setAddress(customerDTO.getAddress());
        customer.setShopId(customerDTO.getShopId());
        customer.setUserId(customerDTO.getUserId());
        customer.setStatus(ActiveStatus.ACTIVE);
        return (Customer) create(customer,createBy);
    }
    public Customer findByEmailAndShopId(String email, String shopId) {
        Criteria criteria = Criteria.where("email").is(email).and("shopId").is(shopId);
        Query query = new Query(criteria);
        return mongoTemplate.findOne(query, Customer.class);
    }

    public List<Customer> findByShopId(String shopId) {
        Criteria criteria = Criteria.where("shopId").is(shopId);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Customer.class);
    }


}
