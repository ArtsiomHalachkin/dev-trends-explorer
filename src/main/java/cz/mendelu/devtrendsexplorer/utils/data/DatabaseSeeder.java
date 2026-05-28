package cz.mendelu.devtrendsexplorer.utils.data;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import cz.mendelu.devtrendsexplorer.domain.githubrepository.GitHubRepositoryRepository;
import cz.mendelu.devtrendsexplorer.domain.githubrepository.GithubRepository;
import cz.mendelu.devtrendsexplorer.domain.repositorydomain.Domain;
import cz.mendelu.devtrendsexplorer.domain.repositorydomain.DomainRepository;
import cz.mendelu.devtrendsexplorer.domain.repositorylanguage.Language;
import cz.mendelu.devtrendsexplorer.domain.repositorylanguage.LanguageRepository;
import cz.mendelu.devtrendsexplorer.domain.repositoryowner.Owner;
import cz.mendelu.devtrendsexplorer.domain.repositoryowner.OwnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final GitHubRepositoryRepository repoRepository;
    private final OwnerRepository ownerRepository;
    private final LanguageRepository languageRepository;
    private final DomainRepository domainRepository;

    @Override
    public void run(String... args) {
        if (repoRepository.count() > 0) {
            log.info("Database already contains data. Skipping CSV import.");
            return;
        }

        log.info("Starting Github CSV data import...");
        try {
            InputStream is = new ClassPathResource("github_top_repositories.csv").getInputStream();
            importCsv(is);
        } catch (Exception e) {
            log.error("Failed to read main CSV file: {}", e.getMessage(), e);
        }
    }

    public int importCsv(InputStream inputStream) {
        int count = 0;
        try (InputStreamReader isr = new InputStreamReader(inputStream);
             CSVReader csvReader = new CSVReaderBuilder(isr).withSkipLines(1).build()) {

            String[] columns;

            while ((columns = csvReader.readNext()) != null) {
                if (columns.length < 18) continue;

                String domainName = columns[0].trim();
                String repoName = columns[1].trim();
                String fullName = columns[2].trim();
                String description = columns[3].trim();
                String languageName = columns[4].trim();

                int stars = parseIntSafe(columns[5]);
                int forks = parseIntSafe(columns[6]);
                int watchers = parseIntSafe(columns[7]);
                int issues = parseIntSafe(columns[8]);
                boolean hasWiki = Boolean.parseBoolean(columns[9]);
                String ownerLogin = columns[17].trim();

                Domain domain = domainRepository.findByName(domainName)
                        .orElseGet(() -> {
                            Domain newDomain = new Domain();
                            newDomain.setName(domainName);
                            return domainRepository.save(newDomain);
                        });

                Owner owner = ownerRepository.findByLogin(ownerLogin)
                        .orElseGet(() -> {
                            Owner newOwner = new Owner();
                            newOwner.setLogin(ownerLogin);
                            return ownerRepository.save(newOwner);
                        });

                Language language = null;
                if (!languageName.isEmpty()) {
                    language = languageRepository.findByName(languageName)
                            .orElseGet(() -> {
                                Language newLang = new Language();
                                newLang.setName(languageName);
                                return languageRepository.save(newLang);
                            });
                }

                GithubRepository repo = new GithubRepository();
                repo.setName(repoName);
                repo.setFullName(fullName);
                repo.setDescription(description);
                repo.setStars(stars);
                repo.setForks(forks);
                repo.setWatchers(watchers);
                repo.setIssues(issues);
                repo.setHasWiki(hasWiki);

                repo.setDomain(domain);
                repo.setOwner(owner);
                repo.setLanguage(language);

                repoRepository.save(repo);
                count++;
            }
            log.info("CSV Import completed successfully! Imported {} repositories.", count);
            return count;

        } catch (Exception e) {
            log.error("Failed to parse CSV data: {}", e.getMessage(), e);
            return 0;
        }
    }

    private int parseIntSafe(String value) {
        try {
            return value == null || value.trim().isEmpty() ? 0 : Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}