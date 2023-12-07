package nlu.edu.vn.ecommerce.base;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;


public class BaseEntityManager<T> extends BaseManager<T> {
    public BaseEntityManager(String collectionName, Class<T> tClass) {
        super(collectionName, tClass);
    }

    public T findById(String id) {
        return mongoTemplate.findOne(Query.query(Criteria.where("_id").is(id)), this.tClass, this.collectionName);
    }
}
