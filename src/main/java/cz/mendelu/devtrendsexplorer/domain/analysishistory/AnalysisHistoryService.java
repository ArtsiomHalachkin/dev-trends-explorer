package cz.mendelu.devtrendsexplorer.domain.analysishistory;

import cz.mendelu.devtrendsexplorer.domain.githubrepository.GithubRepository;
import cz.mendelu.devtrendsexplorer.domain.githubrepository.GitHubRepositoryRepository;
import cz.mendelu.devtrendsexplorer.utils.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisHistoryService {

    private final AnalysisHistoryRepository analysisHistoryRepository;
    private final GitHubRepositoryRepository repoRepository;

    @CacheEvict(value = "analysisHistory", allEntries = true)
    @Transactional
    public AnalysisHistory runAnalysis(Long repoId) {
        GithubRepository repo = repoRepository.findById(repoId)
                .orElseThrow(NotFoundException::new);

        double raw = (double) (repo.getStars() + repo.getForks()) / (repo.getIssues() + 1);
        double normalized = 100.0 * Math.log1p(raw) / Math.log1p(100_000.0);
        double roundedScore = Math.min(100.0, Math.round(normalized * 100.0) / 100.0);

        AnalysisHistory entry = new AnalysisHistory();
        entry.setRepository(repo);
        entry.setCalculatedScore(roundedScore);
        entry.setTimestamp(LocalDateTime.now());

        log.info("Analysis run for repo '{}': score={}", repo.getName(), roundedScore);
        return analysisHistoryRepository.save(entry);
    }

    @Cacheable(value = "analysisHistory")
    @Transactional(readOnly = true)
    public List<AnalysisHistory> getHistory() {
        return analysisHistoryRepository.findAllByOrderByTimestampDesc();
    }

    @CacheEvict(value = "analysisHistory", allEntries = true)
    @Transactional
    public void deleteHistory(Long id) {
        if (!analysisHistoryRepository.existsById(id)) throw new NotFoundException();
        analysisHistoryRepository.deleteById(id);
    }
}
