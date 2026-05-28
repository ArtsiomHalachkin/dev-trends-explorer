package cz.mendelu.devtrendsexplorer.domain.repositorylanguage;

import lombok.Data;

@Data
public class LanguageDTO {

    private Long id;
    private String name;

    public static LanguageDTO fromEntity(Language entity) {
        LanguageDTO dto = new LanguageDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}