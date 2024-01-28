package touch.baton.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.util.ObjectUtils;
import redis.embedded.RedisServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@TestConfiguration
public class EmbbedRedisConfig {

    private static final int START_PORT = 10000;
    private static final int END_PORT = 65535;

    @Value("${spring.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() throws IOException {
        final int port = findPort();
        redisServer = new RedisServer(port);
        redisServer.start();
    }

    private int findPort() throws IOException {
        if (isRedisRunning()) {
            return findAvailablePort();
        }

        return redisPort;
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    private boolean isRedisRunning() throws IOException {
        return isRunning(executeGrepProcessCommand(redisPort));
    }

    public int findAvailablePort() throws IOException {
        for (int port = START_PORT; port <= END_PORT; port++) {
            final Process process = executeGrepProcessCommand(port);
            if (!isRunning(process)) {
                return port;
            }
        }

        throw new IllegalArgumentException(String.format("사용가능한 레디스 포트가 없습니다(범위 : %d ~ %d).", START_PORT, END_PORT));
    }

    private Process executeGrepProcessCommand(int port) throws IOException {
        final String command = String.format("netstat -nat | grep LISTEN | grep %d", port);
        final String[] shell = {"/bin/sh", "-c", command};
        return Runtime.getRuntime().exec(shell);
    }

    private boolean isRunning(Process process) {
        String line;
        final StringBuilder pidInfo = new StringBuilder();

        try (final BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }

        } catch (Exception e) {
        }

        return !ObjectUtils.isEmpty(pidInfo.toString());
    }
}
