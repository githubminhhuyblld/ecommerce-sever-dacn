package nlu.edu.vn.ecommerce.base;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import io.jsonwebtoken.lang.Assert;
import lombok.Getter;
import nlu.edu.vn.ecommerce.models.log.UpdateLog;
import nlu.edu.vn.ecommerce.untils.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;


public class BaseManager<T> {
    @Autowired
    protected MongoTemplate mongoTemplate;

    @Getter
    protected String collectionName;
    protected Class<T> tClass;

    public BaseManager(String collectionName, Class<T> tClass) {
        this.collectionName = collectionName;
        this.tClass = tClass;
    }


    public BaseManager(MongoTemplate mongoTemplate, String collectionName, Class<T> tClass) {
        this.mongoTemplate = mongoTemplate;
        this.collectionName = collectionName;
        this.tClass = tClass;
    }

    public T getById(String id) {
        return this.mongoTemplate.findById(id, this.tClass, this.collectionName);
    }

    public T findByField(String fieldName, Object value) {
        Query query = Query.query(Criteria.where(fieldName).is(value));
        return this.mongoTemplate.findOne(query, this.tClass, this.collectionName);
    }
    public Object create(T object,String createBy){
        return this.createObject(object,createBy,this.collectionName);
    }
    private Object createObject(Object object,String createBy,String collectionName){
        Assert.notNull(object,"Object must not null");
        if(object instanceof BaseEntity){
            BaseEntity baseEntity = (BaseEntity) object;
            if(baseEntity.getCreateAt() == null){
                baseEntity.setCreateAt(new Timestamp().getTime());
            }
            baseEntity.setUpdateBy(createBy);
            baseEntity.setCreateBy(createBy);
        }
        Object newObject = this.mongoTemplate.insert(object,collectionName);
        this.mongoTemplate.insert(UpdateLog.createCreateLog(newObject,createBy,this.tClass.getName()));
        return newObject;
    }

    public void updateAttribute(String id, String name, Object value, String updateBy) {
        Update update = new Update();
        update.set("updateAt", new Timestamp().getTime());
        update.set("updateBy", updateBy);
        update.set(name, value);
        UpdateResult updateResult = this.mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id")), update, this.collectionName);
        Assert.isTrue(updateResult.getModifiedCount() != 0L, "Update record is" + id + "for collection"
                + this.collectionName);
        this.mongoTemplate.insert(UpdateLog.createUpdateAttributeLog("Update attribute:" + name + "value: " + value, value, updateBy, this.tClass.getName(), id));
    }

    public void delete(String id, String updateBy) {
        DeleteResult result = this.mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)), this.tClass, this.collectionName);
        Assert.isTrue(result.getDeletedCount() != 0L, "record is " + id + "for collection" +
                this.collectionName + "is not exist!");
    }

}
