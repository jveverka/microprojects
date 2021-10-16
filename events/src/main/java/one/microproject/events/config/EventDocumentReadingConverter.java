package one.microproject.events.config;

import one.microproject.events.model.EventDocument;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.Map;

@ReadingConverter
public class EventDocumentReadingConverter implements Converter<Map<String, Object>, EventDocument> {

    @Override
    public EventDocument convert(Map<String, Object> source) {
        return null;
    }

}
