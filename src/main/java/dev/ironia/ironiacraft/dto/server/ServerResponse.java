package dev.ironia.ironiacraft.dto.server;

import dev.ironia.ironiacraft.types.ServerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServerResponse {
    private Long id;
    private String name;
    private String modpackPath;
    private String minecraftVersion;
    private int javaVersion;
    private int port;
    private int ramMb;
    private ServerStatus status;
    private Long ownerId;
    private String ownerUsername;
    private LocalDateTime createdAt;
}
