package cz.mendelu.devtrendsexplorer.domain.githubrepository;

import lombok.Data;

@Data
public class GithubRepositoryDTO {


    private Long id;
    private String name;
    private String fullName;
    private String description;
    private int stars;
    private int forks;
    private int watchers;
    private int issues;
    private boolean hasWiki;
    private String domainName;
    private String languageName;
    private String ownerLogin;

    public static GithubRepositoryDTO fromEntity(GithubRepository entity) {
        GithubRepositoryDTO dto = new GithubRepositoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setFullName(entity.getFullName());
        dto.setDescription(entity.getDescription());
        dto.setStars(entity.getStars());
        dto.setForks(entity.getForks());
        dto.setWatchers(entity.getWatchers());
        dto.setIssues(entity.getIssues());
        dto.setHasWiki(entity.isHasWiki());
        dto.setDomainName(entity.getDomain() != null ? entity.getDomain().getName() : null);
        dto.setLanguageName(entity.getLanguage() != null ? entity.getLanguage().getName() : null);
        dto.setOwnerLogin(entity.getOwner() != null ? entity.getOwner().getLogin() : null);
        return dto;
    }
}
