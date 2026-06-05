package dev.ironia.ironiacraft.infrastructure.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class JavaPathResolver {

    private static final Logger log = LoggerFactory.getLogger(JavaPathResolver.class);

    @Value("${ironiacraft.java.java8-path}")
    private String java8Path;

    @Value("${ironiacraft.java.java17-path}")
    private String java17Path;

    @Value("${ironiacraft.java.java21-path}")
    private String java21Path;

    public String getJavaExecutable(int javaVersion) {
        String executablePath = switch (javaVersion) {
            case 8 -> java8Path;
            case 17 -> java17Path;
            case 21 -> java21Path;
            default -> throw new IllegalArgumentException("Unsupported Java version requested: " + javaVersion + ". Allowed: 8, 17, 21.");
        };

        if (!executablePath.equals("java")) {
            Path path = Paths.get(executablePath);
            if (!Files.exists(path) || !Files.isExecutable(path)) {
                log.error("Configured Java {} path does not exist or is not executable: {}", javaVersion, executablePath);
                throw new RuntimeException("Server cannot start: Java " + javaVersion + " environment is improperly configured.");
            }
        }
        return executablePath;
    }
}
