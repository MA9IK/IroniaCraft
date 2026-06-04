package dev.ironia.ironiacraft;

import dev.ironia.ironiacraft.model.User;
import dev.ironia.ironiacraft.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;


    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        testUser = new User();
        testUser.setTelegramChatId(12312312L);
        testUser.setUsername("Ivan");
        userRepository.save(testUser);
    }

    @Test
    void findByTelegramCharId_shouldReturnUser_whenExists() {
        Optional<User> result = userRepository.findByTelegramChatId(12312312L);

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("Ivan");
    }

    @Test
    void findByTelegramChatIt_shouldReturnEmpty_whenNotExists() {
        Optional<User> result = userRepository.findByTelegramChatId(9999999L);

        assertThat(result).isEmpty();
    }

    @Test
    void existsByTelegramChatId_shouldReturnTrue_whenExists() {
        boolean exists = userRepository.existsByTelegramChatId(12312312L);

        assertThat(exists).isTrue();
    }

    @Test
    void existsByTelegramChatId_shouldReturnFalse_whenNotExists() {
        boolean exists = userRepository.existsByTelegramChatId(99999999L);

        assertThat(exists).isFalse();
    }

    @Test
    void findByUsername_shouldReturnUser_whenExists() {
        Optional<User> result = userRepository.findByUsername("Ivan");

        assertThat(result).isPresent();
        assertThat(result.get().getTelegramChatId()).isEqualTo(12312312L);
    }

    @Test
    void findByUsername_shouldReturnEmpty_whenNotExists() {
        Optional<User> result = userRepository.findByUsername("Notch");

        assertThat(result).isEmpty();
    }
}
