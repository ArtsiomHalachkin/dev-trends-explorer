package cz.mendelu.devtrendsexplorer.utils;

import cz.mendelu.devtrendsexplorer.domain.githubrepository.GitHubRepositoryRepository;
import cz.mendelu.devtrendsexplorer.domain.githubrepository.GithubRepository;
import cz.mendelu.devtrendsexplorer.domain.repositorydomain.DomainRepository;
import cz.mendelu.devtrendsexplorer.domain.repositorylanguage.LanguageRepository;
import cz.mendelu.devtrendsexplorer.domain.repositoryowner.OwnerRepository;
import cz.mendelu.devtrendsexplorer.utils.data.DatabaseSeeder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DatabaseSeederUnitTest {

    private GitHubRepositoryRepository repoRepository;
    private OwnerRepository ownerRepository;
    private LanguageRepository languageRepository;
    private DomainRepository domainRepository;
    private DatabaseSeeder databaseSeeder;

    @BeforeEach
    public void setUp() {
        repoRepository = mock(GitHubRepositoryRepository.class);
        ownerRepository = mock(OwnerRepository.class);
        languageRepository = mock(LanguageRepository.class);
        domainRepository = mock(DomainRepository.class);

        when(domainRepository.findByName(any())).thenReturn(Optional.empty());
        when(ownerRepository.findByLogin(any())).thenReturn(Optional.empty());
        when(languageRepository.findByName(any())).thenReturn(Optional.empty());

        when(domainRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(ownerRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(languageRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        databaseSeeder = new DatabaseSeeder(repoRepository, ownerRepository, languageRepository, domainRepository);
    }

    @Test
    public void testImportCsv_ValidFile() throws IOException {
        // given
        InputStream is = new FileInputStream("src/test/resources/data/repos.csv");

        // when
        int count = databaseSeeder.importCsv(is);

        // then
        assertThat(count, is(3));

        ArgumentCaptor<GithubRepository> captor = ArgumentCaptor.forClass(GithubRepository.class);
        verify(repoRepository, times(3)).save(captor.capture());

        GithubRepository firstRepo = captor.getAllValues().get(0);
        assertThat(firstRepo.getName(), equalTo("tensorflow"));
        assertThat(firstRepo.getStars(), is(194126));
        assertThat(firstRepo.getLanguage().getName(), equalTo("C++"));
        assertThat(firstRepo.getOwner().getLogin(), equalTo("tensorflow"));
    }

    @Test
    public void testImportCsv_EmptyFile() throws IOException {
        // given
        InputStream is = new FileInputStream("src/test/resources/data/empty.csv");

        // when
        int count = databaseSeeder.importCsv(is);

        // then
        assertThat(count, is(0));
        verify(repoRepository, never()).save(any());
    }

    @Test
    public void testImportCsv_NullStream() {
        // given
        InputStream is = null;

        // whe
        int count = databaseSeeder.importCsv(is);

        // then
        assertThat(count, is(0));
        verify(repoRepository, never()).save(any());
    }
}