package one.microproject.logger.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class DataRecord {

    @Id
    private String id;

    private String seriesId;
    private Long timeStamp;

}
