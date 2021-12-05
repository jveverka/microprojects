package one.microproject.events.service.impl;

import one.microproject.events.dto.EventDTO;
import one.microproject.events.dto.IdDTO;
import one.microproject.events.model.EventDocument;
import one.microproject.events.service.EventLoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventLoggerServiceImpl implements EventLoggerService {

    private final ElasticsearchOperations elasticsearchOperations;

    @Autowired
    public EventLoggerServiceImpl(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public IdDTO save(String userId, EventDTO eventDTO) {
        String id = UUID.randomUUID().toString();
        Date date = new Date();
        EventDocument eventDocument = new EventDocument(id, date, userId,
                eventDTO.streamId(), eventDTO.activityType(), eventDTO.quantity(), eventDTO.duration(), eventDTO.unit(), eventDTO.labels());
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(id)
                .withObject(eventDocument)
                .build();
        String documentId = elasticsearchOperations.index(indexQuery, IndexCoordinates.of("events"));
        return new IdDTO(documentId);
    }

    @Override
    public List<EventDocument> getAll() {
        SearchHits<EventDocument> events = elasticsearchOperations
                .search(Query.findAll(), EventDocument.class, IndexCoordinates.of("events"));
        return events.get().map(s->s.getContent()).collect(Collectors.toList());
    }

}
