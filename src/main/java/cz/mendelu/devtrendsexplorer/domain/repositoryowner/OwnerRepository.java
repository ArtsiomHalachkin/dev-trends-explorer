package cz.mendelu.devtrendsexplorer.domain.repositoryowner;

import cz.mendelu.devtrendsexplorer.domain.githubrepository.GithubRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface OwnerRepository extends CrudRepository<Owner, Long> {
    Optional<Owner> findByLogin(String login);
}