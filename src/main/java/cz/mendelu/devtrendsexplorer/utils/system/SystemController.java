package cz.mendelu.devtrendsexplorer.utils.system;

import cz.mendelu.devtrendsexplorer.domain.githubrepository.GitHubRepositoryRepository;
import cz.mendelu.devtrendsexplorer.utils.response.ObjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/system")
@RequiredArgsConstructor
public class SystemController {

    private final GitHubRepositoryRepository repoRepository;

    @GetMapping("/info")
    public ObjectResponse<Map<String, Object>> getInfo() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("appName", "DevTrendsExplorer");
        info.put("version", "0.0.1-SNAPSHOT");
        info.put("repositoryCount", repoRepository.count());
        return ObjectResponse.of(info, i -> i);
    }
}
