package one.microproject.logger.service;

import one.microproject.logger.dto.CreateDataSeriesRequest;
import one.microproject.logger.dto.DataSeriesInfo;
import one.microproject.logger.dto.DeleteDataSeriesRequest;
import one.microproject.logger.dto.GenericResponse;
import one.microproject.logger.model.DataSeries;
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
        String id = request.getGroupId() + "-" + request.getName();
        DataSeries dataSeries = new DataSeries(id, request.getGroupId(), request.getName(), request.getDescription());
        Mono<DataSeries> saved = dataSeriesRepository.save(dataSeries);
        return saved.transform(mono -> { return mono.map( m -> { return GenericResponse.ok(); }); });
    }

    @Override
    public Flux<DataSeriesInfo> getAll() {
        Flux<DataSeries> dataSeriesFlux = dataSeriesRepository.findAll();
        return dataSeriesFlux.transform(flux -> {
            return flux.map( f ->  {
                return new DataSeriesInfo(f.getGroupId(), f.getName(), f.getName());
            });
        });
    }

    @Override
    public Mono<GenericResponse> deleteDataSeries(DeleteDataSeriesRequest request) {
        String id = request.getGroupId() + "-" + request.getName();
        Mono<Void> deleted = dataSeriesRepository.deleteById(id);
        return deleted.transform(mono -> { return mono.map( m -> { return GenericResponse.ok(); }); });
    }

}
