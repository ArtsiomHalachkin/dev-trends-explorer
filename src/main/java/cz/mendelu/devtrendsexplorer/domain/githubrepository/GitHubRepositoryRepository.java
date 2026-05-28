package cz.mendelu.devtrendsexplorer.domain.githubrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface GitHubRepositoryRepository extends CrudRepository<GithubRepository, Long> {

    @Query("SELECT r.name FROM GithubRepository r ORDER BY r.stars DESC")
    List<String> findNameOfReposSortedByStars();


}
