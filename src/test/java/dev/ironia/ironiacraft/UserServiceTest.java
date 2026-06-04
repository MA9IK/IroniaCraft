package dev.ironia.ironiacraft;

import dev.ironia.ironiacraft.dto.user.UserResponse;
import dev.ironia.ironiacraft.model.User;
import dev.ironia.ironiacraft.repository.UserRepository;
import dev.ironia.ironiacraft.service.UserService;
import dev.ironia.ironiacraft.types.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void authenticateUser_UserExists_ReturnsExistingUser() {
        String username = "Notch";
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername(username);
        existingUser.setRole(Role.ROLE_USER);
        existingUser.setCreatedAt(LocalDateTime.now());

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));

        UserResponse response = userService.authenticateUser(username);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(username, response.getUsername());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void authenticateUser_UserDoesNotExist_RegistersAndReturnsNewUser() {
        String username = "Jeb";
        User savedUser = new User();
        savedUser.setId(2L);
        savedUser.setUsername(username);
        savedUser.setRole(Role.ROLE_USER);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.authenticateUser(username);

        assertNotNull(response);
        assertEquals(2L, response.getId());
        assertEquals(username, response.getUsername());

        verify(userRepository, times(1)).save(any(User.class));
    }
}
