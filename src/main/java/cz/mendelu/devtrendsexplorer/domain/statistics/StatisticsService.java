package cz.mendelu.devtrendsexplorer.domain.statistics;

import cz.mendelu.devtrendsexplorer.domain.githubrepository.GithubRepository;
import cz.mendelu.devtrendsexplorer.domain.githubrepository.GitHubRepositoryRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.StreamSupport;

@Service
public class StatisticsService {

    private final GitHubRepositoryRepository repositoryRepository;

    public StatisticsService(GitHubRepositoryRepository repositoryRepository) {
        this.repositoryRepository = repositoryRepository;
    }

    public Map<String, Double> getHealthScores(Iterable<GithubRepository> repos) {
        final double REFERENCE = 100_000.0;
        Map<String, Double> results = new HashMap<>();
        repos.forEach(repo -> {
            double raw = (double) (repo.getStars() + repo.getForks()) / (repo.getIssues() + 1);
            double normalized = 100.0 * Math.log1p(raw) / Math.log1p(REFERENCE);
            double score = Math.min(100.0, Math.round(normalized * 100.0) / 100.0);
            results.put(repo.getName(), score);
        });
        return results;
    }

    public Map<String, Double> getEngagementScores(Iterable<GithubRepository> repos) {
        Map<String, Double> results = new HashMap<>();
        repos.forEach(repo -> {
            double score = repo.getForks() + repo.getWatchers();

            double roundedScore = Math.round(score * 100.0) / 100.0;
            results.put(repo.getName(), roundedScore);
        });
        return results;
    }

    public Map<String, String> getLanguageDominance(List<GithubRepository> repos) {
        Map<String, GithubRepository> dominantByLanguage = new HashMap<>();
        for (GithubRepository repo : repos) {
            if (repo.getLanguage() == null) continue;
            String lang = repo.getLanguage().getName();
            GithubRepository current = dominantByLanguage.get(lang);
            if (current == null || repo.getWatchers() > current.getWatchers()) {
                dominantByLanguage.put(lang, repo);
            }
        }
        Map<String, String> result = new LinkedHashMap<>();
        dominantByLanguage.forEach((lang, repo) -> result.put(lang, repo.getName()));
        return result;
    }

    public Map<String, String> getComplexityCategories(List<GithubRepository> repos) {
        Map<String, String> result = new LinkedHashMap<>();
        for (GithubRepository repo : repos) {
            String category;
            int complexityFactor = repo.getIssues() + repo.getForks();

            if (repo.getIssues() > 1000 && !repo.isHasWiki()) {
                category = "LEGACY";
            } else if (complexityFactor > 20000) {
                category = "HIGH_COMPLEXITY";
            } else if (complexityFactor > 5000) {
                category = "MEDIUM_COMPLEXITY";
            } else {
                category = "STANDARD";
            }
            result.put(repo.getName(), category);
        }
        return result;
    }

    public Map<String, Long> getOwnerPopularitySums(List<GithubRepository> repos) {
        Map<String, Long> result = new LinkedHashMap<>();
        for (GithubRepository repo : repos) {
            if (repo.getOwner() == null) continue;
            String ownerLogin = repo.getOwner().getLogin();
            result.merge(ownerLogin, (long) repo.getStars(), Long::sum);
        }
        return result;
    }

    @Cacheable("globalStatistics")
    public Statistics getStatistics() {
        List<GithubRepository> allRepos = StreamSupport
                .stream(repositoryRepository.findAll().spliterator(), false)
                .toList();

        return new Statistics(
                repositoryRepository.findNameOfReposSortedByStars(),
                getHealthScores(allRepos),
                getEngagementScores(allRepos),
                getLanguageDominance(allRepos),
                getComplexityCategories(allRepos),
                getOwnerPopularitySums(allRepos)
        );
    }
}
