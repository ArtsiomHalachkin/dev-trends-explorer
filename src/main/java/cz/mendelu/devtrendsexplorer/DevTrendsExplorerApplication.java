package cz.mendelu.devtrendsexplorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DevTrendsExplorerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevTrendsExplorerApplication.class, args);
    }

}
