package cz.mendelu.devtrendsexplorer.domain.repositoryowner;

import lombok.Data;
@Data
public class OwnerDTO {

    private Long id;
    private String login;

    public static OwnerDTO fromEntity(Owner entity) {
        OwnerDTO dto = new OwnerDTO();
        dto.setId(entity.getId());
        dto.setLogin(entity.getLogin());
        return dto;
    }
}