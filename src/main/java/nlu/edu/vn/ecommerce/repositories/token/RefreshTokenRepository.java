package nlu.edu.vn.ecommerce.repositories.token;

import nlu.edu.vn.ecommerce.models.auth.RefreshToken;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
    void deleteByOwner_Id(ObjectId id);

    default void deleteByOwner_Id(String id) {
        deleteByOwner_Id(new ObjectId(id));
    }

    ;
}
