package cz.mendelu.devtrendsexplorer.domain.repositorydomain;

import cz.mendelu.devtrendsexplorer.utils.response.ArrayResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/domains")
@RequiredArgsConstructor
@Tag(name = "Domains", description = "Operations related to repository domains")
public class DomainController {

    private final DomainRepository domainRepository;

    @GetMapping
    @Operation(summary = "Get all domains")
    @ApiResponse(responseCode = "200", description = "List of all domains")
    @Cacheable("domains")
    public ArrayResponse<DomainDTO> getAll() {
        List<Domain> domains = new ArrayList<>();
        domainRepository.findAll().forEach(domains::add);
        return ArrayResponse.of(domains, DomainDTO::fromEntity);
    }
}