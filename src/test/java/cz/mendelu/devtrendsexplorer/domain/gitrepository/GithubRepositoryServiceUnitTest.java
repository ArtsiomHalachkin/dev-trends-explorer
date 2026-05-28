package cz.mendelu.devtrendsexplorer.domain.gitrepository;

import cz.mendelu.devtrendsexplorer.domain.githubrepository.GitHubRepositoryRepository;
import cz.mendelu.devtrendsexplorer.domain.githubrepository.GithubRepository;
import cz.mendelu.devtrendsexplorer.domain.githubrepository.GithubRepositoryCreateDTO;
import cz.mendelu.devtrendsexplorer.domain.githubrepository.GithubRepositoryService;
import cz.mendelu.devtrendsexplorer.domain.repositorydomain.Domain;
import cz.mendelu.devtrendsexplorer.domain.repositorydomain.DomainRepository;
import cz.mendelu.devtrendsexplorer.domain.repositorylanguage.Language;
import cz.mendelu.devtrendsexplorer.domain.repositorylanguage.LanguageRepository;
import cz.mendelu.devtrendsexplorer.domain.repositoryowner.Owner;
import cz.mendelu.devtrendsexplorer.domain.repositoryowner.OwnerRepository;
import cz.mendelu.devtrendsexplorer.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GithubRepositoryServiceUnitTest {

    @Mock
    private GitHubRepositoryRepository repoRepository;
    @Mock
    private DomainRepository domainRepository;
    @Mock
    private LanguageRepository languageRepository;
    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private GithubRepositoryService service;

    @Test
    public void testFindAll() {
        // given
        GithubRepository repo1 = new GithubRepository();
        repo1.setId(1L);
        repo1.setName("awesome-go");

        GithubRepository repo2 = new GithubRepository();
        repo2.setId(2L);
        repo2.setName("prompts.chat");

        when(repoRepository.findAll()).thenReturn(List.of(repo1, repo2));

        // when
        List<GithubRepository> result = service.findAll();

        // then
        assertThat(result, hasSize(2));
        assertThat(result.get(0).getName(), is("awesome-go"));
        assertThat(result.get(1).getName(), is("prompts.chat"));
        verify(repoRepository).findAll();
    }

    @Test
    public void testFindById() {
        // given
        GithubRepository repo = new GithubRepository();
        repo.setId(1L);
        repo.setName("awesome-go");

        when(repoRepository.findById(1L)).thenReturn(Optional.of(repo));

        // when
        GithubRepository result = service.findById(1L);

        // then
        assertThat(result.getName(), is("awesome-go"));
        verify(repoRepository).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        // given
        when(repoRepository.findById(999L)).thenReturn(Optional.empty());

        // when + then
        assertThrows(NotFoundException.class, () -> service.findById(999L));
    }

    @Test
    public void testCreate() {
        // given
        GithubRepositoryCreateDTO dto = new GithubRepositoryCreateDTO();
        dto.setName("new-repo");
        dto.setFullName("google/new-repo");
        dto.setDescription("A new repo");
        dto.setStars(100);
        dto.setForks(10);
        dto.setWatchers(5);
        dto.setIssues(2);
        dto.setHasWiki(true);
        dto.setDomainName("Developer Tools");
        dto.setLanguageName("Go");
        dto.setOwnerLogin("google");

        Domain domain = new Domain();
        domain.setName("Developer Tools");

        Language language = new Language();
        language.setName("Go");

        Owner owner = new Owner();
        owner.setLogin("google");

        when(domainRepository.findByName("Developer Tools")).thenReturn(Optional.of(domain));
        when(languageRepository.findByName("Go")).thenReturn(Optional.of(language));
        when(ownerRepository.findByLogin("google")).thenReturn(Optional.of(owner));
        when(repoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        GithubRepository result = service.create(dto);

        // then
        assertThat(result.getName(), is("new-repo"));
        assertThat(result.getStars(), is(100));
        assertThat(result.getDomain().getName(), is("Developer Tools"));
        assertThat(result.getLanguage().getName(), is("Go"));
        assertThat(result.getOwner().getLogin(), is("google"));
        verify(repoRepository).save(result);
    }

    @Test
    public void testCreate_NewDomainLanguageOwner() {
        // given
        GithubRepositoryCreateDTO dto = new GithubRepositoryCreateDTO();
        dto.setName("new-repo");
        dto.setFullName("newowner/new-repo");
        dto.setStars(0);
        dto.setForks(0);
        dto.setWatchers(0);
        dto.setIssues(0);
        dto.setDomainName("New Domain");
        dto.setLanguageName("Rust");
        dto.setOwnerLogin("newowner");

        when(domainRepository.findByName("New Domain")).thenReturn(Optional.empty());
        when(languageRepository.findByName("Rust")).thenReturn(Optional.empty());
        when(ownerRepository.findByLogin("newowner")).thenReturn(Optional.empty());
        when(domainRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(languageRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(ownerRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(repoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        GithubRepository result = service.create(dto);

        // then
        assertThat(result.getDomain().getName(), is("New Domain"));
        assertThat(result.getLanguage().getName(), is("Rust"));
        assertThat(result.getOwner().getLogin(), is("newowner"));
        verify(domainRepository).save(any());
        verify(languageRepository).save(any());
        verify(ownerRepository).save(any());
    }

    @Test
    public void testUpdate() {
        // given
        GithubRepository existing = new GithubRepository();
        existing.setId(1L);
        existing.setName("old-name");
        existing.setStars(50);

        GithubRepositoryCreateDTO dto = new GithubRepositoryCreateDTO();
        dto.setName("updated-name");
        dto.setFullName("google/updated-name");
        dto.setStars(999);
        dto.setForks(0);
        dto.setWatchers(0);
        dto.setIssues(0);

        when(repoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(repoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        GithubRepository result = service.update(1L, dto);

        // then
        assertThat(result.getName(), is("updated-name"));
        assertThat(result.getStars(), is(999));
        verify(repoRepository).save(existing);
    }

    @Test
    public void testUpdate_NotFound() {
        // given
        GithubRepositoryCreateDTO dto = new GithubRepositoryCreateDTO();
        dto.setName("updated-repo");

        when(repoRepository.findById(999L)).thenReturn(Optional.empty());

        // when + then
        assertThrows(NotFoundException.class, () -> service.update(999L, dto));
        verify(repoRepository, never()).save(any());
    }

    @Test
    public void testDelete() {
        // given
        when(repoRepository.existsById(1L)).thenReturn(true);

        // when
        service.delete(1L);

        // then
        verify(repoRepository).deleteById(1L);
    }

    @Test
    public void testDelete_NotFound() {
        // given
        when(repoRepository.existsById(999L)).thenReturn(false);

        // when + then
        assertThrows(NotFoundException.class, () -> service.delete(999L));
        verify(repoRepository, never()).deleteById(any());
    }
}