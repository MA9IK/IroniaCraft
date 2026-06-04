package dev.ironia.ironiacraft.repository;

import dev.ironia.ironiacraft.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByTelegramChatId(Long chatId);
    boolean existsByTelegramChatId(Long chatId);
    Optional<User> findByUsername(String username);
}
