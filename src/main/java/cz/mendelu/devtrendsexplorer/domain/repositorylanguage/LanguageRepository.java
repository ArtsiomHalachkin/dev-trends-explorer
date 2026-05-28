package cz.mendelu.devtrendsexplorer.domain.repositorylanguage;

import cz.mendelu.devtrendsexplorer.domain.githubrepository.GithubRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LanguageRepository extends CrudRepository<Language, Long> {

    Optional<Language> findByName(String languageName);
}

