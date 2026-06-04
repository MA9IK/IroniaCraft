package dev.ironia.ironiacraft.service;

import dev.ironia.ironiacraft.dto.user.UserResponse;
import dev.ironia.ironiacraft.exception.UserNotFoundException;
import dev.ironia.ironiacraft.model.User;
import dev.ironia.ironiacraft.repository.UserRepository;
import dev.ironia.ironiacraft.types.Role;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse authenticateUser(String username) {
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (existingUser.isPresent()) {
            return toResponse(existingUser.get());
        }

        User user = new User();
        user.setUsername(username);
        user.setRole(Role.ROLE_USER);
        User saved = userRepository.save(user);

        return toResponse(saved);
    }

    private UserResponse findByChatId(Long chatId) {
        return userRepository.findByTelegramChatId(chatId)
                .map(this::toResponse)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getTelegramChatId(),
            user.getUsername(),
            user.getRole(),
            user.getCreatedAt()
        );
    }

}
