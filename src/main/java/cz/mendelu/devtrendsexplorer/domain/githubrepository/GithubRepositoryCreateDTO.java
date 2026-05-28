package cz.mendelu.devtrendsexplorer.domain.githubrepository;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class GithubRepositoryCreateDTO {

    @NotEmpty
    private String name;

    @NotEmpty
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
}
