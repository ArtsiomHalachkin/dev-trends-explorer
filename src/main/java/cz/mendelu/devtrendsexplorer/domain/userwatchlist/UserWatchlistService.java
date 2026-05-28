package cz.mendelu.devtrendsexplorer.domain.userwatchlist;

import cz.mendelu.devtrendsexplorer.domain.githubrepository.GithubRepository;
import cz.mendelu.devtrendsexplorer.domain.githubrepository.GitHubRepositoryRepository;
import cz.mendelu.devtrendsexplorer.utils.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserWatchlistService {

    private final UserWatchlistRepository watchlistRepository;
    private final GitHubRepositoryRepository repoRepository;

    @Transactional(readOnly = true)
    public List<UserWatchlist> getFavorites(String userId) {
        return watchlistRepository.findAllByUserIdWithRepository(userId);
    }

    @Transactional
    public UserWatchlist addFavorite(String userId, Long repoId) {
        GithubRepository repo = repoRepository.findById(repoId)
                .orElseThrow(NotFoundException::new);

        if (watchlistRepository.existsByUserIdAndRepositoryId(userId, repoId)) {
            log.info("Repo '{}' is already in favorites", repo.getName());
            return watchlistRepository.findByUserIdAndRepositoryIdWithRepository(userId, repoId)
                    .orElseThrow(NotFoundException::new);
        }

        UserWatchlist entry = new UserWatchlist();
        entry.setUserId(userId);
        entry.setRepository(repo);

        log.info("Added repo '{}' to favorites", repo.getName());
        return watchlistRepository.save(entry);
    }

    @Transactional
    public void removeFavorite(String userId, Long repoId) {
        if (!watchlistRepository.existsByUserIdAndRepositoryId(userId, repoId)) {
            throw new NotFoundException();
        }
        watchlistRepository.deleteByUserIdAndRepositoryId(userId, repoId);
        log.info("Removed repo id={} from favorites", repoId);
    }
}
