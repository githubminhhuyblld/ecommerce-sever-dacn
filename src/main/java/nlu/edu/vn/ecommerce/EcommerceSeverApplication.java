package nlu.edu.vn.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableMongoRepositories
@EnableScheduling
public class EcommerceSeverApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceSeverApplication.class, args);
    }

}
