package dev.ironia.ironiacraft;

import dev.ironia.ironiacraft.model.Server;
import dev.ironia.ironiacraft.model.User;
import dev.ironia.ironiacraft.repository.ServerRepository;
import dev.ironia.ironiacraft.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class ServerRepositoryTest {

    @Autowired
    private ServerRepository serverRepository;

    private Server testServer;
}
