package dev.ironia.ironiacraft.dto.server;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServerRequest {

    @NotBlank(message = "Server name cannot be empty")
    private String name;

    private String modpackPath;

    @NotBlank(message = "Minecraft Version cannot be empty")
    private String minecraftVersion;

    private int javaVersion = 21;

    @NotNull
    @Min(value = 1024, message = "Port must be above 1024")
    @Max(value = 65535, message = "Port must be below 65535")
    private int port;

    @Min(value = 512, message = "Minimum RAM is 512MB")
    @Max(value = 32768, message = "Maximum RAM is 32GB")
    private int ramMb = 4096;
}
