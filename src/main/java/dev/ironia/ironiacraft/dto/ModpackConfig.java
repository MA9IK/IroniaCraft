package dev.ironia.ironiacraft.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ModpackConfig(
        String name,
        @JsonProperty("minecraft_version") String minecraftVersion,
        @JsonProperty("java_version") int javaVersion,
        @JsonProperty("ram_mb") int ramMb,
        int port,
        @JsonProperty("server_jar") String serverJar,

        String folderName
) {
    public ModpackConfig withFolderName(String folderName) {
        return new ModpackConfig(name, minecraftVersion, javaVersion, ramMb, port, serverJar, folderName);
    }
}
