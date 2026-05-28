package cz.mendelu.devtrendsexplorer.domain.githubrepository;

import cz.mendelu.devtrendsexplorer.config.RateLimited;
import cz.mendelu.devtrendsexplorer.utils.response.ArrayResponse;
import cz.mendelu.devtrendsexplorer.utils.response.ObjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/repos")
@RequiredArgsConstructor
@RateLimited
@Tag(name = "Repositories", description = "CRUD operations for GitHub repositories")
public class GithubRepositoryController {

    private final GithubRepositoryService service;

    @GetMapping
    @Operation(summary = "Get all repositories")
    @ApiResponse(responseCode = "200", description = "List of all repositories")
    @Cacheable("repositories")
    public ArrayResponse<GithubRepositoryDTO> getAll() {
        return ArrayResponse.of(service.findAll(), GithubRepositoryDTO::fromEntity);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a repository by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Repository found"),
            @ApiResponse(responseCode = "404", description = "Repository not found")
    })
    public ObjectResponse<GithubRepositoryDTO> getById(@PathVariable Long id) {
        return ObjectResponse.of(service.findById(id), GithubRepositoryDTO::fromEntity);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new repository")
    @ApiResponse(responseCode = "201", description = "Repository successfully created")
    @CacheEvict(value = {"repositories", "globalStatistics"}, allEntries = true)
    public ObjectResponse<GithubRepositoryDTO> create(@Valid @RequestBody GithubRepositoryCreateDTO dto) {
        return ObjectResponse.of(service.create(dto), GithubRepositoryDTO::fromEntity);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing repository")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Repository successfully updated"),
            @ApiResponse(responseCode = "404", description = "Repository not found")
    })
    @CacheEvict(value = {"repositories", "globalStatistics"}, allEntries = true)
    public ObjectResponse<GithubRepositoryDTO> update(@PathVariable Long id,
                                                      @Valid @RequestBody GithubRepositoryCreateDTO dto) {
        return ObjectResponse.of(service.update(id, dto), GithubRepositoryDTO::fromEntity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a repository by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Repository successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Repository not found")
    })
    @CacheEvict(value = {"repositories", "globalStatistics"}, allEntries = true)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}