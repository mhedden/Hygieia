package com.capitalone.dashboard.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *      Represents a unique collection in an external tool. For example, for a CI tool
 *      the collector item would be a Job. For a project management tool, the collector item
 *      might be a Scope.
 * </p>
 * <p>
 *      Each {@link Collector} is responsible for specifying how it's {@link CollectorItem}s are
 *      uniquely identified by storing key/value pairs in the options Map. The description field will
 *      be visible to users in the UI to aid in selecting the correct {@link CollectorItem} for their dashboard.
 *      Ideally, the description will be unique for a given {@link Collector}.
 * </p>
 */
@Document(collection="collector_items")
public class CollectorItem extends BaseModel {

    private String description;
    private boolean enabled;
    private ObjectId collectorId;
    private Map<String,Object> options = new HashMap<>();

    @Transient
    private Collector collector;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ObjectId getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(ObjectId collectorId) {
        this.collectorId = collectorId;
    }

    public Collector getCollector() {
        return collector;
    }

    public void setCollector(Collector collector) {
        this.collector = collector;
    }

    public Map<String, Object> getOptions() {
        return options;
    }
    
}
