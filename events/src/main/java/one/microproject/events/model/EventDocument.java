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

    @Field(type= FieldType.Object)
    private Object body;

    public EventDocument() {
    }

    public EventDocument(String id, Date date, String userId, Object body) {
        this.id = id;
        this.date = date;
        this.userId = userId;
        this.body = body;
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

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
