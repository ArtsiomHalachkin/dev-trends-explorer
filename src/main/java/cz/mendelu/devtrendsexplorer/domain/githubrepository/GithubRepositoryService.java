package cz.mendelu.devtrendsexplorer.domain.githubrepository;

import cz.mendelu.devtrendsexplorer.domain.repositorydomain.Domain;
import cz.mendelu.devtrendsexplorer.domain.repositorydomain.DomainRepository;
import cz.mendelu.devtrendsexplorer.domain.repositorylanguage.Language;
import cz.mendelu.devtrendsexplorer.domain.repositorylanguage.LanguageRepository;
import cz.mendelu.devtrendsexplorer.domain.repositoryowner.Owner;
import cz.mendelu.devtrendsexplorer.domain.repositoryowner.OwnerRepository;
import cz.mendelu.devtrendsexplorer.utils.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GithubRepositoryService {

    private final GitHubRepositoryRepository repoRepository;
    private final DomainRepository domainRepository;
    private final LanguageRepository languageRepository;
    private final OwnerRepository ownerRepository;

    @Transactional(readOnly = true)
    public List<GithubRepository> findAll() {
        List<GithubRepository> result = new ArrayList<>();
        repoRepository.findAll().forEach(result::add);
        return result;
    }

    @Transactional(readOnly = true)
    public GithubRepository findById(Long id) {
        return repoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public GithubRepository create(GithubRepositoryCreateDTO dto) {
        GithubRepository repo = new GithubRepository();
        mapDtoToEntity(dto, repo);
        return repoRepository.save(repo);
    }

    @Transactional
    public GithubRepository update(Long id, GithubRepositoryCreateDTO dto) {
        GithubRepository repo = repoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapDtoToEntity(dto, repo);
        return repoRepository.save(repo);
    }

    @Transactional
    public void delete(Long id) {
        if (!repoRepository.existsById(id)) {
            throw new NotFoundException();
        }
        repoRepository.deleteById(id);
    }

    private void mapDtoToEntity(GithubRepositoryCreateDTO dto, GithubRepository repo) {
        repo.setName(dto.getName());
        repo.setFullName(dto.getFullName());
        repo.setDescription(dto.getDescription());
        repo.setStars(dto.getStars());
        repo.setForks(dto.getForks());
        repo.setWatchers(dto.getWatchers());
        repo.setIssues(dto.getIssues());
        repo.setHasWiki(dto.isHasWiki());

        if (dto.getDomainName() != null) {
            Domain domain = domainRepository.findByName(dto.getDomainName())
                    .orElseGet(() -> {
                        Domain d = new Domain();
                        d.setName(dto.getDomainName());
                        return domainRepository.save(d);
                    });
            repo.setDomain(domain);
        }

        if (dto.getLanguageName() != null) {
            Language language = languageRepository.findByName(dto.getLanguageName())
                    .orElseGet(() -> {
                        Language l = new Language();
                        l.setName(dto.getLanguageName());
                        return languageRepository.save(l);
                    });
            repo.setLanguage(language);
        }

        if (dto.getOwnerLogin() != null) {
            Owner owner = ownerRepository.findByLogin(dto.getOwnerLogin())
                    .orElseGet(() -> {
                        Owner o = new Owner();
                        o.setLogin(dto.getOwnerLogin());
                        return ownerRepository.save(o);
                    });
            repo.setOwner(owner);
        }
    }
}
