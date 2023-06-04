package nlu.edu.vn.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@EnableMongoRepositories
public class EcommerceSeverApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceSeverApplication.class, args);
    }

}
