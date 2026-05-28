package cz.mendelu.devtrendsexplorer.domain.repositoryowner;

import cz.mendelu.devtrendsexplorer.domain.githubrepository.GithubRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "owners")
@Data
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String login;

    // A single owner can have many repositories
    @ToString.Exclude
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<GithubRepository> repositories;
}