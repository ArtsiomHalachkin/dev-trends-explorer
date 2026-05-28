package cz.mendelu.devtrendsexplorer.domain.repositorylanguage;

import cz.mendelu.devtrendsexplorer.domain.githubrepository.GithubRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "languages")
@Data
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    // A single language can have many repositories
    @ToString.Exclude
    @OneToMany(mappedBy = "language", cascade = CascadeType.ALL)
    private List<GithubRepository> repositories;
}