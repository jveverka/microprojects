package one.microproject.events.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Document(indexName = "events", createIndex = true)
public class EventDocument {

    @Id
    private String id;

    @Field(type= FieldType.Date)
    private Date date;

    @Field(type= FieldType.Keyword)
    private String userId;

    @Field(type= FieldType.Keyword)
    private String streamId;

    @Field(type= FieldType.Keyword)
    private String activityType;

    @Field(type= FieldType.Long)
    private Long quantity;

    @Field(type= FieldType.Long)
    private Long duration;

    @Field(type= FieldType.Keyword)
    private String unit;

    @Field(type= FieldType.Object)
    private String[] labels;

    public EventDocument() {
    }

    public EventDocument(String id, Date date, String userId, String streamId, String activityType,
                         Long quantity, Long duration, String unit, String[] labels) {
        this.id = id;
        this.date = date;
        this.userId = userId;
        this.streamId = streamId;
        this.activityType = activityType;
        this.quantity = quantity;
        this.duration = duration;
        this.unit = unit;
        this.labels = labels;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }
}
