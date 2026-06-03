package dev.ironia.ironiacraft.dto.user;

import dev.ironia.ironiacraft.types.Role;

import java.time.LocalDateTime;

public class UserResponse {
    private Long id;
    private Long telegramChatId;
    private String username;
    private Role role;
    private LocalDateTime createdAt;

    public UserResponse(Long id, Long telegramChatId, String username, Role role, LocalDateTime createdAt) {
        this.id = id;
        this.telegramChatId = telegramChatId;
        this.username = username;
        this.role = role;
        this.createdAt = createdAt;
    }

    public Long getTelegramChatId() {
        return telegramChatId;
    }
    public String getUsername() {
        return username;
    }
    public Long getId() {
        return id;
    }
    public Role getRole() {
        return role;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
