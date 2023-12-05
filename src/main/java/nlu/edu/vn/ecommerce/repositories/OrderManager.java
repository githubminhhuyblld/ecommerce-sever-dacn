package nlu.edu.vn.ecommerce.repositories;

import nlu.edu.vn.ecommerce.models.Order;
import nlu.edu.vn.ecommerce.models.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;

import static nlu.edu.vn.ecommerce.untils.Timestamp.convertLocalDateToTimestamp;

@Repository
public class OrderManager {
    private final String COLLECTION = "order";
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Order> findOrdersDelivered(String shopId,LocalDate startDate, LocalDate endDate) {
        long startTimestamp = convertLocalDateToTimestamp(startDate);
        long endTimestamp = convertLocalDateToTimestamp(endDate);
        Criteria criteria = Criteria.where("shopId").is(shopId)
                .and("createAt").gte(startTimestamp).lte(endTimestamp)
                .and("orderStatus").is(OrderStatus.DELIVERED.toString());
        Query query = Query.query(criteria);
        return mongoTemplate.find(query, Order.class);
    }
    public List<Order> findOrdersCanceled(String shopId,LocalDate startDate, LocalDate endDate) {
        long startTimestamp = convertLocalDateToTimestamp(startDate);
        long endTimestamp = convertLocalDateToTimestamp(endDate);
        Criteria criteria = Criteria.where("shopId").is(shopId)
                .and("createAt").gte(startTimestamp).lte(endTimestamp)
                .and("orderStatus").is(OrderStatus.CANCELED.toString());
        Query query = Query.query(criteria);
        return mongoTemplate.find(query, Order.class);
    }
    public List<Order> findOrdersDeliveredByStartDate(String shopId,LocalDate startDate) {
        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = startDate.atTime(LocalTime.MAX);

        long startTimestamp = startOfDay.toInstant(ZoneOffset.UTC).toEpochMilli();
        long endTimestamp = endOfDay.toInstant(ZoneOffset.UTC).toEpochMilli();

        Query query = new Query(Criteria.where("shopId").is(shopId)
                .and("createAt").gte(startTimestamp).lte(endTimestamp)
                .and("orderStatus").is(OrderStatus.DELIVERED.toString()));
        return mongoTemplate.find(query, Order.class);
    }
    public List<Order> findOrdersCanceledByStartDate(String shopId, LocalDate startDate) {
        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = startDate.atTime(LocalTime.MAX);

        long startTimestamp = startOfDay.toInstant(ZoneOffset.UTC).toEpochMilli();
        long endTimestamp = endOfDay.toInstant(ZoneOffset.UTC).toEpochMilli();

        Query query = new Query(Criteria.where("shopId").is(shopId)
                .and("createAt").gte(startTimestamp).lte(endTimestamp)
                .and("orderStatus").is(OrderStatus.CANCELED.toString()));
        return mongoTemplate.find(query, Order.class);
    }



}
