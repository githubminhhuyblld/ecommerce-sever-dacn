package nlu.edu.vn.ecommerce.models.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nlu.edu.vn.ecommerce.models.enums.LogActionType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("updateLog")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLog {
    @Id
    private String id;
    private String type;
    private String actionType;
    private String forId;
    private String name;
    private Object value;
    private String updateBy;
    private Date updateTime;
    private String forCollection;

    public static UpdateLog createUpdateAttributeLog(String name, Object newValue, String updateBy, String forCollection, String id) {
        return createLog(LogActionType.UPDATE.name(), name, newValue, updateBy, forCollection, id);
    }

    public static UpdateLog createCreateLog(Object newValue, String updateBy, String forCollection) {
        return createLog(LogActionType.CREATE.name(), "Create Object", newValue, updateBy, forCollection, (String) null);
    }

    public static UpdateLog createDeleteLog(String updateBy, String forCollection, String id) {
        return createLog(LogActionType.DELETE.name(), "Delete", (Object) null, updateBy, forCollection, id);
    }

    protected static UpdateLog createLog(String actionType, String name, Object newValue, String updateBy, String forCollection, String forId) {
        UpdateLog updateLog = new UpdateLog();
        updateLog.setActionType(actionType);
        updateLog.setName(name);
        updateLog.setValue(newValue);
        updateLog.setUpdateBy(updateBy);
        updateLog.setForId(forId);
        updateLog.setUpdateTime(new Date());
        return updateLog;
    }

}
