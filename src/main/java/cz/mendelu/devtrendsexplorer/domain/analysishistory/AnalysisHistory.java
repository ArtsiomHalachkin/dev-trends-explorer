package cz.mendelu.devtrendsexplorer.domain.analysishistory;

import cz.mendelu.devtrendsexplorer.domain.githubrepository.GithubRepository;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "analysis_history")
@Data
public class AnalysisHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repo_id", nullable = false)
    private GithubRepository repository;

    @Column(name = "calculated_score")
    private double calculatedScore;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
