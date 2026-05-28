package cz.mendelu.devtrendsexplorer.domain.analysishistory;

import cz.mendelu.devtrendsexplorer.config.RateLimited;
import cz.mendelu.devtrendsexplorer.utils.response.ArrayResponse;
import cz.mendelu.devtrendsexplorer.utils.response.ObjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
@Tag(name = "Analysis History", description = "Operations related to analysis history of repositories")
public class AnalysisHistoryController {

    private final AnalysisHistoryService service;

    @PostMapping("/run/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @RateLimited
    @Operation(summary = "Run analysis for a specific repository by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Analysis completed and saved"),
            @ApiResponse(responseCode = "404", description = "Repository not found")
    })
    public ObjectResponse<AnalysisHistoryDTO> runAnalysis(@PathVariable Long id) {
        return ObjectResponse.of(service.runAnalysis(id), AnalysisHistoryDTO::fromEntity);
    }

    @GetMapping("/history")
    @Operation(summary = "Get full analysis history")
    @ApiResponse(responseCode = "200", description = "List of all analysis history records")
    public ArrayResponse<AnalysisHistoryDTO> getHistory() {
        return ArrayResponse.of(service.getHistory(), AnalysisHistoryDTO::fromEntity);
    }

    @DeleteMapping("/history/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RateLimited
    @Operation(summary = "Delete a specific analysis history record by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "History record deleted"),
            @ApiResponse(responseCode = "404", description = "History record not found")
    })
    public void deleteHistory(@PathVariable Long id) {
        service.deleteHistory(id);
    }
}