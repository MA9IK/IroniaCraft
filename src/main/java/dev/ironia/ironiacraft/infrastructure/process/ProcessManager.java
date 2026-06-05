package dev.ironia.ironiacraft.infrastructure.process;

import dev.ironia.ironiacraft.model.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class ProcessManager {

    private static final Logger log = LoggerFactory.getLogger(ProcessManager.class);

    private final JavaPathResolver javaPathResolver;

    private final Map<Long, Process> activeProcesses = new ConcurrentHashMap<>();

    private final Map<Long, StringBuffer> processLogs = new ConcurrentHashMap<>();

    public ProcessManager(JavaPathResolver javaPathResolver) {
        this.javaPathResolver = javaPathResolver;
    }

    public void startServer(Server server, String modpackPath) throws IOException {
        Long serverId = server.getId();

        if (isRunning(serverId)) {
            throw new IllegalStateException("Server " + serverId + " is already running");
        }

        if (modpackPath.contains("..")) {
            log.warn("SECURITY ALERT: Attempted directory traversal detected for server ID: {}. Path: {}", serverId, modpackPath);
            throw new SecurityException("Invalid modpack path. '..' is not allowed.");
        }

        File workingDir = new File(modpackPath);

        if (!workingDir.exists() || !workingDir.isDirectory()) {
            throw new FileNotFoundException("Modpack directory does not exist: " + modpackPath);
        }
        acceptEula(workingDir);

        String javaPath = javaPathResolver.getJavaExecutable(server.getJavaVersion());

        ProcessBuilder pb = new ProcessBuilder(
                javaPath,
                "-Xmx" + server.getRamMb() + "M",
                "-Xms" + server.getRamMb() + "M",
                "-jar",
                "server.jar",
                "nogui"
        );

        pb.directory(workingDir);
        pb.redirectErrorStream(true);

        log.info("Starting server {} with Java {} on port {}", serverId, server.getJavaVersion(), server.getPort());

        Process process = pb.start();
        activeProcesses.put(serverId, process);

        StringBuffer logBuffer = new StringBuffer();
        processLogs.put(serverId, logBuffer);
        startLogDrainingThread(serverId, process.getInputStream(), logBuffer);
    }

    private void startLogDrainingThread(Long serverId, InputStream inputStream, StringBuffer logBuffer) {
        Thread thread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logBuffer.append(line).append("\n");
                }
            } catch (IOException e) {
                log.debug("Log streaming stopped for server ID: {} (Process likely terminated)", serverId);
            }
        });

        thread.setName("mc-logs-" + serverId);
        thread.setDaemon(true);
        thread.start();
    }

    public void stopServer(Long serverId) {
        Process process = activeProcesses.get(serverId);

        if (process == null || !process.isAlive()) {
            activeProcesses.remove(serverId);
            log.info("Server {} is already stopped.", serverId);
            return;
        }

        try {
            log.info("Attempting graceful shutdown for server {}", serverId);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            writer.write("stop\n");
            writer.flush();

            boolean exited = process.waitFor(30, TimeUnit.SECONDS);

            if (!exited) {
                log.warn("Server {} did not stop gracefully. Forcing kill...", serverId);
                process.destroyForcibly();
            } else {
                log.info("Server {} stopped gracefully.", serverId);
            }
        } catch (IOException | InterruptedException e) {
            log.error("Error while stopping server {}. Forcing kill.", serverId, e);
            process.destroyForcibly();
            Thread.currentThread().interrupt();
        } finally {
            activeProcesses.remove(serverId);
        }
    }

    public boolean isRunning(Long serverId) {
        Process process = activeProcesses.get(serverId);
        return process != null && process.isAlive();
    }

    public String getProcessOutput(Long serverId) {
        StringBuffer logBuffer = processLogs.get(serverId);
        if (logBuffer == null) {
            return isRunning(serverId) ? "" : "Server is not running";
        }

        synchronized (logBuffer) {
            String output = logBuffer.toString();
            logBuffer.setLength(0);
            return output;
        }
    }

    private void acceptEula(File workingDir) {
        Path eulaPath = workingDir.toPath().resolve("eula.txt");

        try {
            Files.writeString(eulaPath, "eula=true\n");
            log.info("Automatically accepted Minecraft EULA in {}", workingDir.getName());
        } catch (IOException e) {
            log.warn("Failed to auto-accept EULA. The server might refuse to start.", e);
        }
    }
}
