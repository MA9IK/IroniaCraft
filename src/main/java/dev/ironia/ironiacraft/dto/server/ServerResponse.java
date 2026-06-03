package dev.ironia.ironiacraft.dto.server;

import dev.ironia.ironiacraft.model.User;
import dev.ironia.ironiacraft.types.ServerStatus;

import java.time.LocalDateTime;

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

    public ServerResponse(LocalDateTime createdAt, Long ownerId, String ownerUsername, ServerStatus status, int ramMb, int port, int javaVersion, String minecraftVersion, String modpackPath, String name, Long id) {
        this.createdAt = createdAt;
        this.ownerId = ownerId;
        this.ownerUsername = ownerUsername;
        this.status = status;
        this.ramMb = ramMb;
        this.port = port;
        this.javaVersion = javaVersion;
        this.minecraftVersion = minecraftVersion;
        this.modpackPath = modpackPath;
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModpackPath() {
        return modpackPath;
    }

    public void setModpackPath(String modpackPath) {
        this.modpackPath = modpackPath;
    }

    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    public void setMinecraftVersion(String minecraftVersion) {
        this.minecraftVersion = minecraftVersion;
    }

    public int getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(int javaVersion) {
        this.javaVersion = javaVersion;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getRamMb() {
        return ramMb;
    }

    public void setRamMb(int ramMb) {
        this.ramMb = ramMb;
    }

    public ServerStatus getStatus() {
        return status;
    }

    public void setStatus(ServerStatus status) {
        this.status = status;
    }
}
