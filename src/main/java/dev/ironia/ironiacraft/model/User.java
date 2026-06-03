package dev.ironia.ironiacraft.model;

import dev.ironia.ironiacraft.types.Role;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "telegram_chat_id", unique = true, nullable = false)
    private Long telegramChatId;

    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Role getRole() {
        return role;
    }

    public Long getId() {
        return id;
    }

    public Long getTelegramChatId() {
        return telegramChatId;
    }

    public String getUsername() {
        return username;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setTelegramChatId(Long telegramChatId) {
        this.telegramChatId = telegramChatId;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
