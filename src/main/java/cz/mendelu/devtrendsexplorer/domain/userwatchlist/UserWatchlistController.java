package cz.mendelu.devtrendsexplorer.domain.userwatchlist;

import cz.mendelu.devtrendsexplorer.config.RateLimited;
import cz.mendelu.devtrendsexplorer.utils.response.ArrayResponse;
import cz.mendelu.devtrendsexplorer.utils.response.ObjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@RateLimited
@Tag(name = "Favorites", description = "User's favorite repositories management")
public class UserWatchlistController {

    private final UserWatchlistService service;

    @GetMapping
    @Operation(summary = "Get all favorite repositories for the current user")
    @ApiResponse(responseCode = "200", description = "List of favorite repositories")
    public ArrayResponse<UserWatchlistDTO> getFavorites(@AuthenticationPrincipal Jwt jwt) {

        String userId = jwt.getSubject();
        return ArrayResponse.of(service.getFavorites(userId), UserWatchlistDTO::fromEntity);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a repository to favorites")
    public ObjectResponse<UserWatchlistDTO> addFavorite(@PathVariable Long id,
                                                        @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return ObjectResponse.of(service.addFavorite(userId, id), UserWatchlistDTO::fromEntity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove a repository from favorites")
    public void removeFavorite(@PathVariable Long id,
                               @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        service.removeFavorite(userId, id);
    }
}