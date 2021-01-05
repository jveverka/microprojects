package one.microproject.logger.service;

import one.microproject.logger.dto.CreateDataSeriesRequest;
import one.microproject.logger.dto.DataSeriesInfo;
import one.microproject.logger.dto.GenericResponse;
import one.microproject.logger.model.DataSeries;
import one.microproject.logger.model.DataSeriesId;
import one.microproject.logger.repository.DataSeriesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DataSeriesServiceImpl implements DataSeriesService {

    private static final Logger LOG = LoggerFactory.getLogger(DataSeriesServiceImpl.class);

    private final DataSeriesRepository dataSeriesRepository;
    private final DataRecordService dataRecordService;

    @Autowired
    public DataSeriesServiceImpl(DataSeriesRepository dataSeriesRepository, DataRecordService dataRecordService) {
        this.dataSeriesRepository = dataSeriesRepository;
        this.dataRecordService = dataRecordService;
    }

    @Override
    @PreAuthorize("@authorizerService.hasAccess('DataSeries', 'create', #request, authentication)")
    public Mono<GenericResponse> createDataSeries(CreateDataSeriesRequest request) {
        LOG.info("createDataSeries: {}:{}", request.getGroupId(), request.getName());
        DataSeriesId id = new DataSeriesId(request.getGroupId(), request.getName());
        DataSeries dataSeries = new DataSeries(id.toStringId(), id.getGroupId(), id.getName(), request.getDescription());
        Mono<DataSeries> saved = dataSeriesRepository.save(dataSeries);
        return saved.transform(mono -> mono.map( m -> GenericResponse.ok()));
    }

    @Override
    @PreAuthorize("@authorizerService.hasAccess('DataSeries', 'getAll', authentication)")
    public Flux<DataSeriesInfo> getAll() {
        LOG.info("getAll");
        Flux<DataSeries> dataSeriesFlux = dataSeriesRepository.findAll();
        return dataSeriesFlux.transform(flux -> flux.map( f ->  new DataSeriesInfo(f.getGroupId(), f.getName(), f.getName())));
    }

    @Override
    @PreAuthorize("@authorizerService.hasAccess('DataSeries', 'delete', #id, authentication)")
    public Mono<GenericResponse> deleteDataSeries(DataSeriesId id) {
        LOG.info("deleteDataSeries {}", id.toStringId());
        Mono<Void> deleted = dataSeriesRepository.deleteById(id.toStringId());
        dataRecordService.dropAll(id);
        return deleted.transform(mono -> mono.map( m -> GenericResponse.ok()));
    }

    @Override
    @PreAuthorize("@authorizerService.hasAccess('DataSeries', 'get', #id, authentication)")
    public Mono<DataSeriesInfo> get(DataSeriesId id) {
        LOG.info("get {}", id.toStringId());
        Mono<DataSeries> found = dataSeriesRepository.findById(id.toStringId());
        return found.transform(mono -> mono.map( m -> new DataSeriesInfo(m.getGroupId(), m.getName(), m.getDescription())));
    }

}
