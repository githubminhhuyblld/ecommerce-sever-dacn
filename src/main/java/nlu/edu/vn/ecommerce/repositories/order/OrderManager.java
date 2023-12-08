package nlu.edu.vn.ecommerce.repositories.order;

import nlu.edu.vn.ecommerce.base.BaseEntityManager;
import nlu.edu.vn.ecommerce.models.order.Order;
import nlu.edu.vn.ecommerce.models.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;

@Repository
public class OrderManager extends BaseEntityManager<Order> {
    private static final String ORDER_COLLECTION = "order";
    @Autowired
    private MongoTemplate mongoTemplate;
    public OrderManager() {
        super(ORDER_COLLECTION, Order.class);
    }
    public List<Order> findOrdersCanceled(String shopId, long startTimestamp, long endTimestamp) {
        Criteria criteria = Criteria.where("shopId").is(shopId)
                .and("createAt").gte(startTimestamp).lte(endTimestamp)
                .and("orderStatus").is(OrderStatus.CANCELED.toString());
        Query query = Query.query(criteria);
        return mongoTemplate.find(query, Order.class);
    }
    public List<Order> findOrdersDelivered(String shopId, long startTimestamp, long endTimestamp) {
        Criteria criteria = Criteria.where("shopId").is(shopId)
                .and("createAt").gte(startTimestamp).lte(endTimestamp)
                .and("orderStatus").is(OrderStatus.DELIVERED.toString());
        Query query = Query.query(criteria);
        return mongoTemplate.find(query, Order.class);
    }
    public List<Order> findOrdersDeliveredByStartDate(String shopId, LocalDate startDate) {
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
    public List<Order> findOrdersForUser(String userId, LocalDate cutoffDate) {
        LocalDateTime cutoffDateTime = cutoffDate.atStartOfDay();
        long cutoffTimestamp = cutoffDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("userId").is(userId),
                new Criteria().orOperator(
                        Criteria.where("orderStatus").ne(OrderStatus.CANCELED.toString()),
                        Criteria.where("orderStatus").is(OrderStatus.CANCELED.toString())
                                .and("canceledAt").gt(cutoffTimestamp)
                )
        );
        Query query = Query.query(criteria);
        query.with(Sort.by(Sort.Order.desc("createAt")));

        return mongoTemplate.find(query, Order.class, ORDER_COLLECTION);
    }
    public List<Order> findOrdersByShopAndEmail(String shopId, String email) {
        Criteria criteria = Criteria.where("shopId").is(shopId).and("email").is(email);
        Query query = Query.query(criteria);
        query.with(Sort.by(Sort.Order.desc("createAt")));
        return mongoTemplate.find(query, Order.class);
    }



}
