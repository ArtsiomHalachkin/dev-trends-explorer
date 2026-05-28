package cz.mendelu.devtrendsexplorer.domain.repositorydomain;


import cz.mendelu.devtrendsexplorer.domain.githubrepository.GithubRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "domains")
@Data
public class Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    // A single domain can have many repositories
    @ToString.Exclude
    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL)
    private List<GithubRepository> repositories;
}