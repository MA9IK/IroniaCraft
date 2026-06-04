package dev.ironia.ironiacraft.dto.user;

import dev.ironia.ironiacraft.types.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private Long telegramChatId;
    private String username;
    private Role role;
    private LocalDateTime createdAt;
}
