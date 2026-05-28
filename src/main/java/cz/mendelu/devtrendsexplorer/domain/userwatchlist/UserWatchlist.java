package cz.mendelu.devtrendsexplorer.domain.userwatchlist;

import cz.mendelu.devtrendsexplorer.domain.githubrepository.GithubRepository;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_watchlist")
@IdClass(UserWatchlistId.class)
@Data
public class UserWatchlist {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repo_id")
    private GithubRepository repository;
}
