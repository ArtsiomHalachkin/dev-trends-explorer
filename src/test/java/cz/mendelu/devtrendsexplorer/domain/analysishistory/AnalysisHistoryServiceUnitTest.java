package cz.mendelu.devtrendsexplorer.domain.analysishistory;

import cz.mendelu.devtrendsexplorer.domain.githubrepository.GithubRepository;
import cz.mendelu.devtrendsexplorer.domain.githubrepository.GitHubRepositoryRepository;
import cz.mendelu.devtrendsexplorer.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AnalysisHistoryServiceUnitTest {

    @Test
    public void testRunAnalysis() {
        // given
        GithubRepository repo = new GithubRepository();
        repo.setId(1L);
        repo.setName("awesome-go");
        repo.setStars(10000);
        repo.setForks(500);
        repo.setIssues(50);

        AnalysisHistoryRepository analysisHistoryRepository = mock(AnalysisHistoryRepository.class);
        GitHubRepositoryRepository repoRepository = mock(GitHubRepositoryRepository.class);

        when(repoRepository.findById(1L)).thenReturn(Optional.of(repo));
        when(analysisHistoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        AnalysisHistoryService service = new AnalysisHistoryService(analysisHistoryRepository, repoRepository);

        // when
        AnalysisHistory result = service.runAnalysis(1L);

        // then
        double expectedScore = 46.31;

        assertThat(result.getCalculatedScore(), is(expectedScore));
        assertThat(result.getRepository(), is(repo));
        verify(analysisHistoryRepository).save(result);
    }
    @Test
    public void testRunAnalysis_NotFound() {
        // given
        AnalysisHistoryRepository analysisHistoryRepository = mock(AnalysisHistoryRepository.class);
        GitHubRepositoryRepository repoRepository = mock(GitHubRepositoryRepository.class);

        when(repoRepository.findById(999L)).thenReturn(Optional.empty());

        AnalysisHistoryService service = new AnalysisHistoryService(analysisHistoryRepository, repoRepository);

        // when + then
        assertThrows(NotFoundException.class, () -> service.runAnalysis(999L));
        verify(analysisHistoryRepository, never()).save(any());
    }

    @Test
    public void testGetHistory() {
        // given
        AnalysisHistory entry1 = new AnalysisHistory();
        entry1.setCalculatedScore(195.1);
        AnalysisHistory entry2 = new AnalysisHistory();
        entry2.setCalculatedScore(42.0);

        AnalysisHistoryRepository analysisHistoryRepository = mock(AnalysisHistoryRepository.class);
        GitHubRepositoryRepository repoRepository = mock(GitHubRepositoryRepository.class);

        when(analysisHistoryRepository.findAllByOrderByTimestampDesc()).thenReturn(List.of(entry1, entry2));

        AnalysisHistoryService service = new AnalysisHistoryService(analysisHistoryRepository, repoRepository);

        // when
        List<AnalysisHistory> result = service.getHistory();

        // then
        assertThat(result, hasSize(2));
        assertThat(result.get(0).getCalculatedScore(), is(195.1));
        assertThat(result.get(1).getCalculatedScore(), is(42.0));
        verify(analysisHistoryRepository).findAllByOrderByTimestampDesc();
    }
}