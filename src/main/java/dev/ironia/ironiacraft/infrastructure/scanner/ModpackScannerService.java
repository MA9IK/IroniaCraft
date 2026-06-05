package dev.ironia.ironiacraft.infrastructure.scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dev.ironia.ironiacraft.dto.ModpackConfig;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


@Service
public class ModpackScannerService {

    private static final Logger log = LoggerFactory.getLogger(ModpackScannerService.class);

    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    @Value("${ironiacraft.modpacks-dir:./modpacks}")
    private String modpacksDirectory;

    public List<ModpackConfig> scanModpacksDirectory() {
        Path basePath = Paths.get(modpacksDirectory);

        if (!Files.exists(basePath)) {
            try {
                Files.createDirectories(basePath);
                log.info("Created base modpacks directory at: {}", basePath.toAbsolutePath());
                return List.of();
            } catch (IOException e) {
                throw new RuntimeException("Could not create modpacks directory", e);
            }
        }

        try (Stream<Path> paths = Files.list(basePath)) {
            return paths
                    .filter(Files::isDirectory)
                    .map(this::processModpackDirectory)
                    .filter(Objects::nonNull)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Failed to scan modpacks directory: " + basePath, e);
        }
    }

    private String extractMinecraftVersion(String jarName) {
        Pattern pattern = Pattern.compile("1\\.\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(jarName);

        if (matcher.find()) {
            return matcher.group();
        }

        log.warn("Could not extract Minecraft version from JAR name: {}. Defaulting to 1.20.4", jarName);
        return "1.20.4";
    }

    private int determineJavaVersion(String mcVersion) {
        try {
            String[] parts = mcVersion.split("\\.");
            if (parts.length >= 2) {
                int minor = Integer.parseInt(parts[1]);

                if (minor <= 16) return 8;
                if (minor == 17) return 16;
                if (minor < 20) return 17;

                if (minor == 20) {
                    if (parts.length == 3 && Integer.parseInt(parts[2]) >= 5) {
                        return 21;
                    }
                    return 17;
                }

                return 21;
            }
        } catch (Exception e) {
            log.warn("Failed to parse minor version from {}. Defaulting to Java 21.", mcVersion);
        }
        return 21;
    }

    private ModpackConfig processModpackDirectory(Path modpackFolder) {
        Path configPath = modpackFolder.resolve("ironia.yml");

        String detectedJar = findServerJar(modpackFolder);

        if (detectedJar == null) {
            log.warn("Skipping folder {}: No valid server/forge/fabric JAR found.", modpackFolder.getFileName());
            return null;
        }
        ensureConfigExists(modpackFolder, detectedJar);

        try {
            ModpackConfig parsedConfig = yamlMapper.readValue(configPath.toFile(), ModpackConfig.class);

            return parsedConfig.withFolderName(modpackFolder.getFileName().toString());
        } catch (IOException e) {
            log.error("Failed to parse ironia.yml in {}", modpackFolder.getFileName(), e);
            return null;
        }
    }

    private String findServerJar(Path modpackFolder) {
        try (Stream<Path> files = Files.list(modpackFolder)) {
            List<String> jarName = files
                    .filter(Files::isRegularFile)
                    .map(p -> p.getFileName().toString())
                    .filter(name -> name.endsWith(".jar"))
                    .toList();

            for (String jar : jarName) if (jar.startsWith("forge-")) return jar;

            for (String jar : jarName) if (jar.startsWith("fabric-") || jar.startsWith("neoforge-")) return jar;

            for (String jar : jarName) if (jar.contains("server")) return jar;
        } catch (IOException e) {
            log.error("Failed to read directory for JARs: {}", modpackFolder, e);
        }
        return null;
    }

    private void ensureConfigExists(Path modpackFolder, String detectedJar) {
        Path configPath = modpackFolder.resolve("ironia.yml");

        if (!Files.exists(configPath)) {
            try {
                String mcVersion = extractMinecraftVersion(detectedJar);
                int javaVersion = determineJavaVersion(mcVersion);

                String defaultConfig = String.format("""
                        name: "%s"
                        minecraft_version: "%s"
                        java_version: %d
                        ram_mb: 8192
                        port: 25565
                        server_jar: "%s"
                        """,
                        modpackFolder.getFileName(),
                        mcVersion,
                        javaVersion,
                        detectedJar);

                Files.writeString(configPath, defaultConfig);
                log.info("Generated default ironia.yml for modpack: {}", modpackFolder.getFileName());
            } catch (IOException e) {
                log.error("Failed to write default ironia.yml for {}", modpackFolder.getFileName());
            }
        }
    }

}
