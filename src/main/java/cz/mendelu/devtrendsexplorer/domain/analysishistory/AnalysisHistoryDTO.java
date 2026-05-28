package cz.mendelu.devtrendsexplorer.domain.analysishistory;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnalysisHistoryDTO {

    private Long id;
    private String repositoryName;
    private Long repositoryId;
    private double calculatedScore;
    private LocalDateTime timestamp;

    public static AnalysisHistoryDTO fromEntity(AnalysisHistory entity) {
        AnalysisHistoryDTO dto = new AnalysisHistoryDTO();
        dto.setId(entity.getId());
        dto.setRepositoryId(entity.getRepository().getId());
        dto.setRepositoryName(entity.getRepository().getName());
        dto.setCalculatedScore(entity.getCalculatedScore());
        dto.setTimestamp(entity.getTimestamp());
        return dto;
    }
}
