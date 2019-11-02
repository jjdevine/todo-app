package toDo.persistence;

import java.util.HashMap;
import java.util.Map;

public class PersistenceModel {

    private String id;
    private String type;
    private String sourceClass;
    private Map<String, String> properties;

    public PersistenceModel() {
        properties = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSourceClass() {
        return sourceClass;
    }

    public void setSourceClass(String sourceClass) {
        this.sourceClass = sourceClass;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public void addProperty(String key, String value) {
        properties.put(key, value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PersistenceModel{");
        sb.append("id='").append(id).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", sourceClass=").append(sourceClass);
        sb.append(", properties=").append(properties);
        sb.append('}');
        return sb.toString();
    }
}
