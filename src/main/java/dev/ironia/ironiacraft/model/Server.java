package dev.ironia.ironiacraft.model;

import dev.ironia.ironiacraft.types.ServerStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "servers")
@Data
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(name = "modpack_path")
    private String modpackPath;
    @Column(name = "minecraft_version")
    private String minecraftVersion;
    @Column(name = "java_version")
    private int javaVersion;
    private int port;
    @Column(name = "ram_mb")
    private int ramMb;

    @Enumerated(EnumType.STRING)
    private ServerStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
