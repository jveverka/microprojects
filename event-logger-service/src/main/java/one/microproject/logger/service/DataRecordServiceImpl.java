package one.microproject.logger.service;

import one.microproject.logger.repository.DataRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataRecordServiceImpl implements DataRecordService {

    private final DataRecordRepository dataRecordRepository;

    @Autowired
    public DataRecordServiceImpl(DataRecordRepository dataRecordRepository) {
        this.dataRecordRepository = dataRecordRepository;
    }

}
