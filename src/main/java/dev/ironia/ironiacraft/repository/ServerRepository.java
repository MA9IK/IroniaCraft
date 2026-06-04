package dev.ironia.ironiacraft.repository;

import dev.ironia.ironiacraft.model.Server;
import dev.ironia.ironiacraft.model.User;
import dev.ironia.ironiacraft.types.ServerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServerRepository extends JpaRepository<Server, Long> {
    List<Server> findAllByOwnerId(Long ownerId);
    List<Server> findByStatus(ServerStatus status);
}
