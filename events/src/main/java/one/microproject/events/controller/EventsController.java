package one.microproject.events.controller;

import one.microproject.events.dto.EventDTO;
import one.microproject.events.dto.IdDTO;
import one.microproject.events.model.EventDocument;
import one.microproject.events.service.EventLoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventsController {

    private EventLoggerService eventLoggerService;

    @Autowired
    public EventsController(EventLoggerService eventLoggerService) {
        this.eventLoggerService = eventLoggerService;
    }

    @PostMapping()
    public IdDTO save(@RequestBody EventDTO eventDTO) {
        return eventLoggerService.save("user-001", eventDTO);
    }

    @GetMapping()
    public List<EventDocument> getAll() {
        return eventLoggerService.getAll();
    }

}
