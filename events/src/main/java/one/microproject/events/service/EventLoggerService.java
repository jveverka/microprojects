package one.microproject.events.service;

import one.microproject.events.dto.EventDTO;
import one.microproject.events.dto.IdDTO;
import one.microproject.events.model.EventDocument;

import java.util.List;

public interface EventLoggerService {

    IdDTO save(String userId, EventDTO eventDTO);

    List<EventDocument> getAll();

}
