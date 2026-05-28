package cz.mendelu.devtrendsexplorer.domain.statistics;

import cz.mendelu.devtrendsexplorer.domain.githubrepository.GithubRepository;
import cz.mendelu.devtrendsexplorer.domain.githubrepository.GitHubRepositoryRepository;
import cz.mendelu.devtrendsexplorer.domain.repositorylanguage.Language;
import cz.mendelu.devtrendsexplorer.domain.repositoryowner.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

public class StatisticsServiceUnitTest {

    private StatisticsService statisticsService;
    private GitHubRepositoryRepository repositoryRepositoryMock;

    @BeforeEach
    public void setUp() {
        repositoryRepositoryMock = mock(GitHubRepositoryRepository.class);
        statisticsService = new StatisticsService(repositoryRepositoryMock);
    }

    private GithubRepository createMockRepo(String name, int stars, int forks, int issues, int watchers, boolean hasWiki) {
        GithubRepository repo = new GithubRepository();
        repo.setName(name);
        repo.setStars(stars);
        repo.setForks(forks);
        repo.setIssues(issues);
        repo.setWatchers(watchers);
        repo.setHasWiki(hasWiki);
        return repo;
    }

    @Test
    public void testGetHealthScores() {
        // given
        GithubRepository repo = createMockRepo("repo1", 100, 20, 2, 0, true);
        List<GithubRepository> repos = Arrays.asList(repo);

        // when
        Map<String, Double> result = statisticsService.getHealthScores(repos);

        // then
        assertThat(result.get("repo1"), is(32.26));
    }

    @Test
    public void testGetEngagementScores() {
        // given
        // Vzorec: 10 forks + 50 watchers = 60.0
        GithubRepository repo = createMockRepo("repo1", 0, 10, 0, 50, true);
        List<GithubRepository> repos = Arrays.asList(repo);

        // when
        Map<String, Double> result = statisticsService.getEngagementScores(repos);

        // then
        assertThat(result.get("repo1"), is(60.0));
    }

    @Test
    public void testGetLanguageDominance() {
        // given
        Language java = new Language();
        java.setName("Java");

        GithubRepository weakJava = createMockRepo("weakJava", 0, 0, 0, 100, true);
        weakJava.setLanguage(java);

        GithubRepository strongJava = createMockRepo("strongJava", 0, 0, 0, 500, true);
        strongJava.setLanguage(java);

        List<GithubRepository> repos = Arrays.asList(weakJava, strongJava);

        // when
        Map<String, String> result = statisticsService.getLanguageDominance(repos);

        // then
        assertThat(result.get("Java"), is("strongJava"));
    }

    @Test
    public void testGetComplexityCategories() {
        // given
        GithubRepository legacyRepo = createMockRepo("legacy", 0, 0, 1500, 0, false);
        GithubRepository highCompRepo = createMockRepo("high", 0, 20000, 5000, 0, true);
        GithubRepository standardRepo = createMockRepo("standard", 0, 100, 100, 0, true);

        List<GithubRepository> repos = Arrays.asList(legacyRepo, highCompRepo, standardRepo);

        // when
        Map<String, String> result = statisticsService.getComplexityCategories(repos);

        // then
        assertThat(result.get("legacy"), is("LEGACY"));
        assertThat(result.get("high"), is("HIGH_COMPLEXITY"));
        assertThat(result.get("standard"), is("STANDARD"));
    }

    @Test
    public void testGetOwnerPopularitySums() {
        // given
        Owner owner = new Owner();
        owner.setLogin("mendelu-owner");

        GithubRepository repo1 = createMockRepo("repo1", 300, 0, 0, 0, true);
        repo1.setOwner(owner);

        GithubRepository repo2 = createMockRepo("repo2", 700, 0, 0, 0, true);
        repo2.setOwner(owner);

        List<GithubRepository> repos = Arrays.asList(repo1, repo2);

        // when
        Map<String, Long> result = statisticsService.getOwnerPopularitySums(repos);

        // then
        assertThat(result.get("mendelu-owner"), is(1000L));
    }
}