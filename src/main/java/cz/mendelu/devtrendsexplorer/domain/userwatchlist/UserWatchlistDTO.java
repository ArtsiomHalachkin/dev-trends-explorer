package cz.mendelu.devtrendsexplorer.domain.userwatchlist;

import lombok.Data;

@Data
public class UserWatchlistDTO {

    private String userId;
    private Long repositoryId;
    private String repositoryName;

    public static UserWatchlistDTO fromEntity(UserWatchlist entity) {
        UserWatchlistDTO dto = new UserWatchlistDTO();
        dto.setUserId(entity.getUserId());
        dto.setRepositoryId(entity.getRepository().getId());
        dto.setRepositoryName(entity.getRepository().getName());
        return dto;
    }
}
