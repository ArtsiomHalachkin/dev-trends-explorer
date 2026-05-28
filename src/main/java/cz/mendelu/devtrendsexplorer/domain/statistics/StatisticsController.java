package cz.mendelu.devtrendsexplorer.domain.statistics;

import cz.mendelu.devtrendsexplorer.config.RateLimited;
import cz.mendelu.devtrendsexplorer.utils.response.ArrayResponse;
import cz.mendelu.devtrendsexplorer.utils.response.ObjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/repos/stats")
@Validated
@RequiredArgsConstructor
@RateLimited
@Tag(name = "Statistics", description = "Calculations and analytics for GitHub repositories")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping(value = "", produces = "application/json")
    @Operation(summary = "Get all statistics at once")
    @ApiResponse(responseCode = "200", description = "Statistics successfully calculated")
    public ObjectResponse<Statistics> getStatistics() {
        var statistics = statisticsService.getStatistics();
        return new ObjectResponse<>(statistics, 1);
    }

    @GetMapping(value = "/top-stars", produces = "application/json")
    @Operation(summary = "Get top starred repositories")
    @ApiResponse(responseCode = "200", description = "List of top starred repositories")
    public ArrayResponse<String> getTopStars() {
        List<String> topStars = statisticsService.getStatistics().getNamesOfTopStarredRepos();
        return ArrayResponse.of(topStars, name -> name);
    }

    @GetMapping(value = "/health", produces = "application/json")
    @Operation(summary = "Get health scores for repositories")
    @ApiResponse(responseCode = "200", description = "Map of repository health scores")
    public ObjectResponse<Map<String, Double>> getHealthScores() {
        Map<String, Double> health = statisticsService.getStatistics().getHealthScoreOfEachRepo();
        return new ObjectResponse<>(health, 1);
    }

    @GetMapping(value = "/languages", produces = "application/json")
    @Operation(summary = "Get dominant repositories by language")
    @ApiResponse(responseCode = "200", description = "Map of dominant repositories per language")
    public ObjectResponse<Map<String, String>> getLanguageDominance() {
        Map<String, String> languages = statisticsService.getStatistics().getLanguageDominanceRepos();
        return new ObjectResponse<>(languages, 1);
    }
}