package one.microproject.events.controller;

import one.microproject.events.dto.EventDTO;
import one.microproject.events.dto.IdDTO;
import one.microproject.events.model.EventDocument;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
public class EventsController {

    private ElasticsearchOperations elasticsearchOperations;

    public EventsController(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @PostMapping()
    public IdDTO save(@RequestBody EventDTO eventDTO) {
        String id = UUID.randomUUID().toString();
        Date date = new Date();
        EventDocument eventDocument = new EventDocument(id, date, eventDTO);
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(id)
                .withObject(eventDocument)
                .build();
        String documentId = elasticsearchOperations.index(indexQuery, IndexCoordinates.of("events"));
        return new IdDTO(documentId);
    }

    @GetMapping()
    public List<EventDocument> getAll() {
        SearchHits<EventDocument> events = elasticsearchOperations
                .search(Query.findAll(), EventDocument.class);
        return events.get().map(s->s.getContent()).collect(Collectors.toList());
    }

}
