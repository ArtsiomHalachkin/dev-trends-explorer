package cz.mendelu.devtrendsexplorer.domain.repositorylanguage;

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
@RequestMapping("/api/languages")
@RequiredArgsConstructor
@Tag(name = "Languages", description = "Operations related to programming languages")
public class LanguageController {

    private final LanguageRepository languageRepository;

    @GetMapping
    @Operation(summary = "Get all languages")
    @ApiResponse(responseCode = "200", description = "List of all languages")
    @Cacheable("languages")
    public ArrayResponse<LanguageDTO> getAll() {
        List<Language> languages = new ArrayList<>();
        languageRepository.findAll().forEach(languages::add);
        return ArrayResponse.of(languages, LanguageDTO::fromEntity);
    }
}