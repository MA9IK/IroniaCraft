package dev.ironia.ironiacraft.service;

import dev.ironia.ironiacraft.dto.server.ServerResponse;
import dev.ironia.ironiacraft.exception.ServerNotFoundException;
import dev.ironia.ironiacraft.exception.UserNotFoundException;
import dev.ironia.ironiacraft.model.Server;
import dev.ironia.ironiacraft.repository.ServerRepository;
import dev.ironia.ironiacraft.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerService {
    private final ServerRepository serverRepository;
    private final UserRepository userRepository;

    public ServerService(ServerRepository serverRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.serverRepository = serverRepository;
    }

    public List<ServerResponse> listUserServers(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }
        return serverRepository.findAllByOwnerId(userId).stream().map(this::toResponse).toList();
    }

    public ServerResponse getServerById(Long id) {
            return serverRepository.findById(id)
                    .map(this::toResponse)
                    .orElseThrow(() -> new ServerNotFoundException("Server not found"));
    }

    private ServerResponse toResponse(Server server) {
        return new ServerResponse(
            server.getId(),
            server.getName(),
                server.getModpackPath(),
                server.getMinecraftVersion(),
                server.getJavaVersion(),
                server.getPort(),
                server.getRamMb(),
                server.getStatus(),
                server.getOwner().getId(),
                server.getOwner().getUsername(),
                server.getCreatedAt()
        );
    }
}
