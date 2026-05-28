package cz.mendelu.devtrendsexplorer.domain.analysishistory;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AnalysisHistoryRepository extends CrudRepository<AnalysisHistory, Long> {

    List<AnalysisHistory> findAllByOrderByTimestampDesc();
}
