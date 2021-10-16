package one.microproject.events.config;

import one.microproject.events.model.EventDocument;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.LinkedHashMap;
import java.util.Map;

@WritingConverter
public class EventDocumentWritingConverter implements Converter<EventDocument, Map<String, Object>> {

    @Override
    public Map<String, Object> convert(EventDocument source) {
        LinkedHashMap<String, Object> target = new LinkedHashMap<>();
        target.put("timestamp", source.getDate());
        return target;
    }

}
