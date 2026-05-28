package cz.mendelu.devtrendsexplorer.domain.repositorydomain;

import cz.mendelu.devtrendsexplorer.domain.repositorylanguage.Language;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DomainRepository extends CrudRepository<Domain, Long> {

    Optional<Domain> findByName(String domainName);
}

