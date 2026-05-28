package cz.mendelu.devtrendsexplorer.domain.userwatchlist;

import cz.mendelu.devtrendsexplorer.domain.githubrepository.GithubRepository;
import cz.mendelu.devtrendsexplorer.domain.githubrepository.GitHubRepositoryRepository;
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
public class UserWatchlistServiceUnitTest {

    @Mock
    private UserWatchlistRepository watchlistRepository;
    @Mock
    private GitHubRepositoryRepository repoRepository;

    @InjectMocks
    private UserWatchlistService service;

    @Test
    public void testGetFavorites() {
        // given
        GithubRepository repo = new GithubRepository();
        repo.setId(1L);
        repo.setName("awesome-go");

        UserWatchlist entry = new UserWatchlist();
        entry.setUserId("user-123");
        entry.setRepository(repo);

        when(watchlistRepository.findAllByUserIdWithRepository("user-123"))
                .thenReturn(List.of(entry));

        // when
        List<UserWatchlist> result = service.getFavorites("user-123");

        // then
        assertThat(result, hasSize(1));
        assertThat(result.get(0).getUserId(), is("user-123"));
        assertThat(result.get(0).getRepository().getName(), is("awesome-go"));
        verify(watchlistRepository).findAllByUserIdWithRepository("user-123");
    }

    @Test
    public void testGetFavorites_Empty() {
        // given
        when(watchlistRepository.findAllByUserIdWithRepository("user-123"))
                .thenReturn(List.of());

        // when
        List<UserWatchlist> result = service.getFavorites("user-123");

        // then
        assertThat(result, hasSize(0));
        verify(watchlistRepository).findAllByUserIdWithRepository("user-123");
    }

    @Test
    public void testAddFavorite() {
        // given
        GithubRepository repo = new GithubRepository();
        repo.setId(1L);
        repo.setName("awesome-go");

        when(repoRepository.findById(1L)).thenReturn(Optional.of(repo));
        when(watchlistRepository.existsByUserIdAndRepositoryId("user-123", 1L)).thenReturn(false);
        when(watchlistRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        UserWatchlist result = service.addFavorite("user-123", 1L);

        // then
        assertThat(result.getUserId(), is("user-123"));
        assertThat(result.getRepository().getName(), is("awesome-go"));
        verify(watchlistRepository).save(result);
    }

    @Test
    public void testAddFavorite_AlreadyExists() {
        // given
        GithubRepository repo = new GithubRepository();
        repo.setId(1L);
        repo.setName("awesome-go");

        UserWatchlist existing = new UserWatchlist();
        existing.setUserId("user-123");
        existing.setRepository(repo);

        when(repoRepository.findById(1L)).thenReturn(Optional.of(repo));
        when(watchlistRepository.existsByUserIdAndRepositoryId("user-123", 1L)).thenReturn(true);
        when(watchlistRepository.findByUserIdAndRepositoryIdWithRepository("user-123", 1L))
                .thenReturn(Optional.of(existing));

        // when
        UserWatchlist result = service.addFavorite("user-123", 1L);

        // then — existing entry returned, nothing saved
        assertThat(result.getUserId(), is("user-123"));
        assertThat(result.getRepository().getName(), is("awesome-go"));
        verify(watchlistRepository, never()).save(any());
    }

    @Test
    public void testAddFavorite_RepoNotFound() {
        // given
        when(repoRepository.findById(999L)).thenReturn(Optional.empty());

        // when + then
        assertThrows(NotFoundException.class, () -> service.addFavorite("user-123", 999L));
        verify(watchlistRepository, never()).save(any());
    }

    @Test
    public void testRemoveFavorite() {
        // given
        when(watchlistRepository.existsByUserIdAndRepositoryId("user-123", 1L)).thenReturn(true);

        // when
        service.removeFavorite("user-123", 1L);

        // then
        verify(watchlistRepository).deleteByUserIdAndRepositoryId("user-123", 1L);
    }

    @Test
    public void testRemoveFavorite_NotFound() {
        // given
        when(watchlistRepository.existsByUserIdAndRepositoryId("user-123", 999L)).thenReturn(false);

        // when + then
        assertThrows(NotFoundException.class, () -> service.removeFavorite("user-123", 999L));
        verify(watchlistRepository, never()).deleteByUserIdAndRepositoryId(any(), any());
    }
}