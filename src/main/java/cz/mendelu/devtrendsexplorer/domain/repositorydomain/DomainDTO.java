package cz.mendelu.devtrendsexplorer.domain.repositorydomain;

import lombok.Data;

@Data
public class DomainDTO {

    private Long id;
    private String name;

    public static DomainDTO fromEntity(Domain entity) {
        DomainDTO dto = new DomainDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}
