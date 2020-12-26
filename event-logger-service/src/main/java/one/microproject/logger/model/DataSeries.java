package one.microproject.logger.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class DataSeries {

    @Id
    private String id;

    private String name;
    private String description;

}
