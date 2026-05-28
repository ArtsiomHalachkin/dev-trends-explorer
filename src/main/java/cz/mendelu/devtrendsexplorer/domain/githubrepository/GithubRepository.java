package cz.mendelu.devtrendsexplorer.domain.githubrepository;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import cz.mendelu.devtrendsexplorer.domain.repositorydomain.Domain;
import cz.mendelu.devtrendsexplorer.domain.repositorylanguage.Language;
import cz.mendelu.devtrendsexplorer.domain.repositoryowner.Owner;





@Entity
@Table(name = "repositories")
@Data
public class GithubRepository {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    @Column(name = "full_name")
    private String fullName;

    @Column(columnDefinition = "TEXT")
    private String description;

    private int stars = 0;

    private int forks = 0;

    private int watchers = 0;

    private int issues = 0;

    @Column(name = "has_wiki")
    private boolean hasWiki = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id")
    private Domain domain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;
}
