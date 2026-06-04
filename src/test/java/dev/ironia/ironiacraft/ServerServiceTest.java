package dev.ironia.ironiacraft;

import dev.ironia.ironiacraft.dto.server.ServerResponse;
import dev.ironia.ironiacraft.exception.ServerNotFoundException;
import dev.ironia.ironiacraft.exception.UserNotFoundException;
import dev.ironia.ironiacraft.model.Server;
import dev.ironia.ironiacraft.model.User;
import dev.ironia.ironiacraft.repository.ServerRepository;
import dev.ironia.ironiacraft.repository.UserRepository;
import dev.ironia.ironiacraft.service.ServerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServerServiceTest {

    @Mock
    private ServerRepository serverRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ServerService serverService;

    private User mockUser;
    private Server mockServer;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("Steve");

        mockServer = new Server();
        mockServer.setId(100L);
        mockServer.setName("Vanilla Survival");
        mockServer.setPort(25565);
        mockServer.setOwner(mockUser);
    }

    @Test
    void listUserServers_UserExists_ReturnsMappedList() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(serverRepository.findAllByOwnerId(1L)).thenReturn(List.of(mockServer));

        List<ServerResponse> responses = serverService.listUserServers(1L);

        assertEquals(1, responses.size());
        assertEquals("Vanilla Survival", responses.get(0).getName());
        assertEquals("Steve", responses.get(0).getOwnerUsername());
    }

    @Test
    void listUserServers_UserDoesNotExist_ThrowsException() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> {
            serverService.listUserServers(99L);
        });
    }

    @Test
    void getServerById_ServerExists_ReturnsMappedResponse() {
        when(serverRepository.findById(100L)).thenReturn(Optional.of(mockServer));

        ServerResponse response = serverService.getServerById(100L);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals("Vanilla Survival", response.getName());
    }

    @Test
    void getServerById_ServerDoesNotExist_ThrowsException() {
        when(serverRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ServerNotFoundException.class, () -> {
            serverService.getServerById(999L);
        });
    }
}