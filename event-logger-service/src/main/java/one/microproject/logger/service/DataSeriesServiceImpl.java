package one.microproject.logger.service;

import one.microproject.logger.dto.CreateDataSeriesRequest;
import one.microproject.logger.dto.DataSeriesInfo;
import one.microproject.logger.dto.GenericResponse;
import one.microproject.logger.model.DataSeries;
import one.microproject.logger.model.DataSeriesId;
import one.microproject.logger.repository.DataSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DataSeriesServiceImpl implements DataSeriesService {

    private final DataSeriesRepository dataSeriesRepository;

    @Autowired
    public DataSeriesServiceImpl(DataSeriesRepository dataSeriesRepository) {
        this.dataSeriesRepository = dataSeriesRepository;
    }

    @Override
    public Mono<GenericResponse> createDataSeries(CreateDataSeriesRequest request) {
        DataSeriesId id = new DataSeriesId(request.getGroupId(), request.getName());
        DataSeries dataSeries = new DataSeries(id.toStringId(), id.getGroupId(), id.getName(), request.getDescription());
        Mono<DataSeries> saved = dataSeriesRepository.save(dataSeries);
        return saved.transform(mono -> mono.map( m -> GenericResponse.ok()));
    }

    @Override
    public Flux<DataSeriesInfo> getAll() {
        Flux<DataSeries> dataSeriesFlux = dataSeriesRepository.findAll();
        return dataSeriesFlux.transform(flux -> flux.map( f ->  new DataSeriesInfo(f.getGroupId(), f.getName(), f.getName())));
    }

    @Override
    public Mono<GenericResponse> deleteDataSeries(DataSeriesId id) {
        Mono<Void> deleted = dataSeriesRepository.deleteById(id.toStringId());
        return deleted.transform(mono -> mono.map( m -> GenericResponse.ok()));
    }

    @Override
    public Mono<DataSeriesInfo> get(DataSeriesId id) {
        Mono<DataSeries> found = dataSeriesRepository.findById(id.toStringId());
        return found.transform(mono -> mono.map( m -> new DataSeriesInfo(m.getGroupId(), m.getName(), m.getDescription())));
    }

}
