package cz.mendelu.devtrendsexplorer.domain.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class Statistics {

    List<String> namesOfTopStarredRepos = new ArrayList<>();

    private Map<String, Double> healthScoreOfEachRepo;
    private Map<String, Double> engagementScoreOfEachRepo;
    private Map<String, String> languageDominanceRepos;
    private Map<String, String> complexityCategories;
    private Map<String, Long> ownerPopularitySums;

    public Statistics() {}
}