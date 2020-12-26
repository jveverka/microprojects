package one.microproject.logger.service;

import one.microproject.logger.repository.DataSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataSeriesServiceImpl implements DataSeriesService {

    private final DataSeriesRepository dataSeriesRepository;

    @Autowired
    public DataSeriesServiceImpl(DataSeriesRepository dataSeriesRepository) {
        this.dataSeriesRepository = dataSeriesRepository;
    }

}
